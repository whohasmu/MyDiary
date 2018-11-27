package com.jang.user.miniproject2;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jang.user.miniproject2.Object.LoginUser;
import com.jang.user.miniproject2.Object.Post;



public class DetailActivity extends AppCompatActivity {



    ImageView mCardImage;
    String mPostId;
    Post mPost;

    ImageView Image_Profile;
    TextView Text_Writer;
    TextView Text_Title;
    TextView Text_Content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Text_Title = findViewById(R.id.Text_Title);
        mCardImage = findViewById(R.id.cardImage);

        mPost = (Post) getIntent().getSerializableExtra("post");
        mPostId = getIntent().getStringExtra("postId");

        Image_Profile = findViewById(R.id.Image_Profile);
        Text_Writer = findViewById(R.id.Text_Writer);

        Text_Content = findViewById(R.id.Text_Content);



        Glide.with(this)
                .load(mPost.getImageUrl())
                .into(mCardImage);
        Text_Title.setText(mPost.getTitle());
        Text_Content.setText(mPost.getContent());

        final Activity activity = this;





        FirebaseDatabase.getInstance().getReference().child("users").child(mPost.getWriterUID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                LoginUser user  = dataSnapshot.getValue(LoginUser.class);

                Glide.with(activity)
                        .load(user.getUser_uri())
                        .apply(new RequestOptions().circleCrop())
                        .into(Image_Profile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Text_Writer.setText(mPost.getWriterName());


        FirebaseDatabase.getInstance().getReference().child("users").child(mPost.getWriterUID()).child("user_name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Log.d("로그","로그 : " + dataSnapshot.getValue(String.class))
                ;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        /*mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(new MyCommentAdapter(mPost.getCommentMap()));*/

    }
    /*private class MyCommentAdapter extends RecyclerView.Adapter<MyCommentViewHolder> {
        List<Comment> comments = new ArrayList<>();
        String[] keys;

        MyCommentAdapter(Map<String, Comment> commentMap) {
            List<Comment> list = new ArrayList<>(commentMap.values());
            Collections.sort(list, new Comparator<Comment>() {
                public int compare(Comment o1, Comment o2) {
                    Collator collator = Collator.getInstance();
                    String strLhs = String.format("%030d", o1.getWriteTime());
                    String strRhs = String.format("%030d", o2.getWriteTime());
                    return collator.compare(strRhs, strLhs);
                }
            });
            comments.addAll(list);
        }

        @Override
        public MyCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(R.layout.card_comment, null);
            MyCommentViewHolder viewHolder = new MyCommentViewHolder(itemView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyCommentViewHolder holder, int position) {
            Comment comment = comments.get(position);
            Glide.with(DetailActivity.this).load(comment.getBgUrl()).centerCrop().into(holder.background);
            holder.commentText.setText(comment.getText());
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }
    }

    class MyCommentViewHolder extends RecyclerView.ViewHolder {
        ImageView background;
        TextView commentText;

        public MyCommentViewHolder(View itemView) {
            super(itemView);
            background = (ImageView) itemView.findViewById(R.id.background);
            commentText = (TextView) itemView.findViewById(R.id.commentText);
        }
    }*/
}
