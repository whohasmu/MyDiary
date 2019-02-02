package com.jang.user.miniproject2.Fragment;




import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TintableImageSourceView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jang.user.miniproject2.Chat.MessageActivity;
import com.jang.user.miniproject2.Fragment.Friend.SelectPeopleActivity;
import com.jang.user.miniproject2.Fragment.Friend.ViewPagerAdapter;
import com.jang.user.miniproject2.Fragment.Friend.temp1;
import com.jang.user.miniproject2.Fragment.Friend.temp2;
import com.jang.user.miniproject2.Fragment.Friend.temp3;
import com.jang.user.miniproject2.Object.ChatModel;
import com.jang.user.miniproject2.Object.User;
import com.jang.user.miniproject2.R;
import com.jang.user.miniproject2.Temp.MainTabAdapter;
import com.jang.user.miniproject2.Temp.TabPagerAdapter;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;


/**
 * Created by CYH on 2018-11-03.
 */

public class Frag_Friend extends Fragment {



    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm");

    RecyclerView recyclerView;
    ChatRecyclerViewAdapter chatRecyclerViewAdapter;
    FloatingActionButton Button_Chat;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);








        recyclerView = view.findViewById(R.id.List_chat);
        chatRecyclerViewAdapter = new ChatRecyclerViewAdapter();
        recyclerView.setAdapter(chatRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));



        Button_Chat = view.findViewById(R.id.Button_Chat);
        Button_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),SelectPeopleActivity.class));
            }
        });

    /*@Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int position = FragmentPagerItem.getPosition(getArguments());


    }*/





        /*
        tempInflater = inflater;
        Log.d("로그","create");


        *//* SmartTabLayout*//*
        FragmentActivity activity = (FragmentActivity)getActivity();
        fragmentManager = activity.getSupportFragmentManager();
        ViewPager viewPager = view.findViewById(R.id.Frame_Friend);
        viewPagerTab = view.findViewById(R.id.viewpagertab);

        FragmentPagerItems pages = new FragmentPagerItems(getContext());
        pages.add(FragmentPagerItem.of("AA",temp1.class));
        pages.add(FragmentPagerItem.of("BB",temp2.class));
        pages.add(FragmentPagerItem.of("CC",temp3.class));
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(fragmentManager,pages);
        viewPager.setAdapter(adapter);

        viewPagerTab.setBackgroundColor(getResources().getColor(R.color.black));
        viewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                ImageView icon = (ImageView) inflater.inflate(R.layout.custom_tab_icon2, container,false);
                final Resources res = viewPagerTab.getContext().getResources();

                switch (position) {
                    case 0:
                        //아이콘 색상 흰색이라서 임시로 까맣게 설정. 아이콘 새로 구해야함.
                        res.getDrawable(R.drawable.people).setTint(Color.rgb(55,55,55));
                        icon.setImageDrawable(res.getDrawable(R.drawable.people));
                        break;
                    case 1:
                        res.getDrawable(R.drawable.chatting).setTint(Color.rgb(55,55,55));
                        icon.setImageDrawable(res.getDrawable(R.drawable.chatting));
                        break;
                    case 2:
                        res.getDrawable(R.drawable.account).setTint(Color.rgb(55,55,55));
                        icon.setImageDrawable(res.getDrawable(R.drawable.account));
                        break;
                    case 3:
                        icon.setImageDrawable(res.getDrawable(R.drawable.ic_flash_on_white_24d));
                        break;
                    default:
                        throw new IllegalStateException("Invalid position: " + position);
                }
                return icon;
            }


        });
        viewPagerTab.setViewPager(viewPager);*/





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

    class ChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private String userUid;
        private List<ChatModel> chatModels = new ArrayList<>();
        private List<String> keys = new ArrayList<>();
        private ArrayList<String> destinationUsers = new ArrayList<>();

        public ChatRecyclerViewAdapter() {
            userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+userUid).equalTo(true).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    chatModels.clear();
                    keys.clear();
                    for(DataSnapshot item : dataSnapshot.getChildren()){
                        chatModels.add(item.getValue(ChatModel.class));
                        keys.add(item.getKey());

                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false);

            return new CustomViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

            final CustomViewHolder customViewHolder = (ChatRecyclerViewAdapter.CustomViewHolder)holder;
            String destinationUid = null;
            // 일일 채팅방에 있는 유저 체크!!!
            for (String user: chatModels.get(position).users.keySet()){
                if (!user.equals(userUid)){
                    destinationUid = user;
                    destinationUsers.add(destinationUid);
                }

            }

            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User loginUser = dataSnapshot.getValue(User.class);//상대방 유저 정보

                    Glide.with(customViewHolder.itemView.getContext())
                            .load(loginUser.getUser_uri())
                            .apply(new RequestOptions().circleCrop())
                            .into(customViewHolder.profile);

                    customViewHolder.message_title.setText(loginUser.getUser_name());
                    /*customViewHolder.message_title.setText("꺅");*/
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            //메시지를 내림차순으로 정렬

            Map<String,ChatModel.Comment> commentMap = new TreeMap<>(Collections.reverseOrder());

            commentMap.putAll(chatModels.get(position).comments);
            if (commentMap.keySet().toArray().length > 0) {
                String lastMessagekey = (String) commentMap.keySet().toArray()[0];
                customViewHolder.lastMessage.setText(chatModels.get(position).comments.get(lastMessagekey).message);


                //마지막 시간
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                long unixTime = (long) chatModels.get(position).comments.get(lastMessagekey).timestamp;
                Date date = new Date(unixTime);
                customViewHolder.messageTimestamp.setText(simpleDateFormat.format(date));

            }





            customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    /*if (chatModels.get(position).users.size() > 2){
                        intent = new Intent(view.getContext(), GroupMessageActivity.class);
                        intent.putExtra("destinationRoom",keys.get(position));
                    }else {
                        intent = new Intent(view.getContext(), MessageActivity.class);
                        intent.putExtra("destinationUid", destinationUsers.get(position));
                    }*/

                    intent = new Intent(view.getContext(), MessageActivity.class);
                    intent.putExtra("destinationUid", destinationUsers.get(position));

                    ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.from_right, R.anim.to_left);

                    startActivity(intent, activityOptions.toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return chatModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView profile;
            public TextView message_title;
            public TextView lastMessage;
            public TextView messageTimestamp;

            public CustomViewHolder(View view) {
                super(view);
                profile = view.findViewById(R.id.Image_Profile);
                message_title = view.findViewById(R.id.Text_Title);
                lastMessage = view.findViewById(R.id.chatItem_lastmessage);
                messageTimestamp = view.findViewById(R.id.chatItem_timestamp);
            }
        }
    }


    @Override
    public void onResume() {
        Log.d("로그","Mresume");
        super.onResume();

    }

    @Override
    public void onPause() {
        Log.d("로그","Mpause");
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        Log.d("로그","MdestroyView");
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {
        Log.d("로그","Mdestroy");
        super.onDestroy();
    }

    /*@Override
    public void onStart() {
        if (fragmentManager.getBackStackEntryCount() > 0) {     // 이전화면 유지
            fragmentManager.popBackStack();
        } else {                                                // 화면 신규생성
            super.onStart();
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putInt("data",1);
    }*/
}


