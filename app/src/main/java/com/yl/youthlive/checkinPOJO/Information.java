
package com.yl.youthlive.checkinPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Information {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("day")
    @Expose
    private Integer day;
    @SerializedName("month")
    @Expose
    private Integer month;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("broadcast_duration")
    @Expose
    private Integer broadcastDuration;
    @SerializedName("bean")
    @Expose
    private Integer bean;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getBroadcastDuration() {
        return broadcastDuration;
    }

    public void setBroadcastDuration(Integer broadcastDuration) {
        this.broadcastDuration = broadcastDuration;
    }

    public Integer getBean() {
        return bean;
    }

    public void setBean(Integer bean) {
        this.bean = bean;
    }

}
