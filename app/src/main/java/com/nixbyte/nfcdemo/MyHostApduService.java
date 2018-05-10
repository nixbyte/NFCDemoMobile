package com.nixbyte.nfcdemo;

import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class MyHostApduService extends HostApduService {

    public static final String TAG = MyHostApduService.class.getSimpleName();

    private String text = "empty";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getStringExtra("text") != null && !intent.getStringExtra("text").isEmpty())
            text = intent.getStringExtra("text");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
        if (selectAidApdu(apdu)) {
            sendReaderStatusToActivity(true);
            return getWelcomeMessage();
        } else {
            sendReaderMessagesToActivity(new String(apdu));
            return getNextMessage();
        }
    }

    private byte[] getWelcomeMessage() {
        return "Hello Desktop!".getBytes();
    }

    private byte[] getNextMessage() {
        return text.getBytes();
    }

    private boolean selectAidApdu(byte[] apdu) {
        return apdu.length >= 2 && apdu[0] == (byte) 0 && apdu[1] == (byte) 0xa4;
    }

    @Override
    public void onDeactivated(int reason) {
        sendReaderStatusToActivity(false);
    }

    private void sendReaderStatusToActivity(boolean isActive) {
        Intent intent = new Intent("updates");
        intent.putExtra("status", isActive);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void sendReaderMessagesToActivity(String message) {
        Intent intent = new Intent("updates");
        intent.putExtra("status", true);
        intent.putExtra("message", message);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }
}

