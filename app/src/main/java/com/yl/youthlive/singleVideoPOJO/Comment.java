package com.yl.youthlive.singleVideoPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TBX on 11/9/2017.
 */

public class Comment {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("userImage")
    @Expose
    private String userImage;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("time")
    @Expose
    private String time;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
