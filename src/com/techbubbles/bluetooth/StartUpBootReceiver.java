package com.techbubbles.bluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Broadcast receiver that start up the BluetoothDiscoverableService
 * @author Marco Vettorello
 *
 */
public class StartUpBootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent serviceIntent = new Intent();
		serviceIntent.setAction("com.techbubbles.bluetooth.BluetoothDiscoverableService");
		context.startService(serviceIntent);
	}
}