package com.dibs.dibly.gcm;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dibs.dibly.activity.ClaimSuccessActivity;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.staticfunction.StaticFunction;

/**
 * Created by USER on 10/22/2015.
 */
public class ClaimedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String message = extras.getString("message");
        String deal_id = extras.getString("deal_id");
        String merchant_id = extras.getString("merchant_id");
        if (UserController.isLogin(context)) {
            if (UserController.getCurrentUser(context).getIsConsumer()) {
                if (message != null) {
                    ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
                    // The first in the list of RunningTasks is always the foreground task.
                    ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);

                    final String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();
                    if (foregroundTaskPackageName.equalsIgnoreCase("com.em.dibly")) {
                        Intent notificationIntent = new Intent(context, ClaimSuccessActivity.class);
                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        notificationIntent.putExtra("message", message);
                        notificationIntent.putExtra("deal_id", deal_id);
                        notificationIntent.putExtra("merchant_id", merchant_id);
                        context.startActivity(notificationIntent);
                    } else {
                        StaticFunction.sendNotificationClaimed(message, deal_id, merchant_id, Integer.parseInt(deal_id), context);
                    }
                    cancelAlarm(Integer.parseInt(deal_id), context);
                }
            }
        }
    }

    private void cancelAlarm(int alarmCode_groupid, Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ClaimedReceiver.class);

        PendingIntent intentExecuted = PendingIntent.getBroadcast(context, alarmCode_groupid, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(intentExecuted);
    }
}
