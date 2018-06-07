package com.yl.youthlive.allMessagePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Datum {

    @SerializedName("chatId")
    @Expose
    private String chatId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("friendId")
    @Expose
    private String friendId;
    @SerializedName("friendName")
    @Expose
    private String friendName;
    @SerializedName("friendImage")
    @Expose
    private String friendImage;
    @SerializedName("lastMsg")
    @Expose
    private String lastMsg;
    @SerializedName("lastMsgTime")
    @Expose
    private String lastMsgTime;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendImage() {
        return friendImage;
    }

    public void setFriendImage(String friendImage) {
        this.friendImage = friendImage;
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
