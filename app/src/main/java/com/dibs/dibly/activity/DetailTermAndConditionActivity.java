package com.dibs.dibly.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
public class DetailTermAndConditionActivity extends BaseActivity {

    private TextView txtTerm_1;
    private Long dealId = 0l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_term_and_condition);
        overrideFontsLight(findViewById(R.id.root));
        initialIntent();
        initView();
        initData();
    }

    private void initialIntent() {
        if (getIntent().getExtras() != null) {
            dealId = getIntent().getLongExtra("deal_id", 0l);
        }
    }

    private void initView() {
        initialToolBar();
        txtTerm_1 = (TextView) findViewById(R.id.txtTerm_1);
    }

    private void initData() {
        ObjectDeal objectDeal = DealController.getDealById(this, dealId);
        if (objectDeal != null) {
            txtTerm_1.setText(objectDeal.getTerms());
        }
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
}
