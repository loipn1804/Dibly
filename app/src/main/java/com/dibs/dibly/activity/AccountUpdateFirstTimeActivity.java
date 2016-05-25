package com.dibs.dibly.activity;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.RealTimeService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import greendao.ObjectUser;

/**
 * Created by USER on 12/4/2015.
 */
public class AccountUpdateFirstTimeActivity extends BaseActivity implements View.OnClickListener {

    private TextView btnSkip;
    private TextView txtLabelGender;
    private TextView txtLabelBirthday;
    private EditText edtReferalCode;
    private TextView edtBirthday;
    private TextView txtDone;
    private LinearLayout lnlMale, lnlFemale;
    private ImageView imgMale, imgFemale;
    private boolean isMale = true;

    private String dob = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account_first_time);

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_ACCOUNT_UPDATE);
            registerReceiver(activityReceiver, intentFilter);
        }

        initView();
        initialToolBar();
        initData();
    }

    private void initView() {
        overrideFontsLight(findViewById(R.id.root));

        btnSkip = (TextView) findViewById(R.id.btnSkip);
        txtLabelGender = (TextView) findViewById(R.id.txtLabelGender);
        txtLabelBirthday = (TextView) findViewById(R.id.txtLabelBirthday);
        edtBirthday = (TextView) findViewById(R.id.edtBirthday);
        edtReferalCode = (EditText) findViewById(R.id.edtReferalCode);
        txtDone = (TextView) findViewById(R.id.txtDone);
        lnlMale = (LinearLayout) findViewById(R.id.lnlMale);
        lnlFemale = (LinearLayout) findViewById(R.id.lnlFemale);
        imgMale = (ImageView) findViewById(R.id.imgMale);
        imgFemale = (ImageView) findViewById(R.id.imgFemale);


        btnSkip.setOnClickListener(this);
        edtBirthday.setOnClickListener(this);
        txtDone.setOnClickListener(this);

        lnlMale.setOnClickListener(this);
        lnlFemale.setOnClickListener(this);

    }

    private void initData() {
        ObjectUser user = UserController.getCurrentUser(this);
        if (user != null) {
            edtBirthday.setText(user.getDob());
        }
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSkip:
                finish();
                break;
            case R.id.edtBirthday:
                if (edtBirthday.getText().toString().length() == 0) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar c = Calendar.getInstance();

                    showDatePicker(formatter.format(c.getTime()));
                } else {
                    showDatePicker(edtBirthday.getText().toString());
                }
                break;
            case R.id.txtDone:
                buttonDone();
                break;
            case R.id.lnlFemale:
                handleGender();
                break;
            case R.id.lnlMale:
                handleGender();
                break;
        }
    }

    private void buttonDone() {
        ObjectUser user = UserController.getCurrentUser(this);
        Intent intentLogin = new Intent(this, RealTimeService.class);
        intentLogin.setAction(RealTimeService.ACTION_ACCOUNT_UPDATE);
        intentLogin.putExtra(RealTimeService.EXTRA_USER_ID, user.getUser_id() + "");
        intentLogin.putExtra(RealTimeService.EXTRA_USER_NAME, user.getFull_name());
        intentLogin.putExtra(RealTimeService.EXTRA_USER_PHONECODE, user.getPhoneCode());
        intentLogin.putExtra(RealTimeService.EXTRA_USER_PHONENUMBER, user.getPhone());
        intentLogin.putExtra(RealTimeService.EXTRA_USER_EMAIL, user.getEmail());
        intentLogin.putExtra(RealTimeService.EXTRA_USER_GENDER, isMale ? "M" : "F");
        intentLogin.putExtra(RealTimeService.EXTRA_USER_DOB, dob.trim());
        intentLogin.putExtra(RealTimeService.EXTRA_REF_CODE, edtReferalCode.getText().toString().trim());
        startService(intentLogin);
        showProgressDialog();
    }

    private void handleGender() {
        isMale = !isMale;
        if (isMale) {
            lnlFemale.setBackgroundResource(R.drawable.btn_gender_inactive);
            lnlMale.setBackgroundResource(R.drawable.btn_gender_active);
            imgMale.setImageResource(R.drawable.ic_active);
            imgFemale.setImageResource(R.drawable.ic_inactive);
        } else {
            lnlMale.setBackgroundResource(R.drawable.btn_gender_inactive);
            lnlFemale.setBackgroundResource(R.drawable.btn_gender_active);
            imgFemale.setImageResource(R.drawable.ic_active);
            imgMale.setImageResource(R.drawable.ic_inactive);
        }

    }


    private void showDatePicker(String dateInString) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = formatter.parse(dateInString);
        } catch (java.text.ParseException e) {

            e.printStackTrace();
        }
        SimpleDateFormat ftDay = new SimpleDateFormat("dd");
        SimpleDateFormat ftMonth = new SimpleDateFormat("MM");
        SimpleDateFormat ftYear = new SimpleDateFormat("yyyy");

        DatePickerDialog dp = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat formatter = new SimpleDateFormat(
                                "dd-MM-yyyy");
                        String dateInString = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        Date date = null;
                        try {
                            date = formatter.parse(dateInString);
                        } catch (java.text.ParseException e) {

                            e.printStackTrace();
                        }
                        SimpleDateFormat ftDay = new SimpleDateFormat(
                                "dd-MM-yyyy");

                        edtBirthday.setText(ftDay.format(date));

                        dob = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    }

                }, Integer.parseInt(ftYear.format(date)),
                Integer.parseInt(ftMonth.format(date)) - 1,
                Integer.parseInt(ftDay.format(date)));
        dp.setTitle("Calender");
        dp.setMessage("Select Day.");

        dp.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            this.unregisterReceiver(activityReceiver);
        }
        if (UserController.isLoginByFb(this) && UserController.getCurrentUser(this).getEmail().length() == 0) {
            Intent intentEmail = new Intent(AccountUpdateFirstTimeActivity.this, AccountUpdateEmail.class);
            startActivity(intentEmail);
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_ACCOUNT_UPDATE)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    ObjectUser user = UserController.getCurrentUser(AccountUpdateFirstTimeActivity.this);
                    user.setGender(isMale ? "M" : "F");
                    user.setDob(dob);
                    UserController.insertOrUpdate(AccountUpdateFirstTimeActivity.this, user);

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
            }
        }
    };



}
