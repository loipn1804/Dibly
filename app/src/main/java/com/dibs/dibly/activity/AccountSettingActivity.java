package com.dibs.dibly.activity;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.DealAvailableController;
import com.dibs.dibly.daocontroller.DealClaimedController;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.NotifyController;
import com.dibs.dibly.daocontroller.SearchDealController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import greendao.Notify;
import greendao.ObjectDeal;
import greendao.ObjectUser;

/**
 * Created by USER on 6/29/2015.
 */
public class AccountSettingActivity extends BaseActivity implements View.OnClickListener {

    private int GET_IMAGE = 11;
    public static int REQUEST_CODE = 12;
    private LinearLayout lnlLogout;
    private LinearLayout lnlFAQ;
    private LinearLayout lnlTermAndCondition;
    private LinearLayout lnlPrivacy, lnlChangePassword;

    private TextView txtLogout;

    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        overrideFontsLight(findViewById(R.id.root));

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_LOGOUT);
            registerReceiver(activityReceiver, intentFilter);
        }

        initView();
    }

    private void initView() {
        initialToolBar();
        lnlLogout = (LinearLayout) findViewById(R.id.lnlLogout);
        lnlFAQ = (LinearLayout) findViewById(R.id.lnlFAQ);
        lnlTermAndCondition = (LinearLayout) findViewById(R.id.lnlTermAndCondition);
        lnlPrivacy = (LinearLayout) findViewById(R.id.lnlPrivacy);
        lnlChangePassword = (LinearLayout) findViewById(R.id.lnlChangePassword);
        txtLogout = (TextView) findViewById(R.id.txtLogout);
        lnlLogout.setOnClickListener(this);
        lnlFAQ.setOnClickListener(this);
        lnlTermAndCondition.setOnClickListener(this);
        lnlPrivacy.setOnClickListener(this);
        lnlChangePassword.setOnClickListener(this);


        if (UserController.isLoginByFb(AccountSettingActivity.this)) {
            lnlChangePassword.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.lnlLogout:
                showPopupConfirmLogout("Do you wish to log out?");
                break;
            case R.id.lnlFAQ:
                Intent intentFaq = new Intent(AccountSettingActivity.this, FaqActivity.class);
                startActivity(intentFaq);
                break;
            case R.id.lnlTermAndCondition:
                Intent intentTerm = new Intent(AccountSettingActivity.this, TermActivity.class);
                startActivity(intentTerm);
                break;
            case R.id.lnlPrivacy:
                Intent intentPrivacy = new Intent(AccountSettingActivity.this, PrivacyActivity.class);
                startActivity(intentPrivacy);
                break;
            case R.id.lnlChangePassword:
                Intent intentChangePass = new Intent(this, ChangePasswordActivity.class);
                startActivity(intentChangePass);
                break;
        }
    }


    public void showPopupConfirmLogout(String message) {

        dialog = new Dialog(AccountSettingActivity.this);
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
                logoutMixPanel();
                Intent intentLogout = new Intent(AccountSettingActivity.this, RealTimeService.class);
                intentLogout.setAction(RealTimeService.ACTION_LOGOUT);
                intentLogout.putExtra(RealTimeService.EXTRA_USER_ID, UserController.getCurrentUser(AccountSettingActivity.this).getUser_id() + "");
                startService(intentLogout);
                showProgressDialog();
                dialog.dismiss();
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

                    UserController.clearAllUsers(AccountSettingActivity.this);
                    DealAvailableController.clearAll(AccountSettingActivity.this);
                    DealClaimedController.clearAll(AccountSettingActivity.this);
                    SearchDealController.clearAllDeals(AccountSettingActivity.this);

                    SharedPreferences preferences = getSharedPreferences("data_token", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", "");
                    editor.commit();
                    StaticFunction.TOKEN = "";

                    List<ObjectDeal> dealList = DealController.getAllDeals(AccountSettingActivity.this);
                    for (ObjectDeal deal : dealList) {
                        deal.setF_call_dibs(false);
                        deal.setF_claimed(false);
                        DealController.update(AccountSettingActivity.this, deal);
                    }

                    hideProgressDialog();


                    List<Notify> notifies = NotifyController.getAll(AccountSettingActivity.this);
                    for (Notify notify : notifies) {
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancel(Integer.parseInt(notify.getDeal_id() + ""));
                    }
                    NotifyController.clearAll(AccountSettingActivity.this);

                    Intent intentHome = new Intent(context, PreLoginActivity.class);
                    intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentHome);
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

    private void logoutMixPanel() {
        ObjectUser user = UserController.getCurrentUser(this);
        JSONObject object = new JSONObject();
        try {
            object.put("time", StaticFunction.getCurrentTime());
            object.put("email", user.getEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        trackMixPanel(getString(R.string.Logout), object);
    }
}
