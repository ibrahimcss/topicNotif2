package com.example.topicnotif.Service;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.topicnotif.Config.Config;
import com.example.topicnotif.MainActivity;
import com.example.topicnotif.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.io.IOException;
import java.lang.annotation.Target;
import java.net.URL;
import java.util.Map;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getData().size());
        try{
            final Map<String, String> params = remoteMessage.getData();
            JSONObject object=new JSONObject(params);
            String title = object.getString("title");
            String body = object.getString("body");
            if(!title.equals("") && !body.equals("")){
                sendNotificationData(title, body); //send notification to user
            }
        }catch (Exception e){

            e.printStackTrace();
        }



    }

    private void sendNotificationData(String messageTitle,String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0 /* request code */, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        long[] pattern = {500,500,500,500,500};
       Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
              .setVibrate(pattern)
                .setLights(Color.BLUE,1,1)
             .setSound(defaultSoundUri)
               .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


}