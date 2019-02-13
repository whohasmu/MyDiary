package com.jang.user.miniproject2.Temp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jang.user.miniproject2.DetailActivity;
import com.jang.user.miniproject2.R;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageFullActivity extends AppCompatActivity {

    ImageView Image;
    PhotoViewAttacher mAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full);


        Image = findViewById(R.id.Image);

        Intent intent = getIntent();
        String ImageUri = intent.getStringExtra("ImageUri");

        Glide.with(this)
                .load(ImageUri)
                .into(Image);

        mAttacher = new PhotoViewAttacher(Image);



    }
}
