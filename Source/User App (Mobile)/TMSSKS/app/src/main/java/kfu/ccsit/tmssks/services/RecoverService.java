package kfu.ccsit.tmssks.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RecoverService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, NotifierService.class));
    }
}
