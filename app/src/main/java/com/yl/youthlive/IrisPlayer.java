package com.yl.youthlive;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.VideoView;



import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.veer.hiddenshot.HiddenShot;
import com.wowza.gocoder.sdk.api.errors.WZStreamingError;
import com.yasic.bubbleview.BubbleView;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.acceptRejectPOJO.acceptRejectBean;
import com.yl.youthlive.feedBackPOJO.feedBackBean;
import com.yl.youthlive.followPOJO.followBean;
import com.yl.youthlive.getConnectionPOJO.getConnectionBean;
import com.yl.youthlive.getIpdatedPOJO.Comment;
import com.yl.youthlive.getIpdatedPOJO.getUpdatedBean;
import com.yl.youthlive.giftPOJO.Datum;
import com.yl.youthlive.giftPOJO.giftBean;
import com.yl.youthlive.goLivePOJO.goLiveBean;
import com.yl.youthlive.liveCommentPOJO.liveCommentBean;
import com.yl.youthlive.liveLikePOJO.liveLikeBean;
import com.yl.youthlive.sendGiftPOJO.sendGiftBean;
import com.yl.youthlive.startStreamPOJO.startStreamBean;
import com.yl.youthlive.streamPOJO.LiveStream;
import com.yl.youthlive.streamPOJO.streamBean;
import com.yl.youthlive.streamResponsePOJO.streamResponseBean;

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

public class IrisPlayer extends AppCompatActivity {


    /*RecyclerView grid;
    RecyclerView grid2;
    LinearLayoutManager manager;
    LiveAdapter adapter;
    LiveAdapter2 adapter2;
    LinearLayoutManager manager2;
    Broadcaster mBroadcaster;
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
    BroadcastPlayer mBroadcastPlayer;
    //TextView mPlayerStatusTextView;
    final OkHttpClient mOkHttpClient = new OkHttpClient();

    String liveId = "";


    int count = 0;

    String timelineId;

    SurfaceViewWithAutoAR videoView;

    List<Comment> cList;
    List<com.youthlive.youthlive.getIpdatedPOJO.View> vList;

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

    List<Datum2> list;
    LinearLayoutManager gManager;

    ImageButton follow;

    //VideoView videoView;

    RelativeLayout cameraLayout1, cameraLayout2;

    SurfaceViewWithAutoAR camera1;

    ImageButton accept1, accept2, reject1, reject2, reject3;

    private static final String APPLICATION_ID = "ncdEaJSqk7qbRbDRMOdK6Q";
    private static final String API_KEY = "7egiel2r2frgwoe9nwnx3p1n1";


    String key;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iris_player);

        /*uri = getIntent().getStringExtra("uri");
        liveId = getIntent().getStringExtra("liveId");
        timelineId = getIntent().getStringExtra("timelineId");

        videoView = (SurfaceViewWithAutoAR) findViewById(R.id.video);

        cameraLayout1 = (RelativeLayout) findViewById(R.id.camera_layout1);

        camera1 = (SurfaceViewWithAutoAR) findViewById(R.id.camera1);

        accept1 = (ImageButton) findViewById(R.id.accept1);


        reject1 = (ImageButton) findViewById(R.id.reject1);
        reject3 = (ImageButton) findViewById(R.id.reject3);


        follow = (ImageButton) findViewById(R.id.follow);

        giftLayout1 = (LinearLayout) findViewById(R.id.gift_layout);
        giftIcon = (ImageView) findViewById(R.id.gift_icon);
        giftTitle = (TextView) findViewById(R.id.gift_title);

        viewCount = (TextView) findViewById(R.id.view_count);

        cList = new ArrayList<>();
        vList = new ArrayList<>();

        giftLayout = (LinearLayout) findViewById(R.id.ggift);
        giftBean = (TextView) findViewById(R.id.gift_beans);
        giftDiamond = (TextView) findViewById(R.id.gift_diamond);
        giftCoin = (TextView) findViewById(R.id.gift_coin);
        giftList = (RecyclerView) findViewById(R.id.gift_list);
        sendGift = (Button) findViewById(R.id.send_gift);

        beans = (TextView) findViewById(R.id.beans);
        level = (TextView) findViewById(R.id.level);

        list = new ArrayList<>();

        gManager = new GridLayoutManager(this, 3);
        gAdapter = new GiftAdapter(this, list);

        giftList.setAdapter(gAdapter);
        giftList.setLayoutManager(gManager);

        chat = (LinearLayout) findViewById(R.id.chat);
        actions = (LinearLayout) findViewById(R.id.actions);
        chatIcon = (ImageButton) findViewById(R.id.chat_icon);
        crop = (ImageButton) findViewById(R.id.crop);
        gift = (ImageButton) findViewById(R.id.gift);
        progress = (ProgressBar) findViewById(R.id.progress);
        switchCamera = (ImageButton) findViewById(R.id.switch_camera);
        comment = (EditText) findViewById(R.id.comment);
        send = (FloatingActionButton) findViewById(R.id.send);

        likeCount = (TextView) findViewById(R.id.like_count);

        image = (CircleImageView) findViewById(R.id.image);
        username = (TextView) findViewById(R.id.name);


        //mVideoSurface = (SurfaceView) findViewById(R.id.PreviewSurfaceView);


        grid = (RecyclerView) findViewById(R.id.grid);
        grid2 = (RecyclerView) findViewById(R.id.grid2);
        manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        manager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        heart = (ImageButton) findViewById(R.id.heart);
        close = (ImageButton) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mBroadcaster.stopBroadcast();
                finish();
            }
        });
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final bean b = (bean) getApplicationContext();

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

        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HiddenShot.getInstance().buildShotAndShare(IrisPlayer.this, "Check this out");

            }
        });


        follow.setOnClickListener(new View.OnClickListener() {
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


                retrofit2.Call<followBean> call = cr.follow(b.userId, timelineId);

                call.enqueue(new retrofit2.Callback<followBean>() {
                    @Override
                    public void onResponse(retrofit2.Call<followBean> call, retrofit2.Response<followBean> response) {

                        try {
                            Toast.makeText(IrisPlayer.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(IrisPlayer.this, "Some Error Occurred, Please try again", Toast.LENGTH_SHORT).show();
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

                mBroadcaster.stopBroadcast();

                cameraLayout1.setVisibility(View.GONE);

            }
        });


        accept1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mBroadcaster = new Broadcaster(IrisPlayer.this, APPLICATION_ID, mBroadcasterObserver);
                mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation());
                mBroadcaster.setTitle(getIntent().getStringExtra("title"));


                //mBroadcaster.setAuthor(b.userImage);
                mBroadcaster.setSendPosition(true);
                //mBroadcaster.setCustomData(b.userId);
                mBroadcaster.setSaveOnServer(false);


                if (mBroadcaster.canStartBroadcasting()) {
                    mBroadcaster.startBroadcast();
                }


            }
        });


        reject1.setOnClickListener(new View.OnClickListener() {
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

                    final bean b = (bean) getApplicationContext();

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
                                Toast.makeText(IrisPlayer.this, "Some Error Occurred, Please try again", Toast.LENGTH_SHORT).show();
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


        gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (giftLayout.getVisibility() == View.VISIBLE) {
                    giftLayout.setVisibility(View.GONE);
                } else if (giftLayout.getVisibility() == View.GONE) {


                    progress.setVisibility(View.VISIBLE);
                    final bean b = (bean) getApplicationContext();
                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    final AllAPIs cr = retrofit.create(AllAPIs.class);
                    retrofit2.Call<com.youthlive.youthlive.giftPOJO.giftBean> call = cr.getGiftData(b.userId);

                    Log.d("userId", b.userId);

                    call.enqueue(new retrofit2.Callback<com.youthlive.youthlive.giftPOJO.giftBean>() {
                        @Override
                        public void onResponse(retrofit2.Call<giftBean> call, retrofit2.Response<giftBean> response) {
                            try {
                                gAdapter.setGridData(response.body().getData());
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(IrisPlayer.this, "Some Error Occurred, Please try again", Toast.LENGTH_SHORT).show();

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

                Intent intent = new Intent(IrisPlayer.this, TimelineProfile.class);
                intent.putExtra("userId", timelineId);
                startActivity(intent);

            }
        });

        adapter = new LiveAdapter(this, vList);
        adapter2 = new LiveAdapter2(this, cList);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);


        grid2.setAdapter(adapter2);
        grid2.setLayoutManager(manager2);


        bubbleView = (BubbleView) findViewById(R.id.bubble);
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

                final bean b = (bean) getApplicationContext();

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
                                Toast.makeText(IrisPlayer.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            Toast.makeText(IrisPlayer.this, "Some Error Occurred, Please try again", Toast.LENGTH_SHORT).show();

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
        });*/

    }


    /*public void schedule(final String vid) {

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


                retrofit2.Call<getConnectionBean> call1 = cr.getConnection(b.userId, vid);


                call1.enqueue(new retrofit2.Callback<getConnectionBean>() {
                    @Override
                    public void onResponse(retrofit2.Call<getConnectionBean> call, retrofit2.Response<getConnectionBean> response) {


                        try {

                            if (connId.length() == 0) {
                                connId = response.body().getData().get(0).getId();

                                if (connId.length() > 0) {
                                    cameraLayout1.setVisibility(View.VISIBLE);
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(retrofit2.Call<getConnectionBean> call, Throwable t) {

                    }
                });


                retrofit2.Call<getUpdatedBean> call = cr.getUpdatedData(b.userId, vid);


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


                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(IrisPlayer.this, "Some Error Occurred, Please try again", Toast.LENGTH_SHORT).show();

                        }


                    }

                    @Override
                    public void onFailure(retrofit2.Call<getUpdatedBean> call, Throwable t) {

                    }
                });

            }
        }, 0, 1000);

    }


    private Broadcaster.Observer mBroadcasterObserver = new Broadcaster.Observer() {
        @Override
        public void onConnectionStatusChange(BroadcastStatus broadcastStatus) {
        }

        @Override
        public void onStreamHealthUpdate(int i) {
        }

        @Override
        public void onConnectionError(ConnectionError connectionError, String s) {
        }

        @Override
        public void onCameraError(CameraError cameraError) {
        }

        @Override
        public void onChatMessage(String s) {
        }

        @Override
        public void onResolutionsScanned() {
        }

        @Override
        public void onCameraPreviewStateChanged() {
        }

        @Override
        public void onBroadcastInfoAvailable(String s, String s1) {


        }

        @Override
        public void onBroadcastIdAvailable(String s) {

            Log.d("id", s);

            progress.setVisibility(View.VISIBLE);

            final bean b = (bean) getApplicationContext();

            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.irisplatform.io/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final AllAPIs cr = retrofit.create(AllAPIs.class);

            Call<singleStreamBean> call1 = cr.getSingleLive(s);
            call1.enqueue(new retrofit2.Callback<singleStreamBean>() {
                @Override
                public void onResponse(Call<singleStreamBean> call, Response<singleStreamBean> response) {


                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);


                    //Log.d("asasdasd" , response.body());

                    Call<acceptRejectBean> call2 = cr.acceptReject(connId, response.body().getResourceUri(), "2");

                    call2.enqueue(new Callback<acceptRejectBean>() {
                        @Override
                        public void onResponse(Call<acceptRejectBean> call, Response<acceptRejectBean> response) {

                            try {
                                //cameraLayout1.setVisibility(View.VISIBLE);
                                accept1.setVisibility(View.GONE);
                                reject1.setVisibility(View.GONE);
                                reject3.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<acceptRejectBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                        }
                    });

                }

                @Override
                public void onFailure(Call<singleStreamBean> call, Throwable t) {
                    progress.setVisibility(View.GONE);

                }
            });

        }
    };


    BroadcastPlayer.Observer mBroadcastPlayerObserver = new BroadcastPlayer.Observer() {
        @Override
        public void onStateChange(PlayerState playerState) {
            Toast.makeText(IrisPlayer.this, playerState.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBroadcastLoaded(boolean live, int width, int height) {
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        mOkHttpClient.dispatcher().cancelAll();
        videoView = null;
        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = null;
    }


    public void showGift(final int pos, String title) {

        giftLayout1.setVisibility(View.VISIBLE);

        Glide.with(getApplicationContext()).load(gfts[pos - 1]).into(giftIcon);

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


    @Override
    protected void onResume() {
        super.onResume();
        //mVideoSurface = (SurfaceView) findViewById(R.id.PreviewSurfaceView);
        //mPlayerStatusTextView.setText("Loading latest broadcast");
        //getLatestResourceUri();

        initPlayer(uri);

    }

    public void BlockPersson(View view) {
        PersonBlock();
    }

    private void PersonBlock() {


        Log.d("asdasd", "asdasd");

        final Dialog dialog = new Dialog(IrisPlayer.this);
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

                    final bean b = (bean) getApplicationContext();

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
                                Toast.makeText(IrisPlayer.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(IrisPlayer.this, "Some Error Occurred, Please try again", Toast.LENGTH_SHORT).show();
                            }


                            progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(retrofit2.Call<feedBackBean> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                } else {
                    Toast.makeText(IrisPlayer.this, "Please Enter a Comment", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.ViewHolder> {

        List<com.youthlive.youthlive.getIpdatedPOJO.View> list = new ArrayList<>();
        Context context;

        public LiveAdapter(Context context, List<com.youthlive.youthlive.getIpdatedPOJO.View> list) {
            this.context = context;
            this.list = list;
        }

        public void setGridData(List<com.youthlive.youthlive.getIpdatedPOJO.View> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.viewers_model, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.setIsRecyclable(false);

            final com.youthlive.youthlive.getIpdatedPOJO.View item = list.get(position);

            ImageLoader loader = ImageLoader.getInstance();

            loader.displayImage(item.getUserImage(), holder.image);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, TimelineProfile.class);
                    intent.putExtra("userId", item.getUserId());
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

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getUserImage(), holder.index);


            holder.user.setText(item.getUserName());


            holder.index.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, TimelineProfile.class);
                    intent.putExtra("userId", item.getUserId());
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


                    retrofit2.Call<followBean> call = cr.follow(b.userId, item.getUserId());

                    call.enqueue(new retrofit2.Callback<followBean>() {
                        @Override
                        public void onResponse(retrofit2.Call<followBean> call, retrofit2.Response<followBean> response) {


                            try {
                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(IrisPlayer.this, "Some Error Occurred, Please try again", Toast.LENGTH_SHORT).show();
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


            bean b = (bean) context.getApplicationContext();

            if (Objects.equals(item.getUserId(), b.userId)) {
                holder.add.setVisibility(View.GONE);
            } else {
                holder.add.setVisibility(View.VISIBLE);
            }

            holder.name.setText(item.getComment());

        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, user;
            CircleImageView index;
            ImageButton add;

            public ViewHolder(View itemView) {
                super(itemView);

                index = (CircleImageView) itemView.findViewById(R.id.index);
                name = (TextView) itemView.findViewById(R.id.name);
                user = (TextView) itemView.findViewById(R.id.user);
                add = (ImageButton) itemView.findViewById(R.id.add);

            }
        }
    }


    void initPlayer(String resourceUri) {

        if (resourceUri == null) {
            //if (mPlayerStatusTextView != null)
            //   mPlayerStatusTextView.setText("Could not get info about latest broadcast");
            return;
        }
        if (videoView == null) {
            // UI no longer active
            return;
        }

        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = new BroadcastPlayer(this, resourceUri, APPLICATION_ID, mBroadcastPlayerObserver);


        mBroadcastPlayer.setSurfaceView(videoView);
        mBroadcastPlayer.load();


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

        List<Datum2> list = new ArrayList<>();
        Context context;
        int posi = 0;
        String gid = "0";

        public GiftAdapter(Context context, List<Datum2> list) {
            this.context = context;
            this.list = list;
        }


        public void setGridData(List<Datum2> list) {
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
        public void onBindViewHolder(Viewholder holder, final int position) {

            final Datum2 item = list.get(position);

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

                    setPosition(position);
                    setGid(item.getGiftId());

                }
            });

            *//*holder.send.setOnClickListener(new View.OnClickListener() {
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

                }
            });*//*


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
*/

}
