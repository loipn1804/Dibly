package com.dibs.dibly.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.ClickSpan;

/**
 * Created by USER on 6/29/2015.
 */
public class SignupActivity extends BaseActivity implements View.OnClickListener {

    private Button btnSignup;
    private Button btnSigninNow;
    private TextView txtDoItLater;
    private TextView txtPrivacy;
    private EditText edtFirstname;
    private EditText edtLastname;
    private EditText edtEmail;
    private EditText edtPass;
    private EditText edtConfirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        overrideFontsLight(findViewById(R.id.root));

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_SIGNUP);
            registerReceiver(activityReceiver, intentFilter);
        }

        initView();
    }

    private void initView() {
        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnSigninNow = (Button) findViewById(R.id.btnSigninNow);
        txtDoItLater = (TextView) findViewById(R.id.txtDoItLater);
        txtPrivacy = (TextView) findViewById(R.id.txtPrivacy);
        edtFirstname = (EditText) findViewById(R.id.edtFirstname);
        edtLastname = (EditText) findViewById(R.id.edtLastname);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPass = (EditText) findViewById(R.id.edtPass);
        edtConfirmPass = (EditText) findViewById(R.id.edtConfirmPass);

        btnSignup.setOnClickListener(this);
        btnSigninNow.setOnClickListener(this);
        txtDoItLater.setOnClickListener(this);

        SpannableString content = new SpannableString(txtDoItLater.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txtDoItLater.setText(content);

        txtPrivacy.setText(Html.fromHtml(getResources().getString(R.string.by_creating_account_string)));


        clickify(txtPrivacy, "Terms of Service", new ClickSpan.OnClickListener() {
            @Override
            public void onClick() {
                //showToast("term of service");

                Intent intentTerm = new Intent(SignupActivity.this, TermActivity.class);
                startActivity(intentTerm);


            }
        }, R.color.blue_main);
        clickify(txtPrivacy, "Privacy Policy.", new ClickSpan.OnClickListener() {
            @Override
            public void onClick() {
                //showToast("privacy policy.");

                Intent intentPrivacy = new Intent(SignupActivity.this, PrivacyActivity.class);
                startActivity(intentPrivacy);
            }
        }, R.color.blue_main);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getIntent().hasExtra("from_prelogin")) {
            Intent intentPre = new Intent(SignupActivity.this, PreLoginActivity.class);
            startActivity(intentPre);
        } else {
            Intent intentLogin = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intentLogin);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignup:
//                Intent backToHome1 = new Intent();
//                backToHome1.setClass(this, HomeActivity.class);
//                backToHome1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(backToHome1);
                signup();
                break;
            case R.id.btnSigninNow:
                Intent intentLogin = new Intent(SignupActivity.this, LoginActivity.class);
                intentLogin.putExtra("from_signup", true);
                startActivity(intentLogin);
                finish();
                break;
            case R.id.txtDoItLater:
//                Intent backToHome2 = new Intent();
//                backToHome2.setClass(this, HomeActivity.class);
//                backToHome2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(backToHome2);
                Intent intentHome = new Intent(SignupActivity.this, HomeActivity.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentHome);
                finish();
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

    private void signup() {
        String firstname = edtFirstname.getText().toString().trim();
        String lastname = edtLastname.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        String confirm_pass = edtConfirmPass.getText().toString().trim();

        if (firstname.length() == 0) {
            showToastError(getString(R.string.blank_firstname));
        } else if (lastname.length() == 0) {
            showToastError(getString(R.string.blank_lastname));
        } else if (email.length() == 0) {
            showToastError(getString(R.string.blank_email));
        } else if (pass.length() == 0) {
            showToastError(getString(R.string.blank_pass));
        } else if (confirm_pass.length() == 0 || !confirm_pass.equals(pass)) {
            showToastError(getString(R.string.error_confirm_pass));
        } else {
            Intent intentSignup = new Intent(SignupActivity.this, RealTimeService.class);
            intentSignup.setAction(RealTimeService.ACTION_SIGNUP);
            intentSignup.putExtra(RealTimeService.EXTRA_USER_FIRSTNAME, firstname);
            intentSignup.putExtra(RealTimeService.EXTRA_USER_LASTNAME, lastname);
            intentSignup.putExtra(RealTimeService.EXTRA_USER_EMAIL, email);
            intentSignup.putExtra(RealTimeService.EXTRA_USER_PASS, pass);
            startService(intentSignup);
            showProgressDialog();
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_SIGNUP)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    showToastOk("Sign up successfully.");
                    Intent intentLogin = new Intent(SignupActivity.this, LoginActivity.class);
                    intentLogin.putExtra("email", edtEmail.getText().toString().trim());
                    intentLogin.putExtra("verify", true);
                    startActivity(intentLogin);
                    finish();
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
