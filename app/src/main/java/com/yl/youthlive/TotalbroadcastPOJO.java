
package com.yl.youthlive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalbroadcastPOJO {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("total_monthly_broadcast")
    @Expose
    private Integer totalMonthlyBroadcast;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getTotalMonthlyBroadcast() {
        return totalMonthlyBroadcast;
    }

    public void setTotalMonthlyBroadcast(Integer totalMonthlyBroadcast) {
        this.totalMonthlyBroadcast = totalMonthlyBroadcast;
    }

}
