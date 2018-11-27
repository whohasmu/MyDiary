package com.jang.user.miniproject2.Temp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.jang.user.miniproject2.Fragment.Friend.temp1;
import com.jang.user.miniproject2.Fragment.Friend.temp2;
import com.jang.user.miniproject2.Fragment.Friend.temp3;

import java.util.ArrayList;
import java.util.List;

public class MainTabAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();

    public MainTabAdapter(FragmentManager fm) {
        super(fm);
    }

    public void initFragment(Context mContext) {
        mFragments.add(new temp1());
        mFragmentTitles.add("목록");
        mFragments.add(new temp2());
        mFragmentTitles.add("채팅");
        mFragments.add(new temp3());
        mFragmentTitles.add("계정");
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }
}

