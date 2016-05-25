package com.dibs.dibly.staticfunction;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.activity.ClaimSuccessActivity;
import com.dibs.dibly.activity.DealDetailActivity;
import com.dibs.dibly.activity.GuideActivity;
import com.dibs.dibly.activity.HomeActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by USER on 4/22/2015.
 */
public class StaticFunction {

    public static String TOKEN = "";
    public static String MixPanelToken = "f66c393ffa9d53a5182651708e2278b0";

    public static int TIME_DELAY_NOTIFY = 60; // sec
    public static int TIME_DELAY_NEW_DEAL = 180; // sec

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static void sendNotification(Context ctx, String message, long deal_id, int notifyId, long merchant_id) {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(ctx.getApplicationContext(), DealDetailActivity.class);
        notificationIntent.setAction("dibly_1" + System.currentTimeMillis());
        notificationIntent.putExtra("deal_id", deal_id);
        notificationIntent.putExtra("merchant_id", merchant_id);
        notificationIntent.putExtra("from", "notify");

//        // Construct a task stack.
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
//
//        // Add the main Activity to the task stack as the parent.
//        stackBuilder.addParentStack(DealDetailActivity.class);
//
//        // Push the content Intent onto the stack.
//        stackBuilder.addNextIntent(notificationIntent);
//
//        // Get a PendingIntent containing the entire back stack.
//        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        PendingIntent notificationPendingIntent = PendingIntent.getActivity(ctx.getApplicationContext(), notifyId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);

        // Define the notification settings.
        builder.setContentTitle("Dibly").setContentText(message).setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.icon_dibly_noti);
            builder.setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.icon_dibly));
        } else {
            builder.setSmallIcon(R.drawable.icon_dibly);
        }

        builder.setVibrate(new long[]{1000, 500});


        // Get an instance of the Notification manager
        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(notifyId, builder.build());
    }

    public static void sendNotificationMultiDeal(Context ctx, String message, String deal_id, int notifyId, int iconResource) {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(ctx.getApplicationContext(), HomeActivity.class);
        notificationIntent.setAction("dibly_2" + System.currentTimeMillis());
//        notificationIntent.putExtra("deal_id", Long.parseLong(deal_id));

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(ctx.getApplicationContext(), notifyId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx.getApplicationContext());

        // Define the notification settings.
        builder.setContentTitle("Dibly")
                .setContentText(message)
                .setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.icon_dibly_noti);
            builder.setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.icon_dibly));
        } else {
            builder.setSmallIcon(R.drawable.icon_dibly);
        }

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        builder.setVibrate(new long[]{1000, 500});

        builder.setContentIntent(notificationPendingIntent);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager = (NotificationManager) ctx.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(notifyId, builder.build());
    }

    public static void sendNotificationClaimed(String message, String deal_id, String merchant_id, int notifyId, Context context) {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(context, ClaimSuccessActivity.class);
        notificationIntent.putExtra("message", message);
        notificationIntent.putExtra("deal_id", deal_id);
        notificationIntent.putExtra("merchant_id", merchant_id);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, notifyId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        // Define the notification settings.
        builder.setContentTitle("Dibly")
                .setContentText(message)
                .setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.icon_dibly_noti);
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_dibly));
        } else {
            builder.setSmallIcon(R.drawable.icon_dibly);
        }

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        builder.setVibrate(new long[]{0, 2000});

        builder.setContentIntent(notificationPendingIntent);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(notifyId, builder.build());
    }

    public static void sendNotificationTest(Context ctx) {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(ctx.getApplicationContext(), GuideActivity.class);

//        // Construct a task stack.
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
//
//        // Add the main Activity to the task stack as the parent.
//        stackBuilder.addParentStack(DealDetailActivity.class);
//
//        // Push the content Intent onto the stack.
//        stackBuilder.addNextIntent(notificationIntent);
//
//        // Get a PendingIntent containing the entire back stack.
//        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        PendingIntent notificationPendingIntent = PendingIntent.getActivity(ctx.getApplicationContext(), 123, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);

        // Define the notification settings.
//        builder.setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_launcher));
        builder.setContentTitle("Dibly").setContentText("Test").setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.icon_dibly_noti);
            builder.setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.icon_dibly));
        } else {
            builder.setSmallIcon(R.drawable.icon_dibly);
        }

        builder.setVibrate(new long[]{1000, 500});


        // Get an instance of the Notification manager
        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(123, builder.build());
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static float getDensity(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

    public static boolean checkFbInstalled(Activity activity) {
        PackageManager pm = activity.getPackageManager();
        boolean flag = false;
        try {
            pm.getPackageInfo("com.facebook.katana",
                    PackageManager.GET_ACTIVITIES);
            flag = true;
        } catch (PackageManager.NameNotFoundException e) {
            flag = false;
        }
        return flag;
    }

    public static void callPhone(Activity activity, String number) {
        String uri = "tel:" + number.trim();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        activity.startActivity(intent);
    }

    public static void overrideFontsLight(Context context, View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsLight(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(StaticFunction.light(context));
            }
        } catch (Exception e) {
        }
    }

    public static void overrideFontsLightIt(Context context, View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsLightIt(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(StaticFunction.light_it(context));
            }
        } catch (Exception e) {
        }
    }

    public static void overrideFontsBold(Context context, View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsBold(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(StaticFunction.bold(context));
            }
        } catch (Exception e) {
        }
    }

    public static void overrideFontsBoldIt(Context context, View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsBoldIt(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(StaticFunction.bold_it(context));
            }
        } catch (Exception e) {
        }
    }

    public static Typeface bold(Context activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/ProximaNova-Bold.otf");
        return typeface;
    }

    public static Typeface light(Context activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/ProximaNova-Light.otf");
        return typeface;
    }

    public static Typeface bold_it(Context activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/ProximaNova-BoldIt.otf");
        return typeface;
    }

    public static Typeface light_it(Context activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/ProximaNova-LightItalic.otf");
        return typeface;
    }

    public static Typeface semi_bold(Context activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/ProximaNovaCond-Semibold.otf");
        return typeface;
    }

    public static void sendBroad(Context context, String action) {
        Intent i = new Intent();
        i.setAction(action);
        context.sendBroadcast(i);
    }

    public static int getBackgroundAvatarName(String avatarName) {
        if (validate_1(avatarName)) {
            return R.drawable.bg_circle_avatar_1;
        } else if (validate_2(avatarName)) {
            return R.drawable.bg_circle_avatar_2;
        } else if (validate_3(avatarName)) {
            return R.drawable.bg_circle_avatar_3;
        } else if (validate_4(avatarName)) {
            return R.drawable.bg_circle_avatar_4;
        } else if (validate_5(avatarName)) {
            return R.drawable.bg_circle_avatar_5;
        } else if (validate_6(avatarName)) {
            return R.drawable.bg_circle_avatar_6;
        } else return R.drawable.bg_circle_avatar_default;
    }

    public static boolean validate_1(String password) {
        String pattern = "^[A-E]*$";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(password);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validate_2(String password) {
        String pattern = "^[F-J]*$";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(password);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validate_3(String password) {
        String pattern = "^[K-N]*$";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(password);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validate_4(String password) {
        String pattern = "^[O-R]*$";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(password);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validate_5(String password) {
        String pattern = "^[S-V]*$";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(password);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validate_6(String password) {
        String pattern = "^[W-Z]*$";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(password);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }
    }

    public static void showKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        } catch (Exception e) {

        }
    }


    public static String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd hh:mm:ss";
        String outputPattern = "dd-MM-yyyy hh:mm:ss";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getCurrentTime() {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Date date = new Date();
        String str = "";
        str = inputFormat.format(date);
        return str;
    }
}
