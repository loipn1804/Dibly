package com.dibs.dibly.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.adapter.GuideAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.fragment.GuideFragment;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by USER on 6/29/2015.
 */
public class GuideActivity extends BaseActivity implements GuideFragment.SwipePagerCbk {

    private ViewPager viewPager;
    private GuideAdapter guideAdapter;
    private CirclePageIndicator indicator;
    private RelativeLayout rltGerStarted;
    private TextView txtGetStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        JSONObject object = new JSONObject();
        try {
            object.put("time", StaticFunction.getCurrentTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        trackMixPanel(getString(R.string.Enter_Introduction), object);
        startDurationMixPanel(getString(R.string.Duration_Introduction));

        overrideFontsLight(findViewById(R.id.root));

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JSONObject object = new JSONObject();
        try {
            object.put("time", StaticFunction.getCurrentTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        trackMixPanel(getString(R.string.Exit_Introduction), object);
        endDurationMixPanel(getString(R.string.Duration_Introduction), null);
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        rltGerStarted = (RelativeLayout) findViewById(R.id.rltGerStarted);
        txtGetStart = (TextView) findViewById(R.id.txtGetStart);

//        indicator.setSelectedColor(getResources().getColor(R.color.black));
//        indicator.setUnselectedColor(getResources().getColor(R.color.white));
//        indicator.setLineWidth(StaticFunction.getScreenWidth(GuideActivity.this) / 7);
    }

    private void initData() {
        guideAdapter = new GuideAdapter(getSupportFragmentManager(), GuideActivity.this, this);
        viewPager.setAdapter(guideAdapter);
        viewPager.setOffscreenPageLimit(5);

        indicator.setViewPager(viewPager);
        viewPager.setCurrentItem(0);

        //  viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if (position < 2) {
//                    indicator.setSelectedColor(getResources().getColor(R.color.black));
//                    indicator.setUnselectedColor(getResources().getColor(R.color.white));
//                } else {
//                    indicator.setSelectedColor(getResources().getColor(R.color.orance_main));
//                    indicator.setUnselectedColor(getResources().getColor(R.color.black));
//                }
//                indicator.setCurrentItem(position);
//                if (position == 4) {
//                    txtGetStart.setText("Get Started!");
//                } else {
//                    txtGetStart.setText("Next >>");
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

        rltGerStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int current = viewPager.getCurrentItem();
//                if (current == 4) {
                finish();
//                } else {
//                    viewPager.setCurrentItem(current + 1, true);
//                }
            }
        });
    }

    @Override
    public void swipe() {
        //viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
    }
}
