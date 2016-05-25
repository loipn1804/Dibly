package com.dibs.dibly.gcm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dibs.dibly.service.RealTimeService;

/**
 * Created by USER on 11/13/2015.
 */
public class NewDealReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String deal_id = extras.getString("deal_id");
        String outlet_id = "0";//extras.getString("outlet_id");
        if (deal_id != null) {
            Intent intentGetDealDetail = new Intent(context, RealTimeService.class);
            intentGetDealDetail.setAction(RealTimeService.ACTION_GET_DEAL_DETAIL_TO_NOTIFY_NEW_DEAL);
            intentGetDealDetail.putExtra(RealTimeService.EXTRA_DEAL_ID, deal_id);
            intentGetDealDetail.putExtra(RealTimeService.EXTRA_OUTLET_ID, outlet_id);
           // context.startService(intentGetDealDetail);

            cancelAlarm(Integer.parseInt(deal_id), context);
        }
    }

    private void cancelAlarm(int alarmCode_groupid, Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, NewDealReceiver.class);

        PendingIntent intentExecuted = PendingIntent.getBroadcast(context, alarmCode_groupid, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(intentExecuted);
    }
}
