package com.dibs.dibly.activity;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;


/**
 * Created by USER on 7/13/2015.
 */
public class MerchantScannerActivity extends BaseActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private RelativeLayout rltBack;
    private QRCodeReaderView mydecoderview;
    Button btnScanNow;
    String scanData = "";
    boolean isStartScan = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_scanner);

        overrideFontsLight(findViewById(R.id.root));

        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        rltBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnScanNow = (Button) findViewById(R.id.btnScannow);
        btnScanNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanData = "";
                isStartScan = true;

            }
        });

        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(MerchantScannerActivity.this);
    }

    @Override
    public void onQRCodeRead(String s, PointF[] pointFs) {
        if (isStartScan == true) {
            scanData = s;
            if (!scanData.equalsIgnoreCase("")) {
                isStartScan = false;
                Intent intent = new Intent(MerchantScannerActivity.this, MerchantScannerSuccessActivity.class);
                intent.putExtra("data", scanData);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void cameraNotFound() {

    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mydecoderview.getCameraManager().startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mydecoderview.getCameraManager().stopPreview();
    }
}
