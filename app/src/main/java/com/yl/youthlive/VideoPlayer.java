package com.yl.youthlive;

import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.graphics.Region;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.otaliastudios.cameraview.AspectRatio;
import com.streamaxia.android.CameraPreview;
import com.streamaxia.android.StreamaxiaPublisher;
import com.streamaxia.android.handlers.EncoderHandler;
import com.streamaxia.android.handlers.RecordHandler;
import com.streamaxia.android.handlers.RtmpHandler;
import com.streamaxia.android.utils.ScalingMode;
import com.streamaxia.android.utils.Size;
import com.streamaxia.player.StreamaxiaPlayer;
import com.streamaxia.player.listener.StreamaxiaPlayerState;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.acceptRejectPOJO.acceptRejectBean;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class VideoPlayer extends AppCompatActivity implements StreamaxiaPlayerState, EncoderHandler.EncodeListener, RtmpHandler.RtmpListener, RecordHandler.RecordListener {

    String liveId;
    AspectRatioFrameLayout frameLayout;
    SurfaceView surfaceView;
    ProgressBar progress;

    private Uri uri;


    String TAG = "VideoPlayer";


    String connId;


    private StreamaxiaPlayer mStreamaxiaPlayer = new StreamaxiaPlayer();

    TextView stateText;


    CameraPreview thumbCamera1, thumbCamera2;

    AspectRatioFrameLayout thumbFrame1, thumbFrame2;

    SurfaceView thumbSurface1, thumbSurface2;

    ViewPager pager;

    RelativeLayout player_camera_layout1;

    RelativeLayout container;
    ImageView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        liveId = getIntent().getStringExtra("uri");

        loading = findViewById(R.id.loading);

        frameLayout = findViewById(R.id.video_frame);
        surfaceView = findViewById(R.id.surface_view);
        progress = findViewById(R.id.progressBar4);
        stateText = findViewById(R.id.textView3);

        container = findViewById(R.id.view2);

        player_camera_layout1 = findViewById(R.id.thumb_camera_layout1);

        thumbCamera1 = findViewById(R.id.thumb_camera1);
        thumbCamera2 = findViewById(R.id.thumb_camera2);
        thumbFrame1 = findViewById(R.id.thumb_frame1);
        thumbFrame2 = findViewById(R.id.thumb_frame2);
        thumbSurface1 = findViewById(R.id.thumb_surface1);
        thumbSurface2 = findViewById(R.id.thumb_surface2);

        pager = findViewById(R.id.pager);


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float ratio = ((float)metrics.heightPixels / (float)metrics.widthPixels
        );

        frameLayout.setAspectRatio(ratio);


        surfaceView.setZOrderOnTop(false);

        SurfaceHolder holder = surfaceView.getHolder();
        holder.setFormat(PixelFormat.UNKNOWN);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        surfaceView.setZ(6);
        container.setZ(12);

        container.getParent().requestTransparentRegion(surfaceView);

        uri = Uri.parse("rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/videochat/" + liveId);
        //uri = Uri.parse("rtmp://192.168.1.103:1935/live/test");
        //uri = Uri.parse("rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/vod/sample.mp4");
        //uri = Uri.parse("rtmp://192.168.1.103:1935/vod/sample.mp4");

        Log.d("status", "rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/live/" + liveId);

        mStreamaxiaPlayer.initStreamaxiaPlayer(surfaceView, frameLayout,
                stateText, this, this, uri);

        mStreamaxiaPlayer.play(uri, StreamaxiaPlayer.TYPE_RTMP);




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

        Log.d("ssttaattuuss", "ended");
    }

    @Override
    public void stateBUFFERING() {
        Log.d("ssttaattuuss", "buffering");
    }

    @Override
    public void stateIDLE() {
        Log.d("ssttaattuuss", "idle");
    }

    @Override
    public void statePREPARING() {
        loading.setVisibility(View.VISIBLE);
        Log.d("ssttaattuuss", "preparing");
    }

    @Override
    public void stateREADY() {
        Log.d("ssttaattuuss", "ready");
        loading.setVisibility(View.GONE);
    }

    @Override
    public void stateUNKNOWN() {
        Log.d("ssttaattuuss", "uunknown");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStreamaxiaPlayer.stop();

        try {
            mPublisher.stopPublish();
            mPublisher.stopRecord();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onNetworkWeak() {


    }

    @Override
    public void onNetworkResume() {
        Log.d(TAG, "network resumes");
    }

    @Override
    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {
        Log.d(TAG, "illegal argument: " + e.toString());
    }

    @Override
    public void onRtmpConnecting(String s) {
        Log.d(TAG, s);
    }

    @Override
    public void onRtmpConnected(String s) {
        Log.d(TAG, s);

        progress.setVisibility(View.VISIBLE);

        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);

        Call<acceptRejectBean> call1 = cr.acceptReject(connId, liveId + b.userId, "2");
        call1.enqueue(new Callback<acceptRejectBean>() {
            @Override
            public void onResponse(Call<acceptRejectBean> call, Response<acceptRejectBean> response) {

                try {


                    //cameraLayout1.setVisibility(View.VISIBLE);

/*
                            goCoderBroadcastConfig.setHostAddress("ec2-13-58-47-70.us-east-2.compute.amazonaws.com");
                            goCoderBroadcastConfig.setPortNumber(1935);
                            goCoderBroadcastConfig.setApplicationName("live");
                            goCoderBroadcastConfig.setStreamName(b.userId + "-" + liveId);
                            goCoderBroadcastConfig = new WZBroadcastConfig(WZMediaConfig.FRAME_SIZE_640x480);
                            // Set the bitrate to 4000 Kbps
                            goCoderBroadcastConfig.setVideoBitRate(1200);

                            //Toast.makeText(MyApp.getContext(), goCoderBroadcastConfig.getConnectionURL().toString(), Toast.LENGTH_SHORT).show();

                            WZStreamingError configValidationError = goCoderBroadcastConfig.validateForBroadcast();

                            //if (configValidationError != null) {
                            //Toast.makeText(LiveScreen.this, configValidationError.getErrorDescription(), Toast.LENGTH_LONG).show();
                            //} else if (goCoderBroadcaster.getStatus().isRunning()) {
                            // Stop the broadcast that is currently running
                            //    goCoderBroadcaster.endBroadcast(PlayerActivity.this);
                            //} else {
                            // Start streaming
                            goCoderBroadcaster.startBroadcast(goCoderBroadcastConfig, player_firstNew.this);
                            //}

*/


                } catch (Exception e) {
                    e.printStackTrace();
                }


                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<acceptRejectBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });


    }

    @Override
    public void onRtmpVideoStreaming() {
        Log.d("recordloistener" , "video");
    }

    @Override
    public void onRtmpAudioStreaming() {
        Log.d("recordloistener" , "audio");
    }

    @Override
    public void onRtmpStopped() {
        Log.d("recordloistener", "RTMP stopped");
    }

    @Override
    public void onRtmpDisconnected() {
        Log.d("recordloistener", "RTMP disconnected");
    }

    @Override
    public void onRtmpVideoFpsChanged(double v) {
        Log.d(TAG, "fps changed" + String.valueOf(v));
    }

    @Override
    public void onRtmpVideoBitrateChanged(double v) {

    }

    @Override
    public void onRtmpAudioBitrateChanged(double v) {

    }

    @Override
    public void onRtmpSocketException(SocketException e) {
        Log.d("recordloistener" , e.toString());
    }

    @Override
    public void onRtmpIOException(IOException e) {
        Log.d("recordloistener" , e.toString());
    }

    @Override
    public void onRtmpIllegalArgumentException(IllegalArgumentException e) {
        Log.d("recordloistener" , e.toString());
    }

    @Override
    public void onRtmpIllegalStateException(IllegalStateException e) {
        Log.d("recordloistener" , e.toString());
    }

    @Override
    public void onRtmpAuthenticationg(String s) {
        Log.d("recordloistener" , s);
    }

    @Override
    public void onRecordPause() {

        Log.d("recordloistener" , "paused");

    }

    @Override
    public void onRecordResume() {
        Log.d("recordloistener" , "resume");
    }

    @Override
    public void onRecordStarted(String s) {
        Log.d("recordloistener" , s);
    }

    @Override
    public void onRecordFinished(String s) {
        Log.d("recordloistener" , s);
    }

    @Override
    public void onRecordIllegalArgumentException(IllegalArgumentException e) {
        Log.d("recordloistener" , e.toString());
    }

    @Override
    public void onRecordIOException(IOException e) {
        Log.d("recordloistener" , e.toString());
    }


    public class FragAdapter extends FragmentStatePagerAdapter {

        public FragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                PlayerFragment1 frag = new PlayerFragment1();
                Bundle b = new Bundle();
                b.putString("liveId", liveId);
                frag.setArguments(b);
                return frag;
            } else {
                return new secondfrag();
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    }

    StreamaxiaPublisher mPublisher;

    public void startThumbCamera1(String connId) {

        thumbCamera1.setVisibility(View.VISIBLE);

        this.connId = connId;

        final bean b = (bean) getApplicationContext();

        mPublisher = new StreamaxiaPublisher(thumbCamera1, VideoPlayer.this);

        mPublisher.setEncoderHandler(new EncoderHandler(VideoPlayer.this));
        mPublisher.setRtmpHandler(new RtmpHandler(VideoPlayer.this));
        mPublisher.setRecordEventHandler(new RecordHandler(VideoPlayer.this));
        thumbCamera1.startCamera();
        mPublisher.setVideoOutputResolution(160, 120, getResources().getConfiguration().orientation);

        mPublisher.startPublish("rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/live/" + liveId + b.userId);


        player_camera_layout1.setVisibility(View.VISIBLE);
    }

    public void endThumbCamera1()
    {

        try {
            mPublisher.stopPublish();
            mPublisher.stopRecord();
            thumbCamera1.stopCamera();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        player_camera_layout1.setVisibility(View.GONE);
        //thumbCamera1.setVisibility(View.INVISIBLE);



        //surfaceView.setVisibility(View.GONE);
        //surfaceView.setVisibility(View.VISIBLE);

        surfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        surfaceView.getHolder().setFormat(PixelFormat.OPAQUE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //cameraPreview.startCamera();
        try {
            //mPublisher.resumeRecord();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            mPublisher.pauseRecord();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //cameraPreview.stopCamera();

    }


}
