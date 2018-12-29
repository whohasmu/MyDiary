package com.jang.user.miniproject2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
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
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jang.user.miniproject2.Object.LoginUser;

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private static final int RC_SIGN_IN = 10;
    SignInButton btn_google;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    LoginButton btn_facebook;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_google = findViewById(R.id.btn_google);
        Log.d("jsb", "onCreate");
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
                Log.d("로그" , "로그인테스트");
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });
        if(mAuth.getCurrentUser()!=null){
            Log.d("로그","자동로그인");
            Toast.makeText(LoginActivity.this," 자동로그인",Toast.LENGTH_SHORT).show();
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
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


        if (requestCode == RC_SIGN_IN) {
            Log.d("로그", "onActivityResult if");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                Log.d("로그", "onActivityResult try");
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.d("로그", "onActivityResult catch"+e.toString());
                Toast.makeText(LoginActivity.this," 로그인 실패.",Toast.LENGTH_SHORT).show();
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


                            FirebaseUser user = mAuth.getCurrentUser();
                            LoginUser loginUser = new LoginUser(user.getUid(),user.getPhotoUrl().toString(),user.getDisplayName());

                            mDatabase.child("users").child(user.getUid()).setValue(loginUser);

                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);

                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this," 로그인 실패.",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    private void handleFacebookAccessToken(AccessToken token) {

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
    }

    @Override
    public void onClick(View v) {
        Log.d("로그", "onClick");
        int i = v.getId();
    }
}
