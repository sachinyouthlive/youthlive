package com.yl.youthlive;

import android.content.DialogInterface;
import android.graphics.Color;
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

        TextView tv = (TextView)inflater.inflate(R.layout.custom_tab,null);
        tv.setTypeface(Typeface.MONOSPACE);
        tv.setTextColor(Color.WHITE);
        tv.setText("HOT");


        TextView tv1 = (TextView)inflater.inflate(R.layout.custom_tab,null);
        tv1.setTypeface(Typeface.MONOSPACE);
        tv1.setTextColor(Color.WHITE);
        tv1.setText("POPULAR");


        tabs.addTab(tabs.newTab().setCustomView(tv));
        tabs.addTab(tabs.newTab().setCustomView(tv1));

        /*FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getChildFragmentManager(), FragmentPagerItems.with(getContext())
                .add("Hot", HotVolg.class)
                .add("Nearby", HotVolg.class)
                .create());*/

        ViewAdapter adapter = new ViewAdapter(getChildFragmentManager());

        viewPager.setAdapter(adapter);

        tabs.setupWithViewPager(viewPager);


        tabs.getTabAt(0).setCustomView(tv);
        tabs.getTabAt(1).setCustomView(tv1);


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
            return new HotVolg();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
