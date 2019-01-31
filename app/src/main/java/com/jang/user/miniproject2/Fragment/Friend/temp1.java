package com.jang.user.miniproject2.Fragment.Friend;


import android.app.ActivityOptions;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jang.user.miniproject2.Chat.MessageActivity;
import com.jang.user.miniproject2.Object.User;
import com.jang.user.miniproject2.R;

import java.util.ArrayList;
import java.util.List;


public class temp1 extends Fragment {

    private static temp1 curr=null;

    public static temp1 getInstance() {
        if (curr == null) {
            curr = new temp1();
        }

        return curr;
    }

    FloatingActionButton floatingActionButton;
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people,container,false);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.fragment_recycle);

        mAuth = FirebaseAuth.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new PeopleFragmentREcycleViewAdapter());

        floatingActionButton = view.findViewById(R.id.peopleFragment_floatingBtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),SelectPeopleActivity.class));
            }
        });








        return view;
    }

    /*@Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int position = FragmentPagerItem.getPosition(getArguments());


    }*/

    class PeopleFragmentREcycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


        List<User> Friends;
        public PeopleFragmentREcycleViewAdapter() {


            Friends = new ArrayList<>();


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
                            Log.d("로그","친구추가 : " + dataSnapshot.getValue(User.class).getUser_name());
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

//            Glide.with(holder.itemView.getContext())
//                    .load(user.getPhotoUrl())
//                    .apply(new RequestOptions().circleCrop())
//                    .into(((CustomViewHolder)holder).imageView);

//            ((CustomViewHolder)holder).textView.setText(user.getDisplayName());


            Glide.with
                    (holder.itemView.getContext())
                    .load(Friends.get(position).getUser_uri())
                    .apply(new RequestOptions().circleCrop())
                    .into(((CustomViewHolder)holder).imageView);

            ((CustomViewHolder)holder).textView.setText(Friends.get(position).getUser_name());


            ((CustomViewHolder) holder).mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MessageActivity.class);
                    intent.putExtra("destinationUid",Friends.get(position).getUid());
                    ActivityOptions activityOptions = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                        activityOptions = ActivityOptions.makeCustomAnimation(v.getContext(),R.anim.from_right,R.anim.to_left);
                        startActivity(intent,activityOptions.toBundle());
                    }
                }
            });



            if (Friends.get(position).getStatusMessage() != null) {
                ((CustomViewHolder) holder).commentText.setText(Friends.get(position).getStatusMessage());
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

    @Override
    public void onResume() {
        Log.d("로그","resume");
        super.onResume();

    }

    @Override
    public void onPause() {
        Log.d("로그","pause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d("로그","destroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.d("로그","destroyView");
        super.onDestroyView();
    }
}
