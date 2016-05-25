package com.dibs.dibly.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.dibs.dibly.R;
import com.dibs.dibly.adapter.IndustryTypeAdapter;
import com.dibs.dibly.daocontroller.IndustrySearchController;
import com.dibs.dibly.daocontroller.IndustryTypeController;
import com.dibs.dibly.staticfunction.StaticFunction;

import java.util.ArrayList;
import java.util.List;

import greendao.IndustrySearch;
import greendao.IndustryType;

/**
 * Created by USER on 10/20/2015.
 */
public class DialogMerchantListFilterActivity extends Activity implements View.OnClickListener, IndustryTypeAdapter.CheckedChangeCbk {

    private Button btnOk;
    private Button btnCancel;
    private ListView lvIndustryType;
    private List<IndustryType> listIndustryType;
    private List<Boolean> booleanList;
    private IndustryTypeAdapter industryTypeAdapter;
    private CheckBox chkbAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_merchant_list_filter);

        StaticFunction.overrideFontsLight(this, findViewById(R.id.root));

        initView();
        initData();
    }

    private void initView() {
        btnOk = (Button) findViewById(R.id.btnOk);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        lvIndustryType = (ListView) findViewById(R.id.lvIndustryType);
        chkbAll = (CheckBox) findViewById(R.id.chkbAll);

        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void initData() {
        listIndustryType = IndustryTypeController.getAll(this);
        booleanList = new ArrayList<>();
        for (IndustryType industryType : listIndustryType) {
            if (IndustrySearchController.getById(this, industryType.getId()) != null) {
                booleanList.add(true);
            } else {
                booleanList.add(false);
            }
        }

        industryTypeAdapter = new IndustryTypeAdapter(this, listIndustryType, booleanList, this);
        lvIndustryType.setAdapter(industryTypeAdapter);

        chkbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("checkall", b);
                editor.commit();

                for (int i = 0; i < booleanList.size(); i++) {
                    booleanList.set(i, b);
                }
                industryTypeAdapter.notifyCheckAllChange(booleanList);

                if (b) {
                    IndustrySearchController.clearAll(DialogMerchantListFilterActivity.this);
                    for (IndustryType industryType : IndustryTypeController.getAll(DialogMerchantListFilterActivity.this)) {
                        IndustrySearch industrySearch = new IndustrySearch(industryType.getId());
                        IndustrySearchController.insert(DialogMerchantListFilterActivity.this, industrySearch);
                    }
                } else {
                    IndustrySearchController.clearAll(DialogMerchantListFilterActivity.this);
                }
            }
        });

        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        Boolean isCheckAll = preferences.getBoolean("checkall", true);

        if (isCheckAll) {
            chkbAll.setChecked(isCheckAll);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOk:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }

    @Override
    public void unchecked() {
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("checkall", false);
        editor.commit();
    }
}
