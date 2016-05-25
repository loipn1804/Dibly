package com.dibs.dibly.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.RealTimeService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by USER on 7/13/2015.
 */
public class MerchantScannerSuccessActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private Button btnScanOther;
    private TextView txtPrompt;
    private LinearLayout lnlTitle;
    private TextView txtTitle;

    private JSONObject data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_success);

        overrideFontsLight(findViewById(R.id.root));

        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        rltBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_CLAIMED);
            registerReceiver(activityReceiver, intentFilter);
        }

        if (getIntent().getExtras() != null) {
            String str_data = getIntent().getStringExtra("data");
            try {
                data = new JSONObject(str_data);
                Intent intentClaimed = new Intent(MerchantScannerSuccessActivity.this, RealTimeService.class);
                intentClaimed.setAction(RealTimeService.ACTION_CLAIMED);
                intentClaimed.putExtra(RealTimeService.EXTRA_UUID, data.getString("uuid"));
                intentClaimed.putExtra(RealTimeService.EXTRA_DEAL_ID, data.getString("deal_id"));
                intentClaimed.putExtra(RealTimeService.EXTRA_CONSUMER_ID, data.getString("consumer_id"));
                intentClaimed.putExtra(RealTimeService.EXTRA_MERCHANT_ID, UserController.getCurrentUser(this).getConsumer_id() + "");
                startService(intentClaimed);
                showProgressDialog();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        initView();
        initData();
    }

    public void initView() {
        btnScanOther = (Button) findViewById(R.id.btnScanOther);
        txtPrompt = (TextView) findViewById(R.id.txtPrompt);
        lnlTitle = (LinearLayout) findViewById(R.id.lnlTitle);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        btnScanOther.setOnClickListener(this);
    }

    private void initData() {
        txtPrompt.setVisibility(View.INVISIBLE);
        lnlTitle.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScanOther:
                Intent intentFollowing = new Intent(MerchantScannerSuccessActivity.this, MerchantScannerActivity.class);
                startActivity(intentFollowing);
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            this.unregisterReceiver(activityReceiver);
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_CLAIMED)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    txtPrompt.setText(message);
                    txtPrompt.setVisibility(View.VISIBLE);

//                    try {
//                        JSONObject jsonObject = new JSONObject(data);
//
//                        JSONObject deal = jsonObject.getJSONObject("deal");
//                        txtTitle.setText(deal.getString("title"));
//
//                        txtPrompt.setVisibility(View.VISIBLE);
//                        lnlTitle.setVisibility(View.VISIBLE);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

                    hideProgressDialog();
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    txtPrompt.setText(message);
                    txtPrompt.setVisibility(View.VISIBLE);
                    hideProgressDialog();
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                    hideProgressDialog();
                }
            }
        }
    };
}
