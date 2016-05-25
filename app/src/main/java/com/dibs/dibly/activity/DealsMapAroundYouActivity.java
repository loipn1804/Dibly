package com.dibs.dibly.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.consts.Const;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.MyLocationController;
import com.dibs.dibly.daocontroller.OutletController;
import com.dibs.dibly.viewModel.DealsOutletMapsModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import greendao.MyLocation;
import greendao.ObjectDeal;
import greendao.Outlet;

/**
 * Created by VuPhan on 4/20/16.
 */
@Deprecated
public class DealsMapAroundYouActivity extends BaseActivity implements OnMapReadyCallback {


    private List<ObjectDeal> listObjectDeal;
    private List<DealsOutletMapsModel> listDealsOutletMapsModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_deal_arround_map);
        initialToolBar();
        loadDeals();
        loadMap();
    }

    private void loadDeals(){
        listDealsOutletMapsModel = new ArrayList<>();
        listObjectDeal = DealController.getDealByDealType(this, Const.DEALTYPE.SHORT_TERM);
        for(ObjectDeal objectDeal:listObjectDeal){
            Outlet outlet = OutletController.getOutletById(this,objectDeal.getOutlet_id());
            DealsOutletMapsModel dealsOutletMapsModel = new DealsOutletMapsModel();
            dealsOutletMapsModel.setLatitude(outlet.getLatitude());
            dealsOutletMapsModel.setLongtitude(outlet.getLongitude());
            dealsOutletMapsModel.setOrganizationName(objectDeal.getOrganization_name());
            dealsOutletMapsModel.setTitle(objectDeal.getTitle());
            listDealsOutletMapsModel.add(dealsOutletMapsModel);
        }
    }


    private void loadMap(){

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setupPointOutLet(GoogleMap map){
        for(DealsOutletMapsModel dealsOutletMapsModel:listDealsOutletMapsModel){

            if (dealsOutletMapsModel.getLatitude()!=null&&dealsOutletMapsModel.getLatitude().trim().length()>0) {
                double lat = Double.parseDouble(dealsOutletMapsModel.getLatitude());
                double lng = Double.parseDouble(dealsOutletMapsModel.getLongtitude());

                LatLng pointOutlet = new LatLng(lat, lng);
                map.addMarker(new MarkerOptions()
                        .title(dealsOutletMapsModel.getOrganizationName())
                        .snippet(dealsOutletMapsModel.getTitle())
                        .anchor(0.0f, 1.0f)
                        .position(pointOutlet)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maker)));
            }
        }

        MyLocation myLocation = MyLocationController.getLastLocation(this);
        if(myLocation==null) {
            if (listDealsOutletMapsModel != null && listDealsOutletMapsModel.size() > 0) {
                double lat = Double.parseDouble(listDealsOutletMapsModel.get(0).getLatitude());
                double lng = Double.parseDouble(listDealsOutletMapsModel.get(0).getLongtitude());
                LatLng pointOutlet = new LatLng(lat, lng);
                map.addMarker(new MarkerOptions()
                        .title("You are here")
                        .snippet("")
                        .anchor(0.0f, 1.0f)
                        .position(pointOutlet)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_you))).showInfoWindow();
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(pointOutlet, 14));
            }
        }else {
            LatLng pointOutlet = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            map.addMarker(new MarkerOptions()
                    .title("You are here")
                    .snippet("")
                    .anchor(0.0f, 1.0f)
                    .position(pointOutlet)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_you))).showInfoWindow();
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(pointOutlet, 14));
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {

        setupPointOutLet(map);

        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(DealsMapAroundYouActivity.this);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(DealsMapAroundYouActivity.this);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(DealsMapAroundYouActivity.this);
                snippet.setTextColor(Color.GRAY);
                snippet.setGravity(Gravity.CENTER);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
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

