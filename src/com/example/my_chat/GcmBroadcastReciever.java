package com.example.my_chat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class GcmBroadcastReciever extends BroadcastReceiver {

	Context context;
	public static final String TAG = "GcmBroadCastReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;

		PowerManager mPowMan = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		WakeLock mWakeLock = mPowMan.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
		mWakeLock.acquire();

		try {

			GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);

			String messageType = gcm.getMessageType(intent);
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
				sendNotification("Send Error!!", false);

			else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType))
				sendNotification("The Messages have been deleted on Server", false);

			else {
				String msg = intent.getStringExtra("MESSAGE");
				String uname = intent.getStringExtra("USER");

				ContentValues values = new ContentValues(2);
				values.put("MESSAGE", msg);
				values.put("USER", uname);

				context.getContentResolver().insert(null, values);

				sendNotification("New Message", true);
			}

			setResultCode(Activity.RESULT_OK);
		} finally {
			mWakeLock.release();
		}
	}

	@SuppressWarnings("deprecation")
	private void sendNotification(String text, Boolean launchApp) {
		NotificationManager mNotMan = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification.Builder mBuilder = new Notification.Builder(context).setAutoCancel(true)
				.setSmallIcon(R.drawable.ic_launcher).setContentTitle("My-Chat").setContentText(text);

		mBuilder.setSound(RingtoneManager.getValidRingtoneUri(context));

		if (launchApp) {
			Intent intent = new Intent(context, SendMessage.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			PendingIntent pI = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setContentIntent(pI);
		}

		mNotMan.notify(1, mBuilder.getNotification());
	}

}
