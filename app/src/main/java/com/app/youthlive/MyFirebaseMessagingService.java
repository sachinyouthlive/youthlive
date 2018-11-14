package com.app.youthlive;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String TAG = "messageData";

    SharedPreferences pref;
    SharedPreferences.Editor edit;



    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        try {

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

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
/////////////////


        ////////////////////////
        //if(remoteMessage.getData().size() > 0){

        //    Log.d("asdasd" , "asdasasdasdasd");
        //handle the data message here
        //}


        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
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

            String type = data2.getString("type");


            switch (type) {
                case "comment": {
                    JSONObject dat = data2.getJSONObject("data");
                    Intent registrationComplete = new Intent("commentData");
                    registrationComplete.putExtra("data", dat.toString());

                    LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

                    break;
                }
                case "view": {

                    JSONObject dat = data2.getJSONObject("data");
                    Log.d("view", "called");

                    Intent registrationComplete = new Intent("view");
                    registrationComplete.putExtra("data", dat.toString());

                    LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
                    break;
                }
                case "gift": {

                    JSONObject dat = data2.getJSONObject("data");
                    Log.d("view", "called");

                    Intent registrationComplete = new Intent("gift");
                    registrationComplete.putExtra("data", dat.toString());

                    LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
                    break;
                }
                case "like": {
                    String dat = data2.getString("data");

                    Log.d("view", "called");

                    Intent registrationComplete = new Intent("like");
                    registrationComplete.putExtra("data", dat);

                    LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
                    break;
                }
                case "request": {
                    String dat = data2.getString("data");

                    Log.d("request", "called");

                    Intent registrationComplete = new Intent("request");
                    registrationComplete.putExtra("data", dat);

                    LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
                    break;
                }
                case "status": {
                    String dat = data2.getString("data");

                    Log.d("status", "called");

                    Intent registrationComplete = new Intent("status");
                    registrationComplete.putExtra("data", dat);

                    LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
                    break;
                }
                case "live_end": {
                    String dat = data2.getString("data");

                    Log.d("status", "called");

                    Intent registrationComplete = new Intent("live_end");
                    registrationComplete.putExtra("data", dat);

                    LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
                    break;
                }
                case "connection_end": {
                    String dat = data2.getString("data");

                    Log.d("status", "called");

                    Intent registrationComplete = new Intent("connection_end");
                    registrationComplete.putExtra("data", dat);

                    LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
                    break;
                }
                case "request_player": {
                    String dat = data2.getString("data");

                    Log.d("request_player", "called");

                    Intent registrationComplete = new Intent("request_player");
                    registrationComplete.putExtra("data", dat);

                    LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
                    break;
                }
                case "status_player": {
                    String dat = data2.getString("data");

                    Log.d("status_player", "called");

                    Intent registrationComplete = new Intent("status_player");
                    registrationComplete.putExtra("data", dat);

                    LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
                    break;
                }
                case "exit": {
                    String dat = data2.getString("data");

                    Log.d("exit", "called");

                    Intent registrationComplete = new Intent("exit");
                    registrationComplete.putExtra("data", dat);

                    LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
                    break;
                }
                case "mute": {
                    String dat = data2.getString("data");

                    Log.d("mute", "called");

                    Intent registrationComplete = new Intent("mute");
                    registrationComplete.putExtra("data", dat);

                    LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
                    break;
                }
            }


        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


    private void handleNotification(String message) {

        Log.d("notificationData", message);
        String idChannel = "youthlive messages";
        Intent mainIntent;

        mainIntent = new Intent(bean.getContext(), MyFirebaseMessagingService.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(bean.getContext(), 0, mainIntent, 0);

        NotificationManager mNotificationManager = (NotificationManager) bean.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel mChannel = null;
        // The id of the channel.

        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(bean.getContext(), idChannel);
        builder.setContentTitle(bean.getContext().getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_action_action_search)
                .setContentIntent(pendingIntent)
                .setContentText(message);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(idChannel, bean.getContext().getString(R.string.app_name), importance);
            // Configure the notification channel.
            mChannel.setDescription(bean.getContext().getString(R.string.alarm_notification));
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
            }
        } else {
            builder.setContentTitle(bean.getContext().getString(R.string.app_name))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(bean.getContext(), R.color.transparent))
                    .setVibrate(new long[]{100, 250})
                    .setLights(Color.YELLOW, 500, 5000)
                    .setAutoCancel(true);
        }
        if (mNotificationManager != null) {
            mNotificationManager.notify(1, builder.build());
        }


    }


}
