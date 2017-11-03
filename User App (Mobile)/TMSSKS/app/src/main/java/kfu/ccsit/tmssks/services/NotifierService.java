package kfu.ccsit.tmssks.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

import kfu.ccsit.tmssks.PrefUtils;
import kfu.ccsit.tmssks.data.DataMedium;
import kfu.ccsit.tmssks.data.network.NetworkUtils;

public class NotifierService extends Service {

    private static final int PERIOD = 2000;
    private boolean mEnableCheck;
    private Timer mTimer;
    private TimerTask mTask;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mEnableCheck = true;
        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                if (mEnableCheck) {
                    DataMedium.loadNotifications(getApplicationContext(),
                            new NetworkUtils.OnRequestListener() {
                                @Override
                                public void onFinished(String response, boolean error) {
                                    mEnableCheck = true;
                                }
                            });
                    mEnableCheck = false;
                }
            }
        };

        mTimer.scheduleAtFixedRate(mTask, 0, PERIOD);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        try {
            mTimer.cancel();
            mTask.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int id = PrefUtils.getDefault(getApplicationContext()).getInt(PrefUtils.KEY_USER_ID, -1);
        if (id > -1) {
            Intent intent = new Intent(getApplicationContext(), RecoverService.class);
            sendBroadcast(intent);
        }
    }
}
