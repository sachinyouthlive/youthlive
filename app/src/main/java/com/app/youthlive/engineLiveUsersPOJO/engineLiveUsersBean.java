package com.app.youthlive.engineLiveUsersPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mukul on 08/02/18.
 */

public class engineLiveUsersBean {

    @SerializedName("serverName")
    @Expose
    private String serverName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("incomingStreams")
    @Expose
    private List<IncomingStream> incomingStreams = null;
    @SerializedName("outgoingStreams")
    @Expose
    private List<Object> outgoingStreams = null;
    @SerializedName("recorders")
    @Expose
    private List<Object> recorders = null;
    @SerializedName("streamGroups")
    @Expose
    private List<Object> streamGroups = null;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IncomingStream> getIncomingStreams() {
        return incomingStreams;
    }

    public void setIncomingStreams(List<IncomingStream> incomingStreams) {
        this.incomingStreams = incomingStreams;
    }

    public List<Object> getOutgoingStreams() {
        return outgoingStreams;
    }

    public void setOutgoingStreams(List<Object> outgoingStreams) {
        this.outgoingStreams = outgoingStreams;
    }

    public List<Object> getRecorders() {
        return recorders;
    }

    public void setRecorders(List<Object> recorders) {
        this.recorders = recorders;
    }

    public List<Object> getStreamGroups() {
        return streamGroups;
    }

    public void setStreamGroups(List<Object> streamGroups) {
        this.streamGroups = streamGroups;
    }

}
