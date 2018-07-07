package com.yl.youthlive;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class IrisLive extends AppCompatActivity {


    /*RecyclerView grid;
    RecyclerView grid2;
    LinearLayoutManager manager;
    LiveAdapter adapter;
    LiveAdapter2 adapter2;
    LinearLayoutManager manager2;
    ImageButton heart;

    String uri = "";

    BroadcastPlayer mBroadcastPlayer;

    //Toast toast;

    String giftURL = "", giftName = "";

    private BubbleView bubbleView;
    ImageButton close;
    ImageButton folloview_friends;
    SurfaceViewWithAutoAR mPreviewSurface;
    private static final String APPLICATION_ID = "FcXZjZu7gfAKqLhb1xJaeg";
    ListView following_friendList;
    String userId, friendid, str;
    Broadcaster mBroadcaster;
    ArrayList<String> name;
    ArrayList<String> img;
    ImageView back;
    TextView tv;

    SurfaceViewWithAutoAR player1;

    private static final String API_KEY = "Fmn10d2ffa4mnxw8ec13z";


    String key = "";

    private static final int PERMISSIONS_REQUEST_CODE = 0x1;
    private boolean mPermissionsGranted = true;

    private String[] mRequiredPermissions = new String[]{
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO
    };


    // followingfriend_adapter fd;
    String url = "http://nationproducts.in/youthlive/api/follow_unfollow.php";
    RequestQueue requestQueue;
    SharedPreferences settings;



    ProgressBar progress;

    TextView likeCount;

    String title;

    List<Comment> cList;
    List<com.youthlive.youthlive.getIpdatedPOJO.View> vList;

    String liveId = "";

    int count = 0;

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


    LinearLayout giftLayout;
    ImageView giftIcon;
    TextView giftTitle;

    ProgressDialog progressDialog;

    RelativeLayout playerLayout1;
    ProgressBar progress1;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iris_live);




        /*viewCount = (TextView) findViewById(R.id.view_count);

        playerLayout1 = (RelativeLayout)findViewById(R.id.player_layout1);

        progress1 = (ProgressBar)findViewById(R.id.progress1);

        //toast = Toast.makeText(LiveScreen.this , null , Toast.LENGTH_SHORT);


// Create a configuration instance for the broadcaster


// Set the connection properties for the target Wowza Streaming Engine server or Wowza Cloud account

        //goCoderBroadcastConfig.setConnectionParameters(new WZDataMap());

// Designate the camera preview as the video source


// Designate the audio device as the audio broadcaster


        title = getIntent().getStringExtra("title");

        player1 = (SurfaceViewWithAutoAR)findViewById(R.id.player1);

        giftLayout = (LinearLayout) findViewById(R.id.gift_layout);
        giftIcon = (ImageView) findViewById(R.id.gift_icon);
        giftTitle = (TextView) findViewById(R.id.gift_title);

        progress = (ProgressBar) findViewById(R.id.progress);
        chat = (LinearLayout) findViewById(R.id.chat);
        actions = (LinearLayout) findViewById(R.id.actions);
        chatIcon = (ImageButton) findViewById(R.id.chat_icon);
        switchCamera = (ImageButton) findViewById(R.id.switch_camera);
        comment = (EditText) findViewById(R.id.comment);
        send = (FloatingActionButton) findViewById(R.id.send);

        crop = (ImageButton) findViewById(R.id.crop);

        beans = (TextView) findViewById(R.id.beans);
        level = (TextView) findViewById(R.id.level);

        likeCount = (TextView) findViewById(R.id.like_count);

        image = (CircleImageView) findViewById(R.id.image);
        username = (TextView) findViewById(R.id.name);

        cList = new ArrayList<>();
        vList = new ArrayList<>();

        mPreviewSurface = (SurfaceViewWithAutoAR) findViewById(R.id.PreviewSurfaceView);
        settings = getSharedPreferences("mypref", MODE_PRIVATE);
        userId = settings.getString("userid", "");
        folloview_friends = findViewById(R.id.folloview_friends);
        mBroadcaster = new Broadcaster(this, APPLICATION_ID, mBroadcasterObserver);
        mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation());
        mBroadcaster.setTitle(getIntent().getStringExtra("title"));
        bean b = (bean) getApplicationContext();

        username.setText(b.userName);


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


        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBroadcaster.switchCamera();
            }
        });


        mBroadcaster.setAuthor(b.userImage);
        mBroadcaster.setSendPosition(true);
        mBroadcaster.setCustomData(b.userId);
        mBroadcaster.setSaveOnServer(false);
        grid = (RecyclerView) findViewById(R.id.grid);
        grid2 = (RecyclerView) findViewById(R.id.grid2);
        manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        manager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        heart = (ImageButton) findViewById(R.id.heart);
        folloview_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  follow();
            }
        });
        close = (ImageButton) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBroadcaster.stopBroadcast();
                finish();

            }
        });
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bubbleView.startAnimation(bubbleView.getWidth(), bubbleView.getHeight());

            }
        });

        adapter = new LiveAdapter(this, vList);
        adapter2 = new LiveAdapter2(this, cList);

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

                    final bean b = (bean) getApplicationContext();

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
                            progress.setVisibility(View.GONE);
                            Log.d("Video upload find ", t.toString());
                        }
                    });
                }

            }
        });

        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //               HiddenShot.getInstance().buildShotAndShare(LiveScreen.this,"Check this out");

            }
        });

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


        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {



                if (mBroadcaster.canStartBroadcasting()) {
                    mBroadcaster.startBroadcast();
                }



            }
        }, 1000);



*/


    }


    /*private Broadcaster.Observer mBroadcasterObserver = new Broadcaster.Observer() {
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

            //Log.d("iiiid" , s);

            //Log.d("iiiids1" , s1);

        }
        @Override
        public void onBroadcastIdAvailable(String s) {

            //Log.d("iiiid" , s);

            progress.setVisibility(View.VISIBLE);

            final bean b = (bean) getApplicationContext();

            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(b.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final AllAPIs cr = retrofit.create(AllAPIs.class);

            Call<goLiveBean> call3 = cr.goLive(b.userId, s, "");

            call3.enqueue(new Callback<goLiveBean>() {
                @Override
                public void onResponse(Call<goLiveBean> call, retrofit2.Response<goLiveBean> response) {

                    if (Objects.equals(response.body().getStatus(), "1")) {
                        //Toast.makeText(LiveScreen.this, "You are now live", Toast.LENGTH_SHORT).show();
                        liveId = response.body().getData().getLiveId();

                        actions.setVisibility(View.VISIBLE);


                        Log.d("asdasd", liveId);
                        Log.d("asdasd", b.userId);


                        schedule(liveId);

                    } else {
                        //Toast.makeText(LiveScreen.this, "Error going on live", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    progress.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<goLiveBean> call, Throwable t) {
                    progress.setVisibility(View.GONE);
                }
            });

        }
    };


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

            final bean b = (bean) context.getApplicationContext();

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




                    Dialog dialog = new Dialog(IrisLive.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.connect_dialog);
                    dialog.setCancelable(true);
                    dialog.show();


                    CircleImageView image = (CircleImageView)dialog.findViewById(R.id.image);
                    TextView name = (TextView)dialog.findViewById(R.id.name);
                    Button follo = (Button)dialog.findViewById(R.id.follow);
                    Button connect = (Button)dialog.findViewById(R.id.connect);



                    ImageLoader loader1 = ImageLoader.getInstance();

                    loader1.displayImage(b.userImage , image);

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


                            retrofit2.Call<followBean> call = cr.follow(b.userId, item.getUserId());

                            call.enqueue(new retrofit2.Callback<followBean>() {
                                @Override
                                public void onResponse(retrofit2.Call<followBean> call, retrofit2.Response<followBean> response) {

                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                    progress.setVisibility(View.GONE);

                                }

                                @Override
                                public void onFailure(retrofit2.Call<followBean> call, Throwable t) {

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



                            Call<requestConnectionBean> call = cr.requestConnection(liveId , b.userId , item.getUserId());

                            call.enqueue(new Callback<requestConnectionBean>() {
                                @Override
                                public void onResponse(Call<requestConnectionBean> call, Response<requestConnectionBean> response) {



                                    playerLayout1.setVisibility(View.VISIBLE);



                                    progress.setVisibility(View.GONE);
                                }

                                @Override
                                public void onFailure(Call<requestConnectionBean> call, Throwable t) {
                                    progress.setVisibility(View.GONE);
                                }
                            });





                        }
                    });









                }
            });


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


    public void schedule(final String vid) {

        Log.d("hgfjhg", vid);

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


                Call<getUpdatedBean> call = cr.getUpdatedData(b.userId, vid);


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


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(Call<getUpdatedBean> call, Throwable t) {

                        Log.d("asdasd", t.toString());

                    }
                });




                Call<checkStatusBean> call1 = cr.checkStatus(b.userId , vid);

                call1.enqueue(new Callback<checkStatusBean>() {
                    @Override
                    public void onResponse(Call<checkStatusBean> call, Response<checkStatusBean> response) {


                        try {
                            if (uri.length() == 0)
                            {
                                uri = response.body().getData().get(0).getUrl();

                                if (uri.length() > 0)
                                {
                                    if (mBroadcastPlayer != null)
                                        mBroadcastPlayer.close();
                                    mBroadcastPlayer = new BroadcastPlayer(IrisLive.this, uri, APPLICATION_ID, mBroadcastPlayerObserver);
                                    mBroadcastPlayer.setSurfaceView(player1);
                                    mBroadcastPlayer.load();
                                }



                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }








                    }

                    @Override
                    public void onFailure(Call<checkStatusBean> call, Throwable t) {

                    }
                });





            }
        }, 0, 2000);

    }

    public void showGift(final int pos, String title) {

        giftLayout.setVisibility(View.VISIBLE);

        Glide.with(getApplicationContext()).load(gfts[pos]).into(giftIcon);
        giftTitle.setText(title);

        Timer t = new Timer();

        t.schedule(new TimerTask() {
            @Override
            public void run() {


                giftLayout.getHandler().post(new Runnable() {
                    public void run() {
                        giftLayout.setVisibility(View.GONE);
                    }
                });

            }
        }, 2500);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBroadcaster.onActivityDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = null;

        mBroadcaster.onActivityPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        mBroadcaster.setCameraSurface(mPreviewSurface);
        mBroadcaster.onActivityResume();
    }


    BroadcastPlayer.Observer mBroadcastPlayerObserver = new BroadcastPlayer.Observer() {
        @Override
        public void onStateChange(PlayerState playerState) {

        }
        @Override
        public void onBroadcastLoaded(boolean live, int width, int height) {
        }
    };
*/

}
