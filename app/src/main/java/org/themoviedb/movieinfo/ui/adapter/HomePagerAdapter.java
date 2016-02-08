package org.themoviedb.movieinfo.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class HomePagerAdapter extends FragmentStatePagerAdapter {

    private String[] mTitles;
    private Fragment[] mFragments;

    public HomePagerAdapter(@NonNull FragmentManager fm,
                            @NonNull Fragment[] fragments,
                            @NonNull String[] titles) {
        super(fm);
        mFragments = fragments;
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return Math.min(mTitles.length, mFragments.length);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
