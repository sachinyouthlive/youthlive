package com.yl.youthlive.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 5/8/2017.
 */
public class PostWallDeleteResponse {

    @SerializedName("success")
    private int success;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
