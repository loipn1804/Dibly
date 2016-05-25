package com.dibs.dibly.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.adapter.DealHomeAdapter;
import com.dibs.dibly.adapter.DealsDiscoverAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.MerchantController;
import com.dibs.dibly.daocontroller.MyLocationController;
import com.dibs.dibly.interfaceUtils.OnPopUpFollowingListener;
import com.dibs.dibly.service.RealTimeService;

import java.util.ArrayList;
import java.util.List;

import greendao.Merchant;
import greendao.MyLocation;
import greendao.ObjectDeal;

/**
 * Created by USER on 7/3/2015.
 */
public class CategoryActivity extends BaseActivity implements View.OnClickListener {

    private ListView lvDeal;
    private List<ObjectDeal> listDeal;
    private DealsDiscoverAdapter dealAdapter;
    private RelativeLayout rltBack;
    private ImageView rltSearch;
    private TextView txtNameActionBar;
    private TextView txtNoDeal;
    private View progessbarFooter;
    private LayoutInflater layoutInflater;
    private View headerView;

    private long id;
    private String name;

    private int page = 1;
    private int last_page = 0;
    private boolean isLoading;
    private Long merchantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        overrideFontsLight(findViewById(R.id.root));

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_DEAL_BY_DISCOVERY);
            intentFilter.addAction(DealDetailActivity.RECEIVER_NOTIFY_LIST);
            intentFilter.addAction(RealTimeService.RECEIVER_FOLLOWING_MERCHANT);
            intentFilter.addAction(RealTimeService.RECEIVER_STOP_FOLLOWING_MERCHANT);
            intentFilter.addAction(DealDetailActivity.RECEIVER_EXPRITED_TIME);
            registerReceiver(activityReceiver, intentFilter);
        }

        isLoading = false;

        if (getIntent().getExtras() != null) {
            id = getIntent().getLongExtra("id", 0);
            name = getIntent().getStringExtra("name");

            MyLocation myLocation = MyLocationController.getLastLocation(CategoryActivity.this);
            if (myLocation != null) {
                Intent intentGetDeal = new Intent(CategoryActivity.this, RealTimeService.class);
                intentGetDeal.setAction(RealTimeService.ACTION_GET_DEAL_BY_DISCOVERY);
                intentGetDeal.putExtra(RealTimeService.EXTRA_DISCOVERY_ID, id + "");
                intentGetDeal.putExtra(RealTimeService.EXTRA_LATITUDE, myLocation.getLatitude() + "");
                intentGetDeal.putExtra(RealTimeService.EXTRA_LONGITUDE, myLocation.getLongitude() + "");
                intentGetDeal.putExtra(RealTimeService.EXTRA_PAGE, page + "");
                startService(intentGetDeal);
            } else {
                showPopupPrompt("Please turn on your location so that we can help you get the deals around you.");
            }
        } else {
            finish();
        }

        initView();
        initData();
    }

    private void initView() {
        initialToolBar();
        lvDeal = (ListView) findViewById(R.id.lvDeal);
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        rltSearch = (ImageView) findViewById(R.id.rltSearch);
        txtNameActionBar = (TextView) findViewById(R.id.txtNameActionBar);
        txtNoDeal = (TextView) findViewById(R.id.txtNoDeal);

        txtNoDeal.setVisibility(View.GONE);

        rltBack.setOnClickListener(this);
        rltSearch.setOnClickListener(this);

        lvDeal.setOnScrollListener(new EndScrollListener());

        layoutInflater = LayoutInflater.from(this);
        progessbarFooter = layoutInflater.inflate(R.layout.view_progressbar_loadmore, null);
        headerView = layoutInflater.inflate(R.layout.view_invisible_header, null);

        lvDeal.addHeaderView(headerView);
    }

    private void initData() {
        listDeal = new ArrayList<ObjectDeal>();
        List<ObjectDeal> list = DealController.getDealDiscoveryByDealType(CategoryActivity.this, 3);
        for (ObjectDeal deal : list) {
            listDeal.add(deal);
        }

        dealAdapter = new DealsDiscoverAdapter(CategoryActivity.this, listDeal);
        lvDeal.setAdapter(dealAdapter);

        dealAdapter.setOnPopupFollowingListener(new OnPopUpFollowingListener() {
            @Override
            public void onActionStart(long merchantIdArg) {
                merchantId = merchantIdArg;
                showProgressDialog();
            }
        });

        if (listDeal.size() == 0) {
            showCustomProgressDialog();
//            txtNoDeal.setVisibility(View.VISIBLE);
        } else {
//            txtNoDeal.setVisibility(View.GONE);
        }
        txtNameActionBar.setText(name);
    }

    private void notifyListData() {
        listDeal.clear();
        List<ObjectDeal> list = DealController.getDealByDealType(CategoryActivity.this, 3);
        listDeal.addAll(list);
        dealAdapter.myNotifyDataSetChanged(listDeal);
        if (listDeal.size() == 0) {
            txtNoDeal.setVisibility(View.VISIBLE);
        } else {
            txtNoDeal.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.rltSearch:
                Intent intentSearch = new Intent(CategoryActivity.this, SearchActivity.class);
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
//        if (dealAdapter != null) {
//            dealAdapter.onActivityDestroy();
//        }
        DealController.clearDealByDealType(CategoryActivity.this, 3);
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_DEAL_BY_DISCOVERY)) {

                if (result.equals(RealTimeService.RESULT_OK)) {
                    String lastpage = intent.getStringExtra(RealTimeService.EXTRA_RESULT_LAST_PAGE);
                    last_page = Integer.parseInt(lastpage);
                    isLoading = false;
                    notifyListData();
                }
            } else if (intent.getAction().equalsIgnoreCase(DealDetailActivity.RECEIVER_NOTIFY_LIST)) {
                notifyListData();
            } else if (intent.getAction().equalsIgnoreCase(DealDetailActivity.RECEIVER_EXPRITED_TIME)) {
                notifyListData();
            }else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_STOP_FOLLOWING_MERCHANT)) {
                if (result.equals(RealTimeService.RESULT_OK)) {
                    if (merchantId != null) {
                        Merchant merchant = MerchantController.getById(context, merchantId);
                        if (merchant != null) {
                            merchant.setF_follow(false);
                            MerchantController.update(context, merchant);
                        }

                        DealController.updateObjectIdFliked(context, merchantId, false);
                        notifyListData();
                        String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                        showToastOk(message);
                        merchantId = null;
                    }
                }
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_FOLLOWING_MERCHANT)) {
                if (result.equals(RealTimeService.RESULT_OK)) {
                    if (merchantId != null) {
                        Merchant merchant = MerchantController.getById(context, merchantId);
                        if (merchant != null) {
                            merchant.setF_follow(true);
                            MerchantController.update(context, merchant);
                        }

                        DealController.updateObjectIdFliked(context, merchantId, true);
                        notifyListData();
                        String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                        showToastOk(message);
                        merchantId = null;
                    }
                }
            }

            if (result.equals(RealTimeService.RESULT_FAIL)) {
                String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                showToastError(message);
            } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                showToastError(getString(R.string.nointernet));
            }
            lvDeal.removeFooterView(progessbarFooter);
            hideCustomProgressDialog();
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
                        MyLocation myLocation = MyLocationController.getLastLocation(CategoryActivity.this);
                        if (myLocation != null) {
                            if (page < last_page) {
                                page++;
                                Intent intentGetDeal = new Intent(CategoryActivity.this, RealTimeService.class);
                                intentGetDeal.setAction(RealTimeService.ACTION_GET_DEAL_BY_DISCOVERY);
                                intentGetDeal.putExtra(RealTimeService.EXTRA_DISCOVERY_ID, id + "");
                                intentGetDeal.putExtra(RealTimeService.EXTRA_LATITUDE, myLocation.getLatitude() + "");
                                intentGetDeal.putExtra(RealTimeService.EXTRA_LONGITUDE, myLocation.getLongitude() + "");
                                intentGetDeal.putExtra(RealTimeService.EXTRA_PAGE, page + "");
                                startService(intentGetDeal);
                                lvDeal.addFooterView(progessbarFooter);
                            }
                        }
                    }
                }
            }
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
