package com.dibs.dibly.consts;

/**
 * Created by USER on 9/9/2015.
 */
public class TimerDealObj {
    private int percent;
    private int day;
    private long remainTime;
    private long remainHours;
    private String distance;
    private long endInMilliseconds;
    private long totalTime;

    public TimerDealObj(int percent, int day, long remainTime, long remainHours, String distance, long endInMilliseconds) {
        this.percent = percent;
        this.day = day;
        this.remainTime = remainTime;
        this.remainHours = remainHours;
        this.distance = distance;
        this.endInMilliseconds = endInMilliseconds;
    }

    public int getPercent() {
        return percent;
    }

    public int getDay() {
        return day;
    }

    public long getRemainTime() {
        return remainTime;
    }

    public long getRemainHours() {
        return remainHours;
    }

    public String getDistance() {
        return distance;
    }

    public long getEndInMilliseconds() {
        return endInMilliseconds;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }
}
