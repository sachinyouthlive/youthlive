package com.app.youthlive.startStreamPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mukul on 28/12/17.
 */

public class startStreamBean {

    @SerializedName("live_stream")
    @Expose
    private LiveStream liveStream;

    public LiveStream getLiveStream() {
        return liveStream;
    }

    public void setLiveStream(LiveStream liveStream) {
        this.liveStream = liveStream;
    }

}
