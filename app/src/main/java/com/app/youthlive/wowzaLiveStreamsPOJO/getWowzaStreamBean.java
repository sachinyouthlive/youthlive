package com.app.youthlive.wowzaLiveStreamsPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by TBX on 1/13/2018.
 */

public class getWowzaStreamBean {

    @SerializedName("live_streams")
    @Expose
    private List<LiveStream> liveStreams = null;

    public List<LiveStream> getLiveStreams() {
        return liveStreams;
    }

    public void setLiveStreams(List<LiveStream> liveStreams) {
        this.liveStreams = liveStreams;
    }


}
