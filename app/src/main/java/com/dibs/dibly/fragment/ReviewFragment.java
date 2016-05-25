package com.dibs.dibly.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.adapter.ReviewAdapter;
import com.dibs.dibly.base.BaseActivity;
import com.dibs.dibly.daocontroller.CommentController;
import com.dibs.dibly.daocontroller.DealController;
import com.dibs.dibly.daocontroller.UserController;
import com.dibs.dibly.service.ParallaxService;
import com.dibs.dibly.service.RealTimeService;
import com.dibs.dibly.staticfunction.StaticFunction;

import java.util.ArrayList;
import java.util.List;

import greendao.Comment;
import greendao.ObjectDeal;

/**
 * Created by USER on 11/2/2015.
 */
public class ReviewFragment extends Fragment implements View.OnClickListener {

    private LinearLayout lnlLike;
    private LinearLayout lnlUnLike;
    private ImageView imvLike;
    private ImageView imvUnLike;
    private TextView txtYourLike;
    private TextView txtYourUnLike;
    private TextView txtLike;
    private TextView txtUnLike;
    private ProgressBar progLike;
    private ProgressBar progUnLike;
    private View headerView;
    private LayoutInflater layoutInflater;
    private View progessbarFooter;
    private TextView txtNoCmt;

    private ListView lvReview;
    private ReviewAdapter reviewAdapter;
    private List<Comment> listComment;

    private Long dealId;

    private boolean isLoading;
    private int page;
    private int last_page;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        ((BaseActivity) getActivity()).overrideFontsLight(view);

        isLoading = false;
        page = 1;
        last_page = 0;

        registerReceiver();

        if (getActivity().getIntent().hasExtra("deal_id")) {
            dealId = getActivity().getIntent().getLongExtra("deal_id", 0l);
        } else {
            getActivity().finish();
        }

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        layoutInflater = LayoutInflater.from(getActivity());

        headerView = layoutInflater.inflate(R.layout.view_like_unlike, null);
        progessbarFooter = layoutInflater.inflate(R.layout.view_progressbar_loadmore, null);
        StaticFunction.overrideFontsLight(getActivity(), headerView);

        lnlLike = (LinearLayout) headerView.findViewById(R.id.lnlLike);
        lnlUnLike = (LinearLayout) headerView.findViewById(R.id.lnlUnLike);
        imvLike = (ImageView) headerView.findViewById(R.id.imvLike);
        imvUnLike = (ImageView) headerView.findViewById(R.id.imvUnLike);
        txtYourLike = (TextView) headerView.findViewById(R.id.txtYourLike);
        txtYourUnLike = (TextView) headerView.findViewById(R.id.txtYourUnLike);
        txtLike = (TextView) headerView.findViewById(R.id.txtLike);
        txtUnLike = (TextView) headerView.findViewById(R.id.txtUnLike);
        progLike = (ProgressBar) headerView.findViewById(R.id.progLike);
        progUnLike = (ProgressBar) headerView.findViewById(R.id.progUnLike);
        txtNoCmt = (TextView) headerView.findViewById(R.id.txtNoCmt);

        progLike.setVisibility(View.GONE);
        progUnLike.setVisibility(View.GONE);
        txtNoCmt.setVisibility(View.GONE);

        lvReview = (ListView) view.findViewById(R.id.lvReview);

        lnlLike.setOnClickListener(this);
        lnlUnLike.setOnClickListener(this);

        lvReview.setOnScrollListener(new EndScrollListener());

        lvReview.addHeaderView(headerView);
    }

    private void initData() {
        listComment = new ArrayList<Comment>();
        reviewAdapter = new ReviewAdapter(getActivity(), listComment);
        lvReview.setAdapter(reviewAdapter);

        getData();
        ((BaseActivity) getActivity()).showProgressDialog();
    }

    private void getData() {
        Intent intentGetComment = new Intent(getActivity(), ParallaxService.class);
        intentGetComment.setAction(ParallaxService.ACTION_GET_COMMENT_BY_DEAL_ID);
        intentGetComment.putExtra(ParallaxService.EXTRA_DEAL_ID, dealId + "");
        intentGetComment.putExtra(ParallaxService.EXTRA_PAGE, page + "");
        getActivity().startService(intentGetComment);
    }

    private void notifyListData() {
        listComment.clear();
        listComment = CommentController.getAll(getActivity());
        reviewAdapter.setListData(listComment);
        if (listComment.size() == 0) {
            txtNoCmt.setVisibility(View.VISIBLE);
        } else {
            txtNoCmt.setVisibility(View.GONE);
        }
    }

    private void setLikeAndUnlike() {
        ObjectDeal objectDeal = DealController.getDealByDealId(getActivity(), dealId);
        if (objectDeal != null) {
            if (UserController.isLogin(getActivity())) {
                if (objectDeal.getF_claimed()) {
                    if (objectDeal.getF_yay()) {
                        imvLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                        int otherLike = objectDeal.getK_likes() - 1;
                        if (otherLike < 1) {
                            txtYourLike.setText("You yay this ");
                        } else if (otherLike == 1) {
                            txtYourLike.setText("You and " + otherLike + " user yay this ");
                        } else {
                            txtYourLike.setText("You and " + otherLike + " users yay this ");
                        }
                    } else {
                        imvLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                        txtYourLike.setText("");
                    }
                    if (objectDeal.getF_nay()) {
                        imvUnLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_unlike));
                        int otherUnLike = objectDeal.getK_unlikes() - 1;
                        if (otherUnLike < 1) {
                            txtYourUnLike.setText("You nay this ");
                        } else if (otherUnLike == 1) {
                            txtYourUnLike.setText("You and " + (objectDeal.getK_unlikes() - 1) + " user nay this ");
                        } else {
                            txtYourUnLike.setText("You and " + (objectDeal.getK_unlikes() - 1) + " users nay this ");
                        }
                    } else {
                        imvUnLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_unlike));
                        txtYourUnLike.setText("");
                    }
                }
            }
            txtLike.setText(objectDeal.getK_likes() + " ");
            txtUnLike.setText(objectDeal.getK_unlikes() + " ");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lnlLike:
                if (UserController.isLogin(getActivity())) {
                    String consumer_id = UserController.getCurrentUser(getActivity()).getConsumer_id() + "";
                    ObjectDeal deal = DealController.getDealById(getActivity(), dealId);
                    if (deal != null) {
                        if (deal.getF_claimed()) {
                            if (!deal.getF_yay()) {
                                Intent intentGetDealDetail = new Intent(getActivity(), RealTimeService.class);
                                intentGetDealDetail.setAction(RealTimeService.ACTION_LIKE_DEAL);
                                intentGetDealDetail.putExtra(RealTimeService.EXTRA_CONSUMER_ID, consumer_id);
                                intentGetDealDetail.putExtra(RealTimeService.EXTRA_MERCHANT_ID, deal.getMerchant_id() + "");
                                intentGetDealDetail.putExtra(RealTimeService.EXTRA_DEAL_ID, deal.getDeal_id() + "");
                                getActivity().startService(intentGetDealDetail);
                                progLike.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else {
                    ((BaseActivity) getActivity()).showToastInfo(getString(R.string.login_first));
                }
                break;
            case R.id.lnlUnLike:
                if (UserController.isLogin(getActivity())) {
                    String consumer_id = UserController.getCurrentUser(getActivity()).getConsumer_id() + "";
                    ObjectDeal deal = DealController.getDealById(getActivity(), dealId);
                    if (deal != null) {
                        if (deal.getF_claimed()) {
                            if (!deal.getF_nay()) {
                                Intent intentGetDealDetail = new Intent(getActivity(), RealTimeService.class);
                                intentGetDealDetail.setAction(RealTimeService.ACTION_UNLIKE_DEAL);
                                intentGetDealDetail.putExtra(RealTimeService.EXTRA_CONSUMER_ID, consumer_id);
                                intentGetDealDetail.putExtra(RealTimeService.EXTRA_MERCHANT_ID, deal.getMerchant_id() + "");
                                intentGetDealDetail.putExtra(RealTimeService.EXTRA_DEAL_ID, deal.getDeal_id() + "");
                                getActivity().startService(intentGetDealDetail);
                                progUnLike.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else {
                    ((BaseActivity) getActivity()).showToastInfo(getString(R.string.login_first));
                }
                break;
        }
    }

    private void registerReceiver() {
        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ParallaxService.RECEIVER_GET_COMMENT_BY_DEAL_ID);
            intentFilter.addAction(RealTimeService.RECEIVER_GET_DEAL_DETAIL);
            intentFilter.addAction(RealTimeService.RECEIVER_LIKE_DEAL);
            intentFilter.addAction(RealTimeService.RECEIVER_UNLIKE_DEAL);
            getActivity().registerReceiver(activityReceiver, intentFilter);
        }
    }

    private void unRegisterReceiver() {
        if (activityReceiver != null) {
            getActivity().unregisterReceiver(activityReceiver);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(ParallaxService.RECEIVER_GET_COMMENT_BY_DEAL_ID)) {
                String result = intent.getStringExtra(ParallaxService.EXTRA_RESULT);
                if (result.equals(ParallaxService.RESULT_OK)) {
                    String lastpage = intent.getStringExtra(RealTimeService.EXTRA_RESULT_LAST_PAGE);
                    last_page = Integer.parseInt(lastpage);
                    String currentpage = intent.getStringExtra(RealTimeService.EXTRA_RESULT_CURRENT_PAGE);
                    page = Integer.parseInt(currentpage);
                    isLoading = false;
                    notifyListData();
                    if (page == 1) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (lvReview.getFirstVisiblePosition() == 0 && lvReview.getLastVisiblePosition() == lvReview.getCount() - 1) {
                                    isLoading = true;
                                    if (page < last_page) {
                                        page++;
                                        getData();
                                        lvReview.addFooterView(progessbarFooter);
                                    }
                                }
                            }
                        }, 500);
                    }
                } else if (result.equals(ParallaxService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(ParallaxService.EXTRA_RESULT_MESSAGE);
                    ((BaseActivity) getActivity()).showToastError(message);
                } else if (result.equals(ParallaxService.RESULT_NO_INTERNET)) {
                    ((BaseActivity) getActivity()).showToastError(getString(R.string.nointernet));
                }
                lvReview.removeFooterView(progessbarFooter);
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_GET_DEAL_DETAIL)) {
                Log.d("deal_id", dealId + "");
                String result = intent.getStringExtra(ParallaxService.EXTRA_RESULT);
                if (result.equals(ParallaxService.RESULT_OK)) {
                    setLikeAndUnlike();
                }
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_LIKE_DEAL)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    ObjectDeal deal = DealController.getDealByDealId(getActivity(), dealId);
                    if (deal != null) {
                        deal.setF_yay(true);
                        deal.setK_likes(deal.getK_likes() + 1);
                        if (deal.getF_nay()) {
                            deal.setF_nay(false);
                            deal.setK_unlikes(deal.getK_unlikes() - 1);
                        }
                        DealController.update(getActivity(), deal);
                        setLikeAndUnlike();
                    }
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {

                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    ((BaseActivity) getActivity()).showToastError(getString(R.string.nointernet));
                }
                progLike.setVisibility(View.GONE);
            } else if (intent.getAction().equalsIgnoreCase(RealTimeService.RECEIVER_UNLIKE_DEAL)) {
                String result = intent.getStringExtra(RealTimeService.EXTRA_RESULT);
                if (result.equals(RealTimeService.RESULT_OK)) {
                    ObjectDeal deal = DealController.getDealByDealId(getActivity(), dealId);
                    if (deal != null) {
                        deal.setF_nay(true);
                        deal.setK_unlikes(deal.getK_unlikes() + 1);
                        if (deal.getF_yay()) {
                            deal.setF_yay(false);
                            deal.setK_likes(deal.getK_likes() - 1);
                        }
                        DealController.update(getActivity(), deal);
                        setLikeAndUnlike();
                    }
                } else if (result.equals(RealTimeService.RESULT_FAIL)) {

                } else if (result.equals(RealTimeService.RESULT_NO_INTERNET)) {
                    ((BaseActivity) getActivity()).showToastError(getString(R.string.nointernet));
                }
                progUnLike.setVisibility(View.GONE);
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
                            Intent intentGetComment = new Intent(getActivity(), ParallaxService.class);
                            intentGetComment.setAction(ParallaxService.ACTION_GET_COMMENT_BY_DEAL_ID);
                            intentGetComment.putExtra(ParallaxService.EXTRA_DEAL_ID, dealId + "");
                            intentGetComment.putExtra(ParallaxService.EXTRA_PAGE, page + "");
                            getActivity().startService(intentGetComment);
                            lvReview.addFooterView(progessbarFooter);
                        }
                    }
                }
            }
        }
    }
}
