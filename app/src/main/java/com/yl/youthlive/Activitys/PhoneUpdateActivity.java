package com.yl.youthlive.Activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.Login;
import com.yl.youthlive.PhoneupdateminiPOJO;
import com.yl.youthlive.R;
import com.yl.youthlive.bean;
import com.yl.youthlive.updatephonePOJO.UpdatephonePOJO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.yl.youthlive.bean.getContext;

public class PhoneUpdateActivity extends AppCompatActivity {
    Button verify;
    EditText otptxt;
    ProgressBar progress;
    Toolbar toolbar;

    private SharedPreferences pref;
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_update);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);

        toolbar.setTitle("Verify Phone Number");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        progress = findViewById(R.id.progressb);
        otptxt = findViewById(R.id.otptxt);
        verify = findViewById(R.id.verifybtn);
        pref = getContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        edit = pref.edit();
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                progress.setVisibility(View.VISIBLE);
                String otp = getIntent().getStringExtra("otp");

                if (otp.equals(otptxt.getText().toString())) {
                    String pho = getIntent().getStringExtra("phoneno");
                    final bean b = (bean) getContext().getApplicationContext();

                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);

                    Call<PhoneupdateminiPOJO> call = cr.updatePhonemini(b.userId, pho);

                    call.enqueue(new Callback<PhoneupdateminiPOJO>() {
                        @Override
                        public void onResponse(Call<PhoneupdateminiPOJO> call, Response<PhoneupdateminiPOJO> response) {

                            //      if (Objects.equals(response.body().getMessage(), "1")) {
                            //         Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            //    if (mGoogleApiClient.isConnected()) {
                            //       dialog.dismiss();
                            //        signOut();
                            //    }

                            Log.d("sachin", "response");

                            //   LoginManager.getInstance().logOut();

                            edit.remove("type");
                            edit.remove("user");
                            edit.remove("pass");
                            edit.apply();

                            Toast.makeText(getContext(), "Mobile Number Updated, Login with Updated Mobile Number", Toast.LENGTH_LONG).show();

                            Intent i = new Intent(getContext(), Login.class);
                            startActivity(i);
                            finishAffinity();
                            //     } else {
                            //       Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            //     }

                            progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<PhoneupdateminiPOJO> call, Throwable t) {

                            Toast.makeText(getContext(), "failed to update", Toast.LENGTH_SHORT).show();
                            progress.setVisibility(View.GONE);
                        }
                    });

                } else {
                    Toast.makeText(PhoneUpdateActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }


            }


        });


        Typeface typeFace = Typeface.MONOSPACE;
        ((TextView)toolbar.getChildAt(1)).setTypeface(typeFace);


    }

    public void updatenumber() {
        final bean b = (bean) getContext().getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);
        String phone = getIntent().getStringExtra("phoneno");
        Call<UpdatephonePOJO> call = cr.updatePhoneno(phone);

        call.enqueue(new Callback<UpdatephonePOJO>() {
            @Override
            public void onResponse(Call<UpdatephonePOJO> call, Response<UpdatephonePOJO> response) {

                //      if (Objects.equals(response.body().getMessage(), "1")) {
                //         Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                //    if (mGoogleApiClient.isConnected()) {
                //       dialog.dismiss();
                //        signOut();
                //    }

                Log.d("sachin", "response");

                //   LoginManager.getInstance().logOut();

                if (!response.body().getInformation().getOtp().toString().isEmpty()) {
                    edit.putString("otp", response.body().getInformation().getOtp().toString());
                }

//                edit.remove("type");
//                edit.remove("user");
//                edit.remove("pass");
//                edit.apply();
//
//                Toast.makeText(getContext(), "Mobile Number Updated, Login with Updated Mobile Number", Toast.LENGTH_LONG).show();
//
//                Intent i = new Intent(PhoneUpdateActivity.this, Login.class);
//                startActivity(i);
//                finishAffinity();
                //     } else {
                //       Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                //     }

                //  progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UpdatephonePOJO> call, Throwable t) {

                Toast.makeText(getContext(), "failed to update", Toast.LENGTH_SHORT).show();
                //  progress.setVisibility(View.GONE);
            }
        });
    }
}

