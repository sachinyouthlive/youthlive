package com.yl.youthlive;
/*

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
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
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.commentPOJO.commentBean;
import com.yl.youthlive.internetConnectivity.ConnectivityReceiver;
import com.yl.youthlive.pl.MainVideoActivity;
import com.yl.youthlive.sharePOJO.shareBean;
import com.yl.youthlive.singleVideoPOJO.Comment;
import com.yl.youthlive.singleVideoPOJO.singleVideoBean;

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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class SingleVideoActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    Toolbar toolbar;
    CircleImageView profile;
    TextView name, time, views, comments, likes, share;
    RecyclerView grid;
    LinearLayoutManager manager;
    EditText comment;
    CommentsAdapter adapter;
    List<Comment> list;
    ProgressBar progress, progressv;
    String videoId, url, thumb;
    PlayerView mainPlayerView;
    SimpleExoPlayer mainPlayer;


    int count = 0;
    private Uri uri;

    String timelineId;

    Button followbtn;

    ImageView send;
    ImageButton play;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_video);
        checkConnection();
        videoId = getIntent().getStringExtra("videoId");
        url = getIntent().getStringExtra("url");
        thumb = getIntent().getStringExtra("thumb");
        list = new ArrayList<>();
        followbtn=findViewById(R.id.follow);
        followbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(SingleVideoActivity.this, MainVideoActivity.class);
                startActivity(intent);
            }
        });
        toolbar = findViewById(R.id.toolbar);
        mainPlayerView = findViewById(R.id.main_player);
        uri = Uri.parse("rtmp://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com:1935/vod/" + url);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        //Create the player
        mainPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, new DefaultLoadControl(
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
        RtmpDataSourceFactory rtmpDataSourceFactory = new RtmpDataSourceFactory();
        // This is the MediaSource representing the media to be played.
        final MediaSource videoSource = new ExtractorMediaSource.Factory(rtmpDataSourceFactory)
                .createMediaSource(uri);

        mainPlayer.prepare(videoSource);
        mainPlayer.setPlayWhenReady(true);
        mainPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);

        mainPlayer.addListener(new ExoPlayer.EventListener() {
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

                if(playbackState==3)
                {
                    progressv.setVisibility(View.GONE);
                }

               // Toast.makeText(SingleVideoActivity.this, ""+playWhenReady+" "+playbackState, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                pausePlayer();
                progressv.setVisibility(View.VISIBLE);


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





        /////////////////////


        ///////////////////





        play = findViewById(R.id.play);

        share = findViewById(R.id.share);

        progress = findViewById(R.id.progress);
        progressv = findViewById(R.id.progressv);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
        profile = (CircleImageView) findViewById(R.id.profile);
        name = findViewById(R.id.name);
        time = findViewById(R.id.time);
        views = findViewById(R.id.views);
        comments = findViewById(R.id.comments);
        likes = findViewById(R.id.like);
        comment = findViewById(R.id.comment);
        grid = findViewById(R.id.grid);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new CommentsAdapter(this, list);
        grid.setLayoutManager(manager);
        grid.setAdapter(adapter);

        send = findViewById(R.id.send);

        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                final bean b = (bean) getApplicationContext();
                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                final AllAPIs cr = retrofit.create(AllAPIs.class);
                Call<singleVideoBean> call = cr.likeVideo(b.userId, videoId);
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


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SingleVideoActivity.this);
                alertDialogBuilder.setMessage("Do you want to share this VLOG to your timeline?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                progress.setVisibility(View.VISIBLE);

                                final bean b = (bean) getApplicationContext();

                                final Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(b.BASE_URL)
                                        .addConverterFactory(ScalarsConverterFactory.create())
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                final AllAPIs cr = retrofit.create(AllAPIs.class);


                                Call<shareBean> call = cr.share(b.userId, videoId);

                                call.enqueue(new Callback<shareBean>() {
                                    @Override
                                    public void onResponse(Call<shareBean> call, Response<shareBean> response) {

                                        try {
                                            Toast.makeText(SingleVideoActivity.this, "Successfully shared on your Timeline", Toast.LENGTH_SHORT).show();
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

                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);


                    Call<commentBean> call = cr.comment(b.userId, videoId, mess);

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

                Intent intent = new Intent(SingleVideoActivity.this, TimelineProfile.class);

                intent.putExtra("userId", timelineId);
                startActivity(intent);

            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SingleVideoActivity.this, TimelineProfile.class);

                intent.putExtra("userId", timelineId);
                startActivity(intent);

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        startPlayer();
       // register connection status listener
        bean.getInstance().setConnectivityListener(this);
        progress.setVisibility(View.VISIBLE);


        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<singleVideoBean> call = cr.getsingleVideo(b.userId, videoId);

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

                    if (b.userId.equals(response.body().getData().getTimelineId())) {
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
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle("NO INTERNET CONNECTION")
                        .setMessage("Please check your internet connection setting and click refresh")
                        .setPositiveButton(R.string.Refresh, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
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

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<singleVideoBean> call = cr.getsingleVideo(b.userId, videoId);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }



    public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

        List<Comment> list = new ArrayList<>();
        Context context;

        public CommentsAdapter(Context context, List<Comment> list) {
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
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.setIsRecyclable(false);

            Comment item = list.get(position);
            holder.message.setText(item.getComment());
            // holder.msgprofileimg.setImageURI(Uri.parse(item.getUserImage()));
            Glide.with(SingleVideoActivity.this)
                    .load(Uri.parse(item.getUserImage())).apply(new RequestOptions()
                    .placeholder(R.drawable.user_default)
                    .centerCrop()
                    .dontAnimate()
                    .dontTransform())
                    .into(holder.msgprofileimg);


            // setting msg time
            String dateString = item.getTime();
            if (dateString != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date testDate = null;
                try {
                    testDate = sdf.parse(dateString);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // System.out.println("Milliseconds==" + testDate.getTime());
                String mtime = TimeStampConverter.getTimeAgo(testDate.getTime());
                holder.time.setText(mtime);
            }
            //.............

           */
/*
            bean b = (bean) context.getApplicationContext();


            //setting comment view width at runtime
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            holder.message.setMaxWidth((width / 5) * 4);

            if (Objects.equals(item.getUserId(), b.userId)) {
                holder.container.setGravity(Gravity.END);
                holder.bubble.setBackgroundResource(R.drawable.bubble_me);
            } else {
                holder.container.setGravity(Gravity.START);
                holder.bubble.setBackgroundResource(R.drawable.bubble);
            }*//*


        }
        @Override
        public int getItemCount() {
            //return list.size();
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView message, time, name;
            LinearLayout  container;
            RelativeLayout bubble;
            CircleImageView msgprofileimg;

            public ViewHolder(View itemView) {
                super(itemView);

                message = (TextView) itemView.findViewById(R.id.message);
                time = (TextView) itemView.findViewById(R.id.time);
                name = (TextView) itemView.findViewById(R.id.name);
                bubble = itemView.findViewById(R.id.bubble);
                container = (LinearLayout) itemView.findViewById(R.id.container);
                msgprofileimg = itemView.findViewById(R.id.msgprofile_pic);

            }
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPlayer.stop();
    }
    @Override
    protected void onPause() {
        super.onPause();
        pausePlayer();
    }
    private void pausePlayer(){
        mainPlayer.setPlayWhenReady(false);
        mainPlayer.getPlaybackState();
    }
    private void startPlayer(){
        mainPlayer.setPlayWhenReady(true);
        mainPlayer.getPlaybackState();
    }

    }
*/
