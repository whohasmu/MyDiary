package com.jang.user.miniproject2.Fragment.Friend;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.jang.user.miniproject2.R;

import java.util.HashMap;
import java.util.Map;


public class temp3 extends Fragment {

    private static temp3 curr=null;

    public static temp3 getInstance() {
        if (curr == null) {
            curr = new temp3();
        }

        return curr;
    }

    Button accountBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        View view = inflater.inflate(R.layout.fragment_account,container,false);

        accountBtn = view.findViewById(R.id.fragment_account_btn);

        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(view.getContext());
            }
        });





        return view;
    }

    void showDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_comment,null);
        final EditText commentDialog_et = (EditText) view.findViewById(R.id.commentDialog_et);
        builder.setView(view).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Map<String,Object>stringObjectMap = new HashMap<>();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                stringObjectMap.put("comment",commentDialog_et.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(stringObjectMap);

            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }

}
