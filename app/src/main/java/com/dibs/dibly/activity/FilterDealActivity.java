package com.dibs.dibly.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.FilterDealController;
import com.dibs.dibly.daocontroller.TypeSearchController;
import com.dibs.dibly.service.RealTimeService;

import java.util.ArrayList;
import java.util.List;

import greendao.DealFilter;
import greendao.TypeSearch;

/**
 * Created by USER on 6/29/2015.
 */
public class FilterDealActivity extends BaseActivity {

    RelativeLayout btnSaveFilter;
    Button btnCancel;

    LinearLayout btnInApp;
    LinearLayout btnInStore;

    private DealFilter filter;
    SeekBar seekBar;

    private Dialog dialog = null;

    TextView txtIndustryType;
    TextView txtKeyword;
    TextView txtCurrentDistance;
    private LinearLayout lnlType;
    private TextView txtLabelType;

    boolean inApp = false;
    boolean inStore = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        overrideFontsLight(findViewById(R.id.root));

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_TYPE_SEARCH);
            registerReceiver(activityReceiver, intentFilter);
        }

        Intent intentGetTypeSearch = new Intent(FilterDealActivity.this, RealTimeService.class);
        intentGetTypeSearch.setAction(RealTimeService.ACTION_GET_TYPE_SEARCH);
        startService(intentGetTypeSearch);
        showProgressDialog();

        filter = new DealFilter();
        filter.setId(FilterDealController.ID);
        filter.setDistance(5000l);

        initView();
    }

    private void initView() {
        btnSaveFilter = (RelativeLayout) findViewById(R.id.btnSaveFilter);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnInApp = (LinearLayout) findViewById(R.id.btnInApp);
        btnInStore = (LinearLayout) findViewById(R.id.btnInStore);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        txtIndustryType = (TextView) findViewById(R.id.txtIndustryType);
        txtKeyword = (TextView) findViewById(R.id.txtKeyword);
        txtCurrentDistance = (TextView) findViewById(R.id.txtCurrentDistance);
        lnlType = (LinearLayout) findViewById(R.id.lnlType);
        txtLabelType = (TextView) findViewById(R.id.txtLabelType);

        overrideFontsBold(txtLabelType);

        lnlType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TypeSearch> list = TypeSearchController.getAll(FilterDealActivity.this);
                List<String> listData = new ArrayList<String>();
                listData.add("All");
                if (list.size() > 0) {
                    for (TypeSearch typeSearch : list) {
                        listData.add(typeSearch.getName());
                    }

                    showSelectIndustryDialog(FilterDealActivity.this, listData);
                }
            }
        });

        txtIndustryType.setText("");
        filter.setIndustry_type("");

        seekBar.setMax(4800);
        seekBar.setProgress(4800);
        txtCurrentDistance.setText( 000 + "m");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                filter.setDistance((long) (progress + 200));
                txtCurrentDistance.setText((progress + 200) + "m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                showToastInfo(((long) (seekBar.getProgress() + 200)) + "m");
            }
        });

        btnSaveFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = txtKeyword.getText().toString().trim();

                if (keyword != null && keyword.length() > 0) {
                    filter.setKeyword(keyword);
                } else {
                    filter.setKeyword("");
                }

                if (inApp == true && inStore == true) {
                    filter.setPurchase_type(FilterDealController.PURCHASE_TYPE_BOTH);
                } else {
                    if (inApp == true) {
                        filter.setPurchase_type(FilterDealController.PURCHASE_TYPE_INAPP);
                    }

                    if (inStore == true) {
                        filter.setPurchase_type(FilterDealController.PURCHASE_TYPE_INSTORE);
                    }

                    if (inApp == false && inStore == false) {
                        filter.setPurchase_type(FilterDealController.PURCHASE_TYPE_BOTH);
                    }
                }

                FilterDealController.insert(FilterDealActivity.this, filter);

                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED, returnIntent);
                finish();
            }
        });

        btnInApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inApp == false) {
                    inApp = true;
                    enableInApp();

                } else {
                    inApp = false;
                    disableInApp();
                }
            }
        });

        btnInStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inStore == false) {
                    inStore = true;
                    enableInStore();
                } else {
                    inStore = false;
                    disableInStore();
                }

            }
        });

        inApp = false;
        disableInApp();
        inStore = true;
        enableInStore();
    }

    private void enableInApp() {
        btnInApp.setBackgroundColor(Color.parseColor("#4EC2F6"));

    }

    private void disableInApp() {
        btnInApp.setBackgroundColor(Color.parseColor("#f2f2f2"));
    }

    private void enableInStore() {

        btnInStore.setBackgroundColor(Color.parseColor("#4EC2F6"));
    }

    private void disableInStore() {
        btnInStore.setBackgroundColor(Color.parseColor("#f2f2f2"));
    }

    public void showSelectIndustryDialog(final Activity activity, final List<String> listData) {
        // custom dialog
        dialog = new Dialog(FilterDealActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.popup_with_list);
        overrideFontsLight(dialog.findViewById(R.id.root));

        ListView listView = (ListView) dialog.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.row_list_item, listData);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(activity, listData.get(position), Toast.LENGTH_LONG).show();
                if (listData.get(position).equalsIgnoreCase("All")) {
                    filter.setIndustry_type("");
                } else {
                    filter.setIndustry_type(listData.get(position));
                }
                txtIndustryType.setText(listData.get(position));
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            this.unregisterReceiver(activityReceiver);
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_TYPE_SEARCH)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
//                    List<TypeSearch> list = TypeSearchController.getAll(FilterDealActivity.this);
//                    if (list.size() > 0) {
                    txtIndustryType.setText("All");
                    filter.setIndustry_type("");
//                    }

                    hideProgressDialog();
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                    hideProgressDialog();
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                    hideProgressDialog();
                }
            }
        }
    };
}
