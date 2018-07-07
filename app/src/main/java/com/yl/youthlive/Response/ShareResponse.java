package com.yl.youthlive.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 6/10/2017.
 */
public class ShareResponse {
    @SerializedName("share_id")
    private int share_id;
    @SerializedName("message")
    private String message;
    @SerializedName("success")
    private int success;

    public int getShare_id() {
        return share_id;
    }

    public String getMessage() {
        return message;
    }

    public int getSuccess() {
        return success;
    }
}
