package com.dibs.dibly.api;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.util.ArrayList;
import java.util.List;

public class API {

    public static final String API_BASE_LINK = "http://dev.cloudshop.sg/api/1.2/";
    ; //"http://dev.cloudshop.sg/api/1.1/";

    private HttpClient client = new DefaultHttpClient();
    private HttpPost post = new HttpPost("http://dev.cloudshop.sg");
    private HttpGet get = new HttpGet();
    HttpParams httpParams = client.getParams();

    private int TIME_OUT = 30 * 1000;
    //HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
    //HttpConnectionParams.setSoTimeout(httpParams, TIME_OUT);


    public String applyVoucher(String customerID, String voucherCode) {

        String apiExtend = "apply_mobile_voucher";
        post = new HttpPost(API_BASE_LINK + apiExtend);
        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("customer_id", customerID));
        params.add(new BasicNameValuePair("voucher_code", voucherCode));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            post.setEntity(entity);

            ResponseHandler<String> handler = new BasicResponseHandler();
            content = client.execute(post, handler);
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;

    }


    // get already used voucher code after login for current customer id
    public String getUsedVoucher(long customerID) {
        return "";
    }


    // get not used voucher code after login for current customer id
    public String getActiveVoucher(long customerID) {
        return "";
    }


    public String getAllVouchers(String customerID, boolean isCustomerApplied) {
        String apiExtend = "";

        if (isCustomerApplied == true) {
            apiExtend = "get_vouchers_by_customer/" + customerID + "?is_applied=true";
        } else {
            apiExtend = "get_vouchers_by_customer/" + customerID + "?is_applied=false";
        }

        get = new HttpGet(API_BASE_LINK + apiExtend);
        //get = new HttpGet("http://10.0.2.2:1234/api/products/getAllProductsFromDB");
        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";

        try {
            ResponseHandler<String> handler = new BasicResponseHandler();
            content = client.execute(get, handler);
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }


    // generate voucher code base on point user type in
    public String generateVoucher(String customerID, double amount) {

        String apiExtend = "create_voucher";
        post = new HttpPost(API_BASE_LINK + apiExtend);
        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("customer_id", customerID));
        params.add(new BasicNameValuePair("voucher_amount", amount + ""));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            post.setEntity(entity);

            ResponseHandler<String> handler = new BasicResponseHandler();
            content = client.execute(post, handler);
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }


    // login and return customer information
    public String login(String Username, String Password) {
        String apiExtend = "customer/login";
        post = new HttpPost(API_BASE_LINK + apiExtend);
        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("customer_code", Username));
        params.add(new BasicNameValuePair("password", Password));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            post.setEntity(entity);

            ResponseHandler<String> handler = new BasicResponseHandler();
            content = client.execute(post, handler);
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String uploadGCMRegisterIdToServer(String regId) {
        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("action", "insert_notification"));
        params.add(new BasicNameValuePair("register_id", regId));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            post.setEntity(entity);
            ResponseHandler<String> handler = new BasicResponseHandler();
            content = client.execute(post, handler);
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }


}
