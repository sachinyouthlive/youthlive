package com.yl.youthlive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.faucamp.simplertmp.RtmpHandler;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
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
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.endLivePOJO.Data;
import com.yl.youthlive.followPOJO.followBean;
import com.yl.youthlive.pl.widget.MediaController;

import net.ossrs.yasea.SrsCameraView;
import net.ossrs.yasea.SrsEncodeHandler;
import net.ossrs.yasea.SrsPublisher;
import net.ossrs.yasea.SrsRecordHandler;

import java.io.IOException;
import java.net.SocketException;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;

public class VideoPlayerFragment extends Fragment implements PLMediaPlayer.OnCompletionListener, SrsEncodeHandler.SrsEncodeListener, SrsRecordHandler.SrsRecordListener, RtmpHandler.RtmpListener{

    String liveId;
    String tid , tname;


    ProgressBar progress;

    private Uri uri;


    String TAG = "VideoPlayer";


    String connId;


    TextView stateText;


    SrsCameraView thumbCamera1, thumbCamera2;


    String loadingpic;


    ViewPager pager;

    RelativeLayout player_camera_layout1;

    RelativeLayout container;
    ImageView loading;

    PLVideoTextureView mainPlayerView;
    //SimpleExoPlayer mainPlayer;

    ProgressBar loadingProgress;

    PlayerView thumbSurface1, thumbSurface2;
    SimpleExoPlayer thumbPlayer1, thumbPlayer2;

    View loadingPopup;

    View thumbCountdown;

    TextView thumbCount;

    TextView earphones;

    BroadcastReceiver headsetPlug;

    boolean isThumbCamera1 = false;

    ProgressBar thumbProgress1;

    private int mDisplayAspectRatio = PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT;

    ImageView endImage;
    TextView endTime , endUsers;
    CircleImageView endProfile;
    TextView endName;
    Button endFollow;
    Button endBack;
    ProgressBar endProgress;



    View endPopup;

    BroadcastReceiver endReceiver;

    HomeActivity homeActivity;

    public void setHomeActivity(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_video_player , container , false);

        liveId = getArguments().getString("uri");
        loadingpic = getArguments().getString("pic");
        tid = getArguments().getString("tid");
        tname = getArguments().getString("tname");

        homeActivity.fragTag = getTag();

        earphones = view.findViewById(R.id.earphones);

        thumbProgress1 = view.findViewById(R.id.thumb_progress1);

        endImage = view.findViewById(R.id.imageView8);
        endTime = view.findViewById(R.id.textView19);
        endUsers = view.findViewById(R.id.textView20);
        endProfile = view.findViewById(R.id.view7);
        endName = view.findViewById(R.id.textView21);
        endFollow = view.findViewById(R.id.button5);
        endBack = view.findViewById(R.id.button6);
        endProgress = view.findViewById(R.id.progressBar7);
        endPopup = view.findViewById(R.id.end_popup);


        DoubleBounce doubleBounce = new DoubleBounce();
        thumbProgress1.setIndeterminateDrawable(doubleBounce);


        thumbCountdown = view.findViewById(R.id.thumb_countdown);
        thumbCount = view.findViewById(R.id.textView33);


        headsetPlug = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                    boolean connectedHeadphones = (intent.getIntExtra("state", 0) == 1);

                    if (connectedHeadphones) {


                        if (isThumbCamera1) {
                            earphones.setVisibility(View.GONE);
                        }


                    } else {


                        if (isThumbCamera1) {
                            earphones.setVisibility(View.VISIBLE);
                        }


                    }


                }


            }
        };


        loadingPopup = view.findViewById(R.id.loading_popup);

        loading = view.findViewById(R.id.loading);
        loadingProgress = view.findViewById(R.id.loading_progress);

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

                Blurry.with(getContext()).from(loadedImage).into(loading);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });


        mainPlayerView = view.findViewById(R.id.main_player);

        progress = view.findViewById(R.id.progressBar4);
        stateText = view.findViewById(R.id.textView3);

        container = view.findViewById(R.id.view2);

        player_camera_layout1 = view.findViewById(R.id.thumb_camera_layout1);

        thumbCamera1 = view.findViewById(R.id.thumb_camera1);
        thumbCamera2 = view.findViewById(R.id.thumb_camera2);


        thumbSurface1 = view.findViewById(R.id.thumb_frame1);
        thumbSurface2 = view.findViewById(R.id.thumb_frame2);

        pager = view.findViewById(R.id.pager);


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

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);


        MediaController mMediaController = new MediaController(getContext(), false, true);

        //mainPlayerView.setMediaController(mMediaController);


        mainPlayerView.setBufferingIndicator(loadingPopup);

        mainPlayerView.setOnCompletionListener(new PLMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(PLMediaPlayer plMediaPlayer) {
                Log.d("completecode", "completed");
                onEror(loadingpic , tid , tname);
            }
        });
        mainPlayerView.setOnErrorListener(new PLMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(PLMediaPlayer plMediaPlayer, int i) {

                Log.d("errorcode", String.valueOf(i));

                if (i == -1)
                {
                    onEror(loadingpic , tid , tname);
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


        mainPlayerView.setDisplayOrientation(0);

        mainPlayerView.setMirror(true);

        //mainPlayerView.setVideoPath("rtmp://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com:1935/vod/mi6.mp4");
        mainPlayerView.setVideoPath("rtmp://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com:1935/connection/" + liveId);
        mainPlayerView.start();

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


        endReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals("eenndd")) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications

                    try {

                        String image = intent.getStringExtra("image");
                        String tid = intent.getStringExtra("tid");
                        String tname = intent.getStringExtra("tname");
                        String ltime = intent.getStringExtra("ltime");
                        String views = intent.getStringExtra("views");

                        onEndListener(image , tid , tname , ltime , views);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    //displayFirebaseRegId();

                }/* else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }*/
            }
        };


        LocalBroadcastManager.getInstance(getContext()).registerReceiver(endReceiver,
                new IntentFilter("eenndd"));


        FragAdapter adapter = new FragAdapter(getChildFragmentManager());

        pager.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainPlayerView.stopPlayback();

        try {
            thumbPlayer1.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mPublisher.stopPublish();
            mPublisher.stopRecord();
        } catch (Exception e) {
            e.printStackTrace();
        }

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(endReceiver);

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
    public void onCompletion(PLMediaPlayer plMediaPlayer) {

    }


    public void onEror(String image, final String timelineId, String timelineName) {


        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();

        ImageLoader loader = ImageLoader.getInstance();

        loader.loadImage(image, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                Blurry.with(getContext()).from(loadedImage).into(endImage);
                endProfile.setImageBitmap(loadedImage);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        //loader.displayImage(image , background , options);
        //loader.displayImage(image , profile , options);

        endName.setText(timelineName);
        endUsers.setVisibility(View.GONE);
        endTime.setVisibility(View.GONE);



        endFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress.setVisibility(View.VISIBLE);

                final bean b = (bean) getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                retrofit2.Call<followBean> call = cr.follow(b.userId, timelineId);

                call.enqueue(new retrofit2.Callback<followBean>() {
                    @Override
                    public void onResponse(retrofit2.Call<followBean> call, retrofit2.Response<followBean> response) {

                        if (response.body().getStatus().equals("1")) {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            endFollow.setVisibility(View.GONE);
                        }


                        progress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(retrofit2.Call<followBean> call, Throwable t) {

                        progress.setVisibility(View.GONE);

                    }
                });


            }
        });


        endBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                homeActivity.removeFrag();


            }
        });


        endPopup.setVisibility(View.VISIBLE);


    }


    public void onEndListener(String image, final String timelineId, String timelineName, String liveTime, String viewers) {


        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();

        ImageLoader loader = ImageLoader.getInstance();

        loader.loadImage(image, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                Blurry.with(getContext()).from(loadedImage).into(endImage);
                endProfile.setImageBitmap(loadedImage);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        //loader.displayImage(image , background , options);
        //loader.displayImage(image , profile , options);

        endName.setText(timelineName);
        endUsers.setText(viewers);
        endTime.setText(getDurationString(Integer.parseInt(liveTime)));



        endBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                homeActivity.removeFrag();


            }
        });



        endFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress.setVisibility(View.VISIBLE);

                final bean b = (bean) getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                retrofit2.Call<followBean> call = cr.follow(b.userId, timelineId);

                call.enqueue(new retrofit2.Callback<followBean>() {
                    @Override
                    public void onResponse(retrofit2.Call<followBean> call, retrofit2.Response<followBean> response) {

                        if (response.body().getStatus().equals("1")) {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            endFollow.setVisibility(View.GONE);
                        }


                        progress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(retrofit2.Call<followBean> call, Throwable t) {

                        progress.setVisibility(View.GONE);

                    }
                });


            }
        });



        endPopup.setVisibility(View.VISIBLE);


    }

    private String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
    }

    private String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
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
                frag.setHomeActivity(homeActivity);
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

    SrsPublisher mPublisher;

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

                thumbCamera1.setVisibility(View.VISIBLE);
                thumbCountdown.setVisibility(View.VISIBLE);


                VideoPlayerFragment.this.connId = connId;

                final bean b = (bean) getApplicationContext();

                mPublisher = new SrsPublisher(thumbCamera1);

                mPublisher.setEncodeHandler(new SrsEncodeHandler(VideoPlayerFragment.this));
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
                mPublisher.setRecordHandler(new SrsRecordHandler(VideoPlayerFragment.this));
                /*mPublisher.getmCameraView().open_camera();


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

*/

                mPublisher = new SrsPublisher(thumbCamera1);
/*

        mPublisher.setEncoderHandler(new EncoderjHandler(this));
        mPublisher.setRtmpHandler(new RtmpHandler(this));
        mPublisher.setRecordEventHandler(new RecordHandler(this));
*/


                mPublisher.setEncodeHandler(new SrsEncodeHandler(VideoPlayerFragment.this));
                mPublisher.setRtmpHandler(new RtmpHandler(VideoPlayerFragment.this));
                mPublisher.setRecordHandler(new SrsRecordHandler(VideoPlayerFragment.this));


                thumbCamera1.startCamera();


                //mPublisher.setCameraFacing(1);

                mPublisher.setScreenOrientation(Configuration.ORIENTATION_PORTRAIT);

                mPublisher.setVideoBitRate(200 * 1024);


                mPublisher.setOutputResolution(144 * 3, 192 * 3);
                mPublisher.setPreviewResolution(192 * 3, 144 * 3);

                mPublisher.startPublish("rtmp://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com:1935/videochat/" + liveId + b.userId);


                new CountDownTimer(8000, 1000) {

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

        thumbCamera1.setVisibility(View.VISIBLE);
        thumbCountdown.setVisibility(View.VISIBLE);


        this.connId = connId;

        final bean b = (bean) getApplicationContext();

        mPublisher = new SrsPublisher(thumbCamera1);

        mPublisher.setEncodeHandler(new SrsEncodeHandler(VideoPlayerFragment.this));
        mPublisher.setRtmpHandler(new RtmpHandler(new RtmpHandler.RtmpListener() {
            @Override
            public void onRtmpConnecting(String s) {

            }

            @Override
            public void onRtmpConnected(String s) {

                isThumbCamera1 = true;

                /*progress.setVisibility(View.VISIBLE);

                Log.d("rreess" , connId);



                final bean b = (bean) getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);

                Call<String> call1 = cr.acceptReject2(connId, liveId + b.userId, "2" , b.userId);
                call1.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        Log.d("rreess" , response.body());

                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                        Log.d("rreess" , t.toString());
                        t.printStackTrace();
                    }
                });*/
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


        }));
        mPublisher.setRecordHandler(new SrsRecordHandler(VideoPlayerFragment.this));

        //mPublisher.getmCameraView().open_camera();

        /*DisplayMetrics displayMetrics = new DisplayMetrics();
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
*/
        thumbCamera1.startCamera();

        //mPublisher.setCameraFacing(1);

        mPublisher.setVideoBitRate(200 * 1024);


        mPublisher.setOutputResolution(144 * 3, 192 * 3);
        mPublisher.setPreviewResolution(192 * 3, 144 * 3);

        mPublisher.startPublish("rtmp://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com:1935/videochat/" + liveId + b.userId);


        new CountDownTimer(8000, 1000) {

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

        thumbSurface1.setVisibility(View.VISIBLE);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

//Create the player
        thumbPlayer1 = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, new DefaultLoadControl(
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
            public void onTimelineChanged(com.google.android.exoplayer2.Timeline timeline, Object manifest, int reason) {

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


    }

    public void endThumbCamera1() {

        try {

            thumbCamera1.stopCamera2();
            mPublisher.stopPublish();
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
            thumbPlayer1.stop();
            thumbPlayer1 = null;
        } catch (Exception e) {
            e.printStackTrace();
        }


        thumbSurface1.setVisibility(View.GONE);

    }

    @Override
    public void onResume() {
        super.onResume();


        mainPlayerView.start();

        /*if (mainPlayer != null)
        {
            mainPlayer.setPlayWhenReady(true);
        }*/

        if (mPublisher != null) {
            thumbCamera1.startCamera();
        }

        /*if (thumbPlayer1 != null)
        {
            thumbPlayer1.setPlayWhenReady(true);
        }*/


    }

    @Override
    public void onPause() {
        super.onPause();

        mainPlayerView.pause();


        if (mPublisher != null) {
            thumbCamera1.stopCamera();
        }

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


    /*@Override
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

    }*/

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
        options2.setInteger(AVOptions.KEY_CACHE_BUFFER_DURATION, 500);
        options2.setInteger(AVOptions.KEY_MAX_CACHE_BUFFER_DURATION, 3000);
        // whether start play automatically after prepared, default value is 1
        options2.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);
    }

}
