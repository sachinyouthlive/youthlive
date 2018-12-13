package com.app.youthlive.giftHistoryPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Information {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("bId")
    @Expose
    private String bId;
    @SerializedName("pId")
    @Expose
    private String pId;
    @SerializedName("pName")
    @Expose
    private String pName;
    @SerializedName("liveId")
    @Expose
    private String liveId;
    @SerializedName("giftId")
    @Expose
    private String giftId;
    @SerializedName("giftQty")
    @Expose
    private String giftQty;
    @SerializedName("created_date")
    @Expose
    private String createdDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBId() {
        return bId;
    }

    public void setBId(String bId) {
        this.bId = bId;
    }

    public String getPId() {
        return pId;
    }

    public void setPId(String pId) {
        this.pId = pId;
    }

    public String getPName() {
        return pName;
    }

    public void setPName(String pName) {
        this.pName = pName;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGiftQty() {
        return giftQty;
    }

    public void setGiftQty(String giftQty) {
        this.giftQty = giftQty;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}
