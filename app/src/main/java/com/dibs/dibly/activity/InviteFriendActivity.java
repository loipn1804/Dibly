package com.dibs.dibly.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.adapter.DiscountAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.DiscountController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.ParallaxService;
import com.dibs.dibly.service.RealTimeService;

import java.util.ArrayList;
import java.util.List;

import greendao.Discount;

/**
 * Created by USER on 6/29/2015.
 */
public class InviteFriendActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private TextView txtTitle;
    private TextView txtInviteCode;
    private ImageView imvFacebook;
    private ImageView imvTwitter;
    private ImageView imvInstagram;
    private TextView txtNumDiscount;
    private ListView lvData;
    private DiscountAdapter discountAdapter;
    private List<Discount> discountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);

        overrideFontsLight(findViewById(R.id.root));

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_DISCOUNT_AVAILABLE);
            intentFilter.addAction(ParallaxService.RECEIVER_GET_INVITE_CODE);
            registerReceiver(activityReceiver, intentFilter);
        }

        initView();
        initData();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtInviteCode = (TextView) findViewById(R.id.txtInviteCode);
        imvFacebook = (ImageView) findViewById(R.id.imvFacebook);
        imvTwitter = (ImageView) findViewById(R.id.imvTwitter);
        imvInstagram = (ImageView) findViewById(R.id.imvInstagram);
        txtNumDiscount = (TextView) findViewById(R.id.txtNumDiscount);
        lvData = (ListView) findViewById(R.id.lvData);

        overrideFontsBold(txtTitle);

        rltBack.setOnClickListener(this);
        imvFacebook.setOnClickListener(this);
        imvTwitter.setOnClickListener(this);
        imvInstagram.setOnClickListener(this);
    }

    private void initData() {
        discountList = new ArrayList<>();
        discountAdapter = new DiscountAdapter(this, discountList);
        lvData.setAdapter(discountAdapter);

        getData();
        showProgressDialog();

        SharedPreferences preferences = getSharedPreferences("invite", MODE_PRIVATE);
        String invite_code = preferences.getString("invite_code", "");
        if (invite_code.length() == 0) {
            Intent intentGetInviteCode = new Intent(this, ParallaxService.class);
            intentGetInviteCode.setAction(ParallaxService.ACTION_GET_INVITE_CODE);
            startService(intentGetInviteCode);
        } else {
            txtInviteCode.setText(invite_code);
        }
    }

    private void notifyListData() {
        discountList.clear();
        discountList = DiscountController.getAll(this);
        discountAdapter.setData(discountList);

        int size = discountList.size();
        if (size == 1) {
            txtNumDiscount.setText("You have 1 discount token available for use.");
        } else {
            txtNumDiscount.setText("You have " + size + " discount tokens available for use.");
        }
    }

    private void getData() {
        Intent intent = new Intent(this, RealTimeService.class);
        intent.setAction(RealTimeService.ACTION_GET_DISCOUNT_AVAILABLE);
        intent.putExtra(ParallaxService.EXTRA_CONSUMER_ID, UserController.getCurrentUser(this).getConsumer_id() + "");
        startService(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            unregisterReceiver(activityReceiver);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.imvFacebook:

                break;
            case R.id.imvTwitter:

                break;
            case R.id.imvInstagram:

                break;
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_DISCOUNT_AVAILABLE)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    notifyListData();
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
            } else if (intent.getAction().equalsIgnoreCase(ParallaxService.RECEIVER_GET_INVITE_CODE)) {
                String result = intent.getStringExtra(ParallaxService.EXTRA_RESULT);
                if (result.equals(ParallaxService.RESULT_OK)) {
                    SharedPreferences preferences = getSharedPreferences("invite", MODE_PRIVATE);
                    String invite_code = preferences.getString("invite_code", "");
                    txtInviteCode.setText(invite_code);
                } else if (result.equals(ParallaxService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(ParallaxService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                } else if (result.equals(ParallaxService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
            }
            hideCustomProgressDialog();
        }
    };
}
