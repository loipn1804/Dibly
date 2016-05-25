package com.dibs.dibly.activity;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.adapter.WheelSortAdapter;
import com.dibs.dibly.application.MyApplication;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.DealAvailableController;
import com.dibs.dibly.daocontroller.DealClaimedController;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.MyLocationController;
import com.dibs.dibly.daocontroller.NotifyController;
import com.dibs.dibly.daocontroller.PermanentController;
import com.dibs.dibly.daocontroller.SearchDealController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.fragment.DealHomeFragment;
import com.dibs.dibly.fragment.DealMapFragment;
import com.dibs.dibly.service.ParallaxService;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import greendao.MyLocation;
import greendao.Notify;
import greendao.ObjectDeal;
import greendao.ObjectUser;
import kankan.wheel.widget.WheelView;

/**
 * Created by USER on 6/29/2015.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {

//    private ViewPager viewPager;
//    private PagerSlidingTabStrip tabStrip;
//    private HomePagerAdapter homePagerAdapter;

    private LinearLayout lnlContain;

    private DrawerLayout drawerLayout;
    protected ActionBarDrawerToggle drawerToggle;
    private RelativeLayout rltMenu;
    private RelativeLayout rltSearch;
    private RelativeLayout rltSort;
    private RelativeLayout rltAvatar;

    private LinearLayout lnlDeal;
    private LinearLayout lnlDiscovery;
    private LinearLayout lnlPurchaseDeal;
    private LinearLayout lnlMerchantList;
    private LinearLayout lnlMyProfile;
    private LinearLayout lnlSetting;
    private LinearLayout lnlFollowing;

    private LinearLayout lnlInviteFriend;

    private LinearLayout lnlNotSignupYet;
    private LinearLayout lnlSignupAlready, lnlSignOut;

    private TextView txtWelcomeBack, txtTitle;
    private RelativeLayout btnSignupToday;
    private ImageView imvAvatar, btnMap;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public static String RECEIVER_SORT_LIST = "RECEIVER_SORT_LIST";
    public static String EXTRA_SORT = "EXTRA_SORT";
    public static String EXTRA_ORDER = "EXTRA_ORDER";
    public static int DISTANCE = 0;
    public static int START_AT = 1;
    public static int ASC = 0;
    public static int DESC = 1;
    public static int sort;
    public static int order;
    public static List<String> listSort;
    public static List<String> listOrder;
    public static boolean isHome = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (UserController.isLogin(HomeActivity.this)) {
            ObjectUser user = UserController.getCurrentUser(HomeActivity.this);
            if (user.getIsConsumer()) {
                JSONObject object = new JSONObject();
                try {
                    object.put("time", StaticFunction.getCurrentTime());
                    object.put("email", user.getEmail());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                trackMixPanel(getString(R.string.Enter_Deal_Around_You), object);
                startDurationMixPanel(getString(R.string.Duration_Deal_Around_You));
            }
        }

        overrideFontsLight(findViewById(R.id.root));

        options = new DisplayImageOptions.Builder().cacheInMemory(false)
                .displayer(new RoundedBitmapDisplayer(500))
                .showImageForEmptyUri(R.drawable.menu_person)
                .showImageOnLoading(R.drawable.menu_person)
                .cacheOnDisk(true).build();

        listSort = new ArrayList<>();
        listOrder = new ArrayList<>();

        listSort.add("Distance");
        listSort.add("Starting time");
        listOrder.add("Ascending");
        listOrder.add("Descending");
        sort = DISTANCE;
        order = ASC;

        DealHomeFragment.restartLocationServiceToCheckLocationEnable(HomeActivity.this);
        initialToolBar();
        initView();
        initDrawer();
        initData();
        registerReceiver();
    }

    private void registerReceiver() {
        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_LOGOUT);
            registerReceiver(activityReceiver, intentFilter);
        }
    }

    private void initView() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        lnlContain = (LinearLayout) findViewById(R.id.lnlContain);
        rltMenu = (RelativeLayout) findViewById(R.id.rltMenu);
        rltSearch = (RelativeLayout) findViewById(R.id.rltSearch);
        rltAvatar = (RelativeLayout) findViewById(R.id.rltAvatar);
        rltSort = (RelativeLayout) findViewById(R.id.rltSort);
        lnlDeal = (LinearLayout) findViewById(R.id.lnlDeal);
        lnlDiscovery = (LinearLayout) findViewById(R.id.lnlDiscovery);
        lnlPurchaseDeal = (LinearLayout) findViewById(R.id.lnlPurchaseDeal);
        lnlNotSignupYet = (LinearLayout) findViewById(R.id.lnlNotSignupYet);
        lnlSignupAlready = (LinearLayout) findViewById(R.id.lnlSignupAlready);
        lnlMerchantList = (LinearLayout) findViewById(R.id.lnlMerchantList);
        lnlInviteFriend = (LinearLayout) findViewById(R.id.lnlInviteFriend);
        lnlMyProfile = (LinearLayout) findViewById(R.id.lnlMyProfile);
        lnlSetting = (LinearLayout) findViewById(R.id.lnlSetting);
        lnlFollowing = (LinearLayout) findViewById(R.id.lnlFollowing);
        lnlSignOut = (LinearLayout) findViewById(R.id.lnlSignOut);
        txtWelcomeBack = (TextView) findViewById(R.id.txtWelcomeBack);
        btnSignupToday = (RelativeLayout) findViewById(R.id.btnSignupToday);
        imvAvatar = (ImageView) findViewById(R.id.imvAvatar);
        btnMap = (ImageView) findViewById(R.id.btnMap);
        rltMenu.setOnClickListener(this);
        rltSearch.setOnClickListener(this);
        rltSort.setOnClickListener(this);
        lnlDeal.setOnClickListener(this);
        btnMap.setOnClickListener(this);
        lnlDiscovery.setOnClickListener(this);
        lnlPurchaseDeal.setOnClickListener(this);
        lnlMerchantList.setOnClickListener(this);
        lnlInviteFriend.setOnClickListener(this);
        imvAvatar.setOnClickListener(this);
        txtWelcomeBack.setOnClickListener(this);
        btnSignupToday.setOnClickListener(this);
        lnlMyProfile.setOnClickListener(this);
        lnlSetting.setOnClickListener(this);
        lnlFollowing.setOnClickListener(this);
        lnlSignOut.setOnClickListener(this);
    }

    private void initData() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putInt("page", 0);
        DealHomeFragment dealHomeFragment = new DealHomeFragment();
        dealHomeFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.lnlContain, dealHomeFragment, "short");
        fragmentTransaction.commit();
    }

    private void changeToMap() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DealMapFragment dealHomeFragment = new DealMapFragment();
        fragmentTransaction.replace(R.id.lnlContain, dealHomeFragment, "short");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        isHome = false;

        btnMap.setImageResource(R.drawable.ic_map_back);
        txtTitle.setText("Deals on map");
    }

    private void changeToDeal() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
        isHome = true;
        btnMap.setImageResource(R.drawable.ic_map);
        txtTitle.setText("Deals around you");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (!isHome) {
                changeToDeal();
            } else {
                finish();
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltMenu:
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.rltSearch:
                Intent intentSearch = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intentSearch);
                break;
            case R.id.rltSort:
                showPopupSortOld();
                break;
            case R.id.lnlDeal:
                drawerLayout.closeDrawer(Gravity.LEFT);
                if (!isHome) {
                    changeToDeal();
                }

                break;
            case R.id.lnlDiscovery:
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intentDiscovery = new Intent(HomeActivity.this, DiscoveryActivity.class);
                startActivity(intentDiscovery);
                break;
            case R.id.lnlPurchaseDeal:
                if (UserController.isLogin(HomeActivity.this)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    Intent intentPurchaseDeal = new Intent(HomeActivity.this, PurchasedDealActivity.class);
                    startActivity(intentPurchaseDeal);
                } else {
                    showToastInfo(getString(R.string.login_first));
                }
                break;
            case R.id.lnlMerchantList:
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intentMerchantList = new Intent(HomeActivity.this, MerchantListActivity.class);
                startActivity(intentMerchantList);
                break;
            case R.id.lnlInviteFriend:
                if (UserController.isLogin(this)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    Intent intentInviteFriend = new Intent(HomeActivity.this, InviteFriendActivity.class);
                    startActivity(intentInviteFriend);
                } else {
                    showToastInfo(getString(R.string.login_first));
                }
                break;
            case R.id.imvAvatar:
                Intent intentAccountSetting = new Intent(HomeActivity.this, AccountInfoActivity.class);
                startActivityForResult(intentAccountSetting, AccountSettingActivity.REQUEST_CODE);
                break;
            case R.id.txtWelcomeBack:
                Intent intentAccountSetting2 = new Intent(HomeActivity.this, AccountInfoActivity.class);
                startActivityForResult(intentAccountSetting2, AccountSettingActivity.REQUEST_CODE);
                break;
            case R.id.btnSignupToday:
                Intent intentSignup = new Intent(HomeActivity.this, PreLoginActivity.class);
                startActivity(intentSignup);
                break;
            case R.id.btnMap:
               /* Intent intentDealMap = new Intent(HomeActivity.this, DealsMapAroundYouActivity.class);
                startActivity(intentDealMap); */
                if (isHome) {
                    changeToMap();
                } else {
                    changeToDeal();
                }
                break;
            case R.id.lnlMyProfile:
                Intent intentMyProfile = new Intent(HomeActivity.this, AccountInfoActivity.class);
                startActivity(intentMyProfile);
                break;
            case R.id.lnlSetting:
                Intent intentSetting = new Intent(HomeActivity.this, AccountSettingActivity.class);
                startActivity(intentSetting);
                break;
            case R.id.lnlFollowing:
                Intent intentFollowingMerchant = new Intent(HomeActivity.this, FollowingMerchantDealsActivity.class);
                startActivity(intentFollowingMerchant);
                break;
            case R.id.lnlSignOut:
                showPopupConfirmLogout("Do you wish to log out?");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == AccountSettingActivity.REQUEST_CODE) {
            Intent intentPreLogin = new Intent(HomeActivity.this, PreLoginActivity.class);
            startActivity(intentPreLogin);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyApplication.CurrentActivity == null) {
            MyApplication.CurrentActivity = this;
        }


        if (NotifyController.getSize(HomeActivity.this) >= 3) {
            NotifyController.clearAll(HomeActivity.this);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(0);
        }

        if (PermanentController.getAllDeals(this).size() == 0) {
            initAllPermanentData();
        }

        if (UserController.isLogin(HomeActivity.this)) {
            ObjectUser user = UserController.getCurrentUser(HomeActivity.this);
            if (user.getIsConsumer()) {
                lnlNotSignupYet.setVisibility(View.INVISIBLE);
                lnlSignupAlready.setVisibility(View.VISIBLE);
                txtWelcomeBack.setText(user.getFull_name());
                int paddingDP = (int) (3 * StaticFunction.getDensity(HomeActivity.this));
                rltAvatar.setPadding(paddingDP, paddingDP, paddingDP, paddingDP);
                imvAvatar.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(user.getFacebook_id())) {
                    if (!user.getProfile_image().endsWith("/")) {
                        imageLoader.displayImage(user.getProfile_image(), imvAvatar, options);
                    }
                } else {
                    imageLoader.displayImage("https://graph.facebook.com/" + user.getFacebook_id() + "/picture?height=200&width=200", imvAvatar, options);
                }
            } else {
                finish();
            }
        } else {
            imvAvatar.setVisibility(View.VISIBLE);
            lnlNotSignupYet.setVisibility(View.VISIBLE);
            lnlSignupAlready.setVisibility(View.INVISIBLE);
        }

        if (UserController.isLogin(this)) {
            ObjectUser user = UserController.getCurrentUser(HomeActivity.this);
            if (user.getIsConsumer()) {
                SharedPreferences preferences = getSharedPreferences("user_login", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                boolean is_first = preferences.getBoolean("is_first", false);
                if (is_first) {
                    Intent intentAccountUpdateFirstTime = new Intent(this, AccountUpdateFirstTimeActivity.class);
                    startActivity(intentAccountUpdateFirstTime);
                    editor.putBoolean("is_first", false);
                    editor.commit();
                } else {
                    if (UserController.isLoginByFb(this) && UserController.getCurrentUser(this).getEmail().length() == 0) {
                        Intent intentEmail = new Intent(this, AccountUpdateEmail.class);
                        startActivity(intentEmail);
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        MyApplication.CurrentActivity = null;
        super.onDestroy();
        if (activityReceiver != null) {
            this.unregisterReceiver(activityReceiver);
        }

        if (UserController.isLogin(HomeActivity.this)) {
            ObjectUser user = UserController.getCurrentUser(HomeActivity.this);
            if (user.getIsConsumer()) {
                JSONObject object = new JSONObject();
                try {
                    object.put("time", StaticFunction.getCurrentTime());
                    object.put("email", user.getEmail());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                trackMixPanel(getString(R.string.Exit_Deal_Around_You), object);

                JSONObject objectDuration = new JSONObject();
                try {
                    objectDuration.put("email", user.getEmail());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                endDurationMixPanel(getString(R.string.Duration_Deal_Around_You), objectDuration);
            }
        }
    }


    private void initAllPermanentData() {
        MyLocation myLocation = MyLocationController.getLastLocation(this);
        if (myLocation != null) {
            Intent intentGetDeal = new Intent(this, ParallaxService.class);
            intentGetDeal.setAction(ParallaxService.ACTION_SEARCH_DEAL);
            intentGetDeal.putExtra(ParallaxService.EXTRA_LATITUDE, myLocation.getLatitude() + "");
            intentGetDeal.putExtra(ParallaxService.EXTRA_LONGITUDE, myLocation.getLongitude() + "");
            intentGetDeal.putExtra(ParallaxService.EXTRA_PAGE, "1");

            String keyword = "";
            String deal_type = "";
            String discovery_type = "";
            String min = "";
            String max = "";

            intentGetDeal.putExtra(ParallaxService.EXTRA_KEYWORD, keyword);
            intentGetDeal.putExtra(ParallaxService.EXTRA_DEAL_TYPE, deal_type);
            intentGetDeal.putExtra(ParallaxService.EXTRA_DISCOVERY_TYPE, discovery_type);
            intentGetDeal.putExtra(ParallaxService.EXTRA_MIN, min);
            intentGetDeal.putExtra(ParallaxService.EXTRA_MAX, max);

            startService(intentGetDeal);
        }
    }

    public void showPopupSortOld() {
        // custom dialog
        final Dialog dialog = new Dialog(HomeActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_sort);
        overrideFontsLight(dialog.findViewById(R.id.root));

        WheelSortAdapter timeAdapter1;
        WheelSortAdapter timeAdapter2;

        final WheelView wheel1 = (WheelView) dialog.findViewById(R.id.whl1);
        final WheelView wheel2 = (WheelView) dialog.findViewById(R.id.whl2);
        TextView txtLabelSort = (TextView) dialog.findViewById(R.id.txtLabelSort);
        TextView txtDone = (TextView) dialog.findViewById(R.id.txtDone);
        LinearLayout lnlSpace = (LinearLayout) dialog.findViewById(R.id.lnlSpace);

        overrideFontsBold(txtLabelSort);

        timeAdapter1 = new WheelSortAdapter(HomeActivity.this, listSort);
        wheel1.setVisibleItems(3); // Number of items
        wheel1.setWheelBackground(R.drawable.wheel_bg);
        wheel1.setWheelForeground(R.drawable.wheel_fg);
        wheel1.setShadowColor(0xFFffffff, 0x99ffffff, 0x33ffffff);
        wheel1.setViewAdapter(timeAdapter1);
        wheel1.setCurrentItem(sort);

        timeAdapter2 = new WheelSortAdapter(HomeActivity.this, listOrder);
        wheel2.setVisibleItems(3); // Number of items
        wheel2.setWheelBackground(R.drawable.wheel_bg);
        wheel2.setWheelForeground(R.drawable.wheel_fg);
        wheel2.setShadowColor(0xFFffffff, 0x99ffffff, 0x33ffffff);
        wheel2.setViewAdapter(timeAdapter2);
        wheel2.setCurrentItem(order);

        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort = wheel1.getCurrentItem();
                order = wheel2.getCurrentItem();
                sendBroadcastSort(RECEIVER_SORT_LIST);
                dialog.dismiss();
            }
        });

        lnlSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showPopupSortV10() {
        // custom dialog
        final Dialog dialog = new Dialog(HomeActivity.this);


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_sort_home_v10);
        overrideFontsLight(dialog.findViewById(R.id.root));

        TextView txtLabelSort = (TextView) dialog.findViewById(R.id.txtLabelSort);
        TextView txtDone = (TextView) dialog.findViewById(R.id.txtDone);
        final TextView txtDistance = (TextView) dialog.findViewById(R.id.txtDistance);
        final TextView txtStartingTime = (TextView) dialog.findViewById(R.id.txtStartingTime);
        final TextView txtAsc = (TextView) dialog.findViewById(R.id.txtAsc);
        final TextView txtDesc = (TextView) dialog.findViewById(R.id.txtDesc);
        LinearLayout lnlSpace = (LinearLayout) dialog.findViewById(R.id.lnlSpace);

        overrideFontsBold(txtLabelSort);

        if (sort == 0) {
            txtDistance.setBackgroundColor(getResources().getColor(R.color.gray_bg_call_dibs));
            txtDistance.setTextColor(getResources().getColor(R.color.orange_main));
            txtStartingTime.setBackgroundColor(getResources().getColor(R.color.white));
            txtStartingTime.setTextColor(getResources().getColor(R.color.black));
        } else {
            txtStartingTime.setBackgroundColor(getResources().getColor(R.color.gray_bg_call_dibs));
            txtStartingTime.setTextColor(getResources().getColor(R.color.orange_main));
            txtDistance.setBackgroundColor(getResources().getColor(R.color.white));
            txtDistance.setTextColor(getResources().getColor(R.color.black));
        }

        if (order == 0) {
            txtAsc.setBackgroundColor(getResources().getColor(R.color.gray_bg_call_dibs));
            txtAsc.setTextColor(getResources().getColor(R.color.orange_main));
            txtDesc.setBackgroundColor(getResources().getColor(R.color.white));
            txtDesc.setTextColor(getResources().getColor(R.color.black));
        } else {
            txtDesc.setBackgroundColor(getResources().getColor(R.color.gray_bg_call_dibs));
            txtDesc.setTextColor(getResources().getColor(R.color.orange_main));
            txtAsc.setBackgroundColor(getResources().getColor(R.color.white));
            txtAsc.setTextColor(getResources().getColor(R.color.black));
        }

        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBroadcastSort(RECEIVER_SORT_LIST);
                dialog.dismiss();
            }
        });

        lnlSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        txtDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort = 0;
                txtDistance.setBackgroundColor(getResources().getColor(R.color.gray_bg_call_dibs));
                txtDistance.setTextColor(getResources().getColor(R.color.orange_main));
                txtStartingTime.setBackgroundColor(getResources().getColor(R.color.white));
                txtStartingTime.setTextColor(getResources().getColor(R.color.black));
            }
        });

        txtStartingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort = 1;
                txtStartingTime.setBackgroundColor(getResources().getColor(R.color.gray_bg_call_dibs));
                txtStartingTime.setTextColor(getResources().getColor(R.color.orange_main));
                txtDistance.setBackgroundColor(getResources().getColor(R.color.white));
                txtDistance.setTextColor(getResources().getColor(R.color.black));
            }
        });

        txtAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order = 0;
                txtAsc.setBackgroundColor(getResources().getColor(R.color.gray_bg_call_dibs));
                txtAsc.setTextColor(getResources().getColor(R.color.orange_main));
                txtDesc.setBackgroundColor(getResources().getColor(R.color.white));
                txtDesc.setTextColor(getResources().getColor(R.color.black));
            }
        });

        txtDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order = 1;
                txtDesc.setBackgroundColor(getResources().getColor(R.color.gray_bg_call_dibs));
                txtDesc.setTextColor(getResources().getColor(R.color.orange_main));
                txtAsc.setBackgroundColor(getResources().getColor(R.color.white));
                txtAsc.setTextColor(getResources().getColor(R.color.black));
            }
        });

        dialog.show();
    }

    public void hideAndShowToolbar(boolean isShow) {
        if (isShow) {
            if (toolbar != null) {
                toolbar.setVisibility(View.VISIBLE);
            }
        } else {
            if (toolbar != null) {
                toolbar.setVisibility(View.GONE);
            }
        }
    }

    private void sendBroadcastSort(String action) {
        Intent i = new Intent();
        i.setAction(action);
        sendBroadcast(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_noitem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initDrawer() {

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.open) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (drawerToggle != null)
            drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (drawerToggle != null)
            drawerToggle.onConfigurationChanged(newConfig);
    }

    public void showPopupConfirmLogout(String message) {

        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_confirm);

        overrideFontsLight(dialog.findViewById(R.id.root));

        LinearLayout lnlOk = (LinearLayout) dialog.findViewById(R.id.lnlOk);
        LinearLayout lnlCancel = (LinearLayout) dialog.findViewById(R.id.lnlCancel);
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        txtMessage.setText(message);

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogout = new Intent(HomeActivity.this, RealTimeService.class);
                intentLogout.setAction(RealTimeService.ACTION_LOGOUT);
                intentLogout.putExtra(RealTimeService.EXTRA_USER_ID, UserController.getCurrentUser(HomeActivity.this).getUser_id() + "");
                startService(intentLogout);
                showProgressDialog();
                dialog.dismiss();
            }
        });

        lnlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_LOGOUT)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {

                    UserController.clearAllUsers(HomeActivity.this);
                    DealAvailableController.clearAll(HomeActivity.this);
                    DealClaimedController.clearAll(HomeActivity.this);
                    SearchDealController.clearAllDeals(HomeActivity.this);

                    SharedPreferences preferences = getSharedPreferences("data_token", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", "");
                    editor.commit();
                    StaticFunction.TOKEN = "";

                    List<ObjectDeal> dealList = DealController.getAllDeals(HomeActivity.this);
                    for (ObjectDeal deal : dealList) {
                        deal.setF_call_dibs(false);
                        deal.setF_claimed(false);
                        DealController.update(HomeActivity.this, deal);
                    }

                    hideProgressDialog();


                    List<Notify> notifies = NotifyController.getAll(HomeActivity.this);
                    for (Notify notify : notifies) {
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancel(Integer.parseInt(notify.getDeal_id() + ""));
                    }
                    NotifyController.clearAll(HomeActivity.this);

                    Intent intentHome = new Intent(context, PreLoginActivity.class);
                    intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentHome);
                    finish();
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                    hideProgressDialog();
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                    hideProgressDialog();
                }
            }
        }
    };
}
