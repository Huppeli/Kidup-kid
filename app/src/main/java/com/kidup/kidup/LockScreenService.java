package com.kidup.kidup;



import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;


/**
 * Created by t3math00 on 4/5/2017.
 */


public class LockScreenService extends Service implements View.OnClickListener {


    private LinearLayout linearLayout;
    private WindowManager.LayoutParams layoutParams;
    private WindowManager windowManager;
    static float steps = 0;

    public int onStartCommand (Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
//
        /*
        if (intent != null) {
            Log.d("VEIKKO", "Recieving some steps" );
            steps = intent.getFloatExtra("steps", 0);

            Log.d("VEIKKO2", "Steps in LC onchange " + steps);

        }
        */
        return START_STICKY;


    }
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
    }



    private void init() {
        linearLayout = new LinearLayout(this);
        windowManager.addView(linearLayout, layoutParams);
        ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.lock_screen, linearLayout);
        View btnUnlock = linearLayout.findViewById(R.id.btn_close);
        btnUnlock.setOnClickListener(this);
        /* Display time in lockscreen */
        try {
            String filename = "myfile";
            FileInputStream inputStream = openFileInput(filename);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            r.close();
            inputStream.close();
            Log.d("FILE", "File contents: " + total);
            TextView txtTextView = (TextView) linearLayout.findViewById(R.id.textViewTime);
            long millisUntilFinished = Long.parseLong(String.valueOf(total));
            /* Format time to HH:MM:SS */
            String hms = String.format("%02d:%02d:%02d" , TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)) );
            /* Display value in textview */
            txtTextView.setText(hms);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        tv_steps.setText(String.valueOf(MainActivity.steps), TextView.BufferType.EDITABLE);
//        btnEarnTime.setOnClickListener(this);
/*
        btnEarnTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Log.d("working", "earn");
                Log.d("steps LSbtn",String.valueOf(steps));
                timeGot = MainActivity.steps *  10000;
                Intent mIntent = new Intent(LockScreenService.this, CountDownService.class);
                mIntent.putExtra("timeGot", timeGot);
                LockScreenService.this.startService(mIntent);
//                MainActivity.timeLeft = MainActivity.timeLeft + MainActivity.timeGot;


                MainActivity.lastCount = MainActivity.lastCount + MainActivity.steps;
                MainActivity.steps = 0;

                Log.d("steps after LSbtn",String.valueOf(MainActivity.steps));
                Log.d("timegot after LSbtn",String.valueOf(MainActivity.timeGot));
                Log.d("timeleft after LSbtn",String.valueOf(MainActivity.timeLeft));
                Log.d("lastcount after LSbtn",String.valueOf(MainActivity.lastCount));
            }
        });
        */
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
//            stopService(new Intent(this, CountDownService.class));
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



}
