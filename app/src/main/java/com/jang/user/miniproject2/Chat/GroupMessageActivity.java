package com.jang.user.miniproject2.Chat;

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

public class GroupMessageActivity extends AppCompatActivity {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    Map<String,User> users = new HashMap<>();
    String destinationRoom;
    String uid;
    EditText groupMessageAct_et;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    private RecyclerView recyclerView;

    int peopleCount = 0;
    List<ChatModel.Comment> comments = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message);
        destinationRoom = getIntent().getStringExtra("destinationRoom");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        groupMessageAct_et = findViewById(R.id.groupMessageAct_et);
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    users.put(item.getKey(),item.getValue(User.class));

                }
                init();
                recyclerView = findViewById(R.id.groupMessage_recycler);
                recyclerView.setAdapter(new GroupMessageRecyclerViewAdapter());
                recyclerView.setLayoutManager(new LinearLayoutManager(GroupMessageActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    void init(){
        Button group_sendBtn = (Button) findViewById(R.id.group_sendBtn);
        group_sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatModel.Comment comment = new ChatModel.Comment();
                comment.userId = uid;
                comment.message = groupMessageAct_et.getText().toString();
                comment.timestamp = ServerValue.TIMESTAMP;
                FirebaseDatabase.getInstance().getReference().child("chatrooms").child(destinationRoom).child("comments").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        groupMessageAct_et.setText("");
                        FirebaseDatabase.getInstance().getReference().child("chatrooms").child(destinationRoom).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<String,Boolean> map = (Map<String, Boolean>) dataSnapshot.getValue();

                                for (String item : map.keySet()){
                                    if (item.equals(uid)){
                                        continue;
                                    }
                                    sendgcm(users.get(item).pushToken);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        });
    }
    void sendgcm(String pushToken){
        Gson gson = new Gson();
        NotificationModel notificationModel = new NotificationModel();
//        notificationModel.to = destinationUserModel.getPushToken();
        notificationModel.to = "fI1BxQXC4d0:APA91bE2hm1CApYOng6zzqvFDT6JBdyWQI03ZIoimlshyYTGbMO9nYPXZi5eiQ8vxxILGU9fOVOQUNYeK4luHVNn2CtqxTzV-LllfvmMDuS4bxj47NFK6-wxWHZ0VglpEoSmuUOlFh9n";
        notificationModel.notification.title = "보낸 아이디";
        notificationModel.notification.text = groupMessageAct_et.getText().toString();

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

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }
    class GroupMessageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public GroupMessageRecyclerViewAdapter(){
            getMessageList();
        }
        void getMessageList() {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("chatrooms").child(destinationRoom).child("comments");
            valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    comments.clear();
                    Map<String, Object> readUsersMap = new HashMap<>();
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        String key = item.getKey();
                        ChatModel.Comment comment_origin = item.getValue(ChatModel.Comment.class);
                        ChatModel.Comment comment_motify = item.getValue(ChatModel.Comment.class);
                        comment_motify.readUsers.put(uid, true);

                        readUsersMap.put(key, comment_motify);
                        comments.add(comment_origin);
                        if (!comments.get(comments.size() - 1).readUsers.containsKey(uid)) {

                            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(destinationRoom).child("comments")
                                    .updateChildren(readUsersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d("로그", "개수 : " + comments.size());
                                    notifyDataSetChanged();
                                    recyclerView.scrollToPosition(comments.size() - 1);
                                }
                            });
                        } else {
                            notifyDataSetChanged();
                            recyclerView.scrollToPosition(comments.size() - 1);
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
            return new GroupMessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            GroupMessageViewHolder messageViewHolder = ((GroupMessageViewHolder)holder);

            //내가 보낸 메시지
            if (comments.get(position).userId.equals(uid)){
                messageViewHolder.txt_message.setText(comments.get(position).message);
                messageViewHolder.txt_message.setBackgroundResource(R.drawable.rightbubble);
                messageViewHolder.linearLayout_destination.setVisibility(View.INVISIBLE);
                messageViewHolder.txt_message.setTextSize(25);
                messageViewHolder.item_message_layoutMain.setGravity(Gravity.RIGHT);
                setReadCounter(position,messageViewHolder.readCounter_left);

            }else {
                Glide.with(holder.itemView.getContext())
                        .load(users.get(comments.get(position).userId).getUser_uri())
                        .apply(new RequestOptions().circleCrop())
                        .into(messageViewHolder.img_profile);



//상대방이 보낸 메시지
                messageViewHolder.txt_name.setText(users.get(comments.get(position).userId).getUser_name());
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
                FirebaseDatabase.getInstance().getReference().child("chatrooms").child(destinationRoom).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
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

        private class GroupMessageViewHolder extends RecyclerView.ViewHolder {
            public TextView txt_message;
            public TextView txt_name;
            public ImageView img_profile;
            public LinearLayout linearLayout_destination;
            public LinearLayout item_message_layoutMain;
            public TextView textview_timestamp;
            public TextView readCounter_left;
            public TextView readCounter_right;
            public GroupMessageViewHolder(View view) {
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
}
