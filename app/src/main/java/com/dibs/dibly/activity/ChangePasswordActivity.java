package com.dibs.dibly.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.RealTimeService;

/**
 * Created by USER on 12/1/2015.
 */
public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {


    private EditText edtCurrentPass;
    private EditText edtNewPass;
    private EditText edtConfirmPass;
    private TextView txtSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        overrideFontsLight(findViewById(R.id.root));

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_PASSWORD_UPDATE);
            registerReceiver(activityReceiver, intentFilter);
        }

        initView();
        initData();
    }

    private void initView() {
        initialToolBar();

        edtCurrentPass = (EditText) findViewById(R.id.edtCurrentPass);
        edtNewPass = (EditText) findViewById(R.id.edtNewPass);
        edtConfirmPass = (EditText) findViewById(R.id.edtConfirmPass);
        txtSave = (TextView) findViewById(R.id.txtSave);

        txtSave.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.txtSave:
                String currentPass = edtCurrentPass.getText().toString().trim();
                String newPass = edtNewPass.getText().toString().trim();
                String confirmPass = edtConfirmPass.getText().toString().trim();
                if (currentPass.length() == 0) {
                    showToastError(getString(R.string.blank_pass));
                } else if (newPass.length() == 0) {
                    showToastError(getString(R.string.blank_new_pass));
                } else if (!confirmPass.equals(newPass)) {
                    showToastError(getString(R.string.error_confirm_pass));
                } else {
                    Intent intentChangePass = new Intent(this, RealTimeService.class);
                    intentChangePass.setAction(RealTimeService.ACTION_PASSWORD_UPDATE);
                    intentChangePass.putExtra(RealTimeService.EXTRA_USER_ID, UserController.getCurrentUser(this).getUser_id() + "");
                    intentChangePass.putExtra(RealTimeService.EXTRA_USER_PASS, currentPass);
                    intentChangePass.putExtra(RealTimeService.EXTRA_USER_NEW_PASS, newPass);
                    startService(intentChangePass);
                    showProgressDialog();
                }
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
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_PASSWORD_UPDATE)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastOk(message);
                    hideProgressDialog();
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
}
