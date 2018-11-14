package com.app.youthlive.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 5/19/2017.
 */
public class LikeResponse {

    @SerializedName("success")
    private int success;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
