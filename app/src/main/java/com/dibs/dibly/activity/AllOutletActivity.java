package com.dibs.dibly.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dibs.dibly.R;
import com.dibs.dibly.adapter.AllOutletAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.OutletController;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;

import greendao.Outlet;

/**
 * Created by USER on 6/29/2015.
 */
public class AllOutletActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    List<Outlet> listOutlet = new ArrayList<>();
    ListView listViewAllOutlet;
    GoogleMap googleMap;
    protected GoogleApiClient mGoogleApiClient;
    AllOutletAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_outlet);

        overrideFontsLight(findViewById(R.id.root));

        initView();
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        //float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        adapter = new AllOutletAdapter(this, listOutlet, (int) dpWidth);
        listViewAllOutlet.setAdapter(adapter);

        if (getIntent().hasExtra("merchant_id")) {
            String merchantID = getIntent().getStringExtra("merchant_id");
            if (!merchantID.equals("")) {
                loadAllOutletInBackgroud(merchantID);
            }
        }
    }

    private void initView() {
        listViewAllOutlet = (ListView) findViewById(R.id.listAllOutlet);
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);

        rltBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
        }
    }

    private void loadAllOutletInBackgroud(String merchantID) {
        List<Outlet> listOutlet = OutletController.getOutletsByMerchantId(AllOutletActivity.this, merchantID);
        if (adapter != null) {
            adapter.setDataSource(listOutlet);
        }

    }
}