package com.yl.youthlive;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.yl.youthlive.internetConnectivity.ConnectivityReceiver;


/**
 * Created by TBX on 11/8/2017.
 */

public class bean extends Application{

    public String BASE_URL = "http://ec2-13-58-47-70.us-east-2.compute.amazonaws.com/softcode/";

    public String userId = "";
    String userName = "";
    String userImage = "";
    private static Context context;

    String liveId = "";
    private static bean mInstance;
    private String TAG = "myApp";

    public static Context getContext() {
        return context;
    }
    protected String userAgent;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context = getApplicationContext();
        Log.e(TAG, "  myapp stater");

    }
    public static synchronized bean getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }





    public boolean useExtensionRenderers() {
        return BuildConfig.FLAVOR.equals("withExtensions");
    }

}
