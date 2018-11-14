package com.app.youthlive.walletPOJO;

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
    @SerializedName("conversion_rate")
    @Expose
    private String conversion_rate;

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

    public String getConversion_rate() {
        return conversion_rate;
    }

    public void setConversion_rate(String conversion_rate) {
        this.conversion_rate = conversion_rate;
    }

}
