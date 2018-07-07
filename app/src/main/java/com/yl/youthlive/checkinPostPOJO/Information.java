package com.yl.youthlive.checkinPostPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Information {

    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("day")
    @Expose
    private Integer day;
    @SerializedName("month")
    @Expose
    private Integer month;
    @SerializedName("broadcast_duration")
    @Expose
    private Integer broadcastDuration;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getBroadcastDuration() {
        return broadcastDuration;
    }

    public void setBroadcastDuration(Integer broadcastDuration) {
        this.broadcastDuration = broadcastDuration;
    }

}
