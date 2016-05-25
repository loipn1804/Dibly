package com.dibs.dibly.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.API;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.soundcloud.android.crop.Crop;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import greendao.ObjectUser;

/**
 * Created by USER on 6/29/2015.
 */
public class AccountDetailActivity extends BaseActivity implements View.OnClickListener {


    private EditText edtFullName;
    private TextView edtBirthday;
    private EditText edtEmail;
    private TextView btnSaveDetail;
    private LinearLayout lnlMale, lnlFemale;
    private ImageView imgMale, imgFemale,imgAvatar;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private boolean isMale;
    private String dob = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_ACCOUNT_UPDATE);
            registerReceiver(activityReceiver, intentFilter);
        }

        initView();
        initData();
    }


    private void initView() {
        initialToolBar();
        options = new DisplayImageOptions.Builder().cacheInMemory(false)
                .displayer(new RoundedBitmapDisplayer(500))
                .showImageForEmptyUri(R.drawable.menu_person)
                .showImageOnLoading(R.drawable.menu_person)
                .cacheOnDisk(true).build();

        overrideFontsLight(findViewById(R.id.root));
        overrideFontsBold(findViewById(R.id.txtLabelBirthday));
        overrideFontsBold(findViewById(R.id.txtLableFullName));
        overrideFontsBold(findViewById(R.id.txtLableEmail));
        overrideFontsBold(findViewById(R.id.txtLabelGender));

        edtFullName = (EditText) findViewById(R.id.edtFullName);
        edtBirthday = (TextView) findViewById(R.id.edtBirthday);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        btnSaveDetail = (TextView) findViewById(R.id.btnSaveDetail);
        lnlMale = (LinearLayout) findViewById(R.id.lnlMale);
        lnlFemale = (LinearLayout) findViewById(R.id.lnlFemale);
        imgMale = (ImageView) findViewById(R.id.imgMale);
        imgFemale = (ImageView) findViewById(R.id.imgFemale);
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        lnlMale.setOnClickListener(this);
        imgAvatar.setOnClickListener(this);
        lnlFemale.setOnClickListener(this);
        edtBirthday.setOnClickListener(this);
        btnSaveDetail.setOnClickListener(this);
    }

    private void initData() {
        ObjectUser user = UserController.getCurrentUser(AccountDetailActivity.this);
        if (user != null) {
            edtFullName.setText(user.getFull_name());
            isMale = user.getGender().equalsIgnoreCase("M") ? true : false;
            handleGender();

            edtBirthday.setText(user.getDob());
            edtEmail.setText(user.getEmail());


            edtEmail.setEnabled(false);
            edtEmail.setTextColor(getResources().getColor(R.color.txt_black_99));

            if (TextUtils.isEmpty(user.getFacebook_id())) {
                if (!user.getProfile_image().endsWith("/")) {
                    imageLoader.displayImage(user.getProfile_image(), imgAvatar, options);
                }
            } else {
                imageLoader.displayImage("https://graph.facebook.com/" + user.getFacebook_id() + "/picture?height=200&width=200", imgAvatar, options);
            }


            if(user.getDob().trim().length()>0) {
                SimpleDateFormat formatter = new SimpleDateFormat(
                        "yyyy-MM-dd");
                dob = user.getDob();
                Date date = null;
                try {
                    date = formatter.parse(user.getDob());
                    SimpleDateFormat ftDay = new SimpleDateFormat(
                            "dd-MM-yyyy");
                    edtBirthday.setText(ftDay.format(date));
                } catch (java.text.ParseException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    private void handleGender() {

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
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
            case R.id.btnSaveDetail:
                if (edtEmail.getText().toString().trim().length() == 0) {
                    showToastError(getString(R.string.blank_email));
                } else {
                    ObjectUser user = UserController.getCurrentUser(this);
                    Intent intentLogin = new Intent(AccountDetailActivity.this, RealTimeService.class);
                    intentLogin.setAction(RealTimeService.ACTION_ACCOUNT_UPDATE);
                    intentLogin.putExtra(RealTimeService.EXTRA_USER_ID, user.getUser_id() + "");
                    intentLogin.putExtra(RealTimeService.EXTRA_USER_NAME, user.getFull_name());
                    intentLogin.putExtra(RealTimeService.EXTRA_USER_PHONECODE, "");
                    intentLogin.putExtra(RealTimeService.EXTRA_USER_PHONENUMBER, "");
                    intentLogin.putExtra(RealTimeService.EXTRA_USER_EMAIL, user.getEmail());
                    intentLogin.putExtra(RealTimeService.EXTRA_USER_GENDER, isMale ? "M" : "F");
                    intentLogin.putExtra(RealTimeService.EXTRA_USER_DOB, dob);
                    startService(intentLogin);
                    showProgressDialog();
                }

                break;
            case R.id.lnlFemale:
                isMale = !isMale;
                handleGender();
                break;
            case R.id.lnlMale:
                isMale = !isMale;
                handleGender();
                break;
            case R.id.imgAvatar:
                openImagePicker();
                break;

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

        DatePickerDialog dp = new DatePickerDialog(AccountDetailActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat formatter = new SimpleDateFormat(
                                "dd-MM-yyyy");
                        String dateInString = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        dob = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        Date date = null;
                        try {
                            date = formatter.parse(dateInString);
                        } catch (java.text.ParseException e) {

                            e.printStackTrace();
                        }
                        SimpleDateFormat ftDay = new SimpleDateFormat(
                                "dd-MM-yyyy");

                        edtBirthday.setText(ftDay.format(date));
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
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_ACCOUNT_UPDATE)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    ObjectUser user = UserController.getCurrentUser(AccountDetailActivity.this);
                    user.setFull_name(edtFullName.getText().toString().trim());
                    user.setEmail(edtEmail.getText().toString().trim());
                    user.setGender(isMale ? "M" : "F");
                    user.setDob(dob);
                    UserController.insertOrUpdate(AccountDetailActivity.this, user);

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
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_PASSWORD_UPDATE)) {
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

    class ImageUploadTask extends AsyncTask<Bitmap, Void, String> {

        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(API.URL + "updateProfileImage");

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmaps[0].compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] data = bos.toByteArray();
                ContentBody bin = new ByteArrayBody(data, "image.jpg");

                MultipartEntityBuilder multiPartEntityBuilder = MultipartEntityBuilder.create();
                multiPartEntityBuilder.addPart("profile_file", bin);
                multiPartEntityBuilder.addTextBody("user_id", UserController.getCurrentUser(AccountDetailActivity.this).getUser_id() + "");

                httpPost.setEntity(multiPartEntityBuilder.build());
                HttpResponse response = httpClient.execute(httpPost,
                        localContext);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                response.getEntity().getContent(), "UTF-8"));

                String sResponse = reader.readLine();
                return sResponse;
            } catch (Exception e) {

                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Void... unsued) {
        }

        @Override
        protected void onPostExecute(String sResponse) {

            try {
                JSONObject jsonObject = new JSONObject(sResponse);
                JSONObject job_status = jsonObject.getJSONObject("status");
                String type = job_status.getString("type");
                if (type.equalsIgnoreCase("Success")) {
                    String message = job_status.getString("message");
                    JSONObject job_data = jsonObject.getJSONObject("data");
                    String profile_image = job_data.getString("profile_image");
                    ObjectUser user = UserController.getCurrentUser(AccountDetailActivity.this);
                    user.setProfile_image(profile_image);
                    UserController.insertOrUpdate(AccountDetailActivity.this, user);

                    if (TextUtils.isEmpty(user.getFacebook_id())) {
                        if (!user.getProfile_image().endsWith("/")) {
                            imageLoader.displayImage(user.getProfile_image(), imgAvatar, options);
                        }
                    } else {
                        imageLoader.displayImage("https://graph.facebook.com/" + user.getFacebook_id() + "/picture?height=200&width=200", imgAvatar, options);
                    }
                } else {
                    String message = job_status.getString("message");
                    showToastError(message);
                }
            } catch (JSONException e) {

            }
        }
    }

    private void openImagePicker() {
        Crop.pickImage(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);

    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

            Uri selectedImageUri = Crop.getOutput(result);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                Bitmap b = Bitmap.createScaledBitmap(bitmap, StaticFunction.getScreenWidth(AccountDetailActivity.this) / 2, StaticFunction.getScreenWidth(AccountDetailActivity.this) / 2, false);

                ImageUploadTask imageUploadTask = new ImageUploadTask();
                imageUploadTask.execute(b);

            } catch (Exception e) {

            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
