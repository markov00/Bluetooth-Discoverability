package com.techbubbles.bluetooth;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;

/**
 * Settings activity to enable or disable the discoverability of the bluetooth.
 * @author Marco Vettorello
 *
 */
public class BluetoothExtensionSettings extends PreferenceActivity {

	private BluetoothDiscoveryEnabler mEnabler;
	private static final String KEY_BT_CHECKBOX = "bt_discoverable";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		mEnabler = new BluetoothDiscoveryEnabler(this, (CheckBoxPreference) findPreference(KEY_BT_CHECKBOX));

	}

	@Override
	protected void onResume() {
		super.onResume();
		mEnabler.resume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mEnabler.pause();
	}

}