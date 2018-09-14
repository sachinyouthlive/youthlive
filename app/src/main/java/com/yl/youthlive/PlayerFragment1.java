package com.yl.youthlive;

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
import com.yl.youthlive.endLivePOJO.Data;
import com.yl.youthlive.followPOJO.followBean;
import com.yl.youthlive.getIpdatedPOJO.Comment;
import com.yl.youthlive.getIpdatedPOJO.getUpdatedBean;
import com.yl.youthlive.liveCommentPOJO.liveCommentBean;
import com.yl.youthlive.liveLikePOJO.liveLikeBean;
import com.yl.youthlive.requestConnectionPOJO.requestConnectionBean;
import com.yl.youthlive.sendGiftPOJO.sendGiftBean;

import org.json.JSONException;
import org.json.JSONObject;

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
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PlayerFragment1 extends Fragment //implements RecordHandler.RecordListener, RtmpHandler.RtmpListener, EncoderHandler.EncodeListener
{

    TextView newMessage;

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    ImageButton emoji, message, send, gift, crop;

    TextView connect;

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
    BroadcastReceiver requestReceiver2;
    BroadcastReceiver connectionReceiver;
    BroadcastReceiver statusReceiver;
    BroadcastReceiver statusReceiverPlayer;
    BroadcastReceiver exitReceiver;


    View rootView;
    private EmojIconActions emojIcon;


    ProgressBar progress;
    String liveId;

    ImageButton timeLineFollow;


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


    RelativeLayout thumbCameraContainer1;

    TextView reject1;

    View giftLayout;
    ImageView giftImage;
    TextView giftText;
    CircleImageView giftProfile;
    TextView giftUser;


    boolean isConnection = false;

    TextView ylId;

    HomeActivity homeActivity;

    public void setHomeActivity(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.player_fragment_layout1, container, false);


        player = ((VideoPlayer)getActivity());

        ylId = view.findViewById(R.id.ylid);

        mProjectionManager = (MediaProjectionManager) player.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        //giftImage = view.findViewById(R.id.imageView13);
        //giftText = view.findViewById(R.id.textView28);

        giftImage = view.findViewById(R.id.imageView18);
        giftText = view.findViewById(R.id.textView50);
        giftProfile = view.findViewById(R.id.circleImageView);
        giftUser = view.findViewById(R.id.textView49);

        giftLayout = view.findViewById(R.id.gift_layout);


        reject1 = view.findViewById(R.id.reject1);

        reject1.setZ(21);

        thumbCameraContainer1 = view.findViewById(R.id.view3);

        bubbleView = (BubbleView) view.findViewById(R.id.bubble);
        timeLineFollow = view.findViewById(R.id.folloview_friends);


        List<Drawable> drawableList = new ArrayList<>();
        //drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_indigo_900_24dp));
        //drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_deep_purple_900_24dp));
        //drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_cyan_900_24dp));
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


                        Call<liveCommentBean> call = cr.commentLive(SharePreferenceUtils.getInstance().getString("userId"), liveId, mess, "basic");

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


        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final bean b = (bean) player.getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<String> call = cr.exitPlayer(SharePreferenceUtils.getInstance().getString("userId"), liveId);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        progress.setVisibility(View.GONE);


                        //homeActivity.removeFrag();

                        player.finish();

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                        progress.setVisibility(View.GONE);

                    }
                });
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


                retrofit2.Call<liveLikeBean> call = cr.likeLive(SharePreferenceUtils.getInstance().getString("userId"), liveId);

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

                    final String uid = item.getUserId().replace("\"", "");

                    String id = item.getUserId();
                    if (!uid.equals(timelineId)) {
                        viewsAdapter.addView(item);
                        viewersGrid.smoothScrollToPosition(0);
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

/*
                        Intent registrationComplete = new Intent("eenndd");
                        registrationComplete.putExtra("image", image);
                        registrationComplete.putExtra("tid", timelineId);
                        registrationComplete.putExtra("tname", timelineName.getText().toString());
                        registrationComplete.putExtra("ltime", item.getLiveTime());
                        registrationComplete.putExtra("views", item.getViewers());

                        LocalBroadcastManager.getInstance(player).sendBroadcast(registrationComplete);

                        //mCallback.onEndListener(image , timelineId , timelineName.getText().toString() , item.getLiveTime() , item.getViewers());
*/


                        Intent intent1 = new Intent(getContext(), LiveEndedPlayer.class);
                        intent1.putExtra("image", image);
                        intent1.putExtra("id", timelineId);
                        intent1.putExtra("name", timelineName.getText().toString());
                        intent1.putExtra("time", item.getLiveTime());
                        intent1.putExtra("views", item.getViewers());
                        startActivity(intent1);
                        player.overridePendingTransition(0,0);
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

                    Log.d("ddata", intent.getStringExtra("data"));

                    String json = intent.getStringExtra("data");

                    try {
                        JSONObject object = new JSONObject(json);

                        connId = object.getString("conId");

                        String uid = object.getString("uid");

                        if (uid.equals(SharePreferenceUtils.getInstance().getString("userId"))) {
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


                                    thumbCameraContainer1.setVisibility(View.VISIBLE);

                                    reject1.setVisibility(View.VISIBLE);

                                    isConnection = true;

                                    player.startThumbCamera1(connId);
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

                                    Call<acceptRejectBean> call1 = cr.acceptReject(connId, liveId, "1", SharePreferenceUtils.getInstance().getString("userId"));
                                    call1.enqueue(new Callback<acceptRejectBean>() {
                                        @Override
                                        public void onResponse(Call<acceptRejectBean> call, Response<acceptRejectBean> response) {

                                            try {


                                                isConnection = false;
                                                //cameraLayout1.setVisibility(View.VISIBLE);


                                                reject1.setVisibility(View.GONE);

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


                        } else {
                            isConnection = true;
                        }

                    } catch (JSONException e) {
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


        statusReceiver = new BroadcastReceiver() {
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

                        String uid = obj.getString("uid");

                        if (mode.equals("2")) {


                            if (!uid.equals(SharePreferenceUtils.getInstance().getString("userId"))) {
                                Log.d("ddata", uri);


                                new CountDownTimer(3000, 1000) {


                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                    }

                                    @Override
                                    public void onFinish() {

                                        reject1.setVisibility(View.GONE);

                                        isConnection = true;

                                        player.startThumbPlayer1(uri);
                                        thumbCameraContainer1.setVisibility(View.VISIBLE);

                                    }
                                }.start();

                            }

                        } else {
                            isConnection = false;
                        }


                    } catch (JSONException e) {
                        Log.d("ddata", e.toString());
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


        connectionReceiver = new BroadcastReceiver() {
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


                        if (uid.equals(SharePreferenceUtils.getInstance().getString("userId"))) {
                            player.endThumbCamera1();
                            thumbCameraContainer1.setVisibility(View.GONE);
                            isConnection = false;
                        } else {
                            player.endThumbPlayer1();
                            thumbCameraContainer1.setVisibility(View.GONE);
                            isConnection = false;

                        }


                    } catch (JSONException e) {
                        Log.d("uurrii", e.toString());
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


        statusReceiverPlayer = new BroadcastReceiver() {
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


                        String uid = obj.getString("uid");


                        if (uid.equals(SharePreferenceUtils.getInstance().getString("userId"))) {
                            if (mode.equals("2")) {


                                thumbCameraContainer1.setVisibility(View.VISIBLE);

                                reject1.setVisibility(View.VISIBLE);

                                isConnection = true;

                                player.startThumbCamera1FromPlayer(connId);


                            } else {

                                isConnection = false;
                                Toast.makeText(player, "Your Guest Live request has been rejected", Toast.LENGTH_SHORT).show();


                            }

                        } else {

                            if (mode.equals("2")) {


                                new CountDownTimer(3000, 1000) {


                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                    }

                                    @Override
                                    public void onFinish() {

                                        reject1.setVisibility(View.GONE);

                                        isConnection = true;

                                        player.startThumbPlayer1(uri);
                                        thumbCameraContainer1.setVisibility(View.VISIBLE);

                                    }
                                }.start();


                            } else {

                                isConnection = false;


                            }


                        }


                    } catch (JSONException e) {
                        Log.d("uurrii", e.toString());
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


        requestReceiver2 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals("request_player")) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications

                    //isConnection = true;

                    Log.d("ddata", intent.getStringExtra("data"));

                    String json = intent.getStringExtra("data");

                    try {
                        JSONObject object = new JSONObject(json);

                        connId = object.getString("conId");

                        final String uid = object.getString("uid");


                    } catch (JSONException e) {
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


        exitReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals("exit")) {


                    try {
                        Log.d("data", intent.getStringExtra("data"));

                        String json = intent.getStringExtra("data");

                        Gson gson = new Gson();

                        com.yl.youthlive.getIpdatedPOJO.View item = gson.fromJson(json, com.yl.youthlive.getIpdatedPOJO.View.class);

                        final String uid = item.getUserId().replace("\"", "");

                        final bean b = (bean) homeActivity.getApplicationContext();

                        String id = item.getUserId();
                        if (!uid.equals(SharePreferenceUtils.getInstance().getString("userId"))) {
                            viewsAdapter.removeView(item);
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }

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


                    Call<liveCommentBean> call = cr.commentLive(SharePreferenceUtils.getInstance().getString("userId"), liveId, mess, "basic");

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

                GridLayoutManager giftManager = new GridLayoutManager(player, 2);

                GiftAdapter giftAdapter = new GiftAdapter(player, bar, dialog);

                giftGrid.setLayoutManager(giftManager);
                giftGrid.setAdapter(giftAdapter);


            }
        });


        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //broadcaster.switchCamera();


                if (!isConnection) {

                    final bean b = (bean) player.getApplicationContext();

                    progress.setVisibility(View.VISIBLE);

                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);


                    Call<requestConnectionBean> call = cr.requestConnectionFromPlayer(liveId, timelineId, SharePreferenceUtils.getInstance().getString("userId"));

                    call.enqueue(new Callback<requestConnectionBean>() {
                        @Override
                        public void onResponse(Call<requestConnectionBean> call, retrofit2.Response<requestConnectionBean> response) {
                            Toast.makeText(player, "Your request has been sent to the broadcaster", Toast.LENGTH_SHORT).show();

                            progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<requestConnectionBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                            Log.d("asdasdasdas", t.toString());
                        }
                    });


                } else {
                    Toast.makeText(player, "Sorry, No room available for guest live", Toast.LENGTH_SHORT).show();
                }


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


                retrofit2.Call<followBean> call = cr.followLiveUser(SharePreferenceUtils.getInstance().getString("userId"), timelineId, liveId);

                call.enqueue(new retrofit2.Callback<followBean>() {
                    @Override
                    public void onResponse(retrofit2.Call<followBean> call, retrofit2.Response<followBean> response) {

                        if (response.body().getStatus().equals("1")) {
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


        liveId = getArguments().getString("liveId");
        schedule(liveId);


        return view;
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

        Call<getUpdatedBean> call = cr.getPlayerUpdatedData(SharePreferenceUtils.getInstance().getString("userId"), liveId, keey);


        call.enqueue(new Callback<getUpdatedBean>() {
            @Override
            public void onResponse(Call<getUpdatedBean> call, retrofit2.Response<getUpdatedBean> response) {

                try {

                    bubbleChecker.run();

                    ylId.setText(response.body().getData().getYouthliveId());

                    timelineId = response.body().getData().getTimelineId();


                    commentsAdapter.setGridData(response.body().getData().getComments());


                    for (int i = 0; i < response.body().getData().getViews().size(); i++) {

                        final String uid = response.body().getData().getViews().get(i).getUserId().replace("\"", "");

                        if (!uid.equals(SharePreferenceUtils.getInstance().getString("userId"))) {
                            viewsAdapter.addView(response.body().getData().getViews().get(i));
                            //viewersGrid.smoothScrollToPosition(0);
                        }


                    }

                    //viewsAdapter.setGridData(response.body().getData().getViews());

                    int count1 = Integer.parseInt(response.body().getData().getLikesCount());

                    likeCount.setText(String.valueOf(count1));

                    totalBeans.setText(response.body().getData().getBeans2() + " Coins");


                    liveUsers.setText(response.body().getData().getViewsCount());

                    timelineName.setText(response.body().getData().getTimelineName());

                    player.setData(timelineId, response.body().getData().getTimelineName());

                    image = response.body().getData().getTimelineProfileImage();

                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();

                    ImageLoader loader = ImageLoader.getInstance();

                    loader.displayImage(b.BASE_URL + response.body().getData().getTimelineProfileImage(), timelineProfile, options);

//                    Toast.makeText(player , response.body().getData().getTimelineProfileImage() , Toast.LENGTH_SHORT).show();

                    liveUsers.setText(response.body().getData().getViewsCount());


                    if (response.body().getData().getIsConnection().equals("true")) {
                        reject1.setVisibility(View.GONE);

                        player.startThumbPlayer1(response.body().getData().getConnid());
                        thumbCameraContainer1.setVisibility(View.VISIBLE);

                        isConnection = true;

                    }


                    if (response.body().getData().getFollow().equals("true")) {

                        timeLineFollow.setVisibility(View.GONE);

                    } else {
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
                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(requestReceiver2,
                            new IntentFilter("request_player"));
                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(connectionReceiver,
                            new IntentFilter("connection_end"));
                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(statusReceiver,
                            new IntentFilter("status"));
                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(statusReceiverPlayer,
                            new IntentFilter("status_player"));
                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(exitReceiver,
                            new IntentFilter("exit"));
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
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<getUpdatedBean> call, Throwable t) {


                t.printStackTrace();
                // Log.d("asdasd", t.toString());

            }
        });


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

            //Log.d("ttyyppee" , type);

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

                //holder.index.setVisibility(View.GONE);
                if (Objects.equals(item.getFriendStatus().getFollow(), "true")) {
                    holder.add.setVisibility(View.GONE);
                } else {
                    holder.add.setVisibility(View.VISIBLE);
                }

                if (Objects.equals(uid, SharePreferenceUtils.getInstance().getString("userId"))) {
                    holder.add.setVisibility(View.GONE);
                } else {
                    holder.add.setVisibility(View.VISIBLE);
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
                    holder.add.setVisibility(View.GONE);
                } else {
                    holder.add.setVisibility(View.VISIBLE);
                }

                holder.add.setVisibility(View.GONE);

            } else if (type.equals("gift")) {

                String us = item.getUserId().replace("\"", "");
                String gid = item.getComment().replace("\"", "");
                holder.name.setText(us + " has sent a  ");


                Drawable drawable = context.getResources().getDrawable(gifts[Integer.parseInt(gid) - 1]);

                drawable.setBounds(0, 0, 50, 50);

                int selectionCursor = holder.name.getSelectionStart();
                //holder.name.getText().insert(selectionCursor, ".");
                selectionCursor = holder.name.getText().length();

                SpannableStringBuilder builder = new SpannableStringBuilder(holder.name.getText());
                builder.setSpan(new ImageSpan(drawable), selectionCursor - ".".length(), selectionCursor, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.name.setText(builder);

                holder.add.setVisibility(View.GONE);

                holder.container.setBackground(context.getResources().getDrawable(R.drawable.blue_round2));

                holder.index.setVisibility(View.GONE);
            }


            //holder.user.setText(us);

/*
            holder.index.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    */
/*Intent intent = new Intent(context, TimelineProfile.class);
                    intent.putExtra("userId", uid);
                    startActivity(intent);*//*


                }
            });

*/

            holder.index.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    bean b = (bean) context.getApplicationContext();

                    if (!uid.equals(SharePreferenceUtils.getInstance().getString("userId"))) {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.follow_dialog);
                        dialog.setCancelable(true);
                        dialog.show();


                        CircleImageView image = (CircleImageView) dialog.findViewById(R.id.image);
                        TextView name = (TextView) dialog.findViewById(R.id.name);
                        Button follo = (Button) dialog.findViewById(R.id.follow);
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


                                retrofit2.Call<followBean> call = cr.follow(SharePreferenceUtils.getInstance().getString("userId"), uid);

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
                    }






                    /*progress.setVisibility(View.VISIBLE);

                    final bean b = (bean) context.getApplicationContext();

                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);


                    retrofit2.Call<followBean> call = cr.follow(SharePreferenceUtils.getInstance().getString("userId"), uid);

                    call.enqueue(new retrofit2.Callback<followBean>() {
                        @Override
                        public void onResponse(retrofit2.Call<followBean> call, retrofit2.Response<followBean> response) {

                            if (response.body().getStatus().equals("1")) {
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
*/

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


    private void startProjection() {
        startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
    }


    @Override
    public void onStop() {
        super.onStop();

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
                mDisplay = getActivity().getWindowManager().getDefaultDisplay();

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

                Log.d("screenshot", String.valueOf(coun));

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
                } else {

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
            liveUsers.setText(String.valueOf(list.size()));
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

            final bean b = (bean) context.getApplicationContext();

            final com.yl.youthlive.getIpdatedPOJO.View item = list.get(position);

            final DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();

            ImageLoader loader = ImageLoader.getInstance();


            final String uid = item.getUserId().replace("\"", "");
            final String imm = item.getUserImage().replace("\"", "");
            final String un = item.getUserName().replace("\"", "");


            loader.displayImage(b.BASE_URL + imm, holder.image, options);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (!uid.equals(SharePreferenceUtils.getInstance().getString("userId"))) {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.follow_dialog);
                        dialog.setCancelable(true);
                        dialog.show();


                        CircleImageView image = (CircleImageView) dialog.findViewById(R.id.image);
                        TextView name = (TextView) dialog.findViewById(R.id.name);
                        Button follo = (Button) dialog.findViewById(R.id.follow);
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


                                retrofit2.Call<followBean> call = cr.follow(SharePreferenceUtils.getInstance().getString("userId"), uid);

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

    @Override
    public void onResume() {
        super.onResume();


    }


    class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ViewHolder> {

        Context context;
        ProgressBar progressBar;
        Dialog dialog;
        Integer gifs[] = new Integer[]
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


        String diamonds[] = {
                "12",
                "20",
                "32",
                "52",
                "72",
                "112",
                "152",
                "172",
                "180",
                "192",
                "212",
                "240",
                "252",
                "280",
                "300",
                "312",
                "352",
                "380",
                "452",
                "500",
                "612",
                "700",
                "800",
                "900",
                "1000",
                "1100",
                "1200"
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


        public GiftAdapter(Context context, ProgressBar progress, Dialog dialog) {
            this.context = context;
            this.progressBar = progress;
            this.dialog = dialog;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.gift_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

            Glide.with(context).load(gifs[position]).into(holder.image);

            holder.send.setText(diamonds[position]);

            holder.name.setText(names[position]);

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


                    retrofit2.Call<sendGiftBean> call = cr.sendGift(SharePreferenceUtils.getInstance().getString("userId"), liveId, timelineId, String.valueOf(position + 1), "1", diamonds[position]);


                    call.enqueue(new retrofit2.Callback<sendGiftBean>() {
                        @Override
                        public void onResponse(retrofit2.Call<sendGiftBean> call, retrofit2.Response<sendGiftBean> response) {

                            //Log.d("Asdasdsa", response.body().getMessage());


                            try {
                                if (Objects.equals(response.body().getStatus(), "1")) {
                                    //Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
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
            return 27;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            Button send;
            TextView name;

            public ViewHolder(View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.imageView12);
                send = itemView.findViewById(R.id.button8);
                name = itemView.findViewById(R.id.textView48);

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

        Log.d("ggiidd", giftId);


        giftText.setText(names[Integer.parseInt(giftId) - 1]);

        //Toast.makeText(player, profile, Toast.LENGTH_SHORT).show();

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
        ImageLoader loader = ImageLoader.getInstance();

        loader.displayImage(profile, giftProfile, options);

        giftUser.setText(user);

        TranslateAnimation animate = new TranslateAnimation(rootView.getWidth(), 0, 0, 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        giftLayout.startAnimation(animate);
        giftLayout.setVisibility(View.VISIBLE);

        Glide.with(player).load(gifts[Integer.parseInt(giftId) - 1]).into(giftImage);

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


    Runnable bubbleChecker = new Runnable() {
        @Override
        public void run() {
            try {
                //this function can change value of mInterval.

                if (randomBoolean()) {
                    bubbleView.startAnimation(bubbleView.getWidth(), bubbleView.getHeight());
                }

            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(bubbleChecker, 800);
            }
        }
    };


    public boolean randomBoolean() {
        return Math.random() < 0.5;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(commentReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(likeReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(viewReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(giftReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(endReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(requestReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(requestReceiver2);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(connectionReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(statusReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(statusReceiverPlayer);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(exitReceiver);


    }


    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof endListener) {
            mCallback = (endListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnGreenFragmentListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }


    endListener mCallback;

    public interface endListener{
        public void onEndListener(String image , String timelineId , String timelineName , String liveTime , String viewers);
    }

}
