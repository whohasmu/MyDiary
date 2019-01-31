package com.jang.user.miniproject2;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jang.user.miniproject2.Fragment.Frag_Friend;
import com.jang.user.miniproject2.Fragment.Frag_Home;
import com.jang.user.miniproject2.Fragment.Frag_Mypage;
import com.jang.user.miniproject2.Fragment.Frag_Setting;
import com.jang.user.miniproject2.Fragment.Frag_Write;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {




    FragmentManager fragmentManager;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView nav_bot = findViewById(R.id.nav_bot);
        nav_bot.setOnNavigationItemSelectedListener(navigationListener);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frame_Main,new Frag_Home()).commit();

        //commit1234
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_bottom_navigation_menu1:
                    fragmentManager.beginTransaction().replace(R.id.Frame_Main,new Frag_Home()).commit();
                    fragmentManager.beginTransaction().addToBackStack(null);
                    return true;
                case R.id.action_bottom_navigation_menu2:
                    fragmentManager.beginTransaction().replace(R.id.Frame_Main, new Frag_Friend()).commit();
                    fragmentManager.beginTransaction().addToBackStack(null);
                    return true;
                case R.id.action_bottom_navigation_menu3:
                    fragmentManager.beginTransaction().replace(R.id.Frame_Main,new Frag_Write()).commit();
                    return true;
                case R.id.action_bottom_navigation_menu4:
                    fragmentManager.beginTransaction().replace(R.id.Frame_Main,new Frag_Mypage()).commit();
                    return true;
                case R.id.action_bottom_navigation_menu5:
                    fragmentManager.beginTransaction().replace(R.id.Frame_Main,new Frag_Setting()).commit();
                    return true;
            }
            return false;

        }
    };







}
