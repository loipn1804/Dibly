package com.dibs.dibly.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.adapter.FollowingDealsMerchantAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.MerchantController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.interfaceUtils.OnPopUpFollowingListener;
import com.dibs.dibly.service.ParallaxService;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import greendao.Merchant;
import greendao.ObjectDeal;
import greendao.ObjectUser;

/**
 * Created by VuPhan on 4/19/16.
 */
public class FollowingMerchantDealsActivity extends BaseActivity {

    private int page;
    private int last_page;
    private boolean isLoading;
    private boolean isPull;
    private boolean isFirst = true;
    private Long merchantId;
    private FollowingDealsMerchantAdapter dealHomeAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<ObjectDeal> listDeal;
    private ListView lvDeal;
    private ImageView btnMerchantList;
    private LinearLayout lnlCover;
    private TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followingdealsmerchant);

        enterMixPanel();

        overrideFontsLight(findViewById(R.id.root));
        registerReceiver();
        initView();
        initData();
        loadData();
        showProgressDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        notifyListData();

        if (!isFirst) {
            loadData();
        }
        isFirst = false;
    }

    private void initView() {
        initialToolBar();
        lvDeal = (ListView) findViewById(R.id.lvDeal);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        btnMerchantList = (ImageView) findViewById(R.id.btnMerchantList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange_main);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                last_page = 0;
                isPull = true;
                loadData();
            }
        });

        lvDeal.setOnScrollListener(new EndScrollListener());
        btnMerchantList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMerchantList = new Intent(FollowingMerchantDealsActivity.this, MerchantFollowingListActivity.class);
                startActivity(intentMerchantList);
            }
        });

        lnlCover = (LinearLayout) findViewById(R.id.lnlCover);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View headerView = layoutInflater.inflate(R.layout.header_followingdeal, null);

        lvDeal.addHeaderView(headerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            unregisterReceiver(activityReceiver);
        }

        exitMixPanel();
    }

    private void registerReceiver() {
        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_DEAL_FOLLOWING);
            intentFilter.addAction(RealTimeService.RECEIVER_STOP_FOLLOWING_MERCHANT);
            intentFilter.addAction(RealTimeService.RECEIVER_FOLLOWING_MERCHANT);
            intentFilter.addAction(DealDetailActivity.RECEIVER_EXPRITED_TIME);
            registerReceiver(activityReceiver, intentFilter);
        }
    }


    private void loadData() {

        Intent intentGetDeal = new Intent(this, RealTimeService.class);
        intentGetDeal.setAction(RealTimeService.ACTION_DEAL_FOLLOWING);
        intentGetDeal.putExtra(ParallaxService.EXTRA_PAGE, page + "");
        startService(intentGetDeal);

    }

    private void initData() {
        page = 1;
        last_page = 0;
        listDeal = new ArrayList<>();
        dealHomeAdapter = new FollowingDealsMerchantAdapter(this, listDeal);
        lvDeal.setAdapter(dealHomeAdapter);

        dealHomeAdapter.setOnPopupFollowingListener(new OnPopUpFollowingListener() {
            @Override
            public void onActionStart(long merchantIdArg) {
                merchantId = merchantIdArg;
                showProgressDialog();
            }
        });

        notifyListData();
    }

    private void notifyListData() {
        listDeal.clear();
        List<ObjectDeal> list = DealController.getDealsFollowing(this);
        listDeal.addAll(list);
        dealHomeAdapter.myNotifyDataSetChanged(listDeal);

        if (list != null && list.size() > 0) {
            lnlCover.setVisibility(View.GONE);
            lvDeal.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            lnlCover.setVisibility(View.VISIBLE);
            lvDeal.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_DEAL_FOLLOWING)) {

                if (result.equals(RealTimeService.RESULT_OK)) {
                    String lastpage = intent.getStringExtra(RealTimeService.EXTRA_RESULT_LAST_PAGE);
                    last_page = Integer.parseInt(lastpage);
                    String currentpage = intent.getStringExtra(RealTimeService.EXTRA_RESULT_CURRENT_PAGE);
                    page = Integer.parseInt(currentpage);
                    notifyListData();
                    isLoading = false;
                }
                hideCustomProgressDialog();
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_STOP_FOLLOWING_MERCHANT)) {
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

            hideProgressDialog();
            if (result != null)
                if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }

            if (isPull) {
                isPull = false;
                swipeRefreshLayout.setRefreshing(false);
            }
            if (intent.getAction().equalsIgnoreCase(DealDetailActivity.RECEIVER_EXPRITED_TIME)) {
                dealHomeAdapter.myNotifyDataSetChanged(listDeal);
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
                    if (!isLoading && !isPull) {
                        isLoading = true;

                        if (page < last_page) {
                            page++;
                            loadData();
                            showProgressDialog();
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

    private void enterMixPanel() {
        ObjectUser user = UserController.getCurrentUser(this);
        JSONObject object = new JSONObject();
        try {
            object.put("time", StaticFunction.getCurrentTime());
            object.put("email", user.getEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        trackMixPanel(getString(R.string.Enter_Following), object);
        startDurationMixPanel(getString(R.string.Duration_Following));
    }

    private void exitMixPanel() {
        ObjectUser user = UserController.getCurrentUser(this);
        JSONObject object = new JSONObject();
        try {
            object.put("time", StaticFunction.getCurrentTime());
            object.put("email", user.getEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        trackMixPanel(getString(R.string.Exit_Following), object);
        endDurationMixPanel(getString(R.string.Duration_Following), object);
    }
}
