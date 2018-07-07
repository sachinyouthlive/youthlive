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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
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
import com.streamaxia.android.CameraPreview;
import com.streamaxia.android.StreamaxiaPublisher;
import com.streamaxia.android.handlers.EncoderHandler;
import com.streamaxia.android.handlers.RecordHandler;
import com.streamaxia.android.handlers.RtmpHandler;
import com.yasic.bubbleview.BubbleView;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.acceptRejectPOJO.acceptRejectBean;
import com.yl.youthlive.endLivePOJO.Data;
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
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class player_firstNew extends Fragment implements EncoderHandler.EncodeListener, RecordHandler.RecordListener, RtmpHandler.RtmpListener {

    TextView newMessage;

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    ImageButton emoji, message, send, gift, connect, crop;

    EmojiconEditText comment;

    RecyclerView commentGrid;
    LinearLayoutManager commentsManager;
    CommentsAdapter commentsAdapter;
    List<Comment> commentList;

    BroadcastReceiver commentReceiver;
    BroadcastReceiver likeReceiver;
    BroadcastReceiver viewReceiver;
    BroadcastReceiver giftReceiver;
    BroadcastReceiver endReceiver;
    BroadcastReceiver requestReceiver;

    View rootView;
    private EmojIconActions emojIcon;


    ProgressBar progress;
    String liveId;

    ImageButton timeLineFollow;


    final OkHttpClient mOkHttpClient = new OkHttpClient();


    CameraPreview goCoderCameraView;

    //WZBroadcast goCoderBroadcaster;

    // The GoCoder SDK audio device
   // private WZAudioDevice goCoderAudioDevice;

    // The broadcast configuration settings
   // private WZBroadcastConfig goCoderBroadcastConfig;

   // private WowzaGoCoder goCoder;

    private static final int REQUEST_CODE = 100;

    private MediaProjectionManager mProjectionManager;
    //SimpleExoPlayerView thumb;
    private static MediaProjection sMediaProjection;
    private Display mDisplay;

    String TAG = "BroadcasterFragment1";

    private static String STORE_DIRECTORY;

    int coun = 0;

    TextView likeCount;

    private BubbleView bubbleView;

    String connId;

    int count = 0;

    ImageButton heart;

    CircleImageView timelineProfile;
    TextView timelineName;
    TextView liveUsers;
    TextView totalBeans;
    ImageButton end;
    RecyclerView viewersGrid;
    LinearLayoutManager viewersManager;
    ViewsAdapter viewsAdapter;
    List<com.yl.youthlive.getIpdatedPOJO.View> viewsList;

    VideoPlayer player;

    String timelineId;

    String image;


    RelativeLayout cameraLayout1;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.player_first_frag, container, false);


        player = (VideoPlayer)getActivity();

        mProjectionManager = (MediaProjectionManager) player.getSystemService(Context.MEDIA_PROJECTION_SERVICE);


        bubbleView = (BubbleView) view.findViewById(R.id.bubble);
        timeLineFollow = view.findViewById(R.id.folloview_friends);


        cameraLayout1 = (RelativeLayout) view.findViewById(R.id.camera_layout1);



        goCoderCameraView = (CameraPreview) view.findViewById(R.id.camera1);
      // goCoderCameraView.setZOrderOnTop(true);




        /*accept1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final bean b = (bean) getContext().getApplicationContext();

                StreamaxiaPublisher mPublisher = new StreamaxiaPublisher(goCoderCameraView, getContext());

                mPublisher.setEncoderHandler(new EncoderHandler(player_firstNew.this));
                mPublisher.setRtmpHandler(new RtmpHandler(player_firstNew.this));
                mPublisher.setRecordEventHandler(new RecordHandler(player_firstNew.this));
                goCoderCameraView.startCamera();
                mPublisher.setVideoOutputResolution(400, 200, getContext().getResources().getConfiguration().orientation);


                mPublisher.startPublish("rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/live/" + b.userId + liveId);







            }
        });*/


        /*report.setOnClickListener(new View.OnClickListener() {
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
*/

/*
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


                Call<acceptRejectBean> call = cr.acceptReject(connId, "", "1");
                call.enqueue(new Callback<acceptRejectBean>() {
                    @Override
                    public void onResponse(Call<acceptRejectBean> call, Response<acceptRejectBean> response) {

                        try {
                            cameraLayout1.setVisibility(View.GONE);
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
        });



*/


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

        commentList = new ArrayList<>();
        viewsList = new ArrayList<>();

        newMessage = view.findViewById(R.id.textView4);
        likeCount = view.findViewById(R.id.textView5);

        emoji = view.findViewById(R.id.imageButton4);
        message = view.findViewById(R.id.imageButton3);
        send = view.findViewById(R.id.imageButton5);
        gift = view.findViewById(R.id.imageButton6);
        connect = view.findViewById(R.id.imageButton2);
        crop = view.findViewById(R.id.imageButton7);

        progress = view.findViewById(R.id.progressBar3);

        rootView = view.findViewById(R.id.root_view);

        comment = view.findViewById(R.id.editText);
        commentGrid = view.findViewById(R.id.comment_grid);

        heart = view.findViewById(R.id.imageButton);

        timelineProfile = view.findViewById(R.id.image);
        timelineName = view.findViewById(R.id.name);
        liveUsers = view.findViewById(R.id.view_count);
        totalBeans = view.findViewById(R.id.beans);
        end = view.findViewById(R.id.imageButton10);
        viewersGrid = view.findViewById(R.id.viewers_grid);
        viewersManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        viewsAdapter = new ViewsAdapter(getContext(), viewsList);
        viewersGrid.setAdapter(viewsAdapter);
        viewersGrid.setLayoutManager(viewersManager);


        emojIcon = new EmojIconActions(getActivity(), rootView, comment, emoji);
        emojIcon.ShowEmojIcon();


        comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    //do here your stuff f

                    String mess = comment.getText().toString();

                    if (mess.length() > 0) {
                        progress.setVisibility(View.VISIBLE);

                        final bean b = (bean) getActivity().getApplicationContext();

                        final Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.BASE_URL)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        final AllAPIs cr = retrofit.create(AllAPIs.class);


                        Call<liveCommentBean> call = cr.commentLive(b.userId, liveId, mess, "basic");

                        call.enqueue(new Callback<liveCommentBean>() {
                            @Override
                            public void onResponse(Call<liveCommentBean> call, retrofit2.Response<liveCommentBean> response) {


                                if (Objects.equals(response.body().getMessage(), "Video Comment Success")) {
                                    comment.setText("");
                                }

                                progress.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<liveCommentBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                                Log.d("Video upload find ", t.toString());
                            }
                        });
                    }


                    return true;
                }
                return false;
            }
        });


        commentsManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);

        commentsAdapter = new CommentsAdapter(getContext(), commentList);

        commentGrid.setAdapter(commentsAdapter);
        commentGrid.setLayoutManager(commentsManager);

        newMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commentGrid.smoothScrollToPosition(0);
                loading = true;
                newMessage.setVisibility(View.GONE);

            }
        });



        /*start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/videochat/demo");
                StreamaxiaPlayer mStreamaxiaPlayer = new StreamaxiaPlayer();

                mStreamaxiaPlayer.initStreamaxiaPlayer(surfaceView, frameLayout,
                        stateText, VideoBroadcaster.this, VideoBroadcaster.this, uri);


                mStreamaxiaPlayer.play(uri, StreamaxiaPlayer.TYPE_RTMP);


            }
        });*/

        final bean b = (bean)getContext().getApplicationContext();

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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

                        bubbleView.startAnimation(bubbleView.getWidth(), bubbleView.getHeight());

                    }

                    @Override
                    public void onFailure(retrofit2.Call<liveLikeBean> call, Throwable t) {

                    }
                });






            }
        });


        commentGrid.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = commentsManager.getItemCount();
                    firstVisibleItem = commentsManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {
                        // End has been reached

                        // Do something
                        current_page++;
                        newMessage.setVisibility(View.GONE);

                        //onLoadMore(current_page);

                        loading = true;
                    }

                } else if (dy < 0) {

                    loading = false;

                }


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

                    commentsAdapter.addComment(item);

                    if (loading) {
                        commentGrid.scrollToPosition(0);
                        loading = true;
                        newMessage.setVisibility(View.GONE);
                    } else {
                        Log.d("lloogg", "new message");

                        newMessage.setVisibility(View.VISIBLE);
                    }

                    //

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
                        for (int i = 0; i < count1 - count; i++) {
                            bubbleView.startAnimation(bubbleView.getWidth(), bubbleView.getHeight());
                        }


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

                    String id = item.getUserId();
                    if (!id.equals(b.userId)) {
                        viewsAdapter.addView(item);
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

                        String giftName = item.getGiftName();

                        Comment comm = new Comment();

                        comm.setType("gift");
                        comm.setUserId(item.getSenbdId());

                        commentsAdapter.addComment(comm);

                        //comm.set


                        //showGift(Integer.parseInt(item.getGiftId()), item.getGiftName());

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


        endReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals("live_end")) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications


                    Log.d("data", intent.getStringExtra("data"));

                    String json = intent.getStringExtra("data");

                    Gson gson = new Gson();

                    Data item = gson.fromJson(json, Data.class);


                    try {

                        Intent intent1 = new Intent(getContext() , LiveEndedPlayer.class);
                        intent1.putExtra("image" , image);
                        intent1.putExtra("id" , timelineId);
                        intent1.putExtra("name" , timelineName.getText().toString());
                        intent1.putExtra("time" , item.getLiveTime());
                        intent1.putExtra("views" , item.getViewers());
                        startActivity(intent1);
                        getActivity().finish();


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



                    final Dialog dialog = new Dialog(player);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.new_connection_dialog);
                    dialog.show();

                    Button accept = dialog.findViewById(R.id.button11);
                    Button deny = dialog.findViewById(R.id.button12);
                    final ProgressBar dp = dialog.findViewById(R.id.progressBar9);


                    accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            final bean b = (bean) player.getApplicationContext();

                            StreamaxiaPublisher mPublisher = new StreamaxiaPublisher(goCoderCameraView, player);

                            mPublisher.setEncoderHandler(new EncoderHandler(player_firstNew.this));
                            mPublisher.setRtmpHandler(new RtmpHandler(player_firstNew.this));
                            mPublisher.setRecordEventHandler(new RecordHandler(player_firstNew.this));
                            goCoderCameraView.startCamera();
                            mPublisher.setVideoOutputResolution(400, 200, getResources().getConfiguration().orientation);


                            mPublisher.startPublish("rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/live/" + b.userId + liveId);

                            goCoderCameraView.setVisibility(View.VISIBLE);


                            //player.startThumbCamera1(connId);
                            dialog.dismiss();
                        }
                    });


                    deny.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dp.setVisibility(View.VISIBLE);

                            final bean b = (bean) player.getApplicationContext();

                            final Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(b.BASE_URL)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            final AllAPIs cr = retrofit.create(AllAPIs.class);

                            Call<acceptRejectBean> call1 = cr.acceptReject(connId, liveId, "1" , "");
                            call1.enqueue(new Callback<acceptRejectBean>() {
                                @Override
                                public void onResponse(Call<acceptRejectBean> call, Response<acceptRejectBean> response) {

                                    try {




                                        //cameraLayout1.setVisibility(View.VISIBLE);

/*
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
                            goCoderBroadcaster.startBroadcast(goCoderBroadcastConfig, player_firstNew.this);
                            //}

*/



                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    dialog.dismiss();

                                    dp.setVisibility(View.GONE);
                                }

                                @Override
                                public void onFailure(Call<acceptRejectBean> call, Throwable t) {
                                    dp.setVisibility(View.GONE);
                                    t.printStackTrace();
                                }
                            });


                        }
                    });







                    //displayFirebaseRegId();

                }/* else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }*/
            }
        };




        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String mess = comment.getText().toString();

                if (mess.length() > 0) {
                    progress.setVisibility(View.VISIBLE);

                    final bean b = (bean) getActivity().getApplicationContext();

                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);


                    Call<liveCommentBean> call = cr.commentLive(b.userId, liveId, mess, "basic");

                    call.enqueue(new Callback<liveCommentBean>() {
                        @Override
                        public void onResponse(Call<liveCommentBean> call, retrofit2.Response<liveCommentBean> response) {


                            if (Objects.equals(response.body().getMessage(), "Video Comment Success")) {
                                comment.setText("");
                            }

                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<liveCommentBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                            Log.d("Video upload find ", t.toString());
                        }
                    });
                }


            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (comment.getVisibility() == View.GONE) {
                    comment.setVisibility(View.VISIBLE);
                    send.setVisibility(View.VISIBLE);
                    //emoji.setVisibility(View.VISIBLE);
                } else {
                    comment.setVisibility(View.GONE);
                    send.setVisibility(View.GONE);
                    //emoji.setVisibility(View.GONE);
                }

            }
        });

        gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(player);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.gift_popup);
                dialog.show();


                RecyclerView giftGrid = dialog.findViewById(R.id.grid);
                ProgressBar bar = dialog.findViewById(R.id.progressBar8);
                ImageView dialogClose = dialog.findViewById(R.id.close);


                dialogClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                GridLayoutManager giftManager = new GridLayoutManager(player , 2);

                GiftAdapter giftAdapter = new GiftAdapter(player , bar , dialog);

                giftGrid.setLayoutManager(giftManager);
                giftGrid.setAdapter(giftAdapter);



            }
        });


        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //broadcaster.switchCamera();
            }
        });

        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coun = 0;

                startProjection();

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


        timeLineFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progress.setVisibility(View.VISIBLE);

                final bean b = (bean) player.getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                retrofit2.Call<followBean> call = cr.follow(b.userId , timelineId);

                call.enqueue(new retrofit2.Callback<followBean>() {
                    @Override
                    public void onResponse(retrofit2.Call<followBean> call, retrofit2.Response<followBean> response) {

                        if (response.body().getStatus().equals("1"))
                        {
                            Toast.makeText(player, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            timeLineFollow.setVisibility(View.GONE);
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


        return view;
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
        progress.setVisibility(View.VISIBLE);

        final bean b = (bean) getContext().getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);

        Call<acceptRejectBean> call1 = cr.acceptReject(connId, b.userId + liveId, "2" , "");
        call1.enqueue(new Callback<acceptRejectBean>() {
            @Override
            public void onResponse(Call<acceptRejectBean> call, Response<acceptRejectBean> response) {

                try {




/*                    accept1.setVisibility(View.GONE);
                    reject1.setVisibility(View.GONE);
                    reject3.setVisibility(View.VISIBLE);*/
                } catch (Exception e) {
                    e.printStackTrace();
                }


                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<acceptRejectBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });

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
        Toast.makeText(b, keey, Toast.LENGTH_SHORT).show();

        Call<getUpdatedBean> call = cr.getUpdatedData(b.userId, vid, keey);


        call.enqueue(new Callback<getUpdatedBean>() {
            @Override
            public void onResponse(Call<getUpdatedBean> call, Response<getUpdatedBean> response) {

                try {

                    timelineId = response.body().getData().getTimelineId();

                    commentsAdapter.setGridData(response.body().getData().getComments());
                    viewsAdapter.setGridData(response.body().getData().getViews());

                    int count1 = Integer.parseInt(response.body().getData().getLikesCount());

                    likeCount.setText(String.valueOf(count1));

                    totalBeans.setText(response.body().getData().getBeans2() + " Beans");


                    liveUsers.setText(response.body().getData().getViewsCount());

                    timelineName.setText(response.body().getData().getTimelineName());

                    image = response.body().getData().getTimelineProfileImage();

                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();

                    ImageLoader loader = ImageLoader.getInstance();

                    loader.displayImage(response.body().getData().getTimelineProfileImage(), timelineProfile, options);


                    liveUsers.setText(response.body().getData().getViewsCount());


                    if (response.body().getData().getFollow().equals("true"))
                    {

                        timeLineFollow.setVisibility(View.GONE);

                    }
                    else
                    {
                        timeLineFollow.setVisibility(View.VISIBLE);
                    }


//                    level.setText(response.body().getData().getLevel());


//                    viewCount.setText(response.body().getData().getViewsCount());

//                    username.setText(response.body().getData().getTimelineName());

                    /*if (response.body().getData().getGift().size() > 0) {
                        try {

                            giftName = response.body().getData().getGift().get(0).getGiftId();

                            showGift(Integer.parseInt(response.body().getData().getGift().get(0).getGiftId()), response.body().getData().getGift().get(0).getGiftName());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }*/


                    /*if (count1 > count) {
                        for (int i = 0; i < count1 - count; i++)

                            bubbleView.startAnimation(bubbleView.getWidth(), bubbleView.getHeight());

                        likeCount.setText(response.body().getData().getLikesCount());

                        count = count1;
                    }*/


                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(commentReceiver,
                            new IntentFilter("commentData"));

                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(likeReceiver,
                            new IntentFilter("like"));

                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(viewReceiver,
                            new IntentFilter("view"));

                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(giftReceiver,
                            new IntentFilter("gift"));

                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(endReceiver,
                            new IntentFilter("live_end"));
                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(requestReceiver,
                            new IntentFilter("request"));

                    /*LocalBroadcastManager.getInstance(getContext()).registerReceiver(viewReceiver,
                            new IntentFilter("view"));

                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(likeReceiver,
                            new IntentFilter("like"));

                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(giftReceiver,
                            new IntentFilter("gift"));
                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(statusReceiver,
                            new IntentFilter("status"));
*/
                } catch (Exception e) {
                    // e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<getUpdatedBean> call, Throwable t) {

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




/*    public void showGift(final int pos, String title) {

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


    }*/

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


                    Call<feedBackBean> call = cr.report(b.userId, com, timelineId);

                    call.enqueue(new Callback<feedBackBean>() {
                        @Override
                        public void onResponse(Call<feedBackBean> call, Response<feedBackBean> response) {


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
                        public void onFailure(Call<feedBackBean> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                } else {
                    Toast.makeText(getContext(), "Please Enter a Comment", Toast.LENGTH_SHORT).show();
                }

            }
        });


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


        public void addComment(Comment item) {
            list.add(0, item);
            notifyItemInserted(0);
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.chat_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

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


            final bean b = (bean) context.getApplicationContext();


            String type = item.getType().replace("\"", "");


            if (type.equals("basic")) {

                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();

                ImageLoader loader = ImageLoader.getInstance();

                String im = item.getUserImage().replace("\"", "");

                loader.displayImage(im, holder.index, options);


                String com = item.getComment().replace("\"", "");

                String us = item.getUserName().replace("\"", "");

                holder.name.setText(Html.fromHtml("<font color=\"#cdcdcd\">" + us + ":</font> " + com));



                holder.container.setBackground(context.getResources().getDrawable(R.drawable.gray_round2));

                holder.index.setVisibility(View.VISIBLE);

                holder.index.setVisibility(View.GONE);
                if (Objects.equals(item.getFriendStatus().getFollow(), "true")) {
                    holder.add.setVisibility(View.GONE);
                } else {
                    holder.add.setVisibility(View.VISIBLE);
                }

                if (Objects.equals(uid, b.userId)) {
                    holder.add.setVisibility(View.GONE);
                } else {
                    holder.add.setVisibility(View.VISIBLE);
                }

            } else if (item.getType().equals("follow")){

                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();

                ImageLoader loader = ImageLoader.getInstance();

                String im = item.getUserImage().replace("\"", "");

                loader.displayImage(im, holder.index, options);


                String us = item.getUserName().replace("\"", "");

                holder.name.setText("YL: " + us + " became a fan. Won't miss the next live");

                holder.add.setVisibility(View.GONE);

                holder.container.setBackground(context.getResources().getDrawable(R.drawable.red_round2));

                holder.index.setVisibility(View.GONE);
                if (Objects.equals(item.getFriendStatus().getFollow(), "true")) {
                    holder.add.setVisibility(View.GONE);
                } else {
                    holder.add.setVisibility(View.VISIBLE);
                }


            }else if (item.getType().equals("gift"))
            {

                String us = item.getUserId().replace("\"", "");
                holder.name.setText(us + " has sent a ");
                holder.add.setVisibility(View.GONE);

                holder.container.setBackground(context.getResources().getDrawable(R.drawable.blue_round2));

                holder.index.setVisibility(View.GONE);
            }


            //holder.user.setText(us);

            holder.index.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*Intent intent = new Intent(context, TimelineProfile.class);
                    intent.putExtra("userId", uid);
                    startActivity(intent);*/

                }
            });




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

                            if (response.body().getStatus().equals("1"))
                            {
                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                holder.add.setVisibility(View.GONE);
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


        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            CircleImageView index;
            ImageButton add;
            LinearLayout container;


            public ViewHolder(View itemView) {
                super(itemView);

                index = (CircleImageView) itemView.findViewById(R.id.index);
                name = (TextView) itemView.findViewById(R.id.name);
                container = itemView.findViewById(R.id.container);
                add = (ImageButton) itemView.findViewById(R.id.add);

            }
        }
    }


   /* public void schedule(String liveId) {
        final bean b = (bean) getActivity().getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        SharedPreferences fcmPref = getActivity().getSharedPreferences("fcm", Context.MODE_PRIVATE);

        String keey = fcmPref.getString("token", "");

        Log.d("keeey", keey);

        Call<getUpdatedBean> call = cr.getPlayerUpdatedData(b.userId, liveId, keey);


        call.enqueue(new Callback<getUpdatedBean>() {
            @Override
            public void onResponse(Call<getUpdatedBean> call, retrofit2.Response<getUpdatedBean> response) {

                try {

                    timelineId = response.body().getData().getTimelineId();

                    commentsAdapter.setGridData(response.body().getData().getComments());
                    viewsAdapter.setGridData(response.body().getData().getViews());

                    int count1 = Integer.parseInt(response.body().getData().getLikesCount());

                    likeCount.setText(String.valueOf(count1));

                    totalBeans.setText(response.body().getData().getBeans2() + " Beans");


                    liveUsers.setText(response.body().getData().getViewsCount());

                    timelineName.setText(response.body().getData().getTimelineName());

                    image = response.body().getData().getTimelineProfileImage();

                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();

                    ImageLoader loader = ImageLoader.getInstance();

                    loader.displayImage(response.body().getData().getTimelineProfileImage(), timelineProfile, options);


                    liveUsers.setText(response.body().getData().getViewsCount());


                    if (response.body().getData().getFollow().equals("true"))
                    {

                        timeLineFollow.setVisibility(View.GONE);

                    }
                    else
                    {
                        timeLineFollow.setVisibility(View.VISIBLE);
                    }


//                    level.setText(response.body().getData().getLevel());


//                    viewCount.setText(response.body().getData().getViewsCount());

//                    username.setText(response.body().getData().getTimelineName());

                    *//*if (response.body().getData().getGift().size() > 0) {
                        try {

                            giftName = response.body().getData().getGift().get(0).getGiftId();

                            showGift(Integer.parseInt(response.body().getData().getGift().get(0).getGiftId()), response.body().getData().getGift().get(0).getGiftName());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }*//*


                    *//*if (count1 > count) {
                        for (int i = 0; i < count1 - count; i++)

                            bubbleView.startAnimation(bubbleView.getWidth(), bubbleView.getHeight());

                        likeCount.setText(response.body().getData().getLikesCount());

                        count = count1;
                    }*//*


                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(commentReceiver,
                            new IntentFilter("commentData"));

                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(likeReceiver,
                            new IntentFilter("like"));

                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(viewReceiver,
                            new IntentFilter("view"));

                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(giftReceiver,
                            new IntentFilter("gift"));

                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(endReceiver,
                            new IntentFilter("live_end"));
                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(requestReceiver,
                            new IntentFilter("request"));

                    *//*LocalBroadcastManager.getInstance(getContext()).registerReceiver(viewReceiver,
                            new IntentFilter("view"));

                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(likeReceiver,
                            new IntentFilter("like"));

                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(giftReceiver,
                            new IntentFilter("gift"));
                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(statusReceiver,
                            new IntentFilter("status"));
*//*
                } catch (Exception e) {
                    // e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<getUpdatedBean> call, Throwable t) {

                // Log.d("asdasd", t.toString());

            }
        });


    }

*/
    private void startProjection() {
        startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
    }


    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(commentReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(likeReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(viewReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(giftReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(endReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(requestReceiver);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            sMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);

            if (sMediaProjection != null) {
                File externalFilesDir = player.getExternalFilesDir(null);
                if (externalFilesDir != null) {
                    STORE_DIRECTORY = externalFilesDir.getAbsolutePath() + "/youthive/";
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
                mDisplay = player.getWindowManager().getDefaultDisplay();

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

    private int mDensity;

    private ImageReader mImageReader;

    private VirtualDisplay mVirtualDisplay;
    private static final String SCREENCAP_NAME = "screencap";

    private static final int VIRTUAL_DISPLAY_FLAGS = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;

    private Handler mHandler;

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


                if (coun == 0) {
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


                        final String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "Title", null);


                        final Dialog dialog = new Dialog(player);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.setContentView(R.layout.screenshot_dialog);
                        dialog.setCancelable(false);
                        dialog.show();

                        Log.e(TAG, "1");

                        RoundedImageView imeg = dialog.findViewById(R.id.imageView5);
                        ImageButton closeDialog = dialog.findViewById(R.id.imageButton8);
                        Button share = dialog.findViewById(R.id.button3);

                        Log.e(TAG, "2");

                        imeg.setImageURI(Uri.parse(path));

                        Log.e(TAG, "3");

                        closeDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                dialog.dismiss();


                            }
                        });

                        Log.e(TAG, "4");

                        final Bitmap finalBitmap = bitmap;

                        Log.e(TAG, "5");
                        share.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent i = new Intent(Intent.ACTION_SEND);

                                i.setType("image/*");
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();


                                //byte[] bytes = stream.toByteArray();


                                i.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
                                try {
                                    startActivity(Intent.createChooser(i, "Share Screenshot..."));
                                    dialog.dismiss();
                                } catch (android.content.ActivityNotFoundException ex) {

                                    ex.printStackTrace();
                                }


                            }
                        });


                        Log.e(TAG, "6");


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


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    class ViewsAdapter extends RecyclerView.Adapter<ViewsAdapter.ViewHolder> {

        Context context;
        List<com.yl.youthlive.getIpdatedPOJO.View> list = new ArrayList<>();

        public ViewsAdapter(Context context, List<com.yl.youthlive.getIpdatedPOJO.View> list) {
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
            liveUsers.setText(String.valueOf(list.size() - 1));
        }

        public void removeView(com.yl.youthlive.getIpdatedPOJO.View item) {
            list.remove(item);
            notifyDataSetChanged();
            liveUsers.setText(String.valueOf(list.size()));
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.viewers_model, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

    @Override
    public void onResume() {
        super.onResume();

        liveId = getArguments().getString("liveId");
        schedule(liveId);

    }


    class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ViewHolder>
    {

        Context context;
        ProgressBar progressBar;
        Dialog dialog;
        Integer gifs[] = new Integer[]
                {
                        R.drawable.gif1,
                        R.drawable.gif2,
                        R.drawable.gif3,
                        R.drawable.gif4,
                        R.drawable.gif5,
                        R.drawable.gif6,
                        R.drawable.gif7,
                        R.drawable.gif8,
                        R.drawable.gif9,
                        R.drawable.gif10,
                        R.drawable.gif11,
                        R.drawable.gif12,
                        R.drawable.gif13,
                        R.drawable.gif14
                };

        public GiftAdapter(Context context , ProgressBar progress , Dialog dialog)
        {
            this.context = context;
            this.progressBar = progress;
            this.dialog = dialog;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.gift_model , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


            Glide.with(context).load(gifs[position]).into(holder.image);

            holder.send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    progressBar.setVisibility(View.VISIBLE);

                    final bean b = (bean) getContext().getApplicationContext();

                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);


                    retrofit2.Call<sendGiftBean> call = cr.sendGift(b.userId, liveId, timelineId, String.valueOf(position + 1), "1", "1");




                    call.enqueue(new retrofit2.Callback<sendGiftBean>() {
                        @Override
                        public void onResponse(retrofit2.Call<sendGiftBean> call, retrofit2.Response<sendGiftBean> response) {

                            //Log.d("Asdasdsa", response.body().getMessage());


                            try {
                                if (Objects.equals(response.body().getStatus(), "1")) {
                                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                                else
                                {
                                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                Toast.makeText(getContext(), "Some Error Occurred, Please try again", Toast.LENGTH_SHORT).show();

                                e.printStackTrace();
                            }


                            progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(retrofit2.Call<sendGiftBean> call, Throwable t) {
                            Log.d("asdasd", t.toString());
                            progressBar.setVisibility(View.GONE);
                        }
                    });




                }
            });


        }

        @Override
        public int getItemCount() {
            return 14;
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {

            ImageView image;
            Button send;

            public ViewHolder(View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.imageView12);
                send = itemView.findViewById(R.id.button8);

            }
        }
    }


}
