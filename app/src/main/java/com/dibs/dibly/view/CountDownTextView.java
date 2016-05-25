package com.dibs.dibly.view;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.dibs.dibly.activity.DealDetailActivity;
import com.dibs.dibly.staticfunction.StaticFunction;

/**
 * Created by USER on 7/20/2015.
 */
public class CountDownTextView extends TextView {

    private CountDownTimer timer;
    private Context context;

    public CountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setTimeCount(long endInMilliseconds, boolean isShowDay) {
        long current_time = System.currentTimeMillis();
        long remainTime = endInMilliseconds - current_time;

//            Log.e("timer", "start:" + startInMilliseconds + " - " + "end:" + endInMilliseconds + " - " + "current_time:" + current_time+ " - " + "remainTime:" + remainTime + " - " + "deal:" + listDeal.get(position).getDeal_id());
//        long l = 1000 * 3600 * 24;
        int day = (int) (remainTime / 86400000);
        long remainHours = remainTime - (day * 86400000);

        if (remainTime > 0) {
            setTimerCountDown(remainHours, day, endInMilliseconds, isShowDay);
        } else {
            setText("Expired ");
            setTextColor(Color.RED);
        }
//        setTimerCountDown(123456, 0, endInMilliseconds);
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void setTimerCountDown(long timerRemain, final int day, final long endInMilliseconds, final boolean isShowDay) {
        if (timer == null) {
            timer = new CountDownTimer(timerRemain, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int h = (int) millisUntilFinished / (3600000);
                    h = h < 0 ? 0 : h;
                    int m = (int) (millisUntilFinished - h * 3600000) / 60000;
                    m = m < 0 ? 0 : m;
                    int s = (int) (millisUntilFinished - h * 3600000 - m * 60000) / 1000;
                    s = s < 0 ? 0 : s;

                    String hour = h + "";
                    if (h < 10) {
                        hour = "0" + hour;
                    }
                    String min = m + "";
                    if (m < 10) {
                        min = "0" + min;
                    }
                    String sec = s + "";
                    if (s < 10) {
                        sec = "0" + sec;
                    }
                    if (isShowDay) {
                        if (day > 0) {
                            if (day == 1) {
                                setText(day + " day " + hour + ":" + min + ":" + sec + " ");
                            } else {
                                setText(day + " days " + hour + ":" + min + ":" + sec + " ");
                            }
                        } else {
                            setText(hour + ":" + min + ":" + sec + " ");
                        }
                    } else {
                        setText(hour + ":" + min + ":" + sec + " ");
                    }
                    setTextColor(Color.parseColor("#696969"));
//                    Log.e("time", getMeasuredWidth() + "-" + getWidth());
                }

                @Override
                public void onFinish() {
                    if (day > 0) {
                        setText("00:00:00 ");
                        setTextColor(Color.parseColor("#696969"));
//                        setTimeCount(endInMilliseconds, isShowDay);
                        StaticFunction.sendBroad(context, DealDetailActivity.RECEIVER_EXPRITED_TIME);
                    } else {
                        setText("Expired ");
                        setTextColor(Color.RED);
                        StaticFunction.sendBroad(context, DealDetailActivity.RECEIVER_EXPRITED_TIME);
                    }
                }
            };
            timer.start();
        } else {
            timer.cancel();
            timer = null;
            timer = new CountDownTimer(timerRemain, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int h = (int) millisUntilFinished / (3600000);
                    h = h < 0 ? 0 : h;
                    int m = (int) (millisUntilFinished - h * 3600000) / 60000;
                    m = m < 0 ? 0 : m;
                    int s = (int) (millisUntilFinished - h * 3600000 - m * 60000) / 1000;
                    s = s < 0 ? 0 : s;

                    String hour = h + "";
                    if (h < 10) {
                        hour = "0" + hour;
                    }
                    String min = m + "";
                    if (m < 10) {
                        min = "0" + min;
                    }
                    String sec = s + "";
                    if (s < 10) {
                        sec = "0" + sec;
                    }
                    if (isShowDay) {
                        if (day > 0) {
                            if (day == 1) {
                                setText(day + " day " + hour + ":" + min + ":" + sec + " ");
                            } else {
                                setText(day + " days " + hour + ":" + min + ":" + sec + " ");
                            }
                        } else {
                            setText(hour + ":" + min + ":" + sec + " ");
                        }
                    } else {
                        setText(hour + ":" + min + ":" + sec + " ");
                    }
                    setTextColor(Color.parseColor("#696969"));
//                    Log.e("time", getMeasuredWidth() + "-" + getWidth());
                }

                @Override
                public void onFinish() {
                    if (day > 0) {
                        setText("00:00:00 ");
                        setTextColor(Color.parseColor("#696969"));
//                        setTimeCount(endInMilliseconds, isShowDay);
                        StaticFunction.sendBroad(context, DealDetailActivity.RECEIVER_EXPRITED_TIME);
                    } else {
                        setText("Expired ");
                        setTextColor(Color.RED);
                        StaticFunction.sendBroad(context, DealDetailActivity.RECEIVER_EXPRITED_TIME);
                    }
                }
            };
            timer.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelTimer();
    }
}
