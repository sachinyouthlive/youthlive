package com.yl.youthlive;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Region;
import android.hardware.Camera;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import com.flashphoner.fpwcsapi.Flashphoner;
import com.flashphoner.fpwcsapi.bean.Connection;
import com.flashphoner.fpwcsapi.bean.Data;
import com.flashphoner.fpwcsapi.bean.StreamStatus;
import com.flashphoner.fpwcsapi.layout.PercentFrameLayout;
import com.flashphoner.fpwcsapi.session.Session;
import com.flashphoner.fpwcsapi.session.SessionEvent;
import com.flashphoner.fpwcsapi.session.SessionOptions;
import com.flashphoner.fpwcsapi.session.Stream;
import com.flashphoner.fpwcsapi.session.StreamOptions;
import com.flashphoner.fpwcsapi.session.StreamStatusEvent;
import com.github.faucamp.simplertmp.RtmpHandler;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
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
import com.google.android.exoplayer2.video.VideoListener;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.pili.pldroid.player.widget.PLVideoView;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.acceptRejectPOJO.acceptRejectBean;
import com.yl.youthlive.pl.widget.MediaController;


import net.ossrs.yasea.SrsCameraView;
import net.ossrs.yasea.SrsEncodeHandler;
import net.ossrs.yasea.SrsPublisher;
import net.ossrs.yasea.SrsRecordHandler;

import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;

import java.io.IOException;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class VideoPlayer extends AppCompatActivity implements SrsEncodeHandler.SrsEncodeListener, SrsRecordHandler.SrsRecordListener, RtmpHandler.RtmpListener, PLMediaPlayer.OnErrorListener, PLMediaPlayer.OnCompletionListener {

    String liveId;


    ProgressBar progress;

    private Uri uri;


    String TAG = "VideoPlayer";


    String connId;


    TextView stateText;


    SrsCameraView thumbCamera2;


    String loadingpic;


    ViewPager pager;

    RelativeLayout player_camera_layout1;

    RelativeLayout container;
    ImageView loading;

    //PLVideoTextureView mainPlayerView;
    //SimpleExoPlayer mainPlayer;

    ProgressBar loadingProgress;


    PlayerView thumbSurface2;
    //SimpleExoPlayer thumbPlayer1, thumbPlayer2;

    View loadingPopup;

    View thumbCountdown;

    TextView thumbCount;


    private Session session;

    private Stream playStream;

    private SurfaceViewRenderer remoteRender;

    private PercentFrameLayout remoteRenderLayout;


    private Stream publishStream;

    private SurfaceViewRenderer localRender;

    private PercentFrameLayout localRenderLayout;


    private Stream thumbStream;

    private SurfaceViewRenderer thumbRender;

    private PercentFrameLayout thumbRenderLayout;


    boolean isThumbCamera1 = false;

    ProgressBar thumbProgress1;

    private int mDisplayAspectRatio = PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Flashphoner.init(this);

        liveId = getIntent().getStringExtra("uri");
        loadingpic = getIntent().getStringExtra("pic");


        thumbProgress1 = findViewById(R.id.thumb_progress1);

        DoubleBounce doubleBounce = new DoubleBounce();
        thumbProgress1.setIndeterminateDrawable(doubleBounce);


        thumbCountdown = findViewById(R.id.thumb_countdown);
        thumbCount = findViewById(R.id.textView33);


        loadingPopup = findViewById(R.id.loading_popup);

        loading = findViewById(R.id.loading);
        loadingProgress = findViewById(R.id.loading_progress);

        loadingProgress.setIndeterminateDrawable(doubleBounce);

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


        progress = findViewById(R.id.progressBar4);
        stateText = findViewById(R.id.textView3);

        container = findViewById(R.id.view2);

        player_camera_layout1 = findViewById(R.id.thumb_camera_layout1);


        thumbCamera2 = findViewById(R.id.thumb_camera2);


        thumbRender = findViewById(R.id.thumb_renderer);

        thumbRenderLayout = findViewById(R.id.thumb_renderer_layout);


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

        Log.d("statusss", "rtmp://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com:1935/connection/" + liveId);


        //loadingProgress.setVisibility(View.VISIBLE);

        /*BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);


        MediaController mMediaController = new MediaController(this, false, true);

        //mainPlayerView.setMediaController(mMediaController);


        mainPlayerView.setBufferingIndicator(loadingPopup);

        mainPlayerView.setOnCompletionListener(new PLMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(PLMediaPlayer plMediaPlayer) {
                Log.d("completecode", "completed");
                onEror(loadingpic);
            }
        });
        mainPlayerView.setOnErrorListener(new PLMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(PLMediaPlayer plMediaPlayer, int i) {

                Log.d("errorcode", String.valueOf(i));

                if (i == -1)
                {
                    onEror(loadingpic);
                }

                return true;
            }
        });

        mainPlayerView.setOnInfoListener(new PLMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(PLMediaPlayer plMediaPlayer, int i, int i1) {

                Log.d("infocode", String.valueOf(i));

                return true;
            }
        });
        mainPlayerView.setOnSeekCompleteListener(new PLMediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(PLMediaPlayer plMediaPlayer) {
                Log.d("seekcode", "seek completed");
            }
        });
        mainPlayerView.setOnPreparedListener(new PLMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(PLMediaPlayer plMediaPlayer) {
                //thumbProgress1.setVisibility(View.GONE);
            }
        });

        mainPlayerView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
        initAVOptions();
        mainPlayerView.setAVOptions(options2);

        mainPlayerView.setDebugLoggingEnabled(true);

        mainPlayerView.setDisplayOrientation(0);

        mainPlayerView.setMirror(true);

        mainPlayerView.setVideoPath("rtmp://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com:1935/vod/mi6.mp4");
        //mainPlayerView.setVideoPath("rtmp://ec2-52-15-208-193.us-east-2.compute.amazonaws.com:1935/connection/" + liveId);
        //mainPlayerView.setVideoPath("rtmp://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com:1935/connection/" + liveId);
        mainPlayerView.start();
*/
//Create the player
        /*mainPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, new DefaultLoadControl(
                new DefaultAllocator(true, 1000),
                1000,  // min buffer 0.5s
                3000, //max buffer 3s
                1000, // playback 1s
                1000,   //playback after rebuffer 1s
                1,
                true
        ));

        mainPlayerView.setPlayer(mainPlayer);

        mainPlayerView.setUseController(false);

        mainPlayer.addAudioDebugListener(new AudioRendererEventListener() {
            @Override
            public void onAudioEnabled(DecoderCounters counters) {

            }

            @Override
            public void onAudioSessionId(int audioSessionId) {


            }

            @Override
            public void onAudioDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

            }

            @Override
            public void onAudioInputFormatChanged(Format format) {

            }

            @Override
            public void onAudioSinkUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {

            }

            @Override
            public void onAudioDisabled(DecoderCounters counters) {

            }
        });

        RtmpDataSourceFactory rtmpDataSourceFactory = new RtmpDataSourceFactory();
// This is the MediaSource representing the media to be played.
        final MediaSource videoSource = new ExtractorMediaSource.Factory(rtmpDataSourceFactory)
                .createMediaSource(uri);

        mainPlayer.prepare(videoSource);


        mainPlayer.setPlayWhenReady(true);


        mainPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                Log.d("parameters", String.valueOf(trackSelections.length));

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.d("ssttaattee", String.valueOf(playbackState));

                if (playWhenReady) {
                    loadingPopup.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);

                }

                *//*if (playbackState == 4) {


                    Dialog dialog = new Dialog(VideoPlayer.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.ended_dialog);
                    dialog.show();

                    Button ok = dialog.findViewById(R.id.button2);

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            finish();

                        }
                    });


                }*//*

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

                Log.d("eerroorr", error.toString());


                Dialog dialog = new Dialog(VideoPlayer.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.ended_dialog);
                dialog.show();

                Button ok = dialog.findViewById(R.id.button2);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        finish();

                    }
                });


            }


            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                Log.d("parameters", String.valueOf(playbackParameters.pitch));

            }

            @Override
            public void onSeekProcessed() {

            }
        });*/


        remoteRender = findViewById(R.id.remote_video_view);
        remoteRenderLayout = findViewById(R.id.remote_video_layout);

        remoteRender.setZOrderMediaOverlay(true);

        remoteRenderLayout.setPosition(0, 0, 100, 100);
        remoteRender.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        remoteRender.setMirror(false);
        remoteRender.requestLayout();



        localRender = findViewById(R.id.local_video_view);


        localRenderLayout = findViewById(R.id.local_video_layout);


        localRender.setZOrderMediaOverlay(true);


        localRenderLayout.setPosition(0, 0, 100, 100);
        localRender.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        localRender.setMirror(true);
        localRender.requestLayout();


        thumbRender.setZOrderMediaOverlay(true);


        thumbRenderLayout.setPosition(0, 0, 100, 100);
        thumbRender.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        thumbRender.setMirror(true);
        thumbRender.requestLayout();


        URI u = null;
        try {
            u = new URI("ws://ec2-13-127-186-216.ap-south-1.compute.amazonaws.com:8080");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String url = u.getScheme() + "://" + u.getHost() + ":" + u.getPort();


        SessionOptions sessionOptions = new SessionOptions(url);
        sessionOptions.setRemoteRenderer(remoteRender);
        sessionOptions.setLocalRenderer(localRender);

        /**
         * Session for connection to WCS server is created with method createSession().
         */
        session = Flashphoner.createSession(sessionOptions);

        /**
         * Callback functions for session status events are added to make appropriate changes in controls of the interface and play stream when connection is established.
         */
        session.on(new SessionEvent() {
            @Override
            public void onAppData(Data data) {

            }

            @Override
            public void onConnected(final Connection connection) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        /**
                         * The options for the stream to play are set.
                         * The stream name is passed when StreamOptions object is created.
                         */
                        StreamOptions streamOptions = new StreamOptions(liveId);

                        /**
                         * Stream is created with method Session.createStream().
                         */
                        playStream = session.createStream(streamOptions);

                        /**
                         * Callback function for stream status change is added to display the status.
                         */
                        playStream.on(new StreamStatusEvent() {
                            @Override
                            public void onStreamStatus(final Stream stream, final StreamStatus streamStatus) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.d("ssttaattuuss", String.valueOf(streamStatus));

                                        if (!StreamStatus.PLAYING.equals(streamStatus)) {
                                            onEror(loadingpic);
                                            Log.e(TAG, "Can not play stream " + stream.getName() + " " + streamStatus);

                                        } else if (StreamStatus.PLAYING.equals(streamStatus)) {
                                            loadingPopup.setVisibility(View.GONE);
                                            loading.setVisibility(View.GONE);
                                        }

                                    }
                                });
                            }
                        });

                        /*
                         * Method Stream.play() is called to start playback of the stream.
                         */
                        playStream.play();

                    }
                });
            }

            @Override
            public void onRegistered(Connection connection) {

            }

            @Override
            public void onDisconnection(final Connection connection) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });


        /**
         * Connection to WCS server is established with method Session.connect().
         */
        session.connect(new Connection());


        FragAdapter adapter = new FragAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (session != null) {
            session.disconnect();
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
    public void onPointerCaptureChanged(boolean hasCapture) {

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
    public boolean onError(PLMediaPlayer plMediaPlayer, int i) {
        return false;
    }

    @Override
    public void onCompletion(PLMediaPlayer plMediaPlayer) {

    }

/*
    @Override
    public void onRtmpAuthenticationg(String s) {

    }
*/


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
                return null;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    }

    //SrsPublisher mPublisher;

    public void startThumbCamera1(final String connId) {


        progress.setVisibility(View.VISIBLE);
        Log.d("rreess", connId);


        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);

        Call<String> call1 = cr.acceptReject2(connId, liveId + b.userId, "2", b.userId);
        call1.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.d("rreess", response.body());

                isThumbCamera1 = true;
                progress.setVisibility(View.GONE);
                player_camera_layout1.setVisibility(View.VISIBLE);


                thumbCountdown.setVisibility(View.VISIBLE);


                VideoPlayer.this.connId = connId;

                final bean b = (bean) getApplicationContext();

                /*mPublisher = new SrsPublisher(thumbCamera1);

                mPublisher.setEncodeHandler(new SrsEncodeHandler(VideoPlayer.this));
                mPublisher.setRtmpHandler(new RtmpHandler(new RtmpHandler.RtmpListener() {
                    @Override
                    public void onRtmpConnecting(String s) {

                        Log.d("rreess", s);

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
                        Log.d("rreess", "stopped");

                    }

                    @Override
                    public void onRtmpDisconnected() {
                        Log.d("rreess", "disconnected");
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
                        Log.d("rreess", e.toString());
                    }

                    @Override
                    public void onRtmpIllegalArgumentException(IllegalArgumentException e) {
                        Log.d("rreess", e.toString());
                    }

                    @Override
                    public void onRtmpIllegalStateException(IllegalStateException e) {
                        Log.d("rreess", e.toString());
                    }


                }));
                mPublisher.setRecordHandler(new SrsRecordHandler(VideoPlayer.this));
                *//*mPublisher.getmCameraView().open_camera();


                DisplayMetrics displayMetrics = new DisplayMetrics();
                WindowManager wm = (WindowManager)
                        getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                wm.getDefaultDisplay().getMetrics(displayMetrics);
                int screenWidth = displayMetrics.widthPixels;
                int screenHeight = displayMetrics.heightPixels;


                Camera.Size best_size= mPublisher.getmCameraView().get_best_size2(screenWidth , screenHeight);

                if(best_size!=null)
                {
                    Log.d("asdasd","************ Best size is "+best_size.width+" Height: "+best_size.height+" ********************");
                    mPublisher.setPreviewResolution(best_size.width, best_size.height);
                    mPublisher.setOutputResolution(best_size.height, best_size.width);
                }
                else
                {
                    Log.d("asdasd","************ Best size is NULL ********************");
                    mPublisher.setPreviewResolution(640, 480);
                    mPublisher.setOutputResolution(480, 640);
                }

*//*

                mPublisher = new SrsPublisher(thumbCamera1);
*//*

        mPublisher.setEncoderHandler(new EncoderjHandler(this));
        mPublisher.setRtmpHandler(new RtmpHandler(this));
        mPublisher.setRecordEventHandler(new RecordHandler(this));
*//*


                mPublisher.setEncodeHandler(new SrsEncodeHandler(VideoPlayer.this));
                mPublisher.setRtmpHandler(new RtmpHandler(VideoPlayer.this));
                mPublisher.setRecordHandler(new SrsRecordHandler(VideoPlayer.this));


                thumbCamera1.startCamera();


                //mPublisher.setCameraFacing(1);

                mPublisher.setScreenOrientation(Configuration.ORIENTATION_PORTRAIT);

                mPublisher.setVideoBitRate(200 * 1024);


                mPublisher.setOutputResolution(144 * 3, 192 * 3);
                mPublisher.setPreviewResolution(192 * 3, 144 * 3);

                mPublisher.startPublish("rtmp://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com:1935/videochat/" + liveId + b.userId);

*/


                StreamOptions streamOptions = new StreamOptions(liveId + b.userId);

                /**
                 * Stream is created with method Session.createStream().
                 */
                publishStream = session.createStream(streamOptions);

                /**
                 * Callback function for stream status change is added to play the stream when it is published.
                 */
                publishStream.on(new StreamStatusEvent() {
                    @Override
                    public void onStreamStatus(final Stream stream, final StreamStatus streamStatus) {

                        Log.d("ssttaattuuss", String.valueOf(streamStatus));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (StreamStatus.PUBLISHING.equals(streamStatus)) {

                                } else {
                                    Log.e("Streamer", "Can not publish stream " + stream.getName() + " " + streamStatus);
                                }

                            }
                        });
                    }

                });

                ActivityCompat.requestPermissions(VideoPlayer.this,
                        new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA},
                        PUBLISH_REQUEST_CODE);


                new CountDownTimer(3000, 1000) {

                    //Toast toast = Toast.makeText(VideoPlayer.this , null , Toast.LENGTH_SHORT);

                    @Override
                    public void onTick(long millisUntilFinished) {

                        thumbCount.setText(String.valueOf(millisUntilFinished / 1000));

                    }

                    @Override
                    public void onFinish() {

                        thumbCountdown.setVisibility(View.GONE);


                    }
                }.start();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress.setVisibility(View.GONE);
                Log.d("rreess", t.toString());
                t.printStackTrace();
            }
        });


    }


    public void startThumbCamera1FromPlayer(final String connId) {

        player_camera_layout1.setVisibility(View.VISIBLE);

        //thumbCamera1.setVisibility(View.VISIBLE);
        thumbCountdown.setVisibility(View.VISIBLE);


        this.connId = connId;

        final bean b = (bean) getApplicationContext();


        StreamOptions streamOptions = new StreamOptions(liveId + b.userId);

        /**
         * Stream is created with method Session.createStream().
         */
        publishStream = session.createStream(streamOptions);

        /**
         * Callback function for stream status change is added to play the stream when it is published.
         */
        publishStream.on(new StreamStatusEvent() {
            @Override
            public void onStreamStatus(final Stream stream, final StreamStatus streamStatus) {

                Log.d("ssttaattuuss", String.valueOf(streamStatus));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (StreamStatus.PUBLISHING.equals(streamStatus)) {

                        } else {
                            Log.e("Streamer", "Can not publish stream " + stream.getName() + " " + streamStatus);
                        }

                    }
                });
            }

        });

        ActivityCompat.requestPermissions(VideoPlayer.this,
                new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA},
                PUBLISH_REQUEST_CODE);


        new CountDownTimer(3000, 1000) {

            //Toast toast = Toast.makeText(VideoPlayer.this , null , Toast.LENGTH_SHORT);

            @Override
            public void onTick(long millisUntilFinished) {

                thumbCount.setText(String.valueOf(millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {

                thumbCountdown.setVisibility(View.GONE);


            }
        }.start();


    }


    public void startThumbPlayer1(String connId) {


        Uri uri = Uri.parse("rtmp://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com:1935/videochat/" + connId);

        thumbRenderLayout.setVisibility(View.VISIBLE);
        //thumbRender.setVisibility(View.VISIBLE);

        thumbRenderLayout.getParent().requestTransparentRegion(remoteRender);

        URI u = null;
        try {
            u = new URI("ws://ec2-13-127-186-216.ap-south-1.compute.amazonaws.com:8080");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String url = u.getScheme() + "://" + u.getHost() + ":" + u.getPort();


        StreamOptions streamOptions = new StreamOptions(connId);
        streamOptions.setRenderer(thumbRender);

        /**
         * Stream is created with method Session.createStream().
         */
        thumbStream = session.createStream(streamOptions);

        /**
         * Callback function for stream status change is added to display the status.
         */
        thumbStream.on(new StreamStatusEvent() {
            @Override
            public void onStreamStatus(final Stream stream, final StreamStatus streamStatus) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("ssttaattuuss", String.valueOf(streamStatus));

                        if (!StreamStatus.PLAYING.equals(streamStatus)) {

                            endThumbPlayer1();

                        } else if (StreamStatus.PLAYING.equals(streamStatus)) {
                            thumbProgress1.setVisibility(View.GONE);
                        }

                    }
                });
            }
        });

        /*
         * Method Stream.play() is called to start playback of the stream.
         */
        thumbStream.play();





        /*thumbSurface1.setVisibility(View.VISIBLE);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

//Create the player
        thumbPlayer1 = ExoPlayerFactory.newSimpleInstance(this, trackSelector, new DefaultLoadControl(
                new DefaultAllocator(true, 1000),
                1000,  // min buffer 0.5s
                2000, //max buffer 3s
                1000, // playback 1s
                1000,   //playback after rebuffer 1s
                1,
                true
        ));

        thumbSurface1.setPlayer(thumbPlayer1);

        thumbSurface1.setUseController(false);

        thumbPlayer1.addListener(new Player.EventListener() {
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

                if (playWhenReady) {
                    thumbProgress1.setVisibility(View.GONE);
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

                endThumbPlayer1();

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
        });

        RtmpDataSourceFactory rtmpDataSourceFactory = new RtmpDataSourceFactory();
// This is the MediaSource representing the media to be played.
        final MediaSource videoSource = new ExtractorMediaSource.Factory(rtmpDataSourceFactory)
                .createMediaSource(uri);

        thumbPlayer1.prepare(videoSource);

        thumbPlayer1.setPlayWhenReady(true);
*/

    }

    public void endThumbCamera1() {

        try {

            publishStream.stop();

            //mPublisher.stopRecord();

            //mPublisher = null;
            isThumbCamera1 = false;
            player_camera_layout1.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //thumbCamera1.setVisibility(View.INVISIBLE);


    }

    public void endThumbPlayer1() {

        try {
            thumbStream.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }


        thumbRenderLayout.setVisibility(View.GONE);
        //thumbRender.setVisibility(View.GONE);

        thumbRenderLayout.getParent().requestTransparentRegion(remoteRender);

        //remoteRender.bringToFront();





    }

    @Override
    protected void onResume() {
        super.onResume();

        //mainPlayerView.start();

        /*if (mainPlayer != null)
        {
            mainPlayer.setPlayWhenReady(true);
        }*/


        /*if (thumbPlayer1 != null)
        {
            thumbPlayer1.setPlayWhenReady(true);
        }*/


    }

    @Override
    protected void onPause() {
        super.onPause();



/*
        if (mainPlayer != null)
        {
            mainPlayer.stop();
        }
*/

/*
        if (thumbPlayer1 != null)
        {
            thumbPlayer1.stop();
        }
*/

        //cameraPreview.stopCamera();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<String> call = cr.exitPlayer(b.userId, liveId);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                progress.setVisibility(View.GONE);

                finish();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                progress.setVisibility(View.GONE);

            }
        });

    }

    private AVOptions options2;

    private void initAVOptions() {
        options2 = new AVOptions();
        // the unit of timeout is ms
        options2.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        options2.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
        // Some optimization with buffering mechanism when be set to 1
        options2.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        //options2.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
        // 1 -> hw codec enable, 0 -> disable [recommended]
        int codec = 0;
        options2.setInteger(AVOptions.KEY_MEDIACODEC, codec);
        options2.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
        options2.setInteger(AVOptions.KEY_CACHE_BUFFER_DURATION, 1000);
        options2.setInteger(AVOptions.KEY_MAX_CACHE_BUFFER_DURATION, 3000);
        // whether start play automatically after prepared, default value is 1
        options2.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);
    }


    String timelineId, timelineName;


    public void setData(String timelineId, String timelineName) {
        this.timelineId = timelineId;
        this.timelineName = timelineName;
    }


    public void onEror(String loadingpic) {
        Intent intent1 = new Intent(VideoPlayer.this, LiveEndedPlayer.class);
        intent1.putExtra("image", loadingpic);
        intent1.putExtra("id", timelineId);
        intent1.putExtra("name", timelineName);
        intent1.putExtra("time", "");
        intent1.putExtra("views", "");
        startActivity(intent1);
        overridePendingTransition(0, 0);
        finish();

    }

    private static final int PUBLISH_REQUEST_CODE = 100;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PUBLISH_REQUEST_CODE: {
                if (grantResults.length == 0 ||
                        grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[1] != PackageManager.PERMISSION_GRANTED) {

                    session.disconnect();

                } else {
                    /**
                     * Method Stream.publish() is called to publish stream.
                     */
                    publishStream.publish();

                }
            }
        }
    }

}
