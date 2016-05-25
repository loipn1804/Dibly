package com.dibs.dibly.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dibs.dibly.R;
import com.dibs.dibly.adapter.SearchDealAdapter;
import com.dibs.dibly.adapter.SuggestLocationAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.DealMerchantController;
import com.dibs.dibly.daocontroller.FilterDealController;
import com.dibs.dibly.daocontroller.MyLocationController;
import com.dibs.dibly.daocontroller.OutletController;
import com.dibs.dibly.fragment.DealHomeFragment;
import com.dibs.dibly.location.GoogleLocationService;
import com.dibs.dibly.service.RealTimeService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import greendao.DealFilter;
import greendao.MyLocation;
import greendao.ObjectDeal;
import greendao.Outlet;

/**
 * Created by USER on 6/29/2015.
 */
public class SearchActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    //    private static final LatLngBounds BOUNDS_VIETNAM = new LatLngBounds(new LatLng(10.761520, 106.670142), new LatLng(10.813047, 106.716116));
//    private static final LatLngBounds BOUNDS_VIETNAM = new LatLngBounds(new LatLng(-90, -180), new LatLng(90, 180));
    private static final LatLngBounds BOUNDS_VIETNAM = new LatLngBounds(new LatLng(1.157844, 103.581481), new LatLng(1.490829, 104.094203));
    protected GoogleApiClient mGoogleApiClient;
    ListView listViewSuggestLocation;
    EditText edtSearch;
    ArrayList<PlaceAutocomplete> listResultLocation;
    SuggestLocationAdapter adapter;

    LinearLayout rootSearch;
    LinearLayout rootDeals;
    LinearLayout rootMap;
    LinearLayout btnCurrentLocation;

    private RelativeLayout rltBack;

    ListView listViewDeals;

    private SearchDealAdapter dealAdapter;
    private List<ObjectDeal> listDeals;
    RelativeLayout btnFilter;

    RelativeLayout btnMap;
    RelativeLayout rltFilter;

    GoogleMap googleMap;

    LatLng currentSearchBaseLocation;

    ImageView btnClear;

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_SEARCH_DEAL)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    enableDealsView();
                    notifyListData();
                    hideCustomProgressDialog();
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {
                    hideCustomProgressDialog();
                    String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                    showToastError(message);
                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    hideCustomProgressDialog();
                    showToastError(getString(R.string.nointernet));
                }
            } else if (intent.getAction().equalsIgnoreCase(DealDetailActivity.RECEIVER_EXPRITED_TIME)) {
                notifyListData();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_SEARCH_DEAL);
            intentFilter.addAction(DealDetailActivity.RECEIVER_EXPRITED_TIME);
            this.registerReceiver(activityReceiver, intentFilter);
        }

        // use this to start and trigger a service
        Intent i = new Intent(this, GoogleLocationService.class);
        i.addCategory(GoogleLocationService.TAG);
        this.startService(i);

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, 0 /* clientId */, this).addApi(Places.GEO_DATA_API).build();

        setContentView(R.layout.activity_search);

        overrideFontsLight(findViewById(R.id.root));

        listResultLocation = new ArrayList<>();

        initView();
        initData();

        enableSearchView();

        adapter = new SuggestLocationAdapter(this, listResultLocation);
        listViewSuggestLocation.setAdapter(adapter);

        listViewSuggestLocation.setOnItemClickListener(mAutocompleteClickListener);
    }


    private List<ObjectDeal> filterDeals() {
        List<ObjectDeal> listSearch1 = new ArrayList<>();
        DealFilter filter = FilterDealController.getFilter(SearchActivity.this);
        String purchaseType = filter.getPurchase_type();

        if (purchaseType.equalsIgnoreCase(FilterDealController.PURCHASE_TYPE_BOTH)) {
            listSearch1.addAll(listDeals);
        } else {
            for (ObjectDeal objectDeal : listDeals) {
                if (objectDeal.getType().equalsIgnoreCase(purchaseType)) {
                    listSearch1.add(objectDeal);
                }
            }
        }

        long filterDistance = filter.getDistance();
        List<ObjectDeal> listSearch2 = new ArrayList<>();

        for (ObjectDeal objectDeal : listSearch1) {
            int strDistance = OutletController.getNearestOutletByOutletId(SearchActivity.this, objectDeal.getOutlet_id());
            long distance = (long) strDistance;
            if (filterDistance >= distance) {
                listSearch2.add(objectDeal);
            }
        }

        return listSearch2;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
//                Toast.makeText(SearchActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                listDeals = filterDeals();
                dealAdapter.setData(listDeals);
                if (currentSearchBaseLocation != null) {
                    getDealsBaseOnLocation(currentSearchBaseLocation.latitude, currentSearchBaseLocation.longitude);
                } else {
                    MyLocation myLocation = MyLocationController.getLastLocation(SearchActivity.this);
                    if (myLocation != null) {
                        getDealsBaseOnLocation(myLocation.getLatitude(), myLocation.getLongitude());
                    }
                }
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
                //Toast.makeText(SearchActivity.this, "CANCEL", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void enableSearchView() {
        rootDeals.setVisibility(View.GONE);
        rootMap.setVisibility(View.GONE);
        rootSearch.setVisibility(View.VISIBLE);
        btnMap.setVisibility(View.GONE);
        rltFilter.setVisibility(View.GONE);
    }

    private void enableDealsView() {
        rootSearch.setVisibility(View.GONE);
        rootMap.setVisibility(View.GONE);
        rootDeals.setVisibility(View.VISIBLE);
        btnMap.setVisibility(View.VISIBLE);
        rltFilter.setVisibility(View.GONE);
    }

    private void enableMapView() {
        rootMap.setVisibility(View.VISIBLE);
        rootSearch.setVisibility(View.GONE);
        rootDeals.setVisibility(View.GONE);
        btnMap.setVisibility(View.GONE);
        rltFilter.setVisibility(View.GONE);
    }

    private void initView() {
        listViewSuggestLocation = (ListView) findViewById(R.id.listSuggestLocation);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        btnCurrentLocation = (LinearLayout) findViewById(R.id.btnCurrentLocation);
        rootSearch = (LinearLayout) findViewById(R.id.rootSearch);
        rootDeals = (LinearLayout) findViewById(R.id.rootDeals);
        rootMap = (LinearLayout) findViewById(R.id.rootMap);
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        btnClear = (ImageView) findViewById(R.id.btnDeleteSearchText);

        listViewDeals = (ListView) findViewById(R.id.lvDeals);
        btnFilter = (RelativeLayout) findViewById(R.id.btnFilter);
        btnFilter.setVisibility(View.GONE);
        btnMap = (RelativeLayout) findViewById(R.id.btnMap);
        rltFilter = (RelativeLayout) findViewById(R.id.rltFilter);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableMapView();
                if (currentSearchBaseLocation != null) {
                    markBaseSearchLocation(currentSearchBaseLocation);
                }
                markDealOutlet();
            }
        });

        rltFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, FilterDealActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        btnCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLocation myLocation = MyLocationController.getLastLocation(SearchActivity.this);
                if (myLocation != null) {
                    LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    currentSearchBaseLocation = latLng;
                    getDealsBaseOnLocation(myLocation.getLatitude(), myLocation.getLongitude());
                    showCustomProgressDialog();
                } else {
                    DealHomeFragment.restartLocationServiceToCheckLocationEnable(SearchActivity.this);
                }
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, FilterDealActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    btnClear.setVisibility(View.VISIBLE);
                } else {
                    btnClear.setVisibility(View.GONE);
                }

                searchInBackground(s.toString());

//                Handler han = new Handler();
//
//                for (int i = 0; i < 20000; i++) {
//
//                    han.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Random random = new Random();
//                            searchInBackground(s.toString() + random.nextInt(10000));
//                        }
//                    }, 100);
//                }


            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        rltBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                if (rootMap.getVisibility() == View.VISIBLE) {
                    enableDealsView();
                } else if (rootDeals.getVisibility() == View.VISIBLE) {
//                    finish();
                    enableSearchView();
                } else if (rootSearch.getVisibility() == View.VISIBLE) {
                    finish();
                } else {
                    finish();
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (rootMap.getVisibility() == View.VISIBLE) {
            enableDealsView();
        } else if (rootDeals.getVisibility() == View.VISIBLE) {
//                    finish();
            enableSearchView();
        } else if (rootSearch.getVisibility() == View.VISIBLE) {
            finish();
        } else {
            finish();
        }
    }

    @Override
    public void onMapReady(final GoogleMap map) {

        this.googleMap = map;

//        this.googleMap.setOnMarkerClickListener(this);
        this.googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
//                showToastInfo(marker.getTitle());
                for (ObjectDeal objectDeal : listDeals) {
                    Outlet objectDealOutlet = OutletController.getOutletById(SearchActivity.this, objectDeal.getOutlet_id());
                    String outlet_name = objectDealOutlet.getName();
                    if (outlet_name.equalsIgnoreCase(marker.getTitle())) {
                        long merchant_id = objectDeal.getMerchant_id();
                        Intent intentMerchant = new Intent(SearchActivity.this, MerchantDealActivity.class);
                        intentMerchant.putExtra("id", merchant_id);
                        intentMerchant.putExtra("name", DealMerchantController.getNameByMerchantId(SearchActivity.this, merchant_id));
                        startActivity(intentMerchant);
                        break;
                    }
                }
            }
        });

        final MyLocation mLoc = MyLocationController.getLastLocation(SearchActivity.this);

        if (mLoc != null) {
            LatLng currentPos = new LatLng(mLoc.getLatitude(), mLoc.getLongitude());

            map.setMyLocationEnabled(true);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 14), 1000, null);
        }

        markDealOutlet();

    }

    private void markBaseSearchLocation(LatLng latLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14), 1000, null);
        googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.locmarker)).title("").position(latLng));
    }

    private void markDealOutlet() {
        for (ObjectDeal objectDeal : listDeals) {
            Outlet objectDealOutlet = OutletController.getOutletById(SearchActivity.this, objectDeal.getOutlet_id());
//                String merchant_name = DealMerchantController.getNameByMerchantId(SearchActivity.this, objectDeal.getMerchant_id());
            if (objectDealOutlet != null) {
                String outlet_name = objectDealOutlet.getName();
                String address = objectDealOutlet.getAddress1();
                if (objectDealOutlet.getAddress2().length() > 0) {
                    address += ". " + objectDealOutlet.getAddress2();
                }
                googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).title(outlet_name).snippet(address).position(new LatLng(Double.parseDouble(objectDealOutlet.getLatitude()), Double.parseDouble(objectDealOutlet.getLongitude())))).showInfoWindow();
            }
        }
    }

    private void getDealsBaseOnLocation(double lat, double lng) {
//        Intent intentGetDeal = new Intent(this, RealTimeService.class);
//        intentGetDeal.setAction(RealTimeService.ACTION_SEARCH_DEAL);
//        intentGetDeal.putExtra(RealTimeService.EXTRA_LATITUDE, lat + "");
//        intentGetDeal.putExtra(RealTimeService.EXTRA_LONGITUDE, lng + "");
//        intentGetDeal.putExtra(RealTimeService.EXTRA_PAGE, "1");
//        this.startService(intentGetDeal);

        Intent intentGetDeal = new Intent(this, RealTimeService.class);
        intentGetDeal.setAction(RealTimeService.ACTION_SEARCH_DEAL);
        intentGetDeal.putExtra(RealTimeService.EXTRA_LATITUDE, lat + "");
        intentGetDeal.putExtra(RealTimeService.EXTRA_LONGITUDE, lng + "");
        intentGetDeal.putExtra(RealTimeService.EXTRA_PAGE, "1");

        String keyword = "";
        String deal_type = "";
        String discovery_type = "";
        String min = "";
        String max = "5000";
        DealFilter dealFilter = FilterDealController.getFilter(SearchActivity.this);
        if (dealFilter != null) {
            keyword = dealFilter.getKeyword();
            deal_type = dealFilter.getPurchase_type();
            discovery_type = dealFilter.getIndustry_type();
            min = "0";
            max = dealFilter.getDistance() + "";
        }
        intentGetDeal.putExtra(RealTimeService.EXTRA_KEYWORD, keyword);
        intentGetDeal.putExtra(RealTimeService.EXTRA_DEAL_TYPE, deal_type);
        intentGetDeal.putExtra(RealTimeService.EXTRA_DISCOVERY_TYPE, discovery_type);
        intentGetDeal.putExtra(RealTimeService.EXTRA_MIN, min);
        intentGetDeal.putExtra(RealTimeService.EXTRA_MAX, max);

        this.startService(intentGetDeal);
    }

    private void initData() {
        listDeals = new ArrayList<>();
        dealAdapter = new SearchDealAdapter(SearchActivity.this, listDeals);
        listViewDeals.setAdapter(dealAdapter);
    }

    private void notifyListData() {
        listDeals.clear();
        List<ObjectDeal> list = DealController.getDealByDealType(SearchActivity.this, 2);
        listDeals.addAll(list);
        dealAdapter.setData(listDeals);

        FilterDealController.clearAll(SearchActivity.this);
        if (listDeals.size() == 0) {
            btnMap.setVisibility(View.GONE);
            rltFilter.setVisibility(View.GONE);
//            btnFilter.setVisibility(View.GONE);
        } else {
            btnMap.setVisibility(View.VISIBLE);
            rltFilter.setVisibility(View.VISIBLE);
//            btnFilter.setVisibility(View.VISIBLE);
        }
    }

    private synchronized void searchInBackground(final String searchString) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                if (!searchString.trim().equals("")) {
                    if (searchString.length() > 1) {
                        listResultLocation = getAutocomplete(searchString);
                        if (listResultLocation != null) {
                            if (!listResultLocation.isEmpty()) {
                                return "OK";
                            } else {
                                return "NO_RESULT";
                            }
                        } else {
                            return "NO_RESULT";
                        }
                    } else {
                        return "NO_RESULT";
                    }
                } else {
                    return "NO_RESULT";
                }

            }

            @Override
            protected void onPostExecute(String msg) {
                if (msg.equals("OK")) {
                    enableSearchView();
                    adapter.setData(listResultLocation);
                }
                if (msg.equals("NO_RESULT")) {
                    listResultLocation = new ArrayList<>();
                    adapter.setData(listResultLocation);
                }

            }
        }.execute(null, null, null);
    }

    private ArrayList<PlaceAutocomplete> getAutocomplete(CharSequence constraint) {
        if (mGoogleApiClient.isConnected()) {

            // Submit the query to the autocomplete API and retrieve a PendingResult that will contain the results when the query completes.
            PendingResult<AutocompletePredictionBuffer> results = Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, constraint.toString(), BOUNDS_VIETNAM, null);

            // This method should have been called off the main UI thread. Block and wait for at most 60s for a result from the API.
            AutocompletePredictionBuffer autocompletePredictions = results.await(60, TimeUnit.SECONDS);

            // Confirm that the query completed successfully, otherwise return null
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                //Toast.makeText(getContext(), "Error contacting API: " + status.toString(), Toast.LENGTH_SHORT).show();

                autocompletePredictions.release();
                return null;
            }


            // Copy the results into our own data structure, because we can't hold onto the buffer.
            // AutocompletePrediction objects encapsulate the API response (place ID and description).

            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList resultList = new ArrayList<>(autocompletePredictions.getCount());
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                // Get the details of this prediction and copy it into a new PlaceAutocomplete object.
                resultList.add(new PlaceAutocomplete(prediction.getPlaceId(), prediction.getDescription()));
            }


            // Release the buffer now that all data has been copied.
            autocompletePredictions.release();

            return resultList;
        }

        return null;
    }


    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            showCustomProgressDialog();
//
//             Retrieve the place ID of the selected item from the Adapter.
//             The adapter stores each Place suggestion in a PlaceAutocomplete object from which we
//             read the place ID.

            final PlaceAutocomplete item = (PlaceAutocomplete) adapter.getItem(position);
            edtSearch.setText(item.description);
            final String placeId = String.valueOf(item.placeId);
            Log.i("", "Autocomplete item selected: " + item.description);


//             Issue a request to the Places Geo Data API to retrieve a Place object with additional
//              details about the place.

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            //Toast.makeText(getApplicationContext(), "Clicked: " + item.description, Toast.LENGTH_SHORT).show();
            Log.i("", "Called getPlaceById to get Place details for " + item.placeId);
        }
    };


    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {

            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e("", "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            // Format details of the place for display and show it in a TextView.
            //mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(), place.getId(), place.getAddress(), place.getPhoneNumber(), place.getWebsiteUri()));

            // Display the third party attributions if set.
            /*final CharSequence thirdPartyAttribution = places.getAttributions();
            if (thirdPartyAttribution == null)
            {
                mPlaceDetailsAttribution.setVisibility(View.GONE);
            }
            else
            {
                mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
                mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
            }*/

            Log.i("", "Place details received: " + place.getName());
            LatLng latLng = place.getLatLng();
            currentSearchBaseLocation = latLng;
            //Toast.makeText(SearchActivity.this, latLng.toString(), Toast.LENGTH_LONG).show();

            getDealsBaseOnLocation(latLng.latitude, latLng.longitude);

            places.release();
        }
    };


    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id, CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e("", res.getString(R.string.place_details, name, id, address, phoneNumber, websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber, websiteUri));

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e("", "onConnectionFailed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this, "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        showToastInfo(marker.getTitle());
        return false;
    }

    public class PlaceAutocomplete {

        public CharSequence placeId;
        public CharSequence description;

        PlaceAutocomplete(CharSequence placeId, CharSequence description) {
            this.placeId = placeId;
            this.description = description;
        }

        @Override
        public String toString() {
            return description.toString();
        }
    }

    @Override
    public void onDestroy() {
//        MyApplication.CurrentActivity = null;
        super.onDestroy();
        if (activityReceiver != null) {
            this.unregisterReceiver(activityReceiver);
        }
    }
}
