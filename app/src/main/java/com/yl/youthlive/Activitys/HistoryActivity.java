package com.yl.youthlive.Activitys;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yl.youthlive.Adapter.diamondPHAdapter;
import com.yl.youthlive.R;
import com.yl.youthlive.SharePreferenceUtils;
import com.yl.youthlive.bean;
import com.yl.youthlive.diamondpurchasehistoryPOJO.DiamondpurchaselistPOJO;
import com.yl.youthlive.diamondpurchasehistoryPOJO.Information;
import com.yl.youthlive.internetConnectivity.ConnectivityReceiver;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yl.youthlive.bean.getContext;

public class HistoryActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    diamondPHAdapter adapter;
    ProgressBar progress;
    RecyclerView recyclerView;
    GridLayoutManager manager;
    List<Information> list = new ArrayList<>();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        checkConnection();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("History");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        progress = (ProgressBar) findViewById(R.id.progress);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        manager = new GridLayoutManager(getContext(), 1);
        adapter = new diamondPHAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);

        Typeface typeFace = Typeface.MONOSPACE;
        ((TextView) toolbar.getChildAt(0)).setTypeface(typeFace);


    }

    @Override
    protected void onResume() {
        super.onResume();
        bean.getInstance().setConnectivityListener(this);
        loadData();
    }

    public void loadData() {
        progress.setVisibility(View.VISIBLE);
        final LinearLayout linearLayout = findViewById(R.id.nobroadcast);


        final bean b = (bean) getContext().getApplicationContext();
        Call<DiamondpurchaselistPOJO> call = b.getRetrofit().getdiamondpurchasehistory(Integer.valueOf(SharePreferenceUtils.getInstance().getString("userId")));

        Log.d("userId", SharePreferenceUtils.getInstance().getString("userId"));

        call.enqueue(new Callback<DiamondpurchaselistPOJO>() {
            @Override
            public void onResponse(Call<DiamondpurchaselistPOJO> call, Response<DiamondpurchaselistPOJO> response) {

                try {
                    if (!response.body().getInformation().isEmpty()) {
                        adapter.setGridData(response.body().getInformation());
                    } else {
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<DiamondpurchaselistPOJO> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }

        });
    }

    ////////////////////internet connectivity check///////////////
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
            //  Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
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
        showSnack(isConnected);

    }


}
