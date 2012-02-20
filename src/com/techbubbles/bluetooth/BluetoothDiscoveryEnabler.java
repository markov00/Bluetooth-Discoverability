package com.techbubbles.bluetooth;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.util.Log;

/**
 * BluetoothEnabler is a helper to manage the Bluetooth on/off checkbox
 * preference. It is turns on/off Bluetooth and ensures the summary of the
 * preference reflects the current state.
 * 
 * @author Marco Vettorello
 */
public class BluetoothDiscoveryEnabler implements Preference.OnPreferenceChangeListener {
	private static final int SCAN_MODE_DISABLE_ALWAYS_DISCOVERY = 666;
	private static final int SCAN_MODE_ENABLE_ALWAYS_DISCOVERY = 999;
	private final Context mContext;
	private final CheckBoxPreference mCheckBox;
	private final CharSequence mOriginalSummary;
	private BluetoothAdapter mAdapter;
	private static final String TAG = "BluetoothDiscoveryEnabler";
	private final IntentFilter mIntentFilter;

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int state = BluetoothAdapter.ERROR;
			if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
				Log.d(TAG, "bluetoooth ACTION_STATE_CHANGED state: "+state);
				handleStateChanged(state);
			} else if (intent.getAction().equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
				state = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);
				Log.d(TAG, "bluetoooth ACTION_SCAN_MODE_CHANGED state:"+state);
				handleStateChanged(state);
			}
			
		}
	};

	public BluetoothDiscoveryEnabler(Context context, CheckBoxPreference checkBox) {
		mContext = context;
		mCheckBox = checkBox;
		mOriginalSummary = checkBox.getSummary();
		checkBox.setPersistent(false);

		mAdapter = BluetoothAdapter.getDefaultAdapter();

		if (mAdapter == null) {
			// Bluetooth is not supported
			checkBox.setEnabled(false);
		}
		mIntentFilter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		mIntentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		
		

	}


	public void resume() {
		if (mIntentFilter == null) {
			return;
		}
		doBindService();
		handleStateChanged(mAdapter.getScanMode());
		handleStateChanged(mAdapter.getState());
		mContext.registerReceiver(mReceiver, mIntentFilter);
		mCheckBox.setOnPreferenceChangeListener(this);
	}

	public void pause() {
		if (mAdapter == null) {
			return;
		}

		mContext.unregisterReceiver(mReceiver);
		mCheckBox.setOnPreferenceChangeListener(null);
		doUnbindService();
	}
	IBluetoothDiscoverableService mService;
	boolean mIsBound;
	private ServiceConnection mConnection = new ServiceConnection() {
	    public void onServiceConnected(ComponentName className, IBinder service) {
	    	mService = ((BluetoothDiscoverableService.LocalBinder)service).getService();
	    }

	    public void onServiceDisconnected(ComponentName className) {
	    	mService = null;
	    }
	};

	void doBindService() {

	    mContext.bindService(new Intent(mContext,BluetoothDiscoverableService.class), mConnection, Context.BIND_AUTO_CREATE);
	    mIsBound = true;
	}

	void doUnbindService() {
	    if (mIsBound) {
	    	mContext.unbindService(mConnection);
	        mIsBound = false;
	    }
	}

	public boolean onPreferenceChange(Preference preference, Object value) {
		boolean enable = (Boolean) value;
		if(mService == null){
			doBindService();
			return false;
		}
		if(enable){
			mService.enable();
			handleStateChanged(SCAN_MODE_ENABLE_ALWAYS_DISCOVERY);
		}
		else{
			mService.disable();
			handleStateChanged(SCAN_MODE_DISABLE_ALWAYS_DISCOVERY);
		}
		return true;
	}

	private void handleStateChanged(int state) {
		switch (state) {
		case BluetoothAdapter.STATE_TURNING_ON:
			mCheckBox.setSummary(R.string.bt_starting);
			mCheckBox.setEnabled(false);
			break;
		case BluetoothAdapter.STATE_ON:
			mCheckBox.setSummary(mOriginalSummary);
			mCheckBox.setEnabled(true);
			break;
		case BluetoothAdapter.STATE_TURNING_OFF:
			mCheckBox.setSummary(R.string.bt_stopping);
			mCheckBox.setEnabled(false);
			break;
		case BluetoothAdapter.STATE_OFF:
			mCheckBox.setChecked(false);
			mCheckBox.setSummary(R.string.bt_stopping);
			mCheckBox.setEnabled(false);
			break;
		case BluetoothAdapter.SCAN_MODE_NONE:
			mCheckBox.setSummary(R.string.bluetooth_not_discoverable);
			mCheckBox.setChecked(false);
			break;
		case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
			mCheckBox.setSummary(R.string.bluetooth_not_discoverable);
			mCheckBox.setChecked(false);
			break;
		case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
			mCheckBox.setSummary(R.string.bluetooth_is_discoverable);
			mCheckBox.setChecked(true);
			break;
		case SCAN_MODE_DISABLE_ALWAYS_DISCOVERY:
			mCheckBox.setChecked(false);
			mCheckBox.setSummary(R.string.bluetooth_not_discoverable);
			break;
		case SCAN_MODE_ENABLE_ALWAYS_DISCOVERY:
			mCheckBox.setSummary(R.string.bluetooth_is_discoverable);
			mCheckBox.setChecked(true);
			break;
		default:
			mCheckBox.setChecked(false);
			mCheckBox.setSummary(R.string.bt_error);
			mCheckBox.setEnabled(false);
		}
	}
}