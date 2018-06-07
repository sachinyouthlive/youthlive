package com.yl.youthlive.walletPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TBX on 12/13/2017.
 */

public class Data {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("beans")
    @Expose
    private String beans;
    @SerializedName("diamond")
    @Expose
    private String diamond;
    @SerializedName("coin")
    @Expose
    private String coin;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
