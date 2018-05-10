package com.nixbyte.nfcdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private MainView view;
    private MainPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = new MainView(this, findViewById(android.R.id.content));

        presenter = new MainPresenter(this);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isChangingConfigurations())
            presenter.onDestroyed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewAttached(view);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onViewDetached();
    }

}
