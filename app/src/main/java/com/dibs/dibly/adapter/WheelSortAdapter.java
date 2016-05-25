package com.dibs.dibly.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.staticfunction.StaticFunction;

import java.util.List;

import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

/**
 * Created by USER on 4/17/2015.
 */
public class WheelSortAdapter extends AbstractWheelTextAdapter {


    private List<String> listString;
    private Context context;

    public WheelSortAdapter(Context context, List<String> listString) {
        super(context, R.layout.row_wheel_time, NO_RESOURCE);
        this.listString = listString;
        this.context = context;
        setItemTextResource(R.id.txt);
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        View view = super.getItem(index, cachedView, parent);
        TextView txtTime = (TextView) view.findViewById(R.id.txt);
        txtTime.setText(listString.get(index));
        txtTime.setTypeface(StaticFunction.light(context));

        return view;
    }

    @Override
    public int getItemsCount() {
        return listString.size();
    }

    @Override
    protected CharSequence getItemText(int index) {
        return "";
    }
}
