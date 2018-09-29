package com.yl.youthlive;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.exoplayer2.ui.PlayerView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoPlayer extends AppCompatActivity {

    String liveId;


    ProgressBar progress;


    String TAG = "VideoPlayer";


    String connId;


    TextView stateText;


    String loadingpic;


    ViewPager pager;

    RelativeLayout player_camera_layout1;

    RelativeLayout container;
    ImageView loading;

    ProgressBar loadingProgress;

    PlayerView thumbSurface2;

    View loadingPopup;

    View thumbCountdown;

    TextView thumbCount;

    private Session session;

    private Stream playStream;

    private SurfaceViewRenderer remoteRender;

    private Stream publishStream;

    private SurfaceViewRenderer localRender;

    private Stream thumbStream;

    private SurfaceViewRenderer thumbRender;

    private PercentFrameLayout thumbRenderLayout;

    boolean isThumbCamera1 = false;

    ProgressBar thumbProgress1;

    BroadcastReceiver headsetPlug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Flashphoner.init(this);




        headsetPlug = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {



                if (Objects.equals(intent.getAction(), Intent.ACTION_HEADSET_PLUG)) {
                    boolean connectedHeadphones = (intent.getIntExtra("state", 0) == 1);

                    if (connectedHeadphones) {

                        Flashphoner.getAudioManager().setUseSpeakerPhone(false);

                    } else {

                        Flashphoner.getAudioManager().setUseSpeakerPhone(true);

                    }



                }


            }
        };





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

        thumbRender = findViewById(R.id.thumb_renderer);

        thumbRenderLayout = findViewById(R.id.thumb_renderer_layout);


        thumbSurface2 = findViewById(R.id.thumb_frame2);

        pager = findViewById(R.id.pager);


        remoteRender = findViewById(R.id.remote_video_view);
        PercentFrameLayout remoteRenderLayout = findViewById(R.id.remote_video_layout);

        remoteRenderLayout.setPosition(0, 0, 100, 100);
        remoteRender.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        remoteRender.setMirror(false);
        remoteRender.requestLayout();

        localRender = findViewById(R.id.local_video_view);

        PercentFrameLayout localRenderLayout = findViewById(R.id.local_video_layout);

        localRender.setZOrderMediaOverlay(true);

        localRenderLayout.setPosition(0, 0, 100, 100);
        localRender.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        localRender.setMirror(true);
        localRender.requestLayout();


        thumbRender.setZOrderMediaOverlay(true);


        thumbRenderLayout.setPosition(0, 0, 100, 100);
        thumbRender.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        thumbRender.setMirror(false);
        thumbRender.requestLayout();


        URI u = null;
        try {
            u = new URI("ws://13.232.31.52:8080");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String url = null;
        if (u != null) {
            url = u.getScheme() + "://" + u.getHost() + ":" + u.getPort();
        }


        SessionOptions sessionOptions = new SessionOptions(url);
        sessionOptions.setRemoteRenderer(remoteRender);
        sessionOptions.setLocalRenderer(localRender);

        session = Flashphoner.createSession(sessionOptions);

        session.on(new SessionEvent() {
            @Override
            public void onAppData(Data data) {

            }

            @Override
            public void onConnected(final Connection connection) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        StreamOptions streamOptions = new StreamOptions(liveId);


                        playStream = session.createStream(streamOptions);

                        playStream.on(new StreamStatusEvent() {
                            @Override
                            public void onStreamStatus(final Stream stream, final StreamStatus streamStatus) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.d("ssttaattuuss", String.valueOf(streamStatus));

                                        if (StreamStatus.PLAYING.equals(streamStatus)) {
                                            loadingPopup.setVisibility(View.GONE);
                                            loading.setVisibility(View.GONE);
                                            Log.d("ssttaattuuss", "playing " + stream.getName() + " " + streamStatus);

                                        } else if (StreamStatus.NOT_ENOUGH_BANDWIDTH.equals(streamStatus)) {
                                            Log.d("ssttaattuuss", "Not enough bandwidth stream " + stream.getName() + ", consider using lower video resolution or bitrate. " +
                                                    "Bandwidth " + (Math.round(stream.getNetworkBandwidth() / 1000)) + " " +
                                                    "bitrate " + (Math.round(stream.getRemoteBitrate() / 1000)));
                                        } else if (StreamStatus.FAILED.equals(streamStatus)){
                                            onEror(loadingpic);
                                        }


                                    }
                                });
                            }
                        });

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

        session.connect(new Connection());

        FragAdapter adapter = new FragAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        registerReceiver(headsetPlug, intentFilter);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (session != null) {
            session.disconnect();
        }

        if (headsetPlug != null) {
            unregisterReceiver(headsetPlug);
            headsetPlug = null;
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public class FragAdapter extends FragmentStatePagerAdapter {

        FragAdapter(FragmentManager fm) {
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

    public void startThumbCamera1(final String connId) {


        progress.setVisibility(View.VISIBLE);
        Log.d("rreess", connId);


        final bean b = (bean) getApplicationContext();


        Call<String> call1 = b.getRetrofit().acceptReject2(connId, liveId + SharePreferenceUtils.getInstance().getString("userId"), "2", SharePreferenceUtils.getInstance().getString("userId"));
        call1.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                Log.d("rreess", response.body());

                isThumbCamera1 = true;
                progress.setVisibility(View.GONE);
                player_camera_layout1.setVisibility(View.VISIBLE);


                localRender.setVisibility(View.VISIBLE);

                thumbCountdown.setVisibility(View.VISIBLE);


                VideoPlayer.this.connId = connId;

                StreamOptions streamOptions = new StreamOptions(liveId + SharePreferenceUtils.getInstance().getString("userId"));

                publishStream = session.createStream(streamOptions);

                publishStream.on(new StreamStatusEvent() {
                    @Override
                    public void onStreamStatus(final Stream stream, final StreamStatus streamStatus) {

                        Log.d("ssttaattuuss", String.valueOf(streamStatus));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                /*if (StreamStatus.PUBLISHING.equals(streamStatus)) {

                                } else {
                                    Log.e("Streamer", "Can not publish stream " + stream.getName() + " " + streamStatus);
                                }*/

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
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                progress.setVisibility(View.GONE);
                Log.d("rreess", t.toString());
                t.printStackTrace();
            }
        });


    }


    public void startThumbCamera1FromPlayer(final String connId) {

        player_camera_layout1.setVisibility(View.VISIBLE);

        localRender.setVisibility(View.VISIBLE);

        thumbCountdown.setVisibility(View.VISIBLE);

        this.connId = connId;

        StreamOptions streamOptions = new StreamOptions(liveId + SharePreferenceUtils.getInstance().getString("userId"));

        publishStream = session.createStream(streamOptions);

        publishStream.on(new StreamStatusEvent() {
            @Override
            public void onStreamStatus(final Stream stream, final StreamStatus streamStatus) {

                Log.d("ssttaattuuss", String.valueOf(streamStatus));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*if (StreamStatus.PUBLISHING.equals(streamStatus)) {

                        } else {
                            Log.e("Streamer", "Can not publish stream " + stream.getName() + " " + streamStatus);
                        }
*/
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


        thumbRenderLayout.setVisibility(View.VISIBLE);
        thumbRender.setVisibility(View.VISIBLE);

        thumbRenderLayout.getParent().requestTransparentRegion(remoteRender);

        StreamOptions streamOptions = new StreamOptions(connId);
        streamOptions.setRenderer(thumbRender);

        thumbStream = session.createStream(streamOptions);

        thumbStream.on(new StreamStatusEvent() {
            @Override
            public void onStreamStatus(final Stream stream, final StreamStatus streamStatus) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("ssttaattuuss", String.valueOf(streamStatus));

                        if (StreamStatus.FAILED.equals(streamStatus)) {

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

            localRender.release();
            localRender.setVisibility(View.GONE);


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

        thumbRender.release();
        thumbRender.setVisibility(View.GONE);

        thumbRenderLayout.setVisibility(View.GONE);




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


        Call<String> call = b.getRetrofit().exitPlayer(SharePreferenceUtils.getInstance().getString("userId"), liveId);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                progress.setVisibility(View.GONE);

                finish();

            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

                progress.setVisibility(View.GONE);

            }
        });

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
                    publishStream.publish();
                }
            }
        }
    }

}
