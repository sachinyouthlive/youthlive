package com.app.youthlive.giftPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TBX on 12/13/2017.
 */

public class Datum {

    @SerializedName("giftId")
    @Expose
    private String giftId;
    @SerializedName("giftName")
    @Expose
    private String giftName;
    @SerializedName("giftIcon")
    @Expose
    private String giftIcon;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("purchaseQty")
    @Expose
    private String purchaseQty;

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

    public String getGiftIcon() {
        return giftIcon;
    }

    public void setGiftIcon(String giftIcon) {
        this.giftIcon = giftIcon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPurchaseQty() {
        return purchaseQty;
    }

    public void setPurchaseQty(String purchaseQty) {
        this.purchaseQty = purchaseQty;
    }


}
