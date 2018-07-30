package com.yl.youthlive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
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
import com.streamaxia.android.CameraPreview;
import com.streamaxia.android.StreamaxiaPublisher;
import com.streamaxia.android.handlers.EncoderHandler;
import com.streamaxia.android.handlers.RecordHandler;
import com.streamaxia.android.handlers.RtmpHandler;
import com.streamaxia.android.utils.Size;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.endLivePOJO.endLiveBean;

import java.io.IOException;
import java.net.SocketException;
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

public class VideoBroadcaster extends AppCompatActivity implements RtmpHandler.RtmpListener, EncoderHandler.EncodeListener, RecordHandler.RecordListener {

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


    PlayerView thumbPlayerView1;
    SimpleExoPlayer thumbPlayer1;

    RelativeLayout thumbContainer1;


    String liveId;

    View countDownPopup;
    TextSwitcher countdown;

    View thumbcountdown;
    TextView thumbCount;


    Chronometer chronometer;


    boolean isEnded = false;


    SharedPreferences pref;
    SharedPreferences.Editor editor;


    private StreamaxiaPublisher mPublisher;
    CameraPreview cameraPreview;


    BroadcastReceiver headsetPlug;


    TextView earphones;

    ProgressBar thumbProgress1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_broadcaster);

        pref = getSharedPreferences("offline", Context.MODE_PRIVATE);
        editor = pref.edit();

        toast = Toast.makeText(this, null, Toast.LENGTH_SHORT);


        earphones = findViewById(R.id.earphones);
        thumbProgress1 = findViewById(R.id.thumb_progress1);

        DoubleBounce doubleBounce = new DoubleBounce();
        thumbProgress1.setIndeterminateDrawable(doubleBounce);


//        Log.d("offline" , String.valueOf(db.queries().getAll().size()));

        headsetPlug = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                    boolean connectedHeadphones = (intent.getIntExtra("state", 0) == 1);

                    if (connectedHeadphones) {

                        earphones.setVisibility(View.GONE);

                    } else {

                        earphones.setVisibility(View.VISIBLE);

                    }


                }


            }
        };

        chronometer = findViewById(R.id.chronometer);
        thumbcountdown = findViewById(R.id.thumb_countdown);
        thumbCount = findViewById(R.id.textView33);

        countDownPopup = findViewById(R.id.countdown_popup);
        countdown = findViewById(R.id.textView29);
        thumbContainer1 = findViewById(R.id.thumb_container1);


        cameraPreview = findViewById(R.id.preview);
        progress = findViewById(R.id.progressBar5);

        popup = findViewById(R.id.finish_popup);
        end = popup.findViewById(R.id.end);
        cancel = popup.findViewById(R.id.cancel);


        thumbPlayerView1 = findViewById(R.id.video_frame);


        pager = findViewById(R.id.pager);


        stateText = findViewById(R.id.textView3);
        //start = findViewById(R.id.imageButton2);


/*
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
*/

        mPublisher = new StreamaxiaPublisher(cameraPreview, this);
/*

        mPublisher.setEncoderHandler(new EncoderjHandler(this));
        mPublisher.setRtmpHandler(new RtmpHandler(this));
        mPublisher.setRecordEventHandler(new RecordHandler(this));
*/


        mPublisher.setEncoderHandler(new EncoderHandler(this));
        mPublisher.setRtmpHandler(new RtmpHandler(this));
        mPublisher.setRecordEventHandler(new RecordHandler(this));

        /*mPublisher.getmCameraView().open_camera();


        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager)
                getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;


        Camera.Size best_size = mPublisher.getmCameraView().get_best_size(screenWidth, screenHeight);

        Log.d("asdasdasd", String.valueOf(screenWidth));
        Log.d("asdasdasd", String.valueOf(screenHeight));

        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }

        Log.d("asdasdasd", String.valueOf(result));

        if (best_size != null) {
            Log.d("asdasd", "************ Best size is " + (best_size.width * 4)/10 + " Height: " + (best_size.height * 4)/10 + " ********************");


            mPublisher.setPreviewResolution((best_size.width * 4)/10, (best_size.height * 4)/10);
            mPublisher.setOutputResolution((best_size.height * 4)/10, (best_size.width * 4)/10);
            //mPublisher.setPreviewResolution((int) (screenWidth * 0.375), (int) (screenHeight * 0.375));
            //mPublisher.setOutputResolution((int) (screenHeight * 0.375), (int) (screenWidth * 0.375));
        } else {
            Log.d("asdasd", "************ Best size is NULL ********************");
            mPublisher.setPreviewResolution(480, 320);
            mPublisher.setOutputResolution(320, 480);
            //mPublisher.setPreviewResolution((int) (screenWidth * 0.375), (int) (screenHeight * 0.375));
            //mPublisher.setOutputResolution((int) (screenHeight * 0.375), (int) (screenWidth * 0.375));
        }

        mPublisher.setVideoHDMode();
*/


        cameraPreview.startCamera();


        mPublisher.setCameraFacing(1);

        mPublisher.setScreenOrientation(Configuration.ORIENTATION_PORTRAIT);

        Size supportedRes = mPublisher.getSupportedPictureSizes(1).get(1);

        //  Toast.makeText(VideoBroadcaster.this, String.valueOf(supportedRes.width) + " X " + String.valueOf(supportedRes.height), Toast.LENGTH_SHORT).show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager)
                getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.widthPixels;
        int screenWidth = displayMetrics.heightPixels;


        Log.d("asdasdasd", String.valueOf(screenWidth));
        Log.d("asdasdasd", String.valueOf(screenHeight));


        float rat = screenWidth / screenHeight;


        //   Toast.makeText(VideoBroadcaster.this, String.valueOf(screenWidth) + " X " + String.valueOf(screenHeight), Toast.LENGTH_SHORT).show();


        List<Size> sizes = mPublisher.getSupportedPictureSizes(1);
        int bes_width = 0;
        int max_limit = 480;

        Size best_size = null;

        for (Size size : sizes) {
            Log.d("SrsCamera", "Size width:" + size.width + " height:" + size.height);
            if (size.height > bes_width && size.height <= max_limit) {
                if (size.width / size.height == rat) {
                    bes_width = size.width;
                    best_size = size;
                }
            }
        }

        mPublisher.setVideoOutputResolution(best_size.width, best_size.height, 1);




/*
        for (int i = 0 ; i < mPublisher.getSupportedPictureSizes(1).size() ; i++)
        {
            Size s = mPublisher.getSupportedPictureSizes(1).get(i);
            Log.d("asdasdasd" , "1");
            Log.d("asdasdasd" , "width - " + String.valueOf(s.width) + " ; height - " + String.valueOf(s.height));

        }
*/


        mPublisher.setVideoBitRate(500 * 1024);

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

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        registerReceiver(headsetPlug, intentFilter);


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onNetworkWeak() {

        toast.setText("Network Weak");
        toast.show();

    }

    @Override
    public void onNetworkResume() {
        toast.setText("Network Resume");
        toast.show();
    }

    @Override
    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {

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

        if (headsetPlug != null) {
            unregisterReceiver(headsetPlug);
            headsetPlug = null;
        }

        try {
            thumbPlayer1.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (mPublisher != null) {
                mPublisher.stopPublish();
                mPublisher.stopRecord();
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
            cameraPreview.stopTorch();
            torchStatus = false;
        } else {
            cameraPreview.startTorch();
            torchStatus = true;
        }
    }


    public void startPublish(String liveId) {
        mPublisher.startPublish("rtmp://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com:1935/connection/" + liveId);
        //mPublisher.startPublish("rtmp://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com:1935/connection/" + liveId);
    }


    public void switchCamera() {
        mPublisher.switchCamera();
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
        thumbProgress1.setVisibility(View.VISIBLE);


        new CountDownTimer(8000, 1000) {


            @Override
            public void onTick(long millisUntilFinished) {


                thumbCount.setText(String.valueOf(millisUntilFinished / 1000));


            }

            @Override
            public void onFinish() {


                Uri uri = Uri.parse("rtmp://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com:1935/videochat/" + url);


                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
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
                            thumbProgress1.setVisibility(View.GONE);
                        }

                        if (playbackState == 4) {
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

                thumbcountdown.setVisibility(View.GONE);


            }
        }.start();


    }


    public void startCountDown() {

        new CountDownTimer(8000, 1000) {

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

                    Toast.makeText(VideoBroadcaster.this, "Error gounf live", Toast.LENGTH_SHORT).show();

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
            thumbPlayer1.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }


        thumbPlayerView1.setVisibility(View.GONE);
        thumbContainer1.setVisibility(View.GONE);


    }


}
