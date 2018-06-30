package com.yl.youthlive;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.streamaxia.player.StreamaxiaPlayer;
import com.streamaxia.player.listener.StreamaxiaPlayerState;
import com.yasic.bubbleview.BubbleView;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.followPOJO.followBean;
import com.yl.youthlive.getIpdatedPOJO.Comment;
import com.yl.youthlive.getIpdatedPOJO.getUpdatedBean;
import com.yl.youthlive.goLivePOJO.goLiveBean;
import com.yl.youthlive.liveCommentPOJO.liveCommentBean;
import com.yl.youthlive.requestConnectionPOJO.requestConnectionBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.opengles.GL10;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class firstFragNew extends Fragment implements StreamaxiaPlayerState {
    private MyInterface mCallback;
    RecyclerView grid;
    RecyclerView grid2;



    LinearLayoutManager manager;
    LiveAdapter adapter;
    LiveAdapter2 adapter2;
    LinearLayoutManager manager2;
    ImageButton heart;

    //Toast toast;





    String giftURL = "", giftName = "";

    private BubbleView bubbleView;
    ImageButton close;
    ImageButton folloview_friends;
    //Broadcaster mBroadcaster;
    //SurfaceView mPreviewSurface;
    private static final String APPLICATION_ID = "gA1JdKySejF0GfA0ChIvVA";
    ListView following_friendList;
    String userId, friendid, str;
    ArrayList<String> name;
    ArrayList<String> img;
    ImageView back;
    TextView tv;

    String key = "";

    private static final int PERMISSIONS_REQUEST_CODE = 0x1;
    private boolean mPermissionsGranted = true;

    private String[] mRequiredPermissions = new String[]{
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO
    };


    // followingfriend_adapter fd;
    String url = "http://youthlive.in/api/follow_unfollow.php";
    RequestQueue requestQueue;
    SharedPreferences settings;




    // The GoCoder SDK camera view




    // The GoCoder SDK audio device


    // The broadcast configuration settings


    String connId;

    ProgressBar progress;



    TextView likeCount;

    String title;





    List<Comment> cList;
    List<com.yl.youthlive.getIpdatedPOJO.View> vList;

    String liveId = "";

    int count = 0;

    Toast toast;

    TextView viewCount;


    Integer[] gfts = {
            R.drawable.gift1,
            R.drawable.gift2,
            R.drawable.gift3,
            R.drawable.gift4,
            R.drawable.gift5,
            R.drawable.gift6
    };

    ImageButton chatIcon, switchCamera, crop;
    LinearLayout chat, actions;
    EditText comment;
    FloatingActionButton send;
    CircleImageView image;
    TextView username;

    TextView beans, level;

    private int mDensity;
    LinearLayout giftLayout;
    ImageView giftIcon;
    TextView giftTitle;


    AspectRatioFrameLayout playerLayout1;

    String access = "";
    String sid = "";

    private MediaProjectionManager mProjectionManager;
    //SimpleExoPlayerView thumb;
    private static MediaProjection sMediaProjection;





    private Display mDisplay;


    public static final String ACCOUNT_SID = "AC325e3afb64517a3f8a99b2d1133f1b3d";
    public static final String API_KEY_SID = "SKf2f5e0089874c68bb2e59cbb3b9155a2";
    public static final String API_KEY_SECRET = "1js7fkFMjVfccKAKrOpfzOIf2Lvi4Esh";
    private boolean disconnectedFromOnDestroy;
    private String TAG = "ddfsdf";



    //private VideoView player;


    //  MediaPlayerConfig wzPlayerConfig;




    BroadcastReceiver commentReceiver;
    BroadcastReceiver viewReceiver;
    BroadcastReceiver likeReceiver;
    BroadcastReceiver giftReceiver;
    BroadcastReceiver statusReceiver;

    LiveScreenNew lvscreen;

    private static final int REQUEST_CODE = 100;

    private static String STORE_DIRECTORY;

    int PERMISSION_CODE = 12;

    int coun = 0;

    SurfaceView mStreamPlayerView;

    TextView texxt;

  //  protected int mWZNetworkLogLevel = WZLog.LOG_LEVEL_DEBUG;


   // private WZPlayerConfig mStreamPlayerConfig = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.live_pagernew , container , false);


        texxt = view.findViewById(R.id.texxt);
        lvscreen = (LiveScreenNew)getActivity();

        mProjectionManager = (MediaProjectionManager) lvscreen.getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        playerLayout1 = (AspectRatioFrameLayout) view.findViewById(R.id.player_layout1);

        viewCount = (TextView) view.findViewById(R.id.view_count);

        mStreamPlayerView = view.findViewById(R.id.thumbnail_video_view);

        mStreamPlayerView.setZOrderOnTop(true);

        giftLayout = (LinearLayout) view.findViewById(R.id.gift_layout);
        giftIcon = (ImageView) view.findViewById(R.id.gift_icon);
        giftTitle = (TextView) view.findViewById(R.id.gift_title);

        progress = (ProgressBar) view.findViewById(R.id.progress);
        chat = (LinearLayout) view.findViewById(R.id.chat);
        actions = (LinearLayout) view.findViewById(R.id.actions);
        chatIcon = (ImageButton) view.findViewById(R.id.chat_icon);
        switchCamera = (ImageButton) view.findViewById(R.id.switch_camera);
        comment = (EditText) view.findViewById(R.id.comment);
        send = (FloatingActionButton) view.findViewById(R.id.send);

        crop = (ImageButton) view.findViewById(R.id.crop);

        beans = (TextView) view.findViewById(R.id.beans);
        level = (TextView) view.findViewById(R.id.level);

        likeCount = (TextView) view.findViewById(R.id.like_count);

        image = (CircleImageView) view.findViewById(R.id.image);
        username = (TextView) view.findViewById(R.id.name);

        cList = new ArrayList<>();
        vList = new ArrayList<>();

        //mPreviewSurface = (SurfaceView) findViewById(R.id.PreviewSurfaceView);
        settings = getContext().getSharedPreferences("mypref", MODE_PRIVATE);
        userId = settings.getString("userid", "");
        folloview_friends = view.findViewById(R.id.folloview_friends);
        //mBroadcaster = new Broadcaster(this, APPLICATION_ID, mBroadcasterObserver);
        //mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation());
        //mBroadcaster.setTitle(getIntent().getStringExtra("title"));

        final bean b = (bean) getContext().getApplicationContext();




        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(b.userImage, image);
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


        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCallback.switchCamera();
            /*    lvscreen.goCoderCameraView.switchCamera();

                if (lvscreen.goCoder != null && lvscreen.goCoderCameraView != null) {
                    if (lvscreen.mAutoFocusDetector == null)
                        lvscreen.mAutoFocusDetector = new GestureDetectorCompat(getContext() , new AutoFocusListener(getContext() , lvscreen.goCoderCameraView));

                    WZCamera activeCamera = lvscreen.goCoderCameraView.getCamera();
                    if (activeCamera != null && activeCamera.hasCapability(WZCamera.FOCUS_MODE_CONTINUOUS))
                        activeCamera.setFocusMode(WZCamera.FOCUS_MODE_CONTINUOUS);
                }
*/
            }
        });


        switchCamera.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                toast.setText("Switch Camera");
                toast.show();

                return false;

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




        /*goCoderCameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goCoderCameraView.getCamera().hasCapability(WZCamera.FOCUS_MODE_AUTO))
                {
                    goCoderCameraView.getCamera().setFocusMode(WZCamera.FOCUS_MODE_AUTO);
                }
                else if(goCoderCameraView.getCamera().hasCapability(WZCamera.FOCUS_MODE_CONTINUOUS))
                {
                    goCoderCameraView.getCamera().setFocusMode(WZCamera.FOCUS_MODE_CONTINUOUS);
                }
            }
        });*/







        //mBroadcaster.setAuthor(b.userImage);
        //mBroadcaster.setSendPosition(true);
        //mBroadcaster.setCustomData(b.userId);
        //mBroadcaster.setSaveOnServer(false);
        grid = (RecyclerView) view.findViewById(R.id.grid);
        grid2 = (RecyclerView) view.findViewById(R.id.grid2);
        manager = new LinearLayoutManager(getContext() , LinearLayoutManager.HORIZONTAL, false);
        manager2 = new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL, true);
        heart = (ImageButton) view.findViewById(R.id.heart);
        folloview_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  follow();
            }
        });
        close = (ImageButton) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mBroadcaster.stopBroadcast();
                //finish();

                mCallback.closeConnections();




            }
        });
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bubbleView.startAnimation(bubbleView.getWidth(), bubbleView.getHeight());

            }
        });

        heart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                toast.setText("Like this Stream");
                toast.show();

                return false;

            }
        });


        adapter = new LiveAdapter(getContext(), vList);
        adapter2 = new LiveAdapter2(getContext(), cList);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        grid2.setAdapter(adapter2);
        grid2.setLayoutManager(manager2);

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


                    Call<liveCommentBean> call = cr.commentLive(b.userId, liveId, mess);


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
                       //     progress.setVisibility(View.GONE);
                            Log.d("Video upload find ", t.toString());
                        }
                    });
                }

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

                               /*HiddenShot.getInstance().buildShotAndShare(getActivity(),"Check this out");*/



/*
                Instacapture.INSTANCE.capture(lvscreen, new SimpleScreenCapturingListener() {
                    @Override
                    public void onCaptureComplete(Bitmap bitmap) {
                        //Your code here..

                        */
/*Intent i = new Intent(Intent.ACTION_SEND);

                        i.setType("image/*");


                        i.putExtra(Intent.EXTRA_STREAM, getImageUri(getContext(), SavePixels(0,0,100 , 100 , lvscreen.main)));
                        try {
                            startActivity(Intent.createChooser(i, "Share Screenshot..."));
                        } catch (android.content.ActivityNotFoundException ex) {

                            ex.printStackTrace();
                        }*//*



                    }
                });
*/

// or in Rx way
                /*Instacapture.INSTANCE.captureRx(lvscreen).subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        //Your code here..

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

                    }
                });*/



                //Bitmap bitmap_rootview = ScreenShott.getInstance().takeScreenShotOfView(lvscreen.goCoderCameraView);

                /*View rv = lvscreen.main.getRootView();

                Bitmap saveBitmap = Bitmap.createBitmap(rv.getWidth(), rv.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(saveBitmap);
                rv.draw(c);
*/





            }
        });


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


        /*Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {


*//*
                if (mBroadcaster.canStartBroadcasting()) {
                    mBroadcaster.startBroadcast();
                }
*//*


            }
        }, 3000);*/


        /*progress.setVisibility(View.VISIBLE);


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

                    actions.setVisibility(View.VISIBLE);


                    Log.d("liveId", liveId);
                    Log.d("userId", b.userId);

*//*
                    lvscreen.goCoderBroadcastConfig.setHostAddress("ec2-13-58-47-70.us-east-2.compute.amazonaws.com");
                    lvscreen.goCoderBroadcastConfig.setPortNumber(1935);
                    lvscreen.goCoderBroadcastConfig.setApplicationName("live");
                    lvscreen.goCoderBroadcastConfig.setStreamName(liveId);



                    Log.d("keyFrame", String.valueOf(lvscreen.goCoderBroadcastConfig.getVideoKeyFrameInterval()));

                    WZStreamingError configValidationError = lvscreen.goCoderBroadcastConfig.validateForBroadcast();

                    if (configValidationError != null) {
                        //Toast.makeText(LiveScreen.this, configValidationError.getErrorDescription(), Toast.LENGTH_LONG).show();
                    } else if (lvscreen.goCoderBroadcaster.getStatus().isRunning()) {
                        // Stop the broadcast that is currently running
                        lvscreen.goCoderBroadcaster.endBroadcast(firstFragNew.this);
                    } else {
                        // Start streaming
                        lvscreen.goCoderBroadcaster.startBroadcast(lvscreen.goCoderBroadcastConfig, firstFragNew.this);
                    }


                    //connectToRoom("123" , "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImN0eSI6InR3aWxpby1mcGE7dj0xIn0.eyJqdGkiOiJTSzM2NDRiMWZmNjkwYTM1Y2ZjOGZiNTFmYWYyMWI0NTY4LTE1MTkyOTYyODYiLCJpc3MiOiJTSzM2NDRiMWZmNjkwYTM1Y2ZjOGZiNTFmYWYyMWI0NTY4Iiwic3ViIjoiQUNmOWQwZTVhMTk4NmIxZTg2NzI0Y2I3ZmJiNjEyOTk2MCIsImV4cCI6MTUxOTI5OTg4NiwiZ3JhbnRzIjp7ImlkZW50aXR5IjoiY2xpZW50MiIsInZpZGVvIjp7InJvb20iOiIxMjMifX19.ULAQN_l_H1iEqNHM1-iNWWlk_ACs71zR1oiQDl0SGew");

*//*












                    schedule(liveId);

                } else {
                    Toast.makeText(getContext(), "Error going on live", Toast.LENGTH_SHORT).show();
                    lvscreen.finish();
                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<goLiveBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
                Toast.makeText(getContext() , "Error in going Live" , Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });*/



        commentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "commentreceiver test", Toast.LENGTH_SHORT).show();

                // checking for type intent filter
                if (intent.getAction().equals("commentData")) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications


                    Log.d("data" , intent.getStringExtra("data"));

                    String json = intent.getStringExtra("data");
                    Toast.makeText(context, json, Toast.LENGTH_LONG).show();

                    Gson gson = new Gson();

                    Comment item = gson.fromJson( json, Comment.class );

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



        likeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals("like")) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications


                    Log.d("data" , intent.getStringExtra("data"));

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


                    Log.d("data" , intent.getStringExtra("data"));

                    String json = intent.getStringExtra("data");

                    Gson gson = new Gson();

                    com.yl.youthlive.getIpdatedPOJO.Gift item = gson.fromJson( json, com.yl.youthlive.getIpdatedPOJO.Gift.class );



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


        statusReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals("status")) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications


                    Log.d("uurrii" , intent.getStringExtra("data"));

                    String json = intent.getStringExtra("data");

                    try {
                        JSONObject obj = new JSONObject(json);



                        String mode = obj.getString("status");
                        String uri = obj.getString("uri");



                        if (mode.equals("2"))
                        {


                            Log.d("uurrii" , uri);

                            StreamaxiaPlayer mStreamaxiaPlayer = new StreamaxiaPlayer();


                            Uri uri2 = null;
                            mStreamaxiaPlayer.initStreamaxiaPlayer(mStreamPlayerView , playerLayout1 , texxt , firstFragNew.this , getContext() , uri2);

                            uri2 = Uri.parse("rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/live/" + uri);

                            //mStreamaxiaPlayer.play(uri2 , StreamaxiaPlayer.TYPE_RTMP);
                            mStreamaxiaPlayer.play(Uri.parse("rtmp://ec2-13-58-47-70.us-east-2.compute.amazonaws.com:1935/live/" + liveId) , StreamaxiaPlayer.TYPE_RTMP);

                            Log.d("uurrii" , uri2.toString());

                            playerLayout1.setVisibility(View.VISIBLE);


                        }
                        else
                        {

                            playerLayout1.setVisibility(View.GONE);


                        }






                    } catch (JSONException e) {
                        Log.d("uurrii" , e.toString());
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


                    Log.d("data" , intent.getStringExtra("data"));

                    String json = intent.getStringExtra("data");

                    Gson gson = new Gson();

                    com.yl.youthlive.getIpdatedPOJO.View item = gson.fromJson( json, com.yl.youthlive.getIpdatedPOJO.View.class );

                    String id = item.getUserId();
                    if (!id.equals(b.userId))
                    {
                        adapter.addView(item);
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


        return view;

    }


    public static Bitmap SavePixels(int x, int y, int w, int h, GL10 gl)
    {
        int b[]=new int[w*(y+h)];
        int bt[]=new int[w*h];
        IntBuffer ib= IntBuffer.wrap(b);
        ib.position(0);
        gl.glReadPixels(x, 0, w, y+h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib);

        for(int i=0, k=0; i<h; i++, k++)
        {//remember, that OpenGL bitmap is incompatible with Android bitmap
            //and so, some correction need.
            for(int j=0; j<w; j++)
            {
                int pix=b[i*w+j];
                int pb=(pix>>16)&0xff;
                int pr=(pix<<16)&0x00ff0000;
                int pix1=(pix&0xff00ff00) | pr | pb;
                bt[(h-k-1)*w+j]=pix1;
            }
        }


        Bitmap sb=Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);
        return sb;
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public void schedule(final String vid) {

        Log.d("hgfjhg", vid);

        /*t = new Timer();
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

        Log.d("VEG" , b.userId);
        Log.d("VEG" , liveId);

                /*Call<checkStatusBean> call1 = cr.checkStatus(b.userId, liveId);
                call1.enqueue(new Callback<checkStatusBean>() {
                    @Override
                    public void onResponse(Call<checkStatusBean> call, retrofit2.Response<checkStatusBean> response) {


                        try {


                            //Log.d("conId", response.body().getData().get(0).getUrl());

                            if (response.body().getData().get(0).getUrl().length() > 0)
                            {

                            //if (started) {

                                if (!playing) {


                                    MediaPlayerConfig wzPlayerConfig = new MediaPlayerConfig();


                                    //wzPlayerConfig.setConnectionUrl("rtsp://ec2-18-219-154-44.us-east-2.compute.amazonaws.com:1935/live/" + response.body().getData().get(0).getUrl());
                                    wzPlayerConfig.setConnectionUrl("rtsp://ec2-18-219-154-44.us-east-2.compute.amazonaws.com:1935/vod/sample.mp4");

                                    Log.e("url", "rtsp://ec2-18-219-154-44.us-east-2.compute.amazonaws.com:554/live/" + response.body().getData().get(0).getUrl());

                                    wzPlayerConfig.setConnectionNetworkProtocol(1);
                                    wzPlayerConfig.setConnectionDetectionTime(300);
                                    wzPlayerConfig.setConnectionBufferingTime(0);
                                    wzPlayerConfig.setConnectionBufferingSize(0);
                                    wzPlayerConfig.setDecodingType(0);
                                    wzPlayerConfig.setDecoderLatency(1);
                                    wzPlayerConfig.setNumberOfCPUCores(0);
                                    //wzPlayerConfig.setRendererType(1);
                                    wzPlayerConfig.setSynchroEnable(1);
                                    wzPlayerConfig.setSynchroNeedDropVideoFrames(0);
                                    wzPlayerConfig.setEnableAspectRatio(2);
                                    wzPlayerConfig.setDataReceiveTimeout(30000);


                                    //wzPlayerConfig.setNumberOfCPUCores(0);

                                    player.Open(wzPlayerConfig, LiveScreen.this);

                                    //player.nativePlayerSetCallback()



                                    *//*player.setVideoURI(Uri.parse("rtsp://ec2-18-219-154-44.us-east-2.compute.amazonaws.com:554/live/" + response.body().getData().get(0).getUrl()));

                                    player.requestFocus();

                                    player.setOnPreparedListener(new android.media.MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(android.media.MediaPlayer mediaPlayer) {

                                            player.start();

                                        }
                                    });*//*





                                    playing = true;

                                }
                            //}
                            }


*//*


                                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                                TrackSelection.Factory videoTrackSelectionFactory =
                                        new AdaptiveTrackSelection.Factory(bandwidthMeter);
                                TrackSelector trackSelector =
                                        new DefaultTrackSelector(videoTrackSelectionFactory);


/*

                                player = ExoPlayerFactory.newSimpleInstance(LiveScreen.this , trackSelector);

                                SimpleExoPlayerView simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.surface);

                                simpleExoPlayerView.setPlayer(player);


                                simpleExoPlayerView.setUseController(false);
*//*


                            //if (!playing) {

                                *//*playerLayout1.setVisibility(View.VISIBLE);

                                String ur = "rtsp://ec2-18-219-154-44.us-east-2.compute.amazonaws.com:1935/sublive/" + response.body().getData().get(0).getUrl();

                                //surface.setScaleType(TextureVideoView.ScaleType.CENTER_CROP);
// Use `setDataSource` method to set data source, this could be url, assets folder or path
                                //surface.setDataSource(ur);
                                //surface.play();

                                *//**//*rtmpDataSourceFactory = new RtmpDataSourceFactory();

                                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                                MediaSource videoSource = new ExtractorMediaSource(Uri.parse(ur),
                                        rtmpDataSourceFactory, extractorsFactory, null, null);
*//**//*

                                Log.d("player" , "entered");


                                MediaPlayerConfig wzPlayerConfig = new MediaPlayerConfig();


                                wzPlayerConfig.setConnectionUrl("rtsp://ec2-18-219-154-44.us-east-2.compute.amazonaws.com:1935/vod/sample.mp4");

                                wzPlayerConfig.setConnectionNetworkProtocol(1);
                                wzPlayerConfig.setConnectionDetectionTime(300);
                                wzPlayerConfig.setConnectionBufferingTime(0);
                                wzPlayerConfig.setConnectionBufferingSize(0);
                                wzPlayerConfig.setDecodingType(0);
                                wzPlayerConfig.setDecoderLatency(1);
                                wzPlayerConfig.setNumberOfCPUCores(0);
                                //wzPlayerConfig.setRendererType(1);
                                wzPlayerConfig.setSynchroEnable(1);
                                wzPlayerConfig.setSynchroNeedDropVideoFrames(0);
                                wzPlayerConfig.setEnableAspectRatio(2);
                                wzPlayerConfig.setDataReceiveTimeout(30000);
                                //wzPlayerConfig.setNumberOfCPUCores(0);


                                player.Open(wzPlayerConfig, LiveScreen.this);


                                playing = true;*//*

                            //}


                            //player.prepare(videoSource);

*//*                                player.setPlayWhenReady(true);*//*

                            //connId = response.body().getData().get(0).getId();

                        } catch (Exception e) {
                            //e.printStackTrace();

                        }


                    }

                    @Override
                    public void onFailure(Call<checkStatusBean> call, Throwable t) {

                        //Log.d("error" , t.toString());

                    }
                });
*/



        SharedPreferences fcmPref = getContext().getSharedPreferences("fcm" , Context.MODE_PRIVATE);

        String keey = fcmPref.getString("token" , "");
        Toast.makeText(b, keey, Toast.LENGTH_SHORT).show();
        Log.d("keeey" , keey);

        Call<getUpdatedBean> call = cr.getUpdatedData(b.userId, vid , keey);


        call.enqueue(new Callback<getUpdatedBean>() {
            @Override
            public void onResponse(Call<getUpdatedBean> call, retrofit2.Response<getUpdatedBean> response) {

                try {

                    adapter2.setGridData(response.body().getData().getComments());
                    adapter.setGridData(response.body().getData().getViews());

                    int count1 = Integer.parseInt(response.body().getData().getLikesCount());

                    beans.setText(response.body().getData().getBeans());
                    level.setText(response.body().getData().getLevel());


                    viewCount.setText(response.body().getData().getViewsCount());

                    username.setText(response.body().getData().getTimelineName());

                    if (response.body().getData().getGift().size() > 0) {
                        try {

                            giftName = response.body().getData().getGift().get(0).getGiftId();

                            showGift(Integer.parseInt(response.body().getData().getGift().get(0).getGiftId()), response.body().getData().getGift().get(0).getGiftName());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                    if (count1 > count) {
                        for (int i = 0; i < count1 - count; i++)

                            bubbleView.startAnimation(bubbleView.getWidth(), bubbleView.getHeight());

                        likeCount.setText(response.body().getData().getLikesCount());

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
                    LocalBroadcastManager.getInstance(getContext()).registerReceiver(statusReceiver,
                            new IntentFilter("status"));

                } catch (Exception e) {
                    // e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<getUpdatedBean> call, Throwable t) {

                // Log.d("asdasd", t.toString());

            }
        });

      /*      }
        }, 0, 1000);*/

    }

    public void showGift(final int pos, String title) {

        giftLayout.setVisibility(View.VISIBLE);

        Glide.with(getContext().getApplicationContext()).load(gfts[pos - 1]).into(giftIcon);
        giftTitle.setText(title);

        final Timer t = new Timer();

        t.schedule(new TimerTask() {
            @Override
            public void run() {


                giftLayout.getHandler().post(new Runnable() {
                    public void run() {
                        giftLayout.setVisibility(View.GONE);
                    }
                });

                if (t != null) {
                    t.cancel();
                }


            }
        }, 2500);


    }

    @Override
    public void stateENDED() {
Log.d("uurrii" , "ended");
    }

    @Override
    public void stateBUFFERING() {
        Log.d("uurrii" , "buffering");
    }

    @Override
    public void stateIDLE() {
        Log.d("uurrii" , "idle");
    }

    @Override
    public void statePREPARING() {
        Log.d("uurrii" , "preparing");
    }

    @Override
    public void stateREADY() {
        Log.d("uurrii" , "ready");
    }

    @Override
    public void stateUNKNOWN() {
        Log.d("uurrii" , "unknown");
    }






/*

    public void BlockPersson(View view) {
        PersonBlock();
    }
*/





    /*@Override
    public int Status(int i) {
        Log.e("VEG", "From Native listitem status: " + i);
        return 0;
    }

    @Override
    public int OnReceiveData(ByteBuffer byteBuffer, int i, long l) {
        Log.e("VEG", "Form Native listitem OnReceiveData: size: " + i + ", pts: " + l);
        return 0;
    }*/

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

        public void addView(com.yl.youthlive.getIpdatedPOJO.View item)
        {
            list.add(0 , item);
            notifyItemInserted(0);
            viewCount.setText(String.valueOf(list.size()));
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

            final com.yl.youthlive.getIpdatedPOJO.View item = list.get(position);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();

            ImageLoader loader = ImageLoader.getInstance();

            loader.displayImage(item.getUserImage(), holder.image, options);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String uid = item.getUserId();
                    uid.replace("\"" , "");

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


        public void addComment(Comment item)
        {
            list.add(0 , item);

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

            final String uid = item.getUserId().replace("\"" , "");

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();

            ImageLoader loader = ImageLoader.getInstance();

            String im = item.getUserImage().replace("\"" , "");

            loader.displayImage(im , holder.index, options);

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


                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.connect_dialog);
                    dialog.setCancelable(true);
                    dialog.show();


                    CircleImageView image = (CircleImageView) dialog.findViewById(R.id.image);
                    TextView name = (TextView) dialog.findViewById(R.id.name);
                    Button follo = (Button) dialog.findViewById(R.id.follow);
                    Button connect = (Button) dialog.findViewById(R.id.connect);


                    ImageLoader loader1 = ImageLoader.getInstance();

                    loader1.displayImage(b.userImage, image);

                    name.setText(b.userName);

                    follo.setOnClickListener(new View.OnClickListener() {
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


                            Call<followBean> call = cr.follow(b.userId, uid);

                            call.enqueue(new Callback<followBean>() {
                                @Override
                                public void onResponse(Call<followBean> call, retrofit2.Response<followBean> response) {

                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                    progress.setVisibility(View.GONE);

                                    dialog.dismiss();

                                }

                                @Override
                                public void onFailure(Call<followBean> call, Throwable t) {

                                    progress.setVisibility(View.GONE);

                                }
                            });

                        }
                    });


                    connect.setOnClickListener(new View.OnClickListener() {
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


                            Call<requestConnectionBean> call = cr.requestConnection(liveId, b.userId, uid);

                            call.enqueue(new Callback<requestConnectionBean>() {
                                @Override
                                public void onResponse(Call<requestConnectionBean> call, retrofit2.Response<requestConnectionBean> response) {


                                    playerLayout1.setVisibility(View.VISIBLE);


                                    progress.setVisibility(View.GONE);

                                    dialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<requestConnectionBean> call, Throwable t) {
                                    progress.setVisibility(View.GONE);
                                    Log.d("asdasdasdas" , t.toString());
                                }
                            });


                        }
                    });


                }
            });





            if (Objects.equals(uid, b.userId)) {
                holder.add.setVisibility(View.GONE);
            } else {
                holder.add.setVisibility(View.VISIBLE);
            }

            String com = item.getComment().replace("\"" , "");

            String us = item.getUserName().replace("\"" , "");

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


    @Override
    public void onPause() {

        super.onPause();

        //goCoderCameraView.stopPreview();

      //  mBroadcaster.onActivityPause();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            sMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);

            if (sMediaProjection != null) {
                File externalFilesDir = lvscreen.getExternalFilesDir(null);
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
                mDisplay = lvscreen.getWindowManager().getDefaultDisplay();

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

    public void follow() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("status");
                    if (!status.equals(0)) {
                        JSONObject obj2 = jObj.getJSONObject("data");
                        userId = obj2.getString("userId");
                        friendid = obj2.getString("friendId");

                    } else {
                        str = jObj.getString("message");
                        //Toast.makeText(LiveScreen.this, str, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // hidepDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(LiveScreen.this, error.toString(), Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                bean b = new bean();
                params.put("userId", "170");
                params.put("friendId", "19");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        mPermissionsGranted = true;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                // Check the result of each permission granted
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        mPermissionsGranted = false;
                    }
                }
            }
        }
    }

    //
// Utility method to check the status of a permissions request for an array of permission identifiers
//
    private static boolean hasPermissions(Context context, String[] permissions) {
        for (String permission : permissions)
            if (context.checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false;

        return true;
    }


    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(commentReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(viewReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(likeReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(giftReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(statusReceiver);
        super.onStop();


        //goCoderCameraView.stopPreview();




    }

    private void startProjection() {
        startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (MyInterface) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement IFragmentToActivity");
        }
    }
    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        progress.setVisibility(View.VISIBLE);
        final bean b = (bean) getContext().getApplicationContext();

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

                    actions.setVisibility(View.VISIBLE);

                    mCallback.startStreaming(liveId);

                    schedule(liveId);

                } else {
                    Toast.makeText(getContext(), "Error going on live", Toast.LENGTH_SHORT).show();
                    lvscreen.finish();
                }

                progress.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<goLiveBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
                Toast.makeText(getContext() , "Error in going Live" , Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }

            });
    }
}
