package com.yl.youthlive.Activitys;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yl.youthlive.R;
import com.yl.youthlive.Rating1;
import com.yl.youthlive.Rating2;
import com.yl.youthlive.Rating3;
import com.yl.youthlive.bean;
import com.yl.youthlive.internetConnectivity.ConnectivityReceiver;

public class RattingActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    android.support.v7.widget.Toolbar toolbar;


    TabLayout layout;
    ViewPager pager;
    ProgressBar bar;
    ViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratting);
        checkConnection();

        layout = (TabLayout) findViewById(R.id.tab_layout);
        pager = (ViewPager) findViewById(R.id.pager);

        layout.addTab(layout.newTab().setText("Last hour"));
        layout.addTab(layout.newTab().setText("24 hour"));
        layout.addTab(layout.newTab().setText("7 days"));

        layout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new ViewAdapter(getSupportFragmentManager(), 3);

        layout.setupWithViewPager(pager);
        pager.setAdapter(adapter);

        layout.getTabAt(0).setText("Last hour");
        layout.getTabAt(1).setText("24 hour");
        layout.getTabAt(2).setText("7 days");


        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setTitle("Ranking");

        Typeface typeFace = Typeface.MONOSPACE;
        ((TextView)toolbar.getChildAt(1)).setTypeface(typeFace);

        setCustomFont();

    }

    public void setCustomFont() {

        ViewGroup vg = (ViewGroup) layout.getChildAt(0);
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

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        bean.getInstance().setConnectivityListener(this);


    }

    public void methodd() {
        if (!(adapter == null)) {
            adapter.notifyDataSetChanged();
            layout.getTabAt(0).setText("Last hour");
            layout.getTabAt(1).setText("24 hour");
            layout.getTabAt(2).setText("7 days");

        }
    }

    ///////////////////internet connectivity check///////////////
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showAlert(isConnected);
    }

    private void showAlert(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {

            //    message = "Good! Connected to Internet";
            //    color = Color.WHITE;
        } else {
            try {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle("NO INTERNET CONNECTION")
                        .setMessage("Please check your internet connection setting and click refresh")
                        .setPositiveButton(R.string.Refresh, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } catch (Exception e) {
                Log.d("TAG", "Show Dialog: " + e.getMessage());
            }
            //      message = "Sorry! Not connected to internet";
            //     color = Color.RED;
        }

       /* Snackbar snackbar = Snackbar
                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
        */
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showAlert(isConnected);

    }

    public class ViewAdapter extends FragmentStatePagerAdapter {

        int tabs;

        public ViewAdapter(FragmentManager fm, int list) {
            super(fm);

            this.tabs = list;
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {

                return new Rating1();
            } else if (position == 1) {

                return new Rating2();
            } else if (position == 2) {

                return new Rating3();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }


}
