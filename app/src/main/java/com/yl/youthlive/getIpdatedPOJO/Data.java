package com.yl.youthlive.getIpdatedPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by TBX on 11/27/2017.
 */

public class Data {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("beans")
    @Expose
    private String beans;
    @SerializedName("beans2")
    @Expose
    private String beans2;
    @SerializedName("diamond")
    @Expose
    private String diamond;
    @SerializedName("coin")
    @Expose
    private String coin;
    @SerializedName("stars")
    @Expose
    private String stars;
    @SerializedName("level")
    @Expose
    private String level;
    @SerializedName("videoURL")
    @Expose
    private String videoURL;
    @SerializedName("videoId")
    @Expose
    private String videoId;
    @SerializedName("uploadTime")
    @Expose
    private String uploadTime;
    @SerializedName("timelineId")
    @Expose
    private String timelineId;
    @SerializedName("timelineProfileImage")
    @Expose
    private String timelineProfileImage;
    @SerializedName("timelineName")
    @Expose
    private String timelineName;
    @SerializedName("commentCount")
    @Expose
    private String commentCount;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = null;
    @SerializedName("likesCount")
    @Expose
    private String likesCount;
    @SerializedName("likes")
    @Expose
    private List<Like> likes = null;
    @SerializedName("views")
    @Expose
    private List<View> views = null;
    @SerializedName("viewsCount")
    @Expose
    private String viewsCount;
    @SerializedName("gift")
    @Expose
    private List<Gift> gift = null;
    @SerializedName("follow")
    @Expose
    private String follow;
    @SerializedName("isConnection")
    @Expose
    private String isConnection;
    @SerializedName("connid")
    @Expose
    private String connid;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBeans() {
        return beans;
    }

    public void setBeans(String beans) {
        this.beans = beans;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getTimelineId() {
        return timelineId;
    }

    public void setTimelineId(String timelineId) {
        this.timelineId = timelineId;
    }

    public String getTimelineProfileImage() {
        return timelineProfileImage;
    }

    public void setTimelineProfileImage(String timelineProfileImage) {
        this.timelineProfileImage = timelineProfileImage;
    }

    public String getTimelineName() {
        return timelineName;
    }

    public void setTimelineName(String timelineName) {
        this.timelineName = timelineName;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<View> getViews() {
        return views;
    }

    public void setViews(List<View> views) {
        this.views = views;
    }

    public String getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(String viewsCount) {
        this.viewsCount = viewsCount;
    }

    public List<Gift> getGift() {
        return gift;
    }

    public void setGift(List<Gift> gift) {
        this.gift = gift;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getDiamond() {
        return diamond;
    }

    public void setDiamond(String diamond) {
        this.diamond = diamond;
    }

    public String getBeans2() {
        return beans2;
    }

    public void setBeans2(String beans2) {
        this.beans2 = beans2;
    }


    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getConnid() {
        return connid;
    }

    public String getIsConnection() {
        return isConnection;
    }

    public void setConnid(String connid) {
        this.connid = connid;
    }

    public void setIsConnection(String isConnection) {
        this.isConnection = isConnection;
    }
}
