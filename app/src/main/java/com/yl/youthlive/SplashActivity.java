package com.yl.youthlive;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.koushikdutta.ion.Ion;


public class SplashActivity extends AppCompatActivity {
    private String TAG = "splashAcctivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
        Log.e(TAG, " splash start showing");
        ImageView loadingimageView = (ImageView) findViewById(R.id.apploadingimage);

       Glide.with(SplashActivity.this)
               .load(R.drawable.loadinggif)
              .into(loadingimageView);

    }

    public void init() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /// if user registered user
                // then show home screen
                // else  show login screen
                // key  register_user
                Log.e(TAG, "  counter running ");
              //  if (SharePreferenceUtils.getInstance().getString(Constant.USER_DATA).equalsIgnoreCase("")) {
                    // not registted user  so show login screen
                    Intent intent = new Intent(SplashActivity.this, Login.class);
                    startActivity(intent);
            //    } else {
                    // home sscreen
             //       Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
           //         startActivity(intent);
         //       }
                finish();

            }
        }, 10000);

    }
}
