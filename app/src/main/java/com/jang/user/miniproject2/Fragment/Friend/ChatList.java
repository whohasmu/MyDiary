package com.jang.user.miniproject2.Fragment.Friend;


import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jang.user.miniproject2.Chat.MessageActivity;
import com.jang.user.miniproject2.Object.ChatModel;
import com.jang.user.miniproject2.Object.User;
import com.jang.user.miniproject2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

public class ChatList extends Fragment {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm");

    RecyclerView recyclerView;
    ChatRecyclerViewAdapter chatRecyclerViewAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_chat,container,false);

        recyclerView = view.findViewById(R.id.List_chat);
        chatRecyclerViewAdapter = new ChatRecyclerViewAdapter();
        recyclerView.setAdapter(chatRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));



        return view;



    }

    class ChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private String userUid;
        private List<ChatModel> chatModels = new ArrayList<>();
        private List<String> keys = new ArrayList<>();
        private ArrayList<String> destinationUsers = new ArrayList<>();

        public ChatRecyclerViewAdapter() {
            userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+userUid).equalTo(true).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    chatModels.clear();
                    keys.clear();
                    for(DataSnapshot item : dataSnapshot.getChildren()){
                        chatModels.add(item.getValue(ChatModel.class));
                        keys.add(item.getKey());

                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false);

            return new CustomViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

            final CustomViewHolder customViewHolder = (CustomViewHolder)holder;
            String destinationUid = null;
            // 일일 채팅방에 있는 유저 체크!!!
            for (String user: chatModels.get(position).users.keySet()){
                if (!user.equals(userUid)){
                    destinationUid = user;
                    destinationUsers.add(destinationUid);
                }

            }

            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User loginUser = dataSnapshot.getValue(User.class);//상대방 유저 정보

                    Glide.with(customViewHolder.itemView.getContext())
                            .load(loginUser.getUser_uri())
                            .apply(new RequestOptions().centerCrop())
                            .into(customViewHolder.profile);

                    customViewHolder.message_title.setText(loginUser.getUser_name());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            //메시지를 내림차순으로 정렬

            Map<String,ChatModel.Comment>commentMap = new TreeMap<>(Collections.reverseOrder());

            commentMap.putAll(chatModels.get(position).comments);
            if (commentMap.keySet().toArray().length > 0) {
                String lastMessagekey = (String) commentMap.keySet().toArray()[0];
                customViewHolder.lastMessage.setText(chatModels.get(position).comments.get(lastMessagekey).message);


                //마지막 시간
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                long unixTime = (long) chatModels.get(position).comments.get(lastMessagekey).timestamp;
                Date date = new Date(unixTime);
                customViewHolder.messageTimestamp.setText(simpleDateFormat.format(date));

            }





            customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    /*if (chatModels.get(position).users.size() > 2){
                        intent = new Intent(view.getContext(), GroupMessageActivity.class);
                        intent.putExtra("destinationRoom",keys.get(position));
                    }else {
                        intent = new Intent(view.getContext(), MessageActivity.class);
                        intent.putExtra("destinationUid", destinationUsers.get(position));
                    }*/

                    intent = new Intent(view.getContext(), MessageActivity.class);
                    intent.putExtra("destinationUid", destinationUsers.get(position));

                    ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.from_right, R.anim.to_left);

                    startActivity(intent, activityOptions.toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return chatModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView profile;
            public TextView message_title;
            public TextView lastMessage;
            public TextView messageTimestamp;

            public CustomViewHolder(View view) {
                super(view);
                profile = view.findViewById(R.id.Image_Profile);
                message_title = view.findViewById(R.id.Text_Title);
                lastMessage = view.findViewById(R.id.chatItem_lastmessage);
                messageTimestamp = view.findViewById(R.id.chatItem_timestamp);
            }
        }
    }


}
