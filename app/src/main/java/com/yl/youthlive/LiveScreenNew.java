package com.yl.youthlive;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
/*import com.streamaxia.android.CameraPreview;
import com.streamaxia.android.StreamaxiaPublisher;
import com.streamaxia.android.handlers.EncoderHandler;
import com.streamaxia.android.handlers.RecordHandler;
import com.streamaxia.android.handlers.RtmpHandler;
import com.streamaxia.android.utils.Size;*/
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.checkinPostPOJO.CheckinPostPOJO;

import java.io.IOException;
import java.net.SocketException;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.yl.youthlive.bean.getContext;

public class LiveScreenNew extends AppCompatActivity// implements RtmpHandler.RtmpListener, RecordHandler.RecordListener,
        //EncoderHandler.EncodeListener, MyInterface
{


    // Set default values for the streamer
    /*public final static String streamaxiaStreamName = "demo";
    public final static int bitrate = 500;
    public final static int width = 720;
    public final static int height = 1280;
    private static final int PERMISSIONS_REQUEST_CODE = 0x1;


    //private VideoView player;


    //  MediaPlayerConfig wzPlayerConfig;
    protected GestureDetectorCompat mAutoFocusDetector = null;
    ViewPager pager;
    View popup;
    Button end, cancel;
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    View main;
    @BindView(R.id.camera_preview)
    CameraPreview mCameraView;
    Chronometer simpleChronometer;
    boolean started = false;
    boolean playing = false;
    private boolean mPermissionsGranted = true;
    private String[] mRequiredPermissions = new String[]{
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO
    };
    private String TAG = "ddfsdf";
    private PagerAdapter adap;
    private StreamaxiaPublisher mPublisher;




    *//*private void connectToRoom(String roomName, String accessToken) {
        //configureAudio(true);
        ConnectOptions.Builder connectOptionsBuilder = new ConnectOptions.Builder(accessToken)
                .roomName(roomName);

        *//**//*
     * Add local audio track to connect options to share with participants.
     *//**//*
        if (localAudioTrack != null) {
            connectOptionsBuilder
                    .audioTracks(Collections.singletonList(localAudioTrack));
        }

        //cameraCapturerCompat = new CameraCapturerCompat(this, getAvailableCameraSource());
        cameraCapturerCompat = new CameraCapturerCompat(this, CameraCapturer.CameraSource.BACK_CAMERA);

        if (localVideoTrack == null) {
            localVideoTrack = LocalVideoTrack.create(this, true, cameraCapturerCompat.getVideoCapturer());
//            localVideoTrack.addRenderer(localVideoView);

            *//**//*
     * If connected to a Room then share the local video track.
     *//**//*

        }

        *//**//*
     * Add local video track to connect options to share with participants.
     *//**//*
        if (localVideoTrack != null) {
            connectOptionsBuilder.videoTracks(Collections.singletonList(localVideoTrack));
        }
        room = Video.connect(this, connectOptionsBuilder.build(), roomListener());
        //setDisconnectAction();
    }*//*

    //
// Utility method to check the status of a permissions request for an array of permission identifiers
//
    private static boolean hasPermissions(Context context, String[] permissions) {
        for (String permission : permissions)
            if (context.checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false;

        return true;
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_screennew);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
/*

        ButterKnife.bind(this);
        mPublisher = new StreamaxiaPublisher(mCameraView, this);

        mPublisher.setEncoderHandler(new EncoderHandler(this));
        mPublisher.setRtmpHandler(new RtmpHandler(this));
        mPublisher.setRecordEventHandler(new RecordHandler(this));


        main = findViewById(R.id.main);

        pager = findViewById(R.id.pager);


        popup = findViewById(R.id.finish_popup);
        end = popup.findViewById(R.id.end);
        cancel = popup.findViewById(R.id.cancel);

        simpleChronometer = findViewById(R.id.simpleChronometer);
        FragAdapter adap = new FragAdapter(getSupportFragmentManager());

        pager.setAdapter(adap);
////
*/
/*
        mPublisher = new StreamaxiaPublisher(mCameraView, this);

        mPublisher.setEncoderHandler(new EncoderHandler(this));
        mPublisher.setRtmpHandler(new RtmpHandler(this));
        mPublisher.setRecordEventHandler(new RecordHandler(this));
        mCameraView.startCamera();

        setStreamerDefaultValues();

        mPublisher.startPublish("rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/live/" + streamaxiaStreamName);
*//*


        ////


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


                popup.setVisibility(View.GONE);
                mPublisher.stopPublish();

                finish();

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popup.setVisibility(View.GONE);

            }
        });
*/


    }

/*    private void PersonBlock() {
        final Dialog dialog = new Dialog(LiveScreenNew.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.blockpersom_dialog);
        dialog.show();
    }

    @Override
    public void onPause() {

        super.onPause();

        mCameraView.stopCamera();
        mPublisher.pauseRecord();

        //goCoderCameraView.stopPreview();

        //mBroadcaster.onActivityPause();
    }

    @Override
    public void onResume() {
        super.onResume();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPermissionsGranted = hasPermissions(this, mRequiredPermissions);
            if (!mPermissionsGranted)
                ActivityCompat.requestPermissions(this, mRequiredPermissions, PERMISSIONS_REQUEST_CODE);
        } else
            mPermissionsGranted = true;


        //goCoderCameraView.onResume();


        //mBroadcaster.setCameraSurface(mPreviewSurface);
        //mBroadcaster.onActivityResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPublisher.setScreenOrientation(newConfig.orientation);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        mPermissionsGranted = true;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                // Check the result of each permission granted
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        mPermissionsGranted = false;
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (popup.getVisibility() == View.GONE) {
            popup.setVisibility(View.VISIBLE);
        }

    }


    @Override
    protected void onDestroy() {
        *//*
         * Always disconnect from the room before leaving the Activity to
         * ensure any memory allocated to the Room resource is freed.
         *//*
        *//*if (room != null && room.getState() != RoomState.DISCONNECTED) {
            room.disconnect();
            disconnectedFromOnDestroy = true;
        }*//*

        *//*
         * Release the local audio and video tracks ensuring any memory allocated to audio
         * or video is freed.
         *//*


        super.onDestroy();

        mPublisher.stopPublish();
        mPublisher.stopRecord();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mAutoFocusDetector != null)
            mAutoFocusDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    @Override
    public void onNetworkWeak() {
        Log.d("ttaagg", "network weak");
    }

    @Override
    public void onNetworkResume() {
        Log.d("ttaagg", "network resume");
    }

    @Override
    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {
        Log.d("ttaagg", "illegal argument: " + e.toString());
    }

    @Override
    public void onRecordPause() {
        Log.d("ttaagg", "record pause");
    }

    @Override
    public void onRecordResume() {
        Log.d("ttaagg", "record resume");
    }

    @Override
    public void onRecordStarted(String s) {
        Log.d("ttaagg", "record started");
    }

    @Override
    public void onRecordFinished(String s) {
        Log.d("ttaagg", "record finished");
    }

    @Override
    public void onRecordIllegalArgumentException(IllegalArgumentException e) {
        Log.d("ttaagg", "record illegeal exception: " + e.toString());
    }

    @Override
    public void onRecordIOException(IOException e) {
        Log.d("ttaagg", "record IO : " + e.toString());
    }

    @Override
    public void onRtmpConnecting(String s) {
        Log.d("ttaagg", "rtmp connecting : " + s);
    }

    @Override
    public void onRtmpConnected(String s) {
        Log.d("ttaagg", "rtmp connected : " + s);
    }

    @Override
    public void onRtmpVideoStreaming() {
        //Log.d("ttaagg" , "rtmp video streaming");
    }

    @Override
    public void onRtmpAudioStreaming() {
        //Log.d("ttaagg" , "rtmp audio streaming");
    }

    @Override
    public void onRtmpStopped() {
        Log.d("ttaagg", "rtmp stopped");
        long elapsedMillis = SystemClock.elapsedRealtime() - simpleChronometer.getBase();
        simpleChronometer.stop();
        simpleChronometer.setBase(SystemClock.elapsedRealtime());
        loadData(elapsedMillis);

    }

    @Override
    public void onRtmpDisconnected() {
        Log.d("ttaagg", "rtmp disconnected");
    }

    @Override
    public void onRtmpVideoFpsChanged(double v) {
        Log.d("ttaagg", "fps changed : " + String.valueOf(v));
    }

    @Override
    public void onRtmpVideoBitrateChanged(double v) {
        Log.d("ttaagg", "video bitrate changed : " + String.valueOf(v));
    }

    @Override
    public void onRtmpAudioBitrateChanged(double v) {
        Log.d("ttaagg", "audio bitrate changed : " + String.valueOf(v));
    }

    @Override
    public void onRtmpSocketException(SocketException e) {
        Log.d("ttaagg", "rtmp socket exception : " + e.toString());
    }

    @Override
    public void onRtmpIOException(IOException e) {
        Log.d("ttaagg", "rtmp io : " + e.toString());
    }

    @Override
    public void onRtmpIllegalArgumentException(IllegalArgumentException e) {
        Log.d("ttaagg", "rtmp illegal argument : " + e.toString());
    }

    @Override
    public void onRtmpIllegalStateException(IllegalStateException e) {
        Log.d("ttaagg", "rtmp illegal state : " + e.toString());
    }

    @Override
    public void onRtmpAuthenticationg(String s) {
        Log.d("ttaagg", "rtmp authenticate : " + s);
    }

    @Override
    public void startStreaming(String streamName) {

        mCameraView.startCamera();
        setStreamerDefaultValues();
        mPublisher.startPublish("rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/live/" + streamName);


    }

    public void closeConnections() {

        if (popup.getVisibility() == View.GONE) {
            popup.setVisibility(View.VISIBLE);
        }


    }

    public void closeConnection() {
        mPublisher = new StreamaxiaPublisher(mCameraView, this);

        mPublisher.setEncoderHandler(new EncoderHandler(this));
        mPublisher.setRtmpHandler(new RtmpHandler(this));
        mPublisher.stopPublish();


    }

    public void switchCamera() {
        mPublisher.switchCamera();

    }

    private void setStreamerDefaultValues() {
        List<Size> sizes = mPublisher.getSupportedPictureSizes(getResources().getConfiguration().orientation);
        Size resolution = sizes.get(0);

        Log.d("width", String.valueOf(resolution.width));
        Log.d("height", String.valueOf(resolution.height));
        mPublisher.setVideoOutputResolution(resolution.width, resolution.height, this.getResources().getConfiguration().orientation);
    }

    public void loadData(long millis) {
        //   progress.setVisibility(View.VISIBLE);
        Calendar c1 = Calendar.getInstance();
        int iMonth = c1.get(Calendar.MONTH) + 1;

        int iDay = c1.get(Calendar.DATE);

        final bean b = (bean) getContext().getApplicationContext();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final AllAPIs cr = retrofit.create(AllAPIs.class);
        Call<CheckinPostPOJO> call = cr.postcheckin(Integer.parseInt(b.userId), iDay, iMonth, millis);

        Log.d("userId", b.userId);

        call.enqueue(new Callback<CheckinPostPOJO>() {
            @Override
            public void onResponse(Call<CheckinPostPOJO> call, Response<CheckinPostPOJO> response) {

                try {
                    if (!response.body().getInformation().toString().isEmpty()) {
                        Toast.makeText(LiveScreenNew.this, "data uploaded", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LiveScreenNew.this, "data not uploaded", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //  progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<CheckinPostPOJO> call, Throwable t) {
                //   progress.setVisibility(View.GONE);
            }

        });
    }

    public class FragAdapter extends FragmentStatePagerAdapter {

        public FragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new firstFragNew();
            } else {
                return new secondfragNew();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }*/
}