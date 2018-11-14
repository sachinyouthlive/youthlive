package com.app.youthlive.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 5/3/2017.
 */
public class FollowUserProfileResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private int success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

}
