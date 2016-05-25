package com.dibs.dibly.service;

import android.util.Log;

import com.dibs.dibly.staticfunction.StaticFunction;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 7/6/2015.
 */
public class API {

    //public static String URL = "http://dibly.clients.latsolutions.com/WebService/V1/";

    public static String URL = "http://api.v2.getdibly.com/WebService/V1/";
    public static String URL_V2 = "http://api.v2.getdibly.com/WebService/V1/";

    private String TOKEN = StaticFunction.TOKEN;

    private HttpClient client = new DefaultHttpClient();
    private HttpParams httpParams = client.getParams();
    private int TIME_OUT = 30 * 1000;

    public String login(String email, String pass) {
        String url = URL_V2 + "authorization";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", pass));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String logout(String user_id) {
        String url = URL_V2 + "logout";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("user_id", user_id));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            post.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String loginFacebook(String facebook_id, String username, String first_name, String last_name, String gender, String email) {
        String url = URL + "processFbLogin";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("facebook_id", facebook_id));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("first_name", first_name));
        params.add(new BasicNameValuePair("last_name", last_name));
        params.add(new BasicNameValuePair("gender", gender));
        params.add(new BasicNameValuePair("email", email));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String signup(String full_name, String phone_code, String phone, String email, String pass) {
        String url = URL_V2 + "signup";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("full_name", full_name));
        params.add(new BasicNameValuePair("phone_code", phone_code));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", pass));
        // params.add(new BasicNameValuePair("tester","vu"));
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            post.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String updateAccount(String user_id, String fullName, String phoneCode, String phoneNumber, String email, String gender, String dob, String ref_code) {
        String url = URL_V2 + "updateAccount";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("user_id", user_id));
        params.add(new BasicNameValuePair("full_name", fullName));
       // params.add(new BasicNameValuePair("phone_code", phoneCode));
        //params.add(new BasicNameValuePair("phone", phoneNumber));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("gender", gender));
        params.add(new BasicNameValuePair("dob", dob));
        params.add(new BasicNameValuePair("ref_code", ref_code));
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            post.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String changePassword(String user_id, String password, String new_password) {
        String url = URL + "changePassword";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("user_id", user_id));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("new_password", new_password));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            post.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String forgotPassword(String email) {
        String url = URL + "forgotPassword";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("email", email));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String checkEmailExist(String email) {
        String url = URL + "emailExists/" + email;

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getDealLongOrShortTerm(String latitude, String longitude, String page,
                                         int is_long, String consumer_id, String sort_field,
                                         String sort_dir, String keyword, String categories, String typeDeals) {
        String url;
        if (is_long == 1) {
            url = URL + "getDealsLongTerm";
        } else {
            url = URL_V2 + "getDealsShortTerm";
        }


        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("latitude", latitude));
        params.add(new BasicNameValuePair("longitude", longitude));
        params.add(new BasicNameValuePair("page", page));
        params.add(new BasicNameValuePair("consumer_id", consumer_id));
        if (keyword != null && keyword.trim().length() > 0)
            params.add(new BasicNameValuePair("keyword", ""));
        if (categories != null && categories.trim().length() > 0)
            params.add(new BasicNameValuePair("categories", categories));
        if (typeDeals != null && typeDeals.trim().length() > 0)
            params.add(new BasicNameValuePair("groups", typeDeals));


        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;


        try {
            String param = URLEncodedUtils.format(params, "UTF-8");
            url += "?" + param;

            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getDealFollowing(String page) {
        String url = URL + "getDealsFollowing";
        ;

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("page", page));

        try {
            String param = URLEncodedUtils.format(params, "UTF-8");
            url += "?" + param;
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }


    public String getDealsByDiscovery(String discovery_id, String latitude, String longitude, String page, String consumer_id) {

        String url = URL + "getDealsByDiscovery/" + discovery_id;
        url += "?latitude=" + latitude + "&longitude=" + longitude + "&page=" + page + "&consumer_id=" + consumer_id;

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

//        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
//        params.add(new BasicNameValuePair("latitude", latitude));
//        params.add(new BasicNameValuePair("longitude", longitude));
//        params.add(new BasicNameValuePair("page", page));
//        params.add(new BasicNameValuePair("consumer_id", consumer_id));

        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getDealsByMerchant(String merchant_id, String latitude, String longitude, String page, String consumer_id) {

        String url = URL + "getDealsByMerchant/" + merchant_id;
        url += "?latitude=" + latitude + "&longitude=" + longitude + "&page=" + page + "&consumer_id=" + consumer_id;

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

//        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
//        params.add(new BasicNameValuePair("latitude", latitude));
//        params.add(new BasicNameValuePair("longitude", longitude));
//        params.add(new BasicNameValuePair("page", page));
//        params.add(new BasicNameValuePair("consumer_id", consumer_id));

        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getDealsFromListId(String ids, String outlet_id) {

        String url = URL_V2 + "getDealsFromListId/" + ids + "?outlet_id=" + outlet_id;

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getDealDetailByDealId(String dealId, String is_view, String outlet_id) {
        String url = URL + "getDealDetail/" + dealId + "?is_view=" + is_view + "&outlet_id=" + outlet_id;
        //Log.e("url",url);
        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getMerchantById(String merchantId, String dealId) {
        String url = URL + "getMerchant/" + merchantId;
        if (dealId.length() > 0) {
            url += "?deal_id=" + dealId;
        }

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getAllMerchant(String name, String page, String industry_id, String sort_dir) {

        name = name.replace(" ","%20");
        String url = URL + "getMerchants?q=" + name + "&page=" + page + "&industry_type_id=" + industry_id + "&sort_field=organization_name&sort_dir=" + sort_dir;

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getMerchantsFollowing(String comsumerId,int page) {
        String url = URL + "getMerchantsFollowing/" + comsumerId +"?page=" + page;

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }


    public String getCommentByDealId(String dealId, String page) {
        String url = URL + "getDealComments/" + dealId + "?page=" + page;

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getCommentByMerchantId(String merchantId, String page) {
        String url = URL + "getMerchantComments/" + merchantId + "?page=" + page;

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String uploadGCMRegisterIdToServer(String user_id, String token, String device_id) {
        String url = URL + "updateDeviceToken";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("user_id", user_id));
        params.add(new BasicNameValuePair("device_os", "1"));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("device_id", device_id));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            post.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    // binh
    public String getDiscoveryTitle() {

        String postURL = "getDiscoveries";

        String url;

        url = URL + postURL;


        HttpGet get = new HttpGet();

        get = new HttpGet(url);
        get.addHeader("AuthorizationKey", TOKEN);
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

    public String callDibs(String deal_id, String consumer_id, String transaction_id, String outlet_id) {
        String url = URL + "callDibs";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("deal_id", deal_id));
        params.add(new BasicNameValuePair("consumer_id", consumer_id));
        params.add(new BasicNameValuePair("transaction_id", transaction_id));
        params.add(new BasicNameValuePair("outlet_id", outlet_id));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            post.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String callClaimed(String uuid, String deal_id, String consumer_id, String merchant_id) {
        String url = URL + "callClaimed";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("uuid", uuid));
        params.add(new BasicNameValuePair("deal_id", deal_id));
        params.add(new BasicNameValuePair("consumer_id", consumer_id));
        params.add(new BasicNameValuePair("merchant_id", merchant_id));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            post.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getDealsClaimed(String consumer_id, String page, String sort_field, String sort_dir) {
        String url = URL + "getDealsClaimed/" + consumer_id; //+ "?page=" + page + "&sort_field=" + sort_field + "&sort_dir=" + sort_dir;

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getDealsAvailable(String consumer_id) {
        String url = URL + "getDealsAvailable/" + consumer_id;

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getDealsAvailableByDealId(String consumer_id, String deal_id) {
        String url = URL + "getDealCallDibs/" + consumer_id + "/" + deal_id;

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String searchDeals(String latitude, String longitude, String page, String consumer_id, String keyword, String deal_type, String discovery_type, String min, String max) {
        String url;
        url = URL + "searchFilters?";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("latitude", latitude));
        params.add(new BasicNameValuePair("longitude", longitude));
        params.add(new BasicNameValuePair("page", page));
        params.add(new BasicNameValuePair("consumer_id", consumer_id));
        params.add(new BasicNameValuePair("keyword", keyword));
        params.add(new BasicNameValuePair("deal_type", deal_type));
        params.add(new BasicNameValuePair("discovery_type", discovery_type));
        params.add(new BasicNameValuePair("min", min));
        params.add(new BasicNameValuePair("max", max));

        try {
            String paramString = URLEncodedUtils.format(params, "UTF-8");
            url = url + paramString;
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getFollowingList(String consumerID) {
        String url = URL_V2 + "getMerchantsFollowing/" + consumerID;

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }


    public String stopFollowMerchant(String merchantID, String consumerID) {
        String url = URL + "unFollowMerchant";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("merchant_id", merchantID));
        params.add(new BasicNameValuePair("consumer_id", consumerID));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            post.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String followMerchant(String merchantID, String consumerID) {
        String url = URL + "followMerchant";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("merchant_id", merchantID));
        params.add(new BasicNameValuePair("consumer_id", consumerID));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            post.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String likeDeal(String merchantID, String consumerID, String dealID, String review) {
        String url = URL + "likeDeal";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("merchant_id", merchantID));
        params.add(new BasicNameValuePair("consumer_id", consumerID));
        params.add(new BasicNameValuePair("deal_id", dealID));
        params.add(new BasicNameValuePair("review", review));
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            post.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String unLikeDeal(String merchantID, String consumerID, String dealID, String review) {
        String url = URL + "unLikeDeal";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("merchant_id", merchantID));
        params.add(new BasicNameValuePair("consumer_id", consumerID));
        params.add(new BasicNameValuePair("deal_id", dealID));
        params.add(new BasicNameValuePair("review", review));
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            post.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String addCommentDeal(String dealID, String consumerID, String text) {
        String url = URL + "addDealComment";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("deal_id", dealID));
        params.add(new BasicNameValuePair("consumer_id", consumerID));
        params.add(new BasicNameValuePair("text", text));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            post.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String fetchAllDiscovery() {
        String url = URL + "fetchAllDiscovery";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getIndustryTypes() {
        String url = URL + "getIndustryTypes";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getInviteCode(String consumer_id) {
        String url = URL + "getInviteCode/" + consumer_id;

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getDiscountsAvaiable(String consumer_id) {
        String url = URL + "getDiscountsAvaiable/" + consumer_id;

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getURL(String url) {
        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(url);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getKeyClientToken() {
        String url = URL + "braintree/getClientToken";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(url);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String createAPayment(String nonce, String amount, String consumer_id, String merchant_id) {
        String url = URL + "braintree/createAPayment";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("payment_method_nonce", nonce));
        params.add(new BasicNameValuePair("amount", amount));
        params.add(new BasicNameValuePair("consumer_id", consumer_id));
        params.add(new BasicNameValuePair("merchant_id", merchant_id));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }


    public String getCountries() {
        String url = URL_V2 + "getCountries";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String requestNewCode(String consumer_id) {
        String url = URL_V2 + "requestNewCode/" + consumer_id;//+"?tester=vu";


        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }


    public String verifyPhone(String code, String consumer_id) {
        String url = URL_V2 + "verifyPhone";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("code", code));
        params.add(new BasicNameValuePair("consumer_id", consumer_id));


        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }


    public String getFilterDeals() {
        String url = URL_V2 + "getFilterDeals";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }


    public String getMerchantReviews(long merchantId, String page) {
        String url = URL_V2 + "getMerchantReviews/" + merchantId + "?page=" + page;

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(url);
            get.addHeader("AuthorizationKey", TOKEN);
            response = client.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String report(String dealId, String merchantId,String outletId,String text) {
        String url = URL_V2 + "reportDeal";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("merchant_id", merchantId));
        params.add(new BasicNameValuePair("outlet_id", outletId));
        params.add(new BasicNameValuePair("deal_id", dealId));
        params.add(new BasicNameValuePair("text", text));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }
}
