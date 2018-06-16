package com.yl.youthlive;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.streamaxia.android.CameraPreview;
import com.streamaxia.android.StreamaxiaPublisher;
import com.streamaxia.android.handlers.EncoderHandler;
import com.streamaxia.android.handlers.RecordHandler;
import com.streamaxia.android.handlers.RtmpHandler;
import com.streamaxia.android.utils.Size;
import com.streamaxia.player.StreamaxiaPlayer;
import com.streamaxia.player.listener.StreamaxiaPlayerState;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;

public class VideoPlayer extends AppCompatActivity implements StreamaxiaPlayerState, EncoderHandler.EncodeListener, RtmpHandler.RtmpListener, RecordHandler.RecordListener {

    String liveId;
    AspectRatioFrameLayout frameLayout;
    SurfaceView surfaceView;
    ProgressBar progress;

    private Uri uri;

    CameraPreview cameraPreview;
    ImageButton start;

    Toast toast;

    private StreamaxiaPlayer mStreamaxiaPlayer = new StreamaxiaPlayer();

    TextView stateText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        liveId = getIntent().getStringExtra("uri");

        toast = Toast.makeText(this , null , Toast.LENGTH_SHORT);

        frameLayout = findViewById(R.id.video_frame);
        surfaceView = findViewById(R.id.surface_view);
        progress = findViewById(R.id.progressBar4);
        stateText = findViewById(R.id.textView3);

        cameraPreview = findViewById(R.id.view2);
        start = findViewById(R.id.imageButton);

        uri = Uri.parse("rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/live/" + liveId);
        mStreamaxiaPlayer.initStreamaxiaPlayer(surfaceView, frameLayout,
                stateText, this, this, uri);


        mStreamaxiaPlayer.play(uri, StreamaxiaPlayer.TYPE_RTMP);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StreamaxiaPublisher mPublisher = new StreamaxiaPublisher(cameraPreview, VideoPlayer.this);

                mPublisher.setEncoderHandler(new EncoderHandler(VideoPlayer.this));
                mPublisher.setRtmpHandler(new RtmpHandler(VideoPlayer.this));
                mPublisher.setRecordEventHandler(new RecordHandler(VideoPlayer.this));
                cameraPreview.startCamera();

                List<Size> sizes = mPublisher.getSupportedPictureSizes(getResources().getConfiguration().orientation);
                Size resolution = sizes.get(0);
                mPublisher.setVideoOutputResolution(   176, 144, VideoPlayer.this.getResources().getConfiguration().orientation);

                mPublisher.setVideoBitRate(56000);

                mPublisher.startPublish("rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/videochat/demo");


            }
        });

    }

    @Override
    public void stateENDED() {

        toast.setText("ended");
        toast.show();
    }

    @Override
    public void stateBUFFERING() {
        toast.setText("buffering");
        toast.show();
    }

    @Override
    public void stateIDLE() {
        toast.setText("idle");
        toast.show();
    }

    @Override
    public void statePREPARING() {
        toast.setText("preparing");
        toast.show();
    }

    @Override
    public void stateREADY() {
        toast.setText("ready");
        toast.show();
    }

    @Override
    public void stateUNKNOWN() {
        toast.setText("unknown");
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStreamaxiaPlayer.stop();
    }

    @Override
    public void onNetworkWeak() {

    }

    @Override
    public void onNetworkResume() {

    }

    @Override
    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {

    }

    @Override
    public void onRtmpConnecting(String s) {

    }

    @Override
    public void onRtmpConnected(String s) {

    }

    @Override
    public void onRtmpVideoStreaming() {

    }

    @Override
    public void onRtmpAudioStreaming() {

    }

    @Override
    public void onRtmpStopped() {

    }

    @Override
    public void onRtmpDisconnected() {

    }

    @Override
    public void onRtmpVideoFpsChanged(double v) {

    }

    @Override
    public void onRtmpVideoBitrateChanged(double v) {

    }

    @Override
    public void onRtmpAudioBitrateChanged(double v) {

    }

    @Override
    public void onRtmpSocketException(SocketException e) {

    }

    @Override
    public void onRtmpIOException(IOException e) {

    }

    @Override
    public void onRtmpIllegalArgumentException(IllegalArgumentException e) {

    }

    @Override
    public void onRtmpIllegalStateException(IllegalStateException e) {

    }

    @Override
    public void onRtmpAuthenticationg(String s) {

    }

    @Override
    public void onRecordPause() {

    }

    @Override
    public void onRecordResume() {

    }

    @Override
    public void onRecordStarted(String s) {

    }

    @Override
    public void onRecordFinished(String s) {

    }

    @Override
    public void onRecordIllegalArgumentException(IllegalArgumentException e) {

    }

    @Override
    public void onRecordIOException(IOException e) {

    }



}
