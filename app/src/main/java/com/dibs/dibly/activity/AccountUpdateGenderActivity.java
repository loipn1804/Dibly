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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.simpletoast.SimpleToast;
import com.dibs.dibly.staticfunction.StaticFunction;

import java.util.ArrayList;
import java.util.List;

import greendao.ObjectUser;

/**
 * Created by USER on 12/2/2015.
 */
public class AccountUpdateGenderActivity extends Activity implements View.OnClickListener {

    private TextView edtGender;
    private LinearLayout lnlCancel;
    private LinearLayout lnlOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_update_gender);

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
        lnlCancel = (LinearLayout) findViewById(R.id.lnlCancel);
        lnlOk = (LinearLayout) findViewById(R.id.lnlOk);

        edtGender.setOnClickListener(this);
        lnlCancel.setOnClickListener(this);
        lnlOk.setOnClickListener(this);
    }

    private void initData() {
//        ObjectUser user = UserController.getCurrentUser(this);
//        edtGender.setText(user.getGender().equalsIgnoreCase("M") ? "Male" : "Female");
        edtGender.setText("Male");
    }

    @Override
    public void onBackPressed() {
        Intent intentEmail = new Intent(this, AccountUpdateBirthdayActivity.class);
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
            case R.id.lnlCancel:
                Intent intentEmail = new Intent(this, AccountUpdateBirthdayActivity.class);
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
                intentLogin.putExtra(RealTimeService.EXTRA_USER_DOB, user.getDob());
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

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_ACCOUNT_UPDATE)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    ObjectUser user = UserController.getCurrentUser(AccountUpdateGenderActivity.this);
                    user.setGender(edtGender.getText().toString().trim().equalsIgnoreCase("Male") ? "M" : "F");
                    UserController.insertOrUpdate(AccountUpdateGenderActivity.this, user);

                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastOk(message);

                    Intent intentEmail = new Intent(AccountUpdateGenderActivity.this, AccountUpdateBirthdayActivity.class);
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
