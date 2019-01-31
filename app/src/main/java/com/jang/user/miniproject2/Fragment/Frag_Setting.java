package com.jang.user.miniproject2.Fragment;

import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.jang.user.miniproject2.LoginActivity;
import com.jang.user.miniproject2.R;

/**
 * Created by CYH on 2018-11-03.
 */

public class Frag_Setting extends Fragment implements GoogleApiClient.OnConnectionFailedListener{



    Button logout;
    GoogleApiClient mGoogleApiClient;
    FirebaseAuth mAuth;
    TextView textView5;

    EditText et_test;



    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_setting,container,false);

        textView5 = view.findViewById(R.id.textView5);
        et_test = view.findViewById(R.id.et_test);

       /*Typeface typeface1 = getResources().getFont(R.font.gabia_bombaram);*/
       /*et_test.setTypeface(typeface1);*/
        logout = view.findViewById(R.id.logout);

        /*Typeface typeface = getResources().getFont(R.font.gabia_bombaram);*/
        /*textView5.setTypeface(typeface);*/





        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(view.getContext());

                alt_bld.setMessage("로그아웃 하시겠습니까?").setCancelable(false)
                        .setPositiveButton("네",

                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        signOut();
                                        Intent intent = new Intent(getContext(),LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }

                                }).setNegativeButton("아니오",

                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                // 아니오 클릭. dialog 닫기.
                                dialog.cancel();
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



        return view;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(getContext(), "로그인 실패", Toast.LENGTH_LONG).show();

    }
    public void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage((FragmentActivity) this.getContext(),this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        mGoogleApiClient.connect();

        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {

            @Override

            public void onConnected(@Nullable Bundle bundle) {

                FirebaseAuth.getInstance().signOut();

                if (mGoogleApiClient.isConnected()) {

                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {

                        @Override

                        public void onResult(@NonNull Status status) {

                            if (status.isSuccess()) {

                                Log.v("알림", "로그아웃 성공");

                                getActivity().setResult(1);

                            } else {

                                getActivity().setResult(0);

                            }

                            getActivity().finish();

                        }

                    });

                }

            }

            @Override
            public void onConnectionSuspended(int i) {

                Log.v("알림", "Google API Client Connection Suspended");

                getActivity().setResult(-1);

                getActivity().finish();

            }

        });

    }









}


