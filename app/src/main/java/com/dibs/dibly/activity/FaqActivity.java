package com.dibs.dibly.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.service.API;

/**
 * Created by USER on 8/3/2015.
 */
public class FaqActivity extends BaseActivity implements View.OnClickListener {


    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        overrideFontsLight(findViewById(R.id.root));

        initView();
        initData();
    }

    private void initView() {
        initialToolBar();
        webView = (WebView) findViewById(R.id.webView);
    }

    private void initData() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(API.URL + "page/faq.html");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
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
    }}
