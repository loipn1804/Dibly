package com.dibs.dibly.activity;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dibs.dibly.R;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.simpletoast.SimpleToast;
import com.dibs.dibly.staticfunction.StaticFunction;

import greendao.ObjectUser;

/**
 * Created by USER on 11/24/2015.
 */
public class AccountUpdateEmail extends Activity implements View.OnClickListener {

    private LinearLayout lnlCancel;
    private LinearLayout lnlOk;
    private EditText edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_update_email);

        StaticFunction.overrideFontsLight(this, findViewById(R.id.root));

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_ACCOUNT_UPDATE);
            intentFilter.addAction(RealTimeService.RECEIVER_CHECK_EMAIL_EXIST);
            registerReceiver(activityReceiver, intentFilter);
        }

        initView();
        initData();
    }

    private void initView() {
        lnlCancel = (LinearLayout) findViewById(R.id.lnlCancel);
        lnlOk = (LinearLayout) findViewById(R.id.lnlOk);
        edtEmail = (EditText) findViewById(R.id.edtEmail);

        lnlCancel.setOnClickListener(this);
        lnlOk.setOnClickListener(this);
    }

    private void initData() {
        ObjectUser user = UserController.getCurrentUser(this);
        edtEmail.setText(user.getEmail());
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserController.isLoginByFb(this)) {
            ObjectUser user = UserController.getCurrentUser(this);
            if (user.getEmail().length() > 0) {
                finish();
            }
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            this.unregisterReceiver(activityReceiver);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lnlCancel:
                finish();
                break;
            case R.id.lnlOk:
                if (edtEmail.getText().toString().trim().length() == 0) {
                    showToastError(getString(R.string.blank_email));
//                    ObjectUser user = UserController.getCurrentUser(this);
//                    Intent intentLogin = new Intent(this, RealTimeService.class);
//                    intentLogin.setAction(RealTimeService.ACTION_ACCOUNT_UPDATE);
//                    intentLogin.putExtra(RealTimeService.EXTRA_USER_ID, user.getUser_id() + "");
//                    intentLogin.putExtra(RealTimeService.EXTRA_USER_FIRSTNAME, user.getFirst_name());
//                    intentLogin.putExtra(RealTimeService.EXTRA_USER_LASTNAME, user.getLast_name());
//                    intentLogin.putExtra(RealTimeService.EXTRA_USER_EMAIL, edtEmail.getText().toString().trim());
//                    intentLogin.putExtra(RealTimeService.EXTRA_USER_GENDER, user.getGender().equalsIgnoreCase("Male") ? "M" : "F");
//                    intentLogin.putExtra(RealTimeService.EXTRA_USER_DOB, user.getGender());
//                    startService(intentLogin);
//                    showProgressDialog();
                } else {
                    Intent intentCheckEmail = new Intent(this, RealTimeService.class);
                    intentCheckEmail.setAction(RealTimeService.ACTION_CHECK_EMAIL_EXIST);
                    intentCheckEmail.putExtra(RealTimeService.EXTRA_USER_EMAIL, edtEmail.getText().toString().trim());
                    startService(intentCheckEmail);
                    showProgressDialog();
                }
                break;
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_ACCOUNT_UPDATE)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    ObjectUser user = UserController.getCurrentUser(AccountUpdateEmail.this);
                    user.setEmail(edtEmail.getText().toString().trim());
                    UserController.insertOrUpdate(AccountUpdateEmail.this, user);

                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastOk(message);

                    finish();
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
                hideProgressDialog();
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_CHECK_EMAIL_EXIST)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                    hideProgressDialog();
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    ObjectUser user = UserController.getCurrentUser(AccountUpdateEmail.this);
                    Intent intentLogin = new Intent(AccountUpdateEmail.this, RealTimeService.class);
                    intentLogin.setAction(RealTimeService.ACTION_ACCOUNT_UPDATE);
                    intentLogin.putExtra(RealTimeService.EXTRA_USER_ID, user.getUser_id() + "");
                    intentLogin.putExtra(RealTimeService.EXTRA_USER_FIRSTNAME, user.getFirst_name());
                    intentLogin.putExtra(RealTimeService.EXTRA_USER_LASTNAME, user.getLast_name());
                    intentLogin.putExtra(RealTimeService.EXTRA_USER_EMAIL, edtEmail.getText().toString().trim());
                    intentLogin.putExtra(RealTimeService.EXTRA_USER_GENDER, user.getGender().equalsIgnoreCase("Male") ? "M" : "F");
                    intentLogin.putExtra(RealTimeService.EXTRA_USER_DOB, user.getGender());
                    startService(intentLogin);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                    hideProgressDialog();
                }
            }
        }
    };

    public void showToastError(String message) {
        SimpleToast.error(this, message);
    }

    public void showToastOk(String message) {
        SimpleToast.ok(this, message);
    }

    private Dialog dialog = null;

    public void showProgressDialog() {
        showCustomProgressDialog();
    }

    public void hideProgressDialog() {
        hideCustomProgressDialog();
    }

    public void showCustomProgressDialog() {
        if (dialog == null) {
            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.popup_progress);
            dialog.show();
        }
    }

    public void hideCustomProgressDialog() {

        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = null;
        }

    }
}
