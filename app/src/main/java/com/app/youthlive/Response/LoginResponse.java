package com.app.youthlive.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 4/5/2017.
 */
public class LoginResponse implements Serializable {

    @SerializedName("message")
    private String message;
    @SerializedName("success")
    private int success;
    @SerializedName("id")
    private String id;
    @SerializedName("email_verify")
    private String email_verify;
    @SerializedName("profile_verify")
    private String profile_verify;
    @SerializedName("user_id")
    private String user_id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail_verify() {
        return email_verify;
    }

    public void setEmail_verify(String email_verify) {
        this.email_verify = email_verify;
    }

    public String getProfile_verify() {
        return profile_verify;
    }

    public void setProfile_verify(String profile_verify) {
        this.profile_verify = profile_verify;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
