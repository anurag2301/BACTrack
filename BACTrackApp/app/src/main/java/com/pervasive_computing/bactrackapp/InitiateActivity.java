package com.pervasive_computing.bactrackapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
    private final static String KEY_LOCATION_UPDATES_RESULT = "location-update-result";
    private static final byte PERMISSIONS_FOR_SCAN = 100;
    private static final String TAG = "InitiateActivity";
    private static final String RESTART_NEEDED = "RESTART_NEEDED";
    private static final String INTERNET_NEEDED = "INTERNET_NEEDED";
    static boolean restartCondition = true;
    TextView wait_message;
    private BACtrackAPI mAPI;
    private final BACtrackAPICallbacks mCallbacks = new BACtrackAPICallbacks() {
        private static final String TAG = "BACtrackAPICallbacks";

        @Override
        public void BACtrackAPIKeyDeclined(String errorMessage) {
            Log.wtf(TAG, "BACtrackAPIKeyDeclined " + errorMessage);
            /*final StringBuilder error = new StringBuilder();
            error.append("Please ");
            if (errorMessage.contains("connect to the Internet"))
                error.append("connect to the Internet and ");
            error.append("restart the App");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    wait_message.setText(error.toString());
                }
            });
            */
            Intent i = new Intent(getApplicationContext(), RestartActivity.class);
            i.putExtra(RESTART_NEEDED, true);
            i.putExtra(INTERNET_NEEDED, true);
            startActivity(i);
            finish();
        }

        @Override
        public void BACtrackAPIKeyAuthorized() {
            Log.wtf(TAG, "BACtrackAPIKeyAuthorized");
        }


        @Override
        public void BACtrackConnected(BACTrackDeviceType bacTrackDeviceType) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setContentView(R.layout.start_process);
                    //      Toast.makeText(InitiateActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                }
            });
            Log.wtf(TAG, "BACtrackConnected " + bacTrackDeviceType.toString());
        }

        @Override
        public void BACtrackDidConnect(String s) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setContentView(R.layout.wait_screen);
                    wait_message.setText(getString(R.string.TEXT_CONNECTING));
                }
            });
            Log.wtf(TAG, "BACtrackDidConnect " + s);
        }

        @Override
        public void BACtrackDisconnected() {
            /*runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(InitiateActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
                }
            });*/
            if (restartCondition) {
                Intent i = new Intent(getApplicationContext(), RestartActivity.class);
                i.putExtra(RESTART_NEEDED, true);
                startActivity(i);
                Log.wtf(TAG, "BACtrackDisconnected");
            }
            restartCondition = true;
            finish();
        }

        @Override
        public void BACtrackConnectionTimeout() {
            Log.wtf(TAG, "BACtrackConnectionTimeout");
            mAPI.disconnect();
        }

        @Override
        public void BACtrackFoundBreathalyzer(BluetoothDevice bluetoothDevice) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //wait_message.setText(String.format("%s.", getString(R.string.TEXT_CONNECTING)));
                }
            });
            Log.wtf(TAG, "Found breathalyzer : " + bluetoothDevice.getName());
        }

        @Override
        public void BACtrackCountdown(int currentCountdownCount) {
            Log.wtf(TAG, "BACtrackCountdown " + currentCountdownCount);
        }

        @Override
        public void BACtrackStart() {
            Log.wtf(TAG, "BACtrackStart");
        }

        @Override
        public void BACtrackBlow() {
            Log.wtf(TAG, getString(R.string.TEXT_KEEP_BLOWING));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setContentView(R.layout.blow);
                }
            });
        }

        @Override
        public void BACtrackAnalyzing() {
            Log.wtf(TAG, "BACtrackAnalyzing");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(InitiateActivity.this, "Analyzing results", Toast.LENGTH_LONG).show();
                    setContentView(R.layout.wait_screen);
                    wait_message.setText(getString(R.string.TEXT_ANALYZING));
                    //
                }
            });
            //startActivity(new Intent(getApplicationContext(), WaitActivity.class));
        }

        @Override
        public void BACtrackResults(float measuredBac) {

            Log.wtf(TAG, "BACtrackResults " + measuredBac);
            disconnect();
            Intent i;
            if (measuredBac < 0.4) {
                i = new Intent(getApplicationContext(), RedActivity.class);
            } else if (measuredBac < 0.8) {
                i = new Intent(getApplicationContext(), YellowActivity.class);
            } else {
                i = new Intent(getApplicationContext(), RedActivity.class);
            }

            String lat_lon = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(KEY_LOCATION_UPDATES_RESULT, "");
            Log.wtf(TAG, "smsLATLON " + lat_lon);
            i.putExtra(getString(R.string.BAC_LEVEL), measuredBac);
            i.putExtra(KEY_LOCATION_UPDATES_RESULT, lat_lon);
            startActivity(i);
            finish();
        }

        @Override
        public void BACtrackFirmwareVersion(String version) {
            Log.wtf(TAG, "BACtrackFirmwareVersion " + version);
        }

        @Override
        public void BACtrackSerial(String serialHex) {
            Log.wtf(TAG, "BACtrackSerial " + serialHex);
        }

        @Override
        public void BACtrackUseCount(int useCount) {
            Log.wtf(TAG, "UseCount: " + useCount);
        }

        @Override
        public void BACtrackBatteryVoltage(float voltage) {
            Log.wtf(TAG, "BACtrackBatteryVoltage " + voltage);
        }

        @Override
        public void BACtrackBatteryLevel(int level) {
            Log.wtf(TAG, "BACtrackBatteryLevel " + level);
        }

        @Override
        public void BACtrackError(int errorCode) {
            Log.wtf(TAG, "BACtrackError " + errorCode);
            disconnect();
            Intent i = new Intent(getApplicationContext(), RestartActivity.class);
            i.putExtra(RESTART_NEEDED, false);
            startActivity(i);
        }
    };

    public BACtrackAPI getAPIinstance(Context context) throws LocationServicesNotEnabledException, BluetoothLENotSupportedException, BluetoothNotEnabledException, IOException {
        if (mAPI == null) {
            mAPI = new BACtrackAPI(context, mCallbacks, Util.getProperty("BACTRACK_API_KEY", context));
        }
        ((GlobalProperties) getApplicationContext()).mAPI = mAPI;
        return mAPI;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*restartCondition = false;
        try {
            mAPI.disconnect();
            Log.wtf(TAG, "Disconnected");
        } catch(Exception e) {
            restartCondition = true;
            Log.e(TAG, "Tried Disconnecting");
        }*/
        setContentView(R.layout.wait_screen);
        wait_message = findViewById(R.id.wait_message);
        wait_message.setText("Trying to connect to BACTrack device\nPlease check if the device is switched on.");
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //check if adaptor is available
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
        } catch (BluetoothLENotSupportedException e) {
            e.printStackTrace();
        } catch (BluetoothNotEnabledException e) {
            Toast.makeText(InitiateActivity.this, R.string.TEXT_ERR_BT_NOT_ENABLED, Toast.LENGTH_SHORT).show();
        } catch (LocationServicesNotEnabledException e) {
            e.printStackTrace();
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

            Log.wtf(TAG, "Trying to connect now");
            connectToNearestBreathalyzer();
        }

    }

    private void connectToNearestBreathalyzer() {
        /*restartCondition = false;
        try {
            mAPI.disconnect();
            Log.wtf(TAG, "Disconnected");
        } catch(Exception e) {
            restartCondition = true;
            Log.e(TAG, "Tried Disconnecting");
        }*/

        try {
            Log.wtf(TAG, "In func:Trying to connect");
            mAPI.connectToNearestBreathalyzer();
        } catch (Exception e) {
            Toast.makeText(InitiateActivity.this, "Please make sure that the device is switched on!", Toast.LENGTH_SHORT).show();
        }
    }

    private void disconnect() {
        restartCondition = false;
        try {
            mAPI.disconnect();
            Log.wtf(TAG, "Disconnected");
        } catch (Exception e) {
            Log.e(TAG, "Tried Disconnecting");
            restartCondition = true;
        }
        finish();
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
                    connectToNearestBreathalyzer();
                }
            }
        }
    }

    public void blow_clicked(View view) {
        boolean result = false;
        if (mAPI != null) {
            result = mAPI.startCountdown();
        }
        if (!result) {
            Intent intent = new Intent(getApplicationContext(), RestartActivity.class);
            intent.putExtra(RESTART_NEEDED, true);
            startActivity(intent);

            Log.wtf(TAG, "mAPI.startCountdown() failed");
        } else {
            startActivity(new Intent(getApplicationContext(), BreatheInActivity.class));
            Log.wtf(TAG, "Blow process start requested");
        }
    }
}
