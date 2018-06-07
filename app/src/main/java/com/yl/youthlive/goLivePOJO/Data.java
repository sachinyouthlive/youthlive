package com.yl.youthlive.goLivePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TBX on 11/25/2017.
 */

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

}
