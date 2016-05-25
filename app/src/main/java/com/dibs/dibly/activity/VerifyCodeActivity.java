package com.dibs.dibly.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.consts.Const;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.lylc.widget.circularprogressbar.CircularProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

/**
 * Created by VuPhan on 4/9/16.
 */
public class VerifyCodeActivity extends BaseActivity implements View.OnClickListener {


    private TextView btnDone, btnResendCode, txtLabelResend;
    private EditText edtVerificationCode;
    private CircularProgressBar circularProgressBar;
    private CountDownTimer countDownTime;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);

        JSONObject object = new JSONObject();
        try {
            object.put("time", StaticFunction.getCurrentTime());
            object.put("email", getIntent().getExtras().getString(RealTimeService.EXTRA_USER_EMAIL, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        trackMixPanel(getString(R.string.Enter_Verify), object);
        startDurationMixPanel(getString(R.string.Duration_Verify));

        registerReceiver();
        initialView();
        initialIntent();
    }

    private void initialView() {
        //initialToolBar();
        overrideFontsLight(findViewById(R.id.root));
        btnDone = (TextView) findViewById(R.id.btnDone);
        btnResendCode = (TextView) findViewById(R.id.btnResendCode);
        txtLabelResend = (TextView) findViewById(R.id.txtLabelResend);
        edtVerificationCode = (EditText) findViewById(R.id.edtVerificationCode);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgress);
        btnDone.setOnClickListener(this);
        btnResendCode.setOnClickListener(this);
    }

    private void initialIntent() {
        email = getIntent().getExtras().getString(RealTimeService.EXTRA_USER_EMAIL, "");
        password = getIntent().getExtras().getString(RealTimeService.EXTRA_USER_PASS, "");

        String screen = getIntent().getExtras().getString(Const.BUNDLE_EXTRAS.FROM_SCREEN, "");
        if (screen.equals(Const.SCREENS.SIGNIN_SCREEN)) {
            resendVerificationCode();
        } else {
            handleCountDown();
        }
    }

    private void registerReceiver() {
        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_VERIFY_PHONE);
            intentFilter.addAction(RealTimeService.RECEIVER_REQUEST_NEW_CODE);
            intentFilter.addAction(RealTimeService.RECEIVER_AUTO_LOGIN);
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
        if (countDownTime != null)
            countDownTime.cancel();

        JSONObject object = new JSONObject();
        try {
            object.put("time", StaticFunction.getCurrentTime());
            object.put("email", getIntent().getExtras().getString(RealTimeService.EXTRA_USER_EMAIL, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        trackMixPanel(getString(R.string.Exit_Verify), object);

        JSONObject objectDuration = new JSONObject();
        try {
            objectDuration.put("email", getIntent().getExtras().getString(RealTimeService.EXTRA_USER_EMAIL, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        endDurationMixPanel(getString(R.string.Duration_Verify), objectDuration);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intentResult();
    }

    private void intentResult() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_VERIFY_PHONE)) {
                if (result.equals(RealTimeService.RESULT_OK)) {
                    loginAuto();
                }
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_REQUEST_NEW_CODE)) {
                if (result.equals(RealTimeService.RESULT_OK)) {

                    btnResendCode.setVisibility(View.GONE);
                    circularProgressBar.setVisibility(View.VISIBLE);
                    txtLabelResend.setVisibility(View.VISIBLE);
                    handleCountDown();
                }
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_AUTO_LOGIN)) {
                if (result.equals(RealTimeService.RESULT_OK)) {
                    Intent intentHome = new Intent(VerifyCodeActivity.this, HomeActivity.class);
                    intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentHome);
                    finish();
                }
            }

            hideProgressDialog();
            if (result.equals(RealTimeService.RESULT_FAIL)) {
                String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                showToastError(message);
            } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                showToastError(getString(R.string.nointernet));
            }
        }
    };

    private void loginAuto() {
        Intent intentLogin = new Intent(this, RealTimeService.class);
        intentLogin.setAction(RealTimeService.ACTION_AUTO_LOGIN);
        intentLogin.putExtra(RealTimeService.EXTRA_USER_EMAIL, email);
        intentLogin.putExtra(RealTimeService.EXTRA_USER_PASS, password);
        startService(intentLogin);
        showProgressDialog();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDone:
                buttonDone();
                break;
            case R.id.btnResendCode:
                resendVerificationCode();
                break;
        }
    }

    private void buttonDone() {
        String verificationCode = edtVerificationCode.getText().toString().trim();

        if (verificationCode.length() == 0) {
            showToastError(getString(R.string.blank_verificationCode));
        } else {

            SharedPreferences preferences = getSharedPreferences("data_signup", MODE_PRIVATE);
            long consumerId = preferences.getLong("consumer_id", 0);

            Intent intentSignUp = new Intent(this, RealTimeService.class);
            intentSignUp.setAction(RealTimeService.ACTION_VERIFY_PHONE);
            intentSignUp.putExtra(RealTimeService.EXTRA_CONSUMER_ID, String.valueOf(consumerId));
            intentSignUp.putExtra(RealTimeService.EXTRA_VERIFY_CODE, verificationCode);
            startService(intentSignUp);
            showProgressDialog();
        }
    }

    private void resendVerificationCode() {

        SharedPreferences preferences = getSharedPreferences("data_signup", MODE_PRIVATE);
        long consumerId = preferences.getLong("consumer_id", 0);
        Intent intentSignUp = new Intent(this, RealTimeService.class);
        intentSignUp.setAction(RealTimeService.ACTION_REQUEST_NEW_CODE);
        intentSignUp.putExtra(RealTimeService.EXTRA_CONSUMER_ID, String.valueOf(consumerId));
        startService(intentSignUp);
        showProgressDialog();
    }

    private void handleCountDown() {

        final int minute = 5;
        final long timeCountDown = minute * 60 * 1000;

        circularProgressBar.setMax(100);
        circularProgressBar.setProgress(100);

        countDownTime = new CountDownTimer(timeCountDown, 1000) {

            public void onTick(long millisUntilFinished) {
                String minute = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)) + " m";
                String second = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))) + " s";
                circularProgressBar.setTitle(minute);
                circularProgressBar.setSubTitle(second);

                int percent = (int) (((timeCountDown - millisUntilFinished) * 100) / timeCountDown);
                circularProgressBar.setProgress(percent);
            }

            public void onFinish() {
                btnResendCode.setVisibility(View.VISIBLE);
                circularProgressBar.setVisibility(View.GONE);
                txtLabelResend.setVisibility(View.GONE);
            }
        }.start();
    }

}

