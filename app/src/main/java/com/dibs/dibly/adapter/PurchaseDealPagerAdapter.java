package com.dibs.dibly.adapter;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.dibs.dibly.fragment.PurchaseDeaHistoryFragmentV2;
import com.dibs.dibly.fragment.PurchaseDealAvailableFragment;
import com.dibs.dibly.fragment.PurchaseDealAvailableFragmentV2;
import com.dibs.dibly.fragment.PurchaseDealHistoryFragment;

/**
 * Created by USER on 7/3/2015.
 */
public class PurchaseDealPagerAdapter extends FragmentPagerAdapter {

    Activity activity;

    public PurchaseDealPagerAdapter(FragmentManager fragmentManager, Activity activity) {
        super(fragmentManager);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new PurchaseDealAvailableFragmentV2();
            case 1:
                return new PurchaseDeaHistoryFragmentV2();
        }
        return null;
    }

    @Override
    public int getCount() {

        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "AVAILABLE DEALS";
            case 1:
                return "PAST DEALS";
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
