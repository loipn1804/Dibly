package com.dibs.dibly.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.activity.DealDetailActivity;
import com.dibs.dibly.activity.FilterActivity;
import com.dibs.dibly.activity.HomeActivity;
import com.dibs.dibly.adapter.DealHomeAdapter;
import com.dibs.dibly.adapter.DealHomeDetailAdapter;
import com.dibs.dibly.adapter.DealMoreMerchantDetailAdapter;
import com.dibs.dibly.application.MyApplication;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.consts.Const;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.MerchantController;
import com.dibs.dibly.daocontroller.MyLocationController;
import com.dibs.dibly.daocontroller.OutletController;
import com.dibs.dibly.interfaceUtils.OnPopUpFollowingListener;
import com.dibs.dibly.location.GoogleLocationService;
import com.dibs.dibly.service.ParallaxService;
import com.dibs.dibly.service.RealTimeService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import greendao.Merchant;
import greendao.MyLocation;
import greendao.ObjectDeal;
import greendao.Outlet;

/**
 * Created by USER on 6/30/2015.
 */
public class DealHomeFragment extends Fragment {

    public static int REQUEST_FILTER = 1000;
    private ListView lvDeal;
    private List<ObjectDeal> listDeal;
    private DealHomeDetailAdapter dealHomeAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View progessbarFooter;
    private LayoutInflater layoutInflater;
    private View headerView;
    private LinearLayout lnlFilter, lnlShowFilters,lnlCover;
    private Button btnMoreFilter;
    private TextView txtCategory, txtType;

    private int is_longterm;
    private boolean isLoading;
    private boolean isPull;
    private int page;
    private int last_page;
    private String category_ids = "", typeDeal_ids = "", keyword = "";
    private Long merchantId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(listDeal==null) {
            isLoading = false;
            isPull = false;
            page = 1;
            last_page = 0;

            is_longterm = getArguments().getInt("page", 0);
            MyLocation myLocation = MyLocationController.getLastLocation(getActivity());
            if (myLocation != null) {
                getData(myLocation);
                if (is_longterm != 1) {
                    ((BaseActivity) getActivity()).showProgressDialog();
                }
            } else {
                if (is_longterm != 1) {
//                restartLocationServiceToCheckLocationEnable(getActivity());
                }
            }
        }
        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RealTimeService.RECEIVER_GET_DEAL_SHORT_TERM);
            intentFilter.addAction(ParallaxService.RECEIVER_GET_DEAL_LONG_TERM);
            intentFilter.addAction(RealTimeService.RECEIVER_NOTI_NEW_DEAL);
            intentFilter.addAction(DealDetailActivity.RECEIVER_NOTIFY_LIST);
            intentFilter.addAction(RealTimeService.RECEIVER_STOP_FOLLOWING_MERCHANT);
            intentFilter.addAction(RealTimeService.RECEIVER_FOLLOWING_MERCHANT);
            intentFilter.addAction(HomeActivity.RECEIVER_SORT_LIST);
            intentFilter.addAction(DealDetailActivity.RECEIVER_EXPRITED_TIME);
            getActivity().registerReceiver(activityReceiver, intentFilter);
        }

        View view = inflater.inflate(R.layout.fragment_deal_home, container, false);

        ((BaseActivity) getActivity()).overrideFontsLight(view);

        initView(view);
        initData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            getActivity().unregisterReceiver(activityReceiver);
        }
    }

    private void initView(View view) {

        lnlCover = (LinearLayout) view.findViewById(R.id.lnlCover);
        lnlShowFilters = (LinearLayout) view.findViewById(R.id.btnShowFilter);
        lvDeal = (ListView) view.findViewById(R.id.lvDeal);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange_main);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                last_page = 0;
                MyLocation myLocation = MyLocationController.getLastLocation(getActivity());
                if (myLocation != null) {
                    isPull = true;
                    getData(myLocation);
                } else {
                    if (is_longterm != 1) {
                        restartLocationServiceToCheckLocationEnable(getActivity());
                    }
                }
            }
        });

        lvDeal.setOnScrollListener(new EndScrollListener());

        layoutInflater = LayoutInflater.from(getActivity());
        progessbarFooter = layoutInflater.inflate(R.layout.view_progressbar_loadmore, null);
        headerView = layoutInflater.inflate(R.layout.view_deals_header, null);

        lvDeal.addHeaderView(headerView);
        lnlFilter = (LinearLayout) headerView.findViewById(R.id.lnlFilter);
        LinearLayout lnlCategory = (LinearLayout) headerView.findViewById(R.id.lnlCategory);
        btnMoreFilter = (Button) headerView.findViewById(R.id.btnMoreFilter);
        txtCategory = (TextView) headerView.findViewById(R.id.txtCategory);
        txtType = (TextView) headerView.findViewById(R.id.txtTypeDeal);
        lnlShowFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                startActivityForResult(intent, REQUEST_FILTER);
            }
        });
        lnlCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                startActivityForResult(intent, REQUEST_FILTER);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FILTER) {
            if (resultCode == getActivity().RESULT_OK) {
                lvDeal.smoothScrollToPosition(0);
                category_ids = data.getExtras().getString(Const.BUNDLE_EXTRAS.CATEGORY_ID, "");
                typeDeal_ids = data.getExtras().getString(Const.BUNDLE_EXTRAS.TYPE_DEAL_ID, "");
                keyword = data.getExtras().getString(Const.BUNDLE_EXTRAS.KEYWORD, "");
                String category_text = data.getExtras().getString(Const.BUNDLE_EXTRAS.TEXT_CATEGORY, "");
                String typeDeal_text = data.getExtras().getString(Const.BUNDLE_EXTRAS.TEXT_TYPE_DEAL, "");
                txtCategory.setText((category_text.trim().length() > 0) ? category_text : "-");
                txtType.setText((typeDeal_text.trim().length() > 0) ? typeDeal_text : "-");
                loadData();
            }
        }
    }

    private void loadData() {
        page = 1;
        last_page = 0;
        MyLocation myLocation = MyLocationController.getLastLocation(getActivity());
        if (myLocation != null) {
            isPull = true;
            getData(myLocation);
        } else {
            if (is_longterm != 1) {
                restartLocationServiceToCheckLocationEnable(getActivity());
            }
        }
    }

    private void initData() {
        listDeal = new ArrayList<>();
        dealHomeAdapter = new DealHomeDetailAdapter(getActivity(), listDeal);
        lvDeal.setAdapter(dealHomeAdapter);

        dealHomeAdapter.setOnPopupFollowingListener(new OnPopUpFollowingListener() {
            @Override
            public void onActionStart(long merchantIdArg) {
                merchantId = merchantIdArg;
                ((BaseActivity) getActivity()).showProgressDialog();
            }
        });

        notifyListData();

        SharedPreferences prefs = getActivity().getSharedPreferences("trackData", 0);
        String category_text = prefs.getString("categoryText", "");
        String typeDeal_text = prefs.getString("typeDealText", "");
        txtCategory.setText((category_text.trim().length() > 0) ? category_text : "-");
        txtType.setText((typeDeal_text.trim().length() > 0) ? typeDeal_text : "-");
    }

    private void notifyListDataWithoutSort(long dealID) {
        boolean isExist = false;
        for (ObjectDeal deal : listDeal) {
            if (deal.getId() == dealID) {
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            ObjectDeal objectDeal = DealController.getDealByDealIdAndDealType(getActivity(), dealID, is_longterm);
            if (objectDeal != null) {
                listDeal.add(0, objectDeal);
            }

            dealHomeAdapter.myNotifyDataSetChanged(listDeal);
        }
    }

    private void getData(MyLocation myLocation) {
        if (is_longterm == 1) {
            Intent intentGetDeal = new Intent(getActivity(), ParallaxService.class);
            intentGetDeal.setAction(ParallaxService.ACTION_GET_DEAL_LONG_TERM);
            intentGetDeal.putExtra(ParallaxService.EXTRA_LATITUDE, myLocation.getLatitude() + "");
            intentGetDeal.putExtra(ParallaxService.EXTRA_LONGITUDE, myLocation.getLongitude() + "");
            intentGetDeal.putExtra(ParallaxService.EXTRA_PAGE, page + "");
            intentGetDeal.putExtra(ParallaxService.EXTRA_SORT_FIELD, HomeActivity.sort == HomeActivity.DISTANCE ? "distance" : "start_at");
            intentGetDeal.putExtra(ParallaxService.EXTRA_SORT_DIR, HomeActivity.order == HomeActivity.ASC ? "up" : "down");
            intentGetDeal.putExtra(RealTimeService.EXTRA_KEYWORD, "");
            intentGetDeal.putExtra(RealTimeService.EXTRA_CATEGORIES, "");
            intentGetDeal.putExtra(RealTimeService.EXTRA_TYPE_DEAL, "");
            getActivity().startService(intentGetDeal);
        } else {
            Intent intentGetDeal = new Intent(getActivity(), RealTimeService.class);
            intentGetDeal.setAction(RealTimeService.ACTION_GET_DEAL_SHORT_TERM);
            intentGetDeal.putExtra(RealTimeService.EXTRA_LATITUDE, myLocation.getLatitude() + "");
            intentGetDeal.putExtra(RealTimeService.EXTRA_LONGITUDE, myLocation.getLongitude() + "");
            intentGetDeal.putExtra(RealTimeService.EXTRA_PAGE, page + "");
            intentGetDeal.putExtra(ParallaxService.EXTRA_SORT_FIELD, HomeActivity.sort == HomeActivity.DISTANCE ? "distance" : "start_at");
            intentGetDeal.putExtra(RealTimeService.EXTRA_KEYWORD, keyword);
            intentGetDeal.putExtra(RealTimeService.EXTRA_CATEGORIES, category_ids);
            intentGetDeal.putExtra(RealTimeService.EXTRA_TYPE_DEAL, typeDeal_ids);
            getActivity().startService(intentGetDeal);
        }
    }

    private void notifyListData() {
        listDeal.clear();
        List<ObjectDeal> list = DealController.getDealByDealType(getActivity(), is_longterm);

        listDeal.addAll(list);
        if (HomeActivity.sort == HomeActivity.DISTANCE) {
            if (HomeActivity.order == HomeActivity.ASC) {
                final MyLocation mLoc = MyLocationController.getLastLocation(getActivity());
                if (mLoc != null) {
                    Collections.sort(listDeal, new Comparator<ObjectDeal>() {
                        @Override
                        public int compare(ObjectDeal object1, ObjectDeal object2) {

                            int dis1 = 0;
                            int dis2 = 0;

                            Outlet outlet1 = OutletController.getOutletById(getActivity(), object1.getOutlet_id());
                            if (outlet1 != null) {
                                float result[] = new float[3];
                                Location.distanceBetween(mLoc.getLatitude(), mLoc.getLongitude(), Double.parseDouble(outlet1.getLatitude()), Double.parseDouble(outlet1.getLongitude()), result);
                                dis1 = (int) result[0];
                            }

                            Outlet outlet2 = OutletController.getOutletById(getActivity(), object2.getOutlet_id());
                            if (outlet2 != null) {
                                float result[] = new float[3];
                                Location.distanceBetween(mLoc.getLatitude(), mLoc.getLongitude(), Double.parseDouble(outlet2.getLatitude()), Double.parseDouble(outlet2.getLongitude()), result);
                                dis2 = (int) result[0];
                            }

                            return (dis1 - dis2);
                        }
                    });
                }
            } else {
                final MyLocation mLoc = MyLocationController.getLastLocation(getActivity());
                if (mLoc != null) {
                    Collections.sort(listDeal, new Comparator<ObjectDeal>() {
                        @Override
                        public int compare(ObjectDeal object2, ObjectDeal object1) {

                            int dis1 = 0;
                            int dis2 = 0;

                            Outlet outlet1 = OutletController.getOutletById(getActivity(), object1.getOutlet_id());
                            if (outlet1 != null) {
                                float result[] = new float[3];
                                Location.distanceBetween(mLoc.getLatitude(), mLoc.getLongitude(), Double.parseDouble(outlet1.getLatitude()), Double.parseDouble(outlet1.getLongitude()), result);
                                dis1 = (int) result[0];
                            }

                            Outlet outlet2 = OutletController.getOutletById(getActivity(), object2.getOutlet_id());
                            if (outlet2 != null) {
                                float result[] = new float[3];
                                Location.distanceBetween(mLoc.getLatitude(), mLoc.getLongitude(), Double.parseDouble(outlet2.getLatitude()), Double.parseDouble(outlet2.getLongitude()), result);
                                dis2 = (int) result[0];
                            }

                            return (dis1 - dis2);
                        }
                    });
                }
            }
        } else {
            if (HomeActivity.order == HomeActivity.ASC) {
                Collections.sort(listDeal, new Comparator<ObjectDeal>() {
                    @Override
                    public int compare(ObjectDeal object1, ObjectDeal object2) {
                        return object1.getStart_at().compareTo(object2.getStart_at());
                    }
                });
            } else {
                Collections.sort(listDeal, new Comparator<ObjectDeal>() {
                    @Override
                    public int compare(ObjectDeal object1, ObjectDeal object2) {
                        return object2.getStart_at().compareTo(object1.getStart_at());
                    }
                });
            }
        }


        dealHomeAdapter.myNotifyDataSetChanged(listDeal);
        
        if(listDeal!=null&&listDeal.size()>0){
            lvDeal.setVisibility(View.VISIBLE);
            lnlCover.setVisibility(View.GONE);
        }else{
            lnlCover.setVisibility(View.VISIBLE);
        }

    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (is_longterm == 1) {
                if (intent.getAction().equalsIgnoreCase(ParallaxService.RECEIVER_GET_DEAL_LONG_TERM)) {
                    String result = intent.getStringExtra(ParallaxService.EXTRA_RESULT);
                    if (result.equals(ParallaxService.RESULT_OK)) {
                        String lastpage = intent.getStringExtra(ParallaxService.EXTRA_RESULT_LAST_PAGE);
                        last_page = Integer.parseInt(lastpage);
                        String currentpage = intent.getStringExtra(ParallaxService.EXTRA_RESULT_CURRENT_PAGE);
                        page = Integer.parseInt(currentpage);
                        notifyListData();
                        isLoading = false;
                    } else if (result.equals(ParallaxService.RESULT_FAIL)) {
                        String message = intent.getStringExtra(ParallaxService.EXTRA_RESULT_MESSAGE);
                        ((BaseActivity) getActivity()).showToastError(message);
                    } else if (result.equals(ParallaxService.RESULT_NO_INTERNET)) {
                        ((BaseActivity) getActivity()).showToastError(getString(R.string.nointernet));
                    }
                    lvDeal.removeFooterView(progessbarFooter);
//                    ((BaseActivity) getActivity()).hideCustomProgressDialog();
                } else if (intent.getAction().equalsIgnoreCase(HomeActivity.RECEIVER_SORT_LIST)) {
                    MyLocation myLocation = MyLocationController.getLastLocation(getActivity());
                    if (myLocation != null) {
                        page = 1;
                        last_page = 0;
                        isLoading = true;
                        getData(myLocation);
//                        ((BaseActivity) getActivity()).showProgressDialog();
                    }
                } else if (intent.getAction().equalsIgnoreCase(DealDetailActivity.RECEIVER_NOTIFY_LIST)) {
                    notifyListData();
                }
            } else {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_DEAL_SHORT_TERM)) {

                    if (result.equals(RealTimeService.RESULT_OK)) {
                        String lastpage = intent.getStringExtra(RealTimeService.EXTRA_RESULT_LAST_PAGE);
                        last_page = Integer.parseInt(lastpage);
                        String currentpage = intent.getStringExtra(ParallaxService.EXTRA_RESULT_CURRENT_PAGE);
                        page = Integer.parseInt(currentpage);
                        notifyListData();
                        isLoading = false;
                    }
                    lvDeal.removeFooterView(progessbarFooter);
                    ((BaseActivity) getActivity()).hideCustomProgressDialog();
                } else if (intent.getAction().equalsIgnoreCase(HomeActivity.RECEIVER_SORT_LIST)) {
                    MyLocation myLocation = MyLocationController.getLastLocation(getActivity());
                    if (myLocation != null) {
                        page = 1;
                        last_page = 0;
                        isLoading = true;
                        getData(myLocation);
                        ((BaseActivity) getActivity()).showProgressDialog();
                    }
                } else if (intent.getAction().equalsIgnoreCase(DealDetailActivity.RECEIVER_NOTIFY_LIST)) {
                    notifyListData();
                } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_STOP_FOLLOWING_MERCHANT)) {
                    if (result.equals(RealTimeService.RESULT_OK)) {
                        if (merchantId != null) {
                            Merchant merchant = MerchantController.getById(context, merchantId);
                            if (merchant != null) {
                                merchant.setF_follow(false);
                                MerchantController.update(context, merchant);
                            }

                            DealController.updateObjectIdFliked(getActivity(), merchantId, false);
                            notifyListData();
                            String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                            ((BaseActivity) getActivity()).showToastOk(message);
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

                            DealController.updateObjectIdFliked(getActivity(), merchantId, true);
                            notifyListData();
                            String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                            ((BaseActivity) getActivity()).showToastOk(message);
                            merchantId = null;
                        }
                    }
                }

                ((BaseActivity) getActivity()).hideProgressDialog();
                if (result != null)
                    if (result.equals(RealTimeService.RESULT_FAIL)) {
                        String message = intent.getStringExtra(RealTimeService.EXTRA_RESULT_MESSAGE);
                        ((BaseActivity) getActivity()).showToastError(message);
                    } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                        ((BaseActivity) getActivity()).showToastError(getString(R.string.nointernet));
                    }
            }


            if (isPull) {
                isPull = false;
                swipeRefreshLayout.setRefreshing(false);
            }
            if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_NOTI_NEW_DEAL)) {
                String strDealID = intent.getStringExtra(RealTimeService.EXTRA_RESULT_DEAL_ID);
                long dealID = Long.parseLong(strDealID);
                notifyListDataWithoutSort(dealID);
            } else if (intent.getAction().equalsIgnoreCase(DealDetailActivity.RECEIVER_EXPRITED_TIME)) {
                dealHomeAdapter.myNotifyDataSetChanged(listDeal);
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_DELETE_DEAL)) {
                notifyListData();
            }
        }
    };

    private class EndScrollListener implements AbsListView.OnScrollListener {
        int mLastFirstVisibleItem = 0;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount >= visibleItemCount + 2) {
                if (firstVisibleItem + 3 >= totalItemCount - visibleItemCount + 2) {
                    if (!isLoading && !isPull) {
                        isLoading = true;
                        MyLocation myLocation = MyLocationController.getLastLocation(getActivity());
                        if (myLocation != null) {
                            if (page < last_page) {
                                page++;
                                getData(myLocation);
                                lvDeal.addFooterView(progessbarFooter);
                            }
                        }
                    }
                }
            }

            final int currentFirstVisibleItem = lvDeal.getFirstVisiblePosition();

            if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                ((HomeActivity) getActivity()).hideAndShowToolbar(false);
                lnlShowFilters.setVisibility(View.VISIBLE);
            } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                ((HomeActivity) getActivity()).hideAndShowToolbar(true);
                lnlShowFilters.setVisibility(View.GONE);
            }

            mLastFirstVisibleItem = currentFirstVisibleItem;

        }
    }

    public static void restartLocationServiceToCheckLocationEnable(Activity activity) {

        MyApplication.CurrentActivity = activity;
        // use this to start and trigger a service
        Intent i = new Intent(activity, GoogleLocationService.class);
        i.addCategory(GoogleLocationService.TAG);
        activity.stopService(i);

        activity.startService(i);
    }


}
