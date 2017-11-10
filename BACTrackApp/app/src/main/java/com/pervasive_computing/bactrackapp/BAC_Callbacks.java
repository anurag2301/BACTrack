package com.pervasive_computing.bactrackapp;

import android.bluetooth.BluetoothDevice;

import BACtrackAPI.API.BACtrackAPICallbacks;
import BACtrackAPI.Constants.BACTrackDeviceType;

/*
  Created by Pratik on 11/09/2017.
 */

public class BAC_Callbacks implements BACtrackAPICallbacks {

    @Override
    public void BACtrackAPIKeyDeclined(String errorMessage) {
        //APIKeyVerificationAlert verify = new APIKeyVerificationAlert();
        //verify.execute(errorMessage);
    }

    @Override
    public void BACtrackAPIKeyAuthorized() {

    }

    @Override
    public void BACtrackConnected(BACTrackDeviceType bacTrackDeviceType) {
        //setStatus(R.string.TEXT_CONNECTED);
    }

    @Override
    public void BACtrackDidConnect(String s) {
        //setStatus(R.string.TEXT_DISCOVERING_SERVICES);
    }

    @Override
    public void BACtrackDisconnected() {
        //setStatus(R.string.TEXT_DISCONNECTED);
        //setBatteryStatus("");
        //setCurrentFirmware(null);
    }

    @Override
    public void BACtrackConnectionTimeout() {

    }

    @Override
    public void BACtrackFoundBreathalyzer(BluetoothDevice bluetoothDevice) {
        //Log.d(TAG, "Found breathalyzer : " + bluetoothDevice.getName());
    }

    @Override
    public void BACtrackCountdown(int currentCountdownCount) {
        //setStatus(getString(R.string.TEXT_COUNTDOWN) + " " + currentCountdownCount);
    }

    @Override
    public void BACtrackStart() {
        //setStatus(R.string.TEXT_BLOW_NOW);
    }

    @Override
    public void BACtrackBlow() {
        //setStatus(R.string.TEXT_KEEP_BLOWING);
    }

    @Override
    public void BACtrackAnalyzing() {
        //setStatus(R.string.TEXT_ANALYZING);
    }

    @Override
    public void BACtrackResults(float measuredBac) {
        //setStatus(getString(R.string.TEXT_FINISHED) + " " + measuredBac);
    }

    @Override
    public void BACtrackFirmwareVersion(String version) {
        //setStatus(getString(R.string.TEXT_FIRMWARE_VERSION) + " " + version);
        //setCurrentFirmware(version);
    }

    @Override
    public void BACtrackSerial(String serialHex) {
        //setStatus(getString(R.string.TEXT_SERIAL_NUMBER) + " " + serialHex);
    }

    @Override
    public void BACtrackUseCount(int useCount) {
        //Log.d(TAG, "UseCount: " + useCount);
        //setStatus(getString(R.string.TEXT_USE_COUNT) + " " + useCount);
    }

    @Override
    public void BACtrackBatteryVoltage(float voltage) {

    }

    @Override
    public void BACtrackBatteryLevel(int level) {
        //setBatteryStatus(getString(R.string.TEXT_BATTERY_LEVEL) + " " + level);

    }

    @Override
    public void BACtrackError(int errorCode) {
        //if (errorCode == Errors.ERROR_BLOW_ERROR)
        //    setStatus(R.string.TEXT_ERR_BLOW_ERROR);
    }
    /*private class APIKeyVerificationAlert extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return urls[0];
        }

        @Override
        protected void onPostExecute(String result) {
            AlertDialog.Builder apiApprovalAlert = new AlertDialog.Builder(mContext);
            apiApprovalAlert.setTitle("API Approval Failed");
            apiApprovalAlert.setMessage(result);
            apiApprovalAlert.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mAPI.disconnect();
                            setStatus(R.string.TEXT_DISCONNECTED);
                            dialog.cancel();
                        }
                    });

            apiApprovalAlert.create();
            apiApprovalAlert.show();
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

                      //Only start scan if permissions granted.

                    mAPI.connectToNearestBreathalyzer();
                }
            }
        }
    }

    public void connectNearestClicked(View v) {
        if (mAPI != null) {
            setStatus(R.string.TEXT_CONNECTING);
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_FOR_SCAN);
            } else {

                  //Permission already granted, start scan.
                mAPI.connectToNearestBreathalyzer();
            }
        }
    }

    public void disconnectClicked(View v) {
        if (mAPI != null) {
            mAPI.disconnect();
        }
    }

    public void getFirmwareVersionClicked(View v) {
        boolean result = false;
        if (mAPI != null) {
            result = mAPI.getFirmwareVersion();
        }
        if (!result)
            Log.e(TAG, "mAPI.getFirmwareVersion() failed");
        else
            Log.d(TAG, "Firmware version requested");
    }

    public void getSerialNumberClicked(View view) {
        boolean result = false;
        if (mAPI != null) {
            result = mAPI.getSerialNumber();
        }
        if (!result)
            Log.e(TAG, "mAPI.getSerialNumber() failed");
        else
            Log.d(TAG, "Serial Number requested");
    }

    public void requestUseCountClicked(View view) {
        boolean result = false;
        if (mAPI != null) {
            result = mAPI.getUseCount();
        }
        if (!result)
            Log.e(TAG, "mAPI.requestUseCount() failed");
        else
            Log.d(TAG, "Use count requested");
    }

    public void requestBatteryVoltageClicked(View view) {
        boolean result = false;
        if (mAPI != null) {
            result = mAPI.getBreathalyzerBatteryVoltage();
        }
        if (!result)
            Log.e(TAG, "mAPI.getBreathalyzerBatteryVoltage() failed");
        else
            Log.d(TAG, "Battery voltage requested");
    }

    public void startBlowProcessClicked(View v) {
        boolean result = false;
        if (mAPI != null) {
            result = mAPI.startCountdown();
        }
        if (!result)
            Log.e(TAG, "mAPI.startCountdown() failed");
        else
            Log.d(TAG, "Blow process start requested");
    }

    private void setStatus(int resourceId) {
        this.setStatus(this.getResources().getString(resourceId));
    }

    private void setStatus(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, message);
                statusMessageTextView.setText(String.format("Status:\n%s", message));
                if (message.startsWith("Done"))
                    sendSMS(message.split(":")[1]);
            }
        });
    }

    private void setBatteryStatus(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, message);
                batteryLevelTextView.setText(String.format("\n%s", message));
            }
        });
    }

    public void setCurrentFirmware(@Nullable String currentFirmware) {
        this.currentFirmware = currentFirmware;

        String[] firmwareSplit = new String[0];
        if (currentFirmware != null) {
            firmwareSplit = currentFirmware.split("\\s+");
        }
        if (firmwareSplit.length >= 1
                && Long.valueOf(firmwareSplit[0]) >= Long.valueOf("201510150003")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (serialNumberButton != null) {
                        serialNumberButton.setVisibility(View.VISIBLE);
                    }
                    if (useCountButton != null) {
                        useCountButton.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (serialNumberButton != null) {
                        serialNumberButton.setVisibility(View.GONE);
                    }
                    if (useCountButton != null) {
                        useCountButton.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    protected void sendSMS(String bac) {
        Log.i("Send SMS", "");

        StringBuilder numbers = new StringBuilder();
        DBHelper db = new DBHelper(getApplicationContext());
        for (String phone : db.getAllContacts().keySet()) {
            numbers.append(phone).append(";");
        }
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", numbers.toString());
        smsIntent.putExtra("sms_body", "Test " + bac);

        try {
            startActivity(smsIntent);
            //finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }
*/
}