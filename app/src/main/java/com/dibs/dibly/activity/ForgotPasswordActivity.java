package com.dibs.dibly.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.ClickSpan;

/**
 * Created by USER on 12/4/2015.
 */
public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;

    private EditText edtEmail;
    private TextView txtDone;
    private LinearLayout btnResend;
    private TextView txtChangeEmail;
    private TextView txtResendTitle;

    private LinearLayout lnlEnterEmail;
    private LinearLayout lnlResend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_FORGOT_PASSWORD);
            registerReceiver(activityReceiver, intentFilter);
        }

        initView();
        initData();
    }

    private void initView() {
        overrideFontsLight(findViewById(R.id.root));
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        txtDone = (TextView) findViewById(R.id.txtDone);
        btnResend = (LinearLayout) findViewById(R.id.btnResend);
        txtChangeEmail = (TextView) findViewById(R.id.txtChangeEmail);
        txtResendTitle = (TextView) findViewById(R.id.txtResendTitle);

        lnlEnterEmail = (LinearLayout) findViewById(R.id.lnlEnterEmail);
        lnlResend = (LinearLayout) findViewById(R.id.lnlResend);

        rltBack.setOnClickListener(this);
        txtDone.setOnClickListener(this);
        btnResend.setOnClickListener(this);
        txtChangeEmail.setOnClickListener(this);

        lnlEnterEmail.setVisibility(View.VISIBLE);
        lnlResend.setVisibility(View.INVISIBLE);
    }

    private void initData() {
        clickify(txtResendTitle, "email_address", new ClickSpan.OnClickListener() {
            @Override
            public void onClick() {
                Intent intentTerm = new Intent(ForgotPasswordActivity.this, TermActivity.class);
                startActivity(intentTerm);
            }
        }, R.color.orange_main);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.txtDone:
                sendEmail();
                break;
            case R.id.btnResend:
                sendEmail();
                break;
            case R.id.txtChangeEmail:
                lnlEnterEmail.setVisibility(View.VISIBLE);
                lnlResend.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void sendEmail() {
        String email = edtEmail.getText().toString().trim();
        if (email.length() == 0) {
            showToastError(getString(R.string.blank_email));
        } else {
            Intent intentLogin = new Intent(this, RealTimeService.class);
            intentLogin.setAction(RealTimeService.ACTION_FORGOT_PASSWORD);
            intentLogin.putExtra(RealTimeService.EXTRA_USER_EMAIL, email);
            startService(intentLogin);
            showProgressDialog();
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
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_FORGOT_PASSWORD)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastOk(message);
//                    lnlEnterEmail.setVisibility(View.INVISIBLE);
//                    lnlResend.setVisibility(View.VISIBLE);
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
                hideProgressDialog();
            }
        }
    };
}
