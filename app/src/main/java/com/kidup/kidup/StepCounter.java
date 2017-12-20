package com.kidup.kidup;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;


public class StepCounter extends Service implements SensorEventListener {

    public static final String STEP_TO_MAIN ="android.intent.stepToMain";
    public static final String STEP_TO_MAIN_ONCREAT ="android.intent.stepToMainOnCreate";
    public static final String STEP_TO_LOCK_ONSTART ="android.intent.stepToLockOnStart";
    private static final String TAG = "StepCounter";
    float lastCount = 0;
    float stepCount = 0;
    float stepsInSensor = 0;

    public StepCounter() {
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: ");

        /* Get number of steps from file*/
        SharedPreferences settings = getSharedPreferences("fileName",0);
        Log.i(TAG, "onCreate: " + stepCount);
        stepCount = settings.getFloat("savedSteps",0);
        Log.i(TAG, "onCreate: " + stepCount);

        SensorManager smgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor stepCounter = smgr.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        IntentFilter intentFilterForOldStep = new IntentFilter("android.intent.oldstepsToService");

        if (stepCounter != null) {
            Log.i(TAG, " Sensor found");
            smgr.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);
            registerReceiver(receiverfromMain, intentFilterForOldStep);
        }
        else {
            Toast.makeText(this, getString(R.string.sensor_not_found), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand:");
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // Get Noti from mainOnCreate, request steps
        if (intent != null) {
            if (intent.hasExtra("mainActivityStarted")) {
                Log.i(TAG, "Recieve noti from Main started");
                Intent RTReturn = new Intent(STEP_TO_MAIN_ONCREAT);
                RTReturn.putExtra("steps", stepCount);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(RTReturn);
            }
            if (intent.hasExtra("LockScreenServiceStarted")) {
                Log.i(TAG, "Recieve noti from Lockstarted");
                Intent RTReturn = new Intent(STEP_TO_LOCK_ONSTART);
                RTReturn.putExtra("steps", stepCount);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(RTReturn);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service finished", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onDestroy: ");
        /*
        Stops listening to sensor
        SensorManager smgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor stepCounter = smgr.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        smgr.unregisterListener(this, stepCounter);
        */


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.i(TAG, "onSensorChanged: Some shit happen");

        stepCount++;

        Log.i(TAG, "onSensorChanged: " + stepsInSensor + stepCount + lastCount );
        Intent intent = new Intent(STEP_TO_MAIN).putExtra("steps", stepCount);
        this.sendBroadcast(intent);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    BroadcastReceiver receiverfromMain = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.hasExtra("oldsteps")) {
                    lastCount = intent.getFloatExtra("oldsteps",0);
                    Log.i(TAG, "onReceive: " + lastCount);

                    stepCount = 0;
                }
            }

        }
    };

}
