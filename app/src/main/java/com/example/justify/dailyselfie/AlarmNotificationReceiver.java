package com.example.justify.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by justify on 26.04.2015.
 */
public class AlarmNotificationReceiver extends BroadcastReceiver {

    // Notification ID to allow for future updates
    private static final int MY_NOTIFICATION_ID = 1;

    // Notification Text Elements
    private final CharSequence tickerText = "Time for another selfie";
    private final CharSequence contentTitle = "Daily Selfie";
    private final CharSequence contentText = "Time for another selfie";

    // Notification Vibration on Arrival
    private long[] mVibratePattern = { 0, 200, 200, 300 };


    @Override
    public void onReceive(Context context, Intent intent) {

        //Set Up and send notification
        Intent mNotificationIntent = new Intent(context,
                DailySelfieMain.class);
        PendingIntent mContentIntent = PendingIntent.getActivity(context, 0,
                mNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder notificationBuilder = new Notification.Builder(
                context)
                .setTicker(tickerText)
                .setSmallIcon(R.drawable.ic_action_camera_red)
                .setAutoCancel(true)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(mContentIntent)
                .setVibrate(mVibratePattern);

        // Pass the Notification to the NotificationManager:
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MY_NOTIFICATION_ID,
                notificationBuilder.build());


    }
}
