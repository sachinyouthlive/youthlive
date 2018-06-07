package com.yl.youthlive.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 5/19/2017.
 */
public class FollowProfileResponse {

    @SerializedName("follow")
    private String follow;

    @SerializedName("success")
    private int success;

    public int getSuccess() {
        return success;
    }

    public String getFollow() {
        return follow;
    }
}
