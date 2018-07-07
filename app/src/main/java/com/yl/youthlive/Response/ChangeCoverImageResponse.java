package com.yl.youthlive.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 4/19/2017.
 */
public class ChangeCoverImageResponse implements Serializable {

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
