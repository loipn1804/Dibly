package com.dibs.dibly.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by USER on 05/24/2016.
 */
public class PackageReplacedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "PackageReplacedReceiver", Toast.LENGTH_SHORT).show();
//        Intent intentService = new Intent(context, MixPanelService.class);
//        context.startService(intentService);
    }
}
