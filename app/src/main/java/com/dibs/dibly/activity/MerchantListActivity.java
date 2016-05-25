package com.dibs.dibly.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.adapter.MerchantListAdapter;
import com.dibs.dibly.adapter.WheelSortAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.IndustrySearchController;
import com.dibs.dibly.daocontroller.MerchantController;
import com.dibs.dibly.daocontroller.MerchantListController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.interfaceUtils.OnPopUpFollowingListener;
import com.dibs.dibly.service.ParallaxService;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import greendao.IndustrySearch;
import greendao.Merchant;
import greendao.MerchantList;
import greendao.ObjectUser;
import kankan.wheel.widget.WheelView;

/**
 * Created by USER on 10/19/2015.
 */
public class MerchantListActivity extends BaseActivity implements View.OnClickListener {

    private int FILTER_REQUEST_CODE = 123;

    private RelativeLayout rltBack;
    private RelativeLayout rltSearch;
    private EditText edtSearch;
    private ListView lvMerchant;
    private List<MerchantList> listMerchant;
    private MerchantListAdapter merchantListAdapter;
    private View progessbarFooter;
    private LayoutInflater layoutInflater;
    private View headerView;
    private RelativeLayout rltEdtSearch;
    private RelativeLayout rltClear;
    private LinearLayout lnlSort;
    private TextView txtLabelSort, txtCover, txtNameActionBar;
    private TextView txtSort;
    private LinearLayout root, lnlCover;
    private ImageView btnMerchantList;
    private boolean isLoading;
    private int page;
    private int last_page;

    private int order;
    private int ASC = 0;
    private int DESC = 1;
    private String label_sort_asc = "Alphabetical Ascending";
    private String label_sort_desc = "Alphabetical Descending";
    public static List<String> listSort;
    public static List<String> listOrder;
    public Long merchantId;
    public boolean isFirst = true;
    private static final int MESSAGE_TEXT_CHANGED = 100;

    private JSONArray keyword = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_list);

        enterMixPanel();

        overrideFontsLight(findViewById(R.id.root));

        isLoading = false;
        page = 1;
        last_page = 0;
        order = ASC;
        listSort = new ArrayList<>();
        listOrder = new ArrayList<>();
        listSort.add("Alphabetical");
        listOrder.add("Ascending");
        listOrder.add("Descending");

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_MERCHANT_LIST);
            intentFilter.addAction(RealTimeService.RECEIVER_STOP_FOLLOWING_MERCHANT);
            intentFilter.addAction(RealTimeService.RECEIVER_FOLLOWING_MERCHANT);
            registerReceiver(activityReceiver, intentFilter);
        }

        initView();
        initData();
    }


    private void initView() {
        // initialToolBar();
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        rltSearch = (RelativeLayout) findViewById(R.id.rltSearch);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        lvMerchant = (ListView) findViewById(R.id.lvMerchant);
        rltEdtSearch = (RelativeLayout) findViewById(R.id.rltEdtSearch);
        rltClear = (RelativeLayout) findViewById(R.id.rltClear);
        lnlSort = (LinearLayout) findViewById(R.id.lnlSort);
        txtLabelSort = (TextView) findViewById(R.id.txtLabelSort);
        txtSort = (TextView) findViewById(R.id.txtSort);
        txtCover = (TextView) findViewById(R.id.txtCover);
        root = (LinearLayout) findViewById(R.id.root);
        lnlCover = (LinearLayout) findViewById(R.id.lnlCover);
        txtNameActionBar = (TextView) findViewById(R.id.txtNameActionBar);
        rltBack.setOnClickListener(this);
        rltSearch.setOnClickListener(this);
        rltClear.setOnClickListener(this);
        lnlSort.setOnClickListener(this);
        txtNameActionBar.setOnClickListener(this);


        lvMerchant.setOnScrollListener(new EndScrollListener());

        layoutInflater = LayoutInflater.from(this);
        progessbarFooter = layoutInflater.inflate(R.layout.view_progressbar_loadmore, null);
        headerView = layoutInflater.inflate(R.layout.view_invisible_header, null);

        lvMerchant.addHeaderView(headerView);
        rltEdtSearch.setVisibility(View.GONE);


        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        page = 1;
                        last_page = 0;
                        getData();
                        StaticFunction.hideKeyboard(MerchantListActivity.this);
                        showProgressDialog();
                        break;
                }
                return false;
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
                mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, s), 750);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });


        setLabelSort();
        overrideFontsBold(txtLabelSort);
    }

    private void initData() {
        listMerchant = new ArrayList<>();
        merchantListAdapter = new MerchantListAdapter(this, listMerchant);
        lvMerchant.setAdapter(merchantListAdapter);
        merchantListAdapter.setOnPopupFollowingListener(new OnPopUpFollowingListener() {
            @Override
            public void onActionStart(long merchantIdArg) {
                merchantId = merchantIdArg;
                showProgressDialog();
            }
        });
        getData();
        showProgressDialog();

    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            page = 1;
            last_page = 0;
            getData();
        }
    };

    private void notifyListData() {
        listMerchant = MerchantListController.getAll(this, order);
        merchantListAdapter.setData(listMerchant);

        if (listMerchant != null && listMerchant.size() > 0) {
            lnlCover.setVisibility(View.GONE);
            lvMerchant.setVisibility(View.VISIBLE);
        } else {
            txtCover.setText(edtSearch.getText().toString());
            lnlCover.setVisibility(View.VISIBLE);
            lvMerchant.setVisibility(View.GONE);
        }
    }

    private void setLabelSort() {
        if (order == ASC) {
            txtSort.setText(label_sort_asc);
        } else {
            txtSort.setText(label_sort_desc);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            unregisterReceiver(activityReceiver);
        }

        exitMixPanel();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (rltEdtSearch.getVisibility() == View.VISIBLE) {
            edtSearch.setText("");
            rltEdtSearch.setVisibility(View.GONE);
            page = 1;
            last_page = 0;
            getData();
            showProgressDialog();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == FILTER_REQUEST_CODE) {
            page = 1;
            last_page = 0;
            List<IndustrySearch> industrySearches = IndustrySearchController.getAll(this);
            if (industrySearches.size() > 0) {
                getData();
                showProgressDialog();
            } else {
                MerchantListController.clearAll(this);
                notifyListData();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rltBack:
                if (rltEdtSearch.getVisibility() == View.VISIBLE) {
                    edtSearch.setText("");
                    rltEdtSearch.setVisibility(View.GONE);
                    page = 1;
                    last_page = 0;
                    getData();
                    showProgressDialog();
                } else {
                    finish();
                }
                break;
            case R.id.rltSearch:
                rltEdtSearch.setVisibility(View.VISIBLE);
                edtSearch.requestFocus();
                StaticFunction.showKeyboard(MerchantListActivity.this);
                break;
            case R.id.txtNameActionBar:
                rltEdtSearch.setVisibility(View.VISIBLE);
                edtSearch.requestFocus();
                StaticFunction.showKeyboard(MerchantListActivity.this);
                break;
            case R.id.rltClear:
                edtSearch.setText("");
                break;
            case R.id.lnlSort:
                showPopupSortOld();
                break;
            case R.id.btnMerchantList:
                break;
        }
    }

    private void getData() {
        Intent intent = new Intent(MerchantListActivity.this, RealTimeService.class);
        intent.setAction(RealTimeService.ACTION_GET_MERCHANT_LIST);
        intent.putExtra(RealTimeService.EXTRA_MERCHANT_NAME_SEARCH, edtSearch.getText().toString().trim());
        intent.putExtra(ParallaxService.EXTRA_PAGE, page + "");
        intent.putExtra(ParallaxService.EXTRA_SORT_DIR, order == ASC ? "up" : "down");
        startService(intent);
        keyword.put(edtSearch.getText().toString().trim());
    }

    public void showPopupSortOld() {
        // custom dialog
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_sort);
        overrideFontsLight(dialog.findViewById(R.id.root));

        WheelSortAdapter timeAdapter1;
        WheelSortAdapter timeAdapter2;

        final WheelView wheel1 = (WheelView) dialog.findViewById(R.id.whl1);
        final WheelView wheel2 = (WheelView) dialog.findViewById(R.id.whl2);
        TextView txtLabelSort = (TextView) dialog.findViewById(R.id.txtLabelSort);
        TextView txtDone = (TextView) dialog.findViewById(R.id.txtDone);
        LinearLayout lnlSpace = (LinearLayout) dialog.findViewById(R.id.lnlSpace);

        overrideFontsBold(txtLabelSort);

        timeAdapter1 = new WheelSortAdapter(this, listSort);
        wheel1.setVisibleItems(3); // Number of items
        wheel1.setWheelBackground(R.drawable.wheel_bg);
        wheel1.setWheelForeground(R.drawable.wheel_fg);
        wheel1.setShadowColor(0xFFffffff, 0x99ffffff, 0x33ffffff);
        wheel1.setViewAdapter(timeAdapter1);
        wheel1.setCurrentItem(0);

        timeAdapter2 = new WheelSortAdapter(this, listOrder);
        wheel2.setVisibleItems(3); // Number of items
        wheel2.setWheelBackground(R.drawable.wheel_bg);
        wheel2.setWheelForeground(R.drawable.wheel_fg);
        wheel2.setShadowColor(0xFFffffff, 0x99ffffff, 0x33ffffff);
        wheel2.setViewAdapter(timeAdapter2);
        wheel2.setCurrentItem(order);

        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order = wheel2.getCurrentItem();
                page = 1;
                last_page = 0;
                getData();
                showProgressDialog();
                setLabelSort();
                dialog.dismiss();
            }
        });

        lnlSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showPopupSortV10() {
        // custom dialog
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_sort_merchant_v10);
        overrideFontsLight(dialog.findViewById(R.id.root));

        TextView txtLabelSort = (TextView) dialog.findViewById(R.id.txtLabelSort);
        TextView txtDone = (TextView) dialog.findViewById(R.id.txtDone);
        final TextView txtAsc = (TextView) dialog.findViewById(R.id.txtAsc);
        final TextView txtDesc = (TextView) dialog.findViewById(R.id.txtDesc);
        LinearLayout lnlSpace = (LinearLayout) dialog.findViewById(R.id.lnlSpace);

        overrideFontsBold(txtLabelSort);

        if (order == ASC) {
            txtAsc.setBackgroundColor(getResources().getColor(R.color.gray_bg_call_dibs));
            txtAsc.setTextColor(getResources().getColor(R.color.orange_main));
            txtDesc.setBackgroundColor(getResources().getColor(R.color.white));
            txtDesc.setTextColor(getResources().getColor(R.color.black));
        } else {
            txtDesc.setBackgroundColor(getResources().getColor(R.color.gray_bg_call_dibs));
            txtDesc.setTextColor(getResources().getColor(R.color.orange_main));
            txtAsc.setBackgroundColor(getResources().getColor(R.color.white));
            txtAsc.setTextColor(getResources().getColor(R.color.black));
        }

        lnlSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                last_page = 0;
                getData();
                showProgressDialog();
                setLabelSort();
                dialog.dismiss();
            }
        });

        txtAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order = 0;
                txtAsc.setBackgroundColor(getResources().getColor(R.color.gray_bg_call_dibs));
                txtAsc.setTextColor(getResources().getColor(R.color.orange_main));
                txtDesc.setBackgroundColor(getResources().getColor(R.color.white));
                txtDesc.setTextColor(getResources().getColor(R.color.black));
            }
        });

        txtDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order = 1;
                txtDesc.setBackgroundColor(getResources().getColor(R.color.gray_bg_call_dibs));
                txtDesc.setTextColor(getResources().getColor(R.color.orange_main));
                txtAsc.setBackgroundColor(getResources().getColor(R.color.white));
                txtAsc.setTextColor(getResources().getColor(R.color.black));
            }
        });

        dialog.show();
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_MERCHANT_LIST)) {


                if (result.equals(RealTimeService.RESULT_OK)) {
                    String lastpage = intent.getStringExtra(RealTimeService.EXTRA_RESULT_LAST_PAGE);
                    last_page = Integer.parseInt(lastpage);
                    String currentpage = intent.getStringExtra(RealTimeService.EXTRA_RESULT_CURRENT_PAGE);
                    page = Integer.parseInt(currentpage);
                    notifyListData();
                    isLoading = false;
                    if (page == 1) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (lvMerchant.getFirstVisiblePosition() == 0 && lvMerchant.getLastVisiblePosition() == lvMerchant.getCount() - 1) {
                                    isLoading = true;
                                    if (page < last_page) {
                                        page++;
                                        getData();
                                        lvMerchant.addFooterView(progessbarFooter);
                                    }
                                }
//                                showToastInfo(lvMerchant.getFirstVisiblePosition() + " " + lvMerchant.getLastVisiblePosition() + " " + lvMerchant.getCount());
                            }
                        }, 500);
                    }
                }
                lvMerchant.removeFooterView(progessbarFooter);
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_STOP_FOLLOWING_MERCHANT)) {
                if (result.equals(RealTimeService.RESULT_OK)) {

                    if (merchantId != null) {
                        Merchant merchant = MerchantController.getById(context, merchantId);
                        if (merchant != null) {
                            merchant.setF_follow(false);
                            MerchantController.update(context, merchant);
                        }

                        DealController.updateObjectIdFliked(MerchantListActivity.this, merchantId, false);

                        MerchantListController.updateFFollowing(context, merchantId, false);

                        notifyListData();
                        String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                        showToastOk(message);
                        merchantId = null;
                    }
                }
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_FOLLOWING_MERCHANT)) {
                if (result.equals(RealTimeService.RESULT_OK)) {
                    if (merchantId != null) {
                        Merchant merchant = MerchantController.getById(context, merchantId);
                        if (merchant != null) {
                            merchant.setF_follow(true);
                            MerchantController.update(context, merchant);
                        }

                        DealController.updateObjectIdFliked(MerchantListActivity.this, merchantId, true);

                        MerchantListController.updateFFollowing(context, merchantId, true);

                        notifyListData();
                        String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                        showToastOk(message);
                        merchantId = null;
                    }
                }
            }

            hideCustomProgressDialog();

            if (result.equals(RealTimeService.RESULT_FAIL)) {
                String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                showToastError(message);
            } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                showToastError(getString(R.string.nointernet));
            }
        }
    };

    private class EndScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount >= visibleItemCount + 2) {
                if (firstVisibleItem + 3 >= totalItemCount - visibleItemCount + 2) {
                    if (!isLoading) {
                        isLoading = true;
                        if (page < last_page) {
                            page++;
                            getData();
                            lvMerchant.addFooterView(progessbarFooter);
                        }
                    }
                }
            }
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
    }

    private void enterMixPanel() {
        ObjectUser user = UserController.getCurrentUser(this);
        JSONObject object = new JSONObject();
        try {
            object.put("time", StaticFunction.getCurrentTime());
            object.put("email", user.getEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        trackMixPanel(getString(R.string.Enter_Search_Merchant), object);
        startDurationMixPanel(getString(R.string.Duration_Search_Merchant));
    }

    private void exitMixPanel() {
        ObjectUser user = UserController.getCurrentUser(this);
        JSONObject object = new JSONObject();
        try {
            object.put("time", StaticFunction.getCurrentTime());
            object.put("email", user.getEmail());
            object.put("keywords", keyword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        trackMixPanel(getString(R.string.Exit_Search_Merchant), object);
        endDurationMixPanel(getString(R.string.Duration_Search_Merchant), object);
    }
}
