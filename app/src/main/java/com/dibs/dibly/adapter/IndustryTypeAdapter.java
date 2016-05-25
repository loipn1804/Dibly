package com.dibs.dibly.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.daocontroller.IndustrySearchController;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import greendao.IndustrySearch;
import greendao.IndustryType;

/**
 * Created by USER on 10/20/2015.
 */
public class IndustryTypeAdapter extends BaseAdapter {

    public interface CheckedChangeCbk {
        void unchecked();
    }
    private CheckedChangeCbk checkedChangeCbk;

    private List<IndustryType> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private List<Boolean> booleanList;

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    private DisplayImageOptions options;

    private Typeface typeface;

    public IndustryTypeAdapter(Activity activity, List<IndustryType> data, List<Boolean> booleanList, CheckedChangeCbk checkedChangeCbk) {
        this.listData = new ArrayList<>();
        this.listData.addAll(data);
        this.booleanList = new ArrayList<>();
        this.booleanList.addAll(booleanList);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.checkedChangeCbk = checkedChangeCbk;

        typeface = StaticFunction.light(activity);

        initImageLoaderOption();
    }

    public void notifyCheckAllChange(List<Boolean> booleanList) {
        this.booleanList.clear();
        this.booleanList.addAll(booleanList);
        notifyDataSetChanged();
    }

    public void initImageLoaderOption() {
        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.logo).showImageOnFail(R.drawable.logo).cacheInMemory(true).cacheOnDisk(true).build();
    }

    @Override
    public int getCount() {
        int count = (listData == null) ? 0 : listData.size();

        return count;
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.row_industry_type, null);
            holder = new ViewHolder();

            holder.chkb = (CheckBox) convertView.findViewById(R.id.chkb);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtName.setTypeface(typeface);

        holder.chkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    IndustrySearch industrySearch = new IndustrySearch(listData.get(position).getId());
                    IndustrySearchController.insert(activity, industrySearch);
                } else {
                    IndustrySearchController.clearById(activity, listData.get(position).getId());
                    checkedChangeCbk.unchecked();
                }
                booleanList.set(position, b);
            }
        });

        holder.chkb.setChecked(booleanList.get(position));

        holder.txtName.setText(listData.get(position).getName());

        return convertView;
    }

    public class ViewHolder {
        CheckBox chkb;
        TextView txtName;
    }
}
