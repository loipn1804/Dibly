package com.dibs.dibly.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.activity.ForgotPasswordActivity;
import com.dibs.dibly.activity.PrivacyActivity;
import com.dibs.dibly.activity.SigninSignupActivity;
import com.dibs.dibly.activity.TermActivity;
import com.dibs.dibly.activity.VerifyCodeActivity;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.consts.Const;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.MyLocationController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.gcm.PlayServicesHelper;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.ClickSpan;
import com.dibs.dibly.staticfunction.StaticFunction;

import org.json.JSONException;
import org.json.JSONObject;

import greendao.MyLocation;
import greendao.ObjectUser;

/**
 * Created by USER on 10/30/2015.
 */
public class SigninFragment extends Fragment implements View.OnClickListener {

    private EditText edtEmail;
    private EditText edtPassword;
    private TextView txtForgotPass;
    private TextView txtTerm;
    private TextView txtSignin;

    private String email_mixpanel = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        enterMixPanel();

        StaticFunction.overrideFontsLight(getActivity(), view);

        registerReceiver();

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        edtEmail = (EditText) view.findViewById(R.id.edtEmail);
        edtPassword = (EditText) view.findViewById(R.id.edtPassword);
        txtForgotPass = (TextView) view.findViewById(R.id.txtForgotPass);
        txtTerm = (TextView) view.findViewById(R.id.txtTerm);
        txtSignin = (TextView) view.findViewById(R.id.txtSignin);

        txtSignin.setOnClickListener(this);
        txtForgotPass.setOnClickListener(this);

        edtEmail.addTextChangedListener(new MyTextWatcher());
    }

    private void initData() {
        ((BaseActivity) getActivity()).clickify(txtTerm, "Terms", new ClickSpan.OnClickListener() {
            @Override
            public void onClick() {
                Intent intentTerm = new Intent(getActivity(), TermActivity.class);
                startActivity(intentTerm);
            }
        }, R.color.blue_main);

        ((BaseActivity) getActivity()).clickify(txtTerm, "Privacy Policy", new ClickSpan.OnClickListener() {
            @Override
            public void onClick() {
                Intent intentPrivacy = new Intent(getActivity(), PrivacyActivity.class);
                startActivity(intentPrivacy);
            }
        }, R.color.blue_main);

        edtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        login();
                        break;
                }
                return false;
            }
        });

        SharedPreferences preferences = getActivity().getSharedPreferences("sign_in", Context.MODE_PRIVATE);
        edtEmail.setText(preferences.getString("email", ""));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtSignin:
                login();
                break;
            case R.id.txtForgotPass:
                Intent intentForgotPassword = new Intent(getActivity(), ForgotPasswordActivity.class);
                startActivity(intentForgotPassword);
                break;
        }
    }

    private void registerReceiver() {
        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_LOGIN);
            intentFilter.addAction(SigninSignupActivity.SWITCH_TAB);
            intentFilter.addAction(SigninSignupActivity.SIGN_IN_ACTION);
            getActivity().registerReceiver(activityReceiver, intentFilter);
        }
    }

    private void unRegisterReceiver() {
        if (activityReceiver != null) {
            getActivity().unregisterReceiver(activityReceiver);
        }
    }

    private void login() {
        String email = edtEmail.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();
        if (email.length() == 0) {
            ((BaseActivity) getActivity()).showToastError(getString(R.string.blank_email));
        } else if (pass.length() == 0) {
            ((BaseActivity) getActivity()).showToastError(getString(R.string.blank_pass));
        } else {
            Intent intentLogin = new Intent(getActivity(), RealTimeService.class);
            intentLogin.setAction(RealTimeService.ACTION_LOGIN);
            intentLogin.putExtra(RealTimeService.EXTRA_USER_EMAIL, email);
            intentLogin.putExtra(RealTimeService.EXTRA_USER_PASS, pass);
            getActivity().startService(intentLogin);
            ((BaseActivity) getActivity()).showProgressDialog();
        }
    }

    private void processRoleUser() {

        if (UserController.isExist(getActivity())) {
            ObjectUser user = UserController.getCurrentUser(getActivity());
            if (user.getF_verified_phone() == 0) {
                Intent intentVerifyCode = new Intent(getActivity(), VerifyCodeActivity.class);
                intentVerifyCode.putExtra(RealTimeService.EXTRA_USER_EMAIL, edtEmail.getText().toString().trim());
                intentVerifyCode.putExtra(RealTimeService.EXTRA_USER_PASS, edtPassword.getText().toString().trim());
                intentVerifyCode.putExtra(Const.BUNDLE_EXTRAS.FROM_SCREEN, Const.SCREENS.SIGNIN_SCREEN);
                startActivity(intentVerifyCode);
            } else {
                if (UserController.isLogin(getActivity())) {
                    if (user.getIsConsumer()) {
                        PlayServicesHelper playServicesHelper = new PlayServicesHelper(getActivity());
                        loadDataWhenLogin();
                        ((BaseActivity) getActivity()).mixpanelAPI.alias(user.getConsumer_id() + "", null);
                        getActivity().finish();
                    } else {
                        getActivity().finish();
                    }
                }
            }
        }
    }

    private void loadDataWhenLogin() {
        DealController.clearAllDeals(getActivity());
        MyLocation myLocation = MyLocationController.getLastLocation(getActivity());
        if (myLocation != null) {
            /*Intent intentGetDeal = new Intent(getActivity(), ParallaxService.class);
            intentGetDeal.setAction(ParallaxService.ACTION_GET_DEAL_LONG_TERM);
            intentGetDeal.putExtra(ParallaxService.EXTRA_LATITUDE, myLocation.getLatitude() + "");
            intentGetDeal.putExtra(ParallaxService.EXTRA_LONGITUDE, myLocation.getLongitude() + "");
            intentGetDeal.putExtra(ParallaxService.EXTRA_PAGE, "1");
            intentGetDeal.putExtra(RealTimeService.EXTRA_KEYWORD, "");
            intentGetDeal.putExtra(RealTimeService.EXTRA_CATEGORIES, "");
            intentGetDeal.putExtra(RealTimeService.EXTRA_TYPE_DEAL, "");
            getActivity().startService(intentGetDeal); */

            Intent intentGetDealShort = new Intent(getActivity(), RealTimeService.class);
            intentGetDealShort.setAction(RealTimeService.ACTION_GET_DEAL_SHORT_TERM);
            intentGetDealShort.putExtra(RealTimeService.EXTRA_LATITUDE, myLocation.getLatitude() + "");
            intentGetDealShort.putExtra(RealTimeService.EXTRA_LONGITUDE, myLocation.getLongitude() + "");
            intentGetDealShort.putExtra(RealTimeService.EXTRA_PAGE, "1");
            intentGetDealShort.putExtra(RealTimeService.EXTRA_KEYWORD, "");
            intentGetDealShort.putExtra(RealTimeService.EXTRA_CATEGORIES, "");
            intentGetDealShort.putExtra(RealTimeService.EXTRA_TYPE_DEAL, "");
            getActivity().startService(intentGetDealShort);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();

        exitMixPanel();
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_LOGIN)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    email_mixpanel = edtEmail.getText().toString().trim();
                    ((BaseActivity) getActivity()).hideProgressDialog();
                    processRoleUser();
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    ((BaseActivity) getActivity()).showToastError(message);
                    ((BaseActivity) getActivity()).hideProgressDialog();
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    ((BaseActivity) getActivity()).showToastError(getString(R.string.nointernet));
                    ((BaseActivity) getActivity()).hideProgressDialog();
                }
            } else if (intent.getAction().equalsIgnoreCase(SigninSignupActivity.SWITCH_TAB)) {
                SharedPreferences preferences = getActivity().getSharedPreferences("sign_in", Context.MODE_PRIVATE);
                edtEmail.setText(preferences.getString("email", ""));
            } else if (intent.getAction().equalsIgnoreCase(SigninSignupActivity.SIGN_IN_ACTION)) {
                login();
            }
        }
    };

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            SharedPreferences preferences = getActivity().getSharedPreferences("sign_in", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("email", charSequence.toString());
            editor.commit();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private void enterMixPanel() {
        JSONObject object = new JSONObject();
        try {
            object.put("time", StaticFunction.getCurrentTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ((BaseActivity) getActivity()).trackMixPanel(getString(R.string.Enter_SignIn), object);
        ((BaseActivity) getActivity()).startDurationMixPanel(getString(R.string.Duration_SignIn));
    }

    private void exitMixPanel() {
        JSONObject object = new JSONObject();
        try {
            object.put("time", StaticFunction.getCurrentTime());
            if (email_mixpanel != null) {
                object.put("email", email_mixpanel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ((BaseActivity) getActivity()).trackMixPanel(getString(R.string.Exit_SignIn), object);

        JSONObject objectDuration = null;
        try {
            if (email_mixpanel != null) {
                objectDuration = new JSONObject();
                object.put("email", email_mixpanel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ((BaseActivity) getActivity()).endDurationMixPanel(getString(R.string.Duration_SignIn), objectDuration);
    }
}
