package com.dibs.dibly.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.adapter.FollowingAdapter;
import com.dibs.dibly.adapter.WheelSortAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.FollowingController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.RealTimeService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import greendao.Following;
import greendao.ObjectUser;
import kankan.wheel.widget.WheelView;

/**
 * Created by USER on 6/29/2015.
 */
public class FollowingActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private RelativeLayout rltSearch;

    private ListView lvFollowing;
    private FollowingAdapter followingAdapter;

    private LinearLayout lnlSort;
    private TextView txtLabelSort;
    private TextView txtSort;
    private int sort_1;
    private int sort_2;
    private String txt_sort;

    private List<Following> listFollowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_merchant);

        overrideFontsLight(findViewById(R.id.root));

        txt_sort = "Alphabetical Ascending";
        sort_1 = 0;
        sort_2 = 0;

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_FOLLOWING);
            intentFilter.addAction(RealTimeService.RECEIVER_STOP_FOLLOWING_MERCHANT);
            intentFilter.addAction(RealTimeService.RECEIVER_FOLLOWING_MERCHANT);
            registerReceiver(activityReceiver, intentFilter);
        }

        initView();
        initData();
    }

    private void notifyListData() {
        listFollowing.clear();
        List<Following> listData = FollowingController.getAll(FollowingActivity.this);
        listFollowing = sortDeals(listData, txt_sort);
        followingAdapter.setData(listFollowing);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            unregisterReceiver(activityReceiver);
        }
    }

    private void initView() {
        lvFollowing = (ListView) findViewById(R.id.lvFollowing);
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        rltSearch = (RelativeLayout) findViewById(R.id.rltSearch);

        rltBack.setOnClickListener(this);
        rltSearch.setOnClickListener(this);

        lnlSort = (LinearLayout) findViewById(R.id.lnlSort);
        txtLabelSort = (TextView) findViewById(R.id.txtLabelSort);
        txtSort = (TextView) findViewById(R.id.txtSort);

        lnlSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupSort();
            }
        });

        txtSort.setText(txt_sort);
        overrideFontsBold(txtLabelSort);
    }

    private void initData() {
        listFollowing = new ArrayList<>();
        followingAdapter = new FollowingAdapter(FollowingActivity.this, listFollowing);
        lvFollowing.setAdapter(followingAdapter);

        getData();
        showProgressDialog();
    }

    private void getData() {
        if (UserController.isLogin(FollowingActivity.this)) {
            ObjectUser user = UserController.getCurrentUser(FollowingActivity.this);
            Intent intentGetComment = new Intent(FollowingActivity.this, RealTimeService.class);
            intentGetComment.setAction(RealTimeService.ACTION_GET_FOLLOWING);
            intentGetComment.putExtra(RealTimeService.EXTRA_CONSUMER_ID, user.getConsumer_id() + "");
            startService(intentGetComment);
        }
    }

    private List<Following> sortDeals(List<Following> listFollow, String type) {
        if (type.equalsIgnoreCase("Alphabetical Ascending")) {
            return sortDealsByAlphabetical(listFollow);
        } else if (type.equalsIgnoreCase("Num of new deals Ascending")) {
            return sortDealsByNumDeal(listFollow);
        } else if (type.equalsIgnoreCase("Alphabetical Descending")) {
            return sortDealsByAlphabeticalDESC(listFollow);
        } else if (type.equalsIgnoreCase("Num of new deals Descending")) {
            return sortDealsByNumDealDESC(listFollow);
        } else {
            return listFollow;
        }

    }

    public void showPopupSort() {
        // custom dialog
        final Dialog dialog = new Dialog(FollowingActivity.this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_sort);
        overrideFontsLight(dialog.findViewById(R.id.root));

        final List<String> list1 = new ArrayList<String>();
        final List<String> list2 = new ArrayList<String>();

        list1.add("Alphabetical");
        list1.add("Num of new deals");
        list2.add("Ascending");
        list2.add("Descending");

        WheelSortAdapter timeAdapter1;
        WheelSortAdapter timeAdapter2;

        final WheelView wheel1 = (WheelView) dialog.findViewById(R.id.whl1);
        final WheelView wheel2 = (WheelView) dialog.findViewById(R.id.whl2);
        TextView txtLabelSort = (TextView) dialog.findViewById(R.id.txtLabelSort);
        TextView txtDone = (TextView) dialog.findViewById(R.id.txtDone);
        LinearLayout lnlSpace = (LinearLayout) dialog.findViewById(R.id.lnlSpace);

        overrideFontsBold(txtLabelSort);


        timeAdapter1 = new WheelSortAdapter(FollowingActivity.this, list1);
        wheel1.setVisibleItems(3); // Number of items
        wheel1.setWheelBackground(R.drawable.wheel_bg);
        wheel1.setWheelForeground(R.drawable.wheel_fg);
        wheel1.setShadowColor(0xFFffffff, 0x99ffffff, 0x33ffffff);
        wheel1.setViewAdapter(timeAdapter1);
        wheel1.setCurrentItem(sort_1);

        timeAdapter2 = new WheelSortAdapter(FollowingActivity.this, list2);
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
                List<Following> listDa = sortDeals(listFollowing, sort);
                listFollowing = listDa;
                followingAdapter.setData(listFollowing);
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


    private List<Following> sortDealsByAlphabetical(List<Following> listFollow) {


        Collections.sort(listFollow, new Comparator<Following>() {
            @Override
            public int compare(Following object1, Following object2) {
                return object1.getMerchant_name().compareTo(object2.getMerchant_name());
            }
        });

        return listFollow;

    }


    private List<Following> sortDealsByAlphabeticalDESC(List<Following> listFollow) {


        Collections.sort(listFollow, new Comparator<Following>() {
            @Override
            public int compare(Following object1, Following object2) {
                return object2.getMerchant_name().compareTo(object1.getMerchant_name());
            }
        });

        return listFollow;

    }

    private List<Following> sortDealsByNumDeal(List<Following> listFollow) {
        Collections.sort(listFollow, new Comparator<Following>() {
            @Override
            public int compare(Following object1, Following object2) {
                return object1.getNum_of_new_deals() - object2.getNum_of_new_deals();
            }
        });

        return listFollow;
    }

    private List<Following> sortDealsByNumDealDESC(List<Following> listFollow) {
        Collections.sort(listFollow, new Comparator<Following>() {
            @Override
            public int compare(Following object1, Following object2) {
                return object2.getNum_of_new_deals() - object1.getNum_of_new_deals();
            }
        });

        return listFollow;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.rltSearch:
                Intent intentSearch = new Intent(FollowingActivity.this, SearchActivity.class);
                startActivity(intentSearch);
                break;
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            hideProgressDialog();
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_FOLLOWING)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    notifyListData();
                    hideProgressDialog();
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_STOP_FOLLOWING_MERCHANT)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    getData();
                }
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_FOLLOWING_MERCHANT)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    getData();
                }
            }
        }
    };
}
