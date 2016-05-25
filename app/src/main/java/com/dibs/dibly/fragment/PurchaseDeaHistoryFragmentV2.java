package com.dibs.dibly.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dibs.dibly.R;
import com.dibs.dibly.activity.DealDetailActivity;
import com.dibs.dibly.adapter.FollowingDealsMerchantAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.consts.Const;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.MerchantController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.interfaceUtils.OnPopUpFollowingListener;
import com.dibs.dibly.service.ParallaxService;
import com.dibs.dibly.service.RealTimeService;

import java.util.ArrayList;
import java.util.List;

import greendao.Merchant;
import greendao.ObjectDeal;
import greendao.ObjectUser;

/**
 * Created by VuPhan on 4/20/16.
 */
public class PurchaseDeaHistoryFragmentV2 extends Fragment {

    private int page;
    private int last_page;
    private boolean isLoading;
    private boolean isPull;
    private Long merchantId;
    private FollowingDealsMerchantAdapter dealHomeAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<ObjectDeal> listDeal;
    private ListView lvDeal;
    private LinearLayout lnlCover;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        registerReceiver();


        View view = inflater.inflate(R.layout.fragment_purchase_deal_available, container, false);

        ((BaseActivity) getActivity()).overrideFontsLight(view);

        initView(view);
        initData();
        loadData();

        return view;
    }


    private void initView(View view) {

        lvDeal = (ListView) view.findViewById(R.id.lvPurchaseDeal);
        lnlCover = (LinearLayout) view.findViewById(R.id.lnlCover);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
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

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            getActivity().unregisterReceiver(activityReceiver);
        }
    }


    private void registerReceiver() {
        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_STOP_FOLLOWING_MERCHANT);
            intentFilter.addAction(RealTimeService.RECEIVER_FOLLOWING_MERCHANT);
            intentFilter.addAction(DealDetailActivity.RECEIVER_EXPRITED_TIME);
            intentFilter.addAction(ParallaxService.RECEIVER_GET_DEAL_CLAIMED);
            intentFilter.addAction(DealDetailActivity.RECEIVER_CLAIM_SUCCESS);
            getActivity().registerReceiver(activityReceiver, intentFilter);
        }
    }

    private void loadData() {
        ObjectUser user = UserController.getCurrentUser(getActivity());
        Intent intentGetDeal = new Intent(getActivity(), RealTimeService.class);
        intentGetDeal.setAction(RealTimeService.ACTION_GET_DEAL_CLAIMED);
        intentGetDeal.putExtra(RealTimeService.EXTRA_CONSUMER_ID, user.getConsumer_id() + "");
        getActivity().startService(intentGetDeal);
        ((BaseActivity) getActivity()).showProgressDialog();
    }

    private void initData() {
        page = 1;
        last_page = 0;
        listDeal = new ArrayList<>();
        dealHomeAdapter = new FollowingDealsMerchantAdapter(getActivity(), listDeal);
        lvDeal.setAdapter(dealHomeAdapter);

        dealHomeAdapter.setOnPopupFollowingListener(new OnPopUpFollowingListener() {
            @Override
            public void onActionStart(long merchantIdArg) {
                merchantId = merchantIdArg;
                ((BaseActivity) getActivity()).showProgressDialog();
            }
        });

        notifyListData();
    }

    private void notifyListData() {
        listDeal.clear();
        List<ObjectDeal> list = DealController.getDealByDealTypeHistory(getActivity(), Const.DEALTYPE.DEAL_HISTORY);
        listDeal.addAll(list);
        dealHomeAdapter.myNotifyDataSetChanged(listDeal);
        if (list != null && list.size() > 0) {
            lnlCover.setVisibility(View.GONE);
        } else {
            lnlCover.setVisibility(View.VISIBLE);
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_DEAL_CLAIMED)) {

                if (result.equals(RealTimeService.RESULT_OK)) {

                    last_page = 1;
                    page = 1;
                    notifyListData();
                    isLoading = false;
                }
                ((BaseActivity) getActivity()).hideCustomProgressDialog();
            } else if (intent.getAction().equalsIgnoreCase(DealDetailActivity.RECEIVER_CLAIM_SUCCESS)) {

                last_page = 1;
                page = 1;
                loadData();
                isLoading = false;

                ((BaseActivity) getActivity()).hideCustomProgressDialog();
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
                        ((BaseActivity) getActivity()).showToastOk(message);
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
                        ((BaseActivity) getActivity()).showToastOk(message);
                        merchantId = null;
                    }
                }
            }

            ((BaseActivity) getActivity()).hideProgressDialog();
            if (result != null)
                if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    ((BaseActivity) getActivity()).showToastError(message);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    ((BaseActivity) getActivity()).showToastError(getString(R.string.nointernet));
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
                        }

                    }
                }
            }
        }
    }
}
