package com.yl.youthlive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TBX on 11/21/2017.
 */

public class liveBean {

    @SerializedName("applicationInstance")
    @Expose
    private String applicationInstance;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("sourceIp")
    @Expose
    private String sourceIp;
    @SerializedName("isRecordingSet")
    @Expose
    private Boolean isRecordingSet;
    @SerializedName("isStreamManagerStream")
    @Expose
    private Boolean isStreamManagerStream;
    @SerializedName("isPublishedToVOD")
    @Expose
    private Boolean isPublishedToVOD;
    @SerializedName("isConnected")
    @Expose
    private Boolean isConnected;
    @SerializedName("isPTZEnabled")
    @Expose
    private Boolean isPTZEnabled;
    @SerializedName("ptzPollingInterval")
    @Expose
    private Integer ptzPollingInterval;
    @SerializedName("ptzPollingIntervalMinimum")
    @Expose
    private Integer ptzPollingIntervalMinimum;
    @SerializedName("userId")
    @Expose
    private Object userId;
    @SerializedName("liveId")
    @Expose
    private Object liveId;
    @SerializedName("userImage")
    @Expose
    private String userImage;
    @SerializedName("liveUsers")
    @Expose
    private String liveUsers;

    public String getApplicationInstance() {
        return applicationInstance;
    }

    public void setApplicationInstance(String applicationInstance) {
        this.applicationInstance = applicationInstance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public Boolean getIsRecordingSet() {
        return isRecordingSet;
    }

    public void setIsRecordingSet(Boolean isRecordingSet) {
        this.isRecordingSet = isRecordingSet;
    }

    public Boolean getIsStreamManagerStream() {
        return isStreamManagerStream;
    }

    public void setIsStreamManagerStream(Boolean isStreamManagerStream) {
        this.isStreamManagerStream = isStreamManagerStream;
    }

    public Boolean getIsPublishedToVOD() {
        return isPublishedToVOD;
    }

    public void setIsPublishedToVOD(Boolean isPublishedToVOD) {
        this.isPublishedToVOD = isPublishedToVOD;
    }

    public Boolean getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(Boolean isConnected) {
        this.isConnected = isConnected;
    }

    public Boolean getIsPTZEnabled() {
        return isPTZEnabled;
    }

    public void setIsPTZEnabled(Boolean isPTZEnabled) {
        this.isPTZEnabled = isPTZEnabled;
    }

    public Integer getPtzPollingInterval() {
        return ptzPollingInterval;
    }

    public void setPtzPollingInterval(Integer ptzPollingInterval) {
        this.ptzPollingInterval = ptzPollingInterval;
    }

    public Integer getPtzPollingIntervalMinimum() {
        return ptzPollingIntervalMinimum;
    }

    public void setPtzPollingIntervalMinimum(Integer ptzPollingIntervalMinimum) {
        this.ptzPollingIntervalMinimum = ptzPollingIntervalMinimum;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public Object getLiveId() {
        return liveId;
    }

    public void setLiveId(Object liveId) {
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

}
