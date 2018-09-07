package com.yl.youthlive;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WalletNew extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabs;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_new);

        toolbar = findViewById(R.id.toolbar3);
        tabs = findViewById(R.id.view5);
        pager = findViewById(R.id.pager);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Wallet");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tabs.addTab(tabs.newTab().setText("DIAMONDS"));
        tabs.addTab(tabs.newTab().setText("COINS"));

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);

        tabs.setupWithViewPager(pager);

        tabs.getTabAt(0).setText("DIAMONDS");
        tabs.getTabAt(1).setText("COINS");

        Typeface typeFace = Typeface.MONOSPACE;
        ((TextView)toolbar.getChildAt(0)).setTypeface(typeFace);

        setCustomFont();

    }


    public void setCustomFont() {

        ViewGroup vg = (ViewGroup) tabs.getChildAt(0);
        int tabsCount = vg.getChildCount();

        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);

            int tabChildsCount = vgTab.getChildCount();

            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    //Put your font in assests folder
                    //assign name of the font here (Must be case sensitive)
                    ((TextView) tabViewChild).setTypeface(Typeface.MONOSPACE);
                }
            }
        }
    }


    class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new Diamonds();
            } else {
                return new Beans();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


}
