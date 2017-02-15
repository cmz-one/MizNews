package com.cmz_one.miznews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cmz_o on 2016/12/11.
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> title;
    private List<Fragment> views;


    public MyPagerAdapter(FragmentManager fm, List<String> title, List<Fragment> views) {
        super(fm);
        //获取sp里储存的Tab标题
        //根据Tab标题创建Fragment 并传入type参数
        this.title = title;
        this.views = views;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    public void setAdapterTitle(List<String> title) {
        this.title = title;
    }

    public void setAdapterViews(List<Fragment> views) {
        this.views = views;
    }

    @Override
    public Fragment getItem(int position) {
        return views.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }

    @Override
    public int getCount() {
        return views.size();
    }
}
