package com.dibs.dibly.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dibs.dibly.R;
import com.dibs.dibly.service.MixPanelService;
import com.dibs.dibly.simpletoast.SimpleToast;
import com.dibs.dibly.staticfunction.ClickSpan;
import com.dibs.dibly.staticfunction.StaticFunction;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;


/**
 * Created by USER on 6/29/2015.
 */
public class BaseActivity extends AppCompatActivity {

//    public Typeface typefacePN_Bold = Typeface.createFromAsset(getAssets(), "fonts/ProximaNova-Bold.otf");
//    public Typeface typefacePN_Light = Typeface.createFromAsset(getAssets(), "fonts/ProximaNova-Light.otf");

    private Dialog dialog = null;
    private ProgressDialog progress_dialog = null;
    protected Toolbar toolbar;

    public MixpanelAPI mixpanelAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String projectToken = StaticFunction.MixPanelToken;
        mixpanelAPI = MixpanelAPI.getInstance(this, projectToken);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("MixPanel", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isRunning", true);
        editor.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences("MixPanel", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isRunning", false);
        editor.commit();
        MixPanelService.last_page = this.getClass().getSimpleName();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void trackMixPanel(String name, JSONObject object) {
        mixpanelAPI.track(name, object);
    }

    public void startDurationMixPanel(String name) {
        mixpanelAPI.timeEvent(name);
    }

    public void endDurationMixPanel(String name, JSONObject object) {
        if (object == null) {
            mixpanelAPI.track(name);
        } else {
            mixpanelAPI.track(name, object);
        }
    }

    public void showProgressDialog() {
        showCustomProgressDialog();
//        if (progress_dialog == null) {
//            progress_dialog = new ProgressDialog(BaseActivity.this);
//        }
//
//        if (!progress_dialog.isShowing()) {
//            progress_dialog.setMessage("Progressing ... ");
//            progress_dialog.setCancelable(true);
//            progress_dialog.show();
//        }
    }

    protected void initialToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setTitleTextColor(Color.WHITE);
        }
    }

    public void hideProgressDialog() {
       /* if (progress_dialog != null && progress_dialog.isShowing()) {
            progress_dialog.dismiss();
        }*/
        hideCustomProgressDialog();
    }

    public void showToastError(String message) {
        SimpleToast.error(BaseActivity.this, message);
    }

    public void showToastInfo(String message) {
        SimpleToast.info(BaseActivity.this, message);
    }

    public void showToastOk(String message) {
        SimpleToast.ok(BaseActivity.this, message);
    }

    public void showCustomProgressDialog() {
        if (dialog == null) {
            dialog = new Dialog(BaseActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.popup_progress);
            overrideFontsLight(dialog.findViewById(R.id.root));
            dialog.show();
        }
    }

    public static void showCustomProgressDialog(Context context) {
        Dialog dialog;

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.popup_progress);
        dialog.show();

    }

    public void hideCustomProgressDialog() {

        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = null;
        }

    }

    public void showPopupPrompt(String message) {
        // custom dialog
        final Dialog dialog = new Dialog(BaseActivity.this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.pupup_prompt);
        overrideFontsLight(dialog.findViewById(R.id.root));

        LinearLayout lnlOk = (LinearLayout) dialog.findViewById(R.id.lnlOk);
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        txtMessage.setText(message);

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void clickify(TextView view, final String clickableText, final ClickSpan.OnClickListener listener, int color) {

        CharSequence text = view.getText();
        String string = text.toString();
        ClickSpan span = new ClickSpan(listener);

        int start = string.indexOf(clickableText);
        int end = start + clickableText.length();
        if (start == -1) return;

        if (text instanceof Spannable) {
            ((Spannable) text).setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ((Spannable) text).setSpan(new ForegroundColorSpan(getResources().getColor(color)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ((Spannable) text).setSpan(new NoUnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            SpannableString s = SpannableString.valueOf(text);
            s.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new ForegroundColorSpan(getResources().getColor(color)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new NoUnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            view.setText(s);
        }

        MovementMethod m = view.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            view.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    public SpannableString getTitleText(String caption, String merchant) {
        SpannableString text2 = new SpannableString(caption + merchant + "?");
        text2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.txt_black_68)), 0,
                caption.length(), 0);
        text2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange_main)), caption.length(),
                text2.length() - 1, 0);
        return text2;
    }


    public static SpannableString getTitleText(Context context, String caption, String merchant) {
        SpannableString text2 = new SpannableString(caption + merchant + "?");
        text2.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.txt_black_68)), 0,
                caption.length(), 0);
        text2.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.orange_main)), caption.length(),
                text2.length() - 1, 0);
        return text2;
    }

    public class NoUnderlineSpan extends UnderlineSpan {
        public NoUnderlineSpan() {
        }

        public NoUnderlineSpan(Parcel src) {
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }

    public void overrideFontsLight(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsLight(child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(StaticFunction.light(this));
            }
        } catch (Exception e) {
        }
    }

    public void overrideFontsLightIt(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsLightIt(child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(StaticFunction.light_it(this));
            }
        } catch (Exception e) {
        }
    }

    public void overrideFontsBold(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsBold(child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(StaticFunction.bold(this));
            }
        } catch (Exception e) {
        }
    }

    public void overrideFontsBoldIt(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsBoldIt(child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(StaticFunction.bold_it(this));
            }
        } catch (Exception e) {
        }
    }


    protected String parseTimeMillisecondToXMinuteAndXSecond(long millis) {

        return String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }
}
