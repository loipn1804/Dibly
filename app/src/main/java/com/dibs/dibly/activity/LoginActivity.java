package com.dibs.dibly.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.MyLocationController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.gcm.PlayServicesHelper;
import com.dibs.dibly.service.ParallaxService;
import com.dibs.dibly.service.RealTimeService;

import greendao.MyLocation;
import greendao.ObjectUser;

/**
 * Created by USER on 6/29/2015.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Button btnLogin;
    private Button btnSignupNow;
    private TextView txtDoItLater;
    private EditText edtEmail;
    private EditText edtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        overrideFontsLight(findViewById(R.id.root));

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_LOGIN);
            registerReceiver(activityReceiver, intentFilter);
        }

        initView();

        if (getIntent().hasExtra("verify")) {
            showPopupVerifyAccount();
        }
    }

    private void initView() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignupNow = (Button) findViewById(R.id.btnSignupNow);
        txtDoItLater = (TextView) findViewById(R.id.txtDoItLater);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPass = (EditText) findViewById(R.id.edtPass);

        btnLogin.setOnClickListener(this);
        btnSignupNow.setOnClickListener(this);
        txtDoItLater.setOnClickListener(this);

        SpannableString content = new SpannableString(txtDoItLater.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txtDoItLater.setText(content);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getIntent().hasExtra("from_signup")) {
            Intent intentSignup = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intentSignup);
        } else {
            Intent intentPre = new Intent(LoginActivity.this, PreLoginActivity.class);
            startActivity(intentPre);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
//                Intent backToHome1 = new Intent();
//                backToHome1.setClass(this, HomeActivity.class);
//                backToHome1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(backToHome1);
                login();
                break;
            case R.id.btnSignupNow:
                Intent intentSignup = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intentSignup);
                finish();
                break;
            case R.id.txtDoItLater:
//                Intent backToHome2 = new Intent();
//                backToHome2.setClass(this, HomeActivity.class);
//                backToHome2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(backToHome2);
                Intent intentHome = new Intent(LoginActivity.this, HomeActivity.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentHome);
                finish();
                break;
        }
    }

    private void login() {
        String email = edtEmail.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        if (email.length() == 0) {
            showToastError(getString(R.string.blank_email));
        } else if (pass.length() == 0) {
            showToastError(getString(R.string.blank_pass));
        } else {
            Intent intentLogin = new Intent(LoginActivity.this, RealTimeService.class);
            intentLogin.setAction(RealTimeService.ACTION_LOGIN);
            intentLogin.putExtra(RealTimeService.EXTRA_USER_EMAIL, email);
            intentLogin.putExtra(RealTimeService.EXTRA_USER_PASS, pass);
            startService(intentLogin);
            showProgressDialog();
        }
    }

    private void processRoleUser() {
        if (UserController.isLogin(LoginActivity.this)) {
            ObjectUser user = UserController.getCurrentUser(LoginActivity.this);
            if (user.getIsConsumer()) {
                PlayServicesHelper playServicesHelper = new PlayServicesHelper(LoginActivity.this);
                Intent intentHome = new Intent(LoginActivity.this, HomeActivity.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentHome);
                loadDataWhenLogin();
                finish();
            } else {
                Intent intentMerchant = new Intent(LoginActivity.this, MerChantHomeActivity.class);
                intentMerchant.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentMerchant);
//                Intent intentHome = new Intent(LoginActivity.this, HomeActivity.class);
//                startActivity(intentHome);
                finish();
            }
        }
    }

    private void loadDataWhenLogin() {
        DealController.clearAllDeals(LoginActivity.this);
        MyLocation myLocation = MyLocationController.getLastLocation(LoginActivity.this);
        if (myLocation != null) {
            Intent intentGetDeal = new Intent(LoginActivity.this, ParallaxService.class);
            intentGetDeal.setAction(ParallaxService.ACTION_GET_DEAL_LONG_TERM);
            intentGetDeal.putExtra(ParallaxService.EXTRA_LATITUDE, myLocation.getLatitude() + "");
            intentGetDeal.putExtra(ParallaxService.EXTRA_LONGITUDE, myLocation.getLongitude() + "");
            intentGetDeal.putExtra(ParallaxService.EXTRA_PAGE, "1");
            intentGetDeal.putExtra(RealTimeService.EXTRA_KEYWORD, "");
            intentGetDeal.putExtra(RealTimeService.EXTRA_CATEGORIES, "");
            intentGetDeal.putExtra(RealTimeService.EXTRA_TYPE_DEAL, "");
            startService(intentGetDeal);
//            ((BaseActivity) getActivity()).showProgressDialog();

            Intent intentGetDealShort = new Intent(LoginActivity.this, RealTimeService.class);
            intentGetDealShort.setAction(RealTimeService.ACTION_GET_DEAL_SHORT_TERM);
            intentGetDealShort.putExtra(RealTimeService.EXTRA_LATITUDE, myLocation.getLatitude() + "");
            intentGetDealShort.putExtra(RealTimeService.EXTRA_LONGITUDE, myLocation.getLongitude() + "");
            intentGetDealShort.putExtra(RealTimeService.EXTRA_PAGE, "1");
            intentGetDeal.putExtra(RealTimeService.EXTRA_KEYWORD, "");
            intentGetDeal.putExtra(RealTimeService.EXTRA_CATEGORIES, "");
            intentGetDeal.putExtra(RealTimeService.EXTRA_TYPE_DEAL, "");
            startService(intentGetDealShort);
//            ((BaseActivity) getActivity()).showProgressDialog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getExtras() != null) {
            edtEmail.setText(getIntent().getExtras().getString("email", ""));
        }

//        edtEmail.setText("user1@gmail.com");
//        edtPass.setText("123456");
    }

    public void showPopupVerifyAccount() {
        // custom dialog
        final Dialog dialog = new Dialog(LoginActivity.this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_confirm);

        overrideFontsLight(dialog.findViewById(R.id.root));

        LinearLayout lnlOk = (LinearLayout) dialog.findViewById(R.id.lnlOk);
        LinearLayout lnlCancel = (LinearLayout) dialog.findViewById(R.id.lnlCancel);
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        TextView txtOk = (TextView) dialog.findViewById(R.id.txtOk);
        txtMessage.setText("Open your email to verify account.");
        txtOk.setText("Open Email");

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                try {
                    PackageManager pm = getPackageManager();
                    Intent launchIntent = pm.getLaunchIntentForPackage("com.google.android.gm");
                    startActivity(launchIntent);
                } catch (Exception e) {
                    showToastError("Can not open email");
                }
            }
        });

        lnlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_LOGIN)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    hideProgressDialog();
                    processRoleUser();
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                    hideProgressDialog();
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                    hideProgressDialog();
                }
            }
        }
    };
}
