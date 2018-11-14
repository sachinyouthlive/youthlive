package com.app.youthlive;

import com.google.gson.annotations.SerializedName;

class ServerResponse {

    // variable name should be same as in the json response from php
    @SerializedName("status")
    boolean status;
    @SerializedName("message")
    String message;

    String getMessage() {
        return message;
    }

    boolean getStatus() {
        return status;
    }

}
