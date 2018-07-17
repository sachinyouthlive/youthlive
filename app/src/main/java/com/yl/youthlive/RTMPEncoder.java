package com.yl.youthlive;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;


public class RTMPEncoder extends AppCompatActivity {

    SurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rtmpencoder);


        surfaceView = findViewById(R.id.surfaceView);

        /*ConnectCheckerRtmp connectCheckerRtmp = new ConnectCheckerRtmp() {
            @Override
            public void onConnectionSuccessRtmp() {

            }

            @Override
            public void onConnectionFailedRtmp(String s) {

            }

            @Override
            public void onDisconnectRtmp() {

            }

            @Override
            public void onAuthErrorRtmp() {

            }

            @Override
            public void onAuthSuccessRtmp() {

            }
        };

        RtmpCamera1 rtmpCamera1 = new RtmpCamera1(surfaceView , connectCheckerRtmp);

        *//*Camera.Size resolution =
                rtmpCamera1.getResolutionsBack().get();
*//*
        if (rtmpCamera1.prepareAudio(128 * 1024 , 44100 * 1024, false, true, true) && rtmpCamera1.prepareVideo(640, 480, 30, 900 * 1024 , false, 90))
        {
            rtmpCamera1.startStream("rtmp://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com:1935/connection/3056");
        }
        else {
            *//**This device cant init encoders, this could be for 2 reasons: The encoder selected doesnt support any configuration setted or your device hasnt a H264 or AAC encoder (in this case you can see log error valid encoder not found)*//*
        }
*/
    }
}
