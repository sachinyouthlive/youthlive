package com.yl.youthlive;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yl.youthlive.Activitys.UserInformation;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.login2POJO.login2Bean;
import com.yl.youthlive.socialPOJO.socialBean;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class SplashActivity extends AppCompatActivity {
    ImageView bg;
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    ProgressBar progress;
    private String TAG = "splashAcctivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        edit = pref.edit();
        progress = findViewById(R.id.progress);

        bg = findViewById(R.id.splashbg);


        //  Glide.with(this).load(R.drawable.splashyl).into(bg);


        startApp();


    }


    public void startApp() {

        String type = pref.getString("type", "");
        String em = pref.getString("user", "");
        String pa = pref.getString("pass", "");

        if (type.length() > 0 && em.length() > 0 && pa.length() > 0) {
            if (Objects.equals(type, "social")) {

                socialLogin(em, pa);

            } else if (Objects.equals(type, "phone")) {

                phoneLogin(em, pa);

            }

        } else {

            Timer t = new Timer();

            t.schedule(new TimerTask() {
                @Override
                public void run() {

                    Intent intent = new Intent(SplashActivity.this, Login.class);
                    startActivity(intent);
                    finish();

                }
            }, 4000);

        }

    }

    public void phoneLogin(final String phone, final String pass) {

        // progress.setVisibility(View.VISIBLE);

        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<login2Bean> call = cr.signIn(phone, pass);

        call.enqueue(new Callback<login2Bean>() {
            @Override
            public void onResponse(Call<login2Bean> call, retrofit2.Response<login2Bean> response) {


                if (Objects.equals(response.body().getStatus(), "1")) {
                    Toast.makeText(SplashActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    b.userId = response.body().getData().getUserId();
                    b.userName = response.body().getData().getUserName();

                    try {
                        b.userImage = response.body().getData().getUserImage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    edit.putString("type", "phone");
                    edit.putString("user", phone);
                    edit.putString("pass", pass);
                    edit.commit();

                    Intent Inbt = new Intent(SplashActivity.this, HomeActivity.class);
                    Inbt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(Inbt);
                    SplashActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                } else {
                    Toast.makeText(SplashActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }


                //   progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<login2Bean> call, Throwable t) {
                //  progress.setVisibility(View.GONE);
            }
        });


    }


    public void socialLogin(final String email, final String pid) {
        //  progress.setVisibility(View.VISIBLE);

        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<socialBean> call = cr.socialSignIn(pid, email);

        call.enqueue(new Callback<socialBean>() {
            @Override
            public void onResponse(Call<socialBean> call, retrofit2.Response<socialBean> response) {

                if (response.body().getData().getUserName().length() > 0) {

                    b.userId = response.body().getData().getUserId();
                    b.userName = response.body().getData().getUserName();

                    try {
                        b.userImage = response.body().getData().getUserImage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    edit.putString("type", "social");
                    edit.putString("user", email);
                    edit.putString("pass", pid);
                    edit.commit();

                    Toast.makeText(SplashActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    SplashActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


                } else {

                    b.userId = response.body().getData().getUserId();
                    b.userName = response.body().getData().getUserName();

                    try {
                        b.userImage = response.body().getData().getUserImage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    edit.putString("type", "social");
                    edit.putString("user", email);
                    edit.putString("pass", pid);
                    edit.commit();

                    Toast.makeText(SplashActivity.this, "Please update your info", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SplashActivity.this, UserInformation.class);
                    intent.putExtra("userId", response.body().getData().getUserId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                }


                //      progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<socialBean> call, Throwable t) {
                //   progress.setVisibility(View.GONE);
            }
        });


    }


}
