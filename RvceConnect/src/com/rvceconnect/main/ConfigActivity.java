package com.rvceconnect.main;

import com.rvceconnect.main.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ConfigActivity extends Activity {

    private Button lBtnCancel;
    private Button lBtnSave;

    private EditText lTxfUser;
    private EditText lTxfPass;
    private EditText lTxfURL;
    private Activity lInstance;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.config_activity);

        lBtnCancel = (Button) findViewById(R.id.cfgBtnCancel);
        lBtnSave = (Button) findViewById(R.id.cfgBtnSave);
        lTxfURL = (EditText) findViewById(R.id.cfgTxfURL);
        lTxfUser = (EditText) findViewById(R.id.cfgTxfUsername);
        lTxfPass = (EditText) findViewById(R.id.cfgTxfPassword);
        lInstance = this;

        lBtnCancel.setOnClickListener(new OnClickListener() {

			@Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "DISCARDING...",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        lBtnSave.setOnClickListener(new OnClickListener() {

			@Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "SAVING...",
                        Toast.LENGTH_SHORT).show();

                JWC.setURL(lTxfURL.getText().toString());
                JWC.setUsername(lTxfUser.getText().toString());
                JWC.setPassword(lTxfPass.getText().toString());
                JWC.saveSettings(lInstance);
                finish();
            }
        });
        JWC.loadSettings(this);
        lTxfURL.setText(JWC.getURL());
        lTxfUser.setText(JWC.getUsername());
        lTxfPass.setText(JWC.getPassword());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
