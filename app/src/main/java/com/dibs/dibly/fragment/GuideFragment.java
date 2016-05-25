package com.dibs.dibly.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dibs.dibly.R;
import com.dibs.dibly.base.BaseActivity;

/**
 * Created by USER on 6/30/2015.
 */
public class GuideFragment extends Fragment {

    public interface SwipePagerCbk {
        public void swipe();
    }

    private SwipePagerCbk swipePager;

    public void setCallBack(SwipePagerCbk swipePager) {
        this.swipePager = swipePager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final int position = getArguments().getInt("position");
        View view = null;
        if (position == 0) {
            view = inflater.inflate(R.layout.layout_guide_1, container, false);
        } else if (position == 1) {
            view = inflater.inflate(R.layout.layout_guide_2, container, false);
        } else if (position == 2) {
            view = inflater.inflate(R.layout.layout_guide_3, container, false);
        } else if (position == 3) {
            view = inflater.inflate(R.layout.layout_guide_4, container, false);
        } else if (position == 4) {
          //  view = inflater.inflate(R.layout.layout_guide_5, container, false);
        } else {
            getActivity().finish();
        }

        ((BaseActivity) getActivity()).overrideFontsLight(view);

        RelativeLayout rltGerStarted = (RelativeLayout) view.findViewById(R.id.rltGerStarted);

        rltGerStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 3) {
                    getActivity().finish();
                } else {
                    swipePager.swipe();
                }
            }
        });
        return view;
    }
}
