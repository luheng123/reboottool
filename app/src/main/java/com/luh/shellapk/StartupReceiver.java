package com.luh.shellapk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartupReceiver extends BroadcastReceiver {
	static final String ACTION = "android.intent.action.BOOT_COMPLETED";

	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION)) {
			// 开机启动的Activity
			Intent activityIntent = new Intent(context, MainActivity.class);
			activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// 启动Activity
			context.startActivity(activityIntent);

			// 开机启动的Service
			Intent serviceIntent = new Intent(context, MainActivity.class);
			// 启动Service
			context.startService(serviceIntent);
		}

	}

}
