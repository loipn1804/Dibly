package com.dibs.dibly.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.polites.android.GestureImageView;

public class PhotoZoomActivity extends BaseActivity {

    private GestureImageView imgV;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_zoom);

        overrideFontsLight(findViewById(R.id.root));

        String uri = getIntent().getExtras().getString("URL");
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.loading).imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(false).cacheOnDisk(true).bitmapConfig(Bitmap.Config.ARGB_8888).build();

        imgV = (GestureImageView) findViewById(R.id.imageV);
        imgV.setImageLevel(0);

        imageLoader.loadImage(uri, options, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String arg0, View arg1) {

            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {

                imgV.setImageBitmap(arg2);
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {

            }
        });

    }

}
