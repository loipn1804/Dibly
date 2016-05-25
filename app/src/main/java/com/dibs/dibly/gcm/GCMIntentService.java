package com.dibs.dibly.gcm;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dibs.dibly.activity.ClaimSuccessActivity;
import com.dibs.dibly.activity.DealDetailActivity;
import com.dibs.dibly.consts.Const;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.PermanentController;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.List;

import greendao.ObjectDeal;

public class GCMIntentService extends IntentService {
    private static final String TAG = GCMIntentService.class.getSimpleName();

//    Handler han;

//    public static final String INSERT = "insert";
//    public static final String UPDATE = "update";
//    public static final String DELETE = "delete";

    @Override
    public void onCreate() {
        super.onCreate();
//        han = new Handler();
    }

    public GCMIntentService() {

        super(Const.GCM_INTENT_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(TAG, "new push");

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging googleCloudMessaging = GoogleCloudMessaging.getInstance(this);

        String messageType = googleCloudMessaging.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                processNotification(Const.GCM_SEND_ERROR, extras);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                processNotification(Const.GCM_DELETED_MESSAGE, extras);
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // Post notification of received message.
                processNotification(Const.GCM_RECEIVED, extras);
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void processNotification(String type, final Bundle extras) {
        String notification_type = extras.getString("notification_type", "new");

        ActivityManager am = (ActivityManager) GCMIntentService.this.getSystemService(ACTIVITY_SERVICE);
        // The first in the list of RunningTasks is always the foreground task.
        ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);


        if (notification_type.equalsIgnoreCase("claimed")) {
            String message = extras.getString("message");
            String deal_id = extras.getString("deal_id");
            String merchant_id = extras.getString("merchant_id");
            List<ObjectDeal> deals = DealController.getListDealByDealId(this, Long.parseLong(deal_id));
            /* for (ObjectDeal deal : deals) {
                deal.setF_claimed(true);
               // DealController.update(this, deal);
            } */
            StaticFunction.sendBroad(GCMIntentService.this, DealDetailActivity.RECEIVER_NOTIFY_LIST);
            StaticFunction.sendBroad(GCMIntentService.this, DealDetailActivity.RECEIVER_CLAIM_SUCCESS);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(this, ClaimedReceiver.class);
            intent.putExtra("message", message);
            intent.putExtra("deal_id", deal_id);
            intent.putExtra("merchant_id", merchant_id);

            int dealId = Integer.parseInt(deal_id);

            PendingIntent intentExecuted = PendingIntent.getBroadcast(this, dealId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            String deviceMan = android.os.Build.MANUFACTURER;
            if (deviceMan.equalsIgnoreCase("Xiaomi")) {
                alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + StaticFunction.TIME_DELAY_NOTIFY * 1000, 0, intentExecuted);
            } else {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + StaticFunction.TIME_DELAY_NOTIFY * 1000, 0, intentExecuted);
            }
        } else if (notification_type.equalsIgnoreCase("follow")) {
            String message = extras.getString("message");
            String deal_id = extras.getString("deal_id");
            String outlets = extras.getString("outlets", "");
            Log.e("outletsajhdgas","SA  " + outlets );
            if (message != null) {
                Intent intentGetDealDetail = new Intent(GCMIntentService.this, RealTimeService.class);
                intentGetDealDetail.setAction(RealTimeService.ACTION_GET_DEAL_DETAIL_TO_NOTIFY_FOWLOW);
                intentGetDealDetail.putExtra(RealTimeService.EXTRA_DEAL_ID, deal_id);
                intentGetDealDetail.putExtra(RealTimeService.EXTRA_MESSAGE_NOTIFY, message);
               // startService(intentGetDealDetail);
            }
        } else if (notification_type.equalsIgnoreCase("new") || notification_type.equalsIgnoreCase("update")) {

            String deal_id = extras.getString("deal_id");
            String outlets = extras.getString("outlets", "");
            Log.e("outletsajhdgas","SA  " + outlets );

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(this, NewDealReceiver.class);
            intent.putExtra("deal_id", deal_id);

            try {
                int dealId = Integer.parseInt(deal_id);

                PendingIntent intentExecuted = PendingIntent.getBroadcast(this, dealId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                String deviceMan = android.os.Build.MANUFACTURER;
                if (deviceMan.equalsIgnoreCase("Xiaomi")) {
                    alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + StaticFunction.TIME_DELAY_NEW_DEAL * 1000, 0, intentExecuted);
                } else {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + StaticFunction.TIME_DELAY_NEW_DEAL * 1000, 0, intentExecuted);
                }
            } catch (Exception e) {

            }
        } else if (notification_type.equalsIgnoreCase("delete")) {
            String message = extras.getString("message");
            String deal_id = extras.getString("deal_id");
            if (message != null) {
                PermanentController.deleteByDealId(this, Long.parseLong(deal_id));
                DealController.clearByDealId(GCMIntentService.this, Long.parseLong(deal_id));
                Intent intentNoti = new Intent(GCMIntentService.this, RealTimeService.class);
                intentNoti.setAction(RealTimeService.ACTION_DELETE_DEAL);
               // startService(intentNoti);
            }
        }

    }

}
