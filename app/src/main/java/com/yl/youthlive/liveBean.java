package com.yl.youthlive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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

}
