package com.dibs.dibly.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dibs.dibly.R;
import com.dibs.dibly.adapter.AllOutletAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.OutletController;
import com.dibs.dibly.service.ParallaxService;
import com.dibs.dibly.service.RealTimeService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;

import greendao.Outlet;

/**
 * Created by USER on 11/30/2015.
 */
public class AllOutletFragment extends Fragment implements View.OnClickListener {

    List<Outlet> listOutlet = new ArrayList<>();
    ListView listViewAllOutlet;
    GoogleMap googleMap;
    protected GoogleApiClient mGoogleApiClient;
    AllOutletAdapter adapter;

    private Long merchantId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_all_outlet, container, false);

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_MERCHANT);
            getActivity().registerReceiver(activityReceiver, intentFilter);
        }

        ((BaseActivity) getActivity()).overrideFontsLight(view);

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        listViewAllOutlet = (ListView) view.findViewById(R.id.listAllOutlet);

        view.findViewById(R.id.rltActionBar).setVisibility(View.GONE);
    }

    private void initData() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        //float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        adapter = new AllOutletAdapter(getActivity(), listOutlet, (int) dpWidth);
        listViewAllOutlet.setAdapter(adapter);

        if (getActivity().getIntent().hasExtra("merchant_id")) {
            merchantId = getActivity().getIntent().getLongExtra("merchant_id", 0l);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            getActivity().unregisterReceiver(activityReceiver);
        }
    }

    private void loadAllOutletInBackgroud() {
        List<Outlet> listOutlet = OutletController.getOutletsByMerchantId(getActivity(), merchantId + "");
        if (adapter != null) {
            adapter.setDataSource(listOutlet);
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_MERCHANT)) {
                String result = intent.getStringExtra(ParallaxService.EXTRA_RESULT);
                if (result.equals(ParallaxService.RESULT_OK)) {
                    loadAllOutletInBackgroud();
                } else if (result.equals(ParallaxService.RESULT_FAIL)) {

                }
            }
        }
    };
}
