package com.yl.youthlive;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.yl.youthlive.vlogListPOJO.Datum;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class VerticalFragmentVodActivity extends AppCompatActivity {

    List<Datum> list2 = new ArrayList<>();
    CustomViewPager pager;

    int pos;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_fragment_vod2);
        Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Answers());
        Crashlytics.setUserIdentifier(SharePreferenceUtils.getInstance().getString("userId"));
        toolbar = findViewById(R.id.toolbar);
        bean b = (bean) getApplicationContext();

        list2 = b.vlist;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // b.mylist.remove(b.mylist.size() - 1);

                finish();
            }
        });

        pos = Integer.parseInt(getIntent().getStringExtra("position"));

        pager = findViewById(R.id.pager);


        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), list2);
        pager.setAdapter(adapter);

        pager.setCurrentItem(pos);

    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        List<Datum> ll = new ArrayList<>();

        public PagerAdapter(FragmentManager fm, List<Datum> ll) {
            super(fm);
            this.ll = ll;
        }

        @Override
        public Fragment getItem(int position) {
            final bean b1 = (bean) getApplicationContext();


            VideoPlayerFragmentvod frag = new VideoPlayerFragmentvod();
            Bundle b = new Bundle();
            b.putString("uri", ll.get(position).getVideoURL());
            b.putString("videoId", ll.get(position).getVideoId());
            b.putString("userId", ll.get(position).getUserId());
            b.putString("thumburl", ll.get(position).getThumbURL());
            //   b.putString("pic", b1.BASE_URL + ll.get(position).getUserImage());
            frag.setArguments(b);
            return frag;


        }

        @Override
        public int getCount() {
            return ll.size();
            //ll.size();
        }
    }

}


