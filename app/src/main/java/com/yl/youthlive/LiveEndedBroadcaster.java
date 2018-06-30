package com.yl.youthlive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class LiveEndedBroadcaster extends AppCompatActivity {

    ImageView image;
    TextView liveTime , viewers , share;
    Button back;

    String lt , views;

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


        bean b = (bean)getApplicationContext();

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(b.userImage , image , options);



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
