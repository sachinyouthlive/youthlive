package com.yl.youthlive;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String TAG = "messageData";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //if(remoteMessage.getData().size() > 0){

        //    Log.d("asdasd" , "asdasasdasdasd");
        //handle the data message here
        //}


        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }


        //getting the title and the body
        //String title = remoteMessage.getNotification().getTitle();
        //String body = remoteMessage.getNotification().getBody();

        //Log.d("title" , title);
        //Log.d("body" , body);


        //Intent registrationComplete = new Intent("registrationComplete");
        //registrationComplete.putExtra("token", );
        //LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);


    }


    private void handleDataMessage(JSONObject data2) {
        Log.e(TAG, "push json: " + data2.toString());

        try {
            //JSONObject data = data2.getJSONObject("data");


            String type = data2.getString("type");


            if (type.equals("comment")) {
                JSONObject dat = data2.getJSONObject("data");
                Intent registrationComplete = new Intent("commentData");
                registrationComplete.putExtra("data", dat.toString());

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

            } else if (type.equals("view")) {

                JSONObject dat = data2.getJSONObject("data");
                Log.d("view", "called");

                Intent registrationComplete = new Intent("view");
                registrationComplete.putExtra("data", dat.toString());

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
            } else if (type.equals("gift")) {

                JSONObject dat = data2.getJSONObject("data");
                Log.d("view", "called");

                Intent registrationComplete = new Intent("gift");
                registrationComplete.putExtra("data", dat.toString());

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
            } else if (type.equals("like")) {
                String dat = data2.getString("data");

                Log.d("view", "called");

                Intent registrationComplete = new Intent("like");
                registrationComplete.putExtra("data", dat.toString());

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
            } else if (type.equals("request")) {
                String dat = data2.getString("data");

                Log.d("request", "called");

                Intent registrationComplete = new Intent("request");
                registrationComplete.putExtra("data", dat.toString());

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
            } else if (type.equals("status")) {
                String dat = data2.getString("data");

                Log.d("status", "called");

                Intent registrationComplete = new Intent("status");
                registrationComplete.putExtra("data", dat.toString());

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
            } else if (type.equals("live_end")) {
                String dat = data2.getString("data");

                Log.d("status", "called");

                Intent registrationComplete = new Intent("live_end");
                registrationComplete.putExtra("data", dat.toString());

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
            } else if (type.equals("connection_end")) {
                String dat = data2.getString("data");

                Log.d("status", "called");

                Intent registrationComplete = new Intent("connection_end");
                registrationComplete.putExtra("data", dat.toString());

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
            }
            else if (type.equals("request_player")) {
                String dat = data2.getString("data");

                Log.d("request_player", "called");

                Intent registrationComplete = new Intent("request_player");
                registrationComplete.putExtra("data", dat.toString());

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
            }
            else if (type.equals("status_player")) {
                String dat = data2.getString("data");

                Log.d("status_player", "called");

                Intent registrationComplete = new Intent("status_player");
                registrationComplete.putExtra("data", dat.toString());

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
            }
            else if (type.equals("exit")) {
                String dat = data2.getString("data");

                Log.d("exit", "called");

                Intent registrationComplete = new Intent("exit");
                registrationComplete.putExtra("data", dat.toString());

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
            }


        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


    private void handleNotification(String title, String message) {

        Log.d("notificationData", message);

    }


}
