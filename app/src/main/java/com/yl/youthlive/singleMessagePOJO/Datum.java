package com.yl.youthlive.singleMessagePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class Datum {

    @SerializedName("senderId")
    @Expose
    private String senderId;
    @SerializedName("lastMsg")
    @Expose
    private String lastMsg;
    @SerializedName("lastMsgTime")
    @Expose
    private String lastMsgTime;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(String lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }

}
