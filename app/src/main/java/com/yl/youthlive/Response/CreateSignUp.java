package com.yl.youthlive.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 4/3/2017.
 */
public class CreateSignUp {

    @SerializedName("message")
    private String message;

//    @SerializedName("success")
//    private String success;

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
    //    public String getSuccess() {
//        return success;
//    }
//    public void setSuccess(String success) {
//        this.success = success;
//    }
}
