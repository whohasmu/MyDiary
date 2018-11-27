package com.jang.user.miniproject2.Fragment;


import android.app.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;


import com.jang.user.miniproject2.Fragment.Friend.ViewPagerAdapter;
import com.jang.user.miniproject2.Fragment.Friend.temp1;
import com.jang.user.miniproject2.Fragment.Friend.temp2;
import com.jang.user.miniproject2.Fragment.Friend.temp3;
import com.jang.user.miniproject2.R;
import com.jang.user.miniproject2.Temp.MainTabAdapter;
import com.jang.user.miniproject2.Temp.TabPagerAdapter;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;


/**
 * Created by CYH on 2018-11-03.
 */

public class Frag_Friend extends Fragment {






    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_friend,container,false);



        FragmentActivity activity = (FragmentActivity)getActivity();
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        MainTabAdapter mainTabAdapter = new MainTabAdapter(activity.getSupportFragmentManager());
        mainTabAdapter.initFragment(getContext());
        viewPager.setAdapter(mainTabAdapter);

        tabLayout.setupWithViewPager(viewPager);













        /* SmartTabLayout*/
//        FragmentActivity activity = (FragmentActivity)getActivity();
//
//        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
//                activity.getSupportFragmentManager(), FragmentPagerItems.with(getContext())
//                .add(R.string.titleA, temp1.class)
//                .add(R.string.titleB, temp2.class)
//                .add(R.string.titleA, temp3.class)
//                .create());
//
//        ViewPager viewPager = (ViewPager) view.findViewById(R.id.Frame_Friend);
//        viewPager.setAdapter(adapter);
//
//        SmartTabLayout viewPagerTab = (SmartTabLayout) view.findViewById(R.id.viewpagertab);
//        viewPagerTab.setViewPager(viewPager);





        /* TabLayout*/

//        BottomNavigationView TopNavigation_Friend = view.findViewById(R.id.TopNavigation_Friend);
//      getFragmentManager().beginTransaction().replace(R.id.Frame_Friend,new PeopleFragment()).commit();
//        final ViewPager viewpager = view.findViewById(R.id.Frame_Friend);
//
//        FragmentActivity activity = (FragmentActivity)getActivity();
//        final FragmentManager manager = activity.getSupportFragmentManager();
//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(manager);
//        viewpager.setAdapter(viewPagerAdapter);



//        tabLayout = view.findViewById(R.id.viewpagertab);
//        tabLayout.addTab(tabLayout.newTab().setText("친구"));
//        tabLayout.addTab(tabLayout.newTab().setText("채팅"));
//        tabLayout.addTab(tabLayout.newTab().setText("설정"));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Initializing ViewPager


        // Creating TabPagerAdapter adapter
//        TabPagerAdapter pagerAdapter = new TabPagerAdapter(activity.getSupportFragmentManager(), tabLayout.getTabCount());
//        viewpager.setAdapter(pagerAdapter);
//        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

//        // Set TabSelectedListener
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewpager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });








            /*기존꺼*/

//        TopNavigation_Friend.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.action_people:
////                        manager.beginTransaction().replace(R.id.Frame_Friend,temp1.getInstance()).commit();
//                        viewpager.setCurrentItem(0);
//
//                        return true;
//
//                    case R.id.action_chat:
////                        manager.beginTransaction().replace(R.id.Frame_Friend,temp2.getInstance()).commit();
//                        viewpager.setCurrentItem(1);
//                        return true;
//
//                    case R.id.action_account:
////                        manager.beginTransaction().replace(R.id.Frame_Friend,temp3.getInstance()).commit();
//                        viewpager.setCurrentItem(2);
//                        return true;
//                }
//                return false;
//            }
//        });


//        getFragmentManager().beginTransaction().replace(R.id.Frame_Friend,new PeopleFragment()).commit();
//
//
//        TopNavigation_Friend.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.action_people:
//                        getFragmentManager().beginTransaction().replace(R.id.Frame_Friend,new PeopleFragment()).commit();
//                        return true;
//
//                    case R.id.action_chat:
//                        getFragmentManager().beginTransaction().replace(R.id.Frame_Friend,new ChatList()).commit();
//                        return true;
//
//                    case R.id.action_account:
//                        getFragmentManager().beginTransaction().replace(R.id.Frame_Friend,new AccountFragment()).commit();
//                        return true;
//                }
//                return false;
//            }
//        });


        return view;
    }





}


