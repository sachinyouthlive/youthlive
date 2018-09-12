package com.yl.youthlive;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import jp.wasabeef.blurry.Blurry;

public class LiveEndedBroadcaster extends AppCompatActivity {

    ImageView image;
    TextView liveTime, viewers, share;
    Button back;

    String lt, views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_ended_broadcaster);

        lt = getIntent().getStringExtra("liveTime");
        views = getIntent().getStringExtra("views");

        image = findViewById(R.id.imageView10);
        liveTime = findViewById(R.id.textView23);
        viewers = findViewById(R.id.textView24);
        share = findViewById(R.id.textView25);
        back = findViewById(R.id.button7);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        liveTime.setText(getDurationString(Integer.parseInt(lt)));
        viewers.setText(views);


        bean b = (bean) getApplicationContext();

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();
        ImageLoader loader = ImageLoader.getInstance();

        loader.loadImage(b.userImage, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                try {
                    Blurry.with(LiveEndedBroadcaster.this).from(loadedImage).into(image);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

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
