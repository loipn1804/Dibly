package com.dibs.dibly.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.daocontroller.MyLocationController;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.google.android.gms.maps.GoogleMap;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.paypal.android.sdk.ac;

import java.util.ArrayList;
import java.util.List;

import greendao.MyLocation;
import greendao.Outlet;

/**
 * Created by USER on 7/16/2015.
 */
public class AllOutletAdapter extends BaseAdapter {

    private List<Outlet> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    private DisplayImageOptions options;

    GoogleMap map;

    int screenWidth = 0;

    private Typeface typeface;

    public AllOutletAdapter(Activity activity, List<Outlet> data, int screenWidth) {
        this.listData = new ArrayList<>();
        this.listData.addAll(data);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.screenWidth = screenWidth;

        initImageLoaderOption();

        typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/ProximaNova-Light.otf");
    }

    public void setDataSource(List<Outlet> data) {
        this.listData.clear();
        this.listData.addAll(data);
        notifyDataSetChanged();
    }

    public void initImageLoaderOption() {
        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.color.white).showImageOnFail(R.color.white).cacheInMemory(false).cacheOnDisc(true).build();
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
        convertView = this.layoutInflater.inflate(R.layout.row_all_outlet, null);
        holder = new ViewHolder();
        holder.root = (LinearLayout) convertView.findViewById(R.id.root);
        holder.layoutMap = (LinearLayout) convertView.findViewById(R.id.layoutMap);
        holder.txtRoadName = (TextView) convertView.findViewById(R.id.txtRoadName);
        holder.txtAddress = (TextView) convertView.findViewById(R.id.txtAddress);
        holder.txtPhone = (TextView) convertView.findViewById(R.id.txtPhone);
        holder.mapView = (ImageView) convertView.findViewById(R.id.imageMap);
        holder.btnCallMerchant = (LinearLayout) convertView.findViewById(R.id.btnCallMerchant);
        holder.btnViewMap = (LinearLayout) convertView.findViewById(R.id.btnViewMap);
        holder.lnlViewLocation = (LinearLayout) convertView.findViewById(R.id.lnlViewLocation);

        convertView.setTag(holder);

        holder.txtRoadName.setTypeface(typeface);
        holder.txtAddress.setTypeface(typeface);
        holder.txtPhone.setTypeface(typeface);

//        ((BaseActivity)activity).overrideFontsLight(holder.root);

        final Outlet outlet = listData.get(position);
        holder.txtRoadName.setText(outlet.getName());
        if (outlet.getAddress2().length() > 0) {
            holder.txtAddress.setText(outlet.getAddress1() + ", " + outlet.getAddress2());
        } else {
            holder.txtAddress.setText(outlet.getAddress1());
        }
        holder.txtPhone.setText(outlet.getPhone().isEmpty() ? "Not Available" : outlet.getPhone());

//        int height = dpToPx(activity, 250);
//        int width = dpToPx(activity, screenWidth);
//        String getMapURL = "http://maps.googleapis.com/maps/api/staticmap?zoom=16&size=" + width + "x" + height + "&markers=size:mid|color:red|" + outlet.getLatitude() + "," + outlet.getLongitude() + "&sensor=false";
//        imageLoader.displayImage(getMapURL, holder.mapView, options);

//        holder.mapView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Outlet outlet = listData.get(position);
//                if (outlet != null) {
////                    Intent intentMap = new Intent(activity, MapActivity.class);
////                    intentMap.putExtra(MapActivity.EXTRA_LATITUDE, Double.parseDouble(outlet.getLatitude()));
////                    intentMap.putExtra(MapActivity.EXTRA_LONGITUDE, Double.parseDouble(outlet.getLongitude()));
////                    intentMap.putExtra(MapActivity.EXTRA_TITLE, outlet.getName());
//////                    intentMap.putExtra(MapActivity.EXTRA_SUB_TITLE, objectDeal.getTitle());
////                    intentMap.putExtra(MapActivity.EXTRA_MERCHANT_ID, outlet.getMerchant_id());
////                    activity.startActivity(intentMap);
//
//                    String label = outlet.getAddress1() + (outlet.getAddress2().length() == 0 ? "" : ", " + outlet.getAddress2());
//                    String uriBegin = "geo:" + outlet.getLatitude() + "," + outlet.getLongitude();
//                    String query = outlet.getLatitude() + "," + outlet.getLongitude() + "(" + label + ")";
//                    String encodedQuery = Uri.encode(query);
//                    String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
//                    Uri uri = Uri.parse(uriString);
//                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
//                    activity.startActivity(intent);
//                }
//            }
//        });

        holder.lnlViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Outlet outlet = listData.get(position);
                if (outlet != null) {
                    String query = outlet.getLatitude() + "," + outlet.getLongitude();
                    String uriString = "google.navigation:q=" + query;
                    Uri uri = Uri.parse(uriString);
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                    activity.startActivity(intent);
                }
            }
        });

        holder.btnViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Outlet outlet = listData.get(position);
                if (outlet != null) {
                    String query = outlet.getLatitude() + "," + outlet.getLongitude();
                    String uriString = "google.navigation:q=" + query;
                    Uri uri = Uri.parse(uriString);
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                    activity.startActivity(intent);
                }
            }
        });

        holder.btnCallMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!outlet.getPhone().isEmpty()) {
                    StaticFunction.callPhone(activity, outlet.getPhone());
                }
            }
        });

        return convertView;
    }

    public int dpToPx(Activity activity, int dp) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public class ViewHolder {
        LinearLayout root;
        LinearLayout layoutMap;
        LinearLayout btnCallMerchant;
        LinearLayout btnViewMap;

        TextView txtRoadName;
        TextView txtAddress;
        TextView txtPhone;
        ImageView mapView;

        LinearLayout lnlViewLocation;
    }
}
