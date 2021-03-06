package com.app.youthlive.Activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.app.youthlive.MyVLOGs;
import com.app.youthlive.R;
import com.app.youthlive.bean;
import com.app.youthlive.internetConnectivity.ConnectivityReceiver;
import com.app.youthlive.loginResponsePOJO.CoverImage;
import com.app.youthlive.loginResponsePOJO.Data;
import com.app.youthlive.loginResponsePOJO.loginResponseBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;

public class MyVlog extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    static String userid;
    public Context appContext, myContext;
    public FragmentManager fm;
    Toolbar toolbar;
    ViewPager pager;
    ProgressBar progress;
    ImageView profile;
    TextView fans, emptymsg;
    TextView followings, friends;
    ViewPager coverPager;
    CircleIndicator indicator;
    LinearLayout followclick, fanclick, friendclick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vlog);
        checkConnection();
        toolbar = findViewById(R.id.toolbar);
        pager = findViewById(R.id.pager);
        progress = findViewById(R.id.progress);
        profile = findViewById(R.id.profileimg);
        followclick = findViewById(R.id.followings_click);
        fanclick = findViewById(R.id.fans_click);
        friendclick = findViewById(R.id.friends_click);
        emptymsg = findViewById(R.id.emptyvlogmsg);

        userid = getIntent().getStringExtra("userId");


        followclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyVlog.this, FollowingActivity.class);
                intent.putExtra("userId", userid);
                startActivity(intent);

            }
        });

        fanclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyVlog.this, FanActivity.class);
                intent.putExtra("userId", userid);
                startActivity(intent);

            }
        });

        friendclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyVlog.this, FriendActivity.class);
                intent.putExtra("userId", userid);
                startActivity(intent);

            }
        });


        coverPager = findViewById(R.id.cover_pager);
        indicator = findViewById(R.id.indicator);

        fans = findViewById(R.id.fans);
        followings = findViewById(R.id.followings);
        friends = findViewById(R.id.friends);

        appContext = getApplicationContext();
        myContext = this;
        fm = getSupportFragmentManager();

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

        toolbar.setTitle("My VLOGs");

        Typeface typeFace = Typeface.MONOSPACE;
        ((TextView)toolbar.getChildAt(1)).setTypeface(typeFace);


    }


    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        bean.getInstance().setConnectivityListener(this);
        loadData();


    }

    ///////////////////internet connectivity check///////////////
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showAlert(isConnected);
    }

    private void showAlert(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {

            //    message = "Good! Connected to Internet";
            //    color = Color.WHITE;
        } else {
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
        showAlert(isConnected);

    }


    public void loadData() {


        progress.setVisibility(View.VISIBLE);

        bean b = (bean) appContext;

        Call<loginResponseBean> call = b.getRetrofit().getProfile(userid);

        call.enqueue(new retrofit2.Callback<loginResponseBean>() {
            @Override
            public void onResponse(Call<loginResponseBean> call, retrofit2.Response<loginResponseBean> response) {


                if (Objects.equals(response.body().getStatus(), "1")) {

                    CoverPager pageAdapter = new CoverPager(fm, response.body().getData().getCoverImage());
                    coverPager.setAdapter(pageAdapter);
                    indicator.setViewPager(coverPager);


                    ImageLoader loader = ImageLoader.getInstance();
                    loader.displayImage(response.body().getData().getUserImage(), profile);

                    toolbar.setTitle(response.body().getData().getUserName());
                    toolbar.setSubtitle(Html.fromHtml("Youth Live ID: <b>" + response.body().getData().getYouthLiveId() + "</b>"));

                    Typeface typeFace = Typeface.MONOSPACE;
                    ((TextView)toolbar.getChildAt(3)).setTypeface(typeFace);


                    fans.setText(String.valueOf(response.body().getData().getFans()));
                    followings.setText(String.valueOf(response.body().getData().getFollowings()));
                    friends.setText(String.valueOf(response.body().getData().getFriends()));

                    FragStatePAgerAdapter adapter = new FragStatePAgerAdapter(fm, response.body().getData());
                    pager.setAdapter(adapter);


                } else {
                    Toast.makeText(myContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }


                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<loginResponseBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


    }

    public class CoverPager extends FragmentStatePagerAdapter {

        List<CoverImage> list = new ArrayList<>();

        public CoverPager(FragmentManager fm, List<com.app.youthlive.loginResponsePOJO.CoverImage> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            com.app.youthlive.CoverImage frag = new com.app.youthlive.CoverImage();
            Bundle b = new Bundle();
            b.putString("url", list.get(position).getImage());
            frag.setArguments(b);
            return frag;
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }


    /*public class CoverImage extends Fragment
    {

        String url;
        ImageView image;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.cober_image_layout , container , false);

            url = getArguments().getString("url");
            image = (ImageView)view.findViewById(R.id.image);

            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(url , image);


            return  view;
        }
    }*/


    public class FragStatePAgerAdapter extends FragmentStatePagerAdapter {

        Data data;

        public FragStatePAgerAdapter(FragmentManager fm, Data data) {
            super(fm);
            this.data = data;
        }

        @Override
        public Fragment getItem(int position) {

            return new MyVLOGs();

        }

        @Override
        public int getCount() {
            return 1;
        }

    }


}
