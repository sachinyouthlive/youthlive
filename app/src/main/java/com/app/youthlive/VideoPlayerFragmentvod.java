package com.app.youthlive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.app.youthlive.commentPOJO.commentBean;
import com.app.youthlive.followPOJO.followBean;
import com.app.youthlive.internetConnectivity.ConnectivityReceiver;
import com.app.youthlive.sharePOJO.shareBean;
import com.app.youthlive.singleVideoPOJO.Comment;
import com.app.youthlive.singleVideoPOJO.singleVideoBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class VideoPlayerFragmentvod extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {


    private static final String KEY_PLAY_WHEN_READY = "play_when_ready";
    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    CircleImageView profile;
    TextView name, time, views, comments, likes, share;
    RecyclerView grid;
    LinearLayoutManager manager;
    EditText comment;
    CommentsAdapter adapter;
    List<Comment> list;
    ProgressBar progress, progressv;
    String videoId, url, thumb;
    int count = 0;
    String timelineId;
    Button follow;
    ImageView send;
    ImageButton play;
    CustomViewPager pager;
    RelativeLayout container;
    ImageView loading;
    ProgressBar loadingProgress;
    String liveId = "mi6.mp4";
    private Uri uri;
    private PlayerView mPlayerView;
    private SimpleExoPlayer player;
    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;
    private ImageView ivHideControllerButton;
    private boolean playWhenReady;
    private int currentWindow;
    private long playbackPosition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_video_player2, container, false);
        checkConnection();
        assert getArguments() != null;
        videoId = getArguments().getString("videoId");
        list = new ArrayList<>();
        mPlayerView = view.findViewById(R.id.player_view);
        follow = view.findViewById(R.id.follow);
        loadingProgress = view.findViewById(R.id.progress_bar);
        // mainPlayerView = view.findViewById(R.id.main_player);
        // thumb_player=view.findViewById(R.id.thumb_plplayer);
        if (savedInstanceState == null) {
            playWhenReady = true;
            currentWindow = 0;
            playbackPosition = 0;
        } else {
            playWhenReady = savedInstanceState.getBoolean(KEY_PLAY_WHEN_READY);
            currentWindow = savedInstanceState.getInt(KEY_WINDOW);
            playbackPosition = savedInstanceState.getLong(KEY_POSITION);
        }

        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        window = new Timeline.Window();
        //  ivHideControllerButton = view.findViewById(R.id.exo_controller);

        // Log.d("statusss", "rtmp://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com:1935/vod/" + liveId);
        //url="http://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com/softcode/upload/video/maharani.mp4";


        play = view.findViewById(R.id.play);

        share = view.findViewById(R.id.share);

        progress = view.findViewById(R.id.progress);
        progressv = view.findViewById(R.id.progressv);
        profile = view.findViewById(R.id.profile);
        name = view.findViewById(R.id.name);
        time = view.findViewById(R.id.time);
        views = view.findViewById(R.id.views);
        comments = view.findViewById(R.id.comments);
        likes = view.findViewById(R.id.like);
        comment = view.findViewById(R.id.comment);
        grid = view.findViewById(R.id.grid);
        manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new CommentsAdapter(getApplicationContext(), list);
        grid.setLayoutManager(manager);
        grid.setAdapter(adapter);

        send = view.findViewById(R.id.send);

        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                final bean b = (bean) getApplicationContext();

                Call<singleVideoBean> call = b.getRetrofit().likeVideo(SharePreferenceUtils.getInstance().getString("userId"), videoId);
                call.enqueue(new Callback<singleVideoBean>() {
                    @Override
                    public void onResponse(Call<singleVideoBean> call, Response<singleVideoBean> response) {

                        progress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<singleVideoBean> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setMessage("Do you want to share this VLOG to your timeline?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                progress.setVisibility(View.VISIBLE);

                                final bean b = (bean) getApplicationContext();


                                Call<shareBean> call = b.getRetrofit().share(SharePreferenceUtils.getInstance().getString("userId"), videoId);

                                call.enqueue(new Callback<shareBean>() {
                                    @Override
                                    public void onResponse(Call<shareBean> call, Response<shareBean> response) {

                                        try {
                                            Toast.makeText(getContext(), "Successfully shared on your Timeline", Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                        progress.setVisibility(View.GONE);

                                    }

                                    @Override
                                    public void onFailure(Call<shareBean> call, Throwable t) {
                                        progress.setVisibility(View.GONE);
                                        t.printStackTrace();
                                    }
                                });
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mess = comment.getText().toString();

                if (mess.length() > 0) {
                    progress.setVisibility(View.VISIBLE);

                    final bean b = (bean) getApplicationContext();

                    Call<commentBean> call = b.getRetrofit().comment(SharePreferenceUtils.getInstance().getString("userId"), videoId, mess);

                    call.enqueue(new Callback<commentBean>() {
                        @Override
                        public void onResponse(Call<commentBean> call, Response<commentBean> response) {

                            try {
                                if (Objects.equals(response.body().getMessage(), "Video Comment Success")) {
                                    comment.setText("");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<commentBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                            Log.d("Video upload find ", t.toString());
                        }
                    });
                }


            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = getArguments().getString("userId");
                Intent intent = new Intent(getActivity(), TimelineProfile.class);

                intent.putExtra("userId", s);
                startActivity(intent);

            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s = getArguments().getString("userId");
                Intent intent = new Intent(getActivity(), TimelineProfile.class);

                intent.putExtra("userId", s);
                startActivity(intent);

            }
        });
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress.setVisibility(View.VISIBLE);

                final bean b = (bean) getApplicationContext();



                Call<followBean> call = b.getRetrofit().follow(SharePreferenceUtils.getInstance().getString("userId"), getArguments().getString("userId"));

                call.enqueue(new Callback<followBean>() {
                    @Override
                    public void onResponse(Call<followBean> call, Response<followBean> response) {

                        try {

                            if (response.body().getMessage().equals("Follow Success")) {
                                follow.setText("UNFOLLOW");
                                follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.minus_white, 0, 0, 0);
                                Toast.makeText(getContext(), "Started to follow " + name.getText().toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (response.body().getMessage().equals("Unfollow Success")) {
                                follow.setText("FOLLOW");
                                follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.plus_white, 0, 0, 0);
                                Toast.makeText(getContext(), "Unfollowed " + name.getText().toString(), Toast.LENGTH_SHORT).show();
                            }
                            //   loadData();


                        } catch (Exception e) {
                            e.printStackTrace();
                            // Toast.makeText(TimelineProfile.this , "Some Error Occurred, Please try again" , Toast.LENGTH_SHORT).show();
                        }


                        progress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<followBean> call, Throwable t) {

                        progress.setVisibility(View.GONE);

                    }
                });


            }
        });


        return view;
    }


    ////////////////////internet connectivity check///////////////
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        if (!isConnected) {
            // Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
            try {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getContext());
                }
                builder.setTitle("NO INTERNET CONNECTION")
                        .setMessage("Please check your internet connection setting and click refresh")
                        .setPositiveButton(R.string.Refresh, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Objects.requireNonNull(getActivity()).finish();
                                getActivity().overridePendingTransition(0, 0);
                                //getActivity().startActivity(getIntent());
                                getActivity().overridePendingTransition(0, 0);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } catch (Exception e) {
                Log.d("TAG", "Show Dialog: " + e.getMessage());
            }
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);

    }


    public void schedule() {

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                final bean b = (bean) getApplicationContext();


                Call<singleVideoBean> call = b.getRetrofit().getsingleVideo(SharePreferenceUtils.getInstance().getString("userId"), videoId);

                call.enqueue(new Callback<singleVideoBean>() {
                    @Override
                    public void onResponse(Call<singleVideoBean> call, Response<singleVideoBean> response) {


                        try {
                            if (response.body().getData().getComments() != null) {
                                adapter.setGridData(response.body().getData().getComments());


                                if (response.body().getData().getComments().size() > count) {
                                    grid.smoothScrollToPosition(adapter.getItemCount() - 1);
                                    count = response.body().getData().getComments().size();
                                }


                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        if (Objects.equals(response.body().getData().getIsLiked(), "1")) {

                            likes.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);

                        } else {
                            likes.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.black_heart, 0, 0, 0);
                        }


                        try {
                            views.setText(response.body().getData().getViewsCount());
                            likes.setText(response.body().getData().getLikesCount());
                            comments.setText(response.body().getData().getCommentCount());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(Call<singleVideoBean> call, Throwable t) {

                    }
                });

            }
        }, 0, 1000);

    }

    private void initializePlayer() {

        mPlayerView.requestFocus();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

        mPlayerView.setPlayer(player);

        player.setPlayWhenReady(shouldAutoPlay);
/*        MediaSource mediaSource = new HlsMediaSource(Uri.parse("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"),
                mediaDataSourceFactory, mainHandler, null);*/

        player.setRepeatMode(Player.REPEAT_MODE_ONE);
        MediaSource mediaSource = new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                .createMediaSource(Uri.parse("http://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com/softcode/upload/video/" + getArguments().getString("uri")));

        boolean haveStartPosition = currentWindow != C.INDEX_UNSET;
        if (haveStartPosition) {
            player.seekTo(currentWindow, playbackPosition);
        }
        player.prepare(mediaSource, !haveStartPosition, false);
/*
        ivHideControllerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerView.hideController();
            }
        });*/

        player.addListener(new Player.EventListener() {
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
                if (playbackState == Player.STATE_BUFFERING) {
                    loadingProgress.setVisibility(View.VISIBLE);
                } else {
                    loadingProgress.setVisibility(View.INVISIBLE);
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
    }

    @Override
    public void onStart() {
        super.onStart();

        initializePlayer();

    }

    @Override
    public void onResume() {
        super.onResume();
        if ((player == null)) {
            initializePlayer();
        }
        final bean b = (bean) getApplicationContext();
        String uid = getArguments().getString("userId");
        if (SharePreferenceUtils.getInstance().getString("userId").equals(uid)) {

            follow.setVisibility(View.GONE);
        } else {
            loadfollowstatus(uid);
        }

        /*if (mainPlayer != null)
        {
            mainPlayer.setPlayWhenReady(true);
        }*/
        bean.getInstance().setConnectivityListener(this);
        progress.setVisibility(View.VISIBLE);



        Call<singleVideoBean> call = b.getRetrofit().getsingleVideo(SharePreferenceUtils.getInstance().getString("userId"), videoId);

        call.enqueue(new Callback<singleVideoBean>() {
            @Override
            public void onResponse(Call<singleVideoBean> call, Response<singleVideoBean> response) {


                // setting msg time
                String dateString = response.body().getData().getUploadTime();
                if (dateString != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date testDate = null;
                    try {
                        testDate = sdf.parse(dateString);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    String times = TimeStampConverter.getTimeAgo(testDate.getTime());
                    time.setText(times);
                }

                //.............


                try {
                    if (response.body().getData().getComments() != null) {
                        adapter.setGridData(response.body().getData().getComments());
                        grid.smoothScrollToPosition(adapter.getItemCount() - 1);

                        count = response.body().getData().getComments().size();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    timelineId = response.body().getData().getTimelineId();

                } catch (Exception e) {
                    e.printStackTrace();
                }


                progress.setVisibility(View.GONE);


                if (Objects.equals(response.body().getData().getIsLiked(), "1")) {

                    likes.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);

                } else {
                    likes.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.black_heart, 0, 0, 0);
                }


                try {
                    views.setText(response.body().getData().getViewsCount());
                    likes.setText(response.body().getData().getLikesCount());
                    comments.setText(response.body().getData().getCommentCount());


                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    ImageLoader loader = ImageLoader.getInstance();
                    loader.displayImage(response.body().getData().getTimelineProfileImage(), profile);

                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    bean b = (bean) getApplicationContext();

                    if (SharePreferenceUtils.getInstance().getString("userId").equals(response.body().getData().getTimelineId())) {
                        name.setText("Your Profile");
                    } else {
                        name.setText(response.body().getData().getTimelineName());
                    }

                    schedule();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<singleVideoBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();

        releasePlayer();

    }

    @Override
    public void onStop() {
        super.onStop();

        releasePlayer();

    }

    private void releasePlayer() {
        if (player != null) {
            updateStartPosition();
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    /* @Override
     public void onSaveInstanceState(Bundle outState) {
         updateStartPosition();

         outState.putBoolean(KEY_PLAY_WHEN_READY, playWhenReady);
         outState.putInt(KEY_WINDOW, currentWindow);
         outState.putLong(KEY_POSITION, playbackPosition);
         super.onSaveInstanceState(outState);
     }
 */
    private void updateStartPosition() {
        playbackPosition = player.getCurrentPosition();
     /*   if(player!=null)
        {
            currentWindow = player.getCurrentWindowIndex();
        }
     */
        playWhenReady = player.getPlayWhenReady();
    }

    public void loadfollowstatus(String userids) {
        final bean b = (bean) getApplicationContext();



        Call<followBean> call = b.getRetrofit().followcheck(SharePreferenceUtils.getInstance().getString("userId"), userids);

        call.enqueue(new Callback<followBean>() {
            @Override
            public void onResponse(Call<followBean> call, Response<followBean> response) {

                try {


                    // Toast.makeText(TimelineProfile.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();
                    if (response.body().getMessage().equals("Following")) {
                        follow.setVisibility(View.VISIBLE);
                        follow.setText("UNFOLLOW");
                        follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.minus_white, 0, 0, 0);
                        //Toast.makeText(getContext(), "Following " + getArguments().getString("timelineName"), Toast.LENGTH_SHORT).show();

                    }
                    if (response.body().getMessage().equals("Not Following")) {
                        follow.setVisibility(View.VISIBLE);
                        follow.setText("FOLLOW");
                        follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.plus_white, 0, 0, 0);
                        // Toast.makeText(getContext(), "Unfollowed " + getArguments().getString("timelineName"), Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    //  Toast.makeText(TimelineProfile.this, "Some Error Occurred, Please try again follow", Toast.LENGTH_SHORT).show();
                }


                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<followBean> call, Throwable t) {

                progress.setVisibility(View.GONE);

            }
        });


    }

    public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

        List<Comment> list = new ArrayList<>();
        Context context;

        CommentsAdapter(Context context, List<Comment> list) {
            this.context = context;
            this.list = list;
        }


        public void setGridData(List<Comment> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.comment_list_model, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            //  holder.setIsRecyclable(false);

            Comment item = list.get(position);
            holder.message.setText(item.getComment());
            holder.name.setText(item.getUserName());
            // holder.msgprofileimg.setImageURI(Uri.parse(item.getUserImage()));
            Glide.with(VideoPlayerFragmentvod.this)
                    .load(Uri.parse(item.getUserImage())).apply(new RequestOptions()
                    .placeholder(R.drawable.user_default)
                    .centerCrop()
                    .dontAnimate()
                    .dontTransform())
                    .into(holder.msgprofileimg);


            // setting msg time
            String dateString = item.getTime();
            if (dateString != null) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date testDate = null;
                try {
                    testDate = sdf.parse(dateString);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // System.out.println("Milliseconds==" + testDate.getTime());
                assert testDate != null;
                String mtime = TimeStampConverter.getTimeAgo(testDate.getTime());
                holder.time.setText(mtime);
            }
            //.............


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView message, time, name;
            LinearLayout container;
            // RelativeLayout bubble;
            CircleImageView msgprofileimg;

            public ViewHolder(View itemView) {
                super(itemView);

                message = (TextView) itemView.findViewById(R.id.message);
                time = (TextView) itemView.findViewById(R.id.time);
                name = (TextView) itemView.findViewById(R.id.name);
                //    bubble = itemView.findViewById(R.id.bubble);
                container = (LinearLayout) itemView.findViewById(R.id.container);
                msgprofileimg = itemView.findViewById(R.id.msgprofile_pic);

            }
        }

    }


}
