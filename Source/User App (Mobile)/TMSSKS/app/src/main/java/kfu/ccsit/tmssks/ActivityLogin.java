package kfu.ccsit.tmssks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import kfu.ccsit.tmssks.data.DataMedium;
import kfu.ccsit.tmssks.data.network.NetworkUtils;

public class ActivityLogin extends ActivityBase {

    private EditText mUsernameEt, mPasswordEt;
    private int mLongCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUsernameEt = (EditText) findViewById(R.id.et_username);
        mPasswordEt = (EditText) findViewById(R.id.et_password);

        if (DataMedium.login(this)) {
            startActivity(new Intent(ActivityLogin.this,
                    ActivityMain.class));
            finish();
        }

        mLongCounter = 0;
        findViewById(R.id.setting_activator).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mLongCounter++;
                if (mLongCounter == 3) {
                    startActivity(new Intent(ActivityLogin.this, ActivitySettings.class));
                    mLongCounter = 0;
                }
                return true;
            }
        });
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Signing in...");
            dialog.setCancelable(false);
            dialog.show();
            NetworkUtils.OnRequestListener listener = new NetworkUtils.OnRequestListener() {
                @Override
                public void onFinished(String response, boolean error) {
                    if (!error && DataMedium.login(ActivityLogin.this)) {
                        startActivity(new Intent(ActivityLogin.this,
                                ActivityMain.class));
                        finish();
                    } else {
                        Toast.makeText(ActivityLogin.this, "Error: " + response,
                                Toast.LENGTH_SHORT).show();
                    }

                    try {
                        dialog.dismiss();
                    } catch (Exception ex) {
                        LogUtils.d(ex.getMessage());
                    }
                }
            };

            DataMedium.authorize(this, mUsernameEt.getText().toString(),
                    mPasswordEt.getText().toString(), listener);
        }
    }
}
