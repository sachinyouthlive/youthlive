
package com.yl.youthlive.diamondpurchasehistoryPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Information {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("diamonds")
    @Expose
    private Integer diamonds;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("fullorderinfo")
    @Expose
    private String fullorderinfo;

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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(Integer diamonds) {
        this.diamonds = diamonds;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFullorderinfo() {
        return fullorderinfo;
    }

    public void setFullorderinfo(String fullorderinfo) {
        this.fullorderinfo = fullorderinfo;
    }

}
