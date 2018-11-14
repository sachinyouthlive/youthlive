package com.app.youthlive.addWalletPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TBX on 12/13/2017.
 */

public class Data {
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("beans")
    @Expose
    private String beans;
    @SerializedName("diamond")
    @Expose
    private String diamond;
    @SerializedName("coin")
    @Expose
    private String coin;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBeans() {
        return beans;
    }

    public void setBeans(String beans) {
        this.beans = beans;
    }

    public String getDiamond() {
        return diamond;
    }

    public void setDiamond(String diamond) {
        this.diamond = diamond;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }
}
