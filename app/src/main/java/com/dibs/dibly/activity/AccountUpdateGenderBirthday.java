package com.dibs.dibly.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.simpletoast.SimpleToast;
import com.dibs.dibly.staticfunction.StaticFunction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import greendao.ObjectUser;

/**
 * Created by USER on 11/24/2015.
 */
public class AccountUpdateGenderBirthday extends Activity implements View.OnClickListener {

    private TextView edtGender;
    private TextView edtBirthday;
    private LinearLayout lnlCancel;
    private LinearLayout lnlOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_update_gender_birthday);

        StaticFunction.overrideFontsLight(this, findViewById(R.id.root));

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_ACCOUNT_UPDATE);
            registerReceiver(activityReceiver, intentFilter);
        }

        initView();
        initData();
    }

    private void initView() {
        edtGender = (TextView) findViewById(R.id.edtGender);
        edtBirthday = (TextView) findViewById(R.id.edtBirthday);
        lnlCancel = (LinearLayout) findViewById(R.id.lnlCancel);
        lnlOk = (LinearLayout) findViewById(R.id.lnlOk);

        edtGender.setOnClickListener(this);
        edtBirthday.setOnClickListener(this);
        lnlCancel.setOnClickListener(this);
        lnlOk.setOnClickListener(this);
    }

    private void initData() {
        ObjectUser user = UserController.getCurrentUser(this);
        edtGender.setText(user.getGender().equalsIgnoreCase("M") ? "Male" : "Female");
        edtBirthday.setText(user.getDob());
    }

    @Override
    public void onBackPressed() {
        Intent intentEmail = new Intent(AccountUpdateGenderBirthday.this, AccountUpdateEmail.class);
        startActivity(intentEmail);
        finish();
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
            case R.id.edtGender:
                List<String> list = new ArrayList<>();
                list.add("Male");
                list.add("Female");
                showGenderDialog(list);
                break;
            case R.id.edtBirthday:
                if (edtBirthday.getText().toString().length() == 0) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar c = Calendar.getInstance();

                    showDatePicker(formatter.format(c.getTime()));
                } else {
                    showDatePicker(edtBirthday.getText().toString());
                }
                break;
            case R.id.lnlCancel:
                Intent intentEmail = new Intent(AccountUpdateGenderBirthday.this, AccountUpdateEmail.class);
                startActivity(intentEmail);
                finish();
                break;
            case R.id.lnlOk:
                ObjectUser user = UserController.getCurrentUser(this);
                Intent intentLogin = new Intent(this, RealTimeService.class);
                intentLogin.setAction(RealTimeService.ACTION_ACCOUNT_UPDATE);
                intentLogin.putExtra(RealTimeService.EXTRA_USER_ID, user.getUser_id() + "");
                intentLogin.putExtra(RealTimeService.EXTRA_USER_FIRSTNAME, user.getFirst_name());
                intentLogin.putExtra(RealTimeService.EXTRA_USER_LASTNAME, user.getLast_name());
                intentLogin.putExtra(RealTimeService.EXTRA_USER_EMAIL, user.getEmail());
                intentLogin.putExtra(RealTimeService.EXTRA_USER_GENDER, edtGender.getText().toString().trim().equalsIgnoreCase("Male") ? "M" : "F");
                intentLogin.putExtra(RealTimeService.EXTRA_USER_DOB, edtBirthday.getText().toString().trim());
                startService(intentLogin);
                showProgressDialog();
                break;
        }
    }

    public void showGenderDialog(final List<String> listData) {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.popup_with_list);
        StaticFunction.overrideFontsLight(this, dialog.findViewById(R.id.root));

        ListView listView = (ListView) dialog.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_list_item, listData);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                edtGender.setText(listData.get(position));

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showDatePicker(String dateInString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        String dateInString = txtDay.getText().toString();
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
                                "yyyy-MM-dd");
                        String dateInString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        Date date = null;
                        try {
                            date = formatter.parse(dateInString);
                        } catch (java.text.ParseException e) {

                            e.printStackTrace();
                        }
                        SimpleDateFormat ftDay = new SimpleDateFormat(
                                "yyyy-MM-dd");

                        edtBirthday.setText(ftDay.format(date));
                    }

                }, Integer.parseInt(ftYear.format(date)),
                Integer.parseInt(ftMonth.format(date)) - 1,
                Integer.parseInt(ftDay.format(date)));
        dp.setTitle("Calender");
        dp.setMessage("Select Day.");

        dp.show();
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_ACCOUNT_UPDATE)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    ObjectUser user = UserController.getCurrentUser(AccountUpdateGenderBirthday.this);
                    user.setGender(edtGender.getText().toString().trim());
                    user.setDob(edtBirthday.getText().toString().trim());
                    UserController.insertOrUpdate(AccountUpdateGenderBirthday.this, user);

                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastOk(message);

                    Intent intentEmail = new Intent(AccountUpdateGenderBirthday.this, AccountUpdateEmail.class);
                    startActivity(intentEmail);
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
