package com.dibs.dibly.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.view.FlakeView;

/**
 * Created by USER on 8/24/2015.
 */
public class FlashActivity extends BaseActivity
{

    FlakeView flakeView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        overrideFontsLight(findViewById(R.id.root));

        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        flakeView = new FlakeView(this);
        container.addView(flakeView);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intentPre = new Intent(FlashActivity.this, PreLoginActivity.class);
                startActivity(intentPre);
                finish();
            }
        }, 1000);
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        flakeView.pause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        flakeView.resume();
    }

}
