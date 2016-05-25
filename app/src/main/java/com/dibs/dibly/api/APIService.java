package com.dibs.dibly.api;

import android.app.IntentService;
import android.content.Intent;


/**
 * Created by USER on 3/24/2015.
 */
public class APIService extends IntentService {

    public APIService() {
        super(APIService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        String action = intent.getAction();
//
//        // if command load -> load data
//        if (action.equals(APIConst.ACTION_LOGIN))
//        {
//            Log.e("onHandleIntent", "ACTION_LOGIN");
//
//            String username = intent.getStringExtra(APIConst.EXTRA_USERNAME);
//            String password = intent.getStringExtra(APIConst.EXTRA_PASSWORD);
//
//            if (StaticFunction.isNetworkAvailable(APIService.this))
//            {
//
//                boolean isSuccess = APIWrapper.login(APIService.this, username, password);
//                if (isSuccess)
//                {
//                    sendBroadCastResult(APIConst.RECEIVER_FINISH_LOGIN, APIConst.RESULT_OK, "-1");
//
//                }
//                else
//                {
//                    sendBroadCastResult(APIConst.RECEIVER_FINISH_LOGIN, APIConst.RESULT_FAIL, "-1");
//                }
//
//
//            }
//            else
//            {
//                sendBroadCastResult(APIConst.RECEIVER_FINISH_LOGIN, APIConst.RESULT_NO_INTERNET, "-1");
//            }
//
//        }
//        // if command save -> upload data to server
//        else if (action.equals(APIConst.ACTION_LOAD_ACTIVE_VOUCHER))
//        {
//            Log.e("onHandleIntent", "ACTION_LOAD_ACTIVE_VOUCHER");
//
//            if (StaticFunction.isNetworkAvailable(APIService.this))
//            {
//
//
//                if (APIConst.isLoadingActiveVoucher == false)
//                {
//
//
//                    APIConst.isLoadingActiveVoucher = true;
//
//
//                    String cusID = intent.getStringExtra(APIConst.EXTRA_CUSTOMER_ID);
//                    boolean isSuccess = APIWrapper.getAllVouchers(APIService.this, cusID, false);
//
//                    if (isSuccess)
//                    {
//                        sendBroadCastResult(APIConst.RECEIVER_FINISH_LOAD_ACTIVE_VOUCHER, APIConst.RESULT_OK, "-1");
//
//                    }
//                    else
//                    {
//                        sendBroadCastResult(APIConst.RECEIVER_FINISH_LOAD_ACTIVE_VOUCHER, APIConst.RESULT_FAIL, "-1");
//                    }
//
//
//                }
//                else
//                {
//                    sendBroadCastResult(APIConst.RECEIVER_FINISH_LOAD_ACTIVE_VOUCHER, APIConst.RESULT_JUST_LOAD_IN_A_SECOND, "-1");
//                }
//
//
//            }
//            else
//            {
//                sendBroadCastResult(APIConst.RECEIVER_FINISH_LOAD_ACTIVE_VOUCHER, APIConst.RESULT_NO_INTERNET, "-1");
//            }
//        }
//        else if (action.equals(APIConst.ACTION_LOAD_USED_VOUCHER))
//        {
//            Log.e("onHandleIntent", "ACTION_LOAD_ACTIVE_VOUCHER");
//
//            if (StaticFunction.isNetworkAvailable(APIService.this))
//            {
//                if (APIConst.isLoadingUsedVoucher == false)
//                {
//
//
//                    APIConst.isLoadingUsedVoucher = true;
//
//
//                    String cusID = intent.getStringExtra(APIConst.EXTRA_CUSTOMER_ID);
//                    boolean isSuccess = APIWrapper.getAllVouchers(APIService.this, cusID, true);
//
//                    if (isSuccess)
//                    {
//                        sendBroadCastResult(APIConst.RECEIVER_FINISH_LOAD_USED_VOUCHER, APIConst.RESULT_OK, "-1");
//
//                    }
//                    else
//                    {
//                        sendBroadCastResult(APIConst.RECEIVER_FINISH_LOAD_USED_VOUCHER, APIConst.RESULT_FAIL, "-1");
//                    }
//
//
//                }
//                else
//                {
//                    sendBroadCastResult(APIConst.RECEIVER_FINISH_LOAD_USED_VOUCHER, APIConst.RESULT_JUST_LOAD_IN_A_SECOND, "-1");
//                }
//
//
//            }
//            else
//            {
//                sendBroadCastResult(APIConst.RECEIVER_FINISH_LOAD_USED_VOUCHER, APIConst.RESULT_NO_INTERNET, "-1");
//            }
//
//        }
//        else if (action.equals(APIConst.ACTION_GEN_VOUCHER))
//        {
//            Log.e("onHandleIntent", "ACTION_GEN_VOUCHER");
//
//            String strAmount = intent.getStringExtra(APIConst.EXTRA_AMOUNT);
//            String cusID = intent.getStringExtra(APIConst.EXTRA_CUSTOMER_ID);
//
//
//            if (StaticFunction.isNetworkAvailable(APIService.this))
//            {
//
//                boolean isSuccess = APIWrapper.generateVoucher(APIService.this, cusID, Double.parseDouble(strAmount));
//                if (isSuccess)
//                {
//
//                    /*Long time = System.currentTimeMillis();
//
//                    String result = intent.getStringExtra(APIConst.EXTRA_RESULT);
//                    Voucher voucher = new Voucher();
//                    voucher.setVoucherCode(time.toString());
//                    voucher.setVoucherAmount(Double.parseDouble(strAmount));
//                    voucher.setIsApplied(false);
//
//                    Calendar c = Calendar.getInstance();
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
//                    String strDate = sdf.format(c.getTime());
//                    voucher.setVoucherGeneratedTime(strDate);
//
//                    GreedDaoController.insertVoucher(APIService.this, voucher);*/
//
//                    sendBroadCastResult(APIConst.RECEIVER_FINISH_GEN_VOUCHER, APIConst.RESULT_OK, "-1");
//                }
//                else
//                {
//                    sendBroadCastResult(APIConst.RECEIVER_FINISH_GEN_VOUCHER, APIConst.RESULT_FAIL, "-1");
//                }
//            }
//            else
//            {
//                sendBroadCastResult(APIConst.RECEIVER_FINISH_GEN_VOUCHER, APIConst.RESULT_NO_INTERNET, "-1");
//            }
//        }
//        else if (action.equals(APIConst.ACTION_APPLY_VOUCHER))
//        {
//            Log.e("onHandleIntent", "ACTION_APPLY_VOUCHER");
//
//            String voucherCode = intent.getStringExtra(APIConst.EXTRA_VC_CODE);
//            String cusID = intent.getStringExtra(APIConst.EXTRA_CUSTOMER_ID);
//
//            if (StaticFunction.isNetworkAvailable(APIService.this))
//            {
//
//
//                boolean isSuccess = APIWrapper.applyVoucher(APIService.this, cusID, voucherCode);
//
//                if (isSuccess)
//                {
//                    sendBroadCastResult(APIConst.RECEIVER_FINISH_APPLY_VOUCHER, APIConst.RESULT_OK, voucherCode);
//                }
//                else
//                {
//                    sendBroadCastResult(APIConst.RECEIVER_FINISH_APPLY_VOUCHER, APIConst.RESULT_FAIL, voucherCode);
//                }
//            }
//            else
//            {
//                sendBroadCastResult(APIConst.RECEIVER_FINISH_APPLY_VOUCHER, APIConst.RESULT_NO_INTERNET, voucherCode);
//            }
//        }
    }


    private void sendBroadCastResult(String receiver, String result, String id) {
        Intent intent = new Intent();
        intent.setAction(receiver);
        intent.putExtra(APIConst.EXTRA_RESULT, result);
        intent.putExtra(APIConst.EXTRA_RESULT_ID, id);
        sendBroadcast(intent);
    }


}
