package com.yl.youthlive;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;
import com.wowza.gocoder.sdk.api.WowzaGoCoder;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcast;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcastConfig;
import com.wowza.gocoder.sdk.api.configuration.WZMediaConfig;
import com.wowza.gocoder.sdk.api.devices.WZAudioDevice;
import com.wowza.gocoder.sdk.api.devices.WZCamera;
import com.wowza.gocoder.sdk.api.devices.WZCameraView;
import com.wowza.gocoder.sdk.api.errors.WZStreamingError;
import com.wowza.gocoder.sdk.api.status.WZState;
import com.wowza.gocoder.sdk.api.status.WZStatus;
import com.wowza.gocoder.sdk.api.status.WZStatusCallback;

import java.io.IOException;

public class LiveScreen extends AppCompatActivity implements WZStatusCallback {


    private static final int PERMISSIONS_REQUEST_CODE = 0x1;
    public WowzaGoCoder goCoder;
    // The GoCoder SDK camera view
    public WZCameraView goCoderCameraView;
    // The broadcast configuration settings
    public WZBroadcastConfig goCoderBroadcastConfig;
    protected GestureDetectorCompat mAutoFocusDetector = null;
    WZBroadcast goCoderBroadcaster;
    ViewPager pager;
    View popup;
    Button end, cancel;
    SharedPreferences pref;


    //private VideoView player;


    //  MediaPlayerConfig wzPlayerConfig;
    SharedPreferences.Editor edit;
    View main;
    boolean started = false;
    boolean playing = false;
    private boolean mPermissionsGranted = true;
    private String[] mRequiredPermissions = new String[]{
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO
    };
    // The GoCoder SDK audio device
    private WZAudioDevice goCoderAudioDevice;
    private String TAG = "ddfsdf";




    /*private void connectToRoom(String roomName, String accessToken) {
        //configureAudio(true);
        ConnectOptions.Builder connectOptionsBuilder = new ConnectOptions.Builder(accessToken)
                .roomName(roomName);

        *//*
     * Add local audio track to connect options to share with participants.
     *//*
        if (localAudioTrack != null) {
            connectOptionsBuilder
                    .audioTracks(Collections.singletonList(localAudioTrack));
        }

        //cameraCapturerCompat = new CameraCapturerCompat(this, getAvailableCameraSource());
        cameraCapturerCompat = new CameraCapturerCompat(this, CameraCapturer.CameraSource.BACK_CAMERA);

        if (localVideoTrack == null) {
            localVideoTrack = LocalVideoTrack.create(this, true, cameraCapturerCompat.getVideoCapturer());
//            localVideoTrack.addRenderer(localVideoView);

            *//*
     * If connected to a Room then share the local video track.
     *//*

        }

        *//*
     * Add local video track to connect options to share with participants.
     *//*
        if (localVideoTrack != null) {
            connectOptionsBuilder.videoTracks(Collections.singletonList(localVideoTrack));
        }
        room = Video.connect(this, connectOptionsBuilder.build(), roomListener());
        //setDisconnectAction();
    }*/

    //
// Utility method to check the status of a permissions request for an array of permission identifiers
//
    private static boolean hasPermissions(Context context, String[] permissions) {
        for (String permission : permissions)
            if (context.checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false;

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        main = findViewById(R.id.main);

        pager = findViewById(R.id.pager);


        popup = findViewById(R.id.finish_popup);
        end = popup.findViewById(R.id.end);
        cancel = popup.findViewById(R.id.cancel);


        FragAdapter adap = new FragAdapter(getSupportFragmentManager());

        pager.setAdapter(adap);


        goCoder = WowzaGoCoder.init(this, "GOSK-1B45-0103-ADB6-6B32-7145");

        //GOSK-1145-0103-BC29-9CE5-F808


        //toast = Toast.makeText(LiveScreen.this , null , Toast.LENGTH_SHORT);

        goCoderCameraView = (WZCameraView) findViewById(R.id.camera_preview);

        goCoderAudioDevice = new WZAudioDevice();


        goCoderBroadcaster = new WZBroadcast();

// Create a configuration instance for the broadcaster
        goCoderBroadcastConfig = new WZBroadcastConfig(WZMediaConfig.FRAME_SIZE_640x480);
        goCoderBroadcastConfig.setVideoBitRate(1200);

        goCoderCameraView.setScaleMode(WZMediaConfig.RESIZE_TO_ASPECT);

// Set the connection properties for the target Wowza Streaming Engine server or Wowza Cloud account

        //goCoderBroadcastConfig.setConnectionParameters(new WZDataMap());

// Designate the camera preview as the video source
        goCoderBroadcastConfig.setVideoBroadcaster(goCoderCameraView);


        if (mPermissionsGranted && goCoderCameraView != null) {
            if (goCoderCameraView.isPreviewPaused())
                goCoderCameraView.onResume();
            else
                goCoderCameraView.startPreview();
        }

// Designate the audio device as the audio broadcaster
        goCoderBroadcastConfig.setAudioBroadcaster(goCoderAudioDevice);

        //title = getIntent().getStringExtra("title");


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


                WZStreamingError configValidationError = goCoderBroadcastConfig.validateForBroadcast();


                if (configValidationError != null) {
                    //Toast.makeText(LiveScreen.this, configValidationError.getErrorDescription(), Toast.LENGTH_LONG).show();
                } else if (goCoderBroadcaster.getStatus().isRunning()) {
                    // Stop the broadcast that is currently running
                    goCoderBroadcaster.endBroadcast(LiveScreen.this);
                } else {
                    // Start streaming
                    goCoderBroadcaster.startBroadcast(goCoderBroadcastConfig, LiveScreen.this);
                }

                popup.setVisibility(View.GONE);

                finish();

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popup.setVisibility(View.GONE);

            }
        });


    }

    private void PersonBlock() {
        final Dialog dialog = new Dialog(LiveScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.blockpersom_dialog);
        dialog.show();
    }

    @Override
    public void onPause() {

        super.onPause();

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


        if (goCoder != null && goCoderCameraView != null) {
            if (mAutoFocusDetector == null)
                mAutoFocusDetector = new GestureDetectorCompat(this, new AutoFocusListener(this, goCoderCameraView));

            WZCamera activeCamera = goCoderCameraView.getCamera();
            if (activeCamera != null && activeCamera.hasCapability(WZCamera.FOCUS_MODE_CONTINUOUS))
                activeCamera.setFocusMode(WZCamera.FOCUS_MODE_CONTINUOUS);
        }


        //mBroadcaster.setCameraSurface(mPreviewSurface);
        //mBroadcaster.onActivityResume();
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

        closeConnection();

    }


    @Override
    protected void onDestroy() {
        /*
         * Always disconnect from the room before leaving the Activity to
         * ensure any memory allocated to the Room resource is freed.
         */
        /*if (room != null && room.getState() != RoomState.DISCONNECTED) {
            room.disconnect();
            disconnectedFromOnDestroy = true;
        }*/

        /*
         * Release the local audio and video tracks ensuring any memory allocated to audio
         * or video is freed.
         */


        super.onDestroy();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mAutoFocusDetector != null)
            mAutoFocusDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onWZStatus(WZStatus wzStatus) {
        final StringBuffer statusMessage = new StringBuffer("Broadcast status: ");

        switch (wzStatus.getState()) {
            case WZState.STARTING:
                statusMessage.append("Broadcast initialization");
                break;

            case WZState.READY:
                statusMessage.append("Ready to begin streaming");
                break;

            case WZState.RUNNING:
                started = true;
                statusMessage.append("Streaming is active");
                break;

            case WZState.STOPPING:
                statusMessage.append("Broadcast shutting down");
                break;

            case WZState.IDLE:
                statusMessage.append("The broadcast is stopped");
                break;

            default:
                return;
        }

        // Display the status message using the U/I thread
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(LiveScreen.this, statusMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onWZError(final WZStatus wzStatus) {
// If an error is reported by the GoCoder SDK, display a message
        // containing the error details using the U/I thread
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                /*Toast.makeText(LiveScreen.this,
                        "Streaming error: " + wzStatus.getLastError().getErrorDescription(),
                        Toast.LENGTH_LONG).show();*/
            }
        });
    }

    public void closeConnection() {


        if (popup.getVisibility() == View.GONE) {
            popup.setVisibility(View.VISIBLE);
        }


    }

    @Override
    protected void onStop() {
        super.onStop();

        goCoderCameraView.stopPreview();


    }

    public class FragAdapter extends FragmentStatePagerAdapter {

        public FragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new firstFrag();
            } else {
                return new secondfrag();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
