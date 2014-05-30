package com.example.resources;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.ffmpeg_trial.MainActivity;
import com.example.ffmpeg_trial.R;

public class MyNotification {
	
	public static void sendNotification(Context ctxt, String mssg) {
		// generate notification: http://developer.android.com/training/notify-user/build-notification.html#click
		final int MY_NOTIFICATION_ID = 1; 
		String notificationText = mssg;
		NotificationCompat.Builder myNotification = new NotificationCompat.Builder(
				ctxt).setContentTitle("Progress")
				.setContentText(notificationText)
				.setTicker("Notification!")
				.setWhen(System.currentTimeMillis())
				.setDefaults(android.app.Notification.DEFAULT_SOUND)
				.setAutoCancel(true)
				.setSmallIcon(R.drawable.ic_launcher);
		// If clicked, takes them to app
		Intent resultIntent = new Intent(ctxt, MainActivity.class);
		PendingIntent resultPendingIntent =
		    PendingIntent.getActivity(
    		ctxt,
		    0,
		    resultIntent,
		    PendingIntent.FLAG_UPDATE_CURRENT
		);
		
		myNotification.setContentIntent(resultPendingIntent);
		
		NotificationManager notificationManager = 
		        (NotificationManager) ctxt.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(MY_NOTIFICATION_ID, myNotification.build());
	}
}
