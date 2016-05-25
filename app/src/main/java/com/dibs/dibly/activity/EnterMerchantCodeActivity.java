package com.dibs.dibly.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.consts.Const;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.OutletController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunc;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.List;

import greendao.ObjectDeal;
import greendao.ObjectUser;
import greendao.Outlet;

/**
 * Created by USER on 1/8/2016.
 */
public class EnterMerchantCodeActivity extends BaseActivity implements View.OnClickListener {

    private TextView txtNameActionBar;
    private RelativeLayout rltBack;
    private TextView txtEndAt;
    private TextView txtTitle;
    private TextView txtMerchant;
    private long deal_id, outletId;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private EditText edtSecretCode;
    private RelativeLayout btnClaimNow;
    private boolean isYay = false;
    private boolean isClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_merchant_code);
        initialToolBar();
        overrideFontsLight(findViewById(R.id.root));

        options = new DisplayImageOptions.Builder().cacheInMemory(false)
                .showImageForEmptyUri(R.color.white)
                .showImageOnLoading(R.color.white)
                .cacheInMemory(true).build();

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_DEAL_AVAILABLE_BY_DEAL_ID);
            intentFilter.addAction(RealTimeService.RECEIVER_CLAIMED);
            intentFilter.addAction(RealTimeService.RECEIVER_LIKE_DEAL);
            intentFilter.addAction(RealTimeService.RECEIVER_UNLIKE_DEAL);
            registerReceiver(activityReceiver, intentFilter);
        }

        initView();
        initData();

    }

    private void initView() {
        txtNameActionBar = (TextView) findViewById(R.id.txtNameActionBar);
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtEndAt = (TextView) findViewById(R.id.txtEndAt);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtMerchant = (TextView) findViewById(R.id.txtMerchant);
        edtSecretCode = (EditText) findViewById(R.id.edtSecretCode);
        btnClaimNow = (RelativeLayout) findViewById(R.id.btnClaimNow);

        rltBack.setOnClickListener(this);
        btnClaimNow.setOnClickListener(this);

        overrideFontsBold(txtTitle);
        overrideFontsBold(txtMerchant);
        overrideFontsBold(txtEndAt);
    }

    private void initData() {
        if (getIntent().hasExtra("deal_id")) {
            deal_id = getIntent().getLongExtra("deal_id", 0);
            outletId = getIntent().getLongExtra("outlet_id", 0);
            ObjectDeal objectDeal = DealController.getDealByDealIdAndDealType(this, deal_id, Const.DEALTYPE.DEAL_AVAILABLE);
            if (objectDeal != null) {
                setupQRCode(objectDeal);
            } else {
                if (UserController.isLogin(EnterMerchantCodeActivity.this)) {
                    ObjectUser user = UserController.getCurrentUser(EnterMerchantCodeActivity.this);
                    Intent intentGetDeal = new Intent(EnterMerchantCodeActivity.this, RealTimeService.class);
                    intentGetDeal.setAction(RealTimeService.ACTION_GET_DEAL_AVAILABLE_BY_DEAL_ID);
                    intentGetDeal.putExtra(RealTimeService.EXTRA_CONSUMER_ID, user.getConsumer_id() + "");
                    intentGetDeal.putExtra(RealTimeService.EXTRA_DEAL_ID, deal_id + "");
                    intentGetDeal.putExtra(RealTimeService.EXTRA_OUTLET_ID, outletId + "");
                    startService(intentGetDeal);
                    showCustomProgressDialog();
                }
            }
        } else {
            finish();
        }
    }

    private void setupQRCode(ObjectDeal dealAvailable) {

        String message = "You've called dibs for ";

        try {
            SpannableString text2 = new SpannableString(message + dealAvailable.getTitle());
            text2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.txt_black_68)), 0,
                    message.length(), 0);
            text2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange_main)), message.length(),
                    text2.length() - 1, 0);

            txtTitle.setText(text2, TextView.BufferType.SPANNABLE);
            txtMerchant.setText(dealAvailable.getOrganization_name());
        } catch (Exception e) {

        }
        txtEndAt.setText(StaticFunction.parseDateToddMMyyyy(dealAvailable.getEnd_at()));
        //  txtNameActionBar.setText(dealAvailable.getTitle());
        try {
            JSONObject jobj_uuid = new JSONObject();
            jobj_uuid.putOpt("uuid", dealAvailable.getUuid());
            jobj_uuid.putOpt("deal_id", dealAvailable.getDeal_id() + "");
            ObjectUser user = UserController.getCurrentUser(EnterMerchantCodeActivity.this);
            jobj_uuid.putOpt("consumer_id", user.getConsumer_id() + "");
            jobj_uuid.putOpt("title", dealAvailable.getTitle());
            jobj_uuid.putOpt("merchant_id", dealAvailable.getMerchant_id() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        }
    }

    private void DoClaimNow() {

        ObjectDeal objectDeal = DealController.getDealByDealIdAndDealType(this, deal_id, outletId, Const.DEALTYPE.DEAL_AVAILABLE);
        String secret_code = edtSecretCode.getText().toString().trim();
        boolean isExist = false;
        if (objectDeal.getOutlets() != null && objectDeal.getOutlets().trim().length() > 0) {
            List<Outlet> objectDealOutlets = OutletController.getOutletsByDealOutlets(this, objectDeal.getOutlets());
            for (Outlet outlet : objectDealOutlets) {
                if (outlet.getSecret_code().length() > 0) {
                    if (outlet.getSecret_code().equals(secret_code)&&outlet.getOutlet_id()==outletId) {
                        isExist = true;
                        break;
                    }
                }
            }
        }

        if (isExist) {
            Intent intentClaimed = new Intent(this, RealTimeService.class);
            intentClaimed.setAction(RealTimeService.ACTION_CLAIMED);
            intentClaimed.putExtra(RealTimeService.EXTRA_UUID, objectDeal.getUuid());
            intentClaimed.putExtra(RealTimeService.EXTRA_DEAL_ID, objectDeal.getDeal_id() + "");
            intentClaimed.putExtra(RealTimeService.EXTRA_CONSUMER_ID, UserController.getCurrentUser(this).getConsumer_id() + "");
            intentClaimed.putExtra(RealTimeService.EXTRA_MERCHANT_ID, objectDeal.getMerchant_id() + "");
            startService(intentClaimed);
            showProgressDialog();
        } else {
            showToastError("Incorrect code");
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_DEAL_AVAILABLE_BY_DEAL_ID)) {

                if (result.equals(RealTimeService.RESULT_OK)) {
                    ObjectDeal objectDeal = DealController.getDealByDealIdAndDealType(context, deal_id, Const.DEALTYPE.DEAL_AVAILABLE);
                    if (objectDeal != null) {
                        setupQRCode(objectDeal);
                    }
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    showToastError("Can not fetch data. Please try again");
                    finish();
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
                hideCustomProgressDialog();
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_CLAIMED)) {

                if (result.equals(RealTimeService.RESULT_OK)) {

                    List<ObjectDeal> deals = DealController.getListDealByDealId(context, deal_id, outletId);
                    for(ObjectDeal deal:deals) {
                        if (deal != null) {
                            deal.setF_claimed(true);
                            DealController.update(context, deal);
                        }
                    }

                    showPopupClaimDib();
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
                hideCustomProgressDialog();
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_LIKE_DEAL)) {

                if (result.equals(RealTimeService.RESULT_OK)) {

                    ObjectDeal deal = DealController.getDealByDealId(EnterMerchantCodeActivity.this, deal_id);
                    if (deal != null) {


                        if (deal != null) {
                            deal.setF_yay(true);
                            if (deal.getK_likes() != null)
                                deal.setK_likes(deal.getK_likes() + 1);
                            if (deal.getF_nay() != null && deal.getF_nay()) {
                                deal.setF_nay(false);
                                if (deal.getK_unlikes() != null)
                                    deal.setK_unlikes(deal.getK_unlikes() - 1);
                            }
                            DealController.update(EnterMerchantCodeActivity.this, deal);

                        }

                    }

                } else if (result.equals(RealTimeService.RESULT_FAIL)) {

                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }

                hideProgressDialog();
                showPopupClaimDibThanks();
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_UNLIKE_DEAL)) {

                if (result.equals(RealTimeService.RESULT_OK)) {

                    ObjectDeal deal = DealController.getDealByDealId(EnterMerchantCodeActivity.this, deal_id);
                    if (deal != null) {

                        if (deal != null) {
                            deal.setF_nay(true);
                            if (deal.getK_unlikes() != null)
                                deal.setK_unlikes(deal.getK_unlikes() + 1);
                            if (deal.getF_yay() != null && deal.getF_yay()) {
                                deal.setF_yay(false);
                                if (deal.getK_likes() != null)
                                    deal.setK_likes(deal.getK_likes() - 1);
                            }
                            DealController.update(EnterMerchantCodeActivity.this, deal);
                        }

                    }

                } else if (result.equals(RealTimeService.RESULT_FAIL)) {

                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
                hideProgressDialog();
                showPopupClaimDibThanks();
            }
        }
    };

    private void showPopupClaimDib() {

        ObjectDeal objectDeal = DealController.getDealByDealIdAndDealType(this, deal_id, Const.DEALTYPE.DEAL_AVAILABLE);
        // custom dialog
        final Dialog dialog = new Dialog(EnterMerchantCodeActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_claming_success);

        overrideFontsLight(dialog.findViewById(R.id.root));

        LinearLayout btnSubmit = (LinearLayout) dialog.findViewById(R.id.lnlSubmit);
        LinearLayout btnYay = (LinearLayout) dialog.findViewById(R.id.btnYay);
        LinearLayout btnNay = (LinearLayout) dialog.findViewById(R.id.btnNay);

        final TextView txtMerchantName = (TextView) dialog.findViewById(R.id.txtMerchantName);
        final ImageView imgNay = (ImageView) dialog.findViewById(R.id.imgNay);
        final ImageView imgYay = (ImageView) dialog.findViewById(R.id.imgYay);
        final TextView txtYay = (TextView) dialog.findViewById(R.id.txtYay);
        final TextView txtNay = (TextView) dialog.findViewById(R.id.txtNay);
        final EditText edtComment = (EditText) dialog.findViewById(R.id.edtComment);
        edtComment.setTypeface(StaticFunction.light(this));
        btnYay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isYay = true;
                isClick = true;
                imgNay.setColorFilter(getResources().getColor(R.color.silver));
                txtNay.setTextColor(getResources().getColor(R.color.silver));
                imgYay.setColorFilter(getResources().getColor(R.color.white));
                txtYay.setTextColor(getResources().getColor(R.color.white));
            }
        });

        btnNay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isYay = false;
                isClick = true;
                imgNay.setColorFilter(getResources().getColor(R.color.white));
                imgYay.setColorFilter(getResources().getColor(R.color.silver));
                txtNay.setTextColor(getResources().getColor(R.color.white));
                txtYay.setTextColor(getResources().getColor(R.color.silver));
            }
        });

        txtMerchantName.setText(objectDeal.getOrganization_name());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isClick) {
                    dialog.dismiss();
                    submitLikeUnlikeDeal(edtComment.getText().toString().trim());
                } else {
                    showToastError("Please select either ‘YAY’ or ‘NAY’");
                }
            }
        });

        dialog.show();
    }

    private void submitLikeUnlikeDeal(String review) {

        if (UserController.isLogin(EnterMerchantCodeActivity.this)) {

            ObjectDeal deal = DealController.getDealByDealId(EnterMerchantCodeActivity.this, deal_id);

            if (deal != null) {
                if (!deal.getF_yay()) {
                    String consumer_id = UserController.getCurrentUser(EnterMerchantCodeActivity.this).getConsumer_id() + "";
                    Intent intentLike = new Intent(EnterMerchantCodeActivity.this, RealTimeService.class);
                    if (isYay) {
                        intentLike.setAction(RealTimeService.ACTION_LIKE_DEAL);
                    } else {
                        intentLike.setAction(RealTimeService.ACTION_UNLIKE_DEAL);
                    }
                    intentLike.putExtra(RealTimeService.EXTRA_CONSUMER_ID, consumer_id);
                    intentLike.putExtra(RealTimeService.EXTRA_MERCHANT_ID, deal.getMerchant_id() + "");
                    intentLike.putExtra(RealTimeService.EXTRA_DEAL_ID, deal_id + "");
                    intentLike.putExtra(RealTimeService.EXTRA_REVIEW_TEXT, review);
                    EnterMerchantCodeActivity.this.startService(intentLike);
                    showProgressDialog();
                }
            }

        } else {
            showToastInfo(getString(R.string.login_first));
        }
    }


    private void showPopupClaimDibThanks() {

        ObjectDeal objectDeal = DealController.getDealByDealIdAndDealType(this, deal_id, Const.DEALTYPE.DEAL_AVAILABLE);
        // custom dialog
        final Dialog dialog = new Dialog(EnterMerchantCodeActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_claming_success_thanks);

        overrideFontsLight(dialog.findViewById(R.id.root));

        LinearLayout btnSubmit = (LinearLayout) dialog.findViewById(R.id.lnlSubmit);
        final TextView txtMerchantName = (TextView) dialog.findViewById(R.id.txtMerchantName);
        txtMerchantName.setText(objectDeal.getOrganization_name());
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
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