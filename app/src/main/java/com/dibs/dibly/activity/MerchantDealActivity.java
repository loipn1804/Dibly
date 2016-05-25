package com.dibs.dibly.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.adapter.DealHomeAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.MyLocationController;
import com.dibs.dibly.service.RealTimeService;

import java.util.ArrayList;
import java.util.List;

import greendao.MyLocation;
import greendao.ObjectDeal;

/**
 * Created by USER on 7/22/2015.
 */
public class MerchantDealActivity extends BaseActivity implements View.OnClickListener {

    private ListView lvDeal;
    private List<ObjectDeal> listDeal;
    private DealHomeAdapter dealAdapter;
    private RelativeLayout rltBack;
    private RelativeLayout rltSearch;
    private TextView txtNameActionBar;

    private long id;
    private String name;

    private int page = 1;
    private int last_page = 0;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_deal);

        overrideFontsLight(findViewById(R.id.root));

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_DEAL_BY_MERCHANT);
            intentFilter.addAction(DealDetailActivity.RECEIVER_NOTIFY_LIST);
            intentFilter.addAction(DealDetailActivity.RECEIVER_EXPRITED_TIME);
            registerReceiver(activityReceiver, intentFilter);
        }

        if (getIntent().hasExtra("id") && getIntent().hasExtra("name")) {
            id = getIntent().getLongExtra("id", 0);
            name = getIntent().getStringExtra("name");

            MyLocation myLocation = MyLocationController.getLastLocation(MerchantDealActivity.this);
            if (myLocation != null) {
                Intent intentGetDeal = new Intent(MerchantDealActivity.this, RealTimeService.class);
                intentGetDeal.setAction(RealTimeService.ACTION_GET_DEAL_BY_MERCHANT);
                intentGetDeal.putExtra(RealTimeService.EXTRA_MERCHANT_ID, id + "");
                intentGetDeal.putExtra(RealTimeService.EXTRA_LATITUDE, myLocation.getLatitude() + "");
                intentGetDeal.putExtra(RealTimeService.EXTRA_LONGITUDE, myLocation.getLongitude() + "");
                intentGetDeal.putExtra(RealTimeService.EXTRA_PAGE, page + "");
                startService(intentGetDeal);
            } else {
                showPopupPrompt("Please turn on your location so that we can help you get the deals around you.");
            }
            initView();
            initData();
        } else {
            finish();
        }
    }

    private void initView() {
        lvDeal = (ListView) findViewById(R.id.lvDeal);
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        rltSearch = (RelativeLayout) findViewById(R.id.rltSearch);
        txtNameActionBar = (TextView) findViewById(R.id.txtNameActionBar);

        rltBack.setOnClickListener(this);
        rltSearch.setOnClickListener(this);
    }

    private void initData() {
        lvDeal.setOnScrollListener(new EndScrollListener());

        listDeal = new ArrayList<ObjectDeal>();
        List<ObjectDeal> list = DealController.getDealByDealType(MerchantDealActivity.this, 4);
        for (ObjectDeal deal : list) {
            listDeal.add(deal);
        }

        dealAdapter = new DealHomeAdapter(MerchantDealActivity.this, listDeal);
        lvDeal.setAdapter(dealAdapter);

        if (listDeal.size() == 0) {
            showCustomProgressDialog();
        }
        txtNameActionBar.setText(name);
    }

    private void notifyListData() {
        listDeal.clear();
        List<ObjectDeal> list = DealController.getDealByDealType(MerchantDealActivity.this, 4);
        listDeal.addAll(list);
        dealAdapter.myNotifyDataSetChanged(listDeal);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.rltSearch:
                Intent intentSearch = new Intent(MerchantDealActivity.this, SearchActivity.class);
                startActivity(intentSearch);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            unregisterReceiver(activityReceiver);
        }
        DealController.clearDealByDealType(MerchantDealActivity.this, 4);
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_DEAL_BY_MERCHANT)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    String lastpage = intent.getStringExtra(RealTimeService.EXTRA_RESULT_LAST_PAGE);
                    last_page = Integer.parseInt(lastpage);
                    isLoading = false;
                    notifyListData();
                    hideCustomProgressDialog();
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                    hideCustomProgressDialog();
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                    hideCustomProgressDialog();
                }
            } else if (intent.getAction().equalsIgnoreCase(DealDetailActivity.RECEIVER_NOTIFY_LIST)) {
                notifyListData();
            } else if (intent.getAction().equalsIgnoreCase(DealDetailActivity.RECEIVER_EXPRITED_TIME)) {
                notifyListData();
            }
        }
    };

    private class EndScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount >= visibleItemCount + 2) {
                if (firstVisibleItem + 3 >= totalItemCount - visibleItemCount + 2) {
                    if (!isLoading) {
                        isLoading = true;
                        MyLocation myLocation = MyLocationController.getLastLocation(MerchantDealActivity.this);
                        if (myLocation != null) {
                            if (page < last_page) {
                                page++;
                                Intent intentGetDeal = new Intent(MerchantDealActivity.this, RealTimeService.class);
                                intentGetDeal.setAction(RealTimeService.ACTION_GET_DEAL_BY_MERCHANT);
                                intentGetDeal.putExtra(RealTimeService.EXTRA_MERCHANT_ID, id + "");
                                intentGetDeal.putExtra(RealTimeService.EXTRA_LATITUDE, myLocation.getLatitude() + "");
                                intentGetDeal.putExtra(RealTimeService.EXTRA_LONGITUDE, myLocation.getLongitude() + "");
                                intentGetDeal.putExtra(RealTimeService.EXTRA_PAGE, page + "");
                                startService(intentGetDeal);
                            }
                        }
                    }
                }
            }
        }
    }
}
