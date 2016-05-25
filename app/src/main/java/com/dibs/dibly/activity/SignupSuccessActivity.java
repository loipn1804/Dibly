package com.dibs.dibly.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.staticfunction.ClickSpan;

/**
 * Created by USER on 12/4/2015.
 */
public class SignupSuccessActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;

    private EditText edtEmail;
    private TextView txtDone;
    private LinearLayout btnResend;
    private TextView txtChangeEmail;
    private TextView txtResendTitle;

    private LinearLayout lnlEnterEmail;
    private LinearLayout lnlResend;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_success);

        if (getIntent().hasExtra("email")) {
            email = getIntent().getStringExtra("email");
        } else {
            finish();
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

        lnlEnterEmail.setVisibility(View.INVISIBLE);
        lnlResend.setVisibility(View.VISIBLE);
    }

    private void initData() {
        String text = txtResendTitle.getText().toString();
        text = text.replace("email_address", email);
        txtResendTitle.setText(text);

        clickify(txtResendTitle, "email_address", new ClickSpan.OnClickListener() {
            @Override
            public void onClick() {

            }
        }, R.color.orange_main);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rltBack:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.txtDone:

                break;
            case R.id.btnResend:

                break;
            case R.id.txtChangeEmail:
                Intent intent2 = new Intent();
                setResult(RESULT_CANCELED, intent2);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
