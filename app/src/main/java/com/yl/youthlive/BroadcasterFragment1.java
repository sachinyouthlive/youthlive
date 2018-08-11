package com.yl.youthlive;

import android.app.Activity;
import android.app.Dialog;
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
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yasic.bubbleview.BubbleView;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.acceptRejectPOJO.acceptRejectBean;
import com.yl.youthlive.dummyPOJO.Datum;
import com.yl.youthlive.dummyPOJO.dummyBean;
import com.yl.youthlive.followPOJO.followBean;
import com.yl.youthlive.getIpdatedPOJO.Comment;
import com.yl.youthlive.getIpdatedPOJO.getUpdatedBean;
import com.yl.youthlive.goLivePOJO.goLiveBean;
import com.yl.youthlive.liveCommentPOJO.liveCommentBean;
import com.yl.youthlive.requestConnectionPOJO.requestConnectionBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

//import com.streamaxia.player.StreamaxiaPlayer;

public class BroadcasterFragment1 extends Fragment {

    TextView newMessage;

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    ImageButton emoji, message, send, flash, camera, crop;

    EmojiconEditText comment;

    RecyclerView commentGrid;
    LinearLayoutManager commentsManager;
    CommentsAdapter commentsAdapter;
    List<Comment> commentList;

    //BroadcastReceiver commentReceiver;
    //BroadcastReceiver likeReceiver;
    //BroadcastReceiver viewReceiver;
    //BroadcastReceiver giftReceiver;
    //BroadcastReceiver statusReceiver;
    //BroadcastReceiver playerStatusReceiver;
    //BroadcastReceiver connectionReceiver;
    //BroadcastReceiver requestReceiver;

    BroadcastReceiver singleReceiver;

    View rootView;
    private EmojIconActions emojIcon;


    ProgressBar progress;
    String liveId;

    VideoBroadcaster broadcaster;

    private static final int REQUEST_CODE = 100;
    private static final int REQUEST_CODE2 = 102;

    private MediaProjectionManager mProjectionManager;
    private MediaProjectionManager mProjectionManager2;
    //SimpleExoPlayerView thumb;
    private static MediaProjection sMediaProjection;
    private static MediaProjection sMediaProjection2;
    private Display mDisplay;
    private Display mDisplay2;

    String TAG = "BroadcasterFragment1";

    private static String STORE_DIRECTORY;

    int coun = 0;
    int coun2 = 0;

    TextView likeCount;

    private BubbleView bubbleView;

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

    RelativeLayout playerFrame1;

    TextView reject1;

    String connId;

    String thumbPic1;

    boolean isConnection = false;

    View giftLayout;
    ImageView giftImage;
    TextView giftText;
    CircleImageView giftProfile;
    TextView giftUser;


    ImageButton connection;


    TextView ylId;

    String userType;

    SharedPreferences pref;

    List<Datum> dummyList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.broadcaster_fragment_layout1, container, false);

        broadcaster = (VideoBroadcaster) getActivity();

        dummyList = new ArrayList<>();

        pref = broadcaster.getSharedPreferences("pref", Context.MODE_PRIVATE);

        mProjectionManager = (MediaProjectionManager) broadcaster.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        mProjectionManager2 = (MediaProjectionManager) broadcaster.getSystemService(Context.MEDIA_PROJECTION_SERVICE);


        bubbleView = (BubbleView) view.findViewById(R.id.bubble);
        playerFrame1 = view.findViewById(R.id.view3);

        giftImage = view.findViewById(R.id.imageView18);
        giftText = view.findViewById(R.id.textView50);
        giftProfile = view.findViewById(R.id.circleImageView);
        giftUser = view.findViewById(R.id.textView49);

        giftLayout = view.findViewById(R.id.gift_layout);

        reject1 = view.findViewById(R.id.reject1);
        ylId = view.findViewById(R.id.ylid);

        reject1.setZ(21);

        List<Drawable> drawableList = new ArrayList<>();
        //drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_indigo_900_24dp));
        //drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_deep_purple_900_24dp));
        //drawableList.add(getResources().getDrawable(R.drawable.g52));
        //drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_blue_900_24dp));
        //drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_deep_purple_900_24dp));
        //drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_light_blue_900_24dp));
        //drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_lime_a200_24dp));
        //drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_pink_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_red_900_24dp));
        bubbleView.setDrawableList(drawableList);

        commentList = new ArrayList<>();
        viewsList = new ArrayList<>();

        newMessage = view.findViewById(R.id.textView4);
        likeCount = view.findViewById(R.id.textView5);

        connection = view.findViewById(R.id.connect);

        emoji = view.findViewById(R.id.imageButton4);
        message = view.findViewById(R.id.imageButton3);
        send = view.findViewById(R.id.imageButton5);
        flash = view.findViewById(R.id.imageButton6);
        camera = view.findViewById(R.id.imageButton2);
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


        final bean b = (bean) getActivity().getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        final Call<dummyBean> call = cr.getDummy();

        call.enqueue(new Callback<dummyBean>() {
            @Override
            public void onResponse(Call<dummyBean> call, Response<dummyBean> response) {

                if (response.body().getStatus().equals("1")) {

                    dummyList = response.body().getData();

                    runDummy();

                }

            }

            @Override
            public void onFailure(Call<dummyBean> call, Throwable t) {

            }
        });

        userType = pref.getString("userType", "");


        if (userType.equals("user")) {
            progress.setVisibility(View.VISIBLE);

            Call<goLiveBean> call3 = cr.goLive(b.userId, b.userId, "");

            call3.enqueue(new Callback<goLiveBean>() {
                @Override
                public void onResponse(Call<goLiveBean> call, retrofit2.Response<goLiveBean> response) {

                    if (Objects.equals(response.body().getStatus(), "1")) {
                        //Toast.makeText(LiveScreen.this, "You are now live", Toast.LENGTH_SHORT).show();
                        liveId = response.body().getData().getLiveId();

                        broadcaster.setLiveId(liveId);

                        Log.d("lliivvee", liveId);
                        Log.d("lliivvee", b.userId);

                        broadcaster.startPublish(liveId);


                        broadcaster.startCountDown();
                        schedule(liveId);
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

        } else {

            coun2 = 0;
            startActivityForResult(mProjectionManager2.createScreenCaptureIntent(), REQUEST_CODE2);

        }




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


        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                bubbleView.startAnimation(bubbleView.getWidth(), bubbleView.getHeight());


            }
        });

        reject1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progress.setVisibility(View.VISIBLE);

                final bean b = (bean) getContext().getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);

                Call<String> call = cr.endConnection(connId);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {


                        progress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        progress.setVisibility(View.GONE);
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


        singleReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (intent.getAction()) {
                    case "commentData": {
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

                        break;
                    }
                    case "like": {
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

                        break;
                    }
                    case "view": {
                        Log.d("data", intent.getStringExtra("data"));

                        String json = intent.getStringExtra("data");

                        Gson gson = new Gson();

                        com.yl.youthlive.getIpdatedPOJO.View item = gson.fromJson(json, com.yl.youthlive.getIpdatedPOJO.View.class);

                        final String uid = item.getUserId().replace("\"", "");

                        final bean b = (bean) getActivity().getApplicationContext();

                        String id = item.getUserId();
                        if (!uid.equals(b.userId)) {
                            viewsAdapter.addView(item);
                            viewersGrid.smoothScrollToPosition(0);
                        }

                        break;
                    }
                    case "gift": {
                        Log.d("data", intent.getStringExtra("data"));

                        String json = intent.getStringExtra("data");

                        Gson gson = new Gson();

                        com.yl.youthlive.getIpdatedPOJO.Gift item = gson.fromJson(json, com.yl.youthlive.getIpdatedPOJO.Gift.class);


                        try {

                            String giftName = item.getGiftName().replace("\"", "");

                            totalBeans.setText(giftName + " Coins");

                            Comment comm = new Comment();

                            comm.setType("gift");
                            comm.setUserId(item.getSenbdId());
                            comm.setComment(item.getGiftId());

                            commentsAdapter.addComment(comm);

                            if (loading) {
                                commentGrid.scrollToPosition(0);
                                loading = true;
                                newMessage.setVisibility(View.GONE);
                            } else {
                                Log.d("lloogg", "new message");

                                newMessage.setVisibility(View.VISIBLE);
                            }

                            //comm.set


                            showGift(item.getGiftId(), item.getGiftName(), item.getIcon(), item.getSenbdId());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                    }
                    case "status": {
                        Log.d("ddata", intent.getStringExtra("data"));

                        String json = intent.getStringExtra("data");

                        try {
                            JSONObject obj = new JSONObject(json);

                            connId = obj.getString("connId");

                            String mode = obj.getString("status");
                            final String uri = obj.getString("uri");


                            if (mode.equals("2")) {


                                Log.d("ddata", uri);

                                broadcaster.startThumbPlayer1(uri, thumbPic1, connId);
                                playerFrame1.setVisibility(View.VISIBLE);
                                isConnection = true;


                            } else {

                                isConnection = false;
                                Toast.makeText(broadcaster, "Your Guest Live request has been rejected", Toast.LENGTH_SHORT).show();


                            }


                        } catch (JSONException e) {
                            Log.d("ddata", e.toString());
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "status_player": {
                        Log.d("uurrii", intent.getStringExtra("data"));

                        String json = intent.getStringExtra("data");

                        try {
                            JSONObject obj = new JSONObject(json);

                            connId = obj.getString("connId");

                            String mode = obj.getString("status");
                            final String uri = obj.getString("uri");


                            if (mode.equals("2")) {


                                Log.d("uurrii", uri);

                                broadcaster.startThumbPlayer1(uri, thumbPic1, connId);
                                playerFrame1.setVisibility(View.VISIBLE);
                                isConnection = true;


                            } else {

                                isConnection = false;
                                Toast.makeText(broadcaster, "Your Guest Live request has been rejected", Toast.LENGTH_SHORT).show();


                            }


                        } catch (JSONException e) {
                            Log.d("uurrii", e.toString());
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "request_player": {
                        Log.d("ddata", intent.getStringExtra("data"));

                        String json = intent.getStringExtra("data");

                        try {
                            JSONObject object = new JSONObject(json);

                            connId = object.getString("conId");

                            final String uid = object.getString("uid");


                            final Dialog dialog = new Dialog(broadcaster);
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


                                    dp.setVisibility(View.VISIBLE);

                                    final bean b = (bean) broadcaster.getApplicationContext();

                                    final Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(b.BASE_URL)
                                            .addConverterFactory(ScalarsConverterFactory.create())
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    final AllAPIs cr = retrofit.create(AllAPIs.class);

                                    Call<acceptRejectBean> call1 = cr.acceptRejectBroadcaster(connId, liveId + uid, "2", uid);
                                    call1.enqueue(new Callback<acceptRejectBean>() {
                                        @Override
                                        public void onResponse(Call<acceptRejectBean> call, Response<acceptRejectBean> response) {

                                            /*try {



                                                isConnection = false;
                                                //cameraLayout1.setVisibility(View.VISIBLE);





                                                reject1.setVisibility(View.GONE);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }*/

                                            reject1.setVisibility(View.VISIBLE);

                                            isConnection = true;


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


                            deny.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dp.setVisibility(View.VISIBLE);

                                    final bean b = (bean) broadcaster.getApplicationContext();

                                    final Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(b.BASE_URL)
                                            .addConverterFactory(ScalarsConverterFactory.create())
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    final AllAPIs cr = retrofit.create(AllAPIs.class);

                                    Call<acceptRejectBean> call1 = cr.acceptRejectBroadcaster(connId, liveId + uid, "1", uid);
                                    call1.enqueue(new Callback<acceptRejectBean>() {
                                        @Override
                                        public void onResponse(Call<acceptRejectBean> call, Response<acceptRejectBean> response) {

                                            try {


                                                isConnection = false;
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


                                                //reject1.setVisibility(View.GONE);

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


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "connection_end": {
                        Log.d("uurrii", intent.getStringExtra("data"));

                        String json = intent.getStringExtra("data");

                        try {
                            JSONObject obj = new JSONObject(json);


                            String conn = obj.getString("connId");
                            String uid = obj.getString("userId");


                            isConnection = false;
                            playerFrame1.setVisibility(View.GONE);
                            broadcaster.endThumbPlayer1();


                        } catch (JSONException e) {
                            Log.d("uurrii", e.toString());
                            e.printStackTrace();
                        }


                        break;
                    }
                    case "exit": {

                        Log.d("data", intent.getStringExtra("data"));

                        String json = intent.getStringExtra("data");

                        Gson gson = new Gson();

                        com.yl.youthlive.getIpdatedPOJO.View item = gson.fromJson(json, com.yl.youthlive.getIpdatedPOJO.View.class);

                        final String uid = item.getUserId().replace("\"", "");

                        final bean b = (bean) getActivity().getApplicationContext();

                        String id = item.getUserId();
                        if (!uid.equals(b.userId)) {
                            viewsAdapter.removeView(item);
                        }

                        break;
                    }


                }

            }
        };

        /*commentReceiver = new BroadcastReceiver() {
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

                }*//* else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }*//*
            }
        };*/

        /*likeReceiver = new BroadcastReceiver() {
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

                }*//* else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }*//*
            }
        };*/

        /*viewReceiver = new BroadcastReceiver() {
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

                    final String uid = item.getUserId().replace("\"", "");

                    final bean b = (bean) getActivity().getApplicationContext();

                    String id = item.getUserId();
                    if (!uid.equals(b.userId)) {
                        viewsAdapter.addView(item);
                    }


                    //displayFirebaseRegId();

                }*//* else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }*//*
            }
        };
*/

        /*giftReceiver = new BroadcastReceiver() {
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

                        String giftName = item.getGiftName().replace("\"", "");

                        totalBeans.setText(giftName + " Coins");

                        Comment comm = new Comment();

                        comm.setType("gift");
                        comm.setUserId(item.getSenbdId());
                        comm.setComment(item.getGiftId());

                        commentsAdapter.addComment(comm);

                        if (loading) {
                            commentGrid.scrollToPosition(0);
                            loading = true;
                            newMessage.setVisibility(View.GONE);
                        } else {
                            Log.d("lloogg", "new message");

                            newMessage.setVisibility(View.VISIBLE);
                        }

                        //comm.set


                        showGift(item.getGiftId(), item.getGiftName(), item.getIcon(), item.getSenbdId());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    //displayFirebaseRegId();

                }*//* else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }*//*
            }
        };*/


        /*statusReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals("status")) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications


                    Log.d("ddata", intent.getStringExtra("data"));

                    String json = intent.getStringExtra("data");

                    try {
                        JSONObject obj = new JSONObject(json);

                        connId = obj.getString("connId");

                        String mode = obj.getString("status");
                        final String uri = obj.getString("uri");


                        if (mode.equals("2")) {


                            Log.d("ddata", uri);

                            broadcaster.startThumbPlayer1(uri, thumbPic1, connId);
                            playerFrame1.setVisibility(View.VISIBLE);
                            isConnection = true;


                        } else {

                            isConnection = false;
                            Toast.makeText(broadcaster, "Your Guest Live request has been rejected", Toast.LENGTH_SHORT).show();


                        }


                    } catch (JSONException e) {
                        Log.d("ddata", e.toString());
                        e.printStackTrace();
                    }


                    //displayFirebaseRegId();
                }*//* else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }*//*
            }
        };*/

        /*playerStatusReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals("status_player")) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications


                    Log.d("uurrii", intent.getStringExtra("data"));

                    String json = intent.getStringExtra("data");

                    try {
                        JSONObject obj = new JSONObject(json);

                        connId = obj.getString("connId");

                        String mode = obj.getString("status");
                        final String uri = obj.getString("uri");


                        if (mode.equals("2")) {


                            Log.d("uurrii", uri);

                            broadcaster.startThumbPlayer1(uri, thumbPic1, connId);
                            playerFrame1.setVisibility(View.VISIBLE);
                            isConnection = true;


                        } else {

                            isConnection = false;
                            Toast.makeText(broadcaster, "Your Guest Live request has been rejected", Toast.LENGTH_SHORT).show();


                        }


                    } catch (JSONException e) {
                        Log.d("uurrii", e.toString());
                        e.printStackTrace();
                    }


                    //displayFirebaseRegId();
                }*//* else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }*//*
            }
        };*/

        /*connectionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals("connection_end")) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications


                    Log.d("uurrii", intent.getStringExtra("data"));

                    String json = intent.getStringExtra("data");

                    try {
                        JSONObject obj = new JSONObject(json);


                        String conn = obj.getString("connId");
                        String uid = obj.getString("userId");


                        isConnection = false;
                        playerFrame1.setVisibility(View.GONE);
                        broadcaster.endThumbPlayer1();


                    } catch (JSONException e) {
                        Log.d("uurrii", e.toString());
                        e.printStackTrace();
                    }


                    //displayFirebaseRegId();
                }*//* else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }*//*
            }
        };*/


        /*requestReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals("request_player")) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications

                    Log.d("ddata", intent.getStringExtra("data"));

                    String json = intent.getStringExtra("data");

                    try {
                        JSONObject object = new JSONObject(json);

                        connId = object.getString("conId");

                        final String uid = object.getString("uid");


                        final Dialog dialog = new Dialog(broadcaster);
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


                                dp.setVisibility(View.VISIBLE);

                                final bean b = (bean) broadcaster.getApplicationContext();

                                final Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(b.BASE_URL)
                                        .addConverterFactory(ScalarsConverterFactory.create())
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                final AllAPIs cr = retrofit.create(AllAPIs.class);

                                Call<acceptRejectBean> call1 = cr.acceptRejectBroadcaster(connId, liveId + uid, "2", uid);
                                call1.enqueue(new Callback<acceptRejectBean>() {
                                    @Override
                                    public void onResponse(Call<acceptRejectBean> call, Response<acceptRejectBean> response) {

                                            *//*try {



                                                isConnection = false;
                                                //cameraLayout1.setVisibility(View.VISIBLE);





                                                reject1.setVisibility(View.GONE);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }*//*

                                        reject1.setVisibility(View.VISIBLE);

                                        isConnection = true;


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


                        deny.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dp.setVisibility(View.VISIBLE);

                                final bean b = (bean) broadcaster.getApplicationContext();

                                final Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(b.BASE_URL)
                                        .addConverterFactory(ScalarsConverterFactory.create())
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                final AllAPIs cr = retrofit.create(AllAPIs.class);

                                Call<acceptRejectBean> call1 = cr.acceptRejectBroadcaster(connId, liveId + uid, "1", uid);
                                call1.enqueue(new Callback<acceptRejectBean>() {
                                    @Override
                                    public void onResponse(Call<acceptRejectBean> call, Response<acceptRejectBean> response) {

                                        try {


                                            isConnection = false;
                                            //cameraLayout1.setVisibility(View.VISIBLE);

*//*
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

*//*


                                            //reject1.setVisibility(View.GONE);

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


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    //displayFirebaseRegId();

                }*//* else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }*//*
            }
        };*/


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

        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                broadcaster.switchTorch();


            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                broadcaster.switchCamera();
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
                try {
                    Looper.prepare();
                    bubbleHandler = new Handler();
                    Looper.loop();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();


        new Thread() {
            @Override
            public void run() {
                try {
                    Looper.prepare();
                    mHandler = new Handler();
                    Looper.loop();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();


        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mHandler2 = new Handler();
                Looper.loop();
            }
        }.start();


        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                broadcaster.endLive(liveId);


            }
        });


        connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(broadcaster);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.live_users_list_popup);
                dialog.show();

                RecyclerView dialogGrid = dialog.findViewById(R.id.grid);

                ProgressBar dialogBar = dialog.findViewById(R.id.progressBar12);

                ImageButton dialogClose = dialog.findViewById(R.id.imageButton9);

                GridLayoutManager dialogManager = new GridLayoutManager(broadcaster, 1);

                List<com.yl.youthlive.getIpdatedPOJO.View> l2 = viewsAdapter.getList();

                DialogAdapter dialogAdapter = new DialogAdapter(broadcaster, l2, dialogBar, dialog);

                dialogGrid.setAdapter(dialogAdapter);
                dialogGrid.setLayoutManager(dialogManager);


                dialogClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });


        return view;
    }


    class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.ViewHolder> {

        Context context;
        List<com.yl.youthlive.getIpdatedPOJO.View> list = new ArrayList<>();
        ProgressBar dialogProgress;
        Dialog dialog;


        public DialogAdapter(Context context, List<com.yl.youthlive.getIpdatedPOJO.View> list, ProgressBar dialogProgress, Dialog dialog) {
            this.context = context;
            this.list = list;
            this.dialogProgress = dialogProgress;
            this.dialog = dialog;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.guest_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final com.yl.youthlive.getIpdatedPOJO.View item = list.get(position);

            final String uid = item.getUserId().replace("\"", "");
            final String imm = item.getUserImage().replace("\"", "");
            final String un = item.getUserName().replace("\"", "");

            final bean b = (bean) context.getApplicationContext();

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();

            ImageLoader loader = ImageLoader.getInstance();

            loader.displayImage(b.BASE_URL + imm, holder.image, options);

            holder.name.setText(un);


            holder.join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (!isConnection) {

                        String t = item.getType().replace("\"", "");

                        if (t.equals("d")) {

                            Toast.makeText(context, "Your request has been sent to the user", Toast.LENGTH_SHORT).show();

                            new CountDownTimer(3000, 1000) {

                                @Override
                                public void onTick(long millisUntilFinished) {


                                }

                                @Override
                                public void onFinish() {

                                    isConnection = false;
                                    Toast.makeText(context, "Your Guest Live request has been rejected", Toast.LENGTH_SHORT).show();

                                }
                            }.start();


                            dialog.dismiss();


                        } else {
                            dialogProgress.setVisibility(View.VISIBLE);

                            final bean b = (bean) context.getApplicationContext();

                            final Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(b.BASE_URL)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            final AllAPIs cr = retrofit.create(AllAPIs.class);


                            Call<requestConnectionBean> call = cr.requestConnection(liveId, b.userId, uid);

                            call.enqueue(new Callback<requestConnectionBean>() {
                                @Override
                                public void onResponse(Call<requestConnectionBean> call, retrofit2.Response<requestConnectionBean> response) {
                                    Toast.makeText(context, "Your request has been sent to the user", Toast.LENGTH_SHORT).show();
                                    String im = item.getUserImage().replace("\"", "");
                                    thumbPic1 = im;

                                    isConnection = true;

                                    dialog.dismiss();


                                    dialogProgress.setVisibility(View.GONE);
                                }

                                @Override
                                public void onFailure(Call<requestConnectionBean> call, Throwable t) {
                                    thumbPic1 = item.getUserImage();
                                    isConnection = false;
                                    dialogProgress.setVisibility(View.GONE);
                                    Log.d("asdasdasdas", t.toString());
                                }
                            });
                        }


                    } else {
                        Toast.makeText(context, "You don't have any more room left", Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            CircleImageView image;
            TextView name;
            Button join;

            public ViewHolder(View itemView) {
                super(itemView);


                image = itemView.findViewById(R.id.view8);
                name = itemView.findViewById(R.id.textView36);
                join = itemView.findViewById(R.id.button9);


            }
        }
    }


    public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {


        List<Comment> list = new ArrayList<>();
        Context context;

        Integer gifts[] = new Integer[]
                {
                        R.drawable.g52,
                        R.drawable.g20,
                        R.drawable.g32,
                        R.drawable.g1500,
                        R.drawable.g72,
                        R.drawable.g112,
                        R.drawable.g152,
                        R.drawable.g172,
                        R.drawable.g180,
                        R.drawable.g192,
                        R.drawable.g212,
                        R.drawable.g240,
                        R.drawable.g252,
                        R.drawable.g280,
                        R.drawable.g300,
                        R.drawable.g312,
                        R.drawable.g352,
                        R.drawable.g380,
                        R.drawable.g452,
                        R.drawable.g500,
                        R.drawable.g612,
                        R.drawable.g700,
                        R.drawable.g800,
                        R.drawable.g900,
                        R.drawable.g1000,
                        R.drawable.g1100,
                        R.drawable.g1200
                };

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

                if (Objects.equals(uid, b.userId)) {
                    holder.add.setVisibility(View.GONE);
                } else {
                    holder.add.setVisibility(View.VISIBLE);
                }

                holder.container.setBackground(context.getResources().getDrawable(R.drawable.gray_round2));

                holder.index.setVisibility(View.VISIBLE);

                if (Objects.equals(item.getFriendStatus().getFollow(), "true")) {
                    holder.add.setBackgroundResource(R.drawable.tick);
                } else {
                    holder.add.setBackgroundResource(R.drawable.plus_red);
                }

                holder.add.setVisibility(View.GONE);

            } else if (type.equals("follow")) {

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
                    holder.add.setBackgroundResource(R.drawable.tick);
                } else {
                    holder.add.setBackgroundResource(R.drawable.plus_red);
                }


                holder.add.setVisibility(View.GONE);


            } else if (type.equals("gift")) {

                String us = item.getUserId().replace("\"", "");
                String gid = item.getComment().replace("\"", "");
                holder.name.setText(us + " has sent a  ");

                Drawable drawable = context.getResources().getDrawable(gifts[Integer.parseInt(gid) - 1]);

                drawable.setBounds(0, 0, 40, 40);

                int selectionCursor = holder.name.getSelectionStart();
                //holder.name.getText().insert(selectionCursor, ".");
                selectionCursor = holder.name.getText().length();

                SpannableStringBuilder builder = new SpannableStringBuilder(holder.name.getText());
                builder.setSpan(new ImageSpan(drawable), selectionCursor - ".".length(), selectionCursor, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.name.setText(builder);
                //holder.name.setSelection(selectionCursor);

                holder.add.setVisibility(View.GONE);

                holder.container.setBackground(context.getResources().getDrawable(R.drawable.blue_round2));

                holder.index.setVisibility(View.GONE);
            }


            //holder.user.setText(us);

            /*holder.index.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, TimelineProfile.class);
                    intent.putExtra("userId", uid);
                    startActivity(intent);

                }
            });*/


            holder.index.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!uid.equals(b.userId)) {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.connect_dialog);
                        dialog.setCancelable(true);
                        dialog.show();


                        CircleImageView image = (CircleImageView) dialog.findViewById(R.id.image);
                        TextView name = (TextView) dialog.findViewById(R.id.name);
                        Button follo = (Button) dialog.findViewById(R.id.follow);
                        Button connect = (Button) dialog.findViewById(R.id.connect);
                        final ProgressBar bar = dialog.findViewById(R.id.progressBar10);


                        ImageLoader loader1 = ImageLoader.getInstance();

                        loader1.displayImage(b.userImage, image);

                        name.setText(b.userName);

                        follo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                bar.setVisibility(View.VISIBLE);

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

                                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                        bar.setVisibility(View.GONE);

                                        dialog.dismiss();

                                    }

                                    @Override
                                    public void onFailure(retrofit2.Call<followBean> call, Throwable t) {

                                        bar.setVisibility(View.GONE);

                                    }
                                });

                            }
                        });


                        connect.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                if (!isConnection) {
                                    bar.setVisibility(View.VISIBLE);

                                    final bean b = (bean) context.getApplicationContext();

                                    final Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(b.BASE_URL)
                                            .addConverterFactory(ScalarsConverterFactory.create())
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    final AllAPIs cr = retrofit.create(AllAPIs.class);


                                    Call<requestConnectionBean> call = cr.requestConnection(liveId, b.userId, uid);

                                    call.enqueue(new Callback<requestConnectionBean>() {
                                        @Override
                                        public void onResponse(Call<requestConnectionBean> call, retrofit2.Response<requestConnectionBean> response) {

                                            String im = item.getUserImage().replace("\"", "");
                                            thumbPic1 = im;


                                            isConnection = true;
                                            dialog.dismiss();


                                            bar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onFailure(Call<requestConnectionBean> call, Throwable t) {
                                            thumbPic1 = item.getUserImage();
                                            isConnection = false;
                                            bar.setVisibility(View.GONE);
                                            Log.d("asdasdasdas", t.toString());
                                        }
                                    });


                                } else {
                                    Toast.makeText(context, "You don't have any more room left", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }


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


    public void schedule(String liveId) {
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

        Call<getUpdatedBean> call = cr.getUpdatedData(b.userId, liveId, keey);

        Log.d("asdasd", b.userId);

        call.enqueue(new Callback<getUpdatedBean>() {
            @Override
            public void onResponse(Call<getUpdatedBean> call, retrofit2.Response<getUpdatedBean> response) {

                try {


                    //bubbleChecker.run();


                    ylId.setText(response.body().getData().getYouthliveId());


                    commentsAdapter.setGridData(response.body().getData().getComments());


                    for (int i = 0; i < response.body().getData().getViews().size(); i++) {

                        final String uid = response.body().getData().getViews().get(i).getUserId().replace("\"", "");

                        if (!uid.equals(b.userId)) {
                            viewsAdapter.addView(response.body().getData().getViews().get(i));
                        }


                    }


                    int count1 = Integer.parseInt(response.body().getData().getLikesCount());

                    likeCount.setText(String.valueOf(count1));

                    totalBeans.setText(response.body().getData().getBeans() + " Coins");


                    liveUsers.setText(response.body().getData().getViewsCount());

                    timelineName.setText(response.body().getData().getTimelineName());


                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();

                    ImageLoader loader = ImageLoader.getInstance();

                    loader.displayImage(response.body().getData().getTimelineProfileImage(), timelineProfile, options);


                    liveUsers.setText(response.body().getData().getViewsCount());


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


                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(singleReceiver,
                            new IntentFilter("commentData"));

                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(singleReceiver,
                            new IntentFilter("like"));

                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(singleReceiver,
                            new IntentFilter("view"));

                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(singleReceiver,
                            new IntentFilter("gift"));
                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(singleReceiver,
                            new IntentFilter("status"));
                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(singleReceiver,
                            new IntentFilter("connection_end"));
                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(singleReceiver,
                            new IntentFilter("request_player"));
                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(singleReceiver,
                            new IntentFilter("status_player"));
                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(singleReceiver,
                            new IntentFilter("exit"));


/*

                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(commentReceiver,
                            new IntentFilter("commentData"));

                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(likeReceiver,
                            new IntentFilter("like"));

                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(viewReceiver,
                            new IntentFilter("view"));

                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(giftReceiver,
                            new IntentFilter("gift"));
                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(statusReceiver,
                            new IntentFilter("status"));
                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(connectionReceiver,
                            new IntentFilter("connection_end"));
                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(requestReceiver,
                            new IntentFilter("request_player"));
                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(playerStatusReceiver,
                            new IntentFilter("status_player"));

*/


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("asdasd", e.toString());
                }


            }

            @Override
            public void onFailure(Call<getUpdatedBean> call, Throwable t) {

                Log.d("asdasd", t.toString());

            }
        });


    }


    private void startProjection() {
        startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
    }


    @Override
    public void onStop() {
        super.onStop();

    }


    @Override
    public void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {


            sMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);

            if (sMediaProjection != null) {
                File externalFilesDir = broadcaster.getExternalFilesDir(null);
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
                mDisplay = broadcaster.getWindowManager().getDefaultDisplay();

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


        } else if (requestCode == REQUEST_CODE2) {

            if (resultCode == Activity.RESULT_OK) {


                progress.setVisibility(View.VISIBLE);
                final bean b = (bean) getActivity().getApplicationContext();

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
                            liveId = response.body().getData().getLiveId();

                            broadcaster.setLiveId(liveId);

                            Log.d("lliivvee", liveId);
                            Log.d("lliivvee", b.userId);

                            broadcaster.startPublish(liveId);


                            broadcaster.startCountDown();
                            schedule(liveId);


                            repeatHandler = new Handler();

                            mStatusChecker = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        repeat(resultCode, data);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        // 100% guarantee that this always happens, even if
                                        // your update method throws an exception
                                        mHandler.postDelayed(mStatusChecker, 60000);
                                    }
                                }
                            };


                            mStatusChecker.run();


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


            } else {
                Toast.makeText(broadcaster, "This permission is required to go live", Toast.LENGTH_SHORT).show();
                broadcaster.finish();
            }


        }
    }


    Handler repeatHandler;


    Runnable mStatusChecker;


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (repeatHandler != null) {
            repeatHandler.removeCallbacks(mStatusChecker);
        }
/*
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(commentReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(likeReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(viewReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(giftReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(statusReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(playerStatusReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(connectionReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(requestReceiver);*/
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(singleReceiver);

    }

    public void repeat(int resultCode, Intent data) {

        coun2 = 0;

        sMediaProjection2 = mProjectionManager2.getMediaProjection(resultCode, data);

        if (sMediaProjection2 != null) {
            File externalFilesDir = broadcaster.getExternalFilesDir(null);
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


            try {
                // display metrics
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                mDensity2 = metrics.densityDpi;
                mDisplay2 = broadcaster.getWindowManager().getDefaultDisplay();

                // create virtual display depending on device width / height
                createVirtualDisplay2();

                // register orientation change callback
                mOrientationChangeCallback2 = new OrientationChangeCallback2(getContext());
                if (mOrientationChangeCallback2.canDetectOrientation()) {
                    mOrientationChangeCallback2.enable();
                }

                // register media projection stop callback
                sMediaProjection2.registerCallback(new MediaProjectionStopCallback2(), mHandler2);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


    private int mWidth;
    private int mWidth2;
    private int mHeight;
    private int mHeight2;
    private int mRotation;
    private int mRotation2;

    private int mDensity;
    private int mDensity2;

    private ImageReader mImageReader;
    private ImageReader mImageReader2;

    private VirtualDisplay mVirtualDisplay;
    private VirtualDisplay mVirtualDisplay2;
    private static final String SCREENCAP_NAME = "screencap";


    private static final int VIRTUAL_DISPLAY_FLAGS = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;

    private Handler mHandler;
    private Handler mHandler2;

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

    private void createVirtualDisplay2() {
        // get width and height
        Point size = new Point();
        mDisplay2.getSize(size);
        mWidth2 = size.x;
        mHeight2 = size.y;

        coun2 = 0;

        // start capture reader
        mImageReader2 = ImageReader.newInstance(mWidth2, mHeight2, PixelFormat.RGBA_8888, 2);
        mVirtualDisplay2 = sMediaProjection2.createVirtualDisplay(SCREENCAP_NAME, mWidth2, mHeight2, mDensity2, VIRTUAL_DISPLAY_FLAGS, mImageReader2.getSurface(), null, mHandler2);
        mImageReader2.setOnImageAvailableListener(new ImageAvailableListener2(), mHandler2);
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


                        final Dialog dialog = new Dialog(broadcaster);
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


    private class ImageAvailableListener2 implements ImageReader.OnImageAvailableListener {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image image = null;
            //FileOutputStream fos = null;
            Bitmap bitmap = null;

            try {


                if (coun2 == 0) {
                    image = reader.acquireLatestImage();
                    if (image != null) {
                        Image.Plane[] planes = image.getPlanes();
                        ByteBuffer buffer = planes[0].getBuffer();
                        int pixelStride = planes[0].getPixelStride();
                        int rowStride = planes[0].getRowStride();
                        int rowPadding = rowStride - pixelStride * mWidth2;

                        // create bitmap

                        bitmap = Bitmap.createBitmap(mWidth2 + rowPadding / pixelStride, mHeight2, Bitmap.Config.ARGB_8888);
                        bitmap.copyPixelsFromBuffer(buffer);

                        // write bitmap to a file
                        //fos = new FileOutputStream(STORE_DIRECTORY + "/myscreen_" + IMAGES_PRODUCED + ".png");


                        File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "thumb" + String.valueOf(new Random(100)) + ".jpg");

                        OutputStream os = null;
                        try {
                            os = new BufferedOutputStream(new FileOutputStream(file2));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);

                        IMAGES_PRODUCED++;
                        Log.e(TAG, "captured image: " + IMAGES_PRODUCED);


                        final String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "Title", null);


                        //Toast.makeText(broadcaster , path , Toast.LENGTH_SHORT).show();


                        final bean b = (bean) broadcaster.getApplicationContext();


                        RequestBody reqFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
                        MultipartBody.Part body2 = null;
                        body2 = MultipartBody.Part.createFormData("image", file2.getName(), reqFile2);

                        final Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.BASE_URL)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        final AllAPIs cr = retrofit.create(AllAPIs.class);


                        Call<String> call = cr.addScreenshot(b.userId, liveId, body2);

                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {


                                //Toast.makeText(broadcaster , "Success" , Toast.LENGTH_SHORT).show();


                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });


                        Log.e(TAG, "6");


                        coun2++;


                        stopProjection2();

                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                /*if (os != null) {
                    try {
                        fos.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }*/

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

    private void stopProjection2() {
        mHandler2.post(new Runnable() {
            @Override
            public void run() {
                if (sMediaProjection2 != null) {
                    sMediaProjection2.stop();
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


    private class OrientationChangeCallback2 extends OrientationEventListener {

        OrientationChangeCallback2(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            final int rotation = mDisplay2.getRotation();
            if (rotation != mRotation2) {
                mRotation2 = rotation;
                try {
                    // clean up
                    if (mVirtualDisplay2 != null) mVirtualDisplay2.release();
                    if (mImageReader2 != null)
                        mImageReader2.setOnImageAvailableListener(null, null);

                    // re-create virtual display depending on device width / height
                    createVirtualDisplay2();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private OrientationChangeCallback2 mOrientationChangeCallback2;


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


    private class MediaProjectionStopCallback2 extends MediaProjection.Callback {
        @Override
        public void onStop() {
            Log.e("ScreenCapture", "stopping projection.");
            mHandler2.post(new Runnable() {
                @Override
                public void run() {
                    if (mVirtualDisplay2 != null) mVirtualDisplay2.release();
                    if (mImageReader2 != null)
                        mImageReader2.setOnImageAvailableListener(null, null);
                    if (mOrientationChangeCallback2 != null) mOrientationChangeCallback2.disable();
                    sMediaProjection2.unregisterCallback(MediaProjectionStopCallback2.this);
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
            //notifyItemInserted(0);
            noti();
            liveUsers.setText(String.valueOf(list.size() - 1));
        }

        public void noti()
        {

            Collections.sort(list, new Comparator<com.yl.youthlive.getIpdatedPOJO.View>() {
                @Override
                public int compare(com.yl.youthlive.getIpdatedPOJO.View view, com.yl.youthlive.getIpdatedPOJO.View t1) {

                    String lhs = view.getType().replace("\"", "");
                    String rhs = t1.getType().replace("\"", "");

                    return rhs.compareToIgnoreCase(lhs);
                }
            });

            notifyDataSetChanged();

        }

        public void removeView(com.yl.youthlive.getIpdatedPOJO.View item) {
            list.remove(item);
            notifyDataSetChanged();
            liveUsers.setText(String.valueOf(list.size()));
        }

        public List<com.yl.youthlive.getIpdatedPOJO.View> getList() {
            return list;
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

            final bean b = (bean) context.getApplicationContext();

            final com.yl.youthlive.getIpdatedPOJO.View item = list.get(position);

            final DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();

            ImageLoader loader = ImageLoader.getInstance();


            final String uid = item.getUserId().replace("\"", "");
            final String imm = item.getUserImage().replace("\"", "");
            final String un = item.getUserName().replace("\"", "");


            Log.d("imageaaa", item.getUserImage());
            Log.d("imageaaa", imm);


            loader.displayImage(b.BASE_URL + imm, holder.image, options);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (!uid.equals(b.userId)) {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.connect_dialog);
                        dialog.setCancelable(true);
                        dialog.show();


                        CircleImageView image = (CircleImageView) dialog.findViewById(R.id.image);
                        TextView name = (TextView) dialog.findViewById(R.id.name);
                        Button follo = (Button) dialog.findViewById(R.id.follow);
                        Button connect = (Button) dialog.findViewById(R.id.connect);
                        final ProgressBar bar = dialog.findViewById(R.id.progressBar10);


                        ImageLoader loader1 = ImageLoader.getInstance();

                        loader1.displayImage(b.BASE_URL + imm, image, options);

                        name.setText(un);

                        follo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                bar.setVisibility(View.VISIBLE);

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

                                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                        bar.setVisibility(View.GONE);

                                        dialog.dismiss();

                                    }

                                    @Override
                                    public void onFailure(retrofit2.Call<followBean> call, Throwable t) {

                                        bar.setVisibility(View.GONE);

                                    }
                                });

                            }
                        });


                        connect.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                if (!isConnection) {

                                    String t = item.getType().replace("\"", "");

                                    if (t.equals("d")) {

                                        Toast.makeText(context, "Your request has been sent to the user", Toast.LENGTH_SHORT).show();

                                        new CountDownTimer(3000, 1000) {

                                            @Override
                                            public void onTick(long millisUntilFinished) {


                                            }

                                            @Override
                                            public void onFinish() {

                                                isConnection = false;
                                                Toast.makeText(context, "Your Guest Live request has been rejected", Toast.LENGTH_SHORT).show();

                                            }
                                        }.start();


                                        dialog.dismiss();

                                    } else {
                                        bar.setVisibility(View.VISIBLE);

                                        final bean b = (bean) context.getApplicationContext();

                                        final Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl(b.BASE_URL)
                                                .addConverterFactory(ScalarsConverterFactory.create())
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();

                                        final AllAPIs cr = retrofit.create(AllAPIs.class);


                                        Call<requestConnectionBean> call = cr.requestConnection(liveId, b.userId, uid);

                                        call.enqueue(new Callback<requestConnectionBean>() {
                                            @Override
                                            public void onResponse(Call<requestConnectionBean> call, retrofit2.Response<requestConnectionBean> response) {
                                                Toast.makeText(context, "Your request has been sent to the user", Toast.LENGTH_SHORT).show();
                                                String im = item.getUserImage().replace("\"", "");
                                                thumbPic1 = im;

                                                isConnection = true;

                                                dialog.dismiss();


                                                bar.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onFailure(Call<requestConnectionBean> call, Throwable t) {
                                                thumbPic1 = item.getUserImage();
                                                isConnection = false;
                                                bar.setVisibility(View.GONE);
                                                Log.d("asdasdasdas", t.toString());
                                            }
                                        });
                                    }


                                } else {
                                    Toast.makeText(context, "You don't have any more room left", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }


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


    Integer gifts[] = new Integer[]
            {
                    R.drawable.g52,
                    R.drawable.g20,
                    R.drawable.g32,
                    R.drawable.g1500,
                    R.drawable.g72,
                    R.drawable.g112,
                    R.drawable.g152,
                    R.drawable.g172,
                    R.drawable.g180,
                    R.drawable.g192,
                    R.drawable.g212,
                    R.drawable.g240,
                    R.drawable.g252,
                    R.drawable.g280,
                    R.drawable.g300,
                    R.drawable.g312,
                    R.drawable.g352,
                    R.drawable.g380,
                    R.drawable.g452,
                    R.drawable.g500,
                    R.drawable.g612,
                    R.drawable.g700,
                    R.drawable.g800,
                    R.drawable.g900,
                    R.drawable.g1000,
                    R.drawable.g1100,
                    R.drawable.g1200
            };

    String names[] = {
            "heart",
            "gun",
            "scooter",
            "rakhi",
            "teddy",
            "chocolates",
            "treasure",
            "clap",
            "clock",
            "bike",
            "car",
            "bird",
            "rose",
            "dancing girl",
            "diamond",
            "superbee",
            "hug",
            "heart beat",
            "golden egg",
            "love",
            "rabbits",
            "loving heart",
            "ring",
            "kiss",
            "fire",
            "head phone",
            "weapon"
    };


    public void showGift(String giftId, String text, String profile, String user) {


        Glide.with(broadcaster).load(gifts[Integer.parseInt(giftId) - 1]).into(giftImage);
        giftText.setText(names[Integer.parseInt(giftId) - 1]);

        //Toast.makeText(broadcaster , profile , Toast.LENGTH_SHORT).show();

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
        ImageLoader loader = ImageLoader.getInstance();

        loader.displayImage(profile, giftProfile, options);

        giftUser.setText(user);

        TranslateAnimation animate = new TranslateAnimation(rootView.getWidth(), 0, 0, 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        giftLayout.startAnimation(animate);
        giftLayout.setVisibility(View.VISIBLE);

        Timer t = new Timer();

        t.schedule(new TimerTask() {
            @Override
            public void run() {

                giftLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        TranslateAnimation animate = new TranslateAnimation(0, -rootView.getWidth(), 0, 0);
                        animate.setDuration(500);
                        animate.setFillAfter(true);
                        giftLayout.startAnimation(animate);
                        giftLayout.setVisibility(View.GONE);
                    }
                });

            }
        }, 2500);


    }

    private Handler bubbleHandler;


    public boolean randomBoolean() {
        return Math.random() < 0.5;
    }


    Runnable dummyChecker = new Runnable() {
        @Override
        public void run() {
            try {
                //this function can change value of mInterval.

                if (randomBoolean()) {
                    bubbleView.startAnimation(bubbleView.getWidth(), bubbleView.getHeight());
                }

                if (dummyList.size() > 0) {
                    if (randomBoolean()) {
                        final int r = new Random().nextInt(dummyList.size());

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

                        Call<getUpdatedBean> call = cr.getPlayerUpdatedData(dummyList.get(r).getUserId(), liveId, "dummy");


                        call.enqueue(new Callback<getUpdatedBean>() {
                            @Override
                            public void onResponse(Call<getUpdatedBean> call, Response<getUpdatedBean> response) {

                                if (response.body().getStatus().equals("1")) {

                                    dummyList.remove(r);

                                }

                            }

                            @Override
                            public void onFailure(Call<getUpdatedBean> call, Throwable t) {

                            }
                        });

                    }
                }

            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                bubbleHandler.postDelayed(dummyChecker, 2000);
            }
        }
    };


    public void runDummy() {

        new CountDownTimer(12000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {


            }

            @Override
            public void onFinish() {
                dummyChecker.run();
            }
        }.start();


    }

}
