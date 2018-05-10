package com.nixbyte.nfcdemo;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

public class MainView {

    public TextView status;
    public TextView nfcServiceStatus;
    public ListView log;
    public Switch admin;
    public Switch moder;
    public Switch user;
    public EditText text;

    public MainView(AppCompatActivity activity, View rootView) {
        this.status = rootView.findViewById(R.id.nfc_status);
        this.nfcServiceStatus = rootView.findViewById(R.id.nfc_service_status);
        this.admin = rootView.findViewById(R.id.admin);
        this.moder = rootView.findViewById(R.id.moder);
        this.user = rootView.findViewById(R.id.user);
        this.text = rootView.findViewById(R.id.text);
        this.log = rootView.findViewById(R.id.log);
    }
}
