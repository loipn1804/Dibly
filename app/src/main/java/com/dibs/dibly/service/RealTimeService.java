package com.dibs.dibly.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.dibs.dibly.R;
import com.dibs.dibly.activity.DealDetailActivity;
import com.dibs.dibly.consts.Const;
import com.dibs.dibly.daocontroller.CategoryController;
import com.dibs.dibly.daocontroller.DealAvailableController;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.DealMerchantController;
import com.dibs.dibly.daocontroller.DiscountController;
import com.dibs.dibly.daocontroller.DiscoveryController;
import com.dibs.dibly.daocontroller.FollowingController;
import com.dibs.dibly.daocontroller.MerchantController;
import com.dibs.dibly.daocontroller.MerchantListController;
import com.dibs.dibly.daocontroller.NotifyController;
import com.dibs.dibly.daocontroller.OutletController;
import com.dibs.dibly.daocontroller.PermanentController;
import com.dibs.dibly.daocontroller.PhoneCodeController;
import com.dibs.dibly.daocontroller.ReviewController;
import com.dibs.dibly.daocontroller.TypeDealController;
import com.dibs.dibly.daocontroller.TypeSearchController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.staticfunction.StaticFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import greendao.Category;
import greendao.DealAvailable;
import greendao.Discount;
import greendao.Discovery;
import greendao.Following;
import greendao.Merchant;
import greendao.MerchantList;
import greendao.Notify;
import greendao.ObjectDeal;
import greendao.ObjectDealMerchant;
import greendao.ObjectUser;
import greendao.Outlet;
import greendao.PermanentDeals;
import greendao.PhoneCode;
import greendao.Review;
import greendao.TypeDeal;
import greendao.TypeSearch;

/**
 * Created by USER on 7/6/2015.
 */
public class RealTimeService extends IntentService {

    // action
    public static final String ACTION_LOGIN = "ACTION_LOGIN";
    public static final String ACTION_LOGIN_FB = "ACTION_LOGIN_FB";
    public static final String ACTION_AUTO_LOGIN = "ACTION_AUTO_LOGIN";
    public static final String ACTION_SIGNUP = "ACTION_SIGNUP";
    public static final String ACTION_LOGOUT = "ACTION_LOGOUT";
    public static final String ACTION_ACCOUNT_UPDATE = "ACTION_ACCOUNT_UPDATE";
    public static final String ACTION_PASSWORD_UPDATE = "ACTION_PASSWORD_UPDATE";
    public static final String ACTION_FORGOT_PASSWORD = "ACTION_FORGOT_PASSWORD";
    public static final String ACTION_CHECK_EMAIL_EXIST = "ACTION_CHECK_EMAIL_EXIST";
    public static final String ACTION_GET_DEAL_SHORT_TERM = "ACTION_GET_DEAL_SHORT_TERM";
    public static final String ACTION_GET_DEAL_DETAIL = "ACTION_GET_DEAL_DETAIL";
    public static final String ACTION_GET_DEAL_DETAIL_TO_PROMPT_CLAIMED_SUCCESS = "ACTION_GET_DEAL_DETAIL_TO_PROMPT_CLAIMED_SUCCESS";
    public static final String ACTION_GET_DEAL_DETAIL_TO_NOTIFY_FOWLOW = "ACTION_GET_DEAL_DETAIL_TO_NOTIFY_FOWLOW";
    public static final String ACTION_GET_DEAL_DETAIL_TO_NOTIFY_NEW_DEAL = "ACTION_GET_DEAL_DETAIL_TO_NOTIFY_NEW_DEAL";
    public static final String ACTION_GET_MERCHANT = "ACTION_GET_MERCHANT";
    public static final String ACTION_GET_MERCHANT_LIST = "ACTION_GET_MERCHANT_LIST";
    public static final String ACTION_GET_DISCOVERY_TITLE = "ACTION_GET_DISCOVERY_TITLE";
    public static final String ACTION_CALL_DIB = "ACTION_CALL_DIB";
    public static final String ACTION_CLAIMED = "ACTION_CLAIMED";
    public static final String ACTION_GET_DEAL_AVAILABLE = "ACTION_GET_DEAL_AVAILABLE";
    public static final String ACTION_GET_DEAL_AVAILABLE_BY_DEAL_ID = "ACTION_GET_DEAL_AVAILABLE_BY_DEAL_ID";
    public static final String ACTION_SEARCH_DEAL = "ACTION_SEARCH_DEAL";
    public static final String ACTION_GET_FOLLOWING = "ACTION_GET_FOLLOWING";
    public static final String ACTION_STOP_FOLLOWING_MERCHANT = "ACTION_STOP_FOLLOWING_MERCHANT";
    public static final String ACTION_FOLLOWING_MERCHANT = "ACTION_FOLLOWING_MERCHANT";
    public static final String ACTION_LIKE_DEAL = "ACTION_LIKE_DEAL";
    public static final String ACTION_UNLIKE_DEAL = "ACTION_UNLIKE_DEAL";
    public static final String ACTION_GET_TYPE_SEARCH = "ACTION_GET_TYPE_SEARCH";
    public static final String ACTION_ADD_COMMENT_DEAL = "ACTION_ADD_COMMENT_DEAL";
    public static final String ACTION_GET_DEAL_BY_DISCOVERY = "ACTION_GET_DEAL_BY_DISCOVERY";
    public static final String ACTION_GET_DEAL_BY_MERCHANT = "ACTION_GET_DEAL_BY_MERCHANT";
    public static final String ACTION_GET_DEAL_BY_LIST_ID = "ACTION_GET_DEAL_BY_LIST_ID";
    public static final String ACTION_GET_KEY_CLIENT_TOKEN = "ACTION_GET_KEY_CLIENT_TOKEN";
    public static final String ACTION_CREATE_PAYMENT = "ACTION_CREATE_PAYMENT";
    public static final String ACTION_NOTI_NEW_DEAL = "ACTION_NOTI_NEW_DEAL";
    public static final String ACTION_DELETE_DEAL = "ACTION_DELETE_DEAL";
    public static final String ACTION_GET_DISCOUNT_AVAILABLE = "ACTION_GET_DISCOUNT_AVAILABLE";
    public static final String ACTION_GET_PHONE_CODE = "ACTION_GET_PHONE_CODE";
    public static final String ACTION_VERIFY_PHONE = "ACTION_VERIFY_PHONE";
    public static final String ACTION_REQUEST_NEW_CODE = "ACTION_REQUEST_NEW_CODE";
    public static final String ACTION_GET_FILTER_DEAL = "ACTION_GET_FILTER_DEAL";
    public static final String ACTION_DEAL_FOLLOWING = "ACTION_DEAL_FOLLOWING";
    public static final String ACTION_GET_DEAL_CLAIMED = "ACTION_GET_DEAL_CLAIMED";
    public static final String ACTION_GET_MERCHANT_REVIEW = "ACTION_GET_MERCHANT_REVIEW";
    public static final String ACTION_REPORT = "ACTION_REPORT";
    public static final String ACTION_GET_MERCHANT_FOLLOWING = "ACTION_GET_MERCHANT_FOLLOWING";
    // receiver
    public static final String RECEIVER_LOGIN = "RECEIVER_LOGIN";
    public static final String RECEIVER_LOGIN_FB = "RECEIVER_LOGIN_FB";
    public static final String RECEIVER_AUTO_LOGIN = "RECEIVER_AUTO_LOGIN";
    public static final String RECEIVER_SIGNUP = "RECEIVER_SIGNUP";
    public static final String RECEIVER_LOGOUT = "RECEIVER_LOGOUT";
    public static final String RECEIVER_ACCOUNT_UPDATE = "RECEIVER_ACCOUNT_UPDATE";
    public static final String RECEIVER_PASSWORD_UPDATE = "RECEIVER_PASSWORD_UPDATE";
    public static final String RECEIVER_FORGOT_PASSWORD = "RECEIVER_FORGOT_PASSWORD";
    public static final String RECEIVER_CHECK_EMAIL_EXIST = "RECEIVER_CHECK_EMAIL_EXIST";
    public static final String RECEIVER_GET_DEAL_SHORT_TERM = "RECEIVER_GET_DEAL_SHORT_TERM";
    public static final String RECEIVER_GET_DEAL_DETAIL = "RECEIVER_GET_DEAL_DETAIL";
    public static final String RECEIVER_GET_DEAL_DETAIL_TO_PROMPT_CLAIMED_SUCCESS = "RECEIVER_GET_DEAL_DETAIL_TO_PROMPT_CLAIMED_SUCCESS";
    public static final String RECEIVER_GET_MERCHANT = "RECEIVER_GET_MERCHANT";
    public static final String RECEIVER_GET_MERCHANT_LIST = "RECEIVER_GET_MERCHANT_LIST";
    public static final String RECEIVER_GET_DISCOVERY_TITLE = "RECEIVER_GET_DISCOVERY_TITLE";
    public static final String RECEIVER_CALL_DIB = "RECEIVER_CALL_DIB";
    public static final String RECEIVER_CLAIMED = "RECEIVER_CLAIMED";
    public static final String RECEIVER_GET_DEAL_AVAILABLE = "RECEIVER_GET_DEAL_AVAILABLE";
    public static final String RECEIVER_GET_DEAL_AVAILABLE_BY_DEAL_ID = "RECEIVER_GET_DEAL_AVAILABLE_BY_DEAL_ID";
    public static final String RECEIVER_SEARCH_DEAL = "RECEIVER_SEARCH_DEAL";
    public static final String RECEIVER_GET_FOLLOWING = "RECEIVER_GET_FOLLOWING";
    public static final String RECEIVER_STOP_FOLLOWING_MERCHANT = "RECEIVER_STOP_FOLLOWING_MERCHANT";
    public static final String RECEIVER_FOLLOWING_MERCHANT = "RECEIVER_FOLLOWING_MERCHANT";
    public static final String RECEIVER_LIKE_DEAL = "RECEIVER_LIKE_DEAL";
    public static final String RECEIVER_UNLIKE_DEAL = "RECEIVER_UNLIKE_DEAL";
    public static final String RECEIVER_GET_TYPE_SEARCH = "RECEIVER_GET_TYPE_SEARCH";
    public static final String RECEIVER_ADD_COMMENT_DEAL = "RECEIVER_ADD_COMMENT_DEAL";
    public static final String RECEIVER_GET_DEAL_BY_DISCOVERY = "RECEIVER_GET_DEAL_BY_DISCOVERY";
    public static final String RECEIVER_GET_DEAL_BY_MERCHANT = "RECEIVER_GET_DEAL_BY_MERCHANT";
    public static final String RECEIVER_GET_DEAL_BY_LIST_ID = "RECEIVER_GET_DEAL_BY_LIST_ID";
    public static final String RECEIVER_GET_KEY_CLIENT_TOKEN = "RECEIVER_GET_KEY_CLIENT_TOKEN";
    public static final String RECEIVER_CREATE_PAYMENT = "RECEIVER_CREATE_PAYMENT";
    public static final String RECEIVER_NOTI_NEW_DEAL = "RECEIVER_NOTI_NEW_DEAL";
    public static final String RECEIVER_DELETE_DEAL = "RECEIVER_DELETE_DEAL";
    public static final String RECEIVER_GET_DISCOUNT_AVAILABLE = "RECEIVER_GET_DISCOUNT_AVAILABLE";
    public static final String RECEIVER_GET_PHONE_CODE = "RECEIVER_GET_PHONE_CODE";
    public static final String RECEIVER_VERIFY_PHONE = "RECEIVER_VERIFY_PHONE";
    public static final String RECEIVER_REQUEST_NEW_CODE = "RECEIVER_REQUEST_NEW_CODE";
    public static final String RECEIVER_GET_FILTER_DEAL = "RECEIVER_GET_FILTER_DEAL";
    public static final String RECEIVER_GET_DEAL_FOLLOWING = "RECEIVER_DEAL_FOLLOWING";
    public static final String RECEIVER_GET_DEAL_CLAIMED = "RECEIVER_GET_DEAL_CLAIMED";
    public static final String RECEIVER_GET_MERCHANT_REVIEW = "RECEIVER_GET_MERCHANT_REVIEW";
    public static final String RECEIVER_REPORT = "RECEIVER_REPORT";
    public static final String RECEIVER_GET_MERCHANT_FOLLOWING = "RECEIVER_GET_MERCHANT_FOLLOWING";

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
    public static final String EXTRA_RESULT_DEAL_TITLE = "EXTRA_RESULT_DEAL_TITLE";
    public static String RESULT_DEAL_TITLE = "";
    public static final String EXTRA_RESULT_MERCHANT_NAME = "EXTRA_RESULT_MERCHANT_NAME";
    public static String RESULT_MERCHANT_NAME = "";
    public static final String EXTRA_RESULT_KEY_CLIENT_TOKEN = "EXTRA_RESULT_KEY_CLIENT_TOKEN";
    public static String RESULT_KEY_CLIENT_TOKEN = "";
    public static final String EXTRA_RESULT_PAYMENT_TRANS_ID = "EXTRA_RESULT_PAYMENT_TRANS_ID";
    public static String RESULT_PAYMENT_TRANS_ID = "";
    public static final String EXTRA_RESULT_DEAL_ID = "EXTRA_RESULT__DEAL_ID";
    public static String RESULT_DEAL_ID = "0";

    // put extra
    public static final String EXTRA_USER_EMAIL = "EXTRA_USER_EMAIL";
    public static final String EXTRA_USER_PASS = "EXTRA_USER_PASS";
    public static final String EXTRA_USER_NEW_PASS = "EXTRA_USER_NEW_PASS";
    public static final String EXTRA_USER_FIRSTNAME = "EXTRA_USER_FIRSTNAME";
    public static final String EXTRA_USER_LASTNAME = "EXTRA_USER_LASTNAME";
    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";
    public static final String EXTRA_USER_GENDER = "EXTRA_USER_GENDER";
    public static final String EXTRA_USER_DOB = "EXTRA_USER_DOB";
    public static final String EXTRA_USER_PHONECODE = "EXTRA_USER_PHONECODE";
    public static final String EXTRA_USER_PHONENUMBER = "EXTRA_USER_PHONENUMBER";
    public static final String EXTRA_USER_NAME = "EXTRA_USER_NAME";
    public static final String EXTRA_REF_CODE = "EXTRA_REFCODE";

    public static final String EXTRA_LATITUDE = "EXTRA_LATITUDE";
    public static final String EXTRA_LONGITUDE = "EXTRA_LONGITUDE";
    public static final String EXTRA_PAGE = "EXTRA_PAGE";
    public static final String EXTRA_DEAL_ID = "EXTRA_DEAL_ID";
    public static final String EXTRA_DEAL_TITLE = "EXTRA_DEAL_TITLE";
    public static final String EXTRA_MERCHANT_ID = "EXTRA_MERCHANT_ID";
    public static final String EXTRA_OUTLET_ID = "EXTRA_OUTLET_ID";
    public static final String EXTRA_CONSUMER_ID = "EXTRA_CONSUMER_ID";
    public static final String EXTRA_UUID = "EXTRA_UUID";
    public static final String EXTRA_FOLLOWING_ID = "EXTRA_FOLLOWING_ID";
    public static final String EXTRA_KEYWORD = "EXTRA_KEYWORD";
    public static final String EXTRA_DEAL_TYPE = "EXTRA_DEAL_TYPE";
    public static final String EXTRA_DISCOVERY_TYPE = "EXTRA_DISCOVERY_TYPE";
    public static final String EXTRA_DISCOVERY_ID = "EXTRA_DISCOVERY_ID";
    public static final String EXTRA_MIN = "EXTRA_MIN";
    public static final String EXTRA_MAX = "EXTRA_MAX";
    public static final String EXTRA_COMMENT_DEAL_TEXT = "EXTRA_COMMENT_DEAL_TEXT";
    public static final String EXTRA_FB_ID = "EXTRA_FB_ID";
    public static final String EXTRA_FB_USERNAME = "EXTRA_FB_USERNAME";
    public static final String EXTRA_FB_FIRSTNAME = "EXTRA_FB_FIRSTNAME";
    public static final String EXTRA_FB_LASTNAME = "EXTRA_FB_LASTNAME";
    public static final String EXTRA_FB_GENDER = "EXTRA_FB_GENDER";
    public static final String EXTRA_FB_EMAIL = "EXTRA_FB_EMAIL";
    public static final String EXTRA_PAYMENT_NONCE = "EXTRA_PAYMENT_NONCE";
    public static final String EXTRA_PAYMENT_AMOUNT = "EXTRA_PAYMENT_AMOUNT";
    public static final String EXTRA_PAYMENT_TRANS_ID = "EXTRA_PAYMENT_TRANS_ID";
    public static final String EXTRA_MERCHANT_NAME_SEARCH = "EXTRA_MERCHANT_NAME_SEARCH";
    public static final String EXTRA_SORT_FIELD = "EXTRA_SORT_FIELD";
    public static final String EXTRA_SORT_DIR = "EXTRA_SORT_DIR";
    public static final String EXTRA_MESSAGE_NOTIFY = "EXTRA_MESSAGE_NOTIFY";
    public static final String EXTRA_LIST_ID_DEAL = "EXTRA_LIST_ID_DEAL";
    public static final String EXTRA_VERIFY_CODE = "EXTRA_VERIFY_CODE";
    public static final String EXTRA_CATEGORIES = "EXTRA_CATEGORIES";
    public static final String EXTRA_TYPE_DEAL = "EXTRA_TYPE_DEAL";
    public static final String EXTRA_REVIEW_TEXT = "EXTRA_REVIEW_TEXT";
    public static final String EXTRA_REPORT_TEXT = "EXTRA_REPORT_TEXT";
    private long merchantId_follow;

    public RealTimeService() {
        super(RealTimeService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION_LOGIN)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String email = intent.getStringExtra(EXTRA_USER_EMAIL);
                String pass = intent.getStringExtra(EXTRA_USER_PASS);
                boolean result = login(email, pass);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_LOGIN, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_LOGIN, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_LOGIN, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_AUTO_LOGIN)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String email = intent.getStringExtra(EXTRA_USER_EMAIL);
                String pass = intent.getStringExtra(EXTRA_USER_PASS);
                boolean result = login(email, pass);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_AUTO_LOGIN, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_AUTO_LOGIN, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_AUTO_LOGIN, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_SIGNUP)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String name = intent.getStringExtra(EXTRA_USER_NAME);
                String phoneCode = intent.getStringExtra(EXTRA_USER_PHONECODE);
                String phoneNumber = intent.getStringExtra(EXTRA_USER_PHONENUMBER);
                String email = intent.getStringExtra(EXTRA_USER_EMAIL);
                String pass = intent.getStringExtra(EXTRA_USER_PASS);


                boolean result = signUp(name, phoneCode, phoneNumber, email, pass);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_SIGNUP, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_SIGNUP, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_SIGNUP, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_LOGIN_FB)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String fb_id = intent.getStringExtra(EXTRA_FB_ID);
                String username = intent.getStringExtra(EXTRA_FB_USERNAME);
                String firstname = intent.getStringExtra(EXTRA_FB_FIRSTNAME);
                String lastname = intent.getStringExtra(EXTRA_FB_LASTNAME);
                String gender = intent.getStringExtra(EXTRA_FB_GENDER);
                String email = intent.getStringExtra(EXTRA_FB_EMAIL);
                boolean result = loginFacebook(fb_id, username, firstname, lastname, gender, email);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_LOGIN_FB, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_LOGIN_FB, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_LOGIN_FB, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_ACCOUNT_UPDATE)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String user_id = intent.getStringExtra(EXTRA_USER_ID);
                String fullName = intent.getStringExtra(EXTRA_USER_NAME);
                String phoneCode = intent.getStringExtra(EXTRA_USER_PHONECODE);
                String phoneNumber = intent.getStringExtra(EXTRA_USER_PHONENUMBER);
                String email = intent.getStringExtra(EXTRA_USER_EMAIL);
                String gender = intent.getStringExtra(EXTRA_USER_GENDER);
                String dob = intent.getStringExtra(EXTRA_USER_DOB);
                String ref_code = intent.getStringExtra(EXTRA_REF_CODE);
                boolean result = updateAccount(user_id, fullName, phoneCode, phoneNumber, email, gender, dob, ref_code);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_ACCOUNT_UPDATE, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_ACCOUNT_UPDATE, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_ACCOUNT_UPDATE, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_PASSWORD_UPDATE)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String user_id = intent.getStringExtra(EXTRA_USER_ID);
                String password = intent.getStringExtra(EXTRA_USER_PASS);
                String new_password = intent.getStringExtra(EXTRA_USER_NEW_PASS);
                boolean result = changePassword(user_id, password, new_password);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_PASSWORD_UPDATE, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_PASSWORD_UPDATE, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_PASSWORD_UPDATE, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_FORGOT_PASSWORD)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String email = intent.getStringExtra(EXTRA_USER_EMAIL);
                boolean result = forgotPassword(email);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_FORGOT_PASSWORD, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_FORGOT_PASSWORD, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_FORGOT_PASSWORD, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_LOGOUT)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String user_id = intent.getStringExtra(EXTRA_USER_ID);
                boolean result = logout(user_id);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_LOGOUT, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_LOGOUT, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_LOGOUT, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_CHECK_EMAIL_EXIST)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String email = intent.getStringExtra(EXTRA_USER_EMAIL);
                boolean result = checkEmailExist(email);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_CHECK_EMAIL_EXIST, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_CHECK_EMAIL_EXIST, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_CHECK_EMAIL_EXIST, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_DEAL_SHORT_TERM)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String lat = intent.getStringExtra(EXTRA_LATITUDE);
                String lng = intent.getStringExtra(EXTRA_LONGITUDE);
                String page = intent.getStringExtra(EXTRA_PAGE);
                String sort_field = intent.getStringExtra(EXTRA_SORT_FIELD);
                String sort_dir = intent.getStringExtra(EXTRA_SORT_DIR);
                String keyword = intent.getStringExtra(EXTRA_KEYWORD);
                String categories = intent.getStringExtra(EXTRA_CATEGORIES);
                String typeDeals = intent.getStringExtra(EXTRA_TYPE_DEAL);

                boolean result = getDealShortTerm(lat, lng, page, sort_field, sort_dir, keyword, categories, typeDeals);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_SHORT_TERM, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_SHORT_TERM, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_DEAL_SHORT_TERM, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_DEAL_FOLLOWING)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {

                String page = intent.getStringExtra(EXTRA_PAGE);
                boolean result = getDealFollowing(page);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_FOLLOWING, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_FOLLOWING, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_DEAL_FOLLOWING, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_DEAL_DETAIL)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String dealId = intent.getStringExtra(EXTRA_DEAL_ID);
                String outlet_id = intent.getStringExtra(EXTRA_OUTLET_ID);
                boolean result = getDealDetailByDealId(dealId, outlet_id);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_DETAIL, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_DETAIL, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_DEAL_DETAIL, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_DEAL_DETAIL_TO_NOTIFY_FOWLOW)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String dealId = intent.getStringExtra(EXTRA_DEAL_ID);
                String outletId = intent.getStringExtra(EXTRA_OUTLET_ID);
                String message_notify = intent.getStringExtra(EXTRA_MESSAGE_NOTIFY);
                boolean result = getDealDetailByDealIdToNotifyFollow(dealId, outletId);
                if (result) {
                    if (UserController.isLogin(RealTimeService.this)) {
                        if (UserController.getCurrentUser(RealTimeService.this).getIsConsumer()) {
//                            sendBroadNotiNewDeal(RECEIVER_NOTI_NEW_DEAL);
                            NotifyController.insert(RealTimeService.this, new Notify(Long.parseLong(dealId)));
                            if (NotifyController.getSize(RealTimeService.this) < 3) {
                                StaticFunction.sendNotification(RealTimeService.this, message_notify, Long.parseLong(dealId), Integer.parseInt(dealId), merchantId_follow);
                            } else {
                                List<Notify> notifyList = NotifyController.getAll(RealTimeService.this);
                                for (Notify notify : notifyList) {
                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.cancel(Integer.parseInt(notify.getDeal_id() + ""));
                                }
                                String mes = "There are " + NotifyController.getSize(RealTimeService.this) + " new deals.";
                                StaticFunction.sendNotificationMultiDeal(RealTimeService.this, mes, "0", 0, R.drawable.ic_launcher);
                            }
                        }
                    }
                }
            }
        } else if (action.equals(ACTION_GET_DEAL_DETAIL_TO_NOTIFY_NEW_DEAL)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String dealId = intent.getStringExtra(EXTRA_DEAL_ID);
                String outletId = intent.getStringExtra(EXTRA_OUTLET_ID);
                boolean result = getDealDetailByDealIdToNotifyNewDeal(dealId, outletId);
                if (result) {
                    if (UserController.isLogin(RealTimeService.this)) {
                        if (UserController.getCurrentUser(RealTimeService.this).getIsConsumer()) {
                            sendBroadNotiNewDeal(RECEIVER_NOTI_NEW_DEAL);
                        }
                    }
                }
            }
        } else if (action.equals(ACTION_DELETE_DEAL)) {
            sendBroadNotiNewDeal(RECEIVER_DELETE_DEAL);
        } else if (action.equals(ACTION_GET_MERCHANT)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String merchantId = intent.getStringExtra(EXTRA_MERCHANT_ID);
                String dealId = intent.getStringExtra(EXTRA_DEAL_ID);
                boolean result = getMerchantById(merchantId, dealId);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_MERCHANT, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_MERCHANT, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_MERCHANT, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_MERCHANT_LIST)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String name = intent.getStringExtra(EXTRA_MERCHANT_NAME_SEARCH);
                String page = intent.getStringExtra(EXTRA_PAGE);
                String sort_dir = intent.getStringExtra(EXTRA_SORT_DIR);
                boolean result = getAllMerchant(name, page, sort_dir);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_MERCHANT_LIST, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_MERCHANT_LIST, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_MERCHANT_LIST, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_DISCOVERY_TITLE)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                boolean result = getDiscoveryTitle();
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_DISCOVERY_TITLE, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_DISCOVERY_TITLE, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_DISCOVERY_TITLE, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_CALL_DIB)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String deal_id = intent.getStringExtra(EXTRA_DEAL_ID);
                String outlet_id = intent.getStringExtra(EXTRA_OUTLET_ID);
                String consumer_id = intent.getStringExtra(EXTRA_CONSUMER_ID);
                String trans_id = intent.getStringExtra(EXTRA_PAYMENT_TRANS_ID);
                boolean result = callDibs(deal_id, consumer_id, trans_id, outlet_id);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_CALL_DIB, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_CALL_DIB, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_CALL_DIB, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_CLAIMED)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String uuid = intent.getStringExtra(EXTRA_UUID);
                String deal_id = intent.getStringExtra(EXTRA_DEAL_ID);
                String consumer_id = intent.getStringExtra(EXTRA_CONSUMER_ID);
                String merchant_id = intent.getStringExtra(EXTRA_MERCHANT_ID);
                boolean result = callClaimed(uuid, deal_id, consumer_id, merchant_id);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_CLAIMED, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_CLAIMED, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_CLAIMED, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_DEAL_AVAILABLE)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String consumer_id = intent.getStringExtra(EXTRA_CONSUMER_ID);
                boolean result = getDealsAvailableV2(consumer_id);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_AVAILABLE, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_AVAILABLE, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_DEAL_AVAILABLE, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_DEAL_AVAILABLE_BY_DEAL_ID)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String consumer_id = intent.getStringExtra(EXTRA_CONSUMER_ID);
                String deal_id = intent.getStringExtra(EXTRA_DEAL_ID);
                Long outletId = intent.getLongExtra(EXTRA_OUTLET_ID, 0);
                boolean result = getDealsAvailableByDealId(consumer_id, deal_id, outletId);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_AVAILABLE_BY_DEAL_ID, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_AVAILABLE_BY_DEAL_ID, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_DEAL_AVAILABLE_BY_DEAL_ID, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_DEAL_DETAIL_TO_PROMPT_CLAIMED_SUCCESS)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String dealId = intent.getStringExtra(EXTRA_DEAL_ID);
                boolean result = getDealDetailByDealIdToPromptClaimedSuccess(dealId);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_DETAIL_TO_PROMPT_CLAIMED_SUCCESS, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_DETAIL_TO_PROMPT_CLAIMED_SUCCESS, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_DEAL_DETAIL_TO_PROMPT_CLAIMED_SUCCESS, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_SEARCH_DEAL)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
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
        } else if (action.equals(ACTION_GET_FOLLOWING)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String consumerID = intent.getStringExtra(EXTRA_CONSUMER_ID);
                boolean result = getFollowingList(consumerID);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_FOLLOWING, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_FOLLOWING, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_FOLLOWING, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_STOP_FOLLOWING_MERCHANT)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String consumerID = intent.getStringExtra(EXTRA_CONSUMER_ID);
                String merchantID = intent.getStringExtra(EXTRA_MERCHANT_ID);
                boolean result = stopFollowingMerchant(consumerID, merchantID);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_STOP_FOLLOWING_MERCHANT, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_STOP_FOLLOWING_MERCHANT, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_STOP_FOLLOWING_MERCHANT, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_FOLLOWING_MERCHANT)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String consumerID = intent.getStringExtra(EXTRA_CONSUMER_ID);
                String merchantID = intent.getStringExtra(EXTRA_MERCHANT_ID);
                boolean result = followMerchant(consumerID, merchantID);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_FOLLOWING_MERCHANT, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_FOLLOWING_MERCHANT, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_FOLLOWING_MERCHANT, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_LIKE_DEAL)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String consumerID = intent.getStringExtra(EXTRA_CONSUMER_ID);
                String merchantID = intent.getStringExtra(EXTRA_MERCHANT_ID);
                String dealID = intent.getStringExtra(EXTRA_DEAL_ID);
                String review = intent.getStringExtra(EXTRA_REVIEW_TEXT);
                boolean result = likeDeal(consumerID, merchantID, dealID, review);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_LIKE_DEAL, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_LIKE_DEAL, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_LIKE_DEAL, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_UNLIKE_DEAL)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String consumerID = intent.getStringExtra(EXTRA_CONSUMER_ID);
                String merchantID = intent.getStringExtra(EXTRA_MERCHANT_ID);
                String dealID = intent.getStringExtra(EXTRA_DEAL_ID);
                String review = intent.getStringExtra(EXTRA_REVIEW_TEXT);
                boolean result = unLikeDeal(consumerID, merchantID, dealID, review);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_UNLIKE_DEAL, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_UNLIKE_DEAL, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_UNLIKE_DEAL, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_TYPE_SEARCH)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                boolean result = fetchAllDiscovery();
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_TYPE_SEARCH, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_TYPE_SEARCH, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_TYPE_SEARCH, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_ADD_COMMENT_DEAL)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String consumerID = intent.getStringExtra(EXTRA_CONSUMER_ID);
                String dealID = intent.getStringExtra(EXTRA_DEAL_ID);
                String text = intent.getStringExtra(EXTRA_COMMENT_DEAL_TEXT);
                boolean result = addCommentDeal(consumerID, dealID, text);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_ADD_COMMENT_DEAL, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_ADD_COMMENT_DEAL, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_ADD_COMMENT_DEAL, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_DEAL_BY_DISCOVERY)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String discovery_id = intent.getStringExtra(EXTRA_DISCOVERY_ID);
                String lat = intent.getStringExtra(EXTRA_LATITUDE);
                String lng = intent.getStringExtra(EXTRA_LONGITUDE);
                String page = intent.getStringExtra(EXTRA_PAGE);
                boolean result = getDealsByDiscovery(discovery_id, lat, lng, page);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_BY_DISCOVERY, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_BY_DISCOVERY, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_DEAL_BY_DISCOVERY, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_DEAL_BY_MERCHANT)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String merchant_id = intent.getStringExtra(EXTRA_MERCHANT_ID);
                String lat = intent.getStringExtra(EXTRA_LATITUDE);
                String lng = intent.getStringExtra(EXTRA_LONGITUDE);
                String page = intent.getStringExtra(EXTRA_PAGE);
                boolean result = getDealsByMerchant(merchant_id, lat, lng, page);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_BY_MERCHANT, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_BY_MERCHANT, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_DEAL_BY_MERCHANT, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_DEAL_BY_LIST_ID)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String ids = intent.getStringExtra(EXTRA_LIST_ID_DEAL);
                String outlet_id = intent.getStringExtra(EXTRA_OUTLET_ID);
                boolean result = getDealsFromListId(ids, outlet_id);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_BY_LIST_ID, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_BY_LIST_ID, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_DEAL_BY_LIST_ID, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_KEY_CLIENT_TOKEN)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                boolean result = getKeyClientToken();
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_KEY_CLIENT_TOKEN, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_KEY_CLIENT_TOKEN, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_KEY_CLIENT_TOKEN, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_CREATE_PAYMENT)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String nonce = intent.getStringExtra(EXTRA_PAYMENT_NONCE);
                String amount = intent.getStringExtra(EXTRA_PAYMENT_AMOUNT);
                String consumer_id = intent.getStringExtra(EXTRA_CONSUMER_ID);
                String merchant_id = intent.getStringExtra(EXTRA_MERCHANT_ID);
                boolean result = createAPayment(nonce, amount, consumer_id, merchant_id);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_CREATE_PAYMENT, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_CREATE_PAYMENT, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_CREATE_PAYMENT, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_DISCOUNT_AVAILABLE)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String consumer_id = intent.getStringExtra(EXTRA_CONSUMER_ID);
                boolean result = getDiscountsAvaiable(consumer_id);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_DISCOUNT_AVAILABLE, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_DISCOUNT_AVAILABLE, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_DISCOUNT_AVAILABLE, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_PHONE_CODE)) {

            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                boolean result = getCountries();
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_PHONE_CODE, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_PHONE_CODE, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_PHONE_CODE, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_VERIFY_PHONE)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String consumerID = intent.getStringExtra(EXTRA_CONSUMER_ID);
                String code = intent.getStringExtra(EXTRA_VERIFY_CODE);

                boolean result = verifyPhone(code, consumerID);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_VERIFY_PHONE, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_VERIFY_PHONE, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_VERIFY_PHONE, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_REQUEST_NEW_CODE)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String consumerID = intent.getStringExtra(EXTRA_CONSUMER_ID);
                boolean result = requestNewCode(consumerID);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_REQUEST_NEW_CODE, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_REQUEST_NEW_CODE, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_REQUEST_NEW_CODE, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_FILTER_DEAL)) {

            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                boolean result = getFilterDeal();
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_FILTER_DEAL, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_FILTER_DEAL, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_FILTER_DEAL, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_DEAL_CLAIMED)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                String consumer_id = intent.getStringExtra(EXTRA_CONSUMER_ID);
                String page = intent.getStringExtra(EXTRA_PAGE);
                String sort_field = intent.getStringExtra(EXTRA_SORT_FIELD);
                String sort_dir = intent.getStringExtra(EXTRA_SORT_DIR);
                boolean result = getDealsClaimed(consumer_id);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_CLAIMED, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_DEAL_CLAIMED, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_DEAL_CLAIMED, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_MERCHANT_REVIEW)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                long merchant_id = intent.getLongExtra(EXTRA_MERCHANT_ID, 0);
                int page = intent.getIntExtra(EXTRA_PAGE, 1);

                boolean result = getMerchantReviews(merchant_id, page);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_MERCHANT_REVIEW, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_MERCHANT_REVIEW, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_MERCHANT_REVIEW, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_REPORT)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {
                long dealId = intent.getLongExtra(EXTRA_DEAL_ID, 0);
                long merchantId = intent.getLongExtra(EXTRA_MERCHANT_ID, 0);
                long outletId = intent.getLongExtra(EXTRA_OUTLET_ID, 0);
                String text = intent.getStringExtra(EXTRA_REPORT_TEXT);

                boolean result = reportDeal(String.valueOf(dealId), String.valueOf(merchantId), String.valueOf(outletId), text);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_REPORT, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_REPORT, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_REPORT, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_MERCHANT_FOLLOWING)) {
            if (StaticFunction.isNetworkAvailable(RealTimeService.this)) {

                String consumer_id = intent.getStringExtra(EXTRA_CONSUMER_ID);
                int page = intent.getIntExtra(EXTRA_PAGE, 1);
                boolean result = getMerchantsFollowing(consumer_id, page);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_MERCHANT_FOLLOWING, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_MERCHANT_FOLLOWING, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_MERCHANT_FOLLOWING, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        }
    }

    private void sendBroadCastReceiver(String action, String result, String message) {
        Intent i = new Intent();
        i.setAction(action);
        i.putExtra(RealTimeService.EXTRA_RESULT, result);
        i.putExtra(RealTimeService.EXTRA_RESULT_MESSAGE, message);
        RESULT_MESSAGE = "";
        if (action.equals(RECEIVER_GET_MERCHANT_FOLLOWING) || action.equals(RECEIVER_GET_DEAL_SHORT_TERM) || action.equals(RECEIVER_GET_DEAL_FOLLOWING) || action.equals(RECEIVER_GET_DEAL_BY_DISCOVERY) || action.equals(RECEIVER_GET_DEAL_BY_MERCHANT) || action.equals(RECEIVER_GET_MERCHANT_LIST) || action.equals(RECEIVER_GET_DEAL_BY_LIST_ID) || action.equals(RECEIVER_GET_MERCHANT_REVIEW)) {
            i.putExtra(RealTimeService.EXTRA_RESULT_LAST_PAGE, RESULT_LAST_PAGE);
            RESULT_LAST_PAGE = "0";
            i.putExtra(RealTimeService.EXTRA_RESULT_CURRENT_PAGE, RESULT_CURRENT_PAGE);
            RESULT_CURRENT_PAGE = "0";
        }
        if (action.equals(RECEIVER_GET_DEAL_DETAIL_TO_PROMPT_CLAIMED_SUCCESS)) {
            i.putExtra(RealTimeService.EXTRA_RESULT_DEAL_TITLE, RESULT_DEAL_TITLE);
            i.putExtra(RealTimeService.EXTRA_RESULT_MERCHANT_NAME, RESULT_MERCHANT_NAME);
            RESULT_DEAL_TITLE = "";
            RESULT_MERCHANT_NAME = "";
        }
        if (action.equals(RECEIVER_GET_KEY_CLIENT_TOKEN)) {
            i.putExtra(EXTRA_RESULT_KEY_CLIENT_TOKEN, RESULT_KEY_CLIENT_TOKEN);
            RESULT_KEY_CLIENT_TOKEN = "";
        }
        if (action.equals(RECEIVER_CREATE_PAYMENT)) {
            i.putExtra(EXTRA_RESULT_PAYMENT_TRANS_ID, RESULT_PAYMENT_TRANS_ID);
            RESULT_PAYMENT_TRANS_ID = "";
        }
        sendBroadcast(i);
    }

    private void sendBroadNotiNewDeal(String action) {
        Intent i = new Intent();
        i.setAction(action);
        i.putExtra(EXTRA_RESULT_DEAL_ID, RESULT_DEAL_ID);
        RESULT_DEAL_ID = "0";
        sendBroadcast(i);
    }

    private boolean login(String email_input, String pass) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.login(email_input, pass);
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

                String token = jsonResponse.optString("token", "");
                SharedPreferences preferences = getSharedPreferences("data_token", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token", token);
                editor.commit();
                StaticFunction.TOKEN = token;

                JSONObject data = jsonResponse.getJSONObject("data");

                Long user_id = data.getLong("user_id");
                Long group_id = data.getLong("group_id");
                String email = data.optString("email", "");
                if (email.equals("null")) email = "";
                String role = data.optString("role", "");

                if (role.equalsIgnoreCase("consumer")) {
                    JSONObject objConsumer = data.optJSONObject("consumer");
                    boolean isConsumer = true;
                    Long consumer_id = objConsumer.getLong("consumer_id");
                    String full_name = objConsumer.optString("full_name", "");
                    if (full_name.equals("null")) full_name = "";
                    String phone = objConsumer.optString("phone", "");
                    if (phone.equals("null")) phone = "";
                    String phoneCode = objConsumer.optString("phone_code", "");
                    if (phoneCode.equals("null")) phoneCode = "";
                    String gender = objConsumer.optString("gender", "");
                    if (gender.equals("null")) gender = "";
                    String profile_image = objConsumer.optString("profile_image", "");
                    if (profile_image.equals("null")) profile_image = "";
                    String facebook_id = objConsumer.optString("facebook_id", "");
                    if (facebook_id.equals("null")) facebook_id = "";
                    String dob = objConsumer.optString("dob", "");
                    if (dob.equals("null")) dob = "";
                    int k_following = objConsumer.optInt("k_following");
                    int k_call_dibs = objConsumer.optInt("k_call_dibs");
                    int f_verified_phone = objConsumer.getInt("f_verified_phone");
                    int k_ref_bonus = objConsumer.getInt("k_ref_bonus");
                    int k_ref_user = objConsumer.getInt("k_ref_user");
                    String ref_code = objConsumer.getString("ref_code");
                    if (ref_code.equals("null")) ref_code = "";

                    ObjectUser user = new ObjectUser();
                    user.setUser_id(user_id);
                    user.setEmail(email);
                    user.setIsConsumer(isConsumer);
                    user.setConsumer_id(consumer_id);
                    user.setFull_name(full_name);
                    user.setPhoneCode(phoneCode);
                    user.setPhone(phone);
                    user.setGender(gender);
                    user.setRef_code(ref_code);
                    user.setK_following(k_following);
                    user.setK_call_dibs(k_call_dibs);
                    user.setProfile_image(profile_image);
                    user.setFacebook_id(facebook_id);
                    user.setF_verified_phone(f_verified_phone);
                    user.setK_ref_bonus(k_ref_bonus);
                    user.setK_ref_user(k_ref_user);
                    user.setDob(dob);


                    UserController.clearAllUsers(RealTimeService.this);
                    UserController.insertOrUpdate(RealTimeService.this, user);

                    boolean isFirst = false;
                    try {
                        int f_first = objConsumer.getInt("f_first");
                        if (f_first == 1) {
                            isFirst = true;
                        }
                    } catch (JSONException e) {

                    }

                    if (f_verified_phone == 0) {
                        SharedPreferences preferencesdata_signup = getSharedPreferences("data_signup", MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = preferencesdata_signup.edit();
                        editor1.putLong("consumer_id", consumer_id);
                        editor1.commit();
                    }

                    SharedPreferences preferencesIsFirst = getSharedPreferences("user_login", MODE_PRIVATE);
                    SharedPreferences.Editor editorIsFirst = preferencesIsFirst.edit();
                    editorIsFirst.putBoolean("is_first", isFirst);
                    editorIsFirst.commit();


                    isSuccess = true;
                } else if (role.equalsIgnoreCase("merchant")) {
                    JSONObject objMerchant = data.optJSONObject("merchant");
                    Long merchant_id = objMerchant.getLong("merchant_id");
                    String merchant_name = objMerchant.getString("organization_name");
                    boolean isConsumer = false;
                    ObjectUser user = new ObjectUser();
                    user.setUser_id(user_id);
                    user.setConsumer_id(merchant_id);
                    user.setGroup_id(group_id);
                    user.setEmail(email);
                    user.setIsConsumer(isConsumer);
                    user.setFull_name(merchant_name);
                    user.setK_following(0);
                    user.setK_call_dibs(0);
                    UserController.clearAllUsers(RealTimeService.this);
                    UserController.insertOrUpdate(RealTimeService.this, user);
                    isSuccess = true;
                } else if (role.equalsIgnoreCase("outlet")) {
                    JSONObject objMerchant = data.optJSONObject("merchant");
                    Long merchant_id = objMerchant.getLong("merchant_id");
                    String merchant_name = objMerchant.getString("organization_name");
                    boolean isConsumer = false;
                    ObjectUser user = new ObjectUser();
                    user.setUser_id(user_id);
                    user.setGroup_id(group_id);
                    user.setConsumer_id(merchant_id);
                    user.setEmail(email);
                    user.setIsConsumer(isConsumer);
                    user.setFull_name(merchant_name);
                    user.setK_following(0);
                    user.setK_call_dibs(0);
                    UserController.clearAllUsers(RealTimeService.this);
                    UserController.insertOrUpdate(RealTimeService.this, user);
                    isSuccess = true;
                } else {
                    isSuccess = false;
                }
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

    private boolean loginFacebook(String facebook_id_input, String username, String first_name_input, String last_name_input, String gender_input, String email_input) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.loginFacebook(facebook_id_input, username, first_name_input, last_name_input, gender_input, email_input);
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

                String token = jsonResponse.optString("token", "");
                SharedPreferences preferences = getSharedPreferences("data_token", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token", token);
                editor.commit();
                StaticFunction.TOKEN = token;

                JSONObject data = jsonResponse.getJSONObject("data");

                Long user_id = data.getLong("user_id");
                Long group_id = data.getLong("group_id");
                String email = data.optString("email", "");
                if (email.equals("null")) email = "";
                String role = data.optString("role", "");

                if (role.equalsIgnoreCase("consumer")) {
                    JSONObject objConsumer = data.optJSONObject("consumer");
                    boolean isConsumer = true;
                    Long consumer_id = objConsumer.getLong("consumer_id");
                    String full_name = objConsumer.optString("full_name", "");
                    if (full_name.equals("null")) full_name = "";
                    String phone = objConsumer.optString("phone", "");
                    if (phone.equals("null")) phone = "";
                    String phoneCode = objConsumer.optString("phone_code", "");
                    if (phoneCode.equals("null")) phoneCode = "";
                    String gender = objConsumer.optString("gender", "");
                    if (gender.equals("null")) gender = "";
                    String profile_image = objConsumer.optString("profile_image", "");
                    if (profile_image.equals("null")) profile_image = "";
                    String facebook_id = objConsumer.optString("facebook_id", "");
                    if (facebook_id.equals("null")) facebook_id = "";
                    String dob = objConsumer.optString("dob", "");
                    if (dob.equals("null")) dob = "";
                    int k_following = objConsumer.optInt("k_following");
                    int k_call_dibs = objConsumer.optInt("k_call_dibs");
                    int f_verified_phone = objConsumer.getInt("f_verified_phone");
                    int k_ref_bonus = objConsumer.getInt("k_ref_bonus");
                    int k_ref_user = objConsumer.getInt("k_ref_user");
                    String ref_code = objConsumer.getString("ref_code");
                    if (ref_code.equals("null")) ref_code = "";

                    ObjectUser user = new ObjectUser();
                    user.setUser_id(user_id);
                    user.setEmail(email);
                    user.setIsConsumer(isConsumer);
                    user.setConsumer_id(consumer_id);
                    user.setFull_name(full_name);
                    user.setPhoneCode(phoneCode);
                    user.setPhone(phone);
                    user.setGender(gender);
                    user.setRef_code(ref_code);
                    user.setK_following(k_following);
                    user.setK_call_dibs(k_call_dibs);
                    user.setProfile_image(profile_image);
                    user.setFacebook_id(facebook_id);
                    user.setF_verified_phone(f_verified_phone);
                    user.setK_ref_bonus(k_ref_bonus);
                    user.setK_ref_user(k_ref_user);
                    user.setDob(dob);
                    UserController.clearAllUsers(RealTimeService.this);
                    UserController.insertOrUpdate(RealTimeService.this, user);

                    isSuccess = true;
                } else if (role.equalsIgnoreCase("merchant")) {
                    boolean isConsumer = false;
                    ObjectUser user = new ObjectUser();
                    user.setUser_id(user_id);
                    user.setGroup_id(group_id);
                    user.setEmail(email);
                    user.setIsConsumer(isConsumer);
                    user.setConsumer_id(0l);
                    user.setK_call_dibs(0);
                    user.setK_following(0);
                    UserController.clearAllUsers(RealTimeService.this);
                    UserController.insertOrUpdate(RealTimeService.this, user);
                    isSuccess = true;
                } else {
                    isSuccess = false;
                }
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

    private boolean logout(String user_id) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.logout(user_id);
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
                String message = objStatus.getString("message");
                RESULT_MESSAGE = message;
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

    private boolean updateAccount(String user_id, String fullName, String phoneCode, String phoneNumber, String email, String gender, String dob, String ref_code) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.updateAccount(user_id, fullName, phoneCode, phoneNumber, email, gender, dob, ref_code);
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
                String message = objStatus.getString("message");
                RESULT_MESSAGE = message;
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

    private boolean changePassword(String user_id, String password, String new_password) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.changePassword(user_id, password, new_password);
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
                String message = objStatus.getString("message");
                RESULT_MESSAGE = message;
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

    private boolean forgotPassword(String email) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.forgotPassword(email);
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
                String message = objStatus.getString("message");
                RESULT_MESSAGE = message;
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

    private boolean signUp(String full_name, String phoneCode, String phoneNumber, String email, String pass) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.signup(full_name, phoneCode, phoneNumber, email, pass);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            JSONObject objStatus = jsonResponse.getJSONObject("status");
            String type = objStatus.getString("type");
            boolean success;
            if (type.equalsIgnoreCase("Success")) {

                JSONObject data = jsonResponse.getJSONObject("data");

                JSONObject objConsumer = data.optJSONObject("consumer");
                Long consumer_id = objConsumer.getLong("consumer_id");


                SharedPreferences preferences = getSharedPreferences("data_signup", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong("consumer_id", consumer_id);
                editor.commit();


                success = true;
            } else {
                success = false;
            }

            if (success) {
                String message = objStatus.getString("message");
                RESULT_MESSAGE = message;
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

    private boolean checkEmailExist(String email) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.checkEmailExist(email);
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
                String message = objStatus.getString("message");
                RESULT_MESSAGE = message;
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

    private boolean getDealShortTerm(String latitude_input, String longitude_input, String page, String sort_field, String sort_dir, String keyword, String categories, String typeDeals) {
        String consumer_id = "";
        ObjectUser user = UserController.getCurrentUser(RealTimeService.this);
        if (user != null) {
            consumer_id = user.getConsumer_id() + "";
        }
        boolean isSuccess = false;
        API api = new API();
        String result = api.getDealLongOrShortTerm(latitude_input, longitude_input, page, 0, consumer_id, sort_field, sort_dir, keyword, categories, typeDeals);
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
                    DealController.clearDealByDealType(RealTimeService.this, Const.DEALTYPE.SHORT_TERM);
                }
                JSONArray jarr_data = jsonResponse.getJSONArray("data");

                for (int i = 0; i < jarr_data.length(); i++) {
                    JSONObject data = jarr_data.getJSONObject(i);
                    Long deal_id = data.getLong("deal_id");
                    ObjectDeal objectDeal = parseDataObjectDeal(data, "", "", false, Const.DEALTYPE.SHORT_TERM);

                    JSONObject additional = data.getJSONObject("additional");
                    JSONArray jarr_refer_deal_infos = additional.getJSONArray("refer_deal_infos");
                    for (int j = 0; j < jarr_refer_deal_infos.length(); j++) {
                        JSONObject dataRefer = jarr_refer_deal_infos.getJSONObject(j);
                        parseDataObjectDeal(dataRefer, String.valueOf(objectDeal.getId()), "", false, Const.DEALTYPE.SHORT_TERM);
                    }
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

    private ObjectDeal parseDataObjectDeal(JSONObject data, String dealReferId, int dealType) {
        return parseDataObjectDeal(data, "", "", false, dealType);
    }

    private ObjectDeal parseDataObjectDeal(JSONObject data, String dealReferId, String refMerchantId, boolean isFromFollowing, int dealType) {
        try {
            Long deal_id = data.getLong("deal_id");
            String image = (data.has("image")) ? data.getString("image") : "";
            String image_thumbnail = (data.has("image_thumbnail")) ? data.getString("image_thumbnail") : "";
            String title = data.getString("title");
            String type_data = data.getString("type");
            if (!type_data.equalsIgnoreCase("instore")) {
                return null;
            }
            String start_at = data.getString("start_at");
            String end_at = data.getString("end_at");
            Integer max_claim = (data.has("max_claim")) ? data.getInt("max_claim") : 0; // should have
            Boolean f_claimed = data.getInt("f_claimed") == 1 ? true : false;
            Boolean f_call_dibs = data.getInt("f_call_dibs") == 1 ? true : false;
            Float original_price = Float.parseFloat(data.optString("original_price", "0"));
            Float purchase_now_price = Float.parseFloat(data.optString("purchase_now_price", "0"));
            String duration_type = data.optString("duration_type", "");
            Boolean is_exclusive = data.optInt("is_exclusive", 0) == 1 ? true : false;
            Boolean f_liked = data.optInt("f_liked", 0) == 1 ? true : false;

            Integer k_deals_by_outlet = 0;
            String refer_deal_ids = "";
            if (dealReferId.trim().length() == 0) {
                if (data.has("additional")) {
                    JSONObject additional = data.getJSONObject("additional");
                    k_deals_by_outlet = additional.getInt("k_deals_by_outlet");
                    refer_deal_ids = additional.getString("refer_deal_ids");
                }
            }

            JSONObject obj_merchant = data.getJSONObject("merchant");
            Long merchant_id = obj_merchant.getLong("merchant_id");
            String organization_name = obj_merchant.getString("organization_name");
            String logo_url = obj_merchant.optString("logo_image_url", "");
            ObjectDealMerchant objectDealMerchant = new ObjectDealMerchant();
            objectDealMerchant.setMerchant_id(merchant_id);
            objectDealMerchant.setDeal_id(deal_id);
            objectDealMerchant.setOrganization_name(organization_name);
            DealMerchantController.insert(RealTimeService.this, objectDealMerchant);


            String outlets = "";
            JSONObject obj_outlet = null;
            long outlet_id = -1;
            if (data.has("outlet")) {
                obj_outlet = data.getJSONObject("outlet");

                outlet_id = obj_outlet.getLong("outlet_id");
                String latitude = obj_outlet.optString("latitude");
                String longitude = obj_outlet.optString("longitude");
                String name = obj_outlet.getString("name");
                String phone = obj_outlet.optString("phone", "");
                String address1 = obj_outlet.optString("address1");
                String address2 = obj_outlet.optString("address2");

                String zip_code = "";
                String secret_code = "";
                Outlet outletObj = new Outlet(outlet_id, merchant_id, name, phone, address1, address2, zip_code, latitude, longitude, secret_code);
                OutletController.insert(RealTimeService.this, outletObj);
            } else {
                JSONArray arr_outlet = data.getJSONArray("outlets");
                if (arr_outlet.length() > 0) {
                    obj_outlet = arr_outlet.getJSONObject(0);
                }

                outlets = addOutletDeal(arr_outlet, merchant_id);
            }

            if (outlet_id == -1)
                outlet_id = obj_outlet.getLong("outlet_id");

            ObjectDeal objectDeal = new ObjectDeal();
            objectDeal.setDeal_id(deal_id);
            objectDeal.setOutlets(outlets);
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
            objectDeal.setDeal_type(dealType);
            objectDeal.setF_claimed(f_claimed);
            objectDeal.setF_call_dibs(f_call_dibs);
            objectDeal.setDuration_type(duration_type);
            objectDeal.setOrganization_name(organization_name);
            objectDeal.setIs_exclusive(is_exclusive);
            objectDeal.setF_liked(f_liked);
            objectDeal.setLogo_image_url(logo_url);
            objectDeal.setFromDealFollowing(isFromFollowing);

            if (dealReferId.trim().length() > 0) {
                objectDeal.setDealReferId(Long.parseLong(dealReferId));
            } else {
                objectDeal.setK_deals_by_outlet(k_deals_by_outlet);
                objectDeal.setRefer_deal_ids(refer_deal_ids);
                objectDeal.setDealReferId(-1l);
            }

            if (refMerchantId.trim().length() > 0) {
                objectDeal.setRef_merchant_id(Long.parseLong(refMerchantId));
            } else {
                objectDeal.setRef_merchant_id(-1l);
            }

            if (dealType == Const.DEALTYPE.DEAL_AVAILABLE) {
                String uuid = data.optString("uuid", "");
                objectDeal.setUuid(uuid);
            }

            Long id = DealController.insert(RealTimeService.this, objectDeal);
            objectDeal.setId(id);
            return objectDeal;
        } catch (Exception e) {
            Log.e("eRROROR", e.getMessage());
        }
        return null;
    }


    private boolean getDealFollowing(String page) {

        boolean isSuccess = false;
        API api = new API();
        String result = api.getDealFollowing(page);
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
                    DealController.clearDealFollowing(RealTimeService.this);
                }
                JSONArray jarr_data = jsonResponse.getJSONArray("data");

                for (int i = 0; i < jarr_data.length(); i++) {
                    JSONObject data = jarr_data.getJSONObject(i);
                    Long deal_id = data.getLong("deal_id");
                    parseDataObjectDeal(data, "", "", true, Const.DEALTYPE.FOLLOWING);
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

    private boolean getDealsByDiscovery(String discovery_id, String latitude_input, String longitude_input, String page) {
        String consumer_id = "";
        ObjectUser user = UserController.getCurrentUser(RealTimeService.this);
        if (user != null) {
            consumer_id = user.getConsumer_id() + "";
        }
        boolean isSuccess = false;
        API api = new API();
        String result = api.getDealsByDiscovery(discovery_id, latitude_input, longitude_input, page, consumer_id);
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
                if (Integer.parseInt(page) == 1) {
                    DealController.clearDealDiscoveryByDealType(RealTimeService.this, 3);
                }
                JSONArray jarr_data = jsonResponse.getJSONArray("data");

                for (int i = 0; i < jarr_data.length(); i++) {
                    JSONObject data = jarr_data.getJSONObject(i);
                    Long deal_id = data.getLong("deal_id");
                    String image = data.getString("image");
                    String image_thumbnail = data.getString("image_thumbnail");
                    String title = data.getString("title");
                    String type_data = data.getString("type");
                    String start_at = data.getString("start_at");
                    String end_at = data.getString("end_at");
                    Integer max_claim = data.getInt("max_claim");
                    Boolean f_claimed = data.getInt("f_claimed") == 1 ? true : false;
                    Boolean f_call_dibs = data.getInt("f_call_dibs") == 1 ? true : false;
                    Float original_price = Float.parseFloat(data.optString("original_price", "0"));
                    Float purchase_now_price = Float.parseFloat(data.optString("purchase_now_price", "0"));
                    String duration_type = data.getString("duration_type");
                    Boolean is_exclusive = data.getInt("is_exclusive") == 1 ? true : false;
                    Boolean f_liked = data.getInt("f_liked") == 1 ? true : false;
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
                    //String logo_image_url = obj_merchant.getString("logo_image_url");
                    ObjectDealMerchant objectDealMerchant = new ObjectDealMerchant();
                    objectDealMerchant.setMerchant_id(merchant_id);
                    objectDealMerchant.setDeal_id(deal_id);
                    objectDealMerchant.setOrganization_name(organization_name);
                    DealMerchantController.insert(RealTimeService.this, objectDealMerchant);

                    String zip_code = "";
                    String secret_code = "";
                    Outlet outletObj = new Outlet(outlet_id, merchant_id, name, phone, address1, address2, zip_code, latitude, longitude, secret_code);
                    OutletController.insert(RealTimeService.this, outletObj);

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
                    objectDeal.setDeal_type(3);
                    objectDeal.setF_claimed(f_claimed);
                    objectDeal.setF_call_dibs(f_call_dibs);
                    objectDeal.setDuration_type(duration_type);
                    objectDeal.setOrganization_name(organization_name);
                    objectDeal.setIs_exclusive(is_exclusive);
                    objectDeal.setF_liked(f_liked);
                    objectDeal.setLogo_image_url("");
                    objectDeal.setFromDealFollowing(false);
                    objectDeal.setDealReferId(-1l);
                    objectDeal.setRef_merchant_id(-1l);
                    DealController.insert(RealTimeService.this, objectDeal);
                }

                JSONObject paginator = jsonResponse.getJSONObject("paginator");
                String lastpage = paginator.getString("last_page");
                RESULT_LAST_PAGE = lastpage;

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

    private boolean getDealsByMerchant(String merchant_id_input, String latitude_input, String longitude_input, String page) {
        String consumer_id = "";
        ObjectUser user = UserController.getCurrentUser(RealTimeService.this);
        if (user != null) {
            consumer_id = user.getConsumer_id() + "";
        }
        boolean isSuccess = false;
        API api = new API();
        String result = api.getDealsByMerchant(merchant_id_input, latitude_input, longitude_input, page, consumer_id);
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
                if (Integer.parseInt(page) == 1) {
                    DealController.clearDealByDealType(RealTimeService.this, 4);
                }
                JSONArray jarr_data = jsonResponse.getJSONArray("data");

                for (int i = 0; i < jarr_data.length(); i++) {
                    JSONObject data = jarr_data.getJSONObject(i);
                    Long deal_id = data.getLong("deal_id");
                    String image = data.getString("image");
                    String image_thumbnail = data.getString("image_thumbnail");
                    String title = data.getString("title");
                    String type_data = data.getString("type");
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
                    DealMerchantController.insert(RealTimeService.this, objectDealMerchant);

                    String zip_code = "";
                    String secret_code = "";
                    Outlet outletObj = new Outlet(outlet_id, merchant_id, name, phone, address1, address2, zip_code, latitude, longitude, secret_code);
                    OutletController.insert(RealTimeService.this, outletObj);

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
                    objectDeal.setDeal_type(4);
                    objectDeal.setF_claimed(f_claimed);
                    objectDeal.setF_call_dibs(f_call_dibs);
                    objectDeal.setDuration_type(duration_type);
                    objectDeal.setOrganization_name(organization_name);
                    objectDeal.setIs_exclusive(is_exclusive);
                    DealController.insert(RealTimeService.this, objectDeal);
                }

                JSONObject paginator = jsonResponse.getJSONObject("paginator");
                String lastpage = paginator.getString("last_page");
                RESULT_LAST_PAGE = lastpage;

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

    private boolean getDealsFromListId(String ids, String outlet_id_input) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getDealsFromListId(ids, outlet_id_input);
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
                DealController.clearDealByDealType(RealTimeService.this, 5);
                JSONArray jarr_data = jsonResponse.getJSONArray("data");

                for (int i = 0; i < jarr_data.length(); i++) {
                    JSONObject data = jarr_data.getJSONObject(i);
                    Long deal_id = data.getLong("deal_id");
                    String image = data.getString("image");
                    String image_thumbnail = data.getString("image_thumbnail");
                    String title = data.getString("title");
                    String type_data = data.getString("type");
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
                    DealMerchantController.insert(RealTimeService.this, objectDealMerchant);

                    String zip_code = "";
                    String secret_code = "";
                    Outlet outletObj = new Outlet(outlet_id, merchant_id, name, phone, address1, address2, zip_code, latitude, longitude, secret_code);
                    OutletController.insert(RealTimeService.this, outletObj);

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
                    objectDeal.setDeal_type(5);
                    objectDeal.setF_claimed(f_claimed);
                    objectDeal.setF_call_dibs(f_call_dibs);
                    objectDeal.setDuration_type(duration_type);
                    objectDeal.setOrganization_name(organization_name);
                    objectDeal.setIs_exclusive(is_exclusive);
                    objectDeal.setFromDealFollowing(false);

                    DealController.insert(RealTimeService.this, objectDeal);
                }

                JSONObject paginator = jsonResponse.getJSONObject("paginator");
                String lastpage = paginator.getString("last_page");
                RESULT_LAST_PAGE = lastpage;

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

    private boolean getDealDetailByDealId(String dealId, String outlet_id_input) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getDealDetailByDealId(dealId, "1", outlet_id_input);
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
                Long deal_id = data.getLong("deal_id");
                String description = data.getString("description");
                String terms = data.getString("terms");
                String image = data.getString("image");
                String image_thumbnail = data.getString("image_thumbnail");
                String title = data.getString("title");
                String type_data = data.getString("type");
                String start_at = data.getString("start_at");
                String end_at = data.getString("end_at");
                Integer max_claim = data.getInt("max_claim");
                Integer k_rest_claim = data.getInt("k_rest_claim");
                Boolean f_claimed = data.getInt("f_claimed") == 1 ? true : false;
                Boolean f_called = data.getInt("f_call_dibs") == 1 ? true : false;
                Float original_price = Float.parseFloat(data.optString("original_price", "0"));
                Float purchase_now_price = Float.parseFloat(data.optString("purchase_now_price", "0"));
                String duration_type = data.getString("duration_type");
                Integer k_likes = data.getInt("k_likes");
                Integer k_unlikes = data.getInt("k_unlikes");
                Boolean f_yay = data.getInt("f_yay") == 1 ? true : false;
                Boolean f_nay = data.getInt("f_nay") == 1 ? true : false;
                String claim_validity = data.getString("claim_validity");
                Boolean is_exclusive = data.getInt("is_exclusive") == 1 ? true : false;
                Boolean f_liked = data.getInt("f_liked") == 1 ? true : false;
                Integer group_id = data.getInt("group_id");
                String group_name = data.getString("group_name");

                JSONObject obj_merchant = data.getJSONObject("merchant");
                Long merchant_id = obj_merchant.getLong("merchant_id");
                String organization_name = obj_merchant.getString("organization_name");
                String logo_url = obj_merchant.getString("logo_image_url");

                JSONArray arr_outlet = data.getJSONArray("outlets");
                Long outlet_id = 0l;
                if (arr_outlet.length() > 0) {
                    outlet_id = arr_outlet.getJSONObject(0).getLong("outlet_id");
                }

                String outlets = addOutletDeal(arr_outlet, merchant_id);

                ObjectDeal objectDealTmp = DealController.getDealByDealId(RealTimeService.this, deal_id, Long.parseLong(outlet_id_input));


                if (objectDealTmp != null) {
                    List<ObjectDeal> objectDeals = DealController.getListDealById(RealTimeService.this, deal_id, outlet_id_input);
                    for (ObjectDeal objectDeal : objectDeals) {
                        objectDeal.setDescription(description);
                        objectDeal.setTerms(terms);
                        objectDeal.setK_rest_claim(k_rest_claim);
                        objectDeal.setOutlets(outlets);
                        objectDeal.setK_likes(k_likes);
                        objectDeal.setK_unlikes(k_unlikes);
                        objectDeal.setF_yay(f_yay);
                        objectDeal.setF_nay(f_nay);
                        objectDeal.setF_claimed(f_claimed);
                        objectDeal.setF_call_dibs(f_called);
                        objectDeal.setClaim_validity(claim_validity);
                        objectDeal.setF_liked(f_liked);
                        objectDeal.setLogo_image_url(logo_url);
                        objectDeal.setFromDealFollowing(false);
                        objectDeal.setGroup_id(group_id);
                        objectDeal.setGroup_name(group_name);
                        DealController.update(RealTimeService.this, objectDeal);
                    }
                } else {
                    ObjectDeal deal = new ObjectDeal();
                    deal.setDeal_id(deal_id);
                    deal.setImage(image);
                    deal.setImage_thumbnail(image_thumbnail);
                    deal.setTitle(title);
                    deal.setType(type_data);
                    deal.setStart_at(start_at);
                    deal.setEnd_at(end_at);
                    deal.setMax_claim(max_claim);
                    deal.setK_rest_claim(k_rest_claim);
                    deal.setOriginal_price(original_price);
                    deal.setPurchase_now_price(purchase_now_price);
                    deal.setOutlet_id(Long.parseLong(outlet_id_input));
                    deal.setMerchant_id(merchant_id);
                    deal.setDeal_type(2);
                    deal.setF_claimed(f_claimed);
                    deal.setF_call_dibs(f_called);
                    deal.setDescription(description);
                    deal.setTerms(terms);
                    deal.setDuration_type(duration_type);
                    deal.setOutlets(outlets);
                    deal.setK_likes(k_likes);
                    deal.setK_unlikes(k_unlikes);
                    deal.setF_yay(f_yay);
                    deal.setF_nay(f_nay);
                    deal.setClaim_validity(claim_validity);
                    deal.setOrganization_name(organization_name);
                    deal.setIs_exclusive(is_exclusive);
                    deal.setF_liked(f_liked);
                    deal.setLogo_image_url(logo_url);
                    deal.setFromDealFollowing(false);
                    deal.setGroup_id(group_id);
                    deal.setGroup_name(group_name);
                    DealController.insert(RealTimeService.this, deal);
                }

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

    private String addOutletDeal(JSONArray arr_outlet, long merchant_id) {
        String outlets = "";
        try {
            for (int i = 0; i < arr_outlet.length(); i++) {
                JSONObject obj_outlet = arr_outlet.getJSONObject(i);
                Long outlet_id = obj_outlet.getLong("outlet_id");
                String latitude = obj_outlet.getString("latitude");
                String longitude = obj_outlet.getString("longitude");
//                Float distance = Float.parseFloat(obj_outlet.optString("distance", "0"));
                String name = obj_outlet.getString("name");
                String phone = obj_outlet.getString("phone");
                String address1 = obj_outlet.getString("address1");
                String address2 = obj_outlet.getString("address2");
                String secret_code = obj_outlet.getString("secret_code");

                String zip_code = "";
                Outlet outletObj = new Outlet(outlet_id, merchant_id, name, phone, address1, address2, zip_code, latitude, longitude, secret_code);
                OutletController.insert(RealTimeService.this, outletObj);

                outlets += outlet_id + "/";
            }
        } catch (JSONException e) {

        }
//        if (outlets.length() > 1) {
//            outlets = outlets.substring(0, outlets.length() - 1);
//        }
        return outlets;
    }

    private boolean getDealDetailByDealIdToNotifyFollow(String dealId, String outletId) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getDealDetailByDealId(dealId, "0", outletId);
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
                Long deal_id = data.getLong("deal_id");
                String description = data.getString("description");
                String terms = data.getString("terms");
                String image = data.getString("image");
                String image_thumbnail = data.getString("image_thumbnail");
                String title = data.getString("title");
                String type_data = data.getString("type");
                String start_at = data.getString("start_at");
                String end_at = data.getString("end_at");
                Integer max_claim = data.getInt("max_claim");
                Integer k_rest_claim = data.getInt("k_rest_claim");
                Boolean f_claimed = data.getInt("f_claimed") == 1 ? true : false;
                Boolean f_called = data.getInt("f_call_dibs") == 1 ? true : false;
                Float original_price = Float.parseFloat(data.optString("original_price", "0"));
                Float purchase_now_price = Float.parseFloat(data.optString("purchase_now_price", "0"));
                String duration_type = data.getString("duration_type");
                Integer k_likes = data.getInt("k_likes");
                Integer k_unlikes = data.getInt("k_unlikes");
                Boolean f_yay = data.getInt("f_yay") == 1 ? true : false;
                Boolean f_nay = data.getInt("f_nay") == 1 ? true : false;
                String claim_validity = data.getString("claim_validity");
                Boolean is_exclusive = data.getInt("is_exclusive") == 1 ? true : false;
                Boolean f_liked = data.getInt("f_liked") == 1 ? true : false;
                Integer group_id = data.getInt("group_id");
                String group_name = data.getString("group_name");

                JSONObject obj_merchant = data.getJSONObject("merchant");
                Long merchant_id = obj_merchant.getLong("merchant_id");
                merchantId_follow = merchant_id;
                String organization_name = obj_merchant.getString("organization_name");
                String logo_url = obj_merchant.getString("logo_image_url");

                JSONArray arr_outlet = data.getJSONArray("outlets");
                Long outlet_id = 0l;
                if (arr_outlet.length() > 0) {
                    outlet_id = arr_outlet.getJSONObject(0).getLong("outlet_id");
                }

                String outlets = addOutletDeal(arr_outlet, merchant_id);

                ObjectDeal objectDealTmp = DealController.getDealByDealId(RealTimeService.this, deal_id, Long.parseLong(outletId));


                if (objectDealTmp != null) {
                    List<ObjectDeal> objectDeals = DealController.getListDealById(RealTimeService.this, deal_id, outletId);
                    for (ObjectDeal objectDeal : objectDeals) {
                        objectDeal.setDescription(description);
                        objectDeal.setTerms(terms);
                        objectDeal.setK_rest_claim(k_rest_claim);
                        objectDeal.setOutlets(outlets);
                        objectDeal.setK_likes(k_likes);
                        objectDeal.setK_unlikes(k_unlikes);
                        objectDeal.setF_yay(f_yay);
                        objectDeal.setF_nay(f_nay);
                        objectDeal.setClaim_validity(claim_validity);
                        objectDeal.setF_liked(f_liked);
                        objectDeal.setF_claimed(f_claimed);
                        objectDeal.setF_call_dibs(f_called);
                        objectDeal.setLogo_image_url(logo_url);
                        objectDeal.setFromDealFollowing(false);
                        objectDeal.setGroup_id(group_id);
                        objectDeal.setGroup_name(group_name);
                        DealController.update(RealTimeService.this, objectDeal);
                    }
                } else {
                    ObjectDeal deal = new ObjectDeal();
                    deal.setDeal_id(deal_id);
                    deal.setImage(image);
                    deal.setImage_thumbnail(image_thumbnail);
                    deal.setTitle(title);
                    deal.setType(type_data);
                    deal.setStart_at(start_at);
                    deal.setEnd_at(end_at);
                    deal.setMax_claim(max_claim);
                    deal.setK_rest_claim(k_rest_claim);
                    deal.setOriginal_price(original_price);
                    deal.setPurchase_now_price(purchase_now_price);
                    deal.setOutlet_id(Long.parseLong(outletId));
                    deal.setMerchant_id(merchant_id);
                    deal.setDeal_type(2);
                    deal.setF_claimed(f_claimed);
                    deal.setF_call_dibs(f_called);
                    deal.setDescription(description);
                    deal.setTerms(terms);
                    deal.setDuration_type(duration_type);
                    deal.setOutlets(outlets);
                    deal.setK_likes(k_likes);
                    deal.setK_unlikes(k_unlikes);
                    deal.setF_yay(f_yay);
                    deal.setF_nay(f_nay);
                    deal.setClaim_validity(claim_validity);
                    deal.setOrganization_name(organization_name);
                    deal.setIs_exclusive(is_exclusive);
                    deal.setF_liked(f_liked);
                    deal.setLogo_image_url(logo_url);
                    deal.setFromDealFollowing(false);
                    deal.setGroup_id(group_id);
                    deal.setGroup_name(group_name);
                    DealController.insert(RealTimeService.this, deal);

                }
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

    private boolean getDealDetailByDealIdToNotifyNewDeal(String dealId, String outletId) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getDealDetailByDealId(dealId, "0", outletId);
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
                Long deal_id = data.getLong("deal_id");
                String description = data.getString("description");
                String terms = data.getString("terms");
                String image = data.getString("image");
                String image_thumbnail = data.getString("image_thumbnail");
                String title = data.getString("title");
                String type_data = data.getString("type");
                String start_at = data.getString("start_at");
                String end_at = data.getString("end_at");
                Integer max_claim = data.getInt("max_claim");
                Integer k_rest_claim = data.getInt("k_rest_claim");
                Boolean f_claimed = data.getInt("f_claimed") == 1 ? true : false;
                Boolean f_called = data.getInt("f_call_dibs") == 1 ? true : false;
                Float original_price = Float.parseFloat(data.optString("original_price", "0"));
                Float purchase_now_price = Float.parseFloat(data.optString("purchase_now_price", "0"));
                String duration_type = data.getString("duration_type");
                Integer k_likes = data.getInt("k_likes");
                Integer k_unlikes = data.getInt("k_unlikes");
                Boolean f_yay = data.getInt("f_yay") == 1 ? true : false;
                Boolean f_nay = data.getInt("f_nay") == 1 ? true : false;
                String claim_validity = data.getString("claim_validity");
                Boolean is_exclusive = data.getInt("is_exclusive") == 1 ? true : false;
                Boolean f_liked = data.getInt("f_liked") == 1 ? true : false;
                Integer group_id = data.getInt("group_id");
                String group_name = data.getString("group_name");

                JSONObject obj_merchant = data.getJSONObject("merchant");
                Long merchant_id = obj_merchant.getLong("merchant_id");
                String organization_name = obj_merchant.getString("organization_name");
                String logo_url = obj_merchant.getString("logo_image_url");

                JSONArray arr_outlet = data.getJSONArray("outlets");
                Long outlet_id = 0l;
                double latitude = 0;
                double longitude = 0;
                if (arr_outlet.length() > 0) {
                    outlet_id = arr_outlet.getJSONObject(0).getLong("outlet_id");
                    latitude = arr_outlet.getJSONObject(0).optDouble("latitude");
                    longitude = arr_outlet.getJSONObject(0).optDouble("longitude");
                }

                String outlets = addOutletDeal(arr_outlet, merchant_id);
                ObjectDeal objectDealTmp = DealController.getDealByDealId(RealTimeService.this, deal_id, Long.parseLong(outletId));


                if (objectDealTmp != null) {
                    List<ObjectDeal> objectDeals = DealController.getListDealById(RealTimeService.this, deal_id, outletId);
                    for (ObjectDeal objectDeal : objectDeals) {
                        objectDeal.setDescription(description);
                        objectDeal.setTerms(terms);
                        objectDeal.setK_rest_claim(k_rest_claim);
                        objectDeal.setOutlets(outlets);
                        objectDeal.setK_likes(k_likes);
                        objectDeal.setK_unlikes(k_unlikes);
                        objectDeal.setF_yay(f_yay);
                        objectDeal.setF_nay(f_nay);
                        objectDeal.setClaim_validity(claim_validity);
                        objectDeal.setF_liked(f_liked);
                        objectDeal.setF_claimed(f_claimed);
                        objectDeal.setF_call_dibs(f_called);
                        objectDeal.setLogo_image_url(logo_url);
                        objectDeal.setFromDealFollowing(false);
                        objectDeal.setGroup_id(group_id);
                        objectDeal.setGroup_name(group_name);
                        DealController.update(RealTimeService.this, objectDeal);
                    }
                } else {
                    ObjectDeal deal = new ObjectDeal();
                    deal.setDeal_id(deal_id);
                    deal.setImage(image);
                    deal.setImage_thumbnail(image_thumbnail);
                    deal.setTitle(title);
                    deal.setType(type_data);
                    deal.setStart_at(start_at);
                    deal.setEnd_at(end_at);
                    deal.setMax_claim(max_claim);
                    deal.setK_rest_claim(k_rest_claim);
                    deal.setOriginal_price(original_price);
                    deal.setPurchase_now_price(purchase_now_price);
                    deal.setOutlet_id(Long.parseLong(outletId));
                    deal.setMerchant_id(merchant_id);
                    deal.setDeal_type(2);
                    deal.setF_claimed(f_claimed);
                    deal.setF_call_dibs(f_called);
                    deal.setDescription(description);
                    deal.setTerms(terms);
                    deal.setDuration_type(duration_type);
                    deal.setOutlets(outlets);
                    deal.setK_likes(k_likes);
                    deal.setK_unlikes(k_unlikes);
                    deal.setF_yay(f_yay);
                    deal.setF_nay(f_nay);
                    deal.setClaim_validity(claim_validity);
                    deal.setOrganization_name(organization_name);
                    deal.setIs_exclusive(is_exclusive);
                    deal.setF_liked(f_liked);
                    deal.setLogo_image_url(logo_url);
                    deal.setFromDealFollowing(false);
                    deal.setGroup_id(group_id);
                    deal.setGroup_name(group_name);
                    DealController.insert(RealTimeService.this, deal);
                }


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
                objectDeal.setLatitude(latitude);
                objectDeal.setLongitude(longitude);
                objectDeal.setIsNotified(0);

//                PermanentController.deleteByDealId(this, objectDeal);
                PermanentController.insertOrUpdate(this, objectDeal);

                isSuccess = true;
                RESULT_DEAL_ID = deal_id + "";
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

    private boolean getDealDetailByDealIdToPromptClaimedSuccess(String dealId) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getDealDetailByDealId(dealId, "0", "");
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
                Long deal_id = data.getLong("deal_id");
                String description = data.getString("description");
                String terms = data.getString("terms");
                String image = data.getString("image");
                String image_thumbnail = data.getString("image_thumbnail");
                String title = data.getString("title");
                String type_data = data.getString("type");
                String start_at = data.getString("start_at");
                String end_at = data.getString("end_at");
                Integer max_claim = data.getInt("max_claim");
                Integer k_rest_claim = data.getInt("k_rest_claim");
                Boolean f_claimed = data.getInt("f_claimed") == 1 ? true : false;
                Boolean f_called = data.getInt("f_call_dibs") == 1 ? true : false;
                Float original_price = Float.parseFloat(data.optString("original_price", "0"));
                Float purchase_now_price = Float.parseFloat(data.optString("purchase_now_price", "0"));
                String duration_type = data.getString("duration_type");
                Integer k_likes = data.getInt("k_likes");
                Integer k_unlikes = data.getInt("k_unlikes");
                Boolean f_yay = data.getInt("f_yay") == 1 ? true : false;
                Boolean f_nay = data.getInt("f_nay") == 1 ? true : false;
                String claim_validity = data.getString("claim_validity");
                Boolean is_exclusive = data.getInt("is_exclusive") == 1 ? true : false;
                Boolean f_liked = data.getInt("f_liked") == 1 ? true : false;
                Integer group_id = data.getInt("group_id");
                String group_name = data.getString("group_name");

                JSONObject obj_merchant = data.getJSONObject("merchant");
                Long merchant_id = obj_merchant.getLong("merchant_id");
                String organization_name = obj_merchant.getString("organization_name");
                String logo_merchant = obj_merchant.getString("logo_image_url");

                JSONArray arr_outlet = data.getJSONArray("outlets");
                Long outlet_id = 0l;
                if (arr_outlet.length() > 0) {
                    outlet_id = arr_outlet.getJSONObject(0).getLong("outlet_id");
                }

                String outlets = addOutletDeal(arr_outlet, merchant_id);

                ObjectDeal objectDeal = DealController.getDealByDealId(RealTimeService.this, deal_id);

                if (objectDeal != null) {
                    objectDeal.setDescription(description);
                    objectDeal.setTerms(terms);
                    objectDeal.setK_rest_claim(k_rest_claim);
                    objectDeal.setOutlets(outlets);
                    objectDeal.setK_likes(k_likes);
                    objectDeal.setK_unlikes(k_unlikes);
                    objectDeal.setF_yay(f_yay);
                    objectDeal.setF_nay(f_nay);
                    objectDeal.setClaim_validity(claim_validity);
                    objectDeal.setF_liked(f_liked);
                    objectDeal.setLogo_image_url(logo_merchant);
                    objectDeal.setFromDealFollowing(false);
                    objectDeal.setGroup_id(group_id);
                    objectDeal.setGroup_name(group_name);
                    DealController.update(RealTimeService.this, objectDeal);
                } else {
                    ObjectDeal deal = new ObjectDeal();
                    deal.setDeal_id(deal_id);
                    deal.setImage(image);
                    deal.setImage_thumbnail(image_thumbnail);
                    deal.setTitle(title);
                    deal.setType(type_data);
                    deal.setStart_at(start_at);
                    deal.setEnd_at(end_at);
                    deal.setMax_claim(max_claim);
                    deal.setK_rest_claim(k_rest_claim);
                    deal.setOriginal_price(original_price);
                    deal.setPurchase_now_price(purchase_now_price);
                    deal.setOutlet_id(outlet_id);
                    deal.setMerchant_id(merchant_id);
                    deal.setDeal_type(2);
                    deal.setF_claimed(f_claimed);
                    deal.setF_call_dibs(f_called);
                    deal.setDescription(description);
                    deal.setTerms(terms);
                    deal.setDuration_type(duration_type);
                    deal.setOutlets(outlets);
                    deal.setK_likes(k_likes);
                    deal.setK_unlikes(k_unlikes);
                    deal.setF_yay(f_yay);
                    deal.setF_nay(f_nay);
                    deal.setClaim_validity(claim_validity);
                    deal.setOrganization_name(organization_name);
                    deal.setIs_exclusive(is_exclusive);
                    deal.setF_liked(f_liked);
                    deal.setLogo_image_url(logo_merchant);
                    deal.setRef_merchant_id(objectDeal.getRef_merchant_id());
                    deal.setDealReferId(objectDeal.getDealReferId());
                    deal.setFromDealFollowing(false);
                    deal.setGroup_id(group_id);
                    deal.setGroup_name(group_name);
                    DealController.insert(RealTimeService.this, deal);
                }

                RESULT_DEAL_TITLE = title;
                RESULT_MERCHANT_NAME = organization_name;

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

    private boolean getMerchantById(String merchantId, String dealId) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getMerchantById(merchantId, dealId);
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
                OutletController.clearByMerchantId(RealTimeService.this, merchantId);
                JSONObject data = jsonResponse.getJSONObject("data");
                Long merchant_id = data.getLong("merchant_id");
                String first_name = data.getString("first_name");
                String last_name = data.getString("last_name");
                String organization_name = data.getString("organization_name");
                Integer industry_type_id = data.getInt("industry_type_id");
                String phone = data.getString("phone");
                if (phone.equalsIgnoreCase("null")) phone = "";
                String tags = data.getString("tags");
                String logo_image = data.getString("logo_image_url");
                if (logo_image.equalsIgnoreCase("null")) logo_image = "";
                String cover_image = data.getString("cover_image_url");
                if (cover_image.equalsIgnoreCase("null")) cover_image = "";
                Integer k_follows = data.getInt("k_follows");
                Integer k_likes = data.getInt("k_likes");
                Integer k_unlikes = data.getInt("k_unlikes");
                Integer k_outlets = data.getInt("k_outlets");
                Boolean f_yay = data.getInt("f_yay") == 1 ? true : false;
                Boolean f_nay = data.getInt("f_nay") == 1 ? true : false;
                Boolean f_follow = data.getInt("f_follow") == 1 ? true : false;
                String profile_images = data.getString("profile_images");
                int live_deals = data.getInt("k_live_deals");
                int past_deals = data.getInt("k_past_deals");
                String website_url = data.getString("website_url");
                if (website_url.equalsIgnoreCase("null")) website_url = "";
                String facebook_url = data.getString("facebook_url");
                if (facebook_url.equalsIgnoreCase("null")) facebook_url = "";
                String twitter_url = data.getString("twitter_url");
                if (twitter_url.equalsIgnoreCase("null")) twitter_url = "";
                String instagram_url = data.getString("instagram_url");
                if (instagram_url.equalsIgnoreCase("null")) instagram_url = "";
                String description = data.getString("description");
                if (description.equalsIgnoreCase("null")) description = "";

                Merchant merchant = new Merchant(merchant_id, first_name, last_name, organization_name, industry_type_id, phone, tags, logo_image, k_follows, k_likes, k_unlikes, k_outlets, f_yay, f_nay, f_follow, profile_images, live_deals, past_deals, website_url, facebook_url, twitter_url, instagram_url, description, cover_image);
                MerchantController.insert(RealTimeService.this, merchant);

                JSONArray jarr_outlets = data.getJSONArray("outlets");
                for (int i = 0; i < jarr_outlets.length(); i++) {
                    JSONObject outlet = jarr_outlets.getJSONObject(i);
                    Long outlet_id = outlet.getLong("outlet_id");
                    String name = outlet.getString("name");
                    String phone_outlet = outlet.getString("phone");
                    String address1 = outlet.getString("address1");
                    String address2 = outlet.getString("address2");
                    String zip_code = outlet.getString("zip_code");
                    String latitude = outlet.getString("latitude");
                    String longitude = outlet.getString("longitude");

                    String secret_code = "";
                    Outlet outletObj = new Outlet(outlet_id, merchant_id, name, phone_outlet, address1, address2, zip_code, latitude, longitude, secret_code);
                    OutletController.insert(RealTimeService.this, outletObj);
                }


                JSONObject jsonDealsObject = data.getJSONObject("deals");
                JSONArray jarr_deals = jsonDealsObject.getJSONArray("data");
                DealController.clearDealByMerchantRef(RealTimeService.this, merchant_id);
                for (int i = 0; i < jarr_deals.length(); i++) {
                    parseDataObjectDeal(jarr_deals.getJSONObject(i), "", String.valueOf(merchant_id), false, Const.DEALTYPE.MERCHANT);
                }

                ReviewController.clearReviewByMerchantId(RealTimeService.this, merchantId);
                JSONObject jsonCommentObject = data.getJSONObject("comments");
                JSONArray jarr_comments = jsonCommentObject.getJSONArray("data");
                for (int i = 0; i < jarr_comments.length(); i++) {
                    JSONObject jsonComment = jarr_comments.getJSONObject(i);
                    long id = jsonComment.getLong("id");
                    long consumer_id = jsonComment.getLong("consumer_id");
                    long deal_id = jsonComment.getLong("deal_id");
                    long review_merchant_id = jsonComment.getLong("merchant_id");
                    String created_at = jsonComment.getString("created_at");
                    String updated_at = jsonComment.getString("updated_at");
                    boolean isYay = (jsonComment.getInt("is_yay") == 1) ? true : false;
                    String review = jsonComment.getString("review");

                    JSONObject jsonConsumer = jsonComment.getJSONObject("comsumer");
                    String fullName = jsonConsumer.getString("full_name");
                    String profile_image = jsonConsumer.getString("profile_image");

                    Review reviewObject = new Review();
                    reviewObject.setReview_id(id);
                    reviewObject.setConsumer_id(consumer_id);
                    reviewObject.setDeal_id(deal_id);
                    reviewObject.setReview(review);
                    reviewObject.setIs_yay(isYay);
                    reviewObject.setMerchant_id(review_merchant_id);
                    reviewObject.setCreated_at(created_at);
                    reviewObject.setUpdated_at(updated_at);
                    reviewObject.setFullname(fullName);
                    reviewObject.setProfile_image(profile_image);
                    ReviewController.insert(RealTimeService.this, reviewObject);

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

    private boolean getAllMerchant(String name, String page, String sort_dir) {
        String industry_id = "";

        boolean isSuccess = false;
        API api = new API();
        String result = api.getAllMerchant(name, page, industry_id, sort_dir);
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
                    MerchantListController.clearAllByType(RealTimeService.this, Const.MERCHANT_LIST_TYPE.ALL_MERCHANT);
                }
                JSONArray jsonArray = jsonResponse.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.getJSONObject(i);
                    Long merchant_id = data.getLong("merchant_id");
                    String organization_name = data.getString("organization_name");
                    String website_url = data.getString("website_url");
                    if (website_url.equalsIgnoreCase("null")) website_url = "";
                    String logo_image = data.getString("logo_image_url");
                    if (logo_image.equalsIgnoreCase("null")) logo_image = "";
                    String industry_name = data.getString("industry_name");
                    if (industry_name.equalsIgnoreCase("null")) industry_name = "";
                    String cover_image = data.getString("cover_image_url");
                    if (cover_image.equalsIgnoreCase("null")) cover_image = "";
                    Integer k_live_deals = data.getInt("k_live_deals");

                    boolean f_follow = (data.getInt("f_follow") == 0) ? false : true;

                    MerchantList merchantList = new MerchantList();
                    merchantList.setMerchant_id(merchant_id);
                    merchantList.setOrganization_name(organization_name);
                    merchantList.setWebsite_url(website_url);
                    merchantList.setLogo_image(logo_image);
                    merchantList.setIndustry_name(industry_name);
                    merchantList.setCover_image(cover_image);
                    merchantList.setK_live_deals(k_live_deals);
                    merchantList.setF_follow(f_follow);
                    merchantList.setMerchant_type(Const.MERCHANT_LIST_TYPE.ALL_MERCHANT);

                    MerchantListController.insert(RealTimeService.this, merchantList);
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

    private boolean getMerchantsFollowing(String consumerId, int page) {
        String industry_id = "";

        boolean isSuccess = false;
        API api = new API();
        String result = api.getMerchantsFollowing(consumerId, page);
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

                if (page == 1)
                    MerchantListController.clearAllByType(RealTimeService.this, Const.MERCHANT_LIST_TYPE.FOLLOWING);

                JSONArray jsonArray = jsonResponse.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.getJSONObject(i);
                    Long merchant_id = data.getLong("merchant_id");
                    String organization_name = data.getString("organization_name");
                    String website_url = data.optString("website_url", "");
                    if (website_url.equalsIgnoreCase("null")) website_url = "";
                    String logo_image = data.optString("logo_image_url", "");
                    if (logo_image.equalsIgnoreCase("null")) logo_image = "";
                    String industry_name = data.optString("industry_name", "");
                    if (industry_name.equalsIgnoreCase("null")) industry_name = "";
                    String cover_image = data.optString("cover_image_url", "");
                    if (cover_image.equalsIgnoreCase("null")) cover_image = "";
                    Integer k_live_deals = data.getInt("k_live_deals");

                    boolean f_follow = true;

                    MerchantList merchantList = new MerchantList();
                    merchantList.setMerchant_id(merchant_id);
                    merchantList.setOrganization_name(organization_name);
                    merchantList.setWebsite_url(website_url);
                    merchantList.setLogo_image(logo_image);
                    merchantList.setIndustry_name(industry_name);
                    merchantList.setCover_image(cover_image);
                    merchantList.setK_live_deals(k_live_deals);
                    merchantList.setF_follow(f_follow);
                    merchantList.setMerchant_type(Const.MERCHANT_LIST_TYPE.FOLLOWING);
                    MerchantListController.insert(RealTimeService.this, merchantList);
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


    private boolean getDiscoveryTitle() {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getDiscoveryTitle();
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
                DiscoveryController.deleteAll(RealTimeService.this);
                JSONArray jarr_data = jsonResponse.getJSONArray("data");

                for (int i = 0; i < jarr_data.length(); i++) {
                    JSONObject data = jarr_data.getJSONObject(i);
                    Long id = data.getLong("id");
                    String image = data.getString("image");
                    String name = data.getString("name");
                    int position = data.getInt("position");
                    int k_live_deals = data.getInt("k_live_deals");

                    Discovery discovery = new Discovery(id, name, image, position, k_live_deals);

                    DiscoveryController.insert(RealTimeService.this, discovery);
                }

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

    private boolean callDibs(String deal_id, String consumer_id, String trans_id, String outlet_id) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.callDibs(deal_id, consumer_id, trans_id, outlet_id);
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
                RESULT_MESSAGE = data.toString();

                ObjectUser user = UserController.getCurrentUser(this);
                int call_dibs = user.getK_call_dibs();
                user.setK_call_dibs(call_dibs + 1);
                UserController.insertOrUpdate(this, user);

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

    private boolean callClaimed(String uuid, String deal_id, String consumer_id, String merchant_id) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.callClaimed(uuid, deal_id, consumer_id, merchant_id);
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
//                JSONObject data = jsonResponse.getJSONObject("data");
                String message = objStatus.getString("message");

                List<ObjectDeal> deals = DealController.getListDealByDealId(this, Long.parseLong(deal_id));
                for (ObjectDeal deal : deals) {
                    deal.setF_claimed(true);
                    DealController.update(this, deal);
                }
                StaticFunction.sendBroad(this, DealDetailActivity.RECEIVER_NOTIFY_LIST);
                StaticFunction.sendBroad(this, DealDetailActivity.RECEIVER_CLAIM_SUCCESS);

                RESULT_MESSAGE = message;
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

    private boolean getDealsAvailable(String consumer_id) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getDealsAvailable(consumer_id);
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
                DealAvailableController.clearAll(RealTimeService.this);
                JSONArray jarr_data = jsonResponse.getJSONArray("data");

                for (int i = 0; i < jarr_data.length(); i++) {
                    JSONObject data = jarr_data.getJSONObject(i);
                    Long deal_id = data.getLong("deal_id");
                    String title = data.getString("title");
                    String start_at = data.getString("start_at");
                    String end_at = data.getString("end_at");
                    Float purchase_now_price = Float.parseFloat(data.optString("purchase_now_price", "0"));
                    String uuid = data.getString("uuid");
                    String type_deal = data.getString("type");
                    String validity = data.getString("validity");
                    String outlet_id = data.getString("outlet_id");
                    if (outlet_id.equalsIgnoreCase("null")) outlet_id = "";

                    JSONObject merchant = data.getJSONObject("merchant");
                    Long merchant_id = merchant.getLong("merchant_id");
                    String organization_name = merchant.getString("organization_name");
                    String outlet_name = "";

                    String secret_code = "";
                    JSONArray outlets = data.getJSONArray("outlets");
                    for (int j = 0; j < outlets.length(); j++) {
                        secret_code += outlets.getJSONObject(j).getString("secret_code") + "/";
                        if (outlet_id.equals(outlets.getJSONObject(j).getString("outlet_id"))) {
                            outlet_name = outlets.getJSONObject(j).getString("name");
                        } else {
                            outlet_name = outlets.getJSONObject(0).getString("name");
                        }
                    }

                    DealAvailable dealAvailable = new DealAvailable(deal_id, title, start_at, end_at, purchase_now_price, merchant_id, organization_name, uuid, type_deal, secret_code, validity, outlet_name);
                    DealAvailableController.insert(RealTimeService.this, dealAvailable);
                }

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

    private boolean getDealsAvailableV2(String consumer_id) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getDealsAvailable(consumer_id);
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
            DealController.clearDealByDealType(RealTimeService.this, Const.DEALTYPE.DEAL_AVAILABLE);

            if (success) {

                JSONArray jarr_deals = jsonResponse.getJSONArray("data");

                for (int i = 0; i < jarr_deals.length(); i++) {
                    parseDataObjectDeal(jarr_deals.getJSONObject(i), "", Const.DEALTYPE.DEAL_AVAILABLE);
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

    private boolean getDealsClaimed(String consumer_id) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getDealsClaimed(consumer_id, "1", "", "");
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
            DealController.clearDealByDealType(RealTimeService.this, Const.DEALTYPE.DEAL_HISTORY);

            if (success) {

                JSONArray jarr_deals = jsonResponse.getJSONArray("data");

                for (int i = 0; i < jarr_deals.length(); i++) {
                    parseDataObjectDeal(jarr_deals.getJSONObject(i), "", Const.DEALTYPE.DEAL_HISTORY);
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


    private boolean getDealsAvailableByDealId(String consumer_id, String deal_id_input, long outlet_id) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getDealsAvailableByDealId(consumer_id, deal_id_input);
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
                DealController.clearDealByDealIdAndDealType(RealTimeService.this, Long.parseLong(deal_id_input), Const.DEALTYPE.DEAL_AVAILABLE + "", outlet_id);

                JSONObject data = jsonResponse.getJSONObject("data");
                parseDataObjectDeal(data, "", Const.DEALTYPE.DEAL_AVAILABLE);

                isSuccess = true;
            } else {
                isSuccess = false;
            }
        } catch (JSONException e) {
            isSuccess = false;
        }

        return isSuccess;
    }

    private boolean searchDeals(String latitude_input, String longitude_input, String page, String keyword, String deal_type, String discovery_type, String min, String max) {
        String consumer_id = "";
        ObjectUser user = UserController.getCurrentUser(RealTimeService.this);
        if (user != null) {
            consumer_id = user.getConsumer_id() + "";
        }
        boolean isSuccess = false;
        API api = new API();
        String result = api.searchDeals(latitude_input, longitude_input, page, consumer_id, keyword, deal_type, discovery_type, min, max);
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
                if (Integer.parseInt(page) == 1) {
                    DealController.clearDealByDealType(RealTimeService.this, 2);
                }
                JSONArray jarr_data = jsonResponse.getJSONArray("data");

                for (int i = 0; i < jarr_data.length(); i++) {
                    JSONObject data = jarr_data.getJSONObject(i);
                    Long deal_id = data.getLong("deal_id");
                    String image = data.getString("image");
                    String image_thumbnail = data.getString("image_thumbnail");
                    String title = data.getString("title");
                    String type_data = data.getString("type");
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
                    DealMerchantController.insert(RealTimeService.this, objectDealMerchant);

                    String zip_code = "";
                    String secret_code = "";
                    Outlet outletObj = new Outlet(outlet_id, merchant_id, name, phone, address1, address2, zip_code, latitude, longitude, secret_code);
                    OutletController.insert(RealTimeService.this, outletObj);

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
                    objectDeal.setDeal_type(2);
                    objectDeal.setF_claimed(f_claimed);
                    objectDeal.setF_call_dibs(f_call_dibs);
                    objectDeal.setDuration_type(duration_type);
                    objectDeal.setOrganization_name(organization_name);
                    objectDeal.setIs_exclusive(is_exclusive);
                    DealController.insert(RealTimeService.this, objectDeal);
                }

                JSONObject paginator = jsonResponse.getJSONObject("paginator");
                String lastpage = paginator.getString("last_page");
                RESULT_LAST_PAGE = lastpage;

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

    private boolean getFollowingList(String consumerID) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getFollowingList(consumerID);
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
                FollowingController.deleteAll(RealTimeService.this);

                JSONArray jarr_data = jsonResponse.getJSONArray("data");

                for (int i = 0; i < jarr_data.length(); i++) {
                    JSONObject data = jarr_data.getJSONObject(i);
                    Long merchantId = data.getLong("merchant_id");
                    String merchantName = data.getString("organization_name");
                    int newDeals = data.getInt("k_live_deals");
                    String cover_image = data.getString("cover_image_url");
                    String logo_image_url = data.getString("logo_image_url");

                    Following following = new Following();
                    following.setMerchant_id(merchantId);
                    following.setMerchant_name(merchantName);
                    following.setNum_of_new_deals(newDeals);
                    following.setCover_image_url(cover_image);
                    following.setLogo_image_url(logo_image_url);

                    FollowingController.insert(RealTimeService.this, following);
                }


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


    private boolean stopFollowingMerchant(String consumerID, String merchantID) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.stopFollowMerchant(merchantID, consumerID);
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
                String message = objStatus.getString("message");
                RESULT_MESSAGE = message;
                isSuccess = true;

                DealController.clearDealByMerchantIdAndDealType(RealTimeService.this, merchantID, Const.DEALTYPE.FOLLOWING);
                MerchantListController.clearByMerchantId(RealTimeService.this, Long.parseLong(merchantID), Const.MERCHANT_LIST_TYPE.FOLLOWING);

                //Update number followng
                ObjectUser user = UserController.getCurrentUser(this);
                int following = user.getK_following();
                if (following > 0) {
                    user.setK_following(following - 1);
                    UserController.insertOrUpdate(this, user);
                }
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

    private boolean followMerchant(String consumerID, String merchantID) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.followMerchant(merchantID, consumerID);
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
                String message = objStatus.getString("message");
                RESULT_MESSAGE = message;
                isSuccess = true;

                ObjectUser user = UserController.getCurrentUser(this);
                int following = user.getK_following();
                user.setK_following(following + 1);
                UserController.insertOrUpdate(this, user);

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

    private boolean likeDeal(String consumerID, String merchantID, String dealID, String review) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.likeDeal(merchantID, consumerID, dealID, review);
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
                String message = objStatus.getString("message");
                RESULT_MESSAGE = message;
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

    private boolean unLikeDeal(String consumerID, String merchantID, String dealID, String review) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.unLikeDeal(merchantID, consumerID, dealID, review);
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
                String message = objStatus.getString("message");
                RESULT_MESSAGE = message;
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

    private boolean addCommentDeal(String consumerID, String dealID, String text) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.addCommentDeal(dealID, consumerID, text);
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
                String message = objStatus.getString("message");
                RESULT_MESSAGE = message;
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

    private boolean fetchAllDiscovery() {
        boolean isSuccess = false;
        API api = new API();
        String result = api.fetchAllDiscovery();
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
                TypeSearchController.clearAll(RealTimeService.this);
                JSONArray jarr_data = jsonResponse.getJSONArray("data");

                for (int i = 0; i < jarr_data.length(); i++) {
                    JSONObject data = jarr_data.getJSONObject(i);
                    Long id = data.getLong("id");
                    String name = data.getString("name");


                    TypeSearch typeSearch = new TypeSearch(id, name);
                    TypeSearchController.insert(RealTimeService.this, typeSearch);
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

    private boolean getKeyClientToken() {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getKeyClientToken();
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
                String key = data.getString("clientToken");

                RESULT_KEY_CLIENT_TOKEN = key;

                isSuccess = true;
            } else {
                isSuccess = false;
            }
        } catch (JSONException e) {
            isSuccess = false;
        }

        return isSuccess;
    }

    private boolean createAPayment(String nonce, String amount, String consumer_id, String merchant_id) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.createAPayment(nonce, amount, consumer_id, merchant_id);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            JSONObject objStatus = jsonResponse.getJSONObject("status");
            String type = objStatus.getString("type");
            boolean success;
            if (type.equalsIgnoreCase("Success")) {
                success = true;
                RESULT_MESSAGE = objStatus.getString("message");
            } else {
                success = false;
                RESULT_MESSAGE = objStatus.getString("message");
            }

            if (success) {
                String trans_id = jsonResponse.getString("transaction_id");

                RESULT_PAYMENT_TRANS_ID = trans_id;

                isSuccess = true;
            } else {
                isSuccess = false;
            }
        } catch (JSONException e) {
            isSuccess = false;
        }

        return isSuccess;
    }

    private boolean getDiscountsAvaiable(String consumer_id) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getDiscountsAvaiable(consumer_id);
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
                DiscountController.clearAll(this);
                JSONObject data = jsonResponse.getJSONObject("data");

//                int total = data.getInt("total");

                JSONArray arr_discounts = data.getJSONArray("discounts");
                for (int i = 0; i < arr_discounts.length(); i++) {
                    JSONObject discount = arr_discounts.getJSONObject(i);
                    long id = discount.getLong("id");
                    String code = discount.getString("code");
                    float amount = Float.parseFloat(discount.getString("amount"));
                    String note = discount.getString("note");
                    String created_at = discount.getString("created_at");

                    Discount discountObj = new Discount(id, code, amount, note, created_at);
                    DiscountController.insert(this, discountObj);
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


    private boolean getCountries() {
        boolean isSuccess;
        API api = new API();
        String result = api.getCountries();
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

                JSONArray arr_phoneCodes = jsonResponse.getJSONArray("data");
                for (int i = 0; i < arr_phoneCodes.length(); i++) {
                    JSONObject phoneCode = arr_phoneCodes.getJSONObject(i);
                    int id = phoneCode.getInt("id");
                    String code = phoneCode.getString("code");
                    String name = phoneCode.getString("name");
                    String dial_code = phoneCode.getString("dial_code");
                    String f_disabled = phoneCode.getString("f_disabled");

                    PhoneCode phoneCodeObj = new PhoneCode(id, code, name, dial_code, f_disabled);
                    PhoneCodeController.insert(this, phoneCodeObj);

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

    private boolean verifyPhone(String code, String consumer_id) {
        boolean isSuccess;
        API api = new API();
        String result = api.verifyPhone(code, consumer_id);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            JSONObject objStatus = jsonResponse.getJSONObject("status");
            String type = objStatus.getString("type");
            boolean success;
            if (type.equalsIgnoreCase("Success")) {
                success = true;
                RESULT_MESSAGE = objStatus.getString("message");
            } else {
                success = false;
                RESULT_MESSAGE = objStatus.getString("message");
            }

            if (success) {
                isSuccess = true;
            } else {
                isSuccess = false;
            }
        } catch (JSONException e) {
            isSuccess = false;
        }

        return isSuccess;
    }

    private boolean requestNewCode(String consumer_id) {
        boolean isSuccess;
        API api = new API();
        String result = api.requestNewCode(consumer_id);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            JSONObject objStatus = jsonResponse.getJSONObject("status");
            String type = objStatus.getString("type");
            boolean success;
            if (type.equalsIgnoreCase("Success")) {
                success = true;
                RESULT_MESSAGE = objStatus.getString("message");
            } else {
                success = false;
                RESULT_MESSAGE = objStatus.getString("message");
            }

            if (success) {
                isSuccess = true;
            } else {
                isSuccess = false;
            }
        } catch (JSONException e) {
            isSuccess = false;
        }

        return isSuccess;
    }

    private boolean getFilterDeal() {
        boolean isSuccess;
        API api = new API();
        String result = api.getFilterDeals();
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

                CategoryController.deleteAll(RealTimeService.this);
                JSONArray arr_categories = data.getJSONArray("categories");
                for (int i = 0; i < arr_categories.length(); i++) {
                    JSONObject categoryObject = arr_categories.getJSONObject(i);
                    int id = categoryObject.getInt("id");
                    String name = categoryObject.getString("name");
                    int position = categoryObject.getInt("position");
                    Category category = new Category(id, name, position, false);
                    CategoryController.insert(RealTimeService.this, category);
                }

                TypeDealController.deleteAll(RealTimeService.this);
                JSONArray arr_typeDeal = data.getJSONArray("groups");
                for (int i = 0; i < arr_typeDeal.length(); i++) {
                    JSONObject typeDealObject = arr_typeDeal.getJSONObject(i);
                    int id = typeDealObject.getInt("id");
                    String name = typeDealObject.getString("text");
                    int order = typeDealObject.getInt("order");
                    int f_deleted = typeDealObject.getInt("f_deleted");
                    TypeDeal typeDeal = new TypeDeal(id, name, order, false, f_deleted);
                    TypeDealController.insert(RealTimeService.this, typeDeal);
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


    private boolean getMerchantReviews(long merchantId, int page) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getMerchantReviews(merchantId, String.valueOf(page));
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
           /* JSONObject objStatus = jsonResponse.getJSONObject("status");
            String type = objStatus.getString("type");
            boolean success;
            if (type.equalsIgnoreCase("Success")) {
                success = true;
            } else {
                success = false;
            } */

            boolean success = true;
            if (success) {
                JSONArray jarr_comments = jsonResponse.getJSONArray("data");
                for (int i = 0; i < jarr_comments.length(); i++) {
                    JSONObject jsonComment = jarr_comments.getJSONObject(i);
                    long id = jsonComment.getLong("id");
                    long consumer_id = jsonComment.getLong("consumer_id");
                    long deal_id = jsonComment.getLong("deal_id");
                    long review_merchant_id = jsonComment.getLong("merchant_id");
                    String created_at = jsonComment.getString("created_at");
                    String updated_at = jsonComment.getString("updated_at");
                    boolean isYay = (jsonComment.getInt("is_yay") == 1) ? true : false;
                    String review = jsonComment.getString("review");

                    JSONObject jsonConsumer = jsonComment.getJSONObject("comsumer");
                    String fullName = jsonConsumer.getString("full_name");
                    String profile_image = jsonConsumer.getString("profile_image");

                    if (page == 1) {
                        ReviewController.clearReviewByMerchantId(RealTimeService.this, merchantId + "");
                    }
                    Review reviewObject = new Review();
                    reviewObject.setReview_id(id);
                    reviewObject.setConsumer_id(consumer_id);
                    reviewObject.setDeal_id(deal_id);
                    reviewObject.setReview(review);
                    reviewObject.setIs_yay(isYay);
                    reviewObject.setMerchant_id(review_merchant_id);
                    reviewObject.setCreated_at(created_at);
                    reviewObject.setUpdated_at(updated_at);
                    reviewObject.setFullname(fullName);
                    reviewObject.setProfile_image(profile_image);
                    ReviewController.insert(RealTimeService.this, reviewObject);

                }

                JSONObject paginator = jsonResponse.getJSONObject("paginate");
                String lastpage = paginator.getString("last_page");
                RESULT_LAST_PAGE = lastpage;

                isSuccess = true;
            } else {
                String message = "";//objStatus.getString("message");
                RESULT_MESSAGE = message;
                isSuccess = false;
            }
        } catch (JSONException e) {
            isSuccess = false;
        }

        return isSuccess;
    }

    private boolean reportDeal(String dealId, String merchantID, String outletId, String text) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.report(dealId, merchantID, outletId, text);
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
                String message = objStatus.getString("message");
                RESULT_MESSAGE = message;
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

}

// short term - 0
// long term - 1
// search - 2
// discovery - 3
// merchant - 4
// outlet - 5
