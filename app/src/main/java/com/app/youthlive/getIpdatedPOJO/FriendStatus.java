package com.app.youthlive.getIpdatedPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TBX on 1/5/2018.
 */

public class FriendStatus {

    @SerializedName("block")
    @Expose
    private String block;
    @SerializedName("follow")
    @Expose
    private String follow;
    @SerializedName("friend")
    @Expose
    private String friend;

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

}
