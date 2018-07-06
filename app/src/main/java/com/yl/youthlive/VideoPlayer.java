package com.yl.youthlive;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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


import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.otaliastudios.cameraview.AspectRatio;
import com.streamaxia.android.CameraPreview;
import com.streamaxia.android.StreamaxiaPublisher;
import com.streamaxia.android.handlers.EncoderHandler;
import com.streamaxia.android.handlers.RecordHandler;
import com.streamaxia.android.handlers.RtmpHandler;
import com.streamaxia.android.utils.ScalingMode;
import com.streamaxia.android.utils.Size;

import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.acceptRejectPOJO.acceptRejectBean;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;

import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class VideoPlayer extends AppCompatActivity implements EncoderHandler.EncodeListener, RtmpHandler.RtmpListener, RecordHandler.RecordListener, Player.EventListener {

    String liveId;


    ProgressBar progress;

    private Uri uri;


    String TAG = "VideoPlayer";




    String connId;




    TextView stateText;


    CameraPreview thumbCamera1, thumbCamera2;



    String loadingpic;


    ViewPager pager;

    RelativeLayout player_camera_layout1;

    RelativeLayout container;
    ImageView loading;

    PlayerView mainPlayerView;
    SimpleExoPlayer mainPlayer;

    PlayerView thumbSurface1 , thumbSurface2;
    SimpleExoPlayer thumbPlayer1 , thumbPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        liveId = getIntent().getStringExtra("uri");
        loadingpic = getIntent().getStringExtra("pic");

        loading = findViewById(R.id.loading);

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();

        ImageLoader loader = ImageLoader.getInstance();

        loader.loadImage(loadingpic, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                Blurry.with(VideoPlayer.this).from(loadedImage).into(loading);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });





        mainPlayerView = findViewById(R.id.main_player);

        progress = findViewById(R.id.progressBar4);
        stateText = findViewById(R.id.textView3);

        container = findViewById(R.id.view2);

        player_camera_layout1 = findViewById(R.id.thumb_camera_layout1);

        thumbCamera1 = findViewById(R.id.thumb_camera1);
        thumbCamera2 = findViewById(R.id.thumb_camera2);


        thumbSurface1 = findViewById(R.id.thumb_frame1);
        thumbSurface2 = findViewById(R.id.thumb_frame2);

        pager = findViewById(R.id.pager);



        //SurfaceHolder holder = surfaceView.getHolder();
        //holder.setFormat(PixelFormat.UNKNOWN);
        //holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //surfaceView.setZ(6);
        //thumbCamera1.setZ(12);

        //thumbCamera1.getParent().requestTransparentRegion(surfaceView);

        uri = Uri.parse("rtmp://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com:1935/connection/" + liveId);
        //uri = Uri.parse("rtmp://192.168.1.103:1935/live/test");
        //uri = Uri.parse("rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/vod/sample.mp4");
        //uri = Uri.parse("rtmp://192.168.1.103:1935/vod/sample.mp4");

        Log.d("status", "rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/live/" + liveId);


        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

//Create the player
        mainPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, new DefaultLoadControl(
                new DefaultAllocator(true, 1000),
                200,  // min buffer 0.5s
                500, //max buffer 3s
                500, // playback 1s
                500,   //playback after rebuffer 1s
                1,
                true
        ));

        mainPlayerView.setPlayer(mainPlayer);

        mainPlayerView.setUseController(false);

        RtmpDataSourceFactory rtmpDataSourceFactory = new RtmpDataSourceFactory();
// This is the MediaSource representing the media to be played.
        final MediaSource videoSource = new ExtractorMediaSource.Factory(rtmpDataSourceFactory)
                .createMediaSource(uri);

        mainPlayer.prepare(videoSource);

        mainPlayer.setPlayWhenReady(true);

        mainPlayer.addListener(VideoPlayer.this);






        FragAdapter adapter = new FragAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPlayer.stop();

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

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        Log.d("ssttaattee" , String.valueOf(playbackState));

        if (playWhenReady)
        {
            loading.setVisibility(View.GONE);
        }

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

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
        mPublisher.setVideoOutputResolution(480, 360, getResources().getConfiguration().orientation);

        //mPublisher.setVideoBitRate(128000);

        mPublisher.startPublish("rtmp://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com:1935/videochat/" + liveId + b.userId);


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
