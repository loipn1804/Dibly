package com.dibs.dibly.consts;

/**
 * Created by USER on 4/18/2015.
 */
public class Const {
    public static final long MY_LOCATION_ID = 1;

    public static final int DISTANCE_NOTIFY_IN_METTER = 500;
    public static final int MIN_DISTANCE_IN_METTER = 50;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 30000;


    public static final String ACTION_LOAD_ALL_PLOC = "ACTION_LOAD_ALL_PLOC";
    public static final String ACTION_REPORT_PLOC = "ACTION_REPORT_PLOC";
    public static final String ACTION_SEND_GCM_REG_ID_TO_SERVER = "ACTION_SEND_GCM_REG_ID_TO_SERVER";

    public static final String RECEIVER_LOAD_ALL_PLOC_FINISH = "RECEIVER_LOAD_ALL_PLOC_FINISH";
    public static final String RECEIVER_REPORT_PLOC_FINISH = "RECEIVER_REPORT_PLOC_FINISH";
    public static final String RECEIVER_SEND_GCM_REG_ID_TO_SERVER_FINISH = "RECEIVER_SEND_GCM_REG_ID_TO_SERVER_FINISH";

    public static final String EXTRA_RESULT = "EXTRA_RESULT";
    public static final String EXTRA_RESULT_OK = "EXTRA_RESULT_OK";
    public static final String EXTRA_RESULT_FAIL = "EXTRA_RESULT_FAIL";
    public static final String EXTRA_RESULT_NO_INTERNET = "EXTRA_RESULT_NO_INTERNET";

    // In GCM, the Sender ID is a project ID that you acquire from the API
    public static final String PROJECT_NUMBER = "2481062754";

    public static final String GMAP_KEY = "AIzaSyBWoSIPn5dv0UO7cZv9wQU1YjFp9IcQsEc";

    public static final String EXTRA_MESSAGE = "message";

    public static final String GCM_NOTIFICATION = "GCM Notification";

    public static final String GCM_DELETED_MESSAGE = "Deleted messages on server: ";

    public static final String GCM_INTENT_SERVICE = "GcmIntentService";

    public static final String GCM_SEND_ERROR = "Send error: ";

    public static final String GCM_RECEIVED = "Received: ";
    // end GCM setup

    public interface BUNDLE_EXTRAS {
        String FROM_SCREEN = "FROM_SCREEN";
        String CATEGORY_ID = "CATEGORY_ID";
        String TYPE_DEAL_ID = "TYPE_DEAL_ID";
        String TEXT_CATEGORY = "CATEGORY";
        String TEXT_TYPE_DEAL = "TYPE_DEAL";
        String KEYWORD = "KEYWORD";
        String MERCHANT_ID = "MERCHANT_ID";
    }

    public interface SCREENS {
        String SIGNIN_SCREEN = "SIGNIN_SCREEN";
        String SIGNUP_SCREEN = "SIGNUP_SCREEN";

    }

    public interface SOCIAL {
        int FACEBOOK = 0;
        int TWITTER = 1;
        int INSTAGRAM = 2;
        int WEBSITE = 3;

    }
    // short term - 0
// long term - 1
// search - 2
// discovery - 3
// merchant - 4
// outlet - 5
    public interface DEALTYPE {
        int SHORT_TERM = 0;
        int DISCOVERY = 3;
        int MERCHANT = 4;
        int FOLLOWING = 6;
        int DEAL_AVAILABLE = 7;
        int DEAL_HISTORY = 8;
    }

    public interface MERCHANT_LIST_TYPE {
        int ALL_MERCHANT = 0;
        int FOLLOWING = 1;
    }
}

