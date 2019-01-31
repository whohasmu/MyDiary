package com.jang.user.miniproject2.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.jang.user.miniproject2.DetailActivity;
import com.jang.user.miniproject2.MainActivity;
import com.jang.user.miniproject2.Object.Post;
import com.jang.user.miniproject2.R;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by CYH on 2018-11-03.
 */

public class Frag_Home extends Fragment {



    public static final  String FIREBASE_POST_URL = "https://miniproject2-4e7d3.firebaseio.com/Posts";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private MyAdapter mAdapter;
    private List<Post> mPosts = new ArrayList<>();
    private List<String> mKeys = new ArrayList<>();
    private Query mRef;
    Spinner spinners;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home,container,false);

        spinners = view.findViewById(R.id.spinners);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(inflater.getContext(), R.array.location,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinners.setAdapter(adapter);

        //미구현
        spinners.setVisibility(View.GONE);







        Firebase.setAndroidContext(inflater.getContext());

        JodaTimeAndroid.init(inflater.getContext());

        mRecyclerView = view.findViewById(R.id.List_Post);
        gridLayoutManager = new GridLayoutManager(inflater.getContext(),2,GridLayoutManager.VERTICAL,true);

        /*gridLayoutManager.setReverseLayout(true);*/
        /*gridLayoutManager.setStackFromEnd(true);*/
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);









        mRef = new Firebase(FIREBASE_POST_URL).orderByChild("writeTime");
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post value = dataSnapshot.getValue(Post.class);
                String key = dataSnapshot.getKey();

                if(s == null){
                    mPosts.add(0,value);
                    mKeys.add(0,key);


                }else{
                    int previousIndex = mKeys.indexOf(s);
                    int nextIndex = previousIndex + 1;
                    if(nextIndex == mPosts.size()){
                        mPosts.add(value);
                        mKeys.add(key);

                    }else{
                        mPosts.add(nextIndex,value);
                        mKeys.add(nextIndex,key);
                    }
                }
                /*mAdapter.notifyDataSetChanged();*/
                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                //목록을 최신부분으로 이동
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                Post value = dataSnapshot.getValue(Post.class);
                int index = mKeys.indexOf(key);
                mPosts.set(index, value);



                /*mAdapter.notifyDataSetChanged();*/

                /*mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);*/
                //목록을 최신부분으로 이동

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int index = mKeys.indexOf(key);

                mKeys.remove(index);
                mPosts.remove(index);


                mAdapter.notifyDataSetChanged();

                /*mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);*/
                //목록을 최신부분으로 이동
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                /*String key = dataSnapshot.getKey();
                Post newModel = dataSnapshot.getValue(Post.class);
                int index = mKeys.indexOf(key);
                mPosts.remove(index);
                mKeys.remove(index);
                if (s == null) {
                    mPosts.add(0, newModel);
                    mKeys.add(0, key);
                } else {
                    int previousIndex = mKeys.indexOf(s);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == mPosts.size()) {
                        mPosts.add(newModel);
                        mKeys.add(key);
                    } else {
                        mPosts.add(nextIndex, newModel);
                        mKeys.add(nextIndex, key);
                    }
                }*/

                /*mAdapter.notifyDataSetChanged();*/
                /*mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);*/
                //목록을 최신부분으로 이동
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                firebaseError.toException().printStackTrace();
            }
        });



        return view;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView background;
        TextView timeText;
        TextView commentCount;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
            background = (ImageView) itemView.findViewById(R.id.background);
            timeText = (TextView) itemView.findViewById(R.id.timeText);
            commentCount = (TextView) itemView.findViewById(R.id.commentCount);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            /*View itemView = getLayoutInflater().inflate(R.layout.card_post, null);*/
            View itemView = parent.inflate(getContext(),R.layout.card_post,null);
            MyViewHolder myViewHolder = new MyViewHolder(itemView);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Post post = mPosts.get(position);
            if(!post.getTitle().equals("")) {
                holder.text.setText(" " + post.getTitle() + " ");
            }else{
                holder.text.setText(post.getTitle());
            }
            holder.commentCount.setText("" + post.getCommentMap().size());
            holder.timeText.setText(getDiffTimeText(post.getWriteTime()));
            /*Glide.with(MainActivity.this).load(post.getImageUrl()).centerCrop().into(holder.background);*/
            Glide.with(getContext())
                    .load(post.getImageUrl())
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.background);


            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    intent.putExtra("postId", mKeys.get(position));
                    intent.putExtra("post", post);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mPosts.size();
        }
    }

    String getDiffTimeText(long targetTime) {
        DateTime curDateTime = new DateTime();
        DateTime targetDateTime = new DateTime().withMillis(targetTime);

        int diffDay = Days.daysBetween(curDateTime, targetDateTime).getDays();
        int diffHours = Hours.hoursBetween(targetDateTime, curDateTime).getHours();
        int diffMinutes = Minutes.minutesBetween(targetDateTime, curDateTime).getMinutes();
        if (diffDay == 0) {
            if(diffHours == 0 && diffMinutes == 0){
                return "방금전";
            }
            if(diffHours > 0){
                return "" + diffHours + "시간 전";
            }
            return "" + diffMinutes + "분 전";

        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return format.format(new Date(targetTime));
        }


    }


}


