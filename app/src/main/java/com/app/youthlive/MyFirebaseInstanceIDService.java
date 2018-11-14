package com.app.youthlive;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


    SharedPreferences pref;
    SharedPreferences.Editor edit;




    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        try {
            String token = FirebaseInstanceId.getInstance().getToken();

            //for now we are displaying the token in the log
            //copy it as this method is called only when the new token is generated
            //and usually new token is only generated when the app is reinstalled or the data is cleared
            Log.d("MyRefreshedToken", token);


            pref = getSharedPreferences("fcm", Context.MODE_PRIVATE);
            edit = pref.edit();

            edit.putString("token", token);
            edit.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
