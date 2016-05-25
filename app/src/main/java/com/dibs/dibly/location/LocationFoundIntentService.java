package com.dibs.dibly.location;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.dibs.dibly.R;
import com.dibs.dibly.consts.Const;
import com.dibs.dibly.daocontroller.MyLocationController;
import com.dibs.dibly.daocontroller.NotifyController;
import com.dibs.dibly.daocontroller.PermanentController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.google.android.gms.location.FusedLocationProviderApi;

import java.util.ArrayList;
import java.util.List;

import greendao.MyLocation;
import greendao.Notify;
import greendao.PermanentDeals;


/**
 * Created by USER on 4/24/2015.
 */
public class LocationFoundIntentService extends IntentService {

    Handler han;
    public static List<PermanentDeals> listPLocation = new ArrayList<>();

    public LocationFoundIntentService() {
        super(LocationFoundIntentService.class.getName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        han = new Handler();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent.hasExtra(FusedLocationProviderApi.KEY_LOCATION_CHANGED)) {
            Location location = intent.getParcelableExtra(FusedLocationProviderApi.KEY_LOCATION_CHANGED);

            if (location != null) {

                boolean isNotify = true;
                if (UserController.isLogin(LocationFoundIntentService.this)) {
                    if (!UserController.getCurrentUser(LocationFoundIntentService.this).getIsConsumer()) {
                        isNotify = false;
                    }
                }
                /*han.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(LocationFoundIntentService.this, "On Location Change: OKKKKKKKKKKK ", Toast.LENGTH_LONG).show();
                    }
                });*/

                if (isNotify) {
                    listPLocation = new ArrayList<>();
                    listPLocation = PermanentController.getDealThatALive(LocationFoundIntentService.this);//DLocation.findDealLocations(LocationFoundIntentService.this);

                    MyLocation lastMyLoc = MyLocationController.getLastLocation(LocationFoundIntentService.this);

                    MyLocation myLoc = new MyLocation();
                    //myLoc.setId(Const.MY_LOCATION_ID);
                    myLoc.setLatitude(location.getLatitude());
                    myLoc.setLongitude(location.getLongitude());

                    myLoc.setLastUpdateTime(GoogleLocationService.getTime());


                    float result1[] = new float[3];
                    if (lastMyLoc != null) {
                        Location.distanceBetween(location.getLatitude(), location.getLongitude(), lastMyLoc.getLatitude(), lastMyLoc.getLongitude(), result1);
                        myLoc.setDistance((double) result1[0]);
                    }

                    MyLocationController.insertOrUpdateMyLocation(LocationFoundIntentService.this, myLoc);


                    { // cavulate distant insert into deal

                    }

                    float result[] = new float[3];

                    for (int i = 0; i < listPLocation.size(); i++) {
                        PermanentDeals pLocation = listPLocation.get(i);

                        Location.distanceBetween(location.getLatitude(), location.getLongitude(), pLocation.getLatitude(), pLocation.getLongitude(), result);

                        // enter location
                        if (result[0] <= Const.DISTANCE_NOTIFY_IN_METTER) {
                            NotifyController.insert(LocationFoundIntentService.this, new Notify(pLocation.getDeal_id()));
                            if (NotifyController.getSize(LocationFoundIntentService.this) < 3) {
                                StaticFunction.sendNotification(LocationFoundIntentService.this, pLocation.getTitle(), pLocation.getDeal_id(), Integer.parseInt(pLocation.getDeal_id() + ""), pLocation.getMerchant_id());
                            } else {
                                List<Notify> notifyList = NotifyController.getAll(LocationFoundIntentService.this);
                                for (Notify notify : notifyList) {
                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.cancel(Integer.parseInt(notify.getDeal_id() + ""));
                                }
                                String mes = "There are " + NotifyController.getSize(LocationFoundIntentService.this) + " new deals.";
                                StaticFunction.sendNotificationMultiDeal(LocationFoundIntentService.this, mes, "0", 0, R.drawable.ic_launcher);
                            }

                            pLocation.setIsNotified(1);
                            PermanentController.update(LocationFoundIntentService.this, pLocation);
                            // pLocation.outletInfo.setIsNotified(1);
                            // DealOutletController.update(LocationFoundIntentService.this, pLocation.outletInfo);
                        } else // exit location
                        {
                        }
                    }
                }
            } else {
                han.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LocationFoundIntentService.this, "Null on location. Reported by: Binh", Toast.LENGTH_LONG).show();
                    }
                });

            }

        }
    }
}
