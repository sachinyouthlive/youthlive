package com.yl.youthlive;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.streamaxia.android.CameraPreview;
import com.streamaxia.android.StreamaxiaPublisher;
import com.streamaxia.android.handlers.EncoderHandler;
import com.streamaxia.android.handlers.RecordHandler;
import com.streamaxia.android.handlers.RtmpHandler;
import com.streamaxia.android.utils.Size;
import com.streamaxia.player.StreamaxiaPlayer;
import com.streamaxia.player.listener.StreamaxiaPlayerState;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.goLivePOJO.goLiveBean;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class VideoBroadcaster extends AppCompatActivity implements EncoderHandler.EncodeListener, RtmpHandler.RtmpListener, RecordHandler.RecordListener, StreamaxiaPlayerState {

    CameraPreview cameraPreview;
    private StreamaxiaPublisher mPublisher;
    ProgressBar progress;

    AspectRatioFrameLayout frameLayout;
    SurfaceView surfaceView;
    TextView stateText;

    Toast toast;

    ImageButton start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_broadcaster);

        toast = Toast.makeText(this , null , Toast.LENGTH_SHORT);

        cameraPreview = findViewById(R.id.preview);
        progress = findViewById(R.id.progressBar3);

        frameLayout = findViewById(R.id.video_frame);
        surfaceView = findViewById(R.id.surface_view);

        surfaceView.setZOrderOnTop(true);

        stateText = findViewById(R.id.textView3);
        start = findViewById(R.id.imageButton2);


        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        mPublisher = new StreamaxiaPublisher(cameraPreview, this);

        mPublisher.setEncoderHandler(new EncoderHandler(this));
        mPublisher.setRtmpHandler(new RtmpHandler(this));
        mPublisher.setRecordEventHandler(new RecordHandler(this));
        cameraPreview.startCamera();

        List<Size> sizes = mPublisher.getSupportedPictureSizes(getResources().getConfiguration().orientation);
        Size resolution = sizes.get(0);
        mPublisher.setVideoOutputResolution(resolution.width / 2, resolution.height / 2, this.getResources().getConfiguration().orientation);


        progress.setVisibility(View.VISIBLE);
        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);

        Call<goLiveBean> call3 = cr.goLive(b.userId, b.userId, "");

        call3.enqueue(new Callback<goLiveBean>() {
            @Override
            public void onResponse(Call<goLiveBean> call, retrofit2.Response<goLiveBean> response) {

                if (Objects.equals(response.body().getStatus(), "1")) {
                    //Toast.makeText(LiveScreen.this, "You are now live", Toast.LENGTH_SHORT).show();
                    String liveId = response.body().getData().getLiveId();

                    mPublisher.startPublish("rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/live/" + liveId);

                    //actions.setVisibility(View.VISIBLE);

                    //mCallback.startStreaming(liveId);

                    //schedule(liveId);

                } else {
                    //Toast.makeText(getContext(), "Error going on live", Toast.LENGTH_SHORT).show();
                    //lvscreen.finish();
                }

                progress.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<goLiveBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
                //Toast.makeText(getContext() , "Error in going Live" , Toast.LENGTH_SHORT).show();
                //getActivity().finish();
            }

        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/videochat/demo");
                StreamaxiaPlayer mStreamaxiaPlayer = new StreamaxiaPlayer();

                mStreamaxiaPlayer.initStreamaxiaPlayer(surfaceView, frameLayout,
                        stateText, VideoBroadcaster.this, VideoBroadcaster.this, uri);


                mStreamaxiaPlayer.play(uri, StreamaxiaPlayer.TYPE_RTMP);


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        mPublisher.resumeRecord();

    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraPreview.stopCamera();
        mPublisher.pauseRecord();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    @Override
    public void stateENDED() {

        toast.setText("ended");
        toast.show();
    }

    @Override
    public void stateBUFFERING() {
        toast.setText("buffering");
        toast.show();
    }

    @Override
    public void stateIDLE() {
        toast.setText("idle");
        toast.show();
    }

    @Override
    public void statePREPARING() {
        toast.setText("preparing");
        toast.show();
    }

    @Override
    public void stateREADY() {
        toast.setText("ready");
        toast.show();
    }

    @Override
    public void stateUNKNOWN() {
        toast.setText("unknown");
        toast.show();
    }

}
