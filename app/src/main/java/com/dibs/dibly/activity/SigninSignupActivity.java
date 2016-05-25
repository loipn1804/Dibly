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
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.astuetz.PagerSlidingTabStrip;
import com.dibs.dibly.R;
import com.dibs.dibly.adapter.SigninSignupPagerAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunction;

/**
 * Created by USER on 10/30/2015.
 */
public class SigninSignupActivity extends BaseActivity implements View.OnClickListener {

    public static String SWITCH_TAB = "SWITCH_TAB";
    public static String SIGN_UP_SUCCESS = "SIGN_UP_SUCCESS";
    public static String SIGN_UP_ACTION = "SIGN_UP_ACTION";
    public static String SIGN_IN_ACTION = "SIGN_IN_ACTION";

    private RelativeLayout rltBack;
    private boolean isSignin;
    private TextView btnSignInOrSignUp;

    private ViewPager viewPager;
    private PagerSlidingTabStrip tabStrip;
    private SigninSignupPagerAdapter signinSignupPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_signup);

        overrideFontsLight(findViewById(R.id.root));

        registerReceiver();

        isSignin = false;

        initialToolBar();
        initView();
        initData();
    }

    private void initView() {
        btnSignInOrSignUp = (TextView) findViewById(R.id.btnSignInOrSignUp);
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        rltBack.setOnClickListener(this);
        btnSignInOrSignUp.setOnClickListener(this);
    }

    private void initData() {
        signinSignupPagerAdapter = new SigninSignupPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(signinSignupPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        tabStrip.setShouldExpand(true);
        tabStrip.setViewPager(viewPager);
        tabStrip.setTextColor(getResources().getColor(R.color.txt_black_69));
        tabStrip.setDividerColor(getResources().getColor(R.color.transparent));
        tabStrip.setIndicatorColor(getResources().getColor(R.color.orange_main));
        tabStrip.setUnderlineColor(getResources().getColor(R.color.transparent));
        tabStrip.setTypeface(StaticFunction.semi_bold(this), 1);
        tabStrip.setTextSize((int) getResources().getDimension(R.dimen.txt_15sp));

        tabStrip.setBackgroundColor(getResources().getColor(R.color.gray_bg_tab_trip));


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switchTab();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        switchTab();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.btnSignInOrSignUp:
                buttonSignInOrSignUp();
                break;
        }
    }

    private void switchTab() {
        isSignin = !isSignin;
        if (isSignin) {
            btnSignInOrSignUp.setText("Sign In");
            StaticFunction.sendBroad(this, SWITCH_TAB);
        } else {
            btnSignInOrSignUp.setText("Sign Up");
            StaticFunction.sendBroad(this, SWITCH_TAB);
        }
    }

    private void buttonSignInOrSignUp(){
        if (isSignin) {
            StaticFunction.sendBroad(this, SIGN_IN_ACTION);
        } else {
            StaticFunction.sendBroad(this, SIGN_UP_ACTION);
        }
    }


    private void registerReceiver() {
        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_SIGNUP);
            intentFilter.addAction(SIGN_UP_SUCCESS);
            registerReceiver(activityReceiver, intentFilter);
        }
    }

    private void unRegisterReceiver() {
        if (activityReceiver != null) {
            unregisterReceiver(activityReceiver);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();
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


    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(SIGN_UP_SUCCESS)) {
                if (!isSignin) {
                    viewPager.setCurrentItem(0);
                }
            }
        }
    };

    public void showPopupVerifyAccount() {
        // custom dialog
        final Dialog dialog = new Dialog(this);

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
}
