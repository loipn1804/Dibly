package com.dibs.dibly.location;

import android.app.Dialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.dibs.dibly.R;
import com.dibs.dibly.application.MyApplication;
import com.dibs.dibly.consts.Const;
import com.dibs.dibly.daocontroller.MyLocationController;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import greendao.MyLocation;

/**
 * Created by USER on 4/18/2015.
 */
public class GoogleLocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult>//, LocationListener
{

    public static final String TAG = "com.em.loc.tag.emsbilo.ser.sticky";

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    //public static Location mCurrentLocation;

    /**
     * Time when the location was updated represented as a String.
     */
    //protected String mLastUpdateTime;

    public int currentRequestTime = 30;

    PowerManager.WakeLock wakeLock = null;


    private Intent mIntentService;
    private PendingIntent mPendingIntent;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    protected LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Constant used in the location settings dialog.
     */
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildGoogleApiClient(Const.UPDATE_INTERVAL_IN_MILLISECONDS);
        mGoogleApiClient.connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mIntentService = new Intent(this, LocationFoundIntentService.class);
        mPendingIntent = PendingIntent.getService(this, 1001, mIntentService, PendingIntent.FLAG_CANCEL_CURRENT);

        if (mGoogleApiClient == null) {
            buildGoogleApiClient(Const.UPDATE_INTERVAL_IN_MILLISECONDS);
            mGoogleApiClient.connect();
        }

        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected synchronized void buildGoogleApiClient(long requestTimeInterval) {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        createLocationRequest(requestTimeInterval);

        buildLocationSettingsRequest();
    }

    protected void createLocationRequest(long requestTimeInterval) {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(requestTimeInterval);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(requestTimeInterval);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    protected void startLocationUpdates() {
        // use location listener
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        // use pending intent
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mPendingIntent);
    }

    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    public static String getTime() {
//        Calendar c = Calendar.getInstance();
//        int seconds = c.get(Calendar.SECOND);
//        int minute = c.get(Calendar.MINUTE);
//        int hour = c.get(Calendar.HOUR_OF_DAY);

//        return hour + ":" + minute + ":" + seconds;
        return System.currentTimeMillis() + "";
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location != null) {

            MyLocation myLoc = new MyLocation();
            //myLoc.setId(Const.MY_LOCATION_ID);
            myLoc.setLatitude(location.getLatitude());
            myLoc.setLongitude(location.getLongitude());
            myLoc.setLastUpdateTime(getTime());
            myLoc.setDistance(00.00);

            MyLocationController.insertOrUpdateMyLocation(GoogleLocationService.this, myLoc);

            MyApplication.CurrentActivity = null;

            Log.e("GOT LOC", "LOCATION OK");
        } else {
            Log.e("NO LOC", "LOC = NULL");
            if (MyApplication.CurrentActivity != null) {
                // ASK user to enable location update
                showPopupAskUserToTurnOnGPS("Dear Valued User\nWe cannot provide you the best deals information if we cannot access your location! So please help us to turn on your Location Settings by clicking YES on the next popup\nWe promise your information is keep privately\nThank you");
            } else {

            }

        }

        startLocationUpdates();
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();

        // not use anymore
        // now we will ask user to choose YES in pre login actyivity
        // checkLocationSettings();
    }

    public void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mLocationSettingsRequest);
        result.setResultCallback(GoogleLocationService.this);
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");
                //startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" + "upgrade location settings ");

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(MyApplication.CurrentActivity, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " + "not created.");
                break;
        }

    }

    public void showPopupAskUserToTurnOnGPS(String message) {
        // custom dialog
        // system popup
        final Dialog dialog = new Dialog(MyApplication.CurrentActivity);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.popup_confirm_ok_only);

        StaticFunction.overrideFontsLight(GoogleLocationService.this, dialog.findViewById(R.id.root));

        LinearLayout lnlOk = (LinearLayout) dialog.findViewById(R.id.lnlOk);
        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocationSettings();
                dialog.dismiss();
            }
        });


        dialog.show();
    }


}
