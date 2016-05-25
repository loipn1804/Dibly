package com.dibs.dibly.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dibs.dibly.R;
import com.dibs.dibly.adapter.DiscoveryAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.DiscoveryController;
import com.dibs.dibly.service.RealTimeService;

import java.util.ArrayList;
import java.util.List;

import greendao.Discovery;

/**
 * Created by USER on 6/29/2015.
 */
public class DiscoveryActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private ImageView rltSearch;

    private ListView lvDiscovery;
    private DiscoveryAdapter discoveryAdapter;

    private List<Discovery> listDiscovery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);

        overrideFontsLight(findViewById(R.id.root));

        initView();

        registerBroadcastReceiver();

        initData();
    }

    private void initView() {
        initialToolBar();
        lvDiscovery = (ListView) findViewById(R.id.lvDiscovery);
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        rltSearch = (ImageView) findViewById(R.id.rltSearch);

        rltBack.setOnClickListener(this);
        rltSearch.setOnClickListener(this);
    }

    private void initData() {
        listDiscovery = new ArrayList<>();
        discoveryAdapter = new DiscoveryAdapter(DiscoveryActivity.this, listDiscovery);
        lvDiscovery.setAdapter(discoveryAdapter);

        showCustomProgressDialog();

        callServiceToLoadDiscovery();
    }

    private void callServiceToLoadDiscovery() {
        //load active voucher update
        Intent intent = new Intent(RealTimeService.ACTION_GET_DISCOVERY_TITLE, null, DiscoveryActivity.this, RealTimeService.class);
        startService(intent);
    }

    private void registerBroadcastReceiver() {
        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_DISCOVERY_TITLE);
            // this is for online offline status
            //intentFilter.addAction(NetworkChangeReceiver.ON_NETWORK_CHANGE);
            registerReceiver(activityReceiver, intentFilter);
        }
    }

    private void unRegisterBroadcastReceiver() {
        if (activityReceiver != null) {
            unregisterReceiver(activityReceiver);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.rltSearch:
                Intent intentSearch = new Intent(DiscoveryActivity.this, SearchActivity.class);
                startActivity(intentSearch);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBroadcastReceiver();
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_DISCOVERY_TITLE)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equalsIgnoreCase(RealTimeService.RESULT_OK)) {
                    listDiscovery = DiscoveryController.getAll(DiscoveryActivity.this);
                    discoveryAdapter.setDataSource(listDiscovery);
                    discoveryAdapter.notifyDataChange();
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                } else if (result.equalsIgnoreCase(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
                hideCustomProgressDialog();
            }
        }
    };

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
}
