package com.yl.youthlive;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.commentPOJO.commentBean;
import com.yl.youthlive.internetConnectivity.ConnectivityReceiver;
import com.yl.youthlive.sharePOJO.shareBean;
import com.yl.youthlive.singleVideoPOJO.Comment;
import com.yl.youthlive.singleVideoPOJO.singleVideoBean;

import java.nio.ByteBuffer;
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
import tcking.github.com.giraffeplayer2.GiraffePlayer;
import tcking.github.com.giraffeplayer2.PlayerListener;
import tv.danmaku.ijk.media.player.IjkTimedText;
import veg.mediaplayer.sdk.MediaPlayer;

public class SingleVideoActivity extends AppCompatActivity implements MediaPlayer.MediaPlayerCallback,ConnectivityReceiver.ConnectivityReceiverListener {

    Toolbar toolbar;
    CircleImageView profile;
    TextView name, time, views, comments, likes, share;
    RecyclerView grid;
    LinearLayoutManager manager;
    EditText comment;
    CommentsAdapter adapter;
    List<Comment> list;
    ProgressBar progress;
    String videoId, url;

    int count = 0;

    String timelineId;

    FloatingActionButton send;

    private int mSeekPosition;

    tcking.github.com.giraffeplayer2.VideoView player;
    ImageButton play;

    //ImageView image;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_video2);
        checkConnection();
        videoId = getIntent().getStringExtra("videoId");
        url = getIntent().getStringExtra("url");
        list = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // image = findViewById(R.id.image);
        ImageLoader loader = ImageLoader.getInstance();

        player = (tcking.github.com.giraffeplayer2.VideoView) findViewById(R.id.player);



/*
        MediaPlayerConfig wzPlayerConfig = new MediaPlayerConfig();

        wzPlayerConfig.setConnectionUrl(url);

        Log.d("asdasdasd" , url);
*/

/*
        wzPlayerConfig.setConnectionNetworkProtocol(-1);
        wzPlayerConfig.setConnectionDetectionTime(300);
        wzPlayerConfig.setConnectionBufferingTime(0);
        wzPlayerConfig.setConnectionBufferingSize(0);
        wzPlayerConfig.setDecodingType(0);
        wzPlayerConfig.setDecoderLatency(1);
        wzPlayerConfig.setNumberOfCPUCores(0);
        wzPlayerConfig.setRendererType(1);
        wzPlayerConfig.setSynchroEnable(0);
        wzPlayerConfig.setSynchroNeedDropVideoFrames(0);
        wzPlayerConfig.setEnableAspectRatio(2);
        wzPlayerConfig.setDataReceiveTimeout(30000);
        wzPlayerConfig.setNumberOfCPUCores(0);
*/


        /*VideoInfo videoInfo = new VideoInfo(Uri.parse(url))
                .setTitle("test video") //config title
                .setAspectRatio(VideoInfo.AR_ASPECT_FILL_PARENT) //aspectRatio
                .setShowTopBar(true) //show mediacontroller top bar
                .setPortraitWhenFullScreen(false);//portrait when full screen

        */



        DisplayImageOptions options = new DisplayImageOptions.Builder().resetViewBeforeLoading(false).cacheInMemory(true).cacheOnDisk(true).build();

        //  loader.displayImage(getIntent().getStringExtra("thumb") , image , options);




        player.setVideoPath(url).getPlayer().setPlayerListener(new PlayerListener() {
            @Override
            public void onPrepared(GiraffePlayer giraffePlayer) {

                //         image.setVisibility(View.GONE);

            }

            @Override
            public void onBufferingUpdate(GiraffePlayer giraffePlayer, int percent) {

            }

            @Override
            public boolean onInfo(GiraffePlayer giraffePlayer, int what, int extra) {
                return false;
            }

            @Override
            public void onCompletion(GiraffePlayer giraffePlayer) {

            }

            @Override
            public void onSeekComplete(GiraffePlayer giraffePlayer) {

            }

            @Override
            public boolean onError(GiraffePlayer giraffePlayer, int what, int extra) {
                return false;
            }

            @Override
            public void onPause(GiraffePlayer giraffePlayer) {

            }

            @Override
            public void onRelease(GiraffePlayer giraffePlayer) {

            }

            @Override
            public void onStart(GiraffePlayer giraffePlayer) {

            }

            @Override
            public void onTargetStateChange(int oldState, int newState) {

            }

            @Override
            public void onCurrentStateChange(int oldState, int newState) {

            }

            @Override
            public void onDisplayModelChange(int oldModel, int newModel) {

            }

            @Override
            public void onPreparing(GiraffePlayer giraffePlayer) {

            }

            @Override
            public void onTimedText(GiraffePlayer giraffePlayer, IjkTimedText text) {

            }

            @Override
            public void onLazyLoadProgress(GiraffePlayer giraffePlayer, int progress) {

            }

            @Override
            public void onLazyLoadError(GiraffePlayer giraffePlayer, String message) {

            }
        });
        player.getPlayer().start();


        /*player.setVideoSource(url);

        player.setAutoLoop(true);
        player.setAutoPlay(true);

        player.setFullScreenButtonVisible(false);
        player.setFullScreen(false);*/

        play = (ImageButton) findViewById(R.id.play);

        share = (TextView) findViewById(R.id.share);

        progress = (ProgressBar) findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
        profile = (CircleImageView) findViewById(R.id.profile);
        name = (TextView) findViewById(R.id.name);
        time = (TextView) findViewById(R.id.time);
        views = (TextView) findViewById(R.id.views);
        comments = (TextView) findViewById(R.id.comments);
        likes = (TextView) findViewById(R.id.like);
        comment = (EditText) findViewById(R.id.comment);
        grid = (RecyclerView) findViewById(R.id.grid);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new CommentsAdapter(this, list);
        grid.setLayoutManager(manager);
        grid.setAdapter(adapter);

        send = (FloatingActionButton) findViewById(R.id.send);

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
        /*comment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if(event.getRawX() >= (comment.getRight() - comment.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()) - 60)
                {
                    // your action here

                    //og.d("asdasd" , "clicked");



                    String mess = comment.getText().toString();

                    if (mess.length() > 0)
                    {
                        progress.setVisibility(View.VISIBLE);

                        final bean b = (bean) getApplicationContext();

                        final Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.BASE_URL)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        final AllAPIs cr = retrofit.create(AllAPIs.class);


                        Call<commentBean> call = cr.comment(b.userId , videoId , mess);

                        call.enqueue(new Callback<commentBean>() {
                            @Override
                            public void onResponse(Call<commentBean> call, Response<commentBean> response) {


                                if (Objects.equals(response.body().getMessage(), "Video Comment Success"))
                                {
                                    comment.setText("");
                                }

                                progress.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<commentBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                                Log.d("Video upload find " , t.toString());
                            }
                        });
                    }


                    return true;
                }
                return false;
            }
        });
*/


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

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
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


        /*send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String mess = comment.getText().toString();

                if (mess.length() > 0)
                {
                    progress.setVisibility(View.VISIBLE);

                    final bean b = (bean) getApplicationContext();

                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);


                    Call<vlogListBean> call = cr.comment(b.userId , videoId , mess);

                    call.enqueue(new Callback<vlogListBean>() {
                        @Override
                        public void onResponse(Call<vlogListBean> call, Response<vlogListBean> response) {

                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<vlogListBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                        }
                    });
                }

            }
        });*/

        //video.setMediaController(controller);


        /*video.setVideoViewCallback(new UniversalVideoView.VideoViewCallback() {
            @Override
            public void onScaleChange(boolean isFullscreen) {
            }

            @Override
            public void onPause(MediaPlayer mediaPlayer) { // Video pause
                Log.d("Asdasd", "onPause UniversalVideoView callback");
            }

            @Override
            public void onStart(MediaPlayer mediaPlayer) { // Video start/resume to play
                Log.d("Asdasd", "onStart UniversalVideoView callback");
            }

            @Override
            public void onBufferingStart(MediaPlayer mediaPlayer) {// steam start loading
                Log.d("Asdasd", "onBufferingStart UniversalVideoView callback");
            }

            @Override
            public void onBufferingEnd(MediaPlayer mediaPlayer) {// steam end loading
                Log.d("asdasd", "onBufferingEnd UniversalVideoView callback");
            }

        });*/


/*
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //video.setVideoPath(url);
                //video.requestFocus();

                player.setVideoSource(url);

                if (mSeekPosition > 0) {
                    //video.seekTo(mSeekPosition);
                }
                //video.start();
                //holder.controller.setTitle("Big Buck Bunny");
            }
        });
*/


        //Uri uri = Uri.parse(url);
        /*video.setVideoURI(uri);
        video.start();

        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //close the progress dialog when buffering is done

                mp.setLooping(true);
            }
        });*/


    }


    @Override
    protected void onResume() {
        super.onResume();

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


                if (Objects.equals(response.body().getData().getIsLiked(), "1"))
                {

                    likes.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_like , 0 , 0 , 0);

                }
                else
                {
                    likes.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.black_heart , 0 , 0 , 0);
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

                    //   name.setText(response.body().getData().getTimelineName());


                    //  Toast.makeText(SingleVideoActivity.this, "hello"+times, Toast.LENGTH_SHORT).show();



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
        String message;
        int color;
        if (isConnected) {

            // Toast.makeText(this, "Good! Connected to Internet", Toast.LENGTH_SHORT).show();
            //    message = "Good! Connected to Internet";
            //    color = Color.WHITE;
        } else {
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
            }
            catch(Exception e)
            {
                Log.d("TAG", "Show Dialog: "+e.getMessage());
            }
            //      message = "Sorry! Not connected to internet";
            //     color = Color.RED;
        }

       /* Snackbar snackbar = Snackbar
                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
        */
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


                        if (Objects.equals(response.body().getData().getIsLiked(), "1"))
                        {

                            likes.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_like , 0 , 0 , 0);

                        }
                        else
                        {
                            likes.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.black_heart , 0 , 0 , 0);
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
    public int Status(int i) {
        Log.d("status" , String.valueOf(i));
        return 0;
    }

    @Override
    public int OnReceiveData(ByteBuffer byteBuffer, int i, long l) {

        return 0;
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




            bean b = (bean) context.getApplicationContext();


            //setting comment view width at runtime
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;

            //ViewGroup.LayoutParams params = holder.bubble.getLayoutParams();
            //Changes the height and width to the specified *pixels*
           // params.width = (width/5)*4;
          //  holder.bubblewrap.setLayoutParams(params);

            //setting message textview size to custom
            holder.message.setMaxWidth((width/5)*4);


            if (Objects.equals(item.getUserId(), b.userId)) {
                holder.container.setGravity(Gravity.END);
                holder.bubble.setBackgroundResource(R.drawable.bubble_me);
            } else {
                holder.container.setGravity(Gravity.START);
                holder.bubble.setBackgroundResource(R.drawable.bubble);
            }


        }

        @Override
        public int getItemCount() {
            //return list.size();
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView message, time, name;
            LinearLayout bubble, container;
            CircleImageView msgprofileimg;

            public ViewHolder(View itemView) {
                super(itemView);

                message = (TextView) itemView.findViewById(R.id.message);
                time = (TextView) itemView.findViewById(R.id.time);
                name = (TextView) itemView.findViewById(R.id.name);
                bubble = (LinearLayout) itemView.findViewById(R.id.bubble);
                container = (LinearLayout) itemView.findViewById(R.id.container);
                msgprofileimg = itemView.findViewById(R.id.msgprofile_pic);


            }
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
