package com.dibs.dibly.adapter;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.dibs.dibly.fragment.AllOutletFragment;
import com.dibs.dibly.fragment.DetailMerchantProfileFragment;
import com.dibs.dibly.fragment.DetailMerchantReviewFragment;

/**
 * Created by USER on 11/30/2015.
 */
public class DetailMerchantPagerAdapter extends FragmentPagerAdapter {

    private Activity activity;

    public DetailMerchantPagerAdapter(FragmentManager fragmentManager, Activity activity) {
        super(fragmentManager);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                DetailMerchantProfileFragment detailMerchantProfileFragment = new DetailMerchantProfileFragment();
                return detailMerchantProfileFragment;
            case 1:
                AllOutletFragment allOutletFragment = new AllOutletFragment();
                return allOutletFragment;
            case 2:
                DetailMerchantReviewFragment detailMerchantReviewFragment = new DetailMerchantReviewFragment();
                return detailMerchantReviewFragment;
        }
        return null;
    }

    @Override
    public int getCount() {

        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "PROFILE";
            case 1:
                return "OUTLETS";
            case 2:
                return "REVIEWS";
        }
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
