package com.dibs.dibly.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.activity.DealDetailActivity;
import com.dibs.dibly.adapter.PurchaseDealAvailableAdapter;
import com.dibs.dibly.adapter.WheelSortAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.DealAvailableController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.ClickSpan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import greendao.DealAvailable;
import greendao.ObjectUser;
import kankan.wheel.widget.WheelView;

/**
 * Created by USER on 7/3/2015.
 */
public class PurchaseDealAvailableFragment extends Fragment {

    private ListView lvPurchaseDeal;
    private PurchaseDealAvailableAdapter purchaseDealAdapter;
    private List<DealAvailable> listDealAvailable;
    private SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayout lnlSort;
    private TextView txtLabelSort;
    private TextView txtSort;
    private LinearLayout lnlCover;
    private TextView txtCover;

    private int sort_1;
    private int sort_2;
    private String txt_sort;

    private boolean isLoading;
    private boolean isPull;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_DEAL_AVAILABLE);
            intentFilter.addAction(DealDetailActivity.RECEIVER_CLAIM_SUCCESS);
            getActivity().registerReceiver(activityReceiver, intentFilter);
        }

        txt_sort = "Expiry date Descending";
        sort_1 = 0;
        sort_2 = 1;
        isLoading = false;
        isPull = false;

        View view = inflater.inflate(R.layout.fragment_purchase_deal_available, container, false);

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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isPull = true;
                getData();
            }
        });

        txtSort.setText(txt_sort);

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
        listDealAvailable = new ArrayList<>();
        List<DealAvailable> list = DealAvailableController.getAll(getActivity());
        listDealAvailable = sortDeals(list, txt_sort);

        purchaseDealAdapter = new PurchaseDealAvailableAdapter(getActivity(), listDealAvailable, true);
        lvPurchaseDeal.setAdapter(purchaseDealAdapter);

        if (listDealAvailable.size() == 0) {
            lnlCover.setVisibility(View.VISIBLE);
        } else {
            lnlCover.setVisibility(View.GONE);
        }

        getData();
        ((BaseActivity) getActivity()).showCustomProgressDialog();
    }

    private void getData() {
        ObjectUser user = UserController.getCurrentUser(getActivity());
        Intent intentGetDeal = new Intent(getActivity(), RealTimeService.class);
        intentGetDeal.setAction(RealTimeService.ACTION_GET_DEAL_AVAILABLE);
        intentGetDeal.putExtra(RealTimeService.EXTRA_CONSUMER_ID, user.getConsumer_id() + "");
        getActivity().startService(intentGetDeal);
    }

    private List<DealAvailable> sortDeals(List<DealAvailable> listDealAvailable, String type) {
        if (type.equalsIgnoreCase("Alphabetical Ascending")) {
            return sortDealsByAlphabetical(listDealAvailable);
        } else if (type.equalsIgnoreCase("Expiry date Ascending")) {
            return sortDealsByDateTime(listDealAvailable);
        } else if (type.equalsIgnoreCase("Alphabetical Descending")) {
            return sortDealsByAlphabeticalDESC(listDealAvailable);
        } else if (type.equalsIgnoreCase("Expiry date Descending")) {
            return sortDealsByDateTimeDESC(listDealAvailable);
        } else {
            return listDealAvailable;
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

        final List<String> list1 = new ArrayList<String>();
        final List<String> list2 = new ArrayList<String>();

        list1.add("Expiry date");
        list1.add("Alphabetical");
        list2.add("Ascending");
        list2.add("Descending");

        WheelSortAdapter timeAdapter1;
        WheelSortAdapter timeAdapter2;

        final WheelView wheel1 = (WheelView) dialog.findViewById(R.id.whl1);
        final WheelView wheel2 = (WheelView) dialog.findViewById(R.id.whl2);
        TextView txtLabelSort = (TextView) dialog.findViewById(R.id.txtLabelSort);
        TextView txtDone = (TextView) dialog.findViewById(R.id.txtDone);
        LinearLayout lnlSpace = (LinearLayout) dialog.findViewById(R.id.lnlSpace);

        ((BaseActivity) getActivity()).overrideFontsBold(txtLabelSort);

        timeAdapter1 = new WheelSortAdapter(getActivity(), list1);
        wheel1.setVisibleItems(3); // Number of items
        wheel1.setWheelBackground(R.drawable.wheel_bg);
        wheel1.setWheelForeground(R.drawable.wheel_fg);
        wheel1.setShadowColor(0xFFffffff, 0x99ffffff, 0x33ffffff);
        wheel1.setViewAdapter(timeAdapter1);
        wheel1.setCurrentItem(sort_1);

        timeAdapter2 = new WheelSortAdapter(getActivity(), list2);
        wheel2.setVisibleItems(3); // Number of items
        wheel2.setWheelBackground(R.drawable.wheel_bg);
        wheel2.setWheelForeground(R.drawable.wheel_fg);
        wheel2.setShadowColor(0xFFffffff, 0x99ffffff, 0x33ffffff);
        wheel2.setViewAdapter(timeAdapter2);
        wheel2.setCurrentItem(sort_2);

        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort_1 = wheel1.getCurrentItem();
                sort_2 = wheel2.getCurrentItem();
                String sort = list1.get(wheel1.getCurrentItem()) + " " + list2.get(wheel2.getCurrentItem());
                txt_sort = sort;
                listDealAvailable = sortDeals(listDealAvailable, sort);
                purchaseDealAdapter.setListData(listDealAvailable);
                txtSort.setText(sort);
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


    private List<DealAvailable> sortDealsByAlphabetical(List<DealAvailable> listDealAvailable) {
        Collections.sort(listDealAvailable, new Comparator<DealAvailable>() {
            @Override
            public int compare(DealAvailable object1, DealAvailable object2) {
                return object1.getTitle().compareTo(object2.getTitle());
            }
        });

        return listDealAvailable;
    }


    private List<DealAvailable> sortDealsByAlphabeticalDESC(List<DealAvailable> listDealAvailable) {
        Collections.sort(listDealAvailable, new Comparator<DealAvailable>() {
            @Override
            public int compare(DealAvailable object1, DealAvailable object2) {
                return object2.getTitle().compareTo(object1.getTitle());
            }
        });

        return listDealAvailable;
    }

    private List<DealAvailable> sortDealsByDateTime(List<DealAvailable> listDealAvailable) {
        Collections.sort(listDealAvailable, new Comparator<DealAvailable>() {
            @Override
            public int compare(DealAvailable object1, DealAvailable object2) {
                return object1.getValidity().compareTo(object2.getValidity());
            }
        });

        return listDealAvailable;
    }

    private List<DealAvailable> sortDealsByDateTimeDESC(List<DealAvailable> listDealAvailable) {
        Collections.sort(listDealAvailable, new Comparator<DealAvailable>() {
            @Override
            public int compare(DealAvailable object1, DealAvailable object2) {
                return object2.getValidity().compareTo(object1.getValidity());
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

    private void notifyListData() {
        listDealAvailable.clear();
        List<DealAvailable> list = DealAvailableController.getAll(getActivity());
        listDealAvailable = sortDeals(list, txt_sort);

        purchaseDealAdapter.setListData(listDealAvailable);
        if (listDealAvailable.size() == 0) {
            lnlCover.setVisibility(View.VISIBLE);
        } else {
            lnlCover.setVisibility(View.GONE);
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_DEAL_AVAILABLE)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    notifyListData();
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    ((BaseActivity) getActivity()).showToastError(message);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    ((BaseActivity) getActivity()).showToastError(getString(R.string.nointernet));
                }
                ((BaseActivity) getActivity()).hideCustomProgressDialog();
            } else if (intent.getAction().equalsIgnoreCase(DealDetailActivity.RECEIVER_CLAIM_SUCCESS)) {
                getData();
            }
            if (isPull) {
                isPull = false;
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    };
}
