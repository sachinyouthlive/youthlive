package com.yl.youthlive;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;


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


    String TAG = "VideoPlayer";




    private StreamaxiaPlayer mStreamaxiaPlayer = new StreamaxiaPlayer();

    TextView stateText;


    CameraPreview thumbCamera1 , thumbCamera2;

    AspectRatioFrameLayout thumbFrame1 , thumbFrame2;

    SurfaceView thumbSurface1 , thumbSurface2;

    ViewPager pager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        liveId = getIntent().getStringExtra("uri");



        frameLayout = findViewById(R.id.video_frame);
        surfaceView = findViewById(R.id.surface_view);
        progress = findViewById(R.id.progressBar4);
        stateText = findViewById(R.id.textView3);

        thumbCamera1 = findViewById(R.id.thumb_camera1);
        thumbCamera2 = findViewById(R.id.thumb_camera2);
        thumbFrame1 = findViewById(R.id.thumb_frame1);
        thumbFrame2 = findViewById(R.id.thumb_frame2);
        thumbSurface1 = findViewById(R.id.thumb_surface1);
        thumbSurface2 = findViewById(R.id.thumb_surface2);

        pager = findViewById(R.id.pager);

        uri = Uri.parse("rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/live/" + liveId);
        //uri = Uri.parse("rtmp://192.168.1.103:1935/live/test");
        //uri = Uri.parse("rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/vod/sample.mp4");
        //uri = Uri.parse("rtmp://192.168.1.103:1935/vod/sample.mp4");

        Log.d("status" , "rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/live/" + liveId);

        mStreamaxiaPlayer.initStreamaxiaPlayer(surfaceView, frameLayout,
                stateText, this, this, uri);

        mStreamaxiaPlayer.play(uri , StreamaxiaPlayer.TYPE_RTMP);


        /*start.setOnClickListener(new View.OnClickListener() {
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
        });*/

        FragAdapter adapter = new FragAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);

    }

    @Override
    public void stateENDED() {

        Log.d("ssttaattuuss" , "ended");
    }

    @Override
    public void stateBUFFERING() {
        Log.d("ssttaattuuss" , "buffering");
    }

    @Override
    public void stateIDLE() {
        Log.d("ssttaattuuss" , "idle");
    }

    @Override
    public void statePREPARING() {
        Log.d("ssttaattuuss" , "preparing");
    }

    @Override
    public void stateREADY() {
        Log.d("ssttaattuuss" , "ready");
    }

    @Override
    public void stateUNKNOWN() {
        Log.d("ssttaattuuss" , "uunknown");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStreamaxiaPlayer.stop();
    }

    @Override
    public void onNetworkWeak() {

        Log.d(TAG , "network is weak");

    }

    @Override
    public void onNetworkResume() {
        Log.d(TAG , "network resumes");
    }

    @Override
    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {
        Log.d(TAG , "illegal argument: " + e.toString());
    }

    @Override
    public void onRtmpConnecting(String s) {
        Log.d(TAG , s);
    }

    @Override
    public void onRtmpConnected(String s) {
        Log.d(TAG , s);
    }

    @Override
    public void onRtmpVideoStreaming() {
        Log.d(TAG , "RTMP Video streaming");
    }

    @Override
    public void onRtmpAudioStreaming() {
        Log.d(TAG , "RTMP Audio streaming");
    }

    @Override
    public void onRtmpStopped() {
        Log.d(TAG , "RTMP stopped");
    }

    @Override
    public void onRtmpDisconnected() {
        Log.d(TAG , "RTMP disconnected");
    }

    @Override
    public void onRtmpVideoFpsChanged(double v) {
        Log.d(TAG , "fps changed" + String.valueOf(v));
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


    public class FragAdapter extends FragmentStatePagerAdapter
    {

        public FragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
            {
                PlayerFragment1 frag = new PlayerFragment1();
                Bundle b = new Bundle();
                b.putString("liveId" , liveId);
                frag.setArguments(b);
                return frag;
            }
            else
            {
                return new secondfrag();
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    }


}
