package com.jang.user.miniproject2.Fragment;

import android.animation.Animator;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jang.user.miniproject2.Object.Post;
import com.jang.user.miniproject2.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by CYH on 2018-11-03.
 */

public class Frag_Write extends Fragment {



    public static final  String FIREBASE_POST_URL = "https://miniproject2-4e7d3.firebaseio.com/Posts";

    ImageView Image_GetImage;
    Button Button_LoadImage;
    Spinner write_spinner;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Button Button_Write;
    EditText Edit_Title;
    EditText Edit_Content;

    String ImageUri;


    Uri downloadUri;
    ProgressDialog asyncDialog;
    LottieAnimationView Lottie_Write;



    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_write,container,false);

        Firebase.setAndroidContext(inflater.getContext());


        if (ContextCompat.checkSelfPermission(inflater.getContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},2);
            }
        }





        Image_GetImage = view.findViewById(R.id.Image_GetImage);
        Button_LoadImage = view.findViewById(R.id.Button_LoadImage);
        write_spinner = view.findViewById(R.id.write_spinner);
        Button_Write = view.findViewById(R.id.btn_write);
        Edit_Title = view.findViewById(R.id.et_title);
        Edit_Content = view.findViewById(R.id.et_content);

        Lottie_Write = view.findViewById(R.id.Lottie_Write);


        /*
        * 미구현
        * */
        write_spinner.setVisibility(GONE);


        Button_LoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });

        Image_GetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }

        });



        Button_Write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                asyncDialog = new ProgressDialog(getContext());
                asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                asyncDialog.setMessage("일기 쓰는중..");
                Log.d("로그","async 작성시작");
                asyncDialog.show();

                Button_Write.setEnabled(false);

                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://miniproject2-4e7d3.appspot.com");
                StorageReference storageRef = firebaseStorage.getReference();
                if(ImageUri != null ) {
                    Uri file = Uri.fromFile(new File(ImageUri));


                    StorageReference riversRef = storageRef.child("images/" + file.getLastPathSegment());
                    storageRef.getName().equals(riversRef.getName());    // true
                    storageRef.getPath().equals(riversRef.getPath());    // false
                    UploadTask uploadTask = riversRef.putFile(file);


                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Log.d("로그", "이미지업로드 실패...");
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                            Log.d("로그", "이미지업로드 성공! : ");
                        }
                    });

                    final StorageReference ref = storageRef.child("images/" + file.getLastPathSegment());
                    uploadTask = ref.putFile(file);

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {

                                downloadUri = task.getResult();
                                Log.d("로그", "이미지 링크 받아오기 성공!" + downloadUri);
                                Post post = new Post();
                                post.setTitle(Edit_Title.getText().toString());
                                post.setContent(Edit_Content.getText().toString());
                                post.setImageUrl(downloadUri.toString());
                                post.setWriterUID(user.getUid());

                                post.setWriteTime(System.currentTimeMillis());

                                new Firebase(FIREBASE_POST_URL).push().setValue(post);


                                /*getActivity().getFragmentManager().beginTransaction().replace(R.id.Frame_Main,new Frag_Home()).commit();*/

                                /*if(getContext().getResources()!=null){
                                    Toast.makeText(getContext(), "일기 작성이 완료되었습니다.", LENGTH_LONG).show();
                                }*/

                                asyncDialog.dismiss();
                                Lottie_Write.setVisibility(View.VISIBLE);
                                Lottie_Write.addAnimatorListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        Lottie_Write.cancelAnimation();
                                        Lottie_Write.setVisibility(GONE);

                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });
                                Lottie_Write.playAnimation();
                                Button_Write.setEnabled(true);


                            } else {
                                // Handle failures
                                Log.d("로그", "이미지 링크 받아오기 실패...");
                                Toast.makeText(getContext(), "업로드 실패...", LENGTH_LONG).show();

                                asyncDialog.dismiss();
                                Button_Write.setEnabled(true);
                            }
                        }
                    });
                }else{
                    Toast.makeText(getContext(), "이미지를 추가해주세요.", Toast.LENGTH_SHORT).show();
                    asyncDialog.dismiss();
                    Button_Write.setEnabled(true);
                }

            }//https://firebasestorage.googleapis.com/v0/b/miniproject2-4e7d3.appspot.com/o/images%2FIMG_20180918_060912.jpg?alt=media&token=32c80bf2-671c-4b47-8dee-881d8149a247
        });





        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK) {
            Uri image = data.getData();
            ImageUri = getPath(image);

            try {

                //원본
                /*Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), image);
                iv_camera.setImageBitmap(bitmap);*/



                //프레그먼트로 막힌방법
                /*Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.parse(ImageUri));
                iv_camera.setImageBitmap(bitmap);

                Glide.with(getActivity())
                        .load(image)
                        .into(iv_camera);*/

                InputStream in = getContext().getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                Image_GetImage.setVisibility(View.VISIBLE);
                Button_LoadImage.setVisibility(View.INVISIBLE);
                Image_GetImage.setImageBitmap(img);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public String getPath(Uri uri){ // 갤러리에서 인텐트로 받은 이미지의 주소(uri)는 한번에 안받아지므로 따로 정의해주는 메소드

        String [] proj = {MediaStore.Images.Media.DATA};

        CursorLoader cursorLoader = new CursorLoader(getContext(), uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();

        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);

    }






}


