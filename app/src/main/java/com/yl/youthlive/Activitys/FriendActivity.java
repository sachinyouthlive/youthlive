package com.yl.youthlive.Activitys;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yl.youthlive.Adapter.friend_adapter;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.R;
import com.yl.youthlive.bean;
import com.yl.youthlive.friendListPOJO.Datum;
import com.yl.youthlive.friendListPOJO.FriendListPOJO;
import com.yl.youthlive.internetConnectivity.ConnectivityReceiver;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FriendActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    public ProgressBar progress;
    RecyclerView recycler_following;
    friend_adapter recAdapter;
    LinearLayoutManager layoutmanager;
    Toolbar toolbar;
    List<Datum> list;
    TextView nofriendmsg;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        checkConnection();
        userId = getIntent().getStringExtra("userId");
        if (userId == null) {
            final bean b = (bean) getApplicationContext();
            userId = b.userId;
        }
        list = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progress = (ProgressBar) findViewById(R.id.progress);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);

        toolbar.setTitle("Friends");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recycler_following = (RecyclerView) findViewById(R.id.recycler_following);
        layoutmanager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recAdapter = new friend_adapter(this, list);
        recycler_following.setLayoutManager(layoutmanager);
        recycler_following.setAdapter(recAdapter);
        recycler_following.setHasFixedSize(true);
        nofriendmsg = findViewById(R.id.nofriendmsg);


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

        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<FriendListPOJO> call = cr.friendList(userId);

        call.enqueue(new Callback<FriendListPOJO>() {
            @Override
            public void onResponse(Call<FriendListPOJO> call, Response<FriendListPOJO> response) {

                if (response.body().getData().isEmpty()) {
                    nofriendmsg.setVisibility(View.VISIBLE);
                }

                recAdapter.setGridData(response.body().getData());

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<FriendListPOJO> call, Throwable t) {

                progress.setVisibility(View.GONE);

            }
        });
    }


}