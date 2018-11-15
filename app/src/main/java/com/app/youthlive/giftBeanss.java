package com.app.youthlive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class giftBeanss {
    @SerializedName("giftId")
    @Expose
    private String giftId;
    @SerializedName("giftName")
    @Expose
    private String giftName;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("price")
    @Expose
    private String price;

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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
