package com.pervasive_computing.bactrackapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * Created by anura on 11/20/2017.
 */

public class NotificationUtils {

    private static final int BAC_PENDING_INTENT_ID = 3417;
    private static final int BAC_NOTIFICATION_ID = 1138;
    private static final String BAC_NOTIFICATION_CHANNEL_ID = "bac_notification_channel_id";

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, SplashActivity.class);
        return PendingIntent.getActivity(context, BAC_PENDING_INTENT_ID, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void remindUser(Context context) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("BAC")
                .setContentText("Check BAC?")
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(BAC_NOTIFICATION_CHANNEL_ID, context.getString(R.string.BAC_NOTIFICATION_CHANNEL), NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(mChannel);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(BAC_NOTIFICATION_ID, notificationBuilder.build());

    }


}
