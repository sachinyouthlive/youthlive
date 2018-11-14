package com.app.youthlive.blockPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TBX on 12/20/2017.
 */

public class Data {

    @SerializedName("userId")
    @Expose
    private Object userId;
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
    private String comments;
    @SerializedName("likesCount")
    @Expose
    private String likesCount;
    @SerializedName("likes")
    @Expose
    private String likes;
    @SerializedName("viewsCount")
    @Expose
    private String viewsCount;

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(String viewsCount) {
        this.viewsCount = viewsCount;
    }


}
