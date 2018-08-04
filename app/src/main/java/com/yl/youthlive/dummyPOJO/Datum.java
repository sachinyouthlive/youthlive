package com.yl.youthlive.dummyPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("userImage")
    @Expose
    private String userImage;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("youthLiveId")
    @Expose
    private String youthLiveId;
    @SerializedName("level")
    @Expose
    private String level;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getYouthLiveId() {
        return youthLiveId;
    }

    public void setYouthLiveId(String youthLiveId) {
        this.youthLiveId = youthLiveId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
