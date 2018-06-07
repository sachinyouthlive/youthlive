package com.yl.youthlive;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;

import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.streamaxia.player.StreamaxiaPlayer;
import com.streamaxia.player.listener.StreamaxiaPlayerState;
import com.wowza.gocoder.sdk.api.WowzaGoCoder;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcastConfig;
import com.wowza.gocoder.sdk.api.configuration.WZMediaConfig;
import com.wowza.gocoder.sdk.api.devices.WZAudioDevice;
import com.wowza.gocoder.sdk.api.errors.WZStreamingError;
import com.wowza.gocoder.sdk.api.logging.WZLog;
import com.wowza.gocoder.sdk.api.player.WZPlayerConfig;
import com.wowza.gocoder.sdk.api.player.WZPlayerView;
import com.wowza.gocoder.sdk.api.status.WZState;
import com.wowza.gocoder.sdk.api.status.WZStatus;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerActivityNew extends AppCompatActivity implements StreamaxiaPlayerState, MyPlayerInterface {


    String uri;


    // The GoCoder SDK audio device
//    private WZAudioDevice goCoderAudioDevice;

    // The broadcast configuration settings
//    private WZBroadcastConfig goCoderBroadcastConfig;

 //   private WowzaGoCoder goCoder;

    String key;


    //private VlcVideoLibrary vlcVideoLibrary;

//    WZPlayerView mStreamPlayerView;

//    private WZPlayerConfig mStreamPlayerConfig = null;

    String liveId = "";
    String timelineId = "";


    ViewPager pager;
    View popup;

    Button end;


    private Uri uri2;

    private StreamaxiaPlayer mStreamaxiaPlayer = new StreamaxiaPlayer();

    private int STREAM_TYPE = 0;


    @BindView(R.id.video_frame)
    AspectRatioFrameLayout aspectRatioFrameLayout;
    @BindView(R.id.surface_view)
    SurfaceView surfaceView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.player_state_view)
    TextView stateText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playernew);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Toast.makeText(this, "PlayerActivityNew.java", Toast.LENGTH_SHORT).show();



        liveId = getIntent().getStringExtra("liveId");
        timelineId = getIntent().getStringExtra("timelineId");



        popup = findViewById(R.id.popup);

        end = popup.findViewById(R.id.finish);

        pager = (ViewPager) findViewById(R.id.pager);
        FragAdapter adap = new FragAdapter(getSupportFragmentManager());
        pager.setAdapter(adap);

        //

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        STREAM_TYPE=StreamaxiaPlayer.TYPE_RTMP;
        initRTMPExoPlayer();
        String liveid = getIntent().getStringExtra("liveId");
        String urinew = getIntent().getStringExtra("uri");
        String timelineid = getIntent().getStringExtra("timelineId");
        Toast.makeText(this, "liveid"+liveid+"uri"+urinew+"timelineid"+timelineid, Toast.LENGTH_LONG).show();

        uri2=Uri.parse("rtsp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/live/"+liveid);
        mStreamaxiaPlayer.play(uri2, STREAM_TYPE);
        surfaceView.setBackgroundColor(Color.TRANSPARENT);
        progressBar.setVisibility(View.GONE);


        //



      //  goCoder = WowzaGoCoder.init(this, "GOSK-1B45-0103-ADB6-6B32-7145");

        uri = getIntent().getStringExtra("uri");

        //surface = (TextureVideoView) findViewById(R.id.surface);

     //   mStreamPlayerView = findViewById(R.id.surface);


    //    mStreamPlayerConfig = new WZPlayerConfig();


        //videoView = findViewById(R.id.surface);


        //vlcVideoLibrary = new VlcVideoLibrary(this, this, surface);


    //    goCoderAudioDevice = new WZAudioDevice();


// Create a configuration instance for the broadcaster

        //goCoderBroadcastConfig = new WZBroadcastConfig(WZMediaConfig.FRAME_SIZE_176x144);

           //     goCoderBroadcastConfig = new WZBroadcastConfig(WZMediaConfig.FRAME_SIZE_640x480);
        // Set the bitrate to 4000 Kbps
          //       goCoderBroadcastConfig.setVideoBitRate(1200);
        // Create a configuration instance

// Set the connection properties for the target Wowza Streaming Engine server or Wowza Cloud account

        //goCoderBroadcastConfig.setConnectionParameters(new WZDataMap());

// Designate the camera preview as the video source


// Designate the audio device as the audio broadcaster
    //    goCoderBroadcastConfig.setAudioBroadcaster(goCoderAudioDevice);


        //progressDialog.show();


        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FirebaseInstanceId.getInstance().deleteInstanceId();

                            new MyFirebaseInstanceIDService().onTokenRefresh();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                finish();
            }
        });


    }







    /*BroadcastPlayer.Observer mBroadcastPlayerObserver = new BroadcastPlayer.Observer() {
        @Override
        public void onStateChange(PlayerState playerState) {
            Toast.makeText(PlayerActivity.this, playerState.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBroadcastLoaded(boolean live, int width, int height) {
        }
    };*/

  //  @Override
  //  protected void onPause() {

   //     if (mStreamPlayerView != null && mStreamPlayerView.isPlaying()) {
   //         mStreamPlayerView.stop();

            // Wait for the streaming player to disconnect and shutdown...
   //         mStreamPlayerView.getCurrentStatus().waitForState(WZState.IDLE);
      //  }

   //     super.onPause();


        //surface.pause();

        /*mVideoSurface = null;
        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = null;*/
  //  }

    @Override
    protected void onStop() {

        super.onStop();




        //surface.stop();
//        player.stop();

    }


    @Override
    protected void onResume() {
        super.onResume();
        //mVideoSurface = (SurfaceView) findViewById(R.id.PreviewSurfaceView);
        //mPlayerStatusTextView.setText("Loading latest broadcast");
        //getLatestResourceUri();


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

    //    GoCoderSDKPrefs.saveConfigDetails(prefs , uri);
        //GoCoderSDKPrefs.saveConfigDetails(prefs , "sample.mp4");


        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    //    mWZNetworkLogLevel = Integer.valueOf(prefs.getString("wz_debug_net_log_level", String.valueOf(WZLog.LOG_LEVEL_DEBUG)));



  //      if (mStreamPlayerConfig != null)

            Log.d("eeeeee" , "entered");

  //      GoCoderSDKPrefs.updateConfigFromPrefs(prefs, mStreamPlayerConfig);



        //initPlayer(uri);

    }




   /* @Override
    public int Status(int i) {


        Log.d("status", String.valueOf(i));

        //veg.mediaplayer.sdk.MediaPlayer.PlayerNotifyCodes status = veg.mediaplayer.sdk.MediaPlayer.PlayerNotifyCodes.forValue(i);

        if (i == 112) {
            if (popup.getVisibility() == View.GONE) {
                popup.post(new Runnable() {
                    @Override
                    public void run() {
                        popup.setVisibility(View.VISIBLE);
                    }
                });
            }
        }

        return 0;
    }

    @Override
    public int OnReceiveData(ByteBuffer byteBuffer, int i, long l) {
        return 0;
    }*/



    /*void getLatestResourceUri() {
        Request request = new Request.Builder()
                .url("https://api.irisplatform.io/broadcasts")
                .addHeader("Accept", "application/vnd.bambuser.v1+json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + API_KEY)
                .get()
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                runOnUiThread(new Runnable() { @Override public void run() {
                    if (mPlayerStatusTextView != null)
                        mPlayerStatusTextView.setText("Http exception: " + e);
                }});
            }
            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                String body = response.body().string();
                String resourceUri = null;
                try {
                    JSONObject json = new JSONObject(body);
                    JSONArray results = json.getJSONArray("results");
                    JSONObject latestBroadcast = results.optJSONObject(0);
                    resourceUri = latestBroadcast.optString("resourceUri");
                } catch (Exception ignored) {}
                final String uri = resourceUri;
                runOnUiThread(new Runnable() { @Override public void run() {
                    initPlayer(uri);
                }});
            }
        });
    }*/

    void initPlayer(String resourceUri) {


        if (resourceUri == null) {
            //if (mPlayerStatusTextView != null)
            //   mPlayerStatusTextView.setText("Could not get info about latest broadcast");
            return;
        }
        /*if (mVideoSurface == null) {
            // UI no longer active
            return;
        }

        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = new BroadcastPlayer(this, resourceUri, APPLICATION_ID, mBroadcastPlayerObserver);


        mBroadcastPlayer.setSurfaceView(mVideoSurface);
        mBroadcastPlayer.load();*/

        /*WZPlayerConfig wzPlayerConfig = new WZPlayerConfig();

        wzPlayerConfig.setHostAddress("ec2-18-219-154-44.us-east-2.compute.amazonaws.com");
        wzPlayerConfig.setPortNumber(1935);
        wzPlayerConfig.setApplicationName("live");
        wzPlayerConfig.setStreamName(uri);
*/
        //videoView.play();





        /*videoView.play(wzPlayerConfig, new WZStatusCallback() {
            @Override
            public void onWZStatus(WZStatus wzStatus) {

                Log.d("WZStatus:  " , wzStatus.toString());

            }

            @Override
            public void onWZError(WZStatus wzStatus) {

            }
        });*/

        String ur = "rtsp://ec2-18-219-154-44.us-east-2.compute.amazonaws.com:1935/live/" + resourceUri;






        //surface.setScaleType(TextureVideoView.ScaleType.CENTER_CROP);
// Use `setDataSource` method to set data source, this could be url, assets folder or path
        //surface.setDataSource(ur);
        //surface.play();


        /*wzPlayerConfig = new MediaPlayerConfig();


        wzPlayerConfig.setConnectionUrl(ur);

        wzPlayerConfig.setConnectionNetworkProtocol(1);
        wzPlayerConfig.setConnectionDetectionTime(300);
        wzPlayerConfig.setConnectionBufferingTime(0);
        wzPlayerConfig.setConnectionBufferingSize(0);
        wzPlayerConfig.setDecodingType(0);
        wzPlayerConfig.setDecoderLatency(1);
        wzPlayerConfig.setNumberOfCPUCores(0);
        //wzPlayerConfig.setRendererType(1);
        wzPlayerConfig.setSynchroEnable(1);
        wzPlayerConfig.setSynchroNeedDropVideoFrames(0);
        wzPlayerConfig.setEnableAspectRatio(2);
        wzPlayerConfig.setDataReceiveTimeout(2000);
        //wzPlayerConfig.setNumberOfCPUCores(0);


        surfaceView.Open(wzPlayerConfig, this);
*/

        //wzPlayerConfig.setIsPlayback(true);



        /*wzPlayerConfig.setVideoEnabled(true);
        wzPlayerConfig.setVideoFrameWidth(640);
        wzPlayerConfig.setVideoFrameHeight(480);
        wzPlayerConfig.setVideoFramerate(30);
        wzPlayerConfig.setVideoKeyFrameInterval(30);
        wzPlayerConfig.setVideoBitRate(1500);
        wzPlayerConfig.setABREnabled(true);

        WZProfileLevel profileLevel = new WZProfileLevel(1, 1);

        if (profileLevel.validate()) {
            wzPlayerConfig.setVideoProfileLevel(profileLevel);
        }


        wzPlayerConfig.setAudioEnabled(true);

        wzPlayerConfig.setAudioSampleRate(44100);
        wzPlayerConfig.setAudioChannels(1);
        wzPlayerConfig.setAudioBitRate(64000);

*/

        /*wzPlayerConfig.setHostAddress("ec2-18-219-154-44.us-east-2.compute.amazonaws.com");
        wzPlayerConfig.setApplicationName("live");
        wzPlayerConfig.setStreamName(resourceUri);
        wzPlayerConfig.setPortNumber(1935);


        wzPlayerConfig.setVideoEnabled(true);

        //WZStreamingError configValidationError = wzPlayerConfig.validateForPlayback();

        //Log.d("Error Wowza" , configValidationError.getErrorDescription());

        //wzPlayerConfig.setHLSEnabled(true);





        if (surfaceView.isReadyToPlay())
        {



            WZStreamingError configValidationError = wzPlayerConfig.validateForPlayback();
            if (configValidationError != null) {

            } else {
                // Set the detail level for network logging output
                surfaceView.setLogLevel(2);

                // Set the player's pre-buffer duration as stored in the app prefs
                float preBufferDuration = 0;
                wzPlayerConfig.setPreRollBufferDuration(preBufferDuration);

                // Start playback of the live stream
                surfaceView.play(wzPlayerConfig, this);
            }




            wzPlayerConfig.setPreRollBufferDuration(0);

            surfaceView.setLogLevel(1);

            surfaceView.play(wzPlayerConfig , this);

            Log.d("asdasdas" , "ready_to_play");

        }
*/


        /*ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(ur),
                rtmpDataSourceFactory, extractorsFactory, null, null);


        vlcVideoLibrary.play(ur);
*/

//        player.prepare(videoSource);

//        player.setPlayWhenReady(true);


        //vlcVideoLibrary.play(ur);

        /*videoView.setVideoURI(Uri.parse(ur));
        videoView.requestFocus();
        videoView.setKeepScreenOn(true);
        videoView.start();

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {

                Log.d("asdasd" , "video error");

                return true;
            }
        });
*/

    }


    private void initRTMPExoPlayer() {
        mStreamaxiaPlayer.initStreamaxiaPlayer(surfaceView, aspectRatioFrameLayout,
                stateText, this, this, uri2);
    }

    @Override
    public void stateENDED() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void stateBUFFERING() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stateIDLE() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void statePREPARING() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stateREADY() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void stateUNKNOWN() {
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStreamaxiaPlayer.stop();
    }

    @Override
    public void startStreaming(String streamName) {

    }

    @Override
    public void closeConnections() {

    }


    public class FragAdapter extends FragmentStatePagerAdapter {

        public FragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                player_firstNew pf = new player_firstNew();
                Bundle b = new Bundle();
                b.putString("liveId", liveId);
                b.putString("timelineId", timelineId);
                pf.setArguments(b);
                return pf;
            } else {
                return new player_secondNew();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


    /*@Override
    public void syncPreferences() {


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        GoCoderSDKPrefs.saveConfigDetails(prefs , uri);
        //GoCoderSDKPrefs.saveConfigDetails(prefs , "sample.mp4");


        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mWZNetworkLogLevel = Integer.valueOf(prefs.getString("wz_debug_net_log_level", String.valueOf(WZLog.LOG_LEVEL_DEBUG)));



        if (mStreamPlayerConfig != null)

            Log.d("eeeeee" , "entered");

            GoCoderSDKPrefs.updateConfigFromPrefs(prefs, mStreamPlayerConfig);
    }*/


}