package com.dibs.dibly.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.activity.DealDetailV2Activity;
import com.dibs.dibly.consts.Const;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.MyLocationController;
import com.dibs.dibly.daocontroller.OutletController;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.dibs.dibly.viewModel.DealsOutletMapsModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import greendao.MyLocation;
import greendao.ObjectDeal;
import greendao.Outlet;

/**
 * Created by VuPhan on 5/2/16.
 */
public class DealMapFragment extends Fragment implements OnMapReadyCallback {

    private List<ObjectDeal> listObjectDeal;
    private List<DealsOutletMapsModel> listDealsOutletMapsModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deal_arround_map, container, false);
        loadDeals();
        loadMap();
        return view;
    }

    private void loadDeals(){
        listDealsOutletMapsModel = new ArrayList<>();
        listObjectDeal = DealController.getDealByDealType(getActivity(), Const.DEALTYPE.SHORT_TERM);
        for(ObjectDeal objectDeal:listObjectDeal){
            Outlet outlet = OutletController.getOutletById(getActivity(), objectDeal.getOutlet_id());
            DealsOutletMapsModel dealsOutletMapsModel = new DealsOutletMapsModel();
            dealsOutletMapsModel.setLatitude(outlet.getLatitude());
            dealsOutletMapsModel.setLongtitude(outlet.getLongitude());
            dealsOutletMapsModel.setOrganizationName(objectDeal.getOrganization_name());
            dealsOutletMapsModel.setTitle(objectDeal.getTitle());
            dealsOutletMapsModel.setObjectDeal(objectDeal);
            listDealsOutletMapsModel.add(dealsOutletMapsModel);
        }
    }


    private void loadMap(){

        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }

        fragment.getMapAsync(this);
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



                map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {

                        for (DealsOutletMapsModel dealsOutletMapsModel : listDealsOutletMapsModel) {
                           if(marker.getTitle().equals(dealsOutletMapsModel.getOrganizationName())) {
                                Intent intentDealDetail = new Intent(getActivity(), DealDetailV2Activity.class);
                                intentDealDetail.putExtra("deal_id", dealsOutletMapsModel.getObjectDeal().getDeal_id());
                                intentDealDetail.putExtra("outletId", dealsOutletMapsModel.getObjectDeal().getOutlet_id());
                                intentDealDetail.putExtra("merchant_id", dealsOutletMapsModel.getObjectDeal().getMerchant_id());
                                getActivity().startActivity(intentDealDetail);
                            }
                        }

                    }
                });
            }
        }

        MyLocation myLocation = MyLocationController.getLastLocation(getActivity());
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

                LinearLayout info = new LinearLayout(getActivity());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getActivity());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getActivity());
                snippet.setTextColor(Color.GRAY);
                snippet.setGravity(Gravity.CENTER);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                return info;
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
