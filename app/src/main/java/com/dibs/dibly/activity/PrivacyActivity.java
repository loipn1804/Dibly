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
 * Created by USER on 8/10/2015.
 */
public class PrivacyActivity extends BaseActivity {


    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

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
        webView.loadUrl(API.URL + "page/privacy.html");
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
