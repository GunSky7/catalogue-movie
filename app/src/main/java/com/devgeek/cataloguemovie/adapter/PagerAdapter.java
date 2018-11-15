package com.devgeek.cataloguemovie.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.devgeek.cataloguemovie.fragment.FavouriteFragment;
import com.devgeek.cataloguemovie.fragment.HomeFragment;
import com.devgeek.cataloguemovie.fragment.NowPlayingFragment;
import com.devgeek.cataloguemovie.fragment.UpcomingFragment;

/**
 * Created by GunSky7 on 4/24/2018.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;

    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);

        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                HomeFragment tab1 = new HomeFragment();

                return tab1;

            case 1:
                NowPlayingFragment tab2 = new NowPlayingFragment();

                return tab2;

            case 2:
                UpcomingFragment tab3 = new UpcomingFragment();

                return tab3;

            case 3:
                FavouriteFragment tab4 = new FavouriteFragment();

                return tab4;

            default:
                return null;
        }
//        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
