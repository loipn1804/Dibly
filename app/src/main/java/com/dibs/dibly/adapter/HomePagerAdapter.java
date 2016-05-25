package com.dibs.dibly.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.dibs.dibly.fragment.DealHomeFragment;

import java.util.List;

/**
 * Created by USER on 6/30/2015.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {

    Activity activity;
    List<String> listString;

    public HomePagerAdapter(FragmentManager fragmentManager, Activity activity, List<String> listString) {
        super(fragmentManager);
        this.listString = listString;
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("page", i);
        DealHomeFragment dealHomeFragment = new DealHomeFragment();
        dealHomeFragment.setArguments(bundle);
        return dealHomeFragment;
    }

    @Override
    public int getCount() {

        return listString.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listString.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        super.destroyItem(container, position, object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {

        super.finishUpdate(container);
    }

    @Override
    public long getItemId(int position) {

        return super.getItemId(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        return super.instantiateItem(container, position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return super.isViewFromObject(view, object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {

        super.restoreState(state, loader);
    }

    @Override
    public Parcelable saveState() {

        return super.saveState();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {

        super.setPrimaryItem(container, position, object);
    }

    @Override
    public void startUpdate(ViewGroup container) {

        super.startUpdate(container);
    }
}
