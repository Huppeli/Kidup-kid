package com.kidup.kidup;



import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;


/**
 * Created by t3math00 on 4/5/2017.
 */


public class LockScreenService extends Service implements View.OnClickListener {


    private static final String TAG = LockScreenService.class.getSimpleName();
    private LinearLayout linearLayout;
    private WindowManager.LayoutParams layoutParams;
    private WindowManager windowManager;
    private Button btn_getTimeFromLock;
    private float timeGot;
    private TextView tv_StepInLock;
    private float stepsInLock;
    private long millisUntilFinished;
    private TextView tv_textViewTime;
    private String hms;
    private float steps;
    LocalBroadcastManager broadcastManager;


    public int onStartCommand (Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent!= null) {
            if (intent.getExtras()!= null) {
                if (intent.hasExtra("timeToLock")) {
                millisUntilFinished = intent.getLongExtra("timeToLock", 0);

             /* Format time to HH:MM:SS */
                hms = String.format("%02d:%02d:%02d" , TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)) );
                Log.d("time on lock", "onStartCommand: " + hms);
//                tv_textViewTime.setText(" ");
                }
            }
        }

        // Send Notification that Lock Started to StepCounterService
        Intent startLock = new Intent(this, StepCounter.class);
        startLock.putExtra("LockScreenServiceStarted","true");
        Log.i(TAG, "Send noti to Step");
        startService(startLock);

        return START_STICKY;


    }

    // Receive steps Oncreate and update UI
    private BroadcastReceiver reciveStepOnStart = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent!= null) {
                if (intent.hasExtra("steps")) {
                    Log.i(TAG, "Recive Step on Start");
                    steps = intent.getFloatExtra("steps",0);
                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenReceiver, intentFilter);
        windowManager = ((WindowManager) getSystemService(WINDOW_SERVICE));
        layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN //draw on status bar
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION,// hiding the home screen button
                PixelFormat.TRANSLUCENT);

        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter IntentGotFromStep = new IntentFilter();
        IntentGotFromStep.addAction(StepCounter.STEP_TO_LOCK_ONSTART);
        broadcastManager.registerReceiver(reciveStepOnStart, IntentGotFromStep);
    }



    private void init() {
        linearLayout = new LinearLayout(this);
        windowManager.addView(linearLayout, layoutParams);
        ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.lock_screen, linearLayout);
        tv_StepInLock = (TextView) linearLayout.findViewById(R.id.tv_StepInLock);
        tv_StepInLock.setText(String.valueOf(steps));
        View btnUnlock = linearLayout.findViewById(R.id.btn_close);
        btn_getTimeFromLock = (Button) linearLayout.findViewById(R.id.btn_getTimeFromLock);
        tv_textViewTime = (TextView) linearLayout.findViewById(R.id.textViewTime);
        btnUnlock.setOnClickListener(this);
        tv_textViewTime.setText(hms + " ", TextView.BufferType.EDITABLE);
        
        IntentFilter intentFilterForStepService = new IntentFilter(StepCounter.STEP_TO_MAIN);
        registerReceiver(brStepReceiver, intentFilterForStepService);
        Log.i(TAG, "init: ");


        /* Display time in lockscreen */
//        try {
//            String filename = "myfile";
//            FileInputStream inputStream = openFileInput(filename);
//            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
//            StringBuilder total = new StringBuilder();
//            String line;
//            while ((line = r.readLine()) != null) {
//                total.append(line);
//            }
//            r.close();
//            inputStream.close();
//            Log.d("FILE", "File contents: " + total);
//
//            long millisUntilFinished = Long.parseLong(String.valueOf(total));
//            /* Format time to HH:MM:SS */
//            String hms = String.format("%02d:%02d:%02d" , TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
//                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
//                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)) );
//            /* Display value in textview */
//            txtTextView.setText(hms);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        btn_getTimeFromLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newSteps = 0;
                /* Save steps to file */
                String saveLocation = "stepcount";
                int stepsInt = Math.round(steps);
                String input =  Integer.toString(stepsInt);
                FileOutputStream outputStream;
                try {
                    /* Get old file */
                    FileInputStream fis = openFileInput(saveLocation);
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    Log.d("MIKA","READ FROM FILE" + sb );
                    String oldSteps = sb.toString();
                    Log.d("MIKA", "oldSteps = " + oldSteps);
                    newSteps = stepsInt + Integer.valueOf(oldSteps);
                    input = String.valueOf(newSteps);
                    Log.d("MIKA", "New value = " + input);
                    br.close();
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    /* Save to file */
                    outputStream = openFileOutput(saveLocation, Context.MODE_PRIVATE);
                    outputStream.write(input.getBytes());
                    outputStream.close();
                    Log.d("MIKA", "Saved steps to file");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                timeGot = steps * 5000;
                MainActivity.lastCount = MainActivity.lastCount + MainActivity.steps;
                MainActivity.steps = 0;
                stepsInLock = 0;
                tv_StepInLock.setText(String.valueOf(stepsInLock) + " " + getString(R.string.steps), TextView.BufferType.EDITABLE);

                Intent oldSteps = new Intent("android.intent.oldstepsToService").putExtra("oldsteps", steps);
                sendBroadcast(oldSteps);

                if (timeGot != 0) {
                    Intent mIntent = new Intent(LockScreenService.this, CountDownService.class);
                    mIntent.putExtra("timeGot", timeGot);
                    LockScreenService.this.startService(mIntent);
                    Log.d("Lockscreenservice", "Sending more time");
                }
                Toast.makeText(LockScreenService.this, "Earn time : " + timeGot,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick (View view){
//        if (MainActivity.timeLeft > 0) {
//
//            MainActivity.startTimer();

        windowManager.removeView(linearLayout);
        linearLayout = null;
//        }
    }
    @Override
    public void onDestroy () {
        unregisterReceiver(screenReceiver);
        broadcastManager.unregisterReceiver(reciveStepOnStart);
        super.onDestroy();

    }

    BroadcastReceiver screenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF) && linearLayout == null) {

                init();
            }
        }
    };
    BroadcastReceiver brStepReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            steps = intent.getFloatExtra("steps",0);
            tv_StepInLock.setText(String.valueOf(steps));
            Log.i(TAG, "onReceive: " + steps);
        }
    };



}
