package com.kidup.kidup;



import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
<<<<<<< HEAD
import android.content.SharedPreferences;
=======
>>>>>>> dfe131ace2368a137c6219fb8ae7032c825d915d
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

<<<<<<< HEAD
import java.util.concurrent.TimeUnit;

=======
>>>>>>> dfe131ace2368a137c6219fb8ae7032c825d915d

/**
 * Created by t3math00 on 4/5/2017.
 */


public class LockScreenService extends Service implements View.OnClickListener {


    private LinearLayout linearLayout;
    private WindowManager.LayoutParams layoutParams;
    private WindowManager windowManager;
    static float timeGot = 0;
    static float steps = 0;
<<<<<<< HEAD
    public static final String PREFS_NAME = "MyPrefsFile";
    long timeLeftInSeconds = 0;
=======
>>>>>>> dfe131ace2368a137c6219fb8ae7032c825d915d

    public int onStartCommand (Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
//
        if (intent != null) {
<<<<<<< HEAD
            Log.d("VEIKKO1", "Recieving some steps" );
=======
            Log.d("VEIKKO", "Recieving some steps" );
>>>>>>> dfe131ace2368a137c6219fb8ae7032c825d915d
            steps = intent.getFloatExtra("steps", 0);

            Log.d("VEIKKO2", "Steps in LC onchange " + steps);

        }
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
        Log.d("steps LS",String.valueOf(MainActivity.steps));
        Log.d("VEIKKO2", "Current time " + String.valueOf(MainActivity.timeLeft));
<<<<<<< HEAD
=======



>>>>>>> dfe131ace2368a137c6219fb8ae7032c825d915d
    }



    private void init() {
<<<<<<< HEAD

=======
>>>>>>> dfe131ace2368a137c6219fb8ae7032c825d915d
        linearLayout = new LinearLayout(this);
        windowManager.addView(linearLayout, layoutParams);
        ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.lock_screen, linearLayout);
        View btnUnlock = linearLayout.findViewById(R.id.btn_close);
        btnUnlock.setOnClickListener(this);
<<<<<<< HEAD
        TextView timeLeft = (TextView) linearLayout.findViewById(R.id.textViewTime);
        SharedPreferences shared = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        timeLeftInSeconds = shared.getLong("Time", 0);
        // SET TIMELEFT ON LOCKSCREEN
        Log.d("VEIKKO2","Set time on lock screen");
        Long millis = timeLeftInSeconds;
        String hms = String.format("%02d:%02d:%02d" , TimeUnit.MILLISECONDS.toHours(millis),TimeUnit.MILLISECONDS.toMinutes(millis)
                -TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),TimeUnit.MILLISECONDS.toSeconds(millis)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)) );
        timeLeft.append(String.valueOf(hms));

        //View btnEarnTime = linearLayout.findViewById(R.id.btnFinish);
=======
        View btnEarnTime = linearLayout.findViewById(R.id.btnFinish);
>>>>>>> dfe131ace2368a137c6219fb8ae7032c825d915d




//        tv_steps.setText(String.valueOf(MainActivity.steps), TextView.BufferType.EDITABLE);
//        btnEarnTime.setOnClickListener(this);

<<<<<<< HEAD
        /*btnEarnTime.setOnClickListener(new View.OnClickListener(){
=======
        btnEarnTime.setOnClickListener(new View.OnClickListener(){
>>>>>>> dfe131ace2368a137c6219fb8ae7032c825d915d
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
<<<<<<< HEAD
        });*/
    }


=======
        });
    }

>>>>>>> dfe131ace2368a137c6219fb8ae7032c825d915d
    @Override
    public void onClick (View view){
//        if (MainActivity.timeLeft > 0) {
//
//            MainActivity.startTimer();

        windowManager.removeView(linearLayout);
        linearLayout = null;
//        }
    }
<<<<<<< HEAD

=======
>>>>>>> dfe131ace2368a137c6219fb8ae7032c825d915d
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
