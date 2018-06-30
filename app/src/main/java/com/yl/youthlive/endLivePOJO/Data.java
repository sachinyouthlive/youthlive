package com.yl.youthlive.endLivePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("liveId")
    @Expose
    private String liveId;
    @SerializedName("liveTag")
    @Expose
    private String liveTag;
    @SerializedName("thirdPartyKey")
    @Expose
    private String thirdPartyKey;
    @SerializedName("liveTime")
    @Expose
    private String liveTime;
    @SerializedName("viewers")
    @Expose
    private String viewers;

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public String getLiveTag() {
        return liveTag;
    }

    public void setLiveTag(String liveTag) {
        this.liveTag = liveTag;
    }

    public String getThirdPartyKey() {
        return thirdPartyKey;
    }

    public void setThirdPartyKey(String thirdPartyKey) {
        this.thirdPartyKey = thirdPartyKey;
    }

    public String getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(String liveTime) {
        this.liveTime = liveTime;
    }

    public String getViewers() {
        return viewers;
    }

    public void setViewers(String viewers) {
        this.viewers = viewers;
    }
}
