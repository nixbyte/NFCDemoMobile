package com.nixbyte.nfcdemo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import java.util.ArrayList;

public class MainPresenter implements Presenter<MainView> {

    private static final String TAG = MainPresenter.class.getSimpleName();

    private MainView view;
    private Activity activity;
    private BroadcastReceiver mMessageReceiver;
    private ArrayList<String> log;
    private ArrayAdapter<String> adapter;

    private static final int LOG_SIZE = 300;


    public MainPresenter(Activity activity) {
        this.activity = activity;

        log = new ArrayList<>();
        adapter = new ArrayAdapter<>(activity,android.R.layout.simple_spinner_dropdown_item,log);

        this.mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Get extra data included in the Intent
                boolean isActive = intent.getBooleanExtra("status",false);

                String message = intent.getStringExtra("message");

                if(message != null) {
                    if (log.size() < LOG_SIZE) {
                        log.add(0,message);
                    } else {
                        log.clear();
                        log.add(0,message);
                    }
                }

                adapter.notifyDataSetChanged();

                if(isActive)
                    view.status.setText("NFC считыватель активен");
                else
                    view.status.setText("NFC считыватель не активен");
            }
        };


        LocalBroadcastManager.getInstance(activity).registerReceiver(
                mMessageReceiver, new IntentFilter("updates"));
    }

    @Override
    public void onViewAttached(final MainView view) {
        this.view = view;

        this.view.log.setAdapter(adapter);

        NfcManager manager = (NfcManager) activity.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();

        final Intent intent = new Intent(activity,MyHostApduService.class);

        if (adapter != null && adapter.isEnabled()) {
            this.view.nfcServiceStatus.setText("NFC сервис запущен");
        } else {
            this.view.nfcServiceStatus.setText("NFC сервис не запущен");
        }

        this.view.admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    intent.putExtra("text","Administrator connected and Sended Text: " + view.text.getText().toString());
                    activity.startService(intent);
                    view.moder.setChecked(false);
                    view.user.setChecked(false);
                }
            }
        });

        this.view.moder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    intent.putExtra("text","Moderator connected and Sended Text: " + view.text.getText().toString());
                    activity.startService(intent);
                    view.admin.setChecked(false);
                    view.user.setChecked(false);
                }
            }
        });

        this.view.user.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    intent.putExtra("text","User connected and Sended Text: " + view.text.getText().toString());
                    activity.startService(intent);
                    view.moder.setChecked(false);
                    view.admin.setChecked(false);
                }
            }
        });

        this.view.text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                view.moder.setChecked(false);
                view.admin.setChecked(false);
                view.user.setChecked(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onViewDetached() {

    }

    @Override
    public void onDestroyed() {
        this.view = null;
    }
}
