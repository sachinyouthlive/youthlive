package com.yl.youthlive.checkin;


public class Checkin_Model {

    private String id, userId, day, month, time, broadcast_duration, bean_earned;

    public Checkin_Model(String id, String userId, String day, String month, String time, String broadcast_duration, String bean_earned) {

        this.id = id;
        this.userId = userId;
        this.day = day;
        this.month = month;
        this.time = time;
        this.broadcast_duration = broadcast_duration;
        this.bean_earned = bean_earned;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBroadcast_duration() {
        return broadcast_duration;
    }

    public void setBroadcast_duration(String broadcast_duration) {
        this.broadcast_duration = broadcast_duration;
    }

    public String getBean_earned() {
        return bean_earned;
    }

    public void setBean_earned(String bean_earned) {
        this.bean_earned = bean_earned;
    }


}
