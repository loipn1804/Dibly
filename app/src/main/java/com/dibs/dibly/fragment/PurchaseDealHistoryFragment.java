package com.dibs.dibly.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.activity.DealDetailActivity;
import com.dibs.dibly.adapter.PurchaseDealClaimedAdapter;
import com.dibs.dibly.adapter.WheelSortAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.DealClaimedController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.ParallaxService;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.ClickSpan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import greendao.DealClaimed;
import greendao.ObjectUser;
import kankan.wheel.widget.WheelView;

/**
 * Created by USER on 7/3/2015.
 */
public class PurchaseDealHistoryFragment extends Fragment {

    private ListView lvPurchaseDeal;
    private PurchaseDealClaimedAdapter purchaseDealAdapter;
    private List<DealClaimed> listDealClaimed;
    private LinearLayout lnlSort;
    private TextView txtLabelSort;
    private TextView txtSort;
    private View progessbarFooter;
    private LayoutInflater layoutInflater;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View headerView;
    private LinearLayout lnlCover;
    private TextView txtCover;

    private int sort;
    private int order;
    private int CLAIMED_DATE = 0;
    private int ALPHABETICAL = 1;
    private int ASC = 0;
    private int DESC = 1;

    private boolean isLoading;
    private int page;
    private int last_page;
    private boolean isPull;

    private List<String> listSort;
    private List<String> listOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ParallaxService.RECEIVER_GET_DEAL_CLAIMED);
            intentFilter.addAction(DealDetailActivity.RECEIVER_CLAIM_SUCCESS);
            getActivity().registerReceiver(activityReceiver, intentFilter);
        }

        sort = CLAIMED_DATE;
        order = DESC;
        isLoading = false;
        page = 1;
        last_page = 0;
        isPull = false;

        listSort = new ArrayList<>();
        listOrder = new ArrayList<>();
        listSort.add("Claimed date");
        listSort.add("Alphabetical");
        listOrder.add(" Ascending");
        listOrder.add(" Descending");

        View view = inflater.inflate(R.layout.fragment_purchase_deal_history, container, false);

        ((BaseActivity) getActivity()).overrideFontsLight(view);

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        lvPurchaseDeal = (ListView) view.findViewById(R.id.lvPurchaseDeal);
        lnlSort = (LinearLayout) view.findViewById(R.id.lnlSort);
        txtLabelSort = (TextView) view.findViewById(R.id.txtLabelSort);
        txtSort = (TextView) view.findViewById(R.id.txtSort);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange_main);

        lnlSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupSort();
            }
        });

        lvPurchaseDeal.setOnScrollListener(new EndScrollListener());

        layoutInflater = LayoutInflater.from(getActivity());
        progessbarFooter = layoutInflater.inflate(R.layout.view_progressbar_loadmore, null);
        headerView = layoutInflater.inflate(R.layout.view_invisible_header, null);

        lvPurchaseDeal.addHeaderView(headerView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                last_page = 0;
                isPull = true;
                getData();
            }
        });

        txtSort.setText(listSort.get(sort) + listOrder.get(order));

        ((BaseActivity) getActivity()).overrideFontsBold(txtLabelSort);

        lnlCover = (LinearLayout) view.findViewById(R.id.lnlCover);
        txtCover = (TextView) view.findViewById(R.id.txtCover);
        lnlCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ((BaseActivity) getActivity()).clickify(txtCover, getString(R.string.no_purchased_deal_click), new ClickSpan.OnClickListener() {
            @Override
            public void onClick() {
                getActivity().finish();
            }
        }, R.color.orange_main);
    }

    private void initData() {
        listDealClaimed = new ArrayList<>();
        List<DealClaimed> list = DealClaimedController.getAll(getActivity());
        listDealClaimed = sortDeals(list, sort, order);

        purchaseDealAdapter = new PurchaseDealClaimedAdapter(getActivity(), listDealClaimed);
        lvPurchaseDeal.setAdapter(purchaseDealAdapter);

        if (listDealClaimed.size() == 0) {
            lnlCover.setVisibility(View.VISIBLE);
        } else {
            lnlCover.setVisibility(View.GONE);
        }

        getData();
    }

    private void getData() {
        ObjectUser user = UserController.getCurrentUser(getActivity());
        Intent intentGetDeal = new Intent(getActivity(), ParallaxService.class);
        intentGetDeal.setAction(ParallaxService.ACTION_GET_DEAL_CLAIMED);
        intentGetDeal.putExtra(ParallaxService.EXTRA_CONSUMER_ID, user.getConsumer_id() + "");
        intentGetDeal.putExtra(ParallaxService.EXTRA_PAGE, page + "");
        intentGetDeal.putExtra(ParallaxService.EXTRA_SORT_FIELD, sort == CLAIMED_DATE ? "consumed_at" : "title");
        intentGetDeal.putExtra(ParallaxService.EXTRA_SORT_DIR, order == ASC ? "up" : "down");
        getActivity().startService(intentGetDeal);
    }

    private List<DealClaimed> sortDeals(List<DealClaimed> listDealAvailable, int sort, int order) {
        if (sort == ALPHABETICAL) {
            if (order == ASC) {
                return sortDealsByAlphabetical(listDealAvailable);
            } else {
                return sortDealsByAlphabeticalDESC(listDealAvailable);
            }
        } else {
            if (order == ASC) {
//
                return sortDealsByDateTime(listDealAvailable);

            } else {
                return sortDealsByDateTimeDESC(listDealAvailable);
            }
        }
    }

    public void showPopupSort() {
        // custom dialog
        final Dialog dialog = new Dialog(getActivity());

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_sort);
        ((BaseActivity) getActivity()).overrideFontsLight(dialog.findViewById(R.id.root));

        WheelSortAdapter timeAdapter1;
        WheelSortAdapter timeAdapter2;

        final WheelView wheel1 = (WheelView) dialog.findViewById(R.id.whl1);
        final WheelView wheel2 = (WheelView) dialog.findViewById(R.id.whl2);
        TextView txtLabelSort = (TextView) dialog.findViewById(R.id.txtLabelSort);
        TextView txtDone = (TextView) dialog.findViewById(R.id.txtDone);
        LinearLayout lnlSpace = (LinearLayout) dialog.findViewById(R.id.lnlSpace);

        ((BaseActivity) getActivity()).overrideFontsBold(txtLabelSort);


        timeAdapter1 = new WheelSortAdapter(getActivity(), listSort);
        wheel1.setVisibleItems(3); // Number of items
        wheel1.setWheelBackground(R.drawable.wheel_bg);
        wheel1.setWheelForeground(R.drawable.wheel_fg);
        wheel1.setShadowColor(0xFFffffff, 0x99ffffff, 0x33ffffff);
        wheel1.setViewAdapter(timeAdapter1);
        wheel1.setCurrentItem(sort);

        timeAdapter2 = new WheelSortAdapter(getActivity(), listOrder);
        wheel2.setVisibleItems(3); // Number of items
        wheel2.setWheelBackground(R.drawable.wheel_bg);
        wheel2.setWheelForeground(R.drawable.wheel_fg);
        wheel2.setShadowColor(0xFFffffff, 0x99ffffff, 0x33ffffff);
        wheel2.setViewAdapter(timeAdapter2);
        wheel2.setCurrentItem(order);

        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort = wheel1.getCurrentItem();
                order = wheel2.getCurrentItem();
                txtSort.setText(listSort.get(sort) + listOrder.get(order));
                page = 1;
                last_page = 0;
                getData();
                ((BaseActivity) getActivity()).showProgressDialog();
                dialog.dismiss();
            }
        });

        lnlSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private List<DealClaimed> sortDealsByAlphabetical(List<DealClaimed> listDealAvailable) {
        Collections.sort(listDealAvailable, new Comparator<DealClaimed>() {
            @Override
            public int compare(DealClaimed object1, DealClaimed object2) {
                return object1.getTitle().compareTo(object2.getTitle());
            }
        });

        return listDealAvailable;
    }

    private List<DealClaimed> sortDealsByAlphabeticalDESC(List<DealClaimed> listDealAvailable) {
        Collections.sort(listDealAvailable, new Comparator<DealClaimed>() {
            @Override
            public int compare(DealClaimed object1, DealClaimed object2) {
                return object2.getTitle().compareTo(object1.getTitle());
            }
        });

        return listDealAvailable;
    }

    private List<DealClaimed> sortDealsByDateTime(List<DealClaimed> listDealAvailable) {
        Collections.sort(listDealAvailable, new Comparator<DealClaimed>() {
            @Override
            public int compare(DealClaimed object1, DealClaimed object2) {
                String date1 = "";
                String date2 = "";
                if (object1.getF_claimed()) {
                    date1 = object1.getConsumed_at();
                } else {
                    date1 = object1.getValidity();
                }
                if (object2.getF_claimed()) {
                    date2 = object2.getConsumed_at();
                } else {
                    date2 = object2.getValidity();
                }
                return date1.compareTo(date2);
            }
        });

        return listDealAvailable;
    }

    private List<DealClaimed> sortDealsByDateTimeDESC(List<DealClaimed> listDealAvailable) {
        Collections.sort(listDealAvailable, new Comparator<DealClaimed>() {
            @Override
            public int compare(DealClaimed object1, DealClaimed object2) {
                String date1 = "";
                String date2 = "";
                if (object1.getF_claimed()) {
                    date1 = object1.getConsumed_at();
                } else {
                    date1 = object1.getValidity();
                }
                if (object2.getF_claimed()) {
                    date2 = object2.getConsumed_at();
                } else {
                    date2 = object2.getValidity();
                }
                return date2.compareTo(date1);
            }
        });

        return listDealAvailable;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            getActivity().unregisterReceiver(activityReceiver);
        }
    }

    private void setListData() {
        listDealClaimed.clear();
        List<DealClaimed> list = DealClaimedController.getAll(getActivity());
        listDealClaimed = sortDeals(list, sort, order);

        purchaseDealAdapter.setListData(listDealClaimed);

        if (listDealClaimed.size() == 0) {
            lnlCover.setVisibility(View.VISIBLE);
        } else {
            lnlCover.setVisibility(View.GONE);
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(ParallaxService.RECEIVER_GET_DEAL_CLAIMED)) {
                String result = intent.getStringExtra(ParallaxService.EXTRA_RESULT);
                if (result.equals(ParallaxService.RESULT_OK)) {
                    String lastpage = intent.getStringExtra(RealTimeService.EXTRA_RESULT_LAST_PAGE);
                    last_page = Integer.parseInt(lastpage);
                    String currentpage = intent.getStringExtra(RealTimeService.EXTRA_RESULT_CURRENT_PAGE);
                    page = Integer.parseInt(currentpage);
                    setListData();
                    isLoading = false;
                    if (page == 1) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (lvPurchaseDeal.getFirstVisiblePosition() == 0 && lvPurchaseDeal.getLastVisiblePosition() == lvPurchaseDeal.getCount() - 1) {
                                    isLoading = true;
                                    if (page < last_page) {
                                        page++;
                                        getData();
                                        lvPurchaseDeal.addFooterView(progessbarFooter);
                                    }
                                }
                            }
                        }, 500);
                    }
                } else if (result.equals(ParallaxService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(ParallaxService.EXTRA_RESULT_MESSAGE);
                    ((BaseActivity) getActivity()).showToastError(message);
                } else if (result.equals(ParallaxService.RESULT_NO_INTERNET)) {
                    ((BaseActivity) getActivity()).showToastError(getString(R.string.nointernet));
                }
                lvPurchaseDeal.removeFooterView(progessbarFooter);
                ((BaseActivity) getActivity()).hideCustomProgressDialog();
            } else if (intent.getAction().equalsIgnoreCase(DealDetailActivity.RECEIVER_CLAIM_SUCCESS)) {
                page = 1;
                last_page = 0;
                getData();
            }
            if (isPull) {
                isPull = false;
                swipeRefreshLayout.setRefreshing(false);
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
                            getData();
                            lvPurchaseDeal.addFooterView(progessbarFooter);
                        }
                    }
                }
            }
        }
    }
}
