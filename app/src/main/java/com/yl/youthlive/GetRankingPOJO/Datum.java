package com.yl.youthlive.GetRankingPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by USER on 12/18/2017.
 */

public class Datum {


    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("userImage")
    @Expose
    private String userImage;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("beans")
    @Expose
    private String beans;
    @SerializedName("userName")
    @Expose
    private String userName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getBeans() {
        return beans;
    }

    public void setBeans(String beans) {
        this.beans = beans;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
