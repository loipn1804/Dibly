package com.dibs.dibly.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import com.dibs.dibly.R;
import com.dibs.dibly.adapter.MerchantReviewAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.consts.Const;
import com.dibs.dibly.daocontroller.ReviewController;
import com.dibs.dibly.service.ParallaxService;
import com.dibs.dibly.service.RealTimeService;

import java.util.List;

import greendao.Review;

/**
 * Created by VuPhan on 4/22/16.
 */
public class ReviewsActivity extends BaseActivity {

    private boolean isLoading;
    private boolean isPull;
    private int page;
    private int last_page;
    private long merchantId;
    private ListView lstViewReview;
    private MerchantReviewAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        overrideFontsLight(findViewById(R.id.root));
        initialIntent();
        initialView();
        registerReceiver();
        loadDataToView();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(activityReceiver!=null)
        unregisterReceiver(activityReceiver);
    }

    private void initialIntent() {
        merchantId = getIntent().getExtras().getLong(Const.BUNDLE_EXTRAS.MERCHANT_ID, 0);
    }

    private void initialView() {
        initialToolBar();
        lstViewReview = (ListView) findViewById(R.id.lstViewReview);
        lstViewReview.setOnScrollListener(new EndScrollListener());
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
    }

    private void loadData() {

        Intent intentGetMerchant = new Intent(this, RealTimeService.class);
        intentGetMerchant.setAction(RealTimeService.ACTION_GET_MERCHANT_REVIEW);
        intentGetMerchant.putExtra(RealTimeService.EXTRA_MERCHANT_ID, merchantId);
        intentGetMerchant.putExtra(RealTimeService.EXTRA_PAGE, page);
        startService(intentGetMerchant);
        showProgressDialog();

    }


    private void registerReceiver() {
        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_MERCHANT_REVIEW);
            registerReceiver(activityReceiver, intentFilter);
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_MERCHANT_REVIEW)) {
                if (result.equals(RealTimeService.RESULT_OK)) {

                    String lastpage = intent.getStringExtra(ParallaxService.EXTRA_RESULT_LAST_PAGE);
                    last_page = Integer.parseInt(lastpage);
                    String currentpage = intent.getStringExtra(ParallaxService.EXTRA_RESULT_CURRENT_PAGE);
                    page = Integer.parseInt(currentpage);
                    notifyData();
                }
            }

            if (isPull) {
                isPull = false;
                swipeRefreshLayout.setRefreshing(false);
            }

            hideProgressDialog();
            if (result != null)
                if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);

                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
        }
    };


    private void loadDataToView() {
        isLoading = false;
        isPull = false;
        page = 1;
        last_page = 0;

        List<Review> listComment = ReviewController.getReviewByMerchantId(this, merchantId + "");
        adapter = new MerchantReviewAdapter(this, listComment);
        lstViewReview.setAdapter(adapter);
    }

    private void notifyData() {
        List<Review> listComment = ReviewController.getReviewByMerchantId(this, merchantId + "");
        adapter.setData(listComment);
    }

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

