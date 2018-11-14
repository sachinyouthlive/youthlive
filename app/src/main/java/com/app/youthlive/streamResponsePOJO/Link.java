package com.app.youthlive.streamResponsePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mukul on 28/12/17.
 */

public class Link {

    @SerializedName("rel")
    @Expose
    private String rel;
    @SerializedName("method")
    @Expose
    private String method;
    @SerializedName("href")
    @Expose
    private String href;

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

}
