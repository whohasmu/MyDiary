package com.jang.user.miniproject2.Fragment.Friend;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jang.user.miniproject2.Chat.MessageActivity;
import com.jang.user.miniproject2.Object.ChatModel;
import com.jang.user.miniproject2.Object.User;
import com.jang.user.miniproject2.R;

import java.util.ArrayList;
import java.util.List;

public class SelectPeopleActivity extends AppCompatActivity {

    ChatModel chatModel = new ChatModel();
    Button selectPeople_chatBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_people);

        RecyclerView recyclerView = findViewById(R.id.peopleSelect_recycler);
        recyclerView.setAdapter(new PeopleSelectREcycleViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        selectPeople_chatBtn = findViewById(R.id.selectPeople_chatBtn);
        selectPeople_chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(SelectPeopleActivity.this,GroupMessageActivity.class);
//                startActivity(intent);
                String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                chatModel.users.put(myUid,true);

                FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel);

            }
        });
    }

    class PeopleSelectREcycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        List<User> users;
        public PeopleSelectREcycleViewAdapter() {
            users = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    users = new ArrayList<>();
                    final String myUid =FirebaseAuth.getInstance().getCurrentUser().getUid();
                    users.clear();

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                        User user  = snapshot.getValue(User.class);

                        if (user.getUid().equals(myUid)){
                            continue;
                        }
                        users.add(user);
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

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_people_select,parent,false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

//            Glide.with(holder.itemView.getContext())
//                    .load(user.getPhotoUrl())
//                    .apply(new RequestOptions().circleCrop())
//                    .into(((CustomViewHolder)holder).imageView);

//            ((CustomViewHolder)holder).textView.setText(user.getDisplayName());


            Glide.with
                    (holder.itemView.getContext())
                    .load(users.get(position).getUser_uri())
                    .apply(new RequestOptions().centerCrop())
                    .into(((CustomViewHolder)holder).imageView);

            ((CustomViewHolder)holder).textView.setText(users.get(position).getUser_name());

            ((CustomViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MessageActivity.class);
                    intent.putExtra("destinationUid",users.get(position).getUid());
                    ActivityOptions activityOptions = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                        activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(),R.anim.from_right,R.anim.to_left);
                        startActivity(intent,activityOptions.toBundle());
                    }

                }
            });
            if (users.get(position).getStatusMessage() != null) {
                ((CustomViewHolder) holder).commentText.setText(users.get(position).getStatusMessage());
            }
            ((CustomViewHolder) holder).select_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    //체크가 되어있는 상태
                    if (b){
                        chatModel.users.put(users.get(position).getUid(),true);

                    }else{
                        chatModel.users.remove(users.get(position));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            public ImageView imageView;
            public TextView textView;
            public TextView commentText;
            public CheckBox select_check;


            public CustomViewHolder(View view) {
                super(view);
                imageView= view.findViewById(R.id.people_img);
                textView = view.findViewById(R.id.people_txtid);
                commentText = view.findViewById(R.id.friendItem_comment);
                select_check = view.findViewById(R.id.select_check);
            }
        }
    }
}
