package com.yl.youthlive;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.flashphoner.fpwcsapi.Flashphoner;
import com.flashphoner.fpwcsapi.bean.Connection;
import com.flashphoner.fpwcsapi.bean.Data;
import com.flashphoner.fpwcsapi.bean.StreamStatus;
import com.flashphoner.fpwcsapi.handler.CameraSwitchHandler;
import com.flashphoner.fpwcsapi.layout.PercentFrameLayout;
import com.flashphoner.fpwcsapi.session.Session;
import com.flashphoner.fpwcsapi.session.SessionEvent;
import com.flashphoner.fpwcsapi.session.SessionOptions;
import com.flashphoner.fpwcsapi.session.Stream;
import com.flashphoner.fpwcsapi.session.StreamOptions;
import com.flashphoner.fpwcsapi.session.StreamStatusEvent;
import com.github.faucamp.simplertmp.RtmpHandler;
import com.github.faucamp.simplertmp.packets.Video;
import com.github.ybq.android.spinkit.style.DoubleBounce;
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
import com.seu.magicfilter.utils.MagicFilterType;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.endLivePOJO.endLiveBean;

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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

//import com.google.android.exoplayer.AspectRatioFrameLayout;
/**/
//import com.streamaxia.player.StreamaxiaPlayer;
//import com.streamaxia.player.listener.StreamaxiaPlayerState;

public class VideoBroadcaster extends AppCompatActivity implements RtmpHandler.RtmpListener, SrsEncodeHandler.SrsEncodeListener, SrsRecordHandler.SrsRecordListener {

    //CameraPreview cameraPreview;
    //private StreamaxiaPublisher mPublisher;
    ProgressBar progress;

    //

    TextView stateText;


    boolean torchStatus = false;

    ViewPager pager;


    Toast toast;

    View popup;
    Button end, cancel;
    //ImageButton start;


    RelativeLayout thumbContainer1;


    String liveId = "";
    View countDownPopup;
    TextSwitcher countdown;

    View thumbcountdown;
    TextView thumbCount;


    Chronometer chronometer;


    boolean isEnded = false;


    SharedPreferences pref;
    SharedPreferences.Editor editor;


    //private SrsPublisher mPublisher;
    //SrsCameraView cameraPreview;


    //BroadcastReceiver headsetPlug;


    //TextView earphones;

    int flag = 0;

    ProgressBar thumbProgress1;

    View thumbLoading;


    private Session session;

    private Stream publishStream;

    private SurfaceViewRenderer localRender;

    private PercentFrameLayout localRenderLayout;


    private Stream playStream;

    private SurfaceViewRenderer remoteRender;

    private PercentFrameLayout remoteRenderLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_broadcaster);

        Flashphoner.init(this);

        pref = getSharedPreferences("offline", Context.MODE_PRIVATE);
        editor = pref.edit();

        toast = Toast.makeText(this, null, Toast.LENGTH_SHORT);


        thumbLoading = findViewById(R.id.thumb_loading);

        thumbProgress1 = findViewById(R.id.progressBar13);

        DoubleBounce doubleBounce = new DoubleBounce();
        thumbProgress1.setIndeterminateDrawable(doubleBounce);


//        Log.d("offline" , String.valueOf(db.queries().getAll().size()));

        chronometer = findViewById(R.id.chronometer);
        thumbcountdown = findViewById(R.id.thumb_countdown);
        thumbCount = findViewById(R.id.textView33);

        countDownPopup = findViewById(R.id.countdown_popup);
        countdown = findViewById(R.id.textView29);
        thumbContainer1 = findViewById(R.id.thumb_container1);


        localRender = findViewById(R.id.local_video_view);


        localRenderLayout = findViewById(R.id.local_video_layout);


        localRender.setZOrderMediaOverlay(true);


        localRenderLayout.setPosition(0, 0, 100, 100);
        localRender.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        localRender.setMirror(false);
        localRender.requestLayout();

        remoteRender = findViewById(R.id.remote_video_view);
        remoteRenderLayout = findViewById(R.id.remote_video_layout);

        remoteRender.setZOrderOnTop(true);

        remoteRenderLayout.setPosition(0, 0, 100, 100);
        remoteRender.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        remoteRender.setMirror(false);
        remoteRender.requestLayout();


        progress = findViewById(R.id.progressBar5);

        popup = findViewById(R.id.finish_popup);
        end = popup.findViewById(R.id.end);
        cancel = popup.findViewById(R.id.cancel);


        pager = findViewById(R.id.pager);


        stateText = findViewById(R.id.textView3);
        //start = findViewById(R.id.imageButton2);

/*
        mPublisher = new SrsPublisher(cameraPreview);


        mPublisher.setEncodeHandler(new SrsEncodeHandler(this));
        mPublisher.setRtmpHandler(new RtmpHandler(this));
        mPublisher.setRecordHandler(new SrsRecordHandler(this));





        cameraPreview.startCamera();



        Camera.Size supportedRes = mPublisher.getmCameraView().getSupportPreviews().get(1);



        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager)
                getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.widthPixels;
        int screenWidth = displayMetrics.heightPixels;


        Log.d("asdasdasd", String.valueOf(screenWidth));
        Log.d("asdasdasd", String.valueOf(screenHeight));


        float rat = screenWidth / screenHeight;





        List<Camera.Size> sizes = mPublisher.getmCameraView().getSupportPreviews();

        int bes_width = 0;
        int max_limit = 720;

        Camera.Size best_size = null;

        for (Camera.Size size : sizes) {
            Log.d("SrsCamera", "Size width:" + size.width + " height:" + size.height);
            if (size.height > bes_width && size.height <= max_limit) {
                if (size.width / size.height == rat) {
                    bes_width = size.width;
                    best_size = size;
                }
            }
        }


        Camera.Size ps = mPublisher.getmCameraView().getPreferedPreviews();

        //Toast.makeText(VideoBroadcaster.this, String.valueOf(best_size2.height) + " X " + String.valueOf(best_size2.width), Toast.LENGTH_LONG).show();

        mPublisher.setOutputResolution(best_size.height, best_size.width);

        mPublisher.setPreviewResolution(best_size.width, best_size.height);

        mPublisher.setVideoHDMode();
*/




/*
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
*/


        //mPublisher.switchCameraFilter(MagicFilterType.COOL);

//        mPublisher.setEncodeHandler(new SrsEncodeHandler(this));
//        mPublisher.setRtmpHandler(new RtmpHandler(this));
//        mPublisher.setRecordHandler(new SrsRecordHandler(this));
//        mPublisher.setPreviewResolution(640, 480);
//        mPublisher.setOutputResolution(360, 640);
//        //mPublisher.switchCameraFilter(MagicFilterType.BEAUTY);
//        mPublisher.setVideoSmoothMode();
//
//
//
//        mPublisher.startCamera();



        /*try {
            cameraPreview.startCamera();
        }catch (Exception e)
        {
            e.printStackTrace();
        }*/


        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometerChanged) {
                chronometer = chronometerChanged;
            }
        });

        //cameraPreview.setScalingMode(ScalingMode.TRIM);


        countdown.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                // TODO Auto-generated method stub
                // create a TextView
                TextView t = new TextView(VideoBroadcaster.this);
                // set the gravity of text to top and center horizontal
                t.setGravity(Gravity.CENTER);
                t.setTextColor(Color.WHITE);

                // set displayed text size
                t.setTextSize(100);
                return t;
            }
        });


        countdown.setCurrentText("--");



        /*List<Size> sizes = mPublisher.getSupportedPictureSizes(getResources().getConfiguration().orientation);
        final Size resolution = sizes.get(0);
        mPublisher.setVideoOutputResolution(1280, 720, this.getResources().getConfiguration().orientation);
*/
        //mPublisher.setVideoBitRate(1600);


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

                bean b = (bean) getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<endLiveBean> call = cr.endLive(b.userId, liveId);

                call.enqueue(new Callback<endLiveBean>() {
                    @Override
                    public void onResponse(Call<endLiveBean> call, Response<endLiveBean> response) {


                        if (response.body().getStatus().equals("1")) {

                            isEnded = true;

                            Intent intent = new Intent(VideoBroadcaster.this, LiveEndedBroadcaster.class);
                            intent.putExtra("liveTime", response.body().getData().getLiveTime());
                            intent.putExtra("views", response.body().getData().getViewers());
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onNetworkWeak() {

        Toast toast = Toast.makeText(VideoBroadcaster.this, null, Toast.LENGTH_LONG);
        toast.setText("Slow Internet Detected, Exiting broadcast...");
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();


        String et = String.valueOf((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000);


        editor.putString("offline", et);
        editor.putString("liveId", liveId);
        editor.apply();


        finish();

//        Toast.makeText(getApplicationContext(), "Slow Internet Detected, Exiting broadcast...", Toast.LENGTH_LONG).setGravity(Gravity.CENTER_VERTICAL, 0, 0).show();



        /*progress.setVisibility(View.VISIBLE);

        bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<endLiveBean> call = cr.endLive(b.userId, liveId);

        call.enqueue(new Callback<endLiveBean>() {
            @Override
            public void onResponse(Call<endLiveBean> call, Response<endLiveBean> response) {


                if (response.body().getStatus().equals("1")) {

                    isEnded = true;

                    Intent intent = new Intent(VideoBroadcaster.this, LiveEndedBroadcaster.class);
                    intent.putExtra("liveTime", response.body().getData().getLiveTime());
                    intent.putExtra("views", response.body().getData().getViewers());
                    startActivity(intent);
                    finish();

                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<endLiveBean> call, Throwable t) {

            }
        });*/


        //mPublisher.setVideoSmoothMode();
    }

    @Override
    public void onNetworkResume() {
        //mPublisher.setVideoHDMode();
        //Toast.makeText(getApplicationContext(), "resume" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
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


    public class FragAdapter extends FragmentStatePagerAdapter {

        public FragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new BroadcasterFragment1();
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    }

    /*@Override
    protected void onResume() {
        if (mPublisher != null)
        {
            cameraPreview.startCamera();

        }



        super.onResume();

    }*/

    /*@Override
    protected void onPause() {

        if (session != null)
        {
            //mPublisher.stopPublish();
            //Toast.makeText(getApplicationContext(), "Camera Released", Toast.LENGTH_SHORT).show();

            //mPublisher.stopRecord();
        }

        super.onPause();

    }*/

    @Override
    protected void onStop() {
        super.onStop();

        if (!isEnded) {

            String et = String.valueOf((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000);


            editor.putString("offline", et);
            editor.putString("liveId", liveId);
            editor.apply();

        }

    }

    @Override
    protected void onDestroy() {

        Log.d("offline", "destroy");

/*

        try {
            thumbPlayer1.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
*/

        try {

            if (session != null) {
                session.disconnect();
                chronometer.stop();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        super.onDestroy();


    }

    /*@Override
    public void onNetworkWeak() {

        Toast.makeText(VideoBroadcaster.this , "Network Weak" , Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNetworkResume() {

    }

    @Override
    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {

    }*/

    @Override
    public void onRtmpConnecting(String s) {
        //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRtmpConnected(String s) {
        //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
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
        handleException(e);

        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

        if (!isEnded) {

            String et = String.valueOf((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000);


            editor.putString("offline", et);
            editor.putString("liveId", liveId);
            editor.apply();

            setResult(Activity.RESULT_OK, getIntent());
            finish();

        }

    }

    @Override
    public void onRtmpIOException(IOException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIllegalStateException(IllegalStateException e) {
        handleException(e);
    }

    /*@Override
    public void onRtmpAuthenticationg(String s) {

    }*/

/*
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
*/


    public void switchTorch() {
        if (torchStatus) {

            torchStatus = false;
        } else {
            //cameraPreview.startTorch();
            torchStatus = true;
        }
    }


    public void startPublish(final String liveId) {

        URI u = null;
        try {
            u = new URI("ws://ec2-35-154-79-248.ap-south-1.compute.amazonaws.com:8080");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String url = u.getScheme() + "://" + u.getHost() + ":" + u.getPort();

        SessionOptions sessionOptions = new SessionOptions(url);
        sessionOptions.setLocalRenderer(localRender);
        sessionOptions.setRemoteRenderer(remoteRender);


        /**
         * Session for connection to WCS server is created with method createSession().
         */
        session = Flashphoner.createSession(sessionOptions);

        /**
         * Callback functions for session status events are added to make appropriate changes in controls of the interface and publish stream when connection is established.
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
                         * The options for the stream to publish are set.
                         * The stream name is passed when StreamOptions object is created.
                         */
                        StreamOptions streamOptions = new StreamOptions(liveId);

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

                        ActivityCompat.requestPermissions(VideoBroadcaster.this,
                                new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA},
                                PUBLISH_REQUEST_CODE);
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


    }


    public void switchCamera() {

        publishStream.switchCamera(new CameraSwitchHandler() {
            @Override
            public void onCameraSwitchDone(boolean b) {

            }

            @Override
            public void onCameraSwitchError(String s) {

            }
        });

        //mPublisher.switchCameraFace((mPublisher.getCamraId() + 1) % Camera.getNumberOfCameras());
        //mPublisher.switchCamera();
    }


    public void endLive(String liveId) {
        this.liveId = liveId;
        popup.setVisibility(View.VISIBLE);
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    @Override
    public void onBackPressed() {
        endLive(liveId);
    }


    public void startThumbPlayer1(final String url, String thumbPic, final String connId) {

        thumbContainer1.setVisibility(View.VISIBLE);
        thumbcountdown.setVisibility(View.VISIBLE);
        thumbLoading.setVisibility(View.VISIBLE);


        new CountDownTimer(3000, 1000) {


            @Override
            public void onTick(long millisUntilFinished) {


                thumbCount.setText(String.valueOf(millisUntilFinished / 1000));


            }

            @Override
            public void onFinish() {


                Uri uri = Uri.parse("rtmp://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com:1935/videochat/" + url);


                /*BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
                TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

//Create the player
                thumbPlayer1 = ExoPlayerFactory.newSimpleInstance(VideoBroadcaster.this, trackSelector, new DefaultLoadControl(
                        new DefaultAllocator(true, 1000),
                        1000,  // min buffer 0.5s
                        2000, //max buffer 3s
                        1000, // playback 1s
                        1000,   //playback after rebuffer 1s
                        1,
                        true
                ));

                thumbPlayerView1.setPlayer(thumbPlayer1);

                thumbPlayerView1.setUseController(false);

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

                        Log.d("ddata", String.valueOf(playbackState));

                        if (playWhenReady) {
                            thumbLoading.setVisibility(View.GONE);
                        }

                        *//*if (playbackState == 4) {
                            progress.setVisibility(View.VISIBLE);

                            final bean b = (bean) getApplicationContext();

                            final Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(b.BASE_URL)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            final AllAPIs cr = retrofit.create(AllAPIs.class);

                            Call<String> call = cr.endConnection(connId);

                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {


                                    progress.setVisibility(View.GONE);

                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    progress.setVisibility(View.GONE);
                                }
                            });


                        }
*//*
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
                });

                RtmpDataSourceFactory rtmpDataSourceFactory = new RtmpDataSourceFactory();
// This is the MediaSource representing the media to be played.
                final MediaSource videoSource = new ExtractorMediaSource.Factory(rtmpDataSourceFactory)
                        .createMediaSource(uri);

                thumbPlayer1.prepare(videoSource);

                thumbPlayer1.setPlayWhenReady(true);


                thumbPlayerView1.setVisibility(View.VISIBLE);
*/

                //thumbPlayerView1.setVisibility(View.VISIBLE);

                StreamOptions streamOptions = new StreamOptions(url);

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
                                    progress.setVisibility(View.VISIBLE);

                                    final bean b = (bean) getApplicationContext();

                                    final Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(b.BASE_URL)
                                            .addConverterFactory(ScalarsConverterFactory.create())
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    final AllAPIs cr = retrofit.create(AllAPIs.class);

                                    Call<String> call = cr.endConnection(connId);

                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {


                                            progress.setVisibility(View.GONE);

                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            progress.setVisibility(View.GONE);
                                        }
                                    });

                                } else if (StreamStatus.PLAYING.equals(streamStatus)) {
                                    thumbLoading.setVisibility(View.GONE);
                                }

                            }
                        });
                    }
                });

                /*
                 * Method Stream.play() is called to start playback of the stream.
                 */
                playStream.play();


                thumbcountdown.setVisibility(View.GONE);


            }
        }.start();


    }


    public void startCountDown() {

        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                countdown.setText(String.valueOf((millisUntilFinished / 1000) + 1));

            }

            @Override
            public void onFinish() {


                updateLive();


            }
        }.start();


    }


    public void updateLive() {

        progress.setVisibility(View.VISIBLE);

        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<String> call = cr.updateLive(liveId);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.body().equals("success")) {

                    chronometer.setBase(SystemClock.elapsedRealtime());

                    countDownPopup.setVisibility(View.GONE);

                    chronometer.start();


                } else {

                    Toast.makeText(VideoBroadcaster.this, "Error going live", Toast.LENGTH_SHORT).show();

                }

                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }


    public void endThumbPlayer1() {
        try {
            playStream.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }


        //thumbPlayerView1.setVisibility(View.GONE);
        thumbContainer1.setVisibility(View.GONE);

        localRender.setVisibility(View.GONE);
        localRender.setVisibility(View.VISIBLE);

        

    }

    public void handleException(Exception e) {
        try {

            session.disconnect();

            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            //mPublisher.stopPublish();
            //mPublisher.stopRecord();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
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