package com.yl.youthlive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.nio.ByteBuffer;

import veg.mediaplayer.sdk.MediaPlayer;
import veg.mediaplayer.sdk.MediaPlayerConfig;

public class Player2 extends AppCompatActivity implements MediaPlayer.MediaPlayerCallback {

    MediaPlayer player;
    MediaPlayerConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player2);

        player = (MediaPlayer)findViewById(R.id.player);

        config = new MediaPlayerConfig();

        Toast.makeText(this, "Player2.java", Toast.LENGTH_SHORT).show();
        String ur = "rtsp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/live/" + getIntent().getStringExtra("uri");
           // Log.d("rtmpserverpath",ur);

        config.setConnectionUrl(ur);

        config.setConnectionNetworkProtocol(-1);
        config.setConnectionDetectionTime(1000);
        config.setConnectionBufferingTime(99);
        config.setDecodingType(1);
        config.setRendererType(1);
        config.setSynchroEnable(1);
        config.setSynchroNeedDropVideoFrames(1);
        config.setEnableAspectRatio(2);
        config.setDataReceiveTimeout(30000);
        config.setNumberOfCPUCores(0);


        player.Open(config , this);


    }

    @Override
    public int Status(int i) {
        MediaPlayer.PlayerNotifyCodes status = MediaPlayer.PlayerNotifyCodes.forValue(i);
        /*if (handler == null || status == null)
            return 0;*/

        Log.e("VEG", "From Native listitem status: " + i);
        switch (MediaPlayer.PlayerNotifyCodes.forValue(i))
        {
            default:
                Message msg = new Message();
                /*msg.obj = status;
                //handler.removeMessages(mOldMsg);
                //mOldMsg = msg.what;
                handler.sendMessage(msg);*/
        }
        return 0;
    }

    @Override
    public int OnReceiveData(ByteBuffer byteBuffer, int i, long l) {
        Log.e("VEG", "Form Native listitem OnReceiveData: size: " + i + ", pts: " + l);
        return 0;
    }
}
