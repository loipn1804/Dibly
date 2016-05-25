package com.dibs.dibly.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunction;

/**
 * Created by USER on 7/13/2015.
 */
public class MerChantHomeActivity extends BaseActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private RelativeLayout rltMenu;
    private RelativeLayout rltSearch;

    private LinearLayout btnScanner;
    private LinearLayout btnLogout;

    private TextView txtMerchantName;
    private TextView txtNameActionBar;

    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_home);

        overrideFontsLight(findViewById(R.id.root));

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_LOGOUT);
            registerReceiver(activityReceiver, intentFilter);
        }

        initView();
        initData();
    }

    private void initView() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        rltMenu = (RelativeLayout) findViewById(R.id.rltMenu);
        rltSearch = (RelativeLayout) findViewById(R.id.rltSearch);
        btnScanner = (LinearLayout) findViewById(R.id.btnScanner);
        btnLogout = (LinearLayout) findViewById(R.id.btnLogout);

        txtNameActionBar = (TextView) findViewById(R.id.txtNameActionBar);
        txtMerchantName = (TextView) findViewById(R.id.txtMerchantName);

        rltMenu.setOnClickListener(this);
        rltSearch.setOnClickListener(this);
        btnScanner.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

    }

    private void initData() {
        if (UserController.isLogin(MerChantHomeActivity.this)) {
            txtNameActionBar.setText(UserController.getCurrentUser(MerChantHomeActivity.this).getFirst_name());
            txtMerchantName.setText(UserController.getCurrentUser(MerChantHomeActivity.this).getFirst_name());
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rltMenu:
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.btnScanner:
                Intent intentFollowing = new Intent(MerChantHomeActivity.this, MerchantScannerActivity.class);
                startActivity(intentFollowing);
                drawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.btnLogout:
                showPopupConfirmLogout("Do you wish to log out?");
                break;
        }

    }

    public void showPopupConfirmLogout(String message) {
        // custom dialog
        dialog = new Dialog(MerChantHomeActivity.this);

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
        txtMessage.setText(message);

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intentLogout = new Intent(MerChantHomeActivity.this, RealTimeService.class);
//                intentLogout.setAction(RealTimeService.ACTION_LOGOUT);
//                intentLogout.putExtra(RealTimeService.EXTRA_USER_ID, UserController.getCurrentUser(MerChantHomeActivity.this).getUser_id() + "");
//                startService(intentLogout);
//                showProgressDialog();

                UserController.clearAllUsers(MerChantHomeActivity.this);

                SharedPreferences preferences = getSharedPreferences("data_token", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token", "");
                editor.commit();
                StaticFunction.TOKEN = "";

//                Session session = Session.getActiveSession();
//                if (session != null) {
//                    session.closeAndClearTokenInformation();
//                    Session.setActiveSession(null);
//                }

                dialog.dismiss();
                hideProgressDialog();
                Intent intentPre = new Intent(MerChantHomeActivity.this, PreLoginActivity.class);
                startActivity(intentPre);
                finish();
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
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_LOGOUT)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastOk(message);

//                    boolean isConsumer = false;
//
//                    if (UserController.getCurrentUser(AccountSettingActivity.this) != null) {
//                        isConsumer = UserController.getCurrentUser(AccountSettingActivity.this).getIsConsumer();
//                    }

                    UserController.clearAllUsers(MerChantHomeActivity.this);

                    SharedPreferences preferences = getSharedPreferences("data_token", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", "");
                    editor.commit();
                    StaticFunction.TOKEN = "";

//                    Session session = Session.getActiveSession();
//                    if (session != null) {
//                        session.closeAndClearTokenInformation();
//                        Session.setActiveSession(null);
//                    }

                    dialog.dismiss();
                    hideProgressDialog();

                    finish();
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
