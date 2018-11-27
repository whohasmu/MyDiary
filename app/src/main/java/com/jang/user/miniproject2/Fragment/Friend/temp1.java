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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.jang.user.miniproject2.Object.LoginUser;
import com.jang.user.miniproject2.R;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

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
    private ArrayList<LoginUser> mUser = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people,container,false);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.fragment_recycle);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int position = FragmentPagerItem.getPosition(getArguments());


    }

    class PeopleFragmentREcycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        List<LoginUser> users;
        public PeopleFragmentREcycleViewAdapter() {
            users = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    users = new ArrayList<>();
                    final String myUid =FirebaseAuth.getInstance().getCurrentUser().getUid();
                    users.clear();

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                        LoginUser user  = snapshot.getValue(LoginUser.class);

                        if (user.getUserId().equals(myUid)){
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
                    .load(users.get(position).getUser_uri())
                    .apply(new RequestOptions().centerCrop())
                    .into(((CustomViewHolder)holder).imageView);

            ((CustomViewHolder)holder).textView.setText(users.get(position).getUser_name());

            ((CustomViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MessageActivity.class);
                    intent.putExtra("destinationUid",users.get(position).getUserId());
                    ActivityOptions activityOptions = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                        activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(),R.anim.from_right,R.anim.to_left);
                        startActivity(intent,activityOptions.toBundle());
                    }

                }
            });
            if (users.get(position).comment != null) {
                ((CustomViewHolder) holder).commentText.setText(users.get(position).comment);
            }
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            public ImageView imageView;
            public TextView textView;
            public TextView commentText;


            public CustomViewHolder(View view) {
                super(view);
                imageView= view.findViewById(R.id.people_img);
                textView = view.findViewById(R.id.people_txtid);
                commentText = view.findViewById(R.id.friendItem_comment);
            }
        }
    }
}
