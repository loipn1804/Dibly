package com.dibs.dibly.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;

/**
 * Created by USER on 7/21/2015.
 */
public class ClaimSuccessThankActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltBack;
    private Button btnMoreMerchant;
    private Button btnHome;

    private long id;
    private String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_success_thankyou);

        overrideFontsLight(findViewById(R.id.root));

        if (getIntent().getExtras() != null) {
            id = getIntent().getLongExtra("id", 0);
            name = getIntent().getStringExtra("name");
        } else {
            finish();
        }

        initView();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        btnMoreMerchant = (Button) findViewById(R.id.btnMoreMerchant);
        btnHome = (Button) findViewById(R.id.btnHome);

        rltBack.setOnClickListener(this);
        btnMoreMerchant.setOnClickListener(this);
        btnHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
//                Intent intentHome = new Intent(ClaimSuccessThankActivity.this, HomeActivity.class);
//                intentHome.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                startActivity(intentHome);
                finish();
                break;
            case R.id.btnMoreMerchant:
                Intent intentMerchant = new Intent(ClaimSuccessThankActivity.this, MerchantDealActivity.class);
                intentMerchant.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intentMerchant.putExtra("id", id);
                intentMerchant.putExtra("name", name);
                startActivity(intentMerchant);
                finish();
                break;
            case R.id.btnHome:
                Intent intentHome_2 = new Intent(ClaimSuccessThankActivity.this, HomeActivity.class);
                intentHome_2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome_2);
                finish();
                break;
        }
    }
}
