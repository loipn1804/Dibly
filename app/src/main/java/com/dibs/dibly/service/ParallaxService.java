package com.dibs.dibly.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.dibs.dibly.daocontroller.CommentController;
import com.dibs.dibly.daocontroller.DealClaimedController;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.DealMerchantController;
import com.dibs.dibly.daocontroller.IndustryTypeController;
import com.dibs.dibly.daocontroller.OutletController;
import com.dibs.dibly.daocontroller.PermanentController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.staticfunction.StaticFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import greendao.Comment;
import greendao.DealClaimed;
import greendao.IndustryType;
import greendao.ObjectDeal;
import greendao.ObjectDealMerchant;
import greendao.ObjectUser;
import greendao.Outlet;
import greendao.PermanentDeals;

/**
 * Created by USER on 7/8/2015.
 */
public class ParallaxService extends IntentService {

    // action
    public static final String ACTION_GET_DEAL_LONG_TERM = "ACTION_GET_DEAL_LONG_TERM";
    public static final String ACTION_GET_COMMENT_BY_DEAL_ID = "ACTION_GET_COMMENT_BY_DEAL_ID";
    public static final String ACTION_GET_COMMENT_BY_MERCHANT_ID = "ACTION_GET_COMMENT_BY_MERCHANT_ID";
    public static final String ACTION_GET_DEAL_CLAIMED = "ACTION_GET_DEAL_CLAIMED";
    public static final String ACTION_GET_INDUSTRY_TYPE = "ACTION_GET_INDUSTRY_TYPE";
    public static final String ACTION_GET_INVITE_CODE = "ACTION_GET_INVITE_CODE";

    // receiver
    public static final String RECEIVER_GET_DEAL_LONG_TERM = "RECEIVER_GET_DEAL_LONG_TERM";
    public static final String RECEIVER_GET_COMMENT_BY_DEAL_ID = "RECEIVER_GET_COMMENT_BY_DEAL_ID";
    public static final String RECEIVER_GET_COMMENT_BY_MERCHANT_ID = "RECEIVER_GET_COMMENT_BY_MERCHANT_ID";
    public static final String RECEIVER_GET_DEAL_CLAIMED = "RECEIVER_GET_DEAL_CLAIMED";
    public static final String RECEIVER_GET_INVITE_CODE = "RECEIVER_GET_INVITE_CODE";

    // extra code and result value
    public static final String EXTRA_RESULT = "EXTRA_RESULT";
    public static final String RESULT_OK = "RESULT_OK";
    public static final String RESULT_FAIL = "RESULT_FAIL";
    public static final String RESULT_NO_INTERNET = "RESULT_NO_INTERNET";

    public static final String EXTRA_RESULT_MESSAGE = "EXTRA_RESULT_MESSAGE";
    public static String RESULT_MESSAGE = "";
    public static final String EXTRA_RESULT_LAST_PAGE = "EXTRA_RESULT_LAST_PAGE";
    public static String RESULT_LAST_PAGE = "0";
    public static final String EXTRA_RESULT_CURRENT_PAGE = "EXTRA_RESULT_CURRENT_PAGE";
    public static String RESULT_CURRENT_PAGE = "0";

    public static final String EXTRA_LATITUDE = "EXTRA_LATITUDE";
    public static final String EXTRA_LONGITUDE = "EXTRA_LONGITUDE";
    public static final String EXTRA_PAGE = "EXTRA_PAGE";
    public static final String EXTRA_DEAL_ID = "EXTRA_DEAL_ID";
    public static final String EXTRA_MERCHANT_ID = "EXTRA_MERCHANT_ID";
    public static final String EXTRA_CONSUMER_ID = "EXTRA_CONSUMER_ID";

    public static final String ACTION_SEARCH_DEAL = "ACTION_SEARCH_DEAL";
    public static final String RECEIVER_SEARCH_DEAL = "RECEIVER_SEARCH_DEAL";
    public static final String EXTRA_FOLLOWING_ID = "EXTRA_FOLLOWING_ID";
    public static final String EXTRA_KEYWORD = "EXTRA_KEYWORD";
    public static final String EXTRA_DEAL_TYPE = "EXTRA_DEAL_TYPE";
    public static final String EXTRA_DISCOVERY_TYPE = "EXTRA_DISCOVERY_TYPE";
    public static final String EXTRA_DISCOVERY_ID = "EXTRA_DISCOVERY_ID";
    public static final String EXTRA_MIN = "EXTRA_MIN";
    public static final String EXTRA_MAX = "EXTRA_MAX";
    public static final String EXTRA_SORT_FIELD = "EXTRA_SORT_FIELD";
    public static final String EXTRA_SORT_DIR = "EXTRA_SORT_DIR";

    public ParallaxService() {
        super(ParallaxService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION_GET_DEAL_LONG_TERM)) {
            if (StaticFunction.isNetworkAvailable(ParallaxService.this)) {
                String lat = intent.getStringExtra(EXTRA_LATITUDE);
                String lng = intent.getStringExtra(EXTRA_LONGITUDE);
                String page = intent.getStringExtra(EXTRA_PAGE);
                String sort_field = intent.getStringExtra(EXTRA_SORT_FIELD);
                String sort_dir = intent.getStringExtra(EXTRA_SORT_DIR);
                boolean result = getDealLongTerm(lat, lng, page, sort_field, sort_dir);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_LONG_TERM, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_LONG_TERM, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_DEAL_LONG_TERM, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_COMMENT_BY_DEAL_ID)) {
            if (StaticFunction.isNetworkAvailable(ParallaxService.this)) {
                String dealId = intent.getStringExtra(EXTRA_DEAL_ID);
                String page = intent.getStringExtra(EXTRA_PAGE);
                boolean result = getCommentByDealId(dealId, page);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_COMMENT_BY_DEAL_ID, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_COMMENT_BY_DEAL_ID, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_COMMENT_BY_DEAL_ID, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_COMMENT_BY_MERCHANT_ID)) {
            if (StaticFunction.isNetworkAvailable(ParallaxService.this)) {
                String merchantId = intent.getStringExtra(EXTRA_MERCHANT_ID);
                String page = intent.getStringExtra(EXTRA_PAGE);
                boolean result = getCommentByMerchantId(merchantId, page);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_COMMENT_BY_MERCHANT_ID, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_COMMENT_BY_MERCHANT_ID, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_COMMENT_BY_MERCHANT_ID, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_DEAL_CLAIMED)) {
            if (StaticFunction.isNetworkAvailable(ParallaxService.this)) {
                String consumer_id = intent.getStringExtra(EXTRA_CONSUMER_ID);
                String page = intent.getStringExtra(EXTRA_PAGE);
                String sort_field = intent.getStringExtra(EXTRA_SORT_FIELD);
                String sort_dir = intent.getStringExtra(EXTRA_SORT_DIR);
                boolean result = getDealsClaimed(consumer_id, page, sort_field, sort_dir);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_CLAIMED, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_CLAIMED, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_DEAL_CLAIMED, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_SEARCH_DEAL)) {
            if (StaticFunction.isNetworkAvailable(ParallaxService.this)) {
                String lat = intent.getStringExtra(EXTRA_LATITUDE);
                String lng = intent.getStringExtra(EXTRA_LONGITUDE);
                String page = intent.getStringExtra(EXTRA_PAGE);
                String keyword = intent.getStringExtra(EXTRA_KEYWORD);
                String deal_type = intent.getStringExtra(EXTRA_DEAL_TYPE);
                String discovery_type = intent.getStringExtra(EXTRA_DISCOVERY_TYPE);
                String min = intent.getStringExtra(EXTRA_MIN);
                String max = intent.getStringExtra(EXTRA_MAX);
                boolean result = searchDeals(lat, lng, page, keyword, deal_type, discovery_type, min, max);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_SEARCH_DEAL, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_SEARCH_DEAL, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_SEARCH_DEAL, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_INDUSTRY_TYPE)) {
            getIndustryTypes();
        } else if (action.equals(ACTION_GET_INVITE_CODE)) {
            if (StaticFunction.isNetworkAvailable(ParallaxService.this)) {
                boolean result = getInviteCode(UserController.getCurrentUser(this).getConsumer_id() + "");
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_INVITE_CODE, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_INVITE_CODE, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_INVITE_CODE, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        }
    }

    private void sendBroadCastReceiver(String action, String result, String message) {
        Intent i = new Intent();
        i.setAction(action);
        i.putExtra(ParallaxService.EXTRA_RESULT, result);
        i.putExtra(ParallaxService.EXTRA_RESULT_MESSAGE, message);
        RESULT_MESSAGE = "";
        if (action.equals(RECEIVER_GET_DEAL_LONG_TERM) || action.equals(RECEIVER_GET_COMMENT_BY_DEAL_ID) || action.equals(RECEIVER_GET_COMMENT_BY_MERCHANT_ID) || action.equals(RECEIVER_GET_DEAL_CLAIMED)) {
            i.putExtra(ParallaxService.EXTRA_RESULT_LAST_PAGE, RESULT_LAST_PAGE);
            RESULT_LAST_PAGE = "0";
            i.putExtra(RealTimeService.EXTRA_RESULT_CURRENT_PAGE, RESULT_CURRENT_PAGE);
            RESULT_CURRENT_PAGE = "0";
        }
        sendBroadcast(i);
    }

    private boolean getDealLongTerm(String latitude_input, String longitude_input, String page, String sort_field, String sort_dir) {
        String consumer_id = "";
        ObjectUser user = UserController.getCurrentUser(ParallaxService.this);
        if (user != null) {
            consumer_id = user.getConsumer_id() + "";
        }
        boolean isSuccess = false;
        API api = new API();
        String result = api.getDealLongOrShortTerm(latitude_input, longitude_input, page, 1, consumer_id, sort_field, sort_dir,"","","");
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            JSONObject objStatus = jsonResponse.getJSONObject("status");
            String type = objStatus.getString("type");
            boolean success;
            if (type.equalsIgnoreCase("Success")) {
                success = true;
            } else {
                success = false;
            }

            if (success) {
                if (page.equalsIgnoreCase("1")) {
                    DealController.clearDealByDealType(ParallaxService.this, 1);
                }
                JSONArray jarr_data = jsonResponse.getJSONArray("data");

                for (int i = 0; i < jarr_data.length(); i++) {
                    JSONObject data = jarr_data.getJSONObject(i);
                    Long deal_id = data.getLong("deal_id");
                    String image = data.getString("image");
                    String image_thumbnail = data.getString("image_thumbnail");
                    String title = data.getString("title");
                    String type_data = data.getString("type");
                    if (!type_data.equalsIgnoreCase("inapp")) {
                        continue;
                    }
                    String start_at = data.getString("start_at");
                    String end_at = data.getString("end_at");
                    Integer max_claim = data.getInt("max_claim");
                    Boolean f_claimed = data.getInt("f_claimed") == 1 ? true : false;
                    Boolean f_call_dibs = data.getInt("f_call_dibs") == 1 ? true : false;
                    Float original_price = Float.parseFloat(data.optString("original_price", "0"));
                    Float purchase_now_price = Float.parseFloat(data.optString("purchase_now_price", "0"));
                    String duration_type = data.getString("duration_type");
                    Boolean is_exclusive = data.getInt("is_exclusive") == 1 ? true : false;

                    JSONObject obj_outlet = data.getJSONObject("outlet");
                    Long outlet_id = obj_outlet.getLong("outlet_id");
                    String latitude = obj_outlet.getString("latitude");
                    String longitude = obj_outlet.getString("longitude");
//                    Float distance = Float.parseFloat(obj_outlet.optString("distance", "0"));
                    String name = obj_outlet.getString("name");
                    String phone = obj_outlet.getString("phone");
                    String address1 = obj_outlet.getString("address1");
                    String address2 = obj_outlet.getString("address2");

                    JSONObject obj_merchant = data.getJSONObject("merchant");
                    Long merchant_id = obj_merchant.getLong("merchant_id");
                    String organization_name = obj_merchant.getString("organization_name");
                    ObjectDealMerchant objectDealMerchant = new ObjectDealMerchant();
                    objectDealMerchant.setMerchant_id(merchant_id);
                    objectDealMerchant.setDeal_id(deal_id);
                    objectDealMerchant.setOrganization_name(organization_name);
                    DealMerchantController.insert(ParallaxService.this, objectDealMerchant);

                    String zip_code = "";
                    String secret_code = "";
                    Outlet outletObj = new Outlet(outlet_id, merchant_id, name, phone, address1, address2, zip_code, latitude, longitude, secret_code);
                    OutletController.insert(ParallaxService.this, outletObj);

                    ObjectDeal objectDeal = new ObjectDeal();
                    objectDeal.setDeal_id(deal_id);
                    objectDeal.setImage(image);
                    objectDeal.setImage_thumbnail(image_thumbnail);
                    objectDeal.setTitle(title);
                    objectDeal.setType(type_data);
                    objectDeal.setStart_at(start_at);
                    objectDeal.setEnd_at(end_at);
                    objectDeal.setMax_claim(max_claim);
                    objectDeal.setOriginal_price(original_price);
                    objectDeal.setPurchase_now_price(purchase_now_price);
                    objectDeal.setOutlet_id(outlet_id);
                    objectDeal.setMerchant_id(merchant_id);
                    objectDeal.setDeal_type(1);
                    objectDeal.setF_claimed(f_claimed);
                    objectDeal.setF_call_dibs(f_call_dibs);
                    objectDeal.setDuration_type(duration_type);
                    objectDeal.setOrganization_name(organization_name);
                    objectDeal.setIs_exclusive(is_exclusive);
                    DealController.insert(ParallaxService.this, objectDeal);
                }

                JSONObject paginator = jsonResponse.getJSONObject("paginator");
                String lastpage = paginator.getString("last_page");
                RESULT_LAST_PAGE = lastpage;
                String current_page = paginator.getString("current_page");
                RESULT_CURRENT_PAGE = current_page;

                isSuccess = true;
            } else {
                String message = objStatus.getString("message");
                RESULT_MESSAGE = message;
                isSuccess = false;
            }
        } catch (JSONException e) {
            isSuccess = false;
        }

        return isSuccess;
    }

    private boolean getCommentByDealId(String dealId, String page) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getCommentByDealId(dealId, page);
//        CommentController.clearAll(ParallaxService.this);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            JSONObject objStatus = jsonResponse.getJSONObject("status");
            String type = objStatus.getString("type");
            boolean success;
            if (type.equalsIgnoreCase("Success")) {
                success = true;
            } else {
                success = false;
            }

            if (success) {
                if (page.equalsIgnoreCase("1")) {
                    CommentController.clearAll(ParallaxService.this);
                }
                JSONArray jarr_data = jsonResponse.getJSONArray("data");
                for (int i = 0; i < jarr_data.length(); i++) {
                    JSONObject comment = jarr_data.getJSONObject(i);
                    Long comment_id = comment.getLong("comment_id");
                    String text = comment.getString("text");
                    String created_at = comment.getString("created_at");

                    JSONObject consumer = comment.getJSONObject("comsumer");
                    Long consumer_id = consumer.getLong("consumer_id");
                    String first_name = consumer.getString("first_name");
                    String last_name = consumer.getString("last_name");
                    String profile_image = consumer.getString("profile_image");

                    Comment commentObj = new Comment(comment_id, text, created_at, consumer_id, first_name, last_name, profile_image, 0l, "");
                    CommentController.insert(ParallaxService.this, commentObj);
                }

                JSONObject paginator = jsonResponse.getJSONObject("paginator");
                String lastpage = paginator.getString("last_page");
                RESULT_LAST_PAGE = lastpage;
                String current_page = paginator.getString("current_page");
                RESULT_CURRENT_PAGE = current_page;

                isSuccess = true;
            } else {
//                String message = objStatus.getString("message");
//                RESULT_MESSAGE = message;
                isSuccess = false;
            }
        } catch (JSONException e) {
            isSuccess = false;
        }

        return isSuccess;
    }

    private boolean getCommentByMerchantId(String merchantId, String page) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getCommentByMerchantId(merchantId, page);
//        CommentController.clearAll(ParallaxService.this);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            JSONObject objStatus = jsonResponse.getJSONObject("status");
            String type = objStatus.getString("type");
            boolean success;
            if (type.equalsIgnoreCase("Success")) {
                success = true;
            } else {
                success = false;
            }

            if (success) {
                if (page.equalsIgnoreCase("1")) {
                    CommentController.clearAll(ParallaxService.this);
                }
                JSONArray jarr_data = jsonResponse.getJSONArray("data");
                for (int i = 0; i < jarr_data.length(); i++) {
                    JSONObject comment = jarr_data.getJSONObject(i);
                    Long comment_id = comment.getLong("comment_id");
                    String text = comment.getString("text");
                    String created_at = comment.getString("created_at");

                    JSONObject consumer = comment.getJSONObject("comsumer");
                    Long consumer_id = consumer.getLong("consumer_id");
                    String first_name = consumer.getString("first_name");
                    String last_name = consumer.getString("last_name");
                    String profile_image = consumer.getString("profile_image");

                    JSONObject deal = comment.getJSONObject("deal");
                    Long deal_id = deal.getLong("deal_id");
                    String title = deal.getString("title");

                    Comment commentObj = new Comment(comment_id, text, created_at, consumer_id, first_name, last_name, profile_image, deal_id, title);
                    CommentController.insert(ParallaxService.this, commentObj);
                }

                JSONObject paginator = jsonResponse.getJSONObject("paginator");
                String lastpage = paginator.getString("last_page");
                RESULT_LAST_PAGE = lastpage;
                String current_page = paginator.getString("current_page");
                RESULT_CURRENT_PAGE = current_page;

                isSuccess = true;
            } else {
//                String message = objStatus.getString("message");
//                RESULT_MESSAGE = message;
                isSuccess = false;
            }
        } catch (JSONException e) {
            isSuccess = false;
        }

        return isSuccess;
    }

    private boolean getDealsClaimed(String consumer_id, String page, String sort_field, String sort_dir) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getDealsClaimed(consumer_id, page, sort_field, sort_dir);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            JSONObject objStatus = jsonResponse.getJSONObject("status");
            String type = objStatus.getString("type");
            boolean success;
            if (type.equalsIgnoreCase("Success")) {
                success = true;
            } else {
                success = false;
            }

            if (success) {
                if (page.equalsIgnoreCase("1")) {
                    DealClaimedController.clearAll(ParallaxService.this);
                }
                JSONArray jarr_data = jsonResponse.getJSONArray("data");

                for (int i = 0; i < jarr_data.length(); i++) {
                    JSONObject data = jarr_data.getJSONObject(i);
                    Long deal_id = data.getLong("deal_id");
                    String title = data.getString("title");
                    String created_at = data.getString("created_at");
                    String consumed_at = data.getString("consumed_at");
                    String validity = data.getString("validity");
                    Boolean f_claimed = data.getInt("f_claimed") == 1 ? true : false;

                    JSONObject merchant = data.getJSONObject("merchant");
                    Long merchant_id = merchant.getLong("merchant_id");
                    String organization_name = merchant.getString("organization_name");

                    DealClaimed dealClaimed = new DealClaimed(deal_id, title, created_at, consumed_at, merchant_id, organization_name, validity, f_claimed);
                    DealClaimedController.insert(ParallaxService.this, dealClaimed);
                }

                JSONObject paginator = jsonResponse.getJSONObject("paginator");
                String lastpage = paginator.getString("last_page");
                RESULT_LAST_PAGE = lastpage;
                String current_page = paginator.getString("current_page");
                RESULT_CURRENT_PAGE = current_page;

                isSuccess = true;
            } else {
//                String message = objStatus.getString("message");
//                RESULT_MESSAGE = message;
                isSuccess = false;
            }
        } catch (JSONException e) {
            isSuccess = false;
        }

        return isSuccess;
    }

    private boolean searchDeals(String latitude_input, String longitude_input, String page, String keyword, String deal_type, String discovery_type, String min, String max) {

        boolean isSuccess = false;
        API api = new API();
        String result = api.searchDeals(latitude_input, longitude_input, page, "", keyword, deal_type, discovery_type, min, max);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            JSONObject objStatus = jsonResponse.getJSONObject("status");
            String type = objStatus.getString("type");
            boolean success;
            if (type.equalsIgnoreCase("Success")) {
                success = true;
            } else {
                success = false;
            }

            if (success) {
                JSONArray jarr_data = jsonResponse.getJSONArray("data");

                for (int i = 0; i < jarr_data.length(); i++) {
                    JSONObject data = jarr_data.getJSONObject(i);
                    Long deal_id = data.getLong("deal_id");
                    String image = data.getString("image");
                    String title = data.getString("title");
                    String type_data = data.getString("type");
                    String start_at = data.getString("start_at");
                    String end_at = data.getString("end_at");
                    Integer max_claim = data.getInt("max_claim");

                    Float original_price = Float.parseFloat(data.optString("original_price", "0"));
                    Float purchase_now_price = Float.parseFloat(data.optString("purchase_now_price", "0"));

                    JSONObject obj_outlet = data.getJSONObject("outlet");
                    Long outlet_id = obj_outlet.getLong("outlet_id");
                    String latitude = obj_outlet.getString("latitude");
                    String longitude = obj_outlet.getString("longitude");
//                    Float distance = Float.parseFloat(obj_outlet.optString("distance", "0"));


                    JSONObject obj_merchant = data.getJSONObject("merchant");
                    Long merchant_id = obj_merchant.getLong("merchant_id");
//                    String organization_name = obj_merchant.getString("organization_name");

                    PermanentDeals objectDeal = new PermanentDeals();
                    objectDeal.setDeal_id(deal_id);
                    objectDeal.setImage(image);//
                    objectDeal.setTitle(title);
                    objectDeal.setType(type_data);//
                    objectDeal.setStart_at(start_at);//
                    objectDeal.setEnd_at(end_at);//
                    objectDeal.setMax_claim(max_claim);//
                    objectDeal.setOriginal_price(original_price);//
                    objectDeal.setPurchase_now_price(purchase_now_price);
                    objectDeal.setOutlet_id(outlet_id);//
                    objectDeal.setMerchant_id(merchant_id);//
                    objectDeal.setDeal_type(2);
                    objectDeal.setLatitude(Double.parseDouble(latitude));
                    objectDeal.setLongitude(Double.parseDouble(longitude));
                    objectDeal.setIsNotified(0);

//                    PermanentController.deleteByDealId(this, objectDeal);
                    PermanentController.insertOrUpdate(this, objectDeal);
                }

                isSuccess = true;
            } else {
                isSuccess = false;
            }
        } catch (JSONException e) {
            isSuccess = false;
        }

        return isSuccess;
    }

    private boolean getIndustryTypes() {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getIndustryTypes();
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            JSONObject objStatus = jsonResponse.getJSONObject("status");
            String type = objStatus.getString("type");
            boolean success;
            if (type.equalsIgnoreCase("Success")) {
                success = true;
            } else {
                success = false;
            }

            if (success) {
                IndustryTypeController.clearAll(ParallaxService.this);
                JSONArray jarr_data = jsonResponse.getJSONArray("data");

                for (int i = 0; i < jarr_data.length(); i++) {
                    JSONObject data = jarr_data.getJSONObject(i);
                    Long id = data.getLong("id");
                    String name = data.getString("name");

                    IndustryType industryType = new IndustryType(id, name);
                    IndustryTypeController.insert(ParallaxService.this, industryType);
                }

                isSuccess = true;
            } else {
                isSuccess = false;
            }
        } catch (JSONException e) {
            isSuccess = false;
        }

        return isSuccess;
    }

    private boolean getInviteCode(String consumer_id) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getInviteCode(consumer_id);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            JSONObject objStatus = jsonResponse.getJSONObject("status");
            String type = objStatus.getString("type");
            boolean success;
            if (type.equalsIgnoreCase("Success")) {
                success = true;
            } else {
                success = false;
            }

            if (success) {
                JSONObject data = jsonResponse.getJSONObject("data");

                String invite_code = data.getString("invite_code");

                SharedPreferences preferences = getSharedPreferences("invite", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("invite_code", invite_code);
                editor.commit();

                isSuccess = true;
            } else {
                isSuccess = false;
            }
        } catch (JSONException e) {
            isSuccess = false;
        }

        return isSuccess;
    }
}
