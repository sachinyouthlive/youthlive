package com.yl.youthlive.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 6/10/2017.
 */
public class ShareResponse {
    @SerializedName("share_id")
    private int share_id;

    public int getShare_id() {
        return share_id;
    }

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    @SerializedName("success")
    private int success;


    public int getSuccess() {
        return success;
    }
}
