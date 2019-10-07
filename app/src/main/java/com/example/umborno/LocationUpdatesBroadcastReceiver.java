package com.example.umborno;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.umborno.ui.MainActivity;

public class LocationUpdatesBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "LUBroadcastReceiver";
    public static final String ACTION_PROCESS_UPDATES = "com.example.umborno.locationupdatespendingintent.action" + ".PROCESS_UPDATES";
    public static final String NOTIFICATION_CHANNEL_ID = "channel_001";
    public static final String NOTIFICATION_CHANNEL_NAME = "upLocation";
    public static final String LON_KEY = "lon";
    public static final String LAT_KEY = "lat";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: location update");
        if(intent!=null){
             String action = intent.getAction();
             if(action.equals(ACTION_PROCESS_UPDATES)){
                 Bundle bundle = intent.getExtras();
                 Double longitude = bundle.getDouble(LON_KEY);
                 Double latitude = bundle.getDouble(LAT_KEY);
                 //todo send out location to update ui
                 String detail = "Location update: latitude "+ latitude + " longitude "+ longitude;
                 sendNotification(context,detail);
                 
            }
        }

    }

    private void sendNotification(Context context, String notificationDetails){
        Log.d(TAG, "sendNotification: ");
        Intent notifIntent = new Intent(context, MainActivity.class);
        notifIntent.putExtra("from_noti",true);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        /*
        add parent chains for the targeted activity(MainActivity) according to TargetActivity.getParentActivityIntent().
        In order for getParentActivityIntent() to work, we need to specify parentActivityName attribute for each activity in Manifest.xml
         */
        taskStackBuilder.addParentStack(MainActivity.class);
        //add intent to the intent stack, so the last added one will be the first fired
        taskStackBuilder.addNextIntent(notifIntent);
        PendingIntent notiPendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher))
                .setColor(Color.RED)
                .setContentTitle("Location update")
                .setContentText(notificationDetails)
                .setContentIntent(notiPendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String name = context.getString(R.string.app_name);
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,name + NOTIFICATION_CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0,builder.build());
    }
}


























