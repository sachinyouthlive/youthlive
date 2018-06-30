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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yl.youthlive.Adapter.Search_adapter;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.R;
import com.yl.youthlive.bean;
import com.yl.youthlive.internetConnectivity.ConnectivityReceiver;
import com.yl.youthlive.searchlistPOJO.Datum;
import com.yl.youthlive.searchlistPOJO.SearchListPOJO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SearchActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    public ProgressBar progress;
    RecyclerView recycler_searchlist;
    Search_adapter recAdapter;
    LinearLayoutManager layoutmanager;
    Toolbar toolbar;
    List<Datum> list;
    TextView emptylistmsg;
    String userId;
    EditText searchtext;
    ImageView searchbtn;
    TextView umayknow_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        checkConnection();

        list = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progress = (ProgressBar) findViewById(R.id.progress);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchtext = findViewById(R.id.search_text);

        recycler_searchlist = (RecyclerView) findViewById(R.id.recycler_search);
        layoutmanager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recAdapter = new Search_adapter(this, list);
        recycler_searchlist.setLayoutManager(layoutmanager);
        recycler_searchlist.setAdapter(recAdapter);
        recycler_searchlist.setHasFixedSize(true);
        emptylistmsg = findViewById(R.id.nomatchfound);
        umayknow_txt = findViewById(R.id.umayknw_txt);

        searchbtn = findViewById(R.id.searchbtn);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!searchtext.getText().toString().equals("")) {
                    loadData();
                } else {
                    Toast.makeText(SearchActivity.this, "Enter valid username/ID to search", Toast.LENGTH_SHORT).show();
                    searchtext.setText("");
                }
            }
        });
        searchtext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    loadData();
                    return true;
                }
                return false;
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        bean.getInstance().setConnectivityListener(this);
        loadData_umaylike();


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

            //Toast.makeText(this, "Good! Connected to Internet", Toast.LENGTH_SHORT).show();
            //    message = "Good! Connected to Internet";
            //    color = Color.WHITE;
        } else {
            // Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
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
        emptylistmsg.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);

        String search = searchtext.getText().toString();
        Call<SearchListPOJO> call = cr.getSearchList(search);

        call.enqueue(new Callback<SearchListPOJO>() {
            @Override
            public void onResponse(Call<SearchListPOJO> call, Response<SearchListPOJO> response) {
                umayknow_txt.setVisibility(View.GONE);
                if (response.body().getData().isEmpty()) {
                    emptylistmsg.setVisibility(View.VISIBLE);
                }

                recAdapter.setGridData(response.body().getData());

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<SearchListPOJO> call, Throwable t) {

                progress.setVisibility(View.GONE);

            }
        });
    }

    public void loadData_umaylike() {
        emptylistmsg.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);

        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);

        String search = "sachin";
        Call<SearchListPOJO> call = cr.getSearchList(search);

        call.enqueue(new Callback<SearchListPOJO>() {
            @Override
            public void onResponse(Call<SearchListPOJO> call, Response<SearchListPOJO> response) {
                umayknow_txt.setVisibility(View.VISIBLE);
                if (response.body().getData().isEmpty()) {
                    emptylistmsg.setVisibility(View.VISIBLE);
                }

                recAdapter.setGridData(response.body().getData());

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<SearchListPOJO> call, Throwable t) {

                progress.setVisibility(View.GONE);

            }
        });
    }


}
