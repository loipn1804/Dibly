package com.dibs.dibly.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.dibs.dibly.fragment.GuideFragment;

/**
 * Created by USER on 6/30/2015.
 */
public class GuideAdapter extends FragmentStatePagerAdapter {

    private Activity activity;
    private GuideFragment.SwipePagerCbk swipePager;

    public GuideAdapter(FragmentManager fragmentManager, Activity activity, GuideFragment.SwipePagerCbk swipePager) {
        super(fragmentManager);
        this.activity = activity;
        this.swipePager = swipePager;
    }

    @Override
    public Fragment getItem(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", i);
        GuideFragment guideFragment = new GuideFragment();
       // guideFragment.setCallBack(swipePager);
        guideFragment.setArguments(bundle);
        return guideFragment;
    }

    @Override
    public int getCount() {

        return 4;
    }



   /* @Override
    public CharSequence getPageTitle(int position) {
        return "";
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
    } */
}
