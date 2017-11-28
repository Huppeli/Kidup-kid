package com.kidup.kidup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by t3math00 on 4/5/2017.
 */


public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        if(action.equals("android.intent.action.SCREEN_OFF")){

            Log.d("Lock", "Screen off");

        }
        Log.d("VEIKKO2", "onReceive");

        SharedPreferences getPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean switchState = getPrefs.getBoolean("switch_toggle_lockscreen", false);

        context.startService(new Intent(context, CountDownService.class));
        context.startService(new Intent(context, LockScreenService.class));

        if(switchState) {
            context.stopService(new Intent(context, LockScreenService.class));
        }
        Log.d("switch", "onReceive: stop the locikscreen");

    }
}
