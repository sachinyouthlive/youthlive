package com.yl.youthlive.Activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.youthlive.Address;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.R;
import com.yl.youthlive.bean;
import com.yl.youthlive.followPOJO.followBean;
import com.yl.youthlive.internetConnectivity.ConnectivityReceiver;
import com.yl.youthlive.loginResponsePOJO.Data;
import com.yl.youthlive.loginResponsePOJO.loginResponseBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PersonalInfo extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    Toolbar toolbar;
    ViewPager pager;
    ProgressBar progress;
    CircleImageView profile;
    ImageView profileimg;
    // public static ArrayList<String> mylist = new ArrayList<String>();
    String userid;
    String allItems, allItem;
    TextView followings, friends, fans;

    ViewPager coverPager;
    CircleIndicator indicator;

    Button follow;

    LinearLayout followingClick, fanClick, friendClick;

    public Context appContext, myContext;
    public FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info_layout);
        checkConnection();

        toolbar = findViewById(R.id.toolbar);
        pager = findViewById(R.id.pager);
        progress = findViewById(R.id.progress);
        profile = findViewById(R.id.profile);

        follow = (Button)findViewById(R.id.follow);
        profileimg = findViewById(R.id.profileimg);
        final bean b = (bean) getApplicationContext();

        String useridd = getIntent().getStringExtra("userId");
        ///////////////
        b.mylist.add(useridd);
        userid = b.mylist.get(b.mylist.size() - 1);
        /////////////////////
        followingClick = (LinearLayout)findViewById(R.id.followings_click);
        fanClick = (LinearLayout) findViewById(R.id.fans_click);
        friendClick = findViewById(R.id.friends_click);





        if (Objects.equals(userid, b.userId))
        {
            follow.setVisibility(View.GONE);
        }
        else
        {
            follow.setVisibility(View.VISIBLE);

        }


        followingClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PersonalInfo.this, FollowingActivity.class);
                intent.putExtra("userId", userid);
                startActivity(intent);
                //finish();

            }
        });

        fanClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PersonalInfo.this, FanActivity.class);
                intent.putExtra("userId", userid);
                startActivity(intent);
                //finish();

            }
        });
        friendClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PersonalInfo.this, FriendActivity.class);
                intent.putExtra("userId", userid);
                startActivity(intent);
                // finish();

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


                Call<followBean> call = cr.follow(b.userId , userid);

                call.enqueue(new Callback<followBean>() {
                    @Override
                    public void onResponse(Call<followBean> call, Response<followBean> response) {

                        try {

                            if (response.body().getMessage().equals("Follow Success")) {
                                follow.setText("UNFOLLOW");
                                follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.minus, 0, 0, 0);
                                Toast.makeText(PersonalInfo.this, "You started to follow " + toolbar.getTitle().toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (response.body().getMessage().equals("Unfollow Success")) {
                                follow.setText("FOLLOW");
                                follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.plus_white, 0, 0, 0);
                                Toast.makeText(PersonalInfo.this, "You Unfollowed " + toolbar.getTitle().toString(), Toast.LENGTH_SHORT).show();
                            }
                            loadData();
//test
                            progress.setVisibility(View.GONE);
                        } catch (Exception e) {
                            progress.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<followBean> call, Throwable t) {

                        progress.setVisibility(View.GONE);

                    }
                });


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
                // b.mylist.remove(b.mylist.size() - 1);

                finish();
            }
        });


     //   tabs.addTab(tabs.newTab().setText("Personal"));
     //   tabs.addTab(tabs.newTab().setText("Education"));
     //   tabs.addTab(tabs.newTab().setText("Career"));


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        bean b = (bean) appContext;
        if (b.mylist.size() > 1) {
            b.mylist.remove(b.mylist.size() - 1);
        }

    }

    @Override
    protected void onResume() {
     // register connection status listener

        super.onResume();
        loadfollowstatus(userid);
        bean.getInstance().setConnectivityListener(this);
        loadData();


    }
    ///////////////////internet connectivity check///////////////
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
            //Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
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


    public void loadData() {


        progress.setVisibility(View.VISIBLE);

        bean b = (bean) appContext;

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<loginResponseBean> call = cr.getProfile(userid);

        call.enqueue(new retrofit2.Callback<loginResponseBean>() {
            @Override
            public void onResponse(Call<loginResponseBean> call, retrofit2.Response<loginResponseBean> response) {


                if (Objects.equals(response.body().getStatus(), "1")) {

                    CoverPager pageAdapter = new CoverPager(fm, response.body().getData().getCoverImage());
                    coverPager.setAdapter(pageAdapter);
                    indicator.setViewPager(coverPager);


                    ImageLoader loader = ImageLoader.getInstance();
                    loader.displayImage(response.body().getData().getUserImage(), profile);
                    loader.displayImage(response.body().getData().getUserImage(), profileimg);

                    toolbar.setTitle(response.body().getData().getUserName());

                    fans.setText(String.valueOf(response.body().getData().getFans()));
                    followings.setText(String.valueOf(response.body().getData().getFollowings()));
                    friends.setText(String.valueOf(response.body().getData().getFriends()));

                    FragStatePAgerAdapter adapter = new FragStatePAgerAdapter(fm, response.body().getData());
                    pager.setAdapter(adapter);


                 //   tabs.setupWithViewPager(pager);


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

        List<com.yl.youthlive.loginResponsePOJO.CoverImage> list = new ArrayList<>();

        public CoverPager(FragmentManager fm, List<com.yl.youthlive.loginResponsePOJO.CoverImage> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            com.yl.youthlive.CoverImage frag = new com.yl.youthlive.CoverImage();
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
        String title[] = {"Personal"};

        public FragStatePAgerAdapter(FragmentManager fm, Data data) {
            super(fm);
            this.data = data;
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                Address frag = new Address();
                Bundle b = new Bundle();

                b.putString("userId", data.getUserId());
                b.putString("phone", data.getPhone());
                b.putString("user", data.getUserName());
                b.putString("youth", data.getYouthLiveId());
                b.putString("gender", data.getGender());
                b.putString("birth", data.getBirthday());
                b.putString("bio", data.getBio());


                frag.setArguments(b);

                return frag;
            }

          else {
                return null;
            }


        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }

    public void loadfollowstatus(String userids) {
        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<followBean> call = cr.followcheck(b.userId, userids);

        call.enqueue(new Callback<followBean>() {
            @Override
            public void onResponse(Call<followBean> call, Response<followBean> response) {

                try {
                    // Toast.makeText(TimelineProfile.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();
                    if (response.body().getMessage().equals("Following")) {
                        follow.setText("UNFOLLOW");
                        follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.minus, 0, 0, 0);
                        //Toast.makeText(PersonalInfo.this, "You started to follow " + toolbar.getTitle().toString(), Toast.LENGTH_SHORT).show();

                    }
                    if (response.body().getMessage().equals("Not Following")) {
                        follow.setText("FOLLOW");
                        follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.plus, 0, 0, 0);
                        // Toast.makeText(PersonalInfo.this, "You started to notfollow " + toolbar.getTitle().toString(), Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    // Toast.makeText(PersonalInfo.this, "Some Error Occurred, Please try again follow", Toast.LENGTH_SHORT).show();
                }


                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<followBean> call, Throwable t) {

                progress.setVisibility(View.GONE);

            }
        });


    }

}
