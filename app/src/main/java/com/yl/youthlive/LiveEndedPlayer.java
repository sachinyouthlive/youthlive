package com.yl.youthlive;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.followPOJO.followBean;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LiveEndedPlayer extends AppCompatActivity {

    String image , name , id , views , time;

    ImageView background;
    CircleImageView profile;

    TextView viewers , liveTime , username;

    Button follow , back;


    ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_ended_player);

        image = getIntent().getStringExtra("image");
        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        views = getIntent().getStringExtra("views");
        time = getIntent().getStringExtra("time");


        background = findViewById(R.id.imageView8);
        profile = findViewById(R.id.view7);
        liveTime = findViewById(R.id.textView19);
        viewers = findViewById(R.id.textView20);
        username = findViewById(R.id.textView21);
        follow = findViewById(R.id.button5);
        back = findViewById(R.id.button6);
        progress = findViewById(R.id.progressBar7);


        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();

        ImageLoader loader = ImageLoader.getInstance();

        loader.loadImage(image, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                Blurry.with(LiveEndedPlayer.this).from(loadedImage).into(background);
                profile.setImageBitmap(loadedImage);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        //loader.displayImage(image , background , options);
        //loader.displayImage(image , profile , options);

        username.setText(name);
        viewers.setText(views);
        liveTime.setText(getDurationString(Integer.parseInt(time)));


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress.setVisibility(View.VISIBLE);

                final bean b = (bean) getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                retrofit2.Call<followBean> call = cr.follow(b.userId , id);

                call.enqueue(new retrofit2.Callback<followBean>() {
                    @Override
                    public void onResponse(retrofit2.Call<followBean> call, retrofit2.Response<followBean> response) {

                        if (response.body().getStatus().equals("1"))
                        {
                            Toast.makeText(LiveEndedPlayer.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            follow.setVisibility(View.GONE);
                        }



                        progress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(retrofit2.Call<followBean> call, Throwable t) {

                        progress.setVisibility(View.GONE);

                    }
                });



            }
        });


    }

    private String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
    }

    private String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

}
