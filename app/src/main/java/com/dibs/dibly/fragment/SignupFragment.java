package com.dibs.dibly.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.activity.PrivacyActivity;
import com.dibs.dibly.activity.SigninSignupActivity;
import com.dibs.dibly.activity.TermActivity;
import com.dibs.dibly.activity.VerifyCodeActivity;
import com.dibs.dibly.adapter.PhoneCodeAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.consts.Const;
import com.dibs.dibly.daocontroller.PhoneCodeController;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.ClickSpan;
import com.dibs.dibly.staticfunction.StaticFunction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import greendao.PhoneCode;

/**
 * Created by USER on 10/30/2015.
 */
public class SignupFragment extends Fragment implements View.OnClickListener {

    private int SIGNUP_SUCCESS_REQUEST = 125;

    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPass;
    private EditText edtPhoneNumber;
    private TextView txtTerm, edtPhoneCode;
    private TextView txtSignup;

    private List<PhoneCode> lstPhoneCode;

    private String email_mixpanel = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        JSONObject object = new JSONObject();
        try {
            object.put("time", StaticFunction.getCurrentTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ((BaseActivity) getActivity()).trackMixPanel(getString(R.string.Enter_Signup), object);
        ((BaseActivity) getActivity()).startDurationMixPanel(getString(R.string.Duration_Signup));

        StaticFunction.overrideFontsLight(getActivity(), view);

        registerReceiver();

        initView(view);
        initData();
        loadDataPhoneCode();
        return view;
    }


    private void initView(View view) {

        edtEmail = (EditText) view.findViewById(R.id.edtEmail);
        edtPass = (EditText) view.findViewById(R.id.edtPass);
        edtName = (EditText) view.findViewById(R.id.edtName);
        edtPhoneCode = (TextView) view.findViewById(R.id.edtPhoneCode);
        edtPhoneNumber = (EditText) view.findViewById(R.id.edtPhoneNumber);
        txtTerm = (TextView) view.findViewById(R.id.txtTerm);
        txtSignup = (TextView) view.findViewById(R.id.txtSignup);
        txtSignup.setOnClickListener(this);
        edtPhoneCode.setOnClickListener(this);
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtSignup:
                signUp();
                break;
            case R.id.edtPhoneCode:
                showDialogPhoneCountryCode();
                break;
        }
    }

    private void signUp() {
        String name = edtName.getText().toString().trim();
        String phoneCode = edtPhoneCode.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();


        if (name.length() == 0) {
            ((BaseActivity) getActivity()).showToastError(getString(R.string.blank_name));
        } else if (email.length() == 0) {
            ((BaseActivity) getActivity()).showToastError(getString(R.string.blank_email));
        } else if (pass.length() < 6 || pass.length() > 20) {
            ((BaseActivity) getActivity()).showToastError(getString(R.string.invalidate_pass));
        } else if (phoneCode.length() == 0) {
            ((BaseActivity) getActivity()).showToastError(getString(R.string.blank_phonecode));
        } else if (phoneNumber.length() == 0) {
            ((BaseActivity) getActivity()).showToastError(getString(R.string.blank_phonenumber));
        } else {
            Intent intentSignUp = new Intent(getActivity(), RealTimeService.class);
            intentSignUp.setAction(RealTimeService.ACTION_SIGNUP);
            intentSignUp.putExtra(RealTimeService.EXTRA_USER_NAME, name);
            intentSignUp.putExtra(RealTimeService.EXTRA_USER_PHONECODE, phoneCode);
            intentSignUp.putExtra(RealTimeService.EXTRA_USER_PHONENUMBER, phoneNumber);
            intentSignUp.putExtra(RealTimeService.EXTRA_USER_EMAIL, email);
            intentSignUp.putExtra(RealTimeService.EXTRA_USER_PASS, pass);
            getActivity().startService(intentSignUp);
            ((BaseActivity) getActivity()).showProgressDialog();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SIGNUP_SUCCESS_REQUEST && resultCode == getActivity().RESULT_OK) {
            StaticFunction.sendBroad(getActivity(), SigninSignupActivity.SIGN_UP_SUCCESS);
        }
    }

    private void registerReceiver() {
        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_SIGNUP);
            intentFilter.addAction(RealTimeService.RECEIVER_GET_PHONE_CODE);
            intentFilter.addAction(SigninSignupActivity.SWITCH_TAB);
            intentFilter.addAction(SigninSignupActivity.SIGN_UP_ACTION);
            getActivity().registerReceiver(activityReceiver, intentFilter);
        }
    }

    private void unRegisterReceiver() {
        if (activityReceiver != null) {
            getActivity().unregisterReceiver(activityReceiver);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();

        JSONObject object = new JSONObject();
        try {
            object.put("time", StaticFunction.getCurrentTime());
            if (email_mixpanel != null) {
                object.put("email", email_mixpanel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ((BaseActivity) getActivity()).trackMixPanel(getString(R.string.Exit_Signup), object);

        JSONObject objectDuration = null;
        try {
            if (email_mixpanel != null) {
                objectDuration = new JSONObject();
                object.put("email", email_mixpanel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ((BaseActivity) getActivity()).endDurationMixPanel(getString(R.string.Duration_Signup), objectDuration);
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_SIGNUP)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    String email = edtEmail.getText().toString().trim();
                    String pass = edtPass.getText().toString().trim();
                    Intent intentSignUpSuccess = new Intent(getActivity(), VerifyCodeActivity.class);
                    intentSignUpSuccess.putExtra(RealTimeService.EXTRA_USER_EMAIL, email);
                    intentSignUpSuccess.putExtra(RealTimeService.EXTRA_USER_PASS, pass);
                    intentSignUpSuccess.putExtra(Const.BUNDLE_EXTRAS.FROM_SCREEN, Const.SCREENS.SIGNUP_SCREEN);
                    startActivityForResult(intentSignUpSuccess, SIGNUP_SUCCESS_REQUEST);
                    email_mixpanel = email;
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    ((BaseActivity) getActivity()).showToastError(message);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    ((BaseActivity) getActivity()).showToastError(getString(R.string.nointernet));
                }
                ((BaseActivity) getActivity()).hideProgressDialog();
            } else if (intent.getAction().equalsIgnoreCase(SigninSignupActivity.SWITCH_TAB)) {
                SharedPreferences preferences = getActivity().getSharedPreferences("sign_in", Context.MODE_PRIVATE);
                edtEmail.setText(preferences.getString("email", ""));
            } else if (intent.getAction().equalsIgnoreCase(SigninSignupActivity.SIGN_UP_ACTION)) {
                signUp();
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_PHONE_CODE)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    lstPhoneCode = PhoneCodeController.getAll(getActivity());
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    ((BaseActivity) getActivity()).showToastError(message);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    ((BaseActivity) getActivity()).showToastError(getString(R.string.nointernet));
                }
                ((BaseActivity) getActivity()).hideProgressDialog();
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

    private void loadDataPhoneCode() {

        if (lstPhoneCode == null)
            lstPhoneCode = PhoneCodeController.getAll(getActivity());

        if (lstPhoneCode == null || lstPhoneCode.size() == 0) {
            Intent intentGetPhoneCode = new Intent(getActivity(), RealTimeService.class);
            intentGetPhoneCode.setAction(RealTimeService.ACTION_GET_PHONE_CODE);
            getActivity().startService(intentGetPhoneCode);
            ((BaseActivity) getActivity()).showProgressDialog();
        }
    }

    private void showDialogPhoneCountryCode() {

        if (lstPhoneCode != null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Phone Codes");
            // Get the layout inflater
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.dialog_phonecode, null);
            builder.setView(view);

            ListView lstViewPhoneCode = (ListView) view.findViewById(R.id.lstViewPhoneCode);
            PhoneCodeAdapter adapter = new PhoneCodeAdapter(getActivity(), lstPhoneCode);
            lstViewPhoneCode.setAdapter(adapter);


            final AlertDialog dialog = builder.create();
            dialog.show();
            lstViewPhoneCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    edtPhoneCode.setText(lstPhoneCode.get(i).getDial_code());
                    dialog.dismiss();
                }
            });
        }
    }

}
