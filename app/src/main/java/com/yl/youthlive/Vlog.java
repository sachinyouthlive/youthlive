package com.yl.youthlive;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yl.youthlive.internetConnectivity.ConnectivityReceiver;

import java.util.Objects;


public class Vlog extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {


    TabLayout tabs;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vlog, container, false);
        checkConnection();


        tabs = (TabLayout) view.findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);


        tabs.addTab(tabs.newTab().setText("POPULAR"));
        tabs.addTab(tabs.newTab().setText("NEW"));

        /*FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getChildFragmentManager(), FragmentPagerItems.with(getContext())
                .add("Hot", HotVolg.class)
                .add("Nearby", HotVolg.class)
                .create());*/

        ViewAdapter adapter = new ViewAdapter(getChildFragmentManager());

        viewPager.setAdapter(adapter);

        tabs.setupWithViewPager(viewPager);


        tabs.getTabAt(0).setText("POPULAR");
        tabs.getTabAt(1).setText("NEW");

        setCustomFont();

        ((HomeActivity) Objects.requireNonNull(getActivity())).setFragmentRefreshListener(new HomeActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {


                assert getFragmentManager() != null;
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(com.yl.youthlive.Vlog.this).attach(com.yl.youthlive.Vlog.this).commit();

            }
        });



        return view;
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


    ////////////////////internet connectivity check///////////////
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showalert(isConnected);
    }

    private void showalert(boolean isConnected) {
        if (isConnected) {

            //  Toast.makeText(getActivity(), "Good! Connected to Internet", Toast.LENGTH_SHORT).show();
            //    message = "Good! Connected to Internet";
            //    color = Color.WHITE;
        } else {

            try {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getActivity());
                }
                builder.setTitle("NO INTERNET CONNECTION")
                        .setMessage("Please check your internet connection setting and click refresh")
                        .setPositiveButton(R.string.Refresh, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // Reload current fragment
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(Vlog.this).attach(Vlog.this).commit();
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
        }

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showalert(isConnected);

    }

    @Override
    public void onResume() {
        super.onResume();
        // register connection status listener
        bean.getInstance().setConnectivityListener(this);
    }

    class ViewAdapter extends FragmentStatePagerAdapter {

        public ViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {

                return new popularVolg();
            } else if (position == 1) {

                return new HotVolg();
            }

            return null;


        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
