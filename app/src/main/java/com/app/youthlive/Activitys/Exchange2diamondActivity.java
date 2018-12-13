package com.app.youthlive.Activitys;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.youthlive.R;
import com.app.youthlive.SharePreferenceUtils;
import com.app.youthlive.bean;
import com.app.youthlive.loginResponsePOJO.loginResponseBean;
import com.app.youthlive.walletPOJO.walletBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Exchange2diamondActivity extends AppCompatActivity {

    Toolbar toolbar;
    int param = 15;
    ImageButton plus , minus;
    TextView display;
    int limit = 301;
    TextView diamondCount;
    ProgressBar progress;
    TextView coins;
    Button redeem;
    int diamm = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange2diamond);

        toolbar = findViewById(R.id.toolbar3);
        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);
        display = findViewById(R.id.display);
        diamondCount = findViewById(R.id.textView73);
        progress = findViewById(R.id.progressBar15);
        coins = findViewById(R.id.textView12);
        redeem = findViewById(R.id.button19);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Exchange to Diamonds");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Typeface typeFace = Typeface.MONOSPACE;
        ((TextView)toolbar.getChildAt(0)).setTypeface(typeFace);



        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int curr = Integer.parseInt(display.getText().toString());
                int nVal = curr + param;

                if (nVal > limit)
                {
                    Toast.makeText(Exchange2diamondActivity.this, "Max. value reached", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    display.setText(String.valueOf(nVal));
                    diamm = nVal / param;
                    diamondCount.setText(String.valueOf(nVal / param) + " Diamonds");
                }

            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int curr = Integer.parseInt(display.getText().toString());
                int nVal = curr - param;

                if (nVal < 0)
                {
                    Toast.makeText(Exchange2diamondActivity.this, "Min. value reached", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    diamm = nVal / param;
                    display.setText(String.valueOf(nVal));
                    diamondCount.setText(String.valueOf(nVal / param) + " Diamonds");
                }

            }
        });


        redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress.setVisibility(View.VISIBLE);


                final bean b = (bean) getApplicationContext();

                Call<loginResponseBean> call = b.getRetrofit().exchangeBeansToDiamond(SharePreferenceUtils.getInstance().getString("userId") , display.getText().toString() , String.valueOf(diamm));

                call.enqueue(new Callback<loginResponseBean>() {
                    @Override
                    public void onResponse(@NonNull Call<loginResponseBean> call, @NonNull Response<loginResponseBean> response) {

                        if (response.body().getStatus().equals("1"))
                        {
                            Toast.makeText(Exchange2diamondActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            getCoinsData();
                        }



                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(@NonNull Call<loginResponseBean> call, @NonNull Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getCoinsData();
    }

    public void getCoinsData() {
        progress.setVisibility(View.VISIBLE);


        final bean b = (bean) getApplicationContext();

        Call<walletBean> call = b.getRetrofit().getWalletData(SharePreferenceUtils.getInstance().getString("userId"));

        Log.d("userId", SharePreferenceUtils.getInstance().getString("userId"));

        call.enqueue(new Callback<walletBean>() {
            @Override
            public void onResponse(@NonNull Call<walletBean> call, @NonNull Response<walletBean> response) {

                try {
                    if (response.body() != null && !response.body().getData().getDiamond().isEmpty()) {
                        //  Toast.makeText(BuyDiamonds.this, "Purchase done", Toast.LENGTH_SHORT).show();
                        coins.setText(response.body().getData().getBeans());
                        limit = Integer.parseInt(response.body().getData().getBeans());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(@NonNull Call<walletBean> call, @NonNull Throwable t) {
                progress.setVisibility(View.GONE);
            }

        });
    }


}
