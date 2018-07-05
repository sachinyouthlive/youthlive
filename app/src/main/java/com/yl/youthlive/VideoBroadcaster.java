package com.yl.youthlive;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.exoplayer.AspectRatioFrameLayout;
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
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.streamaxia.android.CameraPreview;
import com.streamaxia.android.StreamaxiaPublisher;
import com.streamaxia.android.handlers.EncoderHandler;
import com.streamaxia.android.handlers.RecordHandler;
import com.streamaxia.android.handlers.RtmpHandler;
import com.streamaxia.android.utils.ScalingMode;
import com.streamaxia.android.utils.Size;
//import com.streamaxia.player.StreamaxiaPlayer;
//import com.streamaxia.player.listener.StreamaxiaPlayerState;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.endLivePOJO.endLiveBean;
import com.yl.youthlive.followPOJO.followBean;
import com.yl.youthlive.getIpdatedPOJO.Comment;
import com.yl.youthlive.getIpdatedPOJO.getUpdatedBean;
import com.yl.youthlive.goLivePOJO.goLiveBean;
import com.yl.youthlive.liveCommentPOJO.liveCommentBean;
import com.yl.youthlive.requestConnectionPOJO.requestConnectionBean;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class VideoBroadcaster extends AppCompatActivity implements EncoderHandler.EncodeListener, RtmpHandler.RtmpListener, RecordHandler.RecordListener, Player.EventListener {

    CameraPreview cameraPreview;
    private StreamaxiaPublisher mPublisher;
    ProgressBar progress;

    //

    TextView stateText;



    boolean torchStatus = false;

    ViewPager pager;



    Toast toast;

    View popup;
    Button end , cancel;
    //ImageButton start;


    PlayerView thumbPlayerView1;
    SimpleExoPlayer thumbPlayer1;

    RelativeLayout thumbContainer1;
    ImageView thumbLoading1;

    String liveId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_broadcaster);


        toast = Toast.makeText(this , null , Toast.LENGTH_SHORT);

        thumbContainer1 = findViewById(R.id.thumb_container1);
        thumbLoading1 = findViewById(R.id.thumb_loading1);

        cameraPreview = findViewById(R.id.preview);
        progress = findViewById(R.id.progressBar5);

        popup = findViewById(R.id.finish_popup);
        end = popup.findViewById(R.id.end);
        cancel = popup.findViewById(R.id.cancel);


        thumbPlayerView1 = findViewById(R.id.video_frame);


        pager = findViewById(R.id.pager);



        stateText = findViewById(R.id.textView3);
        //start = findViewById(R.id.imageButton2);


        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        mPublisher = new StreamaxiaPublisher(cameraPreview, this);

        mPublisher.setEncoderHandler(new EncoderHandler(this));
        mPublisher.setRtmpHandler(new RtmpHandler(this));
        mPublisher.setRecordEventHandler(new RecordHandler(this));



        try {
            cameraPreview.startCamera();
        }catch (Exception e)
        {
            e.printStackTrace();
        }



        //cameraPreview.setScalingMode(ScalingMode.TRIM);



        List<Size> sizes = mPublisher.getSupportedPictureSizes(getResources().getConfiguration().orientation);
        final Size resolution = sizes.get(0);
        mPublisher.setVideoOutputResolution(540, 270, this.getResources().getConfiguration().orientation);

        //mPublisher.setVideoBitRate(800000);




        FragAdapter adapter = new FragAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);




        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popup.setVisibility(View.GONE);

            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progress.setVisibility(View.VISIBLE);

                bean b = (bean)getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<endLiveBean> call = cr.endLive(b.userId , liveId);

                call.enqueue(new Callback<endLiveBean>() {
                    @Override
                    public void onResponse(Call<endLiveBean> call, Response<endLiveBean> response) {


                        if (response.body().getStatus().equals("1"))
                        {

                            Intent intent = new Intent(VideoBroadcaster.this , LiveEndedBroadcaster.class);
                            intent.putExtra("liveTime" , response.body().getData().getLiveTime());
                            intent.putExtra("views" , response.body().getData().getViewers());
                            startActivity(intent);
                            finish();

                        }

                        progress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<endLiveBean> call, Throwable t) {

                    }
                });


            }
        });



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


        thumbLoading1.setVisibility(View.GONE);

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

    public class FragAdapter extends FragmentStatePagerAdapter
    {

        public FragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
            {
                return new BroadcasterFragment1();
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

    @Override
    protected void onResume() {
        super.onResume();
        cameraPreview.startCamera();
        mPublisher.resumeRecord();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //cameraPreview.stopCamera();
        mPublisher.pauseRecord();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            thumbPlayer1.stop();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        mPublisher.stopPublish();
        mPublisher.stopRecord();
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

        // conect api

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


    public void switchTorch()
    {
        if (torchStatus)
        {
            cameraPreview.stopTorch();
            torchStatus = false;
        }
        else
        {
            cameraPreview.startTorch();
            torchStatus = true;
        }
    }


    public void startPublish(String liveId)
    {
        mPublisher.startPublish("rtmp://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com:1935/connection/" + liveId);
    }


    public void switchCamera()
    {
        mPublisher.switchCamera();
    }


    public void endLive(String liveId)
    {
        this.liveId = liveId;
        popup.setVisibility(View.VISIBLE);
    }

    public void setLiveId(String liveId)
    {
        this.liveId = liveId;
    }

    @Override
    public void onBackPressed() {
        endLive(liveId);
    }


    public void startThumbPlayer1(String connId , String thumbPic)
    {
        Log.d("uurrii" , thumbPic);

        thumbContainer1.setVisibility(View.VISIBLE);
        thumbPlayerView1.setVisibility(View.VISIBLE);
        thumbLoading1.setVisibility(View.VISIBLE);


        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();
        ImageLoader loader = ImageLoader.getInstance();

        loader.loadImage(thumbPic, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                Blurry.with(VideoBroadcaster.this).from(loadedImage).into(thumbLoading1);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });



        //Uri uri = Uri.parse("rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/vod/sample.mp4");

        Uri uri = Uri.parse("rtmp://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com:1935/videochat/" + connId);


        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

//Create the player
        thumbPlayer1 = ExoPlayerFactory.newSimpleInstance(this, trackSelector, new DefaultLoadControl(
                new DefaultAllocator(true, 1000),
                200,  // min buffer 0.5s
                500, //max buffer 3s
                500, // playback 1s
                500,   //playback after rebuffer 1s
                1,
                true
        ));

        thumbPlayerView1.setPlayer(thumbPlayer1);

        thumbPlayerView1.setUseController(false);

        thumbPlayer1.addListener(VideoBroadcaster.this);

        RtmpDataSourceFactory rtmpDataSourceFactory = new RtmpDataSourceFactory();
// This is the MediaSource representing the media to be played.
        final MediaSource videoSource = new ExtractorMediaSource.Factory(rtmpDataSourceFactory)
                .createMediaSource(uri);

        thumbPlayer1.prepare(videoSource);

        thumbPlayer1.setPlayWhenReady(true);


    }

    public void endThumbPlayer1()
    {
        thumbPlayer1.stop();

        thumbPlayerView1.setVisibility(View.GONE);
        thumbContainer1.setVisibility(View.GONE);
        thumbLoading1.setVisibility(View.GONE);

    }


}
