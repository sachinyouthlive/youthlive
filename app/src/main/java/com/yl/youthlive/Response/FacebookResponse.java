package com.yl.youthlive.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 4/21/2017.
 */
public class FacebookResponse implements Serializable {

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("success")
    private int success;

    @SerializedName("profile_verify")
    private String profile_verify;


    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProfile_verify() {
        return profile_verify;
    }

    public void setProfile_verify(String profile_verify) {
        this.profile_verify = profile_verify;
    }

}
