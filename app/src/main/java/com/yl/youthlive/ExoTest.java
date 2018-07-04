package com.yl.youthlive;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.streamaxia.android.CameraPreview;
import com.streamaxia.android.StreamaxiaPublisher;
import com.streamaxia.android.handlers.EncoderHandler;
import com.streamaxia.android.handlers.RecordHandler;
import com.streamaxia.android.handlers.RtmpHandler;
import com.streamaxia.android.utils.Size;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;

public class ExoTest extends AppCompatActivity implements RecordHandler.RecordListener, RtmpHandler.RtmpListener, EncoderHandler.EncodeListener {

    String uri;
    RelativeLayout container;
    CameraPreview camera;
    boolean isHidden = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_test);

        uri = getIntent().getStringExtra("uri");

        container = findViewById(R.id.container);
        camera = findViewById(R.id.camera);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

//Create the player
        final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, new DefaultLoadControl(
                new DefaultAllocator(true, 1000),
                200,  // min buffer 0.5s
                500, //max buffer 3s
                500, // playback 1s
                500,   //playback after rebuffer 1s
                1,
                true
        ));
        PlayerView playerView = findViewById(R.id.player);
        playerView.setPlayer(player);

        playerView.setUseController(false);

        Button play = findViewById(R.id.play);

        RtmpDataSourceFactory rtmpDataSourceFactory = new RtmpDataSourceFactory();
// This is the MediaSource representing the media to be played.
        final MediaSource videoSource = new ExtractorMediaSource.Factory(rtmpDataSourceFactory)
                .createMediaSource(Uri.parse("rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/videochat/" + uri));

        player.prepare(videoSource);

        player.setPlayWhenReady(true);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


// Prepare the player with the source.

//auto start playing

                if (isHidden)
                {
                    container.setVisibility(View.VISIBLE);
                    StreamaxiaPublisher mPublisher = new StreamaxiaPublisher(camera, ExoTest.this);

                    mPublisher.setEncoderHandler(new EncoderHandler(ExoTest.this));
                    mPublisher.setRtmpHandler(new RtmpHandler(ExoTest.this));
                    mPublisher.setRecordEventHandler(new RecordHandler(ExoTest.this));

                    try {
                        camera.startCamera();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }



                    //cameraPreview.setScalingMode(ScalingMode.TRIM);



                    List<Size> sizes = mPublisher.getSupportedPictureSizes(getResources().getConfiguration().orientation);
                    final Size resolution = sizes.get(0);
                    mPublisher.setVideoOutputResolution(540, 270, getResources().getConfiguration().orientation);



                    //camera.startCamera();
                    isHidden = false;

                }
                else
                {

                    camera.stopCamera();
                    container.setVisibility(View.GONE);
                    isHidden = true;

                }



            }
        });

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
    public void onNetworkWeak() {

    }

    @Override
    public void onNetworkResume() {

    }

    @Override
    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {

    }
}
