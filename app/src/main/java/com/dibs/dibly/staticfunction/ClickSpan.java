package com.dibs.dibly.staticfunction;

import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by USER on 3/20/2015.
 */
public class ClickSpan extends ClickableSpan {

    private OnClickListener mListener;

    public ClickSpan(OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View widget) {
        if (mListener != null) mListener.onClick();
    }

    public interface OnClickListener {
        void onClick();
    }


}