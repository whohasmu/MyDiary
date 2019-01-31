package com.jang.user.miniproject2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.CallbackManager;
import com.facebook.login.Login;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jang.user.miniproject2.Object.User;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private static final int RC_SIGN_IN = 10;
    SignInButton btn_google;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    LoginButton btn_facebook;
    private DatabaseReference mDatabase;
    LottieAnimationView Lottie_Login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_google = findViewById(R.id.btn_google);
        Lottie_Login = findViewById(R.id.Lottie_Login);
        Lottie_Login.setAnimation("Login.json");
        Lottie_Login.loop(true);



        Firebase.setAndroidContext(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);


            }
        });
        if(mAuth.getCurrentUser()!=null){
            /*Log.d("로그","자동로그인");
            Toast.makeText(LoginActivity.this," 자동로그인",Toast.LENGTH_SHORT).show();*/
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);

            /*Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);*/
        }


        mCallbackManager = CallbackManager.Factory.create();
       /* btn_facebook = findViewById(R.id.button_facebook_login);
        btn_facebook.setReadPermissions("email", "public_profile");
        btn_facebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("로그", "onSuccess");

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("로그", "onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("로그", "onError");
                Log.w("로그", error);
                // ...
            }
        });*/

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("로그", "onActivityResult");
        Lottie_Login.setVisibility(View.VISIBLE);
        Lottie_Login.playAnimation();




        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                Log.d("로그", "onActivityResult catch"+e.toString());

                // Google Sign In failed, update UI appropriately

               
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.d("로그", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this,"성공적으로 로그인 되었습니다.",Toast.LENGTH_SHORT).show();


                            final FirebaseUser LoginUser = mAuth.getCurrentUser();

                            /*mDatabase.child("user").child(LoginUser.getUid()).equalTo(LoginUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.exists()) {
                                        Log.d("로그","기존회원");
                                        User loginUser = new User(LoginUser.getUid(), LoginUser.getPhotoUrl().toString(), LoginUser.getDisplayName());
                                        *//*mDatabase.child("users").child(LoginUser.getUid()).setValue(loginUser);*//**//*
                                       /* mDatabase.child("users").child(LoginUser.getUid()).child("google_uri").setValue(loginUser.getGoogle_uri());
                                        mDatabase.child("users").child(LoginUser.getUid()).child("google_name").setValue(loginUser.getGoogle_name());*//*

                                        //기존회원






                                        String token = FirebaseInstanceId.getInstance().getToken();
                                        Map<String,Object> map = new HashMap<>();
                                        map.put("pushToken",token);
                                        map.put("google_uri",loginUser.getGoogle_uri());
                                        map.put("google_name",loginUser.getGoogle_name());

                                        FirebaseDatabase.getInstance().getReference().child("users").child(LoginUser.getUid()).updateChildren(map);


                                    }else {


                                        Log.d("로그","새로운 가입");
                                        //String UId, String google_uri, String user_uri, String google_name, String user_name
                                        String token = FirebaseInstanceId.getInstance().getToken();
                                        User loginUser = new User(LoginUser.getUid(), LoginUser.getPhotoUrl().toString(), LoginUser.getPhotoUrl().toString(), LoginUser.getDisplayName(), LoginUser.getDisplayName(),token);
                                        mDatabase.child("users").child(LoginUser.getUid()).setValue(loginUser);

                                        //최초로그인(회원가입)
                                        //기초정보로 google계정으로부터 이름과 프로필사진 가져옴


                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });*/


                            mDatabase.child("users").child(LoginUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()) {
                                        Log.d("로그","기존회원");
                                        User loginUser = new User(LoginUser.getUid(), LoginUser.getPhotoUrl().toString(), LoginUser.getDisplayName());
                                        mDatabase.child("users").child(LoginUser.getUid()).setValue(loginUser);
                                        mDatabase.child("users").child(LoginUser.getUid()).child("google_uri").setValue(loginUser.getGoogle_uri());
                                        mDatabase.child("users").child(LoginUser.getUid()).child("google_name").setValue(loginUser.getGoogle_name());

                                        //기존회원






                                        String token = FirebaseInstanceId.getInstance().getToken();
                                        Map<String,Object> map = new HashMap<>();
                                        map.put("pushToken",token);
                                        map.put("google_uri",loginUser.getGoogle_uri());
                                        map.put("google_name",loginUser.getGoogle_name());

                                        FirebaseDatabase.getInstance().getReference().child("users").child(LoginUser.getUid()).updateChildren(map);


                                    }else {


                                        Log.d("로그","새로운 가입");
                                        //String UId, String google_uri, String user_uri, String google_name, String user_name
                                        String token = FirebaseInstanceId.getInstance().getToken();
                                        User loginUser = new User(LoginUser.getUid(), LoginUser.getPhotoUrl().toString(), LoginUser.getPhotoUrl().toString(), LoginUser.getDisplayName(), LoginUser.getDisplayName(),token);
                                        mDatabase.child("users").child(LoginUser.getUid()).setValue(loginUser);

                                        //최초로그인(회원가입)
                                        //기초정보로 google계정으로부터 이름과 프로필사진 가져옴


                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            Lottie_Login.setVisibility(View.GONE);
                            Lottie_Login.cancelAnimation();

                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this," 로그인 실패...",Toast.LENGTH_SHORT).show();
                            Lottie_Login.setVisibility(View.GONE);
                            Lottie_Login.cancelAnimation();
                        }

                    }
                });
    }

    /*private void handleFacebookAccessToken(AccessToken token) {

        showProgressDialog();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("로그", "isSuccessful");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Log.d("로그", "user.getDisplayName()=" + user.getDisplayName());
                            Toast.makeText(LoginActivity.this,"성공적으로 로그인 되었습니다.",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {

                        }
                    }
                });
    }*/

    @Override
    public void onClick(View v) {
        Log.d("로그", "onClick");
        int i = v.getId();
    }
}
