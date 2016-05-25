package com.dibs.dibly.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.adapter.ShapeAdapter;
import com.dibs.dibly.daocontroller.MerchantController;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.ItemClickSupport;
import org.lucasr.twowayview.widget.TwoWayView;

import java.util.ArrayList;
import java.util.List;

import greendao.Merchant;

/**
 * Created by USER on 10/12/2015.
 */
public class MerchantPictureActivity extends Activity implements View.OnClickListener {

    private TwoWayView twoWayView;
    private ImageView imvPicture;
    private TextView txtImageCount;
    private LinearLayout root;
    private ProgressBar progressBar;

    private long merchant_id;

    private List<String> thumbs;
    private List<String> images;
    private ShapeAdapter shapeAdapter;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_picture);

        options = new DisplayImageOptions.Builder().cacheInMemory(true).showImageForEmptyUri(R.color.white).showImageOnLoading(R.color.transparent).cacheOnDisk(true).build();

        if (getIntent().hasExtra("merchant_id")) {
            merchant_id = getIntent().getLongExtra("merchant_id", 0);
        } else {
            finish();
        }

        initView();
        initData();
    }

    public void initView() {
        twoWayView = (TwoWayView) findViewById(R.id.twoWayView);
        imvPicture = (ImageView) findViewById(R.id.imvPicture);
        txtImageCount = (TextView) findViewById(R.id.txtImageCount);
        txtImageCount.setTypeface(StaticFunction.light(this));
        root = (LinearLayout) findViewById(R.id.root);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        twoWayView.setHasFixedSize(true);
        progressBar.setVisibility(View.GONE);
    }

    public void initData() {
        thumbs = new ArrayList<>();
        images = new ArrayList<>();
        Merchant merchant = MerchantController.getById(this, merchant_id);
        if (merchant != null) {
//            thumbs.add(merchant.getLogo_image());
//            images.add(merchant.getLogo_image());
            try {
                JSONArray array_gallery_images = new JSONArray(merchant.getProfile_images());
                for (int i = 0; i < array_gallery_images.length(); i++) {
                    JSONObject obj_gallery = array_gallery_images.getJSONObject(i);
                    thumbs.add(obj_gallery.getString("image_thumbnail_url"));
                    images.add(obj_gallery.getString("image_url"));
                }
            } catch (JSONException e) {

            }

            shapeAdapter = new ShapeAdapter(MerchantPictureActivity.this, twoWayView, thumbs);

            imageLoader.displayImage(images.get(0), imvPicture, options);
            imageLoader.setDefaultLoadingListener(new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    progressBar.setVisibility(View.GONE);
                }
            });
            txtImageCount.setText("Image 1 of " + images.size());

            ItemClickSupport itemClick = ItemClickSupport.addTo(twoWayView);
            itemClick.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView parent, View child, int position, long id) {
                    imageLoader.displayImage(images.get(position), imvPicture, options);
                    txtImageCount.setText("Image " + (position + 1) + " of " + images.size());
                }
            });

            root.setOnClickListener(this);
            twoWayView.setOnClickListener(this);
            imvPicture.setOnClickListener(this);
            txtImageCount.setOnClickListener(this);

            twoWayView.setAdapter(shapeAdapter);
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.root:
                finish();
                break;
            case R.id.twoWayView:

                break;
            case R.id.imvPicture:

                break;
            case R.id.txtImageCount:

                break;
        }
    }
}
