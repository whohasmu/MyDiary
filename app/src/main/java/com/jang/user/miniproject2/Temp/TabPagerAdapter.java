package com.jang.user.miniproject2.Temp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jang.user.miniproject2.Fragment.Friend.temp1;
import com.jang.user.miniproject2.Fragment.Friend.temp2;
import com.jang.user.miniproject2.Fragment.Friend.temp3;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    // Count number of tabs
    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                temp1 tabFragment1 = new temp1();
                return tabFragment1;
            case 1:
                temp2 tabFragment2 = new temp2();
                return tabFragment2;
            case 2:
                temp3 tabFragment3 = new temp3();
                return tabFragment3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}


