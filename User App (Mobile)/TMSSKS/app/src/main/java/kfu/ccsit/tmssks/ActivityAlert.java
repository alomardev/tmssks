package kfu.ccsit.tmssks;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class ActivityAlert extends AppCompatActivity {

    public static final String KEY_MESSAGE_EXTRA = "message";
    public static final String KEY_PHONE_SCHOOL_EXTRA = "school_phone";
    public static final String KEY_PHONE_DRIVER_EXTRA = "driver_phone";

    private Handler mDelayer;

    private String mSchoolPhone, mDriverPhone;
    private Ringtone mRingtone;

    public ActivityAlert() {
        mDelayer = new Handler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        String message = getIntent().getStringExtra(KEY_MESSAGE_EXTRA);

        ((TextView) findViewById(R.id.tv_message)).setText(message);

        mSchoolPhone = getIntent().getStringExtra(KEY_PHONE_SCHOOL_EXTRA);
        mDriverPhone = getIntent().getStringExtra(KEY_PHONE_DRIVER_EXTRA);

        mDelayer.postDelayed(new Runnable() {
            @Override
            public void run() {
                Uri notification = Uri.parse("android.resource://kfu.ccsit.tmssks/" + R.raw.alarm);
                mRingtone = RingtoneManager.getRingtone(getApplicationContext(), notification);

                mRingtone.play();

                mDelayer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mRingtone != null) {
                            mRingtone.stop();
                        }
                    }
                }, 10000);
            }
        }, 1000);
    }

    public void onClick(View v) {
        String phone;
        if (v.getId() == R.id.btn_call_driver) {
            phone = mDriverPhone;
        } else {
            phone = mSchoolPhone;
        }

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);

        if (mRingtone != null) {
            mRingtone.stop();
        }
    }

    @Override
    protected void onStop() {
        if (mRingtone != null) {
            mRingtone.stop();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mRingtone != null) {
            mRingtone.stop();
        }
        super.onDestroy();
    }
}
