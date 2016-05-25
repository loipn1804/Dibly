package com.dibs.dibly.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.service.ParallaxService;
import com.dibs.dibly.service.RealTimeService;

import greendao.ObjectDeal;

/**
 * Created by USER on 7/1/2015.
 */
public class DetailTermAndConditionFragment extends Fragment {

    private TextView txtTerm_1;
    private Long dealId = 0l;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_term_and_condition, container, false);

        ((BaseActivity) getActivity()).overrideFontsLight(view);

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_DEAL_DETAIL);
            getActivity().registerReceiver(activityReceiver, intentFilter);
        }

        if (getActivity().getIntent().getExtras() != null) {
            dealId = getActivity().getIntent().getLongExtra("deal_id", 0l);
        }

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        txtTerm_1 = (TextView) view.findViewById(R.id.txtTerm_1);
    }

    private void initData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            getActivity().unregisterReceiver(activityReceiver);
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_DEAL_DETAIL)) {
                String result = intent.getStringExtra(ParallaxService.EXTRA_RESULT);
                if (result.equals(ParallaxService.RESULT_OK)) {
                    ObjectDeal objectDeal = DealController.getDealById(getActivity(), dealId);
                    if (objectDeal != null) {
                        txtTerm_1.setText(objectDeal.getTerms());
                    }
                } else if (result.equals(ParallaxService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(ParallaxService.EXTRA_RESULT_MESSAGE);
                    ((BaseActivity) getActivity()).showToastError(message);
                } else if (result.equals(ParallaxService.RESULT_NO_INTERNET)) {
                    ((BaseActivity) getActivity()).showToastError(getString(R.string.nointernet));
                }
            }
        }
    };
}
