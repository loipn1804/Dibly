package com.dibs.dibly.simpletoast;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.IconTextView;
import android.widget.Toast;

import com.dibs.dibly.R;
import com.dibs.dibly.staticfunction.StaticFunction;


/*
* Copyright (C) 2015 Pierry Borges
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
public class SimpleToast {

    private static LayoutInflater mInflater;
    private static Toast mToast;
    private static View mView;

    public static void ok(Context context, String msg) {
        mInflater = LayoutInflater.from(context);
        mView = mInflater.inflate(R.layout.toast_ok, null);
        StaticFunction.overrideFontsLight(context, mView);
        initSetButtonMsg(msg);
        if (mToast == null) {
            mToast = new Toast(context);
        }
        mToast.setView(mView);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void ok(Context context, String msg, String icon) {
        mInflater = LayoutInflater.from(context);
        mView = mInflater.inflate(R.layout.toast_ok_icon, null);
        StaticFunction.overrideFontsLight(context, mView);
        initSetButtonMsg(msg);
        IconTextView img = (IconTextView) mView.findViewById(R.id.img);
        img.setText(icon);
        img.setTextSize(20);
        if (mToast == null) {
            mToast = new Toast(context);
        }
        mToast.setView(mView);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void error(Context context, String msg) {
        mInflater = LayoutInflater.from(context);
        mView = mInflater.inflate(R.layout.toast_error, null);
        StaticFunction.overrideFontsLight(context, mView);
        initSetButtonMsg(msg);
        if (mToast == null) {
            mToast = new Toast(context);
        }
        mToast.setView(mView);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void error(Context context, String msg, String icon) {
        mInflater = LayoutInflater.from(context);
        mView = mInflater.inflate(R.layout.toast_error_icon, null);
        StaticFunction.overrideFontsLight(context, mView);
        initSetButtonMsg(msg);
        IconTextView img = (IconTextView) mView.findViewById(R.id.img);
        img.setText(icon);
        img.setTextSize(20);
        if (mToast == null) {
            mToast = new Toast(context);
        }
        mToast.setView(mView);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void info(Context context, String msg) {
        mInflater = LayoutInflater.from(context);
        mView = mInflater.inflate(R.layout.toast_info, null);
        StaticFunction.overrideFontsLight(context, mView);
        initSetButtonMsg(msg);
        if (mToast == null) {
            mToast = new Toast(context);
        }
        mToast.setView(mView);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void info(Context context, String msg, String icon) {
        mInflater = LayoutInflater.from(context);
        mView = mInflater.inflate(R.layout.toast_info_icon, null);
        StaticFunction.overrideFontsLight(context, mView);
        initSetButtonMsg(msg);
        ;
        IconTextView img = (IconTextView) mView.findViewById(R.id.img);
        img.setText(icon);
        img.setTextSize(20);
        if (mToast == null) {
            mToast = new Toast(context);
        }
        mToast.setView(mView);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void muted(Context context, String msg) {
        mInflater = LayoutInflater.from(context);
        mView = mInflater.inflate(R.layout.toast_muted, null);
        StaticFunction.overrideFontsLight(context, mView);
        initSetButtonMsg(msg);
        if (mToast == null) {
            mToast = new Toast(context);
        }
        mToast.setView(mView);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void muted(Context context, String msg, String icon) {
        mInflater = LayoutInflater.from(context);
        mView = mInflater.inflate(R.layout.toast_muted_icon, null);
        StaticFunction.overrideFontsLight(context, mView);
        initSetButtonMsg(msg);
        IconTextView img = (IconTextView) mView.findViewById(R.id.img);
        img.setText(icon);
        img.setTextSize(20);
        if (mToast == null) {
            mToast = new Toast(context);
        }
        mToast.setView(mView);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void warning(Context context, String msg) {
        mInflater = LayoutInflater.from(context);
        mView = mInflater.inflate(R.layout.toast_warning, null);
        StaticFunction.overrideFontsLight(context, mView);
        initSetButtonMsg(msg);
        if (mToast == null) {
            mToast = new Toast(context);
        }
        mToast.setView(mView);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void warning(Context context, String msg, String icon) {
        mInflater = LayoutInflater.from(context);
        mView = mInflater.inflate(R.layout.toast_warning_icon, null);
        StaticFunction.overrideFontsLight(context, mView);
        initSetButtonMsg(msg);
        IconTextView img = (IconTextView) mView.findViewById(R.id.img);
        img.setText(icon);
        img.setTextSize(20);
        if (mToast == null) {
            mToast = new Toast(context);
        }
        mToast.setView(mView);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    private static Button initSetButtonMsg(String msg) {
        Button mButton = (Button) mView.findViewById(R.id.button);
        mButton.setText(msg);
        return mButton;
    }
}
