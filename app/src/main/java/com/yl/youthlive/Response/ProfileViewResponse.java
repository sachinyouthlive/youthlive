package com.yl.youthlive.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 4/28/2017.
 */
public class ProfileViewResponse {

    @SerializedName("privacy_status")
    private String privacy_status;

    @SerializedName("success")
    private int success;

    public int getSuccess() {
        return success;
    }
    public void setSuccess(int success) {
        this.success = success;
    }

    public String getPrivacy_status() {
        return privacy_status;
    }
    public void setPrivacy_status(String privacy_status) {
        this.privacy_status = privacy_status;
    }
}
