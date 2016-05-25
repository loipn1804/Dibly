package com.dibs.dibly.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.braintreepayments.api.dropin.BraintreePaymentActivity;
import com.braintreepayments.api.dropin.Customization;
import com.dibs.dibly.R;
import com.dibs.dibly.adapter.ReviewAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.CommentController;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.MerchantController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.ParallaxService;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunction;

import java.util.ArrayList;
import java.util.List;

import greendao.Comment;
import greendao.ObjectDeal;

/**
 * Created by USER on 6/29/2015.
 */
public class AllReviewActivity extends BaseActivity implements View.OnClickListener {

    private ListView lvReview;
    private ReviewAdapter reviewAdapter;
    private RelativeLayout rltBack;
    private RelativeLayout rltCallDib;
    private TextView txtCallDib;
    private List<Comment> listComment;
    private View progessbarFooter;
    private LayoutInflater layoutInflater;
    private View headerView;

    private Long dealId;
    private Long merchantId;
    private boolean isDealDetail;

    private String KEY_CLIENT_TOKEN = "";

    public static int ALL_REVIEW_REQUEST = 1212;

    private boolean isExprited;

    private boolean isLoading;
    private int page;
    private int last_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_review);

        isExprited = false;

        isLoading = false;
        page = 1;
        last_page = 0;

        overrideFontsLight(findViewById(R.id.root));

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ParallaxService.RECEIVER_GET_COMMENT_BY_DEAL_ID);
            intentFilter.addAction(ParallaxService.RECEIVER_GET_COMMENT_BY_MERCHANT_ID);
            intentFilter.addAction(DealDetailActivity.RECEIVER_EXPRITED_TIME);
//            intentFilter.addAction(RealTimeService.RECEIVER_CALL_DIB);
//            intentFilter.addAction(RealTimeService.RECEIVER_GET_KEY_CLIENT_TOKEN);
//            intentFilter.addAction(RealTimeService.RECEIVER_CREATE_PAYMENT);
            registerReceiver(activityReceiver, intentFilter);
        }

        page = 2;

        if (getIntent().getExtras() != null) {
            dealId = getIntent().getLongExtra("deal_id", 0l);
            merchantId = getIntent().getLongExtra("merchant_id", 0l);
            isDealDetail = getIntent().getBooleanExtra("isDealDetail", true);
            isExprited = getIntent().getBooleanExtra("isExprited", true);

            isLoading = true;
            getData();
        } else {
            finish();
        }

        initView();
        initData();
    }

    private void initView() {
        lvReview = (ListView) findViewById(R.id.lvReview);
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        rltCallDib = (RelativeLayout) findViewById(R.id.rltCallDib);
        txtCallDib = (TextView) findViewById(R.id.txtCallDib);

        rltBack.setOnClickListener(this);
        rltCallDib.setOnClickListener(this);

        lvReview.setOnScrollListener(new EndScrollListener());

        layoutInflater = LayoutInflater.from(this);
        progessbarFooter = layoutInflater.inflate(R.layout.view_progressbar_loadmore, null);
        headerView = layoutInflater.inflate(R.layout.view_invisible_header, null);

        lvReview.addHeaderView(headerView);
    }

    private void initData() {
        listComment = new ArrayList<Comment>();
        listComment = CommentController.getAll(AllReviewActivity.this);

        reviewAdapter = new ReviewAdapter(AllReviewActivity.this, listComment);
        lvReview.setAdapter(reviewAdapter);

        setBtnCalldibs();
    }

    private void getData() {
        if (isDealDetail) {
            Intent intentGetComment = new Intent(AllReviewActivity.this, ParallaxService.class);
            intentGetComment.setAction(ParallaxService.ACTION_GET_COMMENT_BY_DEAL_ID);
            intentGetComment.putExtra(ParallaxService.EXTRA_DEAL_ID, dealId + "");
            intentGetComment.putExtra(ParallaxService.EXTRA_PAGE, page + "");
            startService(intentGetComment);
        } else {
            Intent intentGetComment = new Intent(AllReviewActivity.this, ParallaxService.class);
            intentGetComment.setAction(ParallaxService.ACTION_GET_COMMENT_BY_MERCHANT_ID);
            intentGetComment.putExtra(ParallaxService.EXTRA_MERCHANT_ID, merchantId + "");
            intentGetComment.putExtra(ParallaxService.EXTRA_PAGE, page + "");
            startService(intentGetComment);
        }
    }

    private void setBtnCalldibs() {
        if (!isExprited) {
            if (UserController.isLogin(AllReviewActivity.this)) {
                ObjectDeal deal = DealController.getDealById(AllReviewActivity.this, dealId);
                if (deal != null) {
                    if (deal.getF_claimed()) {
                        rltCallDib.setBackgroundResource(R.drawable.btn_disable);
                        txtCallDib.setText("Claimed");
                    } else if (deal.getF_call_dibs()) {
                        rltCallDib.setBackgroundResource(R.drawable.btn_claim_now_lg);
                        txtCallDib.setText("Claim now");
                    } else {
                        rltCallDib.setBackgroundResource(R.drawable.btn_organge);
                        txtCallDib.setText("I Call Dibs");
                    }
                }
            } else {
                rltCallDib.setBackgroundResource(R.drawable.btn_organge);
                txtCallDib.setText("I Call Dibs");
            }
        } else {
            rltCallDib.setBackgroundResource(R.drawable.btn_disable);
            txtCallDib.setText("I Call Dibs");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.rltCallDib:
                if (!isExprited) {
                    if (UserController.isLogin(AllReviewActivity.this)) {
                        ObjectDeal deal = DealController.getDealById(AllReviewActivity.this, dealId);
                        if (deal != null) {
                            if (deal.getF_claimed()) {
//                            showToastInfo("You claimed this deal already.");
                            } else if (deal.getF_call_dibs()) {
//                            showToastInfo("You call dibs this deal already.");
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("claim", false);
                                setResult(RESULT_OK, returnIntent);
                                finish();
                            } else {
                                showPopupConfirmCallDib(deal.getTerms());
                            }
                        }
                    } else {
                        showToastInfo(getString(R.string.login_first));
                    }
                }
                break;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == BraintreePaymentActivity.RESULT_OK) {
                String paymentMethodNonce = data.getStringExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);
//                showPopupPrompt(paymentMethodNonce);

                Intent intentCreatePayment = new Intent(AllReviewActivity.this, RealTimeService.class);
                intentCreatePayment.setAction(RealTimeService.ACTION_CREATE_PAYMENT);
                intentCreatePayment.putExtra(RealTimeService.EXTRA_PAYMENT_NONCE, paymentMethodNonce);
                intentCreatePayment.putExtra(RealTimeService.EXTRA_PAYMENT_AMOUNT, DealController.getDealById(AllReviewActivity.this, dealId).getPurchase_now_price() + "");
                intentCreatePayment.putExtra(RealTimeService.EXTRA_CONSUMER_ID, UserController.getCurrentUser(AllReviewActivity.this).getConsumer_id() + "");
                intentCreatePayment.putExtra(RealTimeService.EXTRA_MERCHANT_ID, DealController.getDealById(AllReviewActivity.this, dealId).getMerchant_id() + "");
                startService(intentCreatePayment);
                showProgressDialog();
            }
        }
//        showToastInfo("onActivityResult");
    }

    private void showPopupConfirmCallDib(String term) {
        // custom dialog
        final Dialog dialog = new Dialog(AllReviewActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_confirm_call_dibs);

        overrideFontsLight(dialog.findViewById(R.id.root));

        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        TextView txtTermAndCon = (TextView) dialog.findViewById(R.id.txtTermAndCon);
        txtTermAndCon.setText(term);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intentQR = new Intent(AllReviewActivity.this, DealQRcodeActivity.class);
//                AllReviewActivity.this.startActivity(intentQR);
//                doCallDib();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("call_dibs", true);
                setResult(RESULT_OK, returnIntent);
                finish();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void doCallDib() {
        if (UserController.isLogin(AllReviewActivity.this)) {
            ObjectDeal deal = DealController.getDealById(AllReviewActivity.this, dealId);
//            if (deal != null) {
//                if (deal.getType().equalsIgnoreCase("instore")) {
//                    Intent intentCallDib = new Intent(DealDetailActivity.this, RealTimeService.class);
//                    intentCallDib.setAction(RealTimeService.ACTION_CALL_DIB);
//                    intentCallDib.putExtra(RealTimeService.EXTRA_DEAL_ID, dealId + "");
//                    intentCallDib.putExtra(RealTimeService.EXTRA_CONSUMER_ID, UserController.getCurrentUser(DealDetailActivity.this).getConsumer_id() + "");
//                    startService(intentCallDib);
//                    showProgressDialog();
//                } else {
//                    showToastInfo("This deal is in app deal.");
//                }
//            }
            if (deal != null) {
                if (deal.getDuration_type().equalsIgnoreCase("short_term")) {
                    callDibs("");
                } else {
//                    Intent intent = new Intent(DealDetailActivity.this, BraintreePaymentActivity.class);
//                    intent.putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, KEY_CLIENT_TOKEN);
//                    startActivityForResult(intent, 100);

                    //// get key client token
                    Intent intentGetKeyToken = new Intent(AllReviewActivity.this, RealTimeService.class);
                    intentGetKeyToken.setAction(RealTimeService.ACTION_GET_KEY_CLIENT_TOKEN);
                    startService(intentGetKeyToken);
                    showProgressDialog();
                }
            }
        } else {
            showToastInfo(getString(R.string.login_first));
        }
    }

    private void callDibs(String trans_id) {
        Intent intentCallDib = new Intent(AllReviewActivity.this, RealTimeService.class);
        intentCallDib.setAction(RealTimeService.ACTION_CALL_DIB);
        intentCallDib.putExtra(RealTimeService.EXTRA_DEAL_ID, dealId + "");
        intentCallDib.putExtra(RealTimeService.EXTRA_CONSUMER_ID, UserController.getCurrentUser(AllReviewActivity.this).getConsumer_id() + "");
        intentCallDib.putExtra(RealTimeService.EXTRA_PAYMENT_TRANS_ID, trans_id);
        startService(intentCallDib);
        showProgressDialog();
    }

    private void preparePayment() {
        ObjectDeal deal = DealController.getDealById(AllReviewActivity.this, dealId);
        Customization customization = new Customization.CustomizationBuilder()
                .primaryDescription(MerchantController.getById(AllReviewActivity.this, deal.getMerchant_id()).getOrganization_name())
                .secondaryDescription(deal.getTitle())
                .amount(deal.getPurchase_now_price() + "")
                .submitButtonText("Buy")
                .build();

        Intent intent = new Intent(this, BraintreePaymentActivity.class)
                .putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, KEY_CLIENT_TOKEN)
                .putExtra(BraintreePaymentActivity.EXTRA_CUSTOMIZATION, customization);
//                .putExtra(BraintreePaymentActivity.EXTRA_ANDROID_PAY_CART, getAndroidPayCart(false))
//                .putExtra(BraintreePaymentActivity.EXTRA_ANDROID_PAY_IS_BILLING_AGREEMENT, false);
//                            .putExtra(BraintreePaymentActivity.EXTRA_ANDROID_PAY_IS_BILLING_AGREEMENT, Settings.isAndroidPayBillingAgreement(this));
//
        startActivityForResult(intent, 100);
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(ParallaxService.RECEIVER_GET_COMMENT_BY_DEAL_ID)) {
                String result = intent.getStringExtra(ParallaxService.EXTRA_RESULT);
                if (result.equals(ParallaxService.RESULT_OK)) {
                    listComment.clear();
                    listComment = CommentController.getAll(AllReviewActivity.this);
                    reviewAdapter.setListData(listComment);
                    hideProgressDialog();
                    String lastpage = intent.getStringExtra(RealTimeService.EXTRA_RESULT_LAST_PAGE);
                    last_page = Integer.parseInt(lastpage);
                    String currentpage = intent.getStringExtra(RealTimeService.EXTRA_RESULT_CURRENT_PAGE);
                    page = Integer.parseInt(currentpage);
                    isLoading = false;
                    if (page == 1) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (lvReview.getFirstVisiblePosition() == 0 && lvReview.getLastVisiblePosition() == lvReview.getCount() - 1) {
                                    isLoading = true;
                                    if (page < last_page) {
                                        page++;
                                        getData();
                                        lvReview.addFooterView(progessbarFooter);
                                    }
                                }
                            }
                        }, 500);
                    }
                } else if (result.equals(ParallaxService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(ParallaxService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                } else if (result.equals(ParallaxService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
                lvReview.removeFooterView(progessbarFooter);
            } else if (intent.getAction().equalsIgnoreCase(ParallaxService.RECEIVER_GET_COMMENT_BY_MERCHANT_ID)) {
                String result = intent.getStringExtra(ParallaxService.EXTRA_RESULT);
                if (result.equals(ParallaxService.RESULT_OK)) {
                    listComment.clear();
                    listComment = CommentController.getAll(AllReviewActivity.this);
                    reviewAdapter.setListData(listComment);
                    hideProgressDialog();
                    String lastpage = intent.getStringExtra(RealTimeService.EXTRA_RESULT_LAST_PAGE);
                    last_page = Integer.parseInt(lastpage);
                    String currentpage = intent.getStringExtra(RealTimeService.EXTRA_RESULT_CURRENT_PAGE);
                    page = Integer.parseInt(currentpage);
                    isLoading = false;
                    if (page == 1) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (lvReview.getFirstVisiblePosition() == 0 && lvReview.getLastVisiblePosition() == lvReview.getCount() - 1) {
                                    isLoading = true;
                                    if (page < last_page) {
                                        page++;
                                        getData();
                                        lvReview.addFooterView(progessbarFooter);
                                    }
                                }
                            }
                        }, 500);
                    }
                } else if (result.equals(ParallaxService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(ParallaxService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                } else if (result.equals(ParallaxService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
                lvReview.removeFooterView(progessbarFooter);
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_CALL_DIB)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    String data = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    Intent intentQR = new Intent(AllReviewActivity.this, EnterMerchantCodeActivity.class);
                    intentQR.putExtra("data", data);
                    intentQR.putExtra("isBack", true);
                    AllReviewActivity.this.startActivity(intentQR);
                    hideProgressDialog();
                    List<ObjectDeal> deals = DealController.getListDealByDealId(AllReviewActivity.this, dealId);
                    for (ObjectDeal deal : deals) {
                        deal.setF_call_dibs(true);
                        DealController.update(AllReviewActivity.this, deal);
                    }
                    StaticFunction.sendBroad(AllReviewActivity.this, DealDetailActivity.RECEIVER_NOTIFY_LIST);
                    finish();
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                    hideProgressDialog();
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                    hideProgressDialog();
                }
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_KEY_CLIENT_TOKEN)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    String key = intent.getStringExtra(RealTimeService.EXTRA_RESULT_KEY_CLIENT_TOKEN);
                    KEY_CLIENT_TOKEN = key;

                    preparePayment();
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
                hideProgressDialog();
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_CREATE_PAYMENT)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    hideProgressDialog();
                    String trans_id = intent.getStringExtra(RealTimeService.EXTRA_RESULT_PAYMENT_TRANS_ID);
                    callDibs(trans_id);
//                    showPopupPrompt(trans_id);
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                    hideProgressDialog();
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                    hideProgressDialog();
                }
            } else if (intent.getAction().equalsIgnoreCase(DealDetailActivity.RECEIVER_EXPRITED_TIME)) {
                isExprited = true;
                setBtnCalldibs();
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
                    if (!isLoading) {
                        isLoading = true;
                        if (page < last_page) {
                            page++;
                            getData();
                            lvReview.addFooterView(progessbarFooter);
                        }
                    }
                }
            }
        }
    }
}
