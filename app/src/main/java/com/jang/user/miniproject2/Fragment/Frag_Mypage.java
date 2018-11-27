package com.jang.user.miniproject2.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jang.user.miniproject2.R;

import java.io.IOException;

/**
 * Created by CYH on 2018-11-03.
 */

public class Frag_Mypage extends Fragment {


    TextView txt_info;
    ImageView my_img;
    TextView txt_name;
    EditText et_name;





    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_mypage,container,false);

        txt_info = view.findViewById(R.id.txt_info);
        my_img = view.findViewById(R.id.my_img);
        txt_name = view.findViewById(R.id.txt_name);
        et_name = view.findViewById(R.id.et_name);


        /*Typeface typeface = getResources().getFont(R.font.gabia_bombaram);
        txt_info.setTypeface(typeface);

        Typeface typeface2 = getResources().getFont(R.font.gabia_bombaram);
        txt_name.setTypeface(typeface2);

        Typeface typeface3 = getResources().getFont(R.font.gabia_bombaram);
        et_name.setTypeface(typeface3);*/


        my_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);

            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri images = data.getData();


        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), images);
            my_img.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
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




}


