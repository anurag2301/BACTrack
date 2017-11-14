package com.pervasive_computing.bactrackapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import BACtrackAPI.API.BACtrackAPI;
import BACtrackAPI.Exceptions.BluetoothLENotSupportedException;
import BACtrackAPI.Exceptions.BluetoothNotEnabledException;
import BACtrackAPI.Exceptions.LocationServicesNotEnabledException;

/*
  Created by Pratik on 11/08/2017.
 */

public class InitiateActivity extends BaseActivity {
    private static final byte PERMISSIONS_FOR_SCAN = 100;
    private static final String TAG = "InitiateActivity";
    private static BACtrackAPI mAPI;

    public static BACtrackAPI getAPIinstance(Context context) throws LocationServicesNotEnabledException, BluetoothLENotSupportedException, BluetoothNotEnabledException, IOException {
        if (mAPI == null)
            mAPI = new BACtrackAPI(context, new BAC_Callbacks(), Util.getProperty("BACTRACK_API_KEY", context));
        return mAPI;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_process);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //check if adatpter is available, please note if you running
        //this application in emulator currently there is no support for bluetooth
        if (mBluetoothAdapter == null) {
            Toast.makeText(InitiateActivity.this, "BlueTooth adapter not found", Toast.LENGTH_SHORT).show();
        }
        //check the status and set the button text accordingly
        else {
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
                while (!mBluetoothAdapter.isEnabled()) ;
            }
        }
        try {
            mAPI = getAPIinstance(getApplicationContext());
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
            Log.e(TAG, "mAPI.startCountdown() failed");
        else
            Log.d(TAG, "Blow process start requested");

        startActivity(new Intent(getApplicationContext(), BreatheInActivity.class));
    }
}
