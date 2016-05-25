package com.dibs.dibly.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.dibs.dibly.R;
import com.dibs.dibly.adapter.PurchaseDealPagerAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.dibs.dibly.view.SlidingTabLayout;

/**
 * Created by USER on 6/29/2015.
 */
public class PurchasedDealActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private SlidingTabLayout tabStrip;
    private PurchaseDealPagerAdapter purchaseDealPagerAdapter;

    private RelativeLayout rltBack;
    private RelativeLayout rltSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_deal);
        initialToolBar();
        overrideFontsLight(findViewById(R.id.root));

        initView();
        initData();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabStrip = (SlidingTabLayout) findViewById(R.id.tabs);
        tabStrip.setDistributeEvenly(true);
        rltSearch = (RelativeLayout) findViewById(R.id.rltSearch);
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);

        rltBack.setOnClickListener(this);
        rltSearch.setOnClickListener(this);
    }

    private void initData() {
        purchaseDealPagerAdapter = new PurchaseDealPagerAdapter(getSupportFragmentManager(), PurchasedDealActivity.this);
        viewPager.setAdapter(purchaseDealPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        //tabStrip.setShouldExpand(true);
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabStrip.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.white);
            }
        });
        tabStrip.setViewPager(viewPager);
       /* tabStrip.setTextColor(getResources().getColor(R.color.white));
        tabStrip.setDividerColor(getResources().getColor(R.color.transparent));
        tabStrip.setIndicatorColor(getResources().getColor(R.color.white));
        tabStrip.setUnderlineColor(getResources().getColor(R.color.transparent));
        tabStrip.setBackgroundColor(getResources().getColor(R.color.orange_main));
        tabStrip.setTypeface(StaticFunction.light(this), 1);
        tabStrip.setTextSize((int) getResources().getDimension(R.dimen.txt_15sp)); */
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.rltSearch:
                Intent intentSearch = new Intent(PurchasedDealActivity.this, SearchActivity.class);
                startActivity(intentSearch);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_noitem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
