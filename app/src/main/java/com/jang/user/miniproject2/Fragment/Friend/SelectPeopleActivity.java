package com.jang.user.miniproject2.Fragment.Friend;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.google.firebase.database.ChildEventListener;
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

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_people);



        RecyclerView List_Friends = findViewById(R.id.List_Friends);

        mAuth = FirebaseAuth.getInstance();

        List_Friends.setLayoutManager(new LinearLayoutManager(this));
        List_Friends.setAdapter(new PeopleFragmentREcycleViewAdapter());
    }

    class PeopleFragmentREcycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


        ArrayList<User> Friends = new ArrayList<>();
        public PeopleFragmentREcycleViewAdapter() {

            FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("friendUID").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Friends = new ArrayList<>();
                    Friends.clear();



                    String FriendUID  = dataSnapshot.getValue(String.class);
                    Log.d("로그","친구 UID :  "+ FriendUID);

                    FirebaseDatabase.getInstance().getReference().child("users").child(FriendUID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User temp = new User();
                            temp = dataSnapshot.getValue(User.class);
                            Log.d("로그","친구추가 : " + temp.getUid() + temp.getUser_uri() + temp.getGoogle_name());
                            Friends.add(dataSnapshot.getValue(User.class));
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_people,parent,false);



            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            CustomViewHolder customViewHolder = (CustomViewHolder)holder;

//            Glide.with(holder.itemView.getContext())
//                    .load(user.getPhotoUrl())
//                    .apply(new RequestOptions().circleCrop())
//                    .into(((CustomViewHolder)holder).imageView);

//            ((CustomViewHolder)holder).textView.setText(user.getDisplayName());


            Glide.with
                    (holder.itemView.getContext())
                    .load(Friends.get(position).getUser_uri())
                    .apply(new RequestOptions().circleCrop())
                    .into(customViewHolder.imageView);

            customViewHolder.textView.setText(Friends.get(position).getUser_name());


            customViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MessageActivity.class);
                    Log.d("로그","보내는 UID : " + Friends.get(position).getUid());
                    intent.putExtra("destinationUid",Friends.get(position).getUid().toString());

                    ActivityOptions activityOptions = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                        activityOptions = ActivityOptions.makeCustomAnimation(v.getContext(),R.anim.from_right,R.anim.to_left);
                        startActivity(intent,activityOptions.toBundle());
                    }
                }


            });



            if (Friends.get(position).getStatusMessage() != null) {
                customViewHolder.commentText.setText(Friends.get(position).getStatusMessage());
            }else{
                customViewHolder.commentText.setText("\uD83D\uDE42");
            }
        }

        @Override
        public int getItemCount() {
            return Friends.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            public View mView;
            public ImageView imageView;
            public TextView textView;
            public TextView commentText;


            public CustomViewHolder(View view) {

                super(view);
                mView = view;
                imageView= view.findViewById(R.id.people_img);
                textView = view.findViewById(R.id.people_txtid);
                commentText = view.findViewById(R.id.friendItem_comment);
            }
        }
    }
}
