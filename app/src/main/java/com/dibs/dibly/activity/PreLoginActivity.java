package com.dibs.dibly.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.fragment.DealHomeFragment;
import com.dibs.dibly.gcm.PlayServicesHelper;
import com.dibs.dibly.service.MixPanelService;
import com.dibs.dibly.service.ParallaxService;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.ClickSpan;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import greendao.ObjectUser;

/**
 * Created by USER on 6/29/2015.
 */
public class PreLoginActivity extends BaseActivity implements View.OnClickListener, Animation.AnimationListener {

    private final List<String> fbPermissions = Arrays.asList("public_profile", "email");
    //    private Session.StatusCallback statusCallback = new SessionStatusCallback();
//    private GraphUser userFb;
    private SimpleFacebook mSimpleFacebook;
    Profile.Properties properties = new Profile.Properties.Builder()
            .add(Profile.Properties.ID)
            .add(Profile.Properties.NAME)
            .add(Profile.Properties.FIRST_NAME)
            .add(Profile.Properties.LAST_NAME)
            .add(Profile.Properties.GENDER)
            .add(Profile.Properties.EMAIL)
            .build();
    private Dialog dialogEmail;

    private LinearLayout btnLogin;
    private LinearLayout btnLoginFb;
    private TextView txtSignupLater;
    private TextView txtTerm;

//    FlakeView flakeView;

    private LinearLayout root1;
    // Animation
    private Animation animFadein;
    private Animation animAlpha;

    private ImageView imageViewLogo;

    private TextView txtTime;

    private AnimationDrawable frameAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prelogin);

        overrideFontsLight(findViewById(R.id.root));
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
//        flakeView = new FlakeView(this);
//        container.addView(flakeView);
        // getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        initView();
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
        animFadein.setAnimationListener(this);

        animAlpha = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_alpha);
        animAlpha.setAnimationListener(this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // root1.startAnimation(animFadein);
                imageViewLogo.startAnimation(animFadein);
                //imageViewLogo.setVisibility(View.VISIBLE);
            }
        }, 500);

//        StaticFunction.sendNotificationTest(PreLoginActivity.this);

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_LOGIN_FB);
            registerReceiver(activityReceiver, intentFilter);
        }

//        getKeHash(this);

        if (!UserController.isLogin(PreLoginActivity.this)) {
            DealHomeFragment.restartLocationServiceToCheckLocationEnable(PreLoginActivity.this);
        }

        SharedPreferences preferences = getSharedPreferences("data_token", MODE_PRIVATE);
        StaticFunction.TOKEN = preferences.getString("token", "");


        initData();

        // get industry type
        Intent intentGetIndustryType = new Intent(this, ParallaxService.class);
        intentGetIndustryType.setAction(ParallaxService.ACTION_GET_INDUSTRY_TYPE);
        startService(intentGetIndustryType);
    }

    private void initView() {

        root1 = (LinearLayout) findViewById(R.id.root1);
        imageViewLogo = (ImageView) findViewById(R.id.imageviewLogo);

        btnLogin = (LinearLayout) findViewById(R.id.btnLogin);
        btnLoginFb = (LinearLayout) findViewById(R.id.btnLoginFb);
        txtSignupLater = (TextView) findViewById(R.id.txtSignupLater);
        txtTerm = (TextView) findViewById(R.id.txtTerm);

        btnLogin.setOnClickListener(this);
        btnLoginFb.setOnClickListener(this);
        txtSignupLater.setOnClickListener(this);


        ImageView imgroot = (ImageView) findViewById(R.id.imgroot);
        // Get the background, which has been compiled to an AnimationDrawable object.
        frameAnimation = (AnimationDrawable) imgroot.getBackground();
        frameAnimation.setEnterFadeDuration(3000);
        frameAnimation.setExitFadeDuration(3000);
        // Start the animation (looped playback by default).
        frameAnimation.start();

       /* TransitionDrawable drawable = (TransitionDrawable) imgroot.getBackground();

        drawable.setCrossFadeEnabled(true);
        drawable.startTransition(5000); */
        //  briefcaseTransition.startTransition(500);

    }

    private void initData() {
        clickify(txtTerm, "Terms", new ClickSpan.OnClickListener() {
            @Override
            public void onClick() {
                Intent intentTerm = new Intent(PreLoginActivity.this, TermActivity.class);
                startActivity(intentTerm);
            }
        }, R.color.orange_main);

        clickify(txtTerm, "Privacy Policy", new ClickSpan.OnClickListener() {
            @Override
            public void onClick() {
                Intent intentPrivacy = new Intent(PreLoginActivity.this, PrivacyActivity.class);
                startActivity(intentPrivacy);
            }
        }, R.color.orange_main);

        String text = txtSignupLater.getText().toString();
        SpannableString s = SpannableString.valueOf(text);
        s.setSpan(new UnderlineSpan(), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtSignupLater.setText(s);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                Intent intentSigninSignup = new Intent(PreLoginActivity.this, SigninSignupActivity.class);
                startActivity(intentSigninSignup);
                break;
            case R.id.btnLoginFb:
                mSimpleFacebook.login(onLoginListener);
                break;
            case R.id.txtSignupLater:
                Intent intentHome = new Intent(PreLoginActivity.this, HomeActivity.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentHome);
                finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        flakeView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(this);

        if (UserController.isLogin(PreLoginActivity.this)) {
            if (UserController.getCurrentUser(PreLoginActivity.this).getIsConsumer()) {
                Intent intentHome = new Intent(PreLoginActivity.this, HomeActivity.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentHome);
                finish();
            } else {
                Intent intentMerchant = new Intent(PreLoginActivity.this, MerChantHomeActivity.class);
                intentMerchant.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentMerchant);
                finish();
            }
        }

        SharedPreferences preferences = getSharedPreferences("first_run", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        boolean is_first = preferences.getBoolean("is_first", true);
        boolean is_first_start_mix_panel = preferences.getBoolean("is_first_start_mix_panel", true);

        if (is_first) {
            Intent intentGuide = new Intent(PreLoginActivity.this, GuideActivity.class);
            startActivity(intentGuide);
            editor.putBoolean("is_first", false);
            editor.commit();
        }

        if (is_first_start_mix_panel) {
            Intent intentService = new Intent(this, MixPanelService.class);
            startService(intentService);
            editor.putBoolean("is_first_start_mix_panel", false);
            editor.commit();
        }

        if (Build.VERSION.SDK_INT < 16) { //ye olde method
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else { // Jellybean and up, new hotness
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSimpleFacebook.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onAnimationStart(Animation animation) {
        if (animation == animAlpha) {
            root1.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == animFadein) {
            root1.startAnimation(animAlpha);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


    OnLoginListener onLoginListener = new OnLoginListener() {

        @Override
        public void onLogin(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
            // change the state of the button or do whatever you want
            mSimpleFacebook.getProfile(properties, onProfileListener);
        }

        @Override
        public void onCancel() {
            // user canceled the dialog
        }

        @Override
        public void onFail(String reason) {
            // failed to login
        }

        @Override
        public void onException(Throwable throwable) {
            // exception from facebook
        }

    };

    OnProfileListener onProfileListener = new OnProfileListener() {
        @Override
        public void onComplete(Profile profile) {

            if (TextUtils.isEmpty(profile.getEmail())) {
                showPopupEnterEmailFb(profile);
            } else {
                Intent intentLogin = new Intent(PreLoginActivity.this, RealTimeService.class);
                intentLogin.setAction(RealTimeService.ACTION_LOGIN_FB);
                intentLogin.putExtra(RealTimeService.EXTRA_FB_ID, profile.getId());
                intentLogin.putExtra(RealTimeService.EXTRA_FB_USERNAME, profile.getName());
                intentLogin.putExtra(RealTimeService.EXTRA_FB_FIRSTNAME, profile.getFirstName());
                intentLogin.putExtra(RealTimeService.EXTRA_FB_LASTNAME, profile.getLastName());
                intentLogin.putExtra(RealTimeService.EXTRA_FB_GENDER, profile.getGender().equalsIgnoreCase("male") ? "M" : "F");
                intentLogin.putExtra(RealTimeService.EXTRA_FB_EMAIL, profile.getEmail());

                startService(intentLogin);
                showProgressDialog();
            }
        }
    };

    private void showPopupEnterEmailFb(final Profile profile) {
        // custom dialog
        dialogEmail = new Dialog(PreLoginActivity.this);

        dialogEmail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEmail.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEmail.setCanceledOnTouchOutside(false);
        dialogEmail.setContentView(R.layout.popup_update_email_fb);

        overrideFontsLight(dialogEmail.findViewById(R.id.root));

        final EditText edtEmail = (EditText) dialogEmail.findViewById(R.id.edtEmail);
        LinearLayout lnlOk = (LinearLayout) dialogEmail.findViewById(R.id.lnlOk);

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                if (email.length() == 0) {
                    showToastError(getString(R.string.blank_email));
                } else {
                    Intent intentLogin = new Intent(PreLoginActivity.this, RealTimeService.class);
                    intentLogin.setAction(RealTimeService.ACTION_LOGIN_FB);
                    intentLogin.putExtra(RealTimeService.EXTRA_FB_ID, profile.getId());
                    intentLogin.putExtra(RealTimeService.EXTRA_FB_USERNAME, profile.getName());
                    intentLogin.putExtra(RealTimeService.EXTRA_FB_FIRSTNAME, profile.getFirstName());
                    intentLogin.putExtra(RealTimeService.EXTRA_FB_LASTNAME, profile.getLastName());
                    intentLogin.putExtra(RealTimeService.EXTRA_FB_GENDER, profile.getGender().equalsIgnoreCase("male") ? "M" : "F");
                    intentLogin.putExtra(RealTimeService.EXTRA_FB_EMAIL, email);

                    startService(intentLogin);
                    showProgressDialog();
                }
            }
        });

        dialogEmail.show();
    }

    private void showPopupInstallFb() {
        // custom dialog
        final Dialog dialog = new Dialog(PreLoginActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_confirm);

        overrideFontsLight(dialog.findViewById(R.id.root));

        LinearLayout lnlOk = (LinearLayout) dialog.findViewById(R.id.lnlOk);
        LinearLayout lnlCancel = (LinearLayout) dialog.findViewById(R.id.lnlCancel);
        TextView txtMsg = (TextView) dialog.findViewById(R.id.txtMessage);
        txtMsg.setText("We need Facebook App installed to use this feature.");

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.facebook.katana")));
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

    private void processRoleUser() {
        if (UserController.isLogin(PreLoginActivity.this)) {
            ObjectUser user = UserController.getCurrentUser(PreLoginActivity.this);
            if (user.getIsConsumer()) {
                PlayServicesHelper playServicesHelper = new PlayServicesHelper(PreLoginActivity.this);
                Intent intentHome = new Intent(PreLoginActivity.this, HomeActivity.class);
                startActivity(intentHome);
                mixpanelAPI.alias(user.getConsumer_id() + "", null);
                finish();
            } else {
                Intent intentMerchant = new Intent(PreLoginActivity.this, MerChantHomeActivity.class);
                intentMerchant.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentMerchant);
//                Intent intentHome = new Intent(LoginActivity.this, HomeActivity.class);
//                startActivity(intentHome);
                finish();
            }
        }
    }

    public void showPopupConfirmLogout(String message) {
        // custom dialog
        final Dialog dialog = new Dialog(PreLoginActivity.this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

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
                Intent intentLogout = new Intent(PreLoginActivity.this, RealTimeService.class);
                intentLogout.setAction(RealTimeService.ACTION_LOGOUT);
                intentLogout.putExtra(RealTimeService.EXTRA_USER_ID, UserController.getCurrentUser(PreLoginActivity.this).getUser_id() + "");
                startService(intentLogout);
                showProgressDialog();
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

    @Override
    protected void onDestroy() {
//        MyApplication.CurrentActivity = null;
        super.onDestroy();
        if (activityReceiver != null) {
            this.unregisterReceiver(activityReceiver);
        }
        if (frameAnimation != null) {
            frameAnimation.stop();
        }

//        flakeView.destroy();
//        flakeView = null;
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_LOGIN_FB)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    processRoleUser();
                    if (dialogEmail != null) {
                        if (dialogEmail.isShowing()) {
                            dialogEmail.dismiss();
                        }
                    }
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
                hideProgressDialog();
            }
        }
    };

    public static String getKeHash(Activity activity) {
        // Add code to print out the key hash
        String keyHash = "";
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo("com.dibs.dibly", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", android.util.Base64.encodeToString(md.digest(), android.util.Base64.DEFAULT));
                keyHash = android.util.Base64.encodeToString(md.digest(), android.util.Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        return keyHash;
    }

}
