package com.dibs.dibly.api;

/**
 * Created by USER on 5/13/2015.
 */
public class APIConst {
    public static final String ACTION_LOGIN = "ACTION_LOGIN";
    public static final String ACTION_LOAD_ACTIVE_VOUCHER = "ACTION_LOAD_ACTIVE_VOUCHER";
    public static final String ACTION_LOAD_USED_VOUCHER = "ACTION_LOAD_USED_VOUCHER";
    public static final String ACTION_GEN_VOUCHER = "ACTION_GEN_VOUCHER";
    public static final String ACTION_APPLY_VOUCHER = "ACTION_APPLY_VOUCHER";


    public static final String RECEIVER_FINISH_LOGIN = "RECEIVER_FINISH_LOGIN";
    public static final String RECEIVER_FINISH_LOAD_ACTIVE_VOUCHER = "RECEIVER_FINISH_LOAD_ACTIVE_VOUCHER";
    public static final String RECEIVER_FINISH_LOAD_USED_VOUCHER = "RECEIVER_FINISH_LOAD_USED_VOUCHER";
    public static final String RECEIVER_FINISH_GEN_VOUCHER = "RECEIVER_FINISH_GEN_VOUCHER";
    public static final String RECEIVER_FINISH_APPLY_VOUCHER = "RECEIVER_FINISH_APPLY_VOUCHER";

    public static final String EXTRA_RESULT = "EXTRA_RESULT";
    public static final String EXTRA_RESULT_ID = "EXTRA_RESULT_ID";

    // result message
    public static final String RESULT_OK = "RESULT_OK";
    public static final String RESULT_FAIL = "RESULT_FAIL";
    public static final String RESULT_NO_INTERNET = "RESULT_NO_INTERNET";
    public static final String RESULT_JUST_LOAD_IN_A_SECOND = "RESULT_JUST_LOAD_IN_A_SECOND";

    public static final String EXTRA_USERNAME = "EXTRA_USERNAME";
    public static final String EXTRA_PASSWORD = "EXTRA_PASSWORD";
    public static final String EXTRA_AMOUNT = "EXTRA_AMOUNT";
    public static final String EXTRA_CUSTOMER_ID = "EXTRA_CUSTOMER_ID";
    public static final String EXTRA_VC_ID = "EXTRA_VC_ID";
    public static final String EXTRA_VC_CODE = "EXTRA_VC_CODE";


    public static boolean isLoadingActiveVoucher = false;
    public static boolean isLoadingUsedVoucher = false;
}
