package com.dibs.dibly.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.adapter.FilterDealAdapter;
import com.dibs.dibly.adapter.FilterTypeDealAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.consts.Const;
import com.dibs.dibly.consts.GeneralMethod;
import com.dibs.dibly.daocontroller.CategoryController;
import com.dibs.dibly.daocontroller.TypeDealController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.dibs.dibly.view.NestedListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import greendao.Category;
import greendao.ObjectUser;
import greendao.TypeDeal;

/**
 * Created by VuPhan on 4/12/16.
 */
public class FilterActivity extends BaseActivity implements OnClickListener {

    private NestedListView lstViewCategory, lstViewTypeOfDeal;
    private EditText edtKeyword;
    private TextView btnReset;
    private Button btnFilter;

    private List<Category> lstCategory;
    private List<TypeDeal> lstTypeDeal;
    private FilterTypeDealAdapter filterTypeDealAdapter;
    private FilterDealAdapter adapter;

    private String categoriesFiltered = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filterv2);

        ObjectUser user = UserController.getCurrentUser(FilterActivity.this);
        JSONObject object = new JSONObject();
        try {
            object.put("time", StaticFunction.getCurrentTime());
            object.put("email", user.getEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        trackMixPanel(getString(R.string.Enter_Filter), object);
        startDurationMixPanel(getString(R.string.Duration_Filter));

        initialView();
        registerReceiver();
        loadData();
    }


    private void initialView() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        initialToolBar();
        overrideFontsLight(findViewById(R.id.root));
        lstViewCategory = (NestedListView) findViewById(R.id.lstViewCategory);
        lstViewTypeOfDeal = (NestedListView) findViewById(R.id.lstViewTypeOfDeal);
        edtKeyword = (EditText) findViewById(R.id.edtKeyword);
        btnReset = (TextView) findViewById(R.id.btnReset);
        btnFilter = (Button) findViewById(R.id.btnFilter);
        btnReset.setOnClickListener(this);
        btnFilter.setOnClickListener(this);
    }

    private void loadData() {

        lstCategory = CategoryController.getAll(this);


        if (lstCategory == null || lstCategory.size() == 0 || GeneralMethod.checkTimeToSync(this)) {
            Intent intentSignUp = new Intent(this, RealTimeService.class);
            intentSignUp.setAction(RealTimeService.ACTION_GET_FILTER_DEAL);
            startService(intentSignUp);
            showProgressDialog();
        } else {
            setupAdapter();
        }
    }

    private void registerReceiver() {
        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_FILTER_DEAL);
            registerReceiver(activityReceiver, intentFilter);
        }
    }

    private void unRegisterReceiver() {
        if (activityReceiver != null) {
            unregisterReceiver(activityReceiver);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();

        ObjectUser user = UserController.getCurrentUser(this);
        JSONObject object = new JSONObject();
        try {
            object.put("time", StaticFunction.getCurrentTime());
            object.put("email", user.getEmail());
            if (categoriesFiltered != null) {
                object.put("selected categories", categoriesFiltered);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        trackMixPanel(getString(R.string.Exit_Filter), object);

        JSONObject objectDuration = new JSONObject();
        try {
            objectDuration.put("email", user.getEmail());
            if (categoriesFiltered != null) {
                object.put("selected categories", categoriesFiltered);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        endDurationMixPanel(getString(R.string.Duration_Filter), objectDuration);
    }

    private void setupAdapter() {
        lstCategory = CategoryController.getAll(this);
        lstTypeDeal = TypeDealController.getAll(this);

        SharedPreferences prefs = getSharedPreferences("trackData", 0);
        String categories[] = prefs.getString("categories", "").split(",");
        String typesDeal[] = prefs.getString("typeDealIds", "").split(",");

        for (String caterogyId : categories) {
            if (caterogyId != null && caterogyId.trim().length() > 0)
                for (Category category : lstCategory) {
                    if (category.getId() == Integer.parseInt(caterogyId)) {
                        category.setIsSelect(true);
                        break;
                    }
                }
        }

        for (String typeDealId : typesDeal) {
            if (typeDealId != null && typeDealId.trim().length() > 0)
                for (TypeDeal typeDeal : lstTypeDeal) {
                    if (typeDeal.getId() == Integer.parseInt(typeDealId)) {
                        typeDeal.setIsSelect(true);
                        break;
                    }
                }
        }

        adapter = new FilterDealAdapter(this, lstCategory);
        lstViewCategory.setAdapter(adapter);
        lstViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lstCategory.get(i).setIsSelect(!lstCategory.get(i).getIsSelect());
                adapter.setDataSource(lstCategory);
            }
        });


        filterTypeDealAdapter = new FilterTypeDealAdapter(this, lstTypeDeal);
        lstViewTypeOfDeal.setAdapter(filterTypeDealAdapter);
        lstViewTypeOfDeal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lstTypeDeal.get(i).setIsSelect(!lstTypeDeal.get(i).getIsSelect());
                filterTypeDealAdapter.setDataSource(lstTypeDeal);
            }
        });
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_FILTER_DEAL)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    GeneralMethod.setTimeSync(FilterActivity.this);
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    showToastError(getString(R.string.nointernet));
                }
                setupAdapter();
                hideProgressDialog();
            }
        }
    };

    private void buttonReset() {
        edtKeyword.setText("");
        for (TypeDeal typeDeal : lstTypeDeal) {
            typeDeal.setIsSelect(false);
        }

        for (Category category : lstCategory) {
            category.setIsSelect(false);
        }

        filterTypeDealAdapter.setDataSource(lstTypeDeal);
        adapter.setDataSource(lstCategory);
    }

    private void buttonFilter() {

        String categories = "";
        String typeDeals = "";
        String keyword = "";
        String categoriesText = "";
        String typeDealsText = "";

        keyword = edtKeyword.getText().toString().trim();

        for (Category category : lstCategory) {
            if (category.getIsSelect()) {
                if (categories.trim().length() > 0) {
                    categories += "," + category.getId();
                    categoriesText += ", " + category.getName();
                } else {
                    categories = String.valueOf(category.getId());
                    categoriesText = category.getName();
                }
            }
        }

        for (TypeDeal typeDeal : lstTypeDeal) {
            if (typeDeal.getIsSelect()) {
                if (typeDeals.trim().length() > 0) {
                    typeDeals += ", " + typeDeal.getId();
                    typeDealsText += "," + typeDeal.getText();
                } else {
                    typeDeals = String.valueOf(typeDeal.getId());
                    typeDealsText = typeDeal.getText();
                }
            }
        }
        saveFilter(categories, typeDeals, categoriesText, typeDealsText);

        Intent returnIntent = new Intent();
        returnIntent.putExtra(Const.BUNDLE_EXTRAS.CATEGORY_ID, categories);
        returnIntent.putExtra(Const.BUNDLE_EXTRAS.TYPE_DEAL_ID, typeDeals);
        returnIntent.putExtra(Const.BUNDLE_EXTRAS.TEXT_CATEGORY, categoriesText);
        returnIntent.putExtra(Const.BUNDLE_EXTRAS.TEXT_TYPE_DEAL, typeDealsText);
        returnIntent.putExtra(Const.BUNDLE_EXTRAS.KEYWORD, keyword);
        setResult(Activity.RESULT_OK, returnIntent);

        categoriesFiltered = categories;

        finish();
    }

    private void saveFilter(String categoryIds, String typeDealIds, String categoryText, String typeDealText) {
        SharedPreferences prefs = getSharedPreferences("trackData", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("categories", categoryIds);
        editor.putString("typeDealIds", typeDealIds);
        editor.putString("categoryText", categoryText);
        editor.putString("typeDealText", typeDealText);
        editor.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnReset:
                buttonReset();
                break;
            case R.id.btnFilter:
                buttonFilter();
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
    }
}
