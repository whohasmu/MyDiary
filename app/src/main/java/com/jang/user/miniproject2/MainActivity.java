package com.jang.user.miniproject2;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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











    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView nav_bot = (BottomNavigationView)findViewById(R.id.nav_bot);
        nav_bot.setOnNavigationItemSelectedListener(navigationListener);


        getFragmentManager().beginTransaction().replace(R.id.Frame_Main,new Frag_Home()).commit();
        passPushTokenToServer();
        //commit hmm...
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_bottom_navigation_menu1:
                    getFragmentManager().beginTransaction().replace(R.id.Frame_Main,new Frag_Home()).commit();
                    return true;
                case R.id.action_bottom_navigation_menu2:
                    getFragmentManager().beginTransaction().replace(R.id.Frame_Main,new Frag_Friend()).commit();
                    return true;
                case R.id.action_bottom_navigation_menu3:
                    getFragmentManager().beginTransaction().replace(R.id.Frame_Main,new Frag_Write()).commit();
                    return true;
                case R.id.action_bottom_navigation_menu4:
                    getFragmentManager().beginTransaction().replace(R.id.Frame_Main,new Frag_Mypage()).commit();
                    return true;
                case R.id.action_bottom_navigation_menu5:
                    getFragmentManager().beginTransaction().replace(R.id.Frame_Main,new Frag_Setting()).commit();
                    return true;
            }
            return false;

        }
    };




    void passPushTokenToServer(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = FirebaseInstanceId.getInstance().getToken();
        Map<String,Object> map = new HashMap<>();
        map.put("pushToken",token);

        FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(map);

    }


}
