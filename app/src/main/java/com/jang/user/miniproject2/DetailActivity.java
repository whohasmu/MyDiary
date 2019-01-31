package com.jang.user.miniproject2;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jang.user.miniproject2.Object.User;
import com.jang.user.miniproject2.Object.Post;



public class DetailActivity extends AppCompatActivity {



    ImageView mCardImage;
    String mPostId;
    Post mPost;

    ImageView Image_Profile;
    TextView Text_Writer;
    TextView Text_Title;
    TextView Text_Content;
    Button Button_Delete;
    Button Button_AddFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Text_Title = findViewById(R.id.Text_Title);
        mCardImage = findViewById(R.id.cardImage);

        mPost = (Post) getIntent().getSerializableExtra("post");
        mPostId = getIntent().getStringExtra("postId");
        Log.d("로그"," "+ mPostId);
        Log.d("로그","WriterUID : " + mPost.getWriterUID());
        Log.d("로그","Title : " + mPost.getTitle());
        Log.d("로그","Content : " + mPost.getContent());
        Log.d("로그","ImgUri  : " + mPost.getImageUrl());




        Image_Profile = findViewById(R.id.Image_Profile);
        Text_Writer = findViewById(R.id.Text_Writer);

        Text_Content = findViewById(R.id.Text_Content);
        Button_Delete = findViewById(R.id.Button_Delete);
        Button_AddFriend = findViewById(R.id.Button_AddFriend);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(mPost.getWriterUID().equals(user.getUid())){
            Button_Delete.setVisibility(View.VISIBLE);
        }else{
            Button_AddFriend.setVisibility(View.VISIBLE);
        }

        Button_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alt_bld = new AlertDialog.Builder(v.getContext());

                alt_bld.setMessage("일기를 지우시겠어요?").setCancelable(false)
                        .setNegativeButton("아니오",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                        }).setPositiveButton("네",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        new Firebase("https://miniproject2-4e7d3.firebaseio.com/DeletedPosts").push().setValue(mPost);
                                        new Firebase("https://miniproject2-4e7d3.firebaseio.com/Posts/"+mPostId).removeValue();
                                        finish();


                                    }
                        });
                AlertDialog alert = alt_bld.create();

                // 대화창 클릭시 뒷 배경 어두워지는 것 막기
                //alert.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                // 대화창 제목 설정

                /*alert.setTitle("로그아웃");*/

                // 대화창 아이콘 설정
//                alert.setIcon(R.drawable.check_dialog_64);

                // 대화창 배경 색 설정

                /*alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(255,62,79,92)));*/

                alert.show();
            }
        });

        Button_AddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(v.getContext());

                alt_bld.setMessage("친구로 등록할래요?").setCancelable(false)
                        .setNegativeButton("아니오",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).setPositiveButton("네",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("friendUID").child(mPost.getWriterUID()).setValue(mPost.getWriterUID());
                        finish();



                    }
                });
                AlertDialog alert = alt_bld.create();


                alert.show();
            }
        });

        Glide.with(DetailActivity.this)
                .load(mPost.getImageUrl())
                .apply(new RequestOptions().fitCenter())
                .into(mCardImage);
        Text_Title.setText(mPost.getTitle());
        Text_Content.setText(mPost.getContent());

        final Activity activity = this;






        FirebaseDatabase.getInstance().getReference().child("users").child(mPost.getWriterUID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user  = dataSnapshot.getValue(User.class);


                if(user!=null){
                    Log.d("로그", "프로필정보 가져오기 성공." + user.getUser_uri());
                    Glide.with(DetailActivity.this)
                            .load(user.getUser_uri())
                            .apply(new RequestOptions().circleCrop())
                            .into(Image_Profile);
                }
                //게시글 클릭시 튕기는이유 : 게시자에 대한 UID가 DB상에서 삭제되었기 때문.

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d("로그", "프로필정보 가져오기 실패." +  databaseError.toException());

            }
        });






        FirebaseDatabase.getInstance().getReference().child("users").child(mPost.getWriterUID()).child("user_name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Log.d("로그","user_name : " + dataSnapshot.getValue(String.class));
                Text_Writer.setText(dataSnapshot.getValue(String.class));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("로그","로그 : " + databaseError.toString());
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
