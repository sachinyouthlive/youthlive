package com.app.youthlive;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
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
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.app.youthlive.endLivePOJO.endLiveBean;

import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoBroadcaster extends AppCompatActivity {

    ProgressBar progress;

    TextView stateText;

    boolean torchStatus = false;

    ViewPager pager;


    Toast toast;

    View popup;
    Button end, cancel;
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

    BroadcastReceiver headsetPlug;

    int flag = 0;

    ProgressBar thumbProgress1;

    View thumbLoading;


    LinearLayout previewLayout;

    private Session session;

    private Stream publishStream;

    private SurfaceViewRenderer localRender;


    private Stream playStream;

    private SurfaceViewRenderer remoteRender;


    private Stream testStream;

    private SurfaceViewRenderer testRender;

    @SuppressLint({"CommitPrefEdits", "ShowToast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_broadcaster);

        Flashphoner.init(this);

        Flashphoner.getAudioManager().setUseSpeakerPhone(true);

        pref = getSharedPreferences("offline", Context.MODE_PRIVATE);
        editor = pref.edit();

        toast = Toast.makeText(this, null, Toast.LENGTH_SHORT);


        thumbLoading = findViewById(R.id.thumb_loading);
        previewLayout = findViewById(R.id.preview_layout);

        thumbProgress1 = findViewById(R.id.progressBar13);

        DoubleBounce doubleBounce = new DoubleBounce();
        thumbProgress1.setIndeterminateDrawable(doubleBounce);


//        Log.d("offline" , String.valueOf(db.queries().getAll().size()));

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

        chronometer = findViewById(R.id.chronometer);
        thumbcountdown = findViewById(R.id.thumb_countdown);
        thumbCount = findViewById(R.id.textView33);

        countDownPopup = findViewById(R.id.countdown_popup);
        countdown = findViewById(R.id.textView29);
        thumbContainer1 = findViewById(R.id.thumb_container1);


        localRender = findViewById(R.id.local_video_view);


        PercentFrameLayout localRenderLayout = findViewById(R.id.local_video_layout);


        localRender.setZOrderMediaOverlay(true);


        localRenderLayout.setPosition(0, 0, 100, 100);
        localRender.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        localRender.setMirror(true);
        localRender.requestLayout();

        remoteRender = findViewById(R.id.remote_video_view);
        PercentFrameLayout remoteRenderLayout = findViewById(R.id.remote_video_layout);

        remoteRender.setZOrderOnTop(true);

        remoteRenderLayout.setPosition(0, 0, 100, 100);
        remoteRender.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        remoteRender.setMirror(false);
        remoteRender.requestLayout();


        testRender = findViewById(R.id.test_video_view);
        PercentFrameLayout testRenderLayout = findViewById(R.id.test_video_layout);

        testRender.setZOrderOnTop(true);

        testRenderLayout.setPosition(0, 0, 100, 100);
        testRender.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        testRender.setMirror(false);
        testRender.requestLayout();



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

                end.setClickable(false);
                end.setEnabled(false);

                progress.setVisibility(View.VISIBLE);

                bean b = (bean) getApplicationContext();


                Call<endLiveBean> call = b.getRetrofit().endLive(SharePreferenceUtils.getInstance().getString("userId"), liveId);

                call.enqueue(new Callback<endLiveBean>() {
                    @Override
                    public void onResponse(@NonNull Call<endLiveBean> call, @NonNull Response<endLiveBean> response) {


                        if (response.body() != null && response.body().getStatus().equals("1")) {

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
                    public void onFailure(@NonNull Call<endLiveBean> call, @NonNull Throwable t) {

                        end.setEnabled(true);
                        end.setClickable(true);

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


    public class FragAdapter extends FragmentStatePagerAdapter {

        FragAdapter(FragmentManager fm) {
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

            if (session != null) {
                session.disconnect();
                chronometer.stop();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        super.onDestroy();

    }

    public void switchTorch() {
        torchStatus = !torchStatus;
    }


    public void startPublish(final String liveId) {

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
        sessionOptions.setLocalRenderer(localRender);
        sessionOptions.setRemoteRenderer(remoteRender);
        sessionOptions.setRemoteRenderer(testRender);


        session = Flashphoner.createSession(sessionOptions);

        session.on(new SessionEvent() {
            @Override
            public void onAppData(Data data) {


                Log.d("fffffffff" , data.toString());


            }

            @Override
            public void onConnected(final Connection connection) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        StreamOptions streamOptions = new StreamOptions(liveId);

                        publishStream = session.createStream(streamOptions);

                        publishStream.on(new StreamStatusEvent() {

                            @Override
                            public void onStreamStatus(final Stream stream, final StreamStatus streamStatus) {

                                Log.d("ssttaattuuss", String.valueOf(streamStatus));

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {


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


    public void startThumbPlayer1(final String url, final String connId) {

        thumbContainer1.setVisibility(View.VISIBLE);
        remoteRender.setVisibility(View.VISIBLE);
        thumbcountdown.setVisibility(View.VISIBLE);
        thumbLoading.setVisibility(View.VISIBLE);


        new CountDownTimer(3000, 1000) {


            @Override
            public void onTick(long millisUntilFinished) {


                thumbCount.setText(String.valueOf(millisUntilFinished / 1000));


            }

            @Override
            public void onFinish() {


                StreamOptions streamOptions = new StreamOptions(url);

                playStream = session.createStream(streamOptions);

                playStream.on(new StreamStatusEvent() {
                    @Override
                    public void onStreamStatus(final Stream stream, final StreamStatus streamStatus) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Log.d("ssttaattuuss", String.valueOf(streamStatus));

                                if (StreamStatus.PLAYING.equals(streamStatus)) {

                                    thumbLoading.setVisibility(View.GONE);

                                } else if (StreamStatus.NOT_ENOUGH_BANDWIDTH.equals(streamStatus)) {
                                    Log.d("ssttaattuuss", "Not enough bandwidth stream " + stream.getName() + ", consider using lower video resolution or bitrate. " +
                                            "Bandwidth " + (Math.round(stream.getNetworkBandwidth() / 1000)) + " " +
                                            "bitrate " + (Math.round(stream.getRemoteBitrate() / 1000)));
                                } else if (StreamStatus.FAILED.equals(streamStatus)) {
                                    progress.setVisibility(View.VISIBLE);

                                    final bean b = (bean) getApplicationContext();

                                    Call<String> call = b.getRetrofit().endConnection(connId);

                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {


                                            progress.setVisibility(View.GONE);

                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                            progress.setVisibility(View.GONE);
                                        }
                                    });

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


        Call<String> call = b.getRetrofit().updateLive(liveId);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                if (response.body() != null) {
                    if (response.body().equals("success")) {

                        chronometer.setBase(SystemClock.elapsedRealtime());

                        countDownPopup.setVisibility(View.GONE);

                        chronometer.start();


                    } else {

                        Toast.makeText(VideoBroadcaster.this, "Error going live", Toast.LENGTH_SHORT).show();

                    }
                }

                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }


    public void endThumbPlayer1() {
        try {
            playStream.stop();
            playStream = null;

        } catch (Exception e) {
            e.printStackTrace();
        }

        remoteRender.release();
        remoteRender.setVisibility(View.GONE);
        thumbContainer1.setVisibility(View.GONE);

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

    public void startPreview()
    {
        StreamOptions streamOptions = new StreamOptions(liveId);

        testStream = session.createStream(streamOptions);

        testStream.muteAudio();

        testStream.on(new StreamStatusEvent() {
            @Override
            public void onStreamStatus(final Stream stream, final StreamStatus streamStatus) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });


        testStream.play();
        testRender.setVisibility(View.VISIBLE);
        previewLayout.setVisibility(View.VISIBLE);
    }

    public void stopPreview()
    {
        testStream.stop();
        testRender.release();
        testRender.setVisibility(View.GONE);
        previewLayout.setVisibility(View.GONE);
    }

}