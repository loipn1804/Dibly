package com.dibs.dibly.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.UserController;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.text.SimpleDateFormat;
import java.util.Date;

import greendao.ObjectUser;

/**
 * Created by VuPhan on 4/15/16.
 */
public class AccountInfoActivity extends BaseActivity implements View.OnClickListener {


    private TextView txtFullName, txtFollowingNo, txtDibsCalled;
    private ImageView imgAvatar;
    private TextView txtEmail, txtGender,txtDOB, txtReferralCode;
    private TextView btnEditProfile;
    private LinearLayout lnlFollowing,lnlCallDib,btnAccountSetting;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        initialView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initialView() {
        initialToolBar();
        overrideFontsLight(findViewById(R.id.root));
        options = new DisplayImageOptions.Builder().cacheInMemory(false)
                .displayer(new RoundedBitmapDisplayer(500))
                .showImageForEmptyUri(R.drawable.menu_person)
                .showImageOnLoading(R.drawable.menu_person)
                .cacheOnDisk(true).build();

        txtFullName = (TextView) findViewById(R.id.txtFullName);
        txtFollowingNo = (TextView) findViewById(R.id.txtFollowingNo);
        txtDibsCalled = (TextView) findViewById(R.id.txtDibsCalled);
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtDOB = (TextView) findViewById(R.id.txtBirthday);
        txtGender = (TextView) findViewById(R.id.txtGender);
        txtReferralCode = (TextView) findViewById(R.id.txtReferCode);
        btnEditProfile = (TextView) findViewById(R.id.btnEditProfile);
        lnlFollowing = (LinearLayout) findViewById(R.id.lnlFollowing);
        lnlCallDib = (LinearLayout) findViewById(R.id.lnlCallDib);
        btnEditProfile.setOnClickListener(this);
        btnAccountSetting = (LinearLayout) findViewById(R.id.btnAccountSetting);
        btnAccountSetting.setOnClickListener(this);
        lnlFollowing.setOnClickListener(this);
        lnlCallDib.setOnClickListener(this);
    }

    private void initData() {
        ObjectUser user = UserController.getCurrentUser(AccountInfoActivity.this);
        if (user != null) {
            txtFullName.setText(user.getFull_name());
            if (TextUtils.isEmpty(user.getFacebook_id())) {
                if (!user.getProfile_image().endsWith("/")) {
                    imageLoader.displayImage(user.getProfile_image(), imgAvatar, options);
                }
            } else {
                imageLoader.displayImage("https://graph.facebook.com/" + user.getFacebook_id() + "/picture?height=200&width=200", imgAvatar, options);
            }

            txtFollowingNo.setText(String.valueOf(user.getK_following()));
            txtDibsCalled.setText(String.valueOf(user.getK_call_dibs()));
            txtGender.setText(user.getGender().equals("M")?"Male":"Female");
            //txtDOB.setText(user.getDob());
            txtEmail.setText(user.getEmail());
            txtReferralCode.setText(user.getRef_code());

            if(user.getDob().trim().length()>0) {
                SimpleDateFormat formatter = new SimpleDateFormat(
                        "yyyy-MM-dd");
                Date date = null;
                try {
                    date = formatter.parse(user.getDob());
                    SimpleDateFormat ftDay = new SimpleDateFormat(
                            "dd-MM-yyyy");
                    txtDOB.setText(ftDay.format(date));
                } catch (java.text.ParseException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnEditProfile:
                Intent intentAccountDetail = new Intent(AccountInfoActivity.this, AccountDetailActivity.class);
                startActivity(intentAccountDetail);
                break;
            case R.id.btnAccountSetting:
                Intent intentAccountSetting = new Intent(AccountInfoActivity.this, AccountSettingActivity.class);
                startActivity(intentAccountSetting);
                break;
            case R.id.lnlFollowing:
                Intent intentFollowing = new Intent(AccountInfoActivity.this, MerchantFollowingListActivity.class);
                startActivity(intentFollowing);
                break;
            case R.id.lnlCallDib:
                Intent intentCallDib = new Intent(AccountInfoActivity.this, PurchasedDealActivity.class);
                startActivity(intentCallDib);
                break;
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
}
