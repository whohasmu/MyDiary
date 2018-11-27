package com.jang.user.miniproject2.Fragment.Friend;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jang.user.miniproject2.Fragment.Friend.temp1;
import com.jang.user.miniproject2.Fragment.Friend.temp2;
import com.jang.user.miniproject2.Fragment.Friend.temp3;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return temp1.getInstance();
        }else if(position == 1){
            return temp2.getInstance();
        }else{
            return temp3.getInstance();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}
