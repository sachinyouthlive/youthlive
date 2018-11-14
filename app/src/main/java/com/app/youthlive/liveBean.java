package com.app.youthlive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TBX on 11/21/2017.
 */

public class liveBean {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("liveId")
    @Expose
    private String liveId;
    @SerializedName("userImage")
    @Expose
    private String userImage;
    @SerializedName("liveUsers")
    @Expose
    private String liveUsers;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("channel_url")
    @Expose
    private String channelUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getLiveUsers() {
        return liveUsers;
    }

    public void setLiveUsers(String liveUsers) {
        this.liveUsers = liveUsers;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

}
