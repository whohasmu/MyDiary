package com.jang.user.miniproject2.Chat;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.jang.user.miniproject2.Object.ChatModel;
import com.jang.user.miniproject2.Object.User;
import com.jang.user.miniproject2.Object.NotificationModel;
import com.jang.user.miniproject2.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessageActivity extends AppCompatActivity {


    private String destinationUid;
    Button btn_send;
    EditText et_chat;

    private String userId;
    private String chatRoomUid;
    private RecyclerView message_recycler;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    private User loginUser;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    int peopleCount =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        message_recycler = findViewById(R.id.message_recycler);

        btn_send = findViewById(R.id.btn_send);
        et_chat = findViewById(R.id.et_chat);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        destinationUid = getIntent().getStringExtra("destinationUid");

        Log.d("로그","메세지엑티비티");
        Log.d("로그","myUID : " + userId);
        Log.d("로그","DestinationUID : " + destinationUid );



        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatModel chatModel = new ChatModel();
                chatModel.users.put(userId,true);
                chatModel.users.put(destinationUid,true);

                if (chatRoomUid == null){
                    btn_send.setEnabled(false);
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            checkChetRoom();
                        }
                    });
                }else {
                    ChatModel.Comment comment = new ChatModel.Comment();
                    comment.userId = userId;
                    comment.message = et_chat.getText().toString();
                    comment.timestamp = ServerValue.TIMESTAMP;

                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            sendgcm();
                            et_chat.setText("");

                        }
                    });

                }

            }
        });
        checkChetRoom();
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(et_chat.getWindowToken(),0);
    }
    void sendgcm(){
        Gson gson = new Gson();
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.to = loginUser.getPushToken();
//        notificationModel.to = "ckBKLacifPI:APA91bHTTjo8bKaTcOavZuT-sxPB-W5iYL7SNdzk_2IMygBT8fc3G3Mp-p1TkpeptV5Bozy7ajVUzaw3x--hZgtygHfdlqthAROuZRNqpsk3LEgzMM1i7h4Y33coajpiRn7k8BnW8X-N";
        notificationModel.notification.title = "보낸이";
        notificationModel.notification.text = et_chat.getText().toString();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"),gson.toJson(notificationModel));

        Request request = new Request.Builder()
                .header("Content-Type","application/json")
                .addHeader("Authorization","key=AIzaSyBr7Q-RoW5A-fScO5Orpx9m_Fe7WPyJiRc")
                .url("https://gcm-http.googleapis.com/gcm/send")
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("로그","sendgcm 실패");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("로그","sendgcm 성공");
            }
        });
    }

    void checkChetRoom(){
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+userId).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot item : dataSnapshot.getChildren()){
                    ChatModel chatModel = item.getValue(ChatModel.class);
                    if (chatModel.users.containsKey(destinationUid) && chatModel.users.size() == 2){
                        chatRoomUid = item.getKey();
                        btn_send.setEnabled(true);
                        et_chat.setText("");
                        message_recycler.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                        message_recycler.setAdapter(new RecyclerViewAdapter());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        List<ChatModel.Comment> comments;


        public RecyclerViewAdapter() {
            comments = new ArrayList<>();


            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    loginUser = dataSnapshot.getValue(User.class);
                    getMessageList();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        void getMessageList(){
            databaseReference = FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments");
            valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    comments.clear();
                    Map<String,Object> readUsersMap = new HashMap<>();
                    for (DataSnapshot item : dataSnapshot.getChildren()){
                        String key = item.getKey();
                        ChatModel.Comment comment_origin =item.getValue(ChatModel.Comment.class);
                        ChatModel.Comment comment_motify =item.getValue(ChatModel.Comment.class);
                        comment_motify.readUsers.put(userId,true);

                        readUsersMap.put(key,comment_motify);
                        comments.add(comment_origin);

                        if (!comments.get(comments.size()-1).readUsers.containsKey(userId)){

                            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments")
                                    .updateChildren(readUsersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d("로그","개수 : "+ comments.size());
                                    notifyDataSetChanged();
                                    message_recycler.scrollToPosition(comments.size()-1);
                                }
                            });
                        }else{
                            notifyDataSetChanged();
                            message_recycler.scrollToPosition(comments.size()-1);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            MessageViewHolder messageViewHolder = ((MessageViewHolder)holder);

            //내가 보낸 메시지
            if (comments.get(position).userId.equals(userId)){
                messageViewHolder.txt_message.setText(comments.get(position).message);
                messageViewHolder.txt_message.setBackgroundResource(R.drawable.rightbubble);
                messageViewHolder.linearLayout_destination.setVisibility(View.INVISIBLE);
                messageViewHolder.txt_message.setTextSize(25);
                messageViewHolder.item_message_layoutMain.setGravity(Gravity.RIGHT);
                setReadCounter(position,messageViewHolder.readCounter_left);

            }else {
                Glide.with(holder.itemView.getContext())
                        .load(loginUser.getUser_uri())
                        .apply(new RequestOptions().circleCrop())
                        .into(messageViewHolder.img_profile);



//상대방이 보낸 메시지
                messageViewHolder.txt_name.setText(loginUser.getUser_name());
                messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE);
                messageViewHolder.txt_message.setBackgroundResource(R.drawable.leftbubble);
                messageViewHolder.txt_message.setText(comments.get(position).message);
                messageViewHolder.txt_message.setTextSize(25);
                messageViewHolder.item_message_layoutMain.setGravity(Gravity.LEFT);
                setReadCounter(position,messageViewHolder.readCounter_right);
            }

                long unixTime = (long) comments.get(position).timestamp;
                Date date = new Date(unixTime);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                String time = simpleDateFormat.format(date);
                messageViewHolder.textview_timestamp.setText(time);

        }
        void setReadCounter(final int position, final TextView textView){
            if (peopleCount==0) {
                FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Boolean> users = (Map<String, Boolean>) dataSnapshot.getValue();
                        peopleCount = users.size();
                        int count = peopleCount - comments.get(position).readUsers.size();
                        if (count > 0) {
                            textView.setVisibility(View.VISIBLE);
                            textView.setText(String.valueOf(count));
                        } else {
                            textView.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }else{
                int count = peopleCount - comments.get(position).readUsers.size();
                if (count > 0) {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(String.valueOf(count));
                } else {
                    textView.setVisibility(View.INVISIBLE);
                }
            }
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        private class MessageViewHolder extends RecyclerView.ViewHolder {
            public TextView txt_message;
            public TextView txt_name;
            public ImageView img_profile;
            public LinearLayout linearLayout_destination;
            public LinearLayout item_message_layoutMain;
            public TextView textview_timestamp;
            public TextView readCounter_left;
            public TextView readCounter_right;



            public MessageViewHolder(View view) {
                super(view);
                txt_message = view.findViewById(R.id.messageItem_textview_message);
                txt_name = view.findViewById(R.id.item_messageTxtName);
                img_profile = view.findViewById(R.id.item_message_profile);
                linearLayout_destination = view.findViewById(R.id.item_message_layoutDestination);
                item_message_layoutMain = view.findViewById(R.id.item_message_layoutMain);
                textview_timestamp = view.findViewById(R.id.messageItem_textview_timetamp);
                readCounter_left = view.findViewById(R.id.readCounter_left);
                readCounter_right = view.findViewById(R.id.readCounter_right);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener);
        }

        overridePendingTransition(R.anim.from_left,R.anim.to_right);
        finish();
    }
}
