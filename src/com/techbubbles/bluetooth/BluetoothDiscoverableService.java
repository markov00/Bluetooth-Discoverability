package com.techbubbles.bluetooth;

import java.io.IOException;
import java.lang.reflect.Method;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/**
 * This Service will provide the ability to maintain the bluetooth
 * discoverability always on. It will force the discoverability mode every 100
 * seconds.
 * 
 * @author Marco Vettorello
 * 
 */
public class BluetoothDiscoverableService extends Service implements IBluetoothDiscoverableService {

	private static final String TAG = "BluetoothDiscoverable";

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			try {
				Log.d(TAG, " Calling setScanMode Discoverable");
				Method m1 = adapter.getClass().getMethod("setDiscoverableTimeout", int.class);
				m1.invoke(adapter, 120);
				Method m2 = adapter.getClass().getMethod("setScanMode", int.class, int.class);
				m2.invoke(adapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, 120);
			} catch (Exception e) {
				Log.e(TAG, "Error on invoking setScanMode ", e);
			}
			sendEmptyMessageDelayed(1, 100000);
		}

	};

	private LocalBinder mBinder = new LocalBinder();

	public class LocalBinder extends Binder {
		IBluetoothDiscoverableService getService() {
			return BluetoothDiscoverableService.this;
		}
	}

	@Override
	public void disable() {
		mHandler.removeMessages(1);
	}

	@Override
	public void enable() {
		mHandler.sendEmptyMessage(1);

	}

}
