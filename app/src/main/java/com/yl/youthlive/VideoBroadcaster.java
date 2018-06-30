package com.yl.youthlive;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.streamaxia.android.CameraPreview;
import com.streamaxia.android.StreamaxiaPublisher;
import com.streamaxia.android.handlers.EncoderHandler;
import com.streamaxia.android.handlers.RecordHandler;
import com.streamaxia.android.handlers.RtmpHandler;
import com.streamaxia.android.utils.ScalingMode;
import com.streamaxia.android.utils.Size;
import com.streamaxia.player.StreamaxiaPlayer;
import com.streamaxia.player.listener.StreamaxiaPlayerState;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.endLivePOJO.endLiveBean;
import com.yl.youthlive.followPOJO.followBean;
import com.yl.youthlive.getIpdatedPOJO.Comment;
import com.yl.youthlive.getIpdatedPOJO.getUpdatedBean;
import com.yl.youthlive.goLivePOJO.goLiveBean;
import com.yl.youthlive.liveCommentPOJO.liveCommentBean;
import com.yl.youthlive.requestConnectionPOJO.requestConnectionBean;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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



    boolean torchStatus = false;

    ViewPager pager;



    Toast toast;

    View popup;
    Button end , cancel;
    //ImageButton start;

    String liveId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_broadcaster);


        toast = Toast.makeText(this , null , Toast.LENGTH_SHORT);

        cameraPreview = findViewById(R.id.preview);
        progress = findViewById(R.id.progressBar5);

        popup = findViewById(R.id.finish_popup);
        end = popup.findViewById(R.id.end);
        cancel = popup.findViewById(R.id.cancel);


        frameLayout = findViewById(R.id.video_frame);
        surfaceView = findViewById(R.id.surface_view);

        pager = findViewById(R.id.pager);

        surfaceView.setZOrderMediaOverlay(true);

        stateText = findViewById(R.id.textView3);
        //start = findViewById(R.id.imageButton2);


        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        mPublisher = new StreamaxiaPublisher(cameraPreview, this);

        mPublisher.setEncoderHandler(new EncoderHandler(this));
        mPublisher.setRtmpHandler(new RtmpHandler(this));
        mPublisher.setRecordEventHandler(new RecordHandler(this));

        try {
            cameraPreview.startCamera();
        }catch (Exception e)
        {
            e.printStackTrace();
        }



        cameraPreview.setScalingMode(ScalingMode.TRIM);


        List<Size> sizes = mPublisher.getSupportedPictureSizes(getResources().getConfiguration().orientation);
        final Size resolution = sizes.get(0);
        mPublisher.setVideoOutputResolution(320, 180, this.getResources().getConfiguration().orientation);

        mPublisher.setVideoBitRate(800000);




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

                bean b = (bean)getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<endLiveBean> call = cr.endLive(b.userId , liveId);

                call.enqueue(new Callback<endLiveBean>() {
                    @Override
                    public void onResponse(Call<endLiveBean> call, Response<endLiveBean> response) {


                        if (response.body().getStatus().equals("1"))
                        {

                            Intent intent = new Intent(VideoBroadcaster.this , LiveEndedBroadcaster.class);
                            intent.putExtra("liveTime" , response.body().getData().getLiveTime());
                            intent.putExtra("views" , response.body().getData().getViewers());
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

    public class FragAdapter extends FragmentStatePagerAdapter
    {

        public FragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
            {
                return new BroadcasterFragment1();
            }
            else
            {
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
        //cameraPreview.startCamera();
        mPublisher.resumeRecord();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //cameraPreview.stopCamera();
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

    public void switchTorch()
    {
        if (torchStatus)
        {
            cameraPreview.stopTorch();
            torchStatus = false;
        }
        else
        {
            cameraPreview.startTorch();
            torchStatus = true;
        }
    }


    public void startPublish(String liveId)
    {
        mPublisher.startPublish("rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/live/" + liveId);
    }


    public void switchCamera()
    {
        mPublisher.switchCamera();
    }


    public void endLive(String liveId)
    {
        this.liveId = liveId;
        popup.setVisibility(View.VISIBLE);
    }

    public void setLiveId(String liveId)
    {
        this.liveId = liveId;
    }

    @Override
    public void onBackPressed() {
        endLive(liveId);
    }
}
