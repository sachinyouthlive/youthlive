package com.yl.youthlive.checkin;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.R;
import com.yl.youthlive.SharePreferenceUtils;
import com.yl.youthlive.bean;
import com.yl.youthlive.checkinPOJO.CheckinPOJO;
import com.yl.youthlive.checkinPOJO.Information;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.yl.youthlive.bean.getContext;

public class CheckInDetailActivity extends AppCompatActivity {

    private static final String TAG = CheckInDetailActivity.class.getSimpleName();
    TextView textView;
    CheckinAdapter adapter;
    ProgressBar progress;
    RecyclerView recyclerView;
    GridLayoutManager manager;
    List<Information> list = new ArrayList<>();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        progress = (ProgressBar) findViewById(R.id.progress);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        manager = new GridLayoutManager(getContext(), 1);
        adapter = new CheckinAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public void loadData() {
        progress.setVisibility(View.VISIBLE);
        final LinearLayout linearLayout = findViewById(R.id.nobroadcast);

        Bundle bundle = getIntent().getExtras();
        String day = (bundle.getString("position"));
        int month = getIntent().getIntExtra("month", 6);
        final int monthFinal = month + 1;

        final bean b = (bean) getContext().getApplicationContext();

        Call<CheckinPOJO> call = b.getRetrofit().getcheckin(SharePreferenceUtils.getInstance().getString("userId"), day, String.valueOf(monthFinal));

        Log.d("userId", SharePreferenceUtils.getInstance().getString("userId"));

        call.enqueue(new Callback<CheckinPOJO>() {
            @Override
            public void onResponse(Call<CheckinPOJO> call, Response<CheckinPOJO> response) {

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
            public void onFailure(Call<CheckinPOJO> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }

        });
    }


}
