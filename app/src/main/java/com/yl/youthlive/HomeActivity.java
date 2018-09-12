package com.yl.youthlive;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.yl.youthlive.Activitys.SearchActivity;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.addVideoPOJO.addVideoBean;
import com.yl.youthlive.feedBackPOJO.feedBackBean;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Random;

import io.github.memfis19.annca.Annca;
import io.github.memfis19.annca.internal.configuration.AnncaConfiguration;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, PlayerFragment1.endListener {


    public final int MEDIA_TYPE_VIDEO = 2;
    public final int REQUEST_IMAGE_CAPTURE = 1;
    public final int GALLEY_REQUEST_CODE_CUSTOMER = 10;
    final int REQUEST_VIDEO_CAPTURE = 1;
    private final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    //AHBottomNavigation bottom;
    DrawerLayout drawer;
    Toolbar toolbar;
    TextView name, logout;
    ProgressBar videoProgress;

    //AHBottomNavigation bottom;
    Bitmap bitmap;
    TextView feedBack;
    private Uri realUri;
    private Uri fileUri;
    private GoogleApiClient mGoogleApiClient;
    private SharedPreferences pref;
    private SharedPreferences.Editor edit;
    private FragmentRefreshListener fragmentRefreshListener;

    /// for fragment refresh on new vlog add
    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }
    ////////////////


    SharedPreferences offlinePref;
    SharedPreferences.Editor offlineEdit;

    public String fragTag;


    ImageView live, vlog, golive, channel, profile;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_home);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        edit = pref.edit();

        offlinePref = getSharedPreferences("offline", Context.MODE_PRIVATE);
        offlineEdit = offlinePref.edit();

        feedBack = (TextView) findViewById(R.id.feedback);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        name = (TextView) findViewById(R.id.name);
        logout = (TextView) findViewById(R.id.logout);

        videoProgress = (ProgressBar) findViewById(R.id.video_progress);

        drawer = (DrawerLayout) findViewById(R.id.container);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        live = findViewById(R.id.imageView);
        vlog = findViewById(R.id.imageView2);
        golive = findViewById(R.id.imageView19);
        channel = findViewById(R.id.imageView20);
        profile = findViewById(R.id.imageView21);





        /*String offline = offlinePref.getString("offline" , "");

        final String liveId = offlinePref.getString("liveId" , "");

        if (offline.length() > 0 && liveId.length() > 0)
        {


            final Dialog dialog = new Dialog(HomeActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.offline_sync_dialog);
            dialog.show();




            bean b = (bean)getApplicationContext();

            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(b.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final AllAPIs cr = retrofit.create(AllAPIs.class);


            Call<endLiveBean> call = cr.syncLive(offline , liveId);

            call.enqueue(new Callback<endLiveBean>() {
                @Override
                public void onResponse(Call<endLiveBean> call, Response<endLiveBean> response) {


                    if (response.body().getStatus().equals("1"))
                    {

                        offlineEdit.remove("offline");
                        offlineEdit.remove("liveId");
                        offlineEdit.apply();

                        dialog.dismiss();

                    }



                }

                @Override
                public void onFailure(Call<endLiveBean> call, Throwable t) {

                }
            });






        }*/


        live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toolbar.setTitle("Live Users");


                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStackImmediate();
                }

                Live frag1 = new Live();
                frag1.setHomeActivity(HomeActivity.this);
                ft.replace(R.id.replace, frag1);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.commit();

                live.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                vlog.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ababab")));
                channel.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ababab")));
                profile.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ababab")));


            }
        });

        vlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbar.setTitle("Vlog");


                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();

                while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStackImmediate();
                }

                Vlog frag3 = new Vlog();
                ft2.replace(R.id.replace, frag3);
                ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft2.commit();
                live.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ababab")));
                vlog.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                channel.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ababab")));
                profile.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ababab")));

            }
        });

        golive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbar.setTitle("Go Live");


                FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();

                while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStackImmediate();
                }

                GoLiveFrag frag2 = new GoLiveFrag();
                ft1.replace(R.id.replace, frag2);
                ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft1.commit();
                live.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ababab")));
                vlog.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ababab")));
                channel.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ababab")));
                profile.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ababab")));

            }
        });


        channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbar.setTitle("Channels");


                FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();

                while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStackImmediate();
                }

                Channel frag4 = new Channel();
                ft3.replace(R.id.replace, frag4);
                ft3.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft3.commit();
                live.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ababab")));
                vlog.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ababab")));
                channel.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                profile.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ababab")));

            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbar.setTitle("Profile");

                FragmentTransaction ft5 = getSupportFragmentManager().beginTransaction();

                while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStackImmediate();
                }

                Profile frag5 = new Profile();
                ft5.replace(R.id.replace, frag5);
                ft5.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft5.commit();


                live.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ababab")));
                vlog.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ababab")));
                channel.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ababab")));
                profile.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));


            }
        });

        /*bottom = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem item1 =
                new AHBottomNavigationItem("" , R.drawable.live);

        AHBottomNavigationItem item2 =
                new AHBottomNavigationItem("", R.drawable.icon);

        AHBottomNavigationItem item3 =
                new AHBottomNavigationItem("", R.drawable.eye);

        AHBottomNavigationItem item4 =
                new AHBottomNavigationItem("", R.drawable.timeline);

        AHBottomNavigationItem item5 =
                new AHBottomNavigationItem("", R.drawable.user2);


        bottom.addItem(item1);
        bottom.addItem(item2);
        bottom.addItem(item3);
        bottom.addItem(item4);
        bottom.addItem(item5);

        bottom.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

        bottom.setDefaultBackgroundColor(Color.parseColor("#ccffffff"));
        bottom.setAccentColor(Color.parseColor("#E91E63"));
        bottom.setInactiveColor(Color.parseColor("#333333"));*/


        feedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("asdasd", "asdasd");

                final Dialog dialog = new Dialog(HomeActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_feed_back);
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


                            Call<feedBackBean> call = cr.feedback(SharePreferenceUtils.getInstance().getString("userId"), com);

                            call.enqueue(new Callback<feedBackBean>() {
                                @Override
                                public void onResponse(Call<feedBackBean> call, retrofit2.Response<feedBackBean> response) {

                                    try {
                                        Toast.makeText(HomeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    dialog.dismiss();

                                    progressBar.setVisibility(View.GONE);

                                }

                                @Override
                                public void onFailure(Call<feedBackBean> call, Throwable t) {
                                    progressBar.setVisibility(View.GONE);
                                }
                            });

                        } else {
                            Toast.makeText(HomeActivity.this, "Please Enter a Comment", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }

            }
        });


        /*bottom.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                switch (position) {
                    case 0:
                        toolbar.setTitle("Live Users");


                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStackImmediate();
                        }

                        Live frag1 = new Live();
                        frag1.setHomeActivity(HomeActivity.this);
                        ft.replace(R.id.replace, frag1);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        ft.commit();

                        return true;
                    case 1:

                        toolbar.setTitle("Vlog");

                        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();

                        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStackImmediate();
                        }

                        Vlog frag3 = new Vlog();
                        ft2.replace(R.id.replace, frag3);
                        ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        ft2.commit();

                        return true;
                    case 2:

                        toolbar.setTitle("Go Live");


                        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();

                        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStackImmediate();
                        }

                        GoLiveFrag frag2 = new GoLiveFrag();
                        ft1.replace(R.id.replace, frag2);
                        ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        ft1.commit();


                        return true;
                    case 3:

                        toolbar.setTitle("Timeline");

                        FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();

                        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStackImmediate();
                        }

                        Timeline frag4 = new Timeline();
                        ft3.replace(R.id.replace, frag4);
                        ft3.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        ft3.commit();

                        return true;
                    case 4:

                        toolbar.setTitle("Profile");

                        FragmentTransaction ft5 = getSupportFragmentManager().beginTransaction();

                        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStackImmediate();
                        }

                        Profile frag5 = new Profile();
                        ft5.replace(R.id.replace, frag5);
                        ft5.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        ft5.commit();

                        return true;
                }

                return false;
            }
        });*/


        buildGoogleApiClient();

        bean b = (bean) getApplicationContext();

        name.setText("Hi, " + b.userName);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mGoogleApiClient.isConnected()) {
                    signOut();
                }

                LoginManager.getInstance().logOut();

                edit.remove("type");
                edit.remove("user");
                edit.remove("pass");
                edit.apply();
                Intent i = new Intent(getApplicationContext(), Spalsh2.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();

            }
        });

        toolbar.setTitle("Live Users");

        Typeface typeFace = Typeface.MONOSPACE;
        ((TextView) toolbar.getChildAt(1)).setTypeface(typeFace);


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Live frag1 = new Live();
        frag1.setHomeActivity(HomeActivity.this);
        ft.replace(R.id.replace, frag1);
        //ft.addToBackStack(null);
        ft.commit();

        live.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
        vlog.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ababab")));
        channel.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ababab")));
        profile.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ababab")));


    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @SuppressLint("CommitPrefEdits")
                    @Override
                    public void onResult(@NonNull Status status) {


                        if (status.isSuccess()) {

                            edit.remove("type");
                            edit.remove("user");
                            edit.remove("pass");
                            edit.apply();
                            Intent i = new Intent(getApplicationContext(), Spalsh2.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            overridePendingTransition(0, 0);
                            finish();

                        }


                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.notifications:

                break;
            case R.id.action_search:

                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
                HomeActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


                break;
            case R.id.video:
                /*Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);*/

                AnncaConfiguration.Builder videoLimited = new AnncaConfiguration.Builder(HomeActivity.this, REQUEST_VIDEO_CAPTURE);
                videoLimited.setMediaAction(AnncaConfiguration.MEDIA_ACTION_VIDEO);
                videoLimited.setMediaQuality(AnncaConfiguration.MEDIA_QUALITY_AUTO);
                videoLimited.setVideoFileSize(2 * 1024 * 1024);
                videoLimited.setMinimumVideoDuration(15 * 1000);
                videoLimited.setVideoDuration(15 * 1000);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return false;
                }
                new Annca(videoLimited.build()).launchCamera();

                /*Log.d("jg", "video");

                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                takeVideoIntent.putExtra(android.provider.MediaStore.EXTRA_VIDEO_QUALITY, 0);
                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                }*/

                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //dialog =ProgressDialog.show(Activity.this, "", "loading...",false,true);
        try {
            if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
                String filePath = data.getStringExtra(AnncaConfiguration.Arguments.FILE_PATH);
                uploadVideo(filePath);
            } else if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    realUri = Uri.parse(fileUri.getPath());
                    //imgPostServices();

                    //uploadVideo(realUri);

                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(HomeActivity.this, "User cancelled the video capture.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(HomeActivity.this, "Video capture failed.", Toast.LENGTH_LONG).show();

                }
            } else if (resultCode == RESULT_OK && requestCode == GALLEY_REQUEST_CODE_CUSTOMER) {
                if (data.getData() != null) {
                    try {
                        Log.d("TAG", "not cust");
                        realUri = data.getData();
                        // Get real path to make File
                        realUri = Uri.parse(getPath(HomeActivity.this, data.getData()));
                        bitmap = BitmapFactory.decodeFile(realUri.getPath());
                        bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
                        //imgPostServices();

                        //uploadVideo(realUri);

                        //Log.d(TAG, "Image path :- " + realUri);
                        //   ivProfileEditProfile.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        Log.e("TAG", e.getMessage());
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "plaese select another image", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private String getPath(Uri uri) throws Exception {
        // this method will be used to get real path of Image chosen from gallery.
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = MainActivity.this.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }*/

    private TypedFile makeFile(String uri) {
        // this will make file which is required by Retrofit.
        File file = new File(uri);
//        File photo = new File(file,  "Pic.image");
        TypedFile typedFile = new TypedFile("", file);
        Log.d("show", String.valueOf(file));
        Log.d("show", String.valueOf(uri));
        return typedFile;
    }
    //video uploading here.,.............................................
    /** Create a file Uri for saving an image or video */
    /**
     * Create a File for saving an image or video
     */
    private Uri getOutputMediaFileUri(int type) {
     /*    mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
      mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
       mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
       mediaRecorder.setVideoEncodingBitRate(690000);
       mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
       mediaRecorder.setVideoFrameRate(30);
        mediaRecorder.setVideoSize(640, 480);*/
        return Uri.fromFile(getOutputMediaFile(type));

    }

    private File getOutputMediaFile(int type) {
        // Check that the SDCard is mounted
        //   File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "JUMP Video");
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/youyhlive/" + "/Send Video/");
        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                // Toast.makeText(ActivityContext, "Failed to create directory MyCameraVideo.", Toast.LENGTH_LONG).show();
                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }
        // Create a media file name
        // For unique file name appending current timeStamp with file name
        java.util.Date date = new java.util.Date();
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(date.getTime());
        File mediaFile;
        if (type == MEDIA_TYPE_VIDEO) {
            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");

        } else {
            return null;
        }
        return mediaFile;
    }


    private MultipartTypedOutput attechPostImg() {

        Log.d("video test", "" + realUri.toString());
        MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
        try {
            multipartTypedOutput.addPart("userId", new TypedString("94"));
            // multipartTypedOutput.addPart("date",new TypedString(formattedDate));
            // multipartTypedOutput.addPart("photo",makeFile(realUri.toString()));
            //  multipartTypedOutput.addPart("post",makeFile(realUri.toString()));
            multipartTypedOutput.addPart("video", makeFile(realUri.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return multipartTypedOutput;
    }

    public void uploadVideo(String uri) {
        videoProgress.setVisibility(View.VISIBLE);
        MultipartBody.Part body = null;
        //String mCurrentPhotoPath = getPath(HomeActivity.this, uri);

        File file = new File(uri);


        RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        body = MultipartBody.Part.createFormData("video", file.getName(), reqFile);

        final bean b = (bean) getApplicationContext();


        Bitmap bMap = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);

        File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "thumb" + String.valueOf(new Random(100)) + ".jpg");
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file2));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bMap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        RequestBody reqFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
        MultipartBody.Part body2 = null;
        body2 = MultipartBody.Part.createFormData("image", file2.getName(), reqFile2);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);

        Call<addVideoBean> call = cr.addVideo(SharePreferenceUtils.getInstance().getString("userId"), "", "", body, body2);

        call.enqueue(new Callback<addVideoBean>() {
            @Override
            public void onResponse(Call<addVideoBean> call, retrofit2.Response<addVideoBean> response) {


                if (getFragmentRefreshListener() != null) {
                    videoProgress.setVisibility(View.VISIBLE);

                try {
                    getFragmentRefreshListener().onRefresh();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                }

                Toast.makeText(HomeActivity.this, "Video Uploaded Successfully", Toast.LENGTH_SHORT).show();
                videoProgress.setVisibility(View.GONE);
                Log.d("status", response.body().getStatus());
                Log.d("message", response.body().getMessage());
            }

            @Override
            public void onFailure(Call<addVideoBean> call, Throwable t) {
                videoProgress.setVisibility(View.GONE);
            }
        });
    }

    private String getPath(final Context context, final Uri uri) {
        final boolean isKitKatOrAbove = true;

        // DocumentProvider
        if (isKitKatOrAbove && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private synchronized void buildGoogleApiClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void removeFrag()
    {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onBackPressed() {

        bean b = (bean)getApplicationContext();

        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            removeFrag();
        }
        else
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(Html.fromHtml("<font color='#282828'>Are you sure, You want to Exit?</font>"));
            alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#282828'>Yes</font>"),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    });

            alertDialogBuilder.setNegativeButton(Html.fromHtml("<font color='#282828'>No</font>"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();

                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }





    }


    @Override
    public void onEndListener(String image, String timelineId, String timelineName, String liveTime, String viewers) {

/*

        VideoPlayerFragment f = (VideoPlayerFragment) getSupportFragmentManager().findFragmentByTag(fragTag);

        f.onEndListener(image , timelineId , timelineName , liveTime , viewers);

*/

    }

    public interface OnsearchListener {
        public void search(String searchtext);
    }

    public interface FragmentRefreshListener {
        void onRefresh();
    }

}
