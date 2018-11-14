package com.app.youthlive.getIpdatedPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TBX on 12/13/2017.
 */

public class Gift {

    @SerializedName("senbdId")
    @Expose
    private String senbdId;
    @SerializedName("giftId")
    @Expose
    private String giftId;
    @SerializedName("giftName")
    @Expose
    private String giftName;
    @SerializedName("icon")
    @Expose
    private String icon;

    public String getSenbdId() {
        return senbdId;
    }

    public void setSenbdId(String senbdId) {
        this.senbdId = senbdId;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}
