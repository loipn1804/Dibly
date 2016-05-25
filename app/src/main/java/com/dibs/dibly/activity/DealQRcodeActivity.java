package com.dibs.dibly.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.DealAvailableController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunc;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.google.zxing.BarcodeFormat;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import greendao.DealAvailable;
import greendao.ObjectUser;

/**
 * Created by USER on 6/29/2015.
 */
public class DealQRcodeActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private TextView txtTypeDeal;
    private TextView txtEndAt;
    private TextView txtTitle;
    private TextView txtMerchant;
    private ImageView imvQRCode;
    private LinearLayout lnlSecretCode;
    private TextView txtSwitch;
    private long deal_id;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private EditText edtSecretCode;
    private Button btnClaimNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        overrideFontsLight(findViewById(R.id.root));

        options = new DisplayImageOptions.Builder().cacheInMemory(false)
                .showImageForEmptyUri(R.color.white)
                .showImageOnLoading(R.color.white)
                .cacheOnDisc(true).build();

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_DEAL_AVAILABLE_BY_DEAL_ID);
            intentFilter.addAction(RealTimeService.RECEIVER_CLAIMED);
            registerReceiver(activityReceiver, intentFilter);
        }

        initView();
        initData();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTypeDeal = (TextView) findViewById(R.id.txtTypeDeal);
        txtEndAt = (TextView) findViewById(R.id.txtEndAt);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtMerchant = (TextView) findViewById(R.id.txtMerchant);
        imvQRCode = (ImageView) findViewById(R.id.imvQRCode);
        edtSecretCode = (EditText) findViewById(R.id.edtSecretCode);
        btnClaimNow = (Button) findViewById(R.id.btnClaimNow);
        lnlSecretCode = (LinearLayout) findViewById(R.id.lnlSecretCode);
        txtSwitch = (TextView) findViewById(R.id.txtSwitch);

        rltBack.setOnClickListener(this);
        btnClaimNow.setOnClickListener(this);
        txtSwitch.setOnClickListener(this);
    }

    private void initData() {
        if (getIntent().hasExtra("deal_id")) {
            deal_id = getIntent().getLongExtra("deal_id", 0);
            DealAvailable dealAvailable = DealAvailableController.getById(DealQRcodeActivity.this, deal_id);
            if (dealAvailable != null) {
                setupQRCode(dealAvailable);
            } else {
                if (UserController.isLogin(DealQRcodeActivity.this)) {
                    ObjectUser user = UserController.getCurrentUser(DealQRcodeActivity.this);
                    Intent intentGetDeal = new Intent(DealQRcodeActivity.this, RealTimeService.class);
                    intentGetDeal.setAction(RealTimeService.ACTION_GET_DEAL_AVAILABLE_BY_DEAL_ID);
                    intentGetDeal.putExtra(RealTimeService.EXTRA_CONSUMER_ID, user.getConsumer_id() + "");
                    intentGetDeal.putExtra(RealTimeService.EXTRA_DEAL_ID, deal_id + "");
                    startService(intentGetDeal);
                    showCustomProgressDialog();
                }
            }
        } else {
            finish();
        }
    }

    private void setupQRCode(DealAvailable dealAvailable) {
        if (dealAvailable.getType().equalsIgnoreCase("instore")) {
            txtTypeDeal.setText("This is an In Store Deal");
        } else {
            txtTypeDeal.setText("This is an In App Deal");
        }
        txtEndAt.setText("and is valid up till " + dealAvailable.getEnd_at());
        txtTitle.setText(dealAvailable.getTitle());
        txtMerchant.setText(dealAvailable.getOrganization_name());
        try {
            JSONObject jobj_uuid = new JSONObject();
            jobj_uuid.putOpt("uuid", dealAvailable.getUuid());
            jobj_uuid.putOpt("deal_id", dealAvailable.getDeal_id() + "");
            ObjectUser user = UserController.getCurrentUser(DealQRcodeActivity.this);
            jobj_uuid.putOpt("consumer_id", user.getConsumer_id() + "");
            jobj_uuid.putOpt("title", dealAvailable.getTitle());
            jobj_uuid.putOpt("merchant_id", dealAvailable.getMerchant_id() + "");
            imvQRCode.setImageBitmap(StaticFunc.generateBarcodeBitmap(jobj_uuid.toString(), BarcodeFormat.QR_CODE, StaticFunction.getScreenWidth(DealQRcodeActivity.this) / 2, StaticFunction.getScreenWidth(DealQRcodeActivity.this) / 2));
        } catch (Exception e) {
            e.printStackTrace();
        }
        imvQRCode.setVisibility(View.VISIBLE);
        lnlSecretCode.setVisibility(View.INVISIBLE);
        txtSwitch.setText(getResources().getString(R.string.claim_code));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            unregisterReceiver(activityReceiver);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.btnClaimNow:
                DoClaimNow();
                break;
            case R.id.txtSwitch:
                if (imvQRCode.getVisibility() == View.VISIBLE) {
                    imvQRCode.setVisibility(View.INVISIBLE);
                    lnlSecretCode.setVisibility(View.VISIBLE);
                    txtSwitch.setText(getResources().getString(R.string.claim_qr));
                } else {
                    imvQRCode.setVisibility(View.VISIBLE);
                    lnlSecretCode.setVisibility(View.INVISIBLE);
                    txtSwitch.setText(getResources().getString(R.string.claim_code));
                }
                break;
        }
    }

    private void DoClaimNow() {
        DealAvailable dealAvailable = DealAvailableController.getById(DealQRcodeActivity.this, deal_id);
        String secret_code = edtSecretCode.getText().toString().trim();
        String[] outlet_codes = dealAvailable.getSecret_code().split("/");
        boolean isExist = false;
        for (int i = 0; i < outlet_codes.length; i++) {
            if (outlet_codes[i].length() > 0) {
                if (outlet_codes[i].equals(secret_code)) {
                    isExist = true;
                    break;
                }
            }
        }
        if (isExist) {
            Intent intentClaimed = new Intent(this, RealTimeService.class);
            intentClaimed.setAction(RealTimeService.ACTION_CLAIMED);
            intentClaimed.putExtra(RealTimeService.EXTRA_UUID, dealAvailable.getUuid());
            intentClaimed.putExtra(RealTimeService.EXTRA_DEAL_ID, dealAvailable.getDeal_id() + "");
            intentClaimed.putExtra(RealTimeService.EXTRA_CONSUMER_ID, UserController.getCurrentUser(this).getConsumer_id() + "");
            intentClaimed.putExtra(RealTimeService.EXTRA_MERCHANT_ID, dealAvailable.getMerchant_id() + "");
            startService(intentClaimed);
            showProgressDialog();
        } else {
            showToastError("Incorrect code");
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_DEAL_AVAILABLE_BY_DEAL_ID)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    DealAvailable dealAvailable = DealAvailableController.getById(DealQRcodeActivity.this, deal_id);
                    if (dealAvailable != null) {
                        setupQRCode(dealAvailable);
                    } else {
                        finish();
                    }
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {

                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
                hideCustomProgressDialog();
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_CLAIMED)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastOk(message);
                    finish();
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
                hideCustomProgressDialog();
            }
        }
    };
}
