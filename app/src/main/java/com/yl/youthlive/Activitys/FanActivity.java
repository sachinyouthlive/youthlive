package com.yl.youthlive.Activitys;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
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

import com.yl.youthlive.Adapter.fan_adapter;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.R;
import com.yl.youthlive.bean;
import com.yl.youthlive.fan_listPOJO.Datum;
import com.yl.youthlive.fan_listPOJO.FanListPOJO;
import com.yl.youthlive.internetConnectivity.ConnectivityReceiver;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FanActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    public ProgressBar progress;
    RecyclerView recyclerfan;
    fan_adapter recAdapter;
    LinearLayoutManager layoutmanager;
    Toolbar toolbar;
    List<Datum> list;
    TextView nofanmsg;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan);
        checkConnection();
        userId = getIntent().getStringExtra("userId");
        list = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progress = (ProgressBar) findViewById(R.id.progress);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);

        toolbar.setTitle("Fans");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerfan = findViewById(R.id.recycler_fan);
        layoutmanager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recAdapter = new fan_adapter(this, list);
        recyclerfan.setLayoutManager(layoutmanager);
        recyclerfan.setAdapter(recAdapter);
        recyclerfan.setHasFixedSize(true);
        nofanmsg = findViewById(R.id.nofanmsg);

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

        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<FanListPOJO> call = cr.fanList(userId);

        call.enqueue(new Callback<FanListPOJO>() {
            @Override
            public void onResponse(Call<FanListPOJO> call, Response<FanListPOJO> response) {

                if (response.body().getData().isEmpty()) {
                    nofanmsg.setVisibility(View.VISIBLE);
                }

                recAdapter.setGridData(response.body().getData());

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<FanListPOJO> call, Throwable t) {

                progress.setVisibility(View.GONE);

            }
        });
    }


}
