package com.app.youthlive.streamResponsePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mukul on 28/12/17.
 */

public class streamResponseBean {

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
