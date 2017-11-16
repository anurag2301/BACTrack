package com.pervasive_computing.bactrackapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;

import BACtrackAPI.API.BACtrackAPI;
import BACtrackAPI.API.BACtrackAPICallbacks;
import BACtrackAPI.Constants.BACTrackDeviceType;
import BACtrackAPI.Exceptions.BluetoothLENotSupportedException;
import BACtrackAPI.Exceptions.BluetoothNotEnabledException;
import BACtrackAPI.Exceptions.LocationServicesNotEnabledException;

/*
  Created by Pratik on 11/08/2017.
 */

public class InitiateActivity extends BaseActivity {
    private static final byte PERMISSIONS_FOR_SCAN = 100;
    private static final String TAG = "InitiateActivity";
    private boolean bacConnected;
    private RelativeLayout loading_panel;
    private Button start_button;
    private final BACtrackAPICallbacks mCallbacks = new BACtrackAPICallbacks() {
        private static final String TAG = "BAC_Callbacks";

        @Override
        public void BACtrackAPIKeyDeclined(String errorMessage) {
            //APIKeyVerificationAlert verify = new APIKeyVerificationAlert();
            //verify.wtfxecute(errorMessage);
            Log.wtf(TAG, "BACtrackAPIKeyDeclined " + errorMessage);
        }

        @Override
        public void BACtrackAPIKeyAuthorized() {
            Log.wtf(TAG, "BACtrackAPIKeyAuthorized");
        }

        @Override
        public void BACtrackConnected(BACTrackDeviceType bacTrackDeviceType) {
            //setStatus(R.string.TEXT_CONNECTED);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loading_panel.setVisibility(View.GONE);
                    start_button.setEnabled(true);
                }
            });
            Log.wtf(TAG, "BACtrackConnected " + bacTrackDeviceType.toString());
        }

        @Override
        public void BACtrackDidConnect(String s) {
            //setStatus(R.string.TEXT_DISCOVERING_SERVICES);
            Log.wtf(TAG, "BACtrackDidConnect " + s);
        }

        @Override
        public void BACtrackDisconnected() {
            //setStatus(R.string.TEXT_DISCONNECTED);
            //setBatteryStatus("");
            //setCurrentFirmware(null);
            Log.wtf(TAG, "BACtrackDisconnected");
        }

        @Override
        public void BACtrackConnectionTimeout() {
            Log.wtf(TAG, "BACtrackConnectionTimeout");
        }

        @Override
        public void BACtrackFoundBreathalyzer(BluetoothDevice bluetoothDevice) {
            Log.wtf(TAG, "Found breathalyzer : " + bluetoothDevice.getName());
        }

        @Override
        public void BACtrackCountdown(int currentCountdownCount) {
            Log.wtf(TAG, "BACtrackCountdown " + currentCountdownCount);
            //setStatus(getString(R.string.TEXT_COUNTDOWN) + " " + currentCountdownCount);
        }

        @Override
        public void BACtrackStart() {
            //setStatus(R.string.TEXT_BLOW_NOW);
            Log.wtf(TAG, "BACtrackStart");
        }

        @Override
        public void BACtrackBlow() {
            //setStatus(R.string.TEXT_KEEP_BLOWING);
            Log.wtf(TAG, "BACtrackStart");
        }

        @Override
        public void BACtrackAnalyzing() {
            Log.wtf(TAG, "BACtrackAnalyzing");
            //setStatus(R.string.TEXT_ANALYZING);
        }

        @Override
        public void BACtrackResults(float measuredBac) {
            Log.wtf(TAG, "BACtrackResults " + measuredBac);
            Intent i;
            if (measuredBac < 0.4) {
                i = new Intent(getApplicationContext(), GreenActivity.class);
            } else if (measuredBac < 0.8) {
                i = new Intent(getApplicationContext(), YellowActivity.class);
            } else {
                i = new Intent(getApplicationContext(), RedActivity.class);
            }
            i.putExtra(getString(R.string.BAC_LEVEL), measuredBac);
            startActivity(i);
            finish();
            //setStatus(getString(R.string.TEXT_FINISHED) + " " + measuredBac);
        }

        @Override
        public void BACtrackFirmwareVersion(String version) {
            Log.wtf(TAG, "BACtrackFirmwareVersion " + version);
            //setStatus(getString(R.string.TEXT_FIRMWARE_VERSION) + " " + version);
            //setCurrentFirmware(version);
        }

        @Override
        public void BACtrackSerial(String serialHex) {
            Log.wtf(TAG, "BACtrackSerial " + serialHex);
            //setStatus(getString(R.string.TEXT_SERIAL_NUMBER) + " " + serialHex);
        }

        @Override
        public void BACtrackUseCount(int useCount) {

            Log.wtf(TAG, "UseCount: " + useCount);
            //setStatus(getString(R.string.TEXT_USE_COUNT) + " " + useCount);
        }

        @Override
        public void BACtrackBatteryVoltage(float voltage) {
            Log.wtf(TAG, "BACtrackBatteryVoltage " + voltage);
        }

        @Override
        public void BACtrackBatteryLevel(int level) {
            Log.wtf(TAG, "BACtrackBatteryLevel " + level);
            //setBatteryStatus(getString(R.string.TEXT_BATTERY_LEVEL) + " " + level);

        }

        @Override
        public void BACtrackError(int errorCode) {
            Log.wtf(TAG, "BACtrackError " + errorCode);
            //if (errorCode == Errors.wtfRROR_BLOW_ERROR)
            //    setStatus(R.string.TEXT_ERR_BLOW_ERROR);
        }
    };
    private BACtrackAPI mAPI;

    public BACtrackAPI getAPIinstance(Context context) throws LocationServicesNotEnabledException, BluetoothLENotSupportedException, BluetoothNotEnabledException, IOException {
        if (mAPI == null) {
            mAPI = new BACtrackAPI(context, mCallbacks, Util.getProperty("BACTRACK_API_KEY", context));
        }
        return mAPI;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_process);
        bacConnected = false;
        start_button = findViewById(R.id.start_button);
        loading_panel = findViewById(R.id.loadingPanel);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //check if adatpter is available, please note if you running
        //this application in emulator currently there is no support for bluetooth
        if (mBluetoothAdapter == null) {
            Toast.makeText(InitiateActivity.this, "BlueTooth adapter not found", Toast.LENGTH_SHORT).show();
        }
        //check the status and set the button text accordingly
        else {
            while (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }
        }
        try {
            mAPI = getAPIinstance(InitiateActivity.this);
            //mContext = this;
        } catch (BluetoothLENotSupportedException e) {
            e.printStackTrace();
            //this.setStatus(R.string.TEXT_ERR_BLE_NOT_SUPPORTED);
        } catch (BluetoothNotEnabledException e) {
            Toast.makeText(InitiateActivity.this, R.string.TEXT_ERR_BT_NOT_ENABLED, Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            //this.setStatus(R.string.TEXT_ERR_BT_NOT_ENABLED);
        } catch (LocationServicesNotEnabledException e) {
            e.printStackTrace();
            //this.setStatus(R.string.TEXT_ERR_LOCATIONS_NOT_ENABLED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(InitiateActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_FOR_SCAN);
        } else {
                /*
                  Permission already granted, start scan.
                 */
            mAPI.connectToNearestBreathalyzer();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_FOR_SCAN: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /*
                      Only start scan if permissions granted.
                     */
                    mAPI.connectToNearestBreathalyzer();
                }
            }
        }
    }

    public void blow_clicked(View view) {
        boolean result = false;
        if (mAPI != null) {
            result = mAPI.startCountdown();
        }
        if (!result)
            Log.wtf(TAG, "mAPI.startCountdown() failed");
        else
            Log.wtf(TAG, "Blow process start requested");
        startActivity(new Intent(getApplicationContext(), BreatheInActivity.class));
    }
}