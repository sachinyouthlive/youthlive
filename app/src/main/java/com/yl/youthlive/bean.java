package com.yl.youthlive;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yl.youthlive.internetConnectivity.ConnectivityReceiver;
import com.yl.youthlive.vlogListPOJO.Datum;

import java.util.ArrayList;
import java.util.List;

public class bean extends Application {

    public static ArrayList<String> mylist;
    private static Context context;
    private static bean mInstance;
    public String BASE_URL = "http://ec2-13-127-59-58.ap-south-1.compute.amazonaws.com/softcode/";
    //public String BASE_URL = "http://youthlive.in/softcode/";
    public String userId = "";
    protected String userAgent;
    String userName = "";
    String userImage = "";
    String liveId = "";

    private String TAG = "myApp";
    List<Datum> vlist = new ArrayList<>();
    public bean() {
        mylist = new ArrayList<String>();
    }
    public static Context getContext() {
        return context;
    }

    public static synchronized bean getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        mInstance = this;
        context = getApplicationContext();
        Log.e(TAG, "  myapp stater");

        try {
            FirebaseApp.initializeApp(this);
        }
        catch (Exception e) {
        }

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));

        FontsOverride.setDefaultFont(this, "MONOSPACE", "calibri.ttf");

    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }


    public boolean useExtensionRenderers() {
        return BuildConfig.FLAVOR.equals("withExtensions");
    }


    String names[] = {
            "heart",
            "gun",
            "scooter",
            "rakhi",
            "teddy",
            "chocolates",
            "treasure",
            "clap",
            "clock",
            "bike",
            "car",
            "bird",
            "rose",
            "dancing girl",
            "diamond",
            "superbee",
            "hug",
            "heart beat",
            "golden egg",
            "love",
            "rabbits",
            "loving heart",
            "ring",
            "kiss",
            "fire",
            "head phone",
            "weapon"
    };


    Integer gifts[] = new Integer[]
            {
                    R.drawable.g52,
                    R.drawable.g20,
                    R.drawable.g32,
                    R.drawable.g1500,
                    R.drawable.g72,
                    R.drawable.g112,
                    R.drawable.g152,
                    R.drawable.g172,
                    R.drawable.g180,
                    R.drawable.g192,
                    R.drawable.g212,
                    R.drawable.g240,
                    R.drawable.g252,
                    R.drawable.g280,
                    R.drawable.g300,
                    R.drawable.g312,
                    R.drawable.g352,
                    R.drawable.g380,
                    R.drawable.g452,
                    R.drawable.g500,
                    R.drawable.g612,
                    R.drawable.g700,
                    R.drawable.g800,
                    R.drawable.g900,
                    R.drawable.g1000,
                    R.drawable.g1100,
                    R.drawable.g1200
            };


    String diamonds[] = {
            "12",
            "20",
            "32",
            "52",
            "72",
            "112",
            "152",
            "172",
            "180",
            "192",
            "212",
            "240",
            "252",
            "280",
            "300",
            "312",
            "352",
            "380",
            "452",
            "500",
            "612",
            "700",
            "800",
            "900",
            "1000",
            "1100",
            "1200"
    };

}
