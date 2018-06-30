package com.yl.youthlive;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wowza.gocoder.sdk.api.WowzaGoCoder;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcast;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcastConfig;
import com.wowza.gocoder.sdk.api.configuration.WZMediaConfig;
import com.wowza.gocoder.sdk.api.devices.WZAudioDevice;
import com.wowza.gocoder.sdk.api.devices.WZCameraView;
import com.wowza.gocoder.sdk.api.errors.WZStreamingError;
import com.wowza.gocoder.sdk.api.status.WZState;
import com.wowza.gocoder.sdk.api.status.WZStatus;
import com.wowza.gocoder.sdk.api.status.WZStatusCallback;
import com.yasic.bubbleview.BubbleView;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.acceptRejectPOJO.acceptRejectBean;
import com.yl.youthlive.feedBackPOJO.feedBackBean;
import com.yl.youthlive.followPOJO.followBean;
import com.yl.youthlive.getIpdatedPOJO.Comment;
import com.yl.youthlive.getIpdatedPOJO.getUpdatedBean;
import com.yl.youthlive.giftPOJO.Datum;
import com.yl.youthlive.giftPOJO.giftBean;
import com.yl.youthlive.liveCommentPOJO.liveCommentBean;
import com.yl.youthlive.liveLikePOJO.liveLikeBean;
import com.yl.youthlive.reportPOJO.reportBean;
import com.yl.youthlive.sendGiftPOJO.sendGiftBean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class player_first extends Fragment implements WZStatusCallback {

    RecyclerView grid;
    RecyclerView grid2;
    LinearLayoutManager manager;
    LiveAdapter adapter;
    LiveAdapter2 adapter2;
    LinearLayoutManager manager2;
    ImageButton heart;
    private BubbleView bubbleView;
    ImageButton close;

    String giftURL = "", giftName = "";

    ProgressDialog progressDialog;

    LinearLayout giftLayout1;
    ImageView giftIcon;
    TextView giftTitle;

    Integer[] gfts = {
            R.drawable.gift1,
            R.drawable.gift2,
            R.drawable.gift3,
            R.drawable.gift4,
            R.drawable.gift5,
            R.drawable.gift6
    };

    GiftAdapter gAdapter;

    String connId = "";

    //SurfaceView mVideoSurface;
    //BroadcastPlayer mBroadcastPlayer;
    //TextView mPlayerStatusTextView;
    final OkHttpClient mOkHttpClient = new OkHttpClient();

    String liveId = "";


    int count = 0;

    String timelineId;

    List<Comment> cList;
    List<com.yl.youthlive.getIpdatedPOJO.View> vList;

    ImageButton chatIcon, switchCamera, crop, gift;
    LinearLayout chat, actions;
    EditText comment;
    FloatingActionButton send;
    CircleImageView image;
    ProgressBar progress;
    TextView username;

    TextView likeCount;

    String uri;

    LinearLayout giftLayout;
    TextView giftBean, giftDiamond, giftCoin;

    TextView beans, level;

    RecyclerView giftList;
    Button sendGift;

    TextView viewCount;

    List<Datum> list;
    LinearLayoutManager gManager;

    ImageButton follow;


    RelativeLayout cameraLayout1, cameraLayout2;

    ImageButton accept1, accept2, reject1, reject2, reject3, report;

    private static final String APPLICATION_ID = "gA1JdKySejF0GfA0ChIvVA";
    private static final String API_KEY = "2v7690nbz6xc4vshnuc2a7yyg";


    private WZCameraView goCoderCameraView;

    WZBroadcast goCoderBroadcaster;

    // The GoCoder SDK audio device
    private WZAudioDevice goCoderAudioDevice;

    // The broadcast configuration settings
    private WZBroadcastConfig goCoderBroadcastConfig;

    private WowzaGoCoder goCoder;

    String key;


    BroadcastReceiver commentReceiver;
    BroadcastReceiver viewReceiver;
    BroadcastReceiver likeReceiver;
    BroadcastReceiver giftReceiver;
    BroadcastReceiver requestReceiver;

    int PERMISSION_CODE = 12;



    Button toggle;



    PlayerActivity plactivity;
    int coun = 0;
    Toast toast;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.player_first_frag, container, false);
        plactivity = (PlayerActivity) getActivity();

        Bundle b = getArguments();

        liveId = b.getString("liveId");
        timelineId = b.getString("timelineId");

        mProjectionManager = (MediaProjectionManager) plactivity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        toggle = view.findViewById(R.id.button2);

        cameraLayout1 = (RelativeLayout) view.findViewById(R.id.camera_layout1);


        accept1 = (ImageButton) view.findViewById(R.id.accept1);


        reject1 = (ImageButton) view.findViewById(R.id.reject1);
        reject3 = (ImageButton) view.findViewById(R.id.reject3);

        report = (ImageButton) view.findViewById(R.id.report);

        follow = (ImageButton) view.findViewById(R.id.follow);

        giftLayout1 = (LinearLayout) view.findViewById(R.id.gift_layout);
        giftIcon = (ImageView) view.findViewById(R.id.gift_icon);
        giftTitle = (TextView) view.findViewById(R.id.gift_title);

        viewCount = (TextView) view.findViewById(R.id.view_count);

        cList = new ArrayList<>();
        vList = new ArrayList<>();

        giftLayout = (LinearLayout) view.findViewById(R.id.ggift);
        giftBean = (TextView) view.findViewById(R.id.gift_beans);
        giftDiamond = (TextView) view.findViewById(R.id.gift_diamond);
        giftCoin = (TextView) view.findViewById(R.id.gift_coin);
        giftList = (RecyclerView) view.findViewById(R.id.gift_list);
        sendGift = (Button) view.findViewById(R.id.send_gift);

        beans = (TextView) view.findViewById(R.id.beans);
        level = (TextView) view.findViewById(R.id.level);

        list = new ArrayList<>();

        gManager = new GridLayoutManager(getContext(), 3);
        gAdapter = new GiftAdapter(getContext(), list);

        giftList.setAdapter(gAdapter);
        giftList.setLayoutManager(gManager);

        chat = (LinearLayout) view.findViewById(R.id.chat);
        actions = (LinearLayout) view.findViewById(R.id.actions);
        chatIcon = (ImageButton) view.findViewById(R.id.chat_icon);
        crop = (ImageButton) view.findViewById(R.id.crop);
        gift = (ImageButton) view.findViewById(R.id.gift);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        switchCamera = (ImageButton) view.findViewById(R.id.switch_camera);
        comment = (EditText) view.findViewById(R.id.comment);
        send = (FloatingActionButton) view.findViewById(R.id.send);

        likeCount = (TextView) view.findViewById(R.id.like_count);

        image = (CircleImageView) view.findViewById(R.id.image);
        username = (TextView) view.findViewById(R.id.name);


        goCoderCameraView = (WZCameraView) view.findViewById(R.id.camera1);

        goCoderCameraView.setZOrderOnTop(true);


        //mVideoSurface = (SurfaceView) findViewById(R.id.PreviewSurfaceView);

        toggle.setVisibility(View.GONE);

        /*toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });*/


        grid = (RecyclerView) view.findViewById(R.id.grid);
        grid2 = (RecyclerView) view.findViewById(R.id.grid2);


        manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        manager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        heart = (ImageButton) view.findViewById(R.id.heart);
        close = (ImageButton) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mBroadcaster.stopBroadcast();

                if (goCoderBroadcaster.getStatus().isRunning()) {
                    goCoderBroadcaster.endBroadcast();
                }


                plactivity.finish();
            }
        });
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final bean b = (bean) getContext().getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                retrofit2.Call<liveLikeBean> call = cr.likeLive(b.userId, liveId);

                call.enqueue(new retrofit2.Callback<liveLikeBean>() {
                    @Override
                    public void onResponse(retrofit2.Call<liveLikeBean> call, retrofit2.Response<liveLikeBean> response) {


                    }

                    @Override
                    public void onFailure(retrofit2.Call<liveLikeBean> call, Throwable t) {

                    }
                });


                bubbleView.startAnimation(bubbleView.getWidth(), bubbleView.getHeight());

            }
        });

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mHandler = new Handler();
                Looper.loop();
            }
        }.start();

        heart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                toast.setText("Like this Stream");
                toast.show();

                return false;

            }
        });

        crop.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                toast.setText("Share Screenshot");
                toast.show();

                return false;

            }
        });

        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                coun = 0;

                startProjection();


            }
        });


        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                progress.setVisibility(View.VISIBLE);

                final bean b = (bean) getContext().getApplicationContext();

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

                        try {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Some Error Occurred, Please try again", Toast.LENGTH_SHORT).show();
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


        reject3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                WZStreamingError configValidationError = goCoderBroadcastConfig.validateForBroadcast();


                if (configValidationError != null) {
                    //Toast.makeText(LiveScreen.this, configValidationError.getErrorDescription(), Toast.LENGTH_LONG).show();
                } else if (goCoderBroadcaster.getStatus().isRunning()) {
                    // Stop the broadcast that is currently running
                    goCoderBroadcaster.endBroadcast(player_first.this);
                    cameraLayout1.setVisibility(View.GONE);

                } else {
                    // Start streaming
                    goCoderBroadcaster.startBroadcast(goCoderBroadcastConfig, player_first.this);
                }


            }
        });


        accept1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final bean b = (bean) getContext().getApplicationContext();


                progress.setVisibility(View.VISIBLE);


                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);

                retrofit2.Call<acceptRejectBean> call1 = cr.acceptReject(connId, b.userId + "-" + liveId, "2");
                call1.enqueue(new retrofit2.Callback<acceptRejectBean>() {
                    @Override
                    public void onResponse(retrofit2.Call<acceptRejectBean> call, retrofit2.Response<acceptRejectBean> response) {

                        try {
                            //cameraLayout1.setVisibility(View.VISIBLE);


                            goCoderBroadcastConfig.setHostAddress("ec2-13-58-47-70.us-east-2.compute.amazonaws.com");
                            goCoderBroadcastConfig.setPortNumber(1935);
                            goCoderBroadcastConfig.setApplicationName("live");
                            goCoderBroadcastConfig.setStreamName(b.userId + "-" + liveId);
                            goCoderBroadcastConfig = new WZBroadcastConfig(WZMediaConfig.FRAME_SIZE_640x480);
                            // Set the bitrate to 4000 Kbps
                            goCoderBroadcastConfig.setVideoBitRate(1200);

                            //Toast.makeText(MyApp.getContext(), goCoderBroadcastConfig.getConnectionURL().toString(), Toast.LENGTH_SHORT).show();

                            WZStreamingError configValidationError = goCoderBroadcastConfig.validateForBroadcast();

                            //if (configValidationError != null) {
                            //Toast.makeText(LiveScreen.this, configValidationError.getErrorDescription(), Toast.LENGTH_LONG).show();
                            //} else if (goCoderBroadcaster.getStatus().isRunning()) {
                            // Stop the broadcast that is currently running
                            //    goCoderBroadcaster.endBroadcast(PlayerActivity.this);
                            //} else {
                            // Start streaming
                            goCoderBroadcaster.startBroadcast(goCoderBroadcastConfig, player_first.this);
                            //}


                            accept1.setVisibility(View.GONE);
                            reject1.setVisibility(View.GONE);
                            reject3.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(retrofit2.Call<acceptRejectBean> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                        t.printStackTrace();
                    }
                });


            }
        });

        report.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                toast.setText("Report this User");
                toast.show();

                return false;

            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.report_dialog);
                dialog.show();


                final EditText text = dialog.findViewById(R.id.comment);
                Button submit = dialog.findViewById(R.id.submit);
                final ProgressBar bar = dialog.findViewById(R.id.progress);


                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String t = text.getText().toString();

                        if (t.length() > 0) {


                            bar.setVisibility(View.VISIBLE);


                            final bean b = (bean) getContext().getApplicationContext();

                            final Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(b.BASE_URL)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            final AllAPIs cr = retrofit.create(AllAPIs.class);


                            Call<reportBean> call = cr.reportUser(b.userId, timelineId, t);

                            call.enqueue(new Callback<reportBean>() {
                                @Override
                                public void onResponse(Call<reportBean> call, Response<reportBean> response) {


                                    if (response.body().getStatus().equals("1")) {
                                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                    }


                                    bar.setVisibility(View.GONE);

                                }

                                @Override
                                public void onFailure(Call<reportBean> call, Throwable t) {
                                    bar.setVisibility(View.GONE);
                                }
                            });


                        } else {
                            Toast.makeText(getContext(), "Please Enter a Reason", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });


        reject1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                progress.setVisibility(View.VISIBLE);

                final bean b = (bean) getContext().getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                retrofit2.Call<acceptRejectBean> call = cr.acceptReject(connId, "", "1");
                call.enqueue(new retrofit2.Callback<acceptRejectBean>() {
                    @Override
                    public void onResponse(retrofit2.Call<acceptRejectBean> call, retrofit2.Response<acceptRejectBean> response) {

                        try {
                            cameraLayout1.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(retrofit2.Call<acceptRejectBean> call, Throwable t) {
                        progress.setVisibility(View.GONE);

                    }
                });


            }
        });





        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mess = comment.getText().toString();

                if (mess.length() > 0) {
                    progress.setVisibility(View.VISIBLE);

                    final bean b = (bean) getContext().getApplicationContext();

                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);


                    retrofit2.Call<liveCommentBean> call = cr.commentLive(b.userId, liveId, mess);

                    call.enqueue(new retrofit2.Callback<liveCommentBean>() {
                        @Override
                        public void onResponse(retrofit2.Call<liveCommentBean> call, retrofit2.Response<liveCommentBean> response) {

                            try {

                                if (Objects.equals(response.body().getMessage(), "Video Comment Success")) {
                                    comment.setText("");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Some Error Occurred, Please try again", Toast.LENGTH_SHORT).show();
                            }


                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(retrofit2.Call<liveCommentBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                            Log.d("Video upload find ", t.toString());
                        }
                    });
                }

            }
        });

        chatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (chat.getVisibility() == View.VISIBLE) {
                    chat.setVisibility(View.GONE);
                } else if (chat.getVisibility() == View.GONE) {
                    chat.setVisibility(View.VISIBLE);
                }


            }
        });

        chatIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                toast.setText("Send a Message");
                toast.show();

                return false;

            }
        });


        gift.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                toast.setText("Send a Gift");
                toast.show();

                return false;

            }
        });


        gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (giftLayout.getVisibility() == View.VISIBLE) {
                    giftLayout.setVisibility(View.GONE);
                } else if (giftLayout.getVisibility() == View.GONE) {


                    progress.setVisibility(View.VISIBLE);
                    final bean b = (bean) getContext().getApplicationContext();
                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    final AllAPIs cr = retrofit.create(AllAPIs.class);
                    retrofit2.Call<com.yl.youthlive.giftPOJO.giftBean> call = cr.getGiftData(b.userId);

                    Log.d("userId", b.userId);

                    call.enqueue(new retrofit2.Callback<com.yl.youthlive.giftPOJO.giftBean>() {
                        @Override
                        public void onResponse(retrofit2.Call<giftBean> call, retrofit2.Response<giftBean> response) {
                            try {
                                gAdapter.setGridData(response.body().getData());
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Some Error Occurred, Please try again", Toast.LENGTH_SHORT).show();

                            }

                            progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(retrofit2.Call<giftBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                        }
                    });


                    giftLayout.setVisibility(View.VISIBLE);
                }


            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), TimelineProfile.class);
                intent.putExtra("userId", timelineId);
                startActivity(intent);

            }
        });

        adapter = new LiveAdapter(getContext(), vList);
        adapter2 = new LiveAdapter2(getContext(), cList);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);


        grid2.setAdapter(adapter2);
        grid2.setLayoutManager(manager2);


        bubbleView = (BubbleView) view.findViewById(R.id.bubble);
        List<Drawable> drawableList = new ArrayList<>();
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_indigo_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_deep_purple_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_cyan_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_blue_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_deep_purple_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_light_blue_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_lime_a200_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_pink_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_red_900_24dp));
        bubbleView.setDrawableList(drawableList);


        schedule(liveId);


        sendGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String gid = gAdapter.getGid();

                progress.setVisibility(View.VISIBLE);

                final bean b = (bean) getContext().getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                retrofit2.Call<sendGiftBean> call = cr.sendGift(b.userId, liveId, timelineId, gid, "1", "5");


                Log.d("asdasd", b.userId);
                Log.d("asdasd", liveId);
                Log.d("asdasd", timelineId);


                call.enqueue(new retrofit2.Callback<sendGiftBean>() {
                    @Override
                    public void onResponse(retrofit2.Call<sendGiftBean> call, retrofit2.Response<sendGiftBean> response) {

                        //Log.d("Asdasdsa", response.body().getMessage());


                        try {
                            if (Objects.equals(response.body().getStatus(), "0")) {
                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Some Error Occurred, Please try again", Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }


                        progress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(retrofit2.Call<sendGiftBean> call, Throwable t) {
                        Log.d("asdasd", t.toString());
                        progress.setVisibility(View.GONE);
                    }
                });

            }
        });


        commentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals("commentData")) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications


                    Log.d("data", intent.getStringExtra("data"));

                    String json = intent.getStringExtra("data");

                    Gson gson = new Gson();

                    Comment item = gson.fromJson(json, Comment.class);

                    adapter2.addComment(item);

                    grid2.scrollToPosition(0);

                    //displayFirebaseRegId();

                }/* else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }*/
            }
        };



        requestReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals("request")) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications

                    Log.d("data", intent.getStringExtra("data"));

                    String json = intent.getStringExtra("data");

                    connId = json;

                    cameraLayout1.setVisibility(View.VISIBLE);



                    //displayFirebaseRegId();

                }/* else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }*/
            }
        };




        likeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals("like")) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications


                    Log.d("data", intent.getStringExtra("data"));

                    String json = intent.getStringExtra("data");

                    int count1 = Integer.parseInt(json);

                    if (count1 > count) {
                        for (int i = 0; i < count1 - count; i++)

                            bubbleView.startAnimation(bubbleView.getWidth(), bubbleView.getHeight());

                        likeCount.setText(json);

                        count = count1;
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


        giftReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals("gift")) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications


                    Log.d("data", intent.getStringExtra("data"));

                    String json = intent.getStringExtra("data");

                    Gson gson = new Gson();

                    com.yl.youthlive.getIpdatedPOJO.Gift item = gson.fromJson(json, com.yl.youthlive.getIpdatedPOJO.Gift.class);


                    try {

                        giftName = item.getGiftId();

                        showGift(Integer.parseInt(item.getGiftId()), item.getGiftName());

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


        viewReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals("view")) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications


                    Log.d("data", intent.getStringExtra("data"));

                    String json = intent.getStringExtra("data");

                    Gson gson = new Gson();

                    com.yl.youthlive.getIpdatedPOJO.View item = gson.fromJson(json, com.yl.youthlive.getIpdatedPOJO.View.class);

                    adapter.addView(item);

                    //displayFirebaseRegId();

                }/* else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }*/
            }
        };


        return view;
    }

    private static final int REQUEST_CODE = 100;

    private static String STORE_DIRECTORY;

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            sMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);

            if (sMediaProjection != null) {
                File externalFilesDir = plactivity.getExternalFilesDir(null);
                if (externalFilesDir != null) {
                    STORE_DIRECTORY = externalFilesDir.getAbsolutePath() + "/screenshots/";
                    File storeDirectory = new File(STORE_DIRECTORY);
                    if (!storeDirectory.exists()) {
                        boolean success = storeDirectory.mkdirs();
                        if (!success) {
                            Log.e(TAG, "failed to create file storage directory.");
                            return;
                        }
                    }
                } else {
                    Log.e(TAG, "failed to create file storage directory, getExternalFilesDir is null.");
                    return;
                }

                // display metrics
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                mDensity = metrics.densityDpi;
                mDisplay = plactivity.getWindowManager().getDefaultDisplay();

                // create virtual display depending on device width / height
                createVirtualDisplay();

                // register orientation change callback
                mOrientationChangeCallback = new OrientationChangeCallback(getContext());
                if (mOrientationChangeCallback.canDetectOrientation()) {
                    mOrientationChangeCallback.enable();
                }

                // register media projection stop callback
                sMediaProjection.registerCallback(new MediaProjectionStopCallback(), mHandler);
            }
        }
    }

    private int mWidth;
    private int mHeight;
    private int mRotation;

    private ImageReader mImageReader;

    private VirtualDisplay mVirtualDisplay;
    private static final String SCREENCAP_NAME = "screencap";

    private static final int VIRTUAL_DISPLAY_FLAGS = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;

    private Handler mHandler;

    private String TAG = "ddfsdf";

    private int mDensity;

    private void createVirtualDisplay() {
        // get width and height
        Point size = new Point();
        mDisplay.getSize(size);
        mWidth = size.x;
        mHeight = size.y;

        // start capture reader
        mImageReader = ImageReader.newInstance(mWidth, mHeight, PixelFormat.RGBA_8888, 2);
        mVirtualDisplay = sMediaProjection.createVirtualDisplay(SCREENCAP_NAME, mWidth, mHeight, mDensity, VIRTUAL_DISPLAY_FLAGS, mImageReader.getSurface(), null, mHandler);
        mImageReader.setOnImageAvailableListener(new ImageAvailableListener(), mHandler);
    }

    private static int IMAGES_PRODUCED;

    private class ImageAvailableListener implements ImageReader.OnImageAvailableListener {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image image = null;
            FileOutputStream fos = null;
            Bitmap bitmap = null;

            try {


                if (coun == 0)
                {
                    image = reader.acquireLatestImage();
                    if (image != null) {
                        Image.Plane[] planes = image.getPlanes();
                        ByteBuffer buffer = planes[0].getBuffer();
                        int pixelStride = planes[0].getPixelStride();
                        int rowStride = planes[0].getRowStride();
                        int rowPadding = rowStride - pixelStride * mWidth;

                        // create bitmap

                        bitmap = Bitmap.createBitmap(mWidth + rowPadding / pixelStride, mHeight, Bitmap.Config.ARGB_8888);
                        bitmap.copyPixelsFromBuffer(buffer);

                        // write bitmap to a file
                        fos = new FileOutputStream(STORE_DIRECTORY + "/myscreen_" + IMAGES_PRODUCED + ".png");
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                        IMAGES_PRODUCED++;
                        Log.e(TAG, "captured image: " + IMAGES_PRODUCED);


                        Intent i = new Intent(Intent.ACTION_SEND);

                        i.setType("image/*");
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        byte[] bytes = stream.toByteArray();


                        i.putExtra(Intent.EXTRA_STREAM, getImageUri(getContext(), bitmap));
                        try {
                            startActivity(Intent.createChooser(i, "Share Screenshot..."));
                        } catch (android.content.ActivityNotFoundException ex) {

                            ex.printStackTrace();
                        }


                        coun++;


                        stopProjection();

                    }
                }




            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }

                if (bitmap != null) {
                    bitmap.recycle();
                }

                if (image != null) {
                    image.close();
                }
            }
        }
    }

    private void stopProjection() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (sMediaProjection != null) {
                    sMediaProjection.stop();
                }
            }
        });
    }

    private class OrientationChangeCallback extends OrientationEventListener {

        OrientationChangeCallback(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            final int rotation = mDisplay.getRotation();
            if (rotation != mRotation) {
                mRotation = rotation;
                try {
                    // clean up
                    if (mVirtualDisplay != null) mVirtualDisplay.release();
                    if (mImageReader != null) mImageReader.setOnImageAvailableListener(null, null);

                    // re-create virtual display depending on device width / height
                    createVirtualDisplay();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private OrientationChangeCallback mOrientationChangeCallback;

    private class MediaProjectionStopCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            Log.e("ScreenCapture", "stopping projection.");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mVirtualDisplay != null) mVirtualDisplay.release();
                    if (mImageReader != null) mImageReader.setOnImageAvailableListener(null, null);
                    if (mOrientationChangeCallback != null) mOrientationChangeCallback.disable();
                    sMediaProjection.unregisterCallback(MediaProjectionStopCallback.this);
                }
            });
        }
    }


    public void schedule(final String vid) {

        /*Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
*/
        final bean b = (bean) getContext().getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


                /*retrofit2.Call<getConnectionBean> call1 = cr.getConnection(b.userId, vid);


                call1.enqueue(new retrofit2.Callback<getConnectionBean>() {
                    @Override
                    public void onResponse(retrofit2.Call<getConnectionBean> call, retrofit2.Response<getConnectionBean> response) {


                        try {

                            if (connId.length() == 0) {
                                connId = response.body().getData().get(0).getId();

                                cameraLayout1.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(retrofit2.Call<getConnectionBean> call, Throwable t) {

                    }
                });*/


        SharedPreferences fcmPref = getContext().getSharedPreferences("fcm", Context.MODE_PRIVATE);

        String keey = fcmPref.getString("token", "");


        retrofit2.Call<getUpdatedBean> call = cr.getUpdatedData(b.userId, vid, keey);


        call.enqueue(new retrofit2.Callback<getUpdatedBean>() {
            @Override
            public void onResponse(retrofit2.Call<getUpdatedBean> call, retrofit2.Response<getUpdatedBean> response) {

                try {

                    likeCount.setText(response.body().getData().getLikesCount());

                    beans.setText(response.body().getData().getBeans2());
                    level.setText(response.body().getData().getLevel());


                    viewCount.setText(response.body().getData().getViewsCount());


                    username.setText(response.body().getData().getTimelineName());
                    ImageLoader loader = ImageLoader.getInstance();


                    giftBean.setText(response.body().getData().getBeans2());
                    giftDiamond.setText(response.body().getData().getDiamond());
                    giftCoin.setText(response.body().getData().getCoin());


                    loader.displayImage(response.body().getData().getTimelineProfileImage(), image);

                    adapter2.setGridData(response.body().getData().getComments());
                    adapter.setGridData(response.body().getData().getViews());

                    int count1 = Integer.parseInt(response.body().getData().getLikesCount());

                    if (response.body().getData().getGift().size() > 0) {
                        try {

                            giftName = response.body().getData().getGift().get(0).getGiftId();

                            showGift(Integer.parseInt(response.body().getData().getGift().get(0).getGiftId()), response.body().getData().getGift().get(0).getGiftName());


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    //Log.d("asd", String.valueOf(count1));

                    if (count1 > count) {
                        Log.d("Asdasd", "entered");

                        bubbleView.startAnimation(bubbleView.getWidth(), bubbleView.getHeight());

                        count = count1;
                    }


                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(commentReceiver,
                            new IntentFilter("commentData"));

                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(viewReceiver,
                            new IntentFilter("view"));

                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(likeReceiver,
                            new IntentFilter("like"));

                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(giftReceiver,
                            new IntentFilter("gift"));
                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(requestReceiver,
                            new IntentFilter("request"));

                    plactivity.initPlayer(liveId);


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Some Error Occurred, Please try again", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(retrofit2.Call<getUpdatedBean> call, Throwable t) {

            }
        });

           /* }
        }, 0, 1000);*/

    }


    @Override
    public void onPause() {
        super.onPause();

        mOkHttpClient.dispatcher().cancelAll();

    }


    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(commentReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(viewReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(likeReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(giftReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(requestReceiver);
        super.onStop();
    }


    public void showGift(final int pos, String title) {

        giftLayout1.setVisibility(View.VISIBLE);

        Glide.with(getContext().getApplicationContext()).load(gfts[pos - 1]).into(giftIcon);

        giftTitle.setText(title);

        Timer t = new Timer();

        t.schedule(new TimerTask() {
            @Override
            public void run() {


                giftLayout1.getHandler().post(new Runnable() {
                    public void run() {
                        giftLayout1.setVisibility(View.GONE);
                    }
                });

            }
        }, 2500);


    }

    public void BlockPersson(View view) {
        PersonBlock();
    }

    private void PersonBlock() {


        Log.d("asdasd", "asdasd");

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.report_dialog);
        dialog.setCancelable(true);
        dialog.show();

        final EditText comment = (EditText) dialog.findViewById(R.id.comment);
        Button submit = (Button) dialog.findViewById(R.id.submit);
        final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progress);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String com = comment.getText().toString();

                if (com.length() > 0) {


                    progressBar.setVisibility(View.VISIBLE);

                    final bean b = (bean) getContext().getApplicationContext();

                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);


                    retrofit2.Call<feedBackBean> call = cr.report(b.userId, com, timelineId);

                    call.enqueue(new retrofit2.Callback<feedBackBean>() {
                        @Override
                        public void onResponse(retrofit2.Call<feedBackBean> call, retrofit2.Response<feedBackBean> response) {


                            try {
                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Some Error Occurred, Please try again", Toast.LENGTH_SHORT).show();
                            }


                            progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(retrofit2.Call<feedBackBean> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                } else {
                    Toast.makeText(getContext(), "Please Enter a Comment", Toast.LENGTH_SHORT).show();
                }

            }
        });


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
    public void onWZError(WZStatus wzStatus) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                /*Toast.makeText(LiveScreen.this,
                        "Streaming error: " + wzStatus.getLastError().getErrorDescription(),
                        Toast.LENGTH_LONG).show();*/
            }
        });
    }


    public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.ViewHolder> {

        List<com.yl.youthlive.getIpdatedPOJO.View> list = new ArrayList<>();
        Context context;

        public LiveAdapter(Context context, List<com.yl.youthlive.getIpdatedPOJO.View> list) {
            this.context = context;
            this.list = list;
        }

        public void setGridData(List<com.yl.youthlive.getIpdatedPOJO.View> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public void addView(com.yl.youthlive.getIpdatedPOJO.View item) {
            list.add(0, item);
            notifyItemInserted(0);

            viewCount.setText(String.valueOf(list.size()));

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.viewers_model, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.setIsRecyclable(false);

            final com.yl.youthlive.getIpdatedPOJO.View item = list.get(position);


            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();

            ImageLoader loader = ImageLoader.getInstance();

            loader.displayImage(item.getUserImage(), holder.image, options);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String uid = item.getUserId();
                    uid.replace("\"", "");

                    Intent intent = new Intent(context, TimelineProfile.class);
                    intent.putExtra("userId", uid);
                    startActivity(intent);

                }
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            CircleImageView image;

            public ViewHolder(View itemView) {
                super(itemView);
                image = (CircleImageView) itemView.findViewById(R.id.image);

            }
        }
    }


    public class LiveAdapter2 extends RecyclerView.Adapter<LiveAdapter2.ViewHolder> {


        List<Comment> list = new ArrayList<>();
        Context context;

        public LiveAdapter2(Context context, List<Comment> list) {
            this.context = context;
            this.list = list;
        }

        public void setGridData(List<Comment> list) {
            this.list = list;
            notifyDataSetChanged();
        }


        public void addComment(Comment item) {
            list.add(0, item);
            notifyItemInserted(0);
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.chat_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.setIsRecyclable(false);

            final Comment item = list.get(position);

            if (position == 0) {

                holder.name.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.gradient_white_top, 0, 0);

            } else if (position == list.size() - 1) {
                holder.name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.gradient_white);
            } else {
                holder.name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            final String uid = item.getUserId().replace("\"", "");

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();

            ImageLoader loader = ImageLoader.getInstance();

            String im = item.getUserImage().replace("\"", "");

            loader.displayImage(im, holder.index, options);

            final bean b = (bean) context.getApplicationContext();



            //holder.user.setText(us);

            holder.index.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, TimelineProfile.class);
                    intent.putExtra("userId", uid);
                    startActivity(intent);

                }
            });


            if (Objects.equals(item.getFriendStatus().getFollow(), "true")) {
                holder.add.setBackgroundResource(R.drawable.tick);
            } else {
                holder.add.setBackgroundResource(R.drawable.plus_red);
            }


            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    progress.setVisibility(View.VISIBLE);

                    final bean b = (bean) context.getApplicationContext();

                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);


                    retrofit2.Call<followBean> call = cr.follow(b.userId, uid);

                    call.enqueue(new retrofit2.Callback<followBean>() {
                        @Override
                        public void onResponse(retrofit2.Call<followBean> call, retrofit2.Response<followBean> response) {


                            try {
                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Some Error Occurred, Please try again", Toast.LENGTH_SHORT).show();
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


            if (Objects.equals(uid, b.userId)) {
                holder.add.setVisibility(View.GONE);
            } else {
                holder.add.setVisibility(View.VISIBLE);
            }

            String com = item.getComment().replace("\"", "");

            String us = item.getUserName().replace("\"", "");

            holder.name.setText(Html.fromHtml("<font color=\"#cdcdcd\">" + us + ":</font> " + com));

        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            CircleImageView index;
            ImageButton add;

            public ViewHolder(View itemView) {
                super(itemView);

                index = (CircleImageView) itemView.findViewById(R.id.index);
                name = (TextView) itemView.findViewById(R.id.name);

                add = (ImageButton) itemView.findViewById(R.id.add);

            }
        }
    }


    public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.Viewholder> {

        Integer[] gfts = {
                R.drawable.gift1,
                R.drawable.gift2,
                R.drawable.gift3,
                R.drawable.gift4,
                R.drawable.gift5,
                R.drawable.gift6
        };

        List<Datum> list = new ArrayList<>();
        Context context;
        int posi = 0;
        String gid = "0";

        public GiftAdapter(Context context, List<Datum> list) {
            this.context = context;
            this.list = list;
        }


        public void setGridData(List<Datum> list) {
            this.list = list;
            notifyDataSetChanged();
        }


        @Override
        public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.gift_list_model, parent, false);
            return new Viewholder(view);
        }

        public void setPosition(int posi) {
            this.posi = posi;
            notifyDataSetChanged();
        }

        public int getPosi() {
            return posi;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        @Override
        public void onBindViewHolder(final Viewholder holder, int position) {

            final Datum item = list.get(position);

            holder.title.setText(item.getGiftName());


            Glide.with(context).load(gfts[position]).into(holder.image);


            holder.price.setText(item.getPurchaseQty());


            if (position == getPosi()) {
                holder.itemView.setBackgroundResource(R.drawable.red_gift);
            } else {
                holder.itemView.setBackgroundResource(0);
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setPosition(holder.getAdapterPosition());
                    setGid(item.getGiftId());

                }
            });

            /*holder.send.setOnClickListener(new View.OnClickListener() {
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


                    retrofit2.Call<sendGiftBean> call = cr.sendGift(b.userId, liveId, timelineId, item.getGiftId(), "1", "5");

                    call.enqueue(new retrofit2.Callback<sendGiftBean>() {
                        @Override
                        public void onResponse(retrofit2.Call<sendGiftBean> call, retrofit2.Response<sendGiftBean> response) {


                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(retrofit2.Call<sendGiftBean> call, Throwable t) {
                            Log.d("asdasd", t.toString());
                            progress.setVisibility(View.GONE);
                        }
                    });
\
                }
            });*/


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Viewholder extends RecyclerView.ViewHolder {

            TextView title, price;
            ImageView image;


            public Viewholder(View itemView) {
                super(itemView);

                title = (TextView) itemView.findViewById(R.id.title);
                price = (TextView) itemView.findViewById(R.id.price);
                image = (ImageView) itemView.findViewById(R.id.image);


            }
        }

    }


    private MediaProjectionManager mProjectionManager;
    //SimpleExoPlayerView thumb;
    private static MediaProjection sMediaProjection;





    private Display mDisplay;

    private void startProjection() {
        startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
    }


}
