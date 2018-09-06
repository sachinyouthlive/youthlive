package com.yl.youthlive;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yl.youthlive.internetConnectivity.ConnectivityReceiver;
import com.yl.youthlive.vlogListPOJO.Datum;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by TBX on 11/8/2017.
 */

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

    boolean frag = false;

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
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));

        FontsOverride.setDefaultFont(this, "MONOSPACE", "calibri.ttf");

    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }


    public boolean useExtensionRenderers() {
        return BuildConfig.FLAVOR.equals("withExtensions");
    }

}
