package com.kidup.kidup;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;



public class CountDownService extends Service {

    public static final String PREFS_NAME = "MyPrefsFile";

    private static final String TAG = "CountDownService";
    public static final String BROADCAST_ACTION ="com.thedroidboy.lockscreentest";
    Intent bi = new Intent(BROADCAST_ACTION);


    CountDownTimer mTimer = null;
    long timeLeft = 0;
    float timeGot = 0 ;
    boolean timer_was_touched = false;
//    public static class CounterClass extends CountDownTimer {
//
//        public CounterClass(long millisInFuture, long countDownInterval){
//            super(millisInFuture, countDownInterval);
//        }
//
////        @Override
////        public void onTick(long millisUntilFinished){
////            long millis = millisUntilFinished ;
//////            MainActivity.timeLeft = millisUntilFinished;
////            String hms = String.format("%02d:%02d:%02d" , TimeUnit.MILLISECONDS.toHours(millis),TimeUnit.MILLISECONDS.toMinutes(millis)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)) );
////
////            System.out.println(hms);
////            MainActivity.textViewTime.setText(hms);
////        }
////
////        @Override
////        public void onFinish(){
////
////
////            MainActivity.textViewTime.setText("Out of time!");
////            boolean active = MainActivity.deviceManager.isAdminActive(MainActivity.compName);
////            if (active) {
////                MainActivity.deviceManager.lockNow();
////            }
////        }
//    }

    public CountDownService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("VEIKKO2", "On Create in the service");




//        /* Get time from system */
//        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//
//        SharedPreferences.Editor editor = sharedPref.edit();
//        Log.d("VEIKKO2", " time saved");
//
//        editor.commit();
//
//        /* If no time left give user 20 seconds */
//        if ( sharedPref == null  ) {
//            editor.putLong("Time",20000);
//        }

//        timeLeft = sharedPref.getLong("Time",0);

        String filename = "myfile";
        String line = null;
        try {
            FileInputStream inputStream = openFileInput(filename);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            line = r.readLine();
            Log.d(TAG, "onCreate: line in myfile " + line);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (line != null && !line.isEmpty()) {
            timeLeft = Long.parseLong(line);
        } else {
            timeLeft = 10000;
        }

        Log.d("VEIKKO2", " onStartcommand in oncreate" + timeLeft);

        startTimer();

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        IntentFilter ScreenOn = new IntentFilter(Intent.ACTION_SCREEN_ON);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("VEIKKO2", "ACTION_SCREEN_OFF");

                if (timer_was_touched == true){
                    mTimer.cancel();
                    Log.d("VEIKKO2", "cancel timer ");
                }
            }
        },filter );

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, Intent intent) {
                Log.d("VEIKKO2", "ACTION_SCREEN_ON");
                if (mTimer != null)
                {
                    mTimer.cancel();
                    Log.d("VEIKKO2", "cancel timer ");
                }

                if (timer_was_touched == true){

                    mTimer.cancel();
                    Log.d("VEIKKO2", "cancel timer ");

                }

                if (timeGot != 0){

                    mTimer.cancel();
                    Log.d("VEIKKO2", "cancel timer ");

                }

                mTimer = new CountDownTimer(timeLeft, 1000) {

                    public void onTick(long millisUntilFinished) {
                        long millis = millisUntilFinished ;
                        String hms = String.format("%02d:%02d:%02d" , TimeUnit.MILLISECONDS.toHours(millis),TimeUnit.MILLISECONDS.toMinutes(millis)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)) );

                        Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
                        bi.putExtra("countdown", millisUntilFinished);
                        sendBroadcast(bi);


                        System.out.println(hms);
                       /* MainActivity.textViewTime.setText(hms); */
                       /* MainActivity.pieView.setInnerText(hms); */
                        timeLeft = millisUntilFinished;


                        //TODO : Make this send by broadcast receiver
//                        Intent lockscreenIntent = new Intent(CountDownService.this, LockScreenService.class);
//                        lockscreenIntent.putExtra("timeToLock", millisUntilFinished);
//                        CountDownService.this.startService(lockscreenIntent);

                        if ( timeLeft < 300000) {
                            Log.d("VEIKKO2","Time left is low");

                            PendingIntent contentIntent = PendingIntent.getActivity(context,0,
                                    new Intent(context, MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);
                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(context)
                                            .setSmallIcon(R.drawable.panda_proud_gray)
                                            .setContentTitle(getString(R.string.notification_hey))
                                            .setContentText(getString(R.string.notification_you))
                                            .setContentIntent(contentIntent);

                            int mNotificationId = 001;

                            NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                            // build and issue
                            mNotifyMgr.notify(mNotificationId, mBuilder.build());
                        }

                        /*
                        Intent mIntent = new Intent(CountDownService.this, MainActivity.class);
                        mIntent.putExtra("timeLeft", millisUntilFinished);
                        CountDownService.this.startService(mIntent);
                        */

                        Log.d("VEIKKO2", "Time left on tick : " + timeLeft);
                        Log.d("VEIKKO2", "Sending time left to main : " + millisUntilFinished);
                    }



                    public void onFinish() {
                        Log.d("VEIKKO2", "Timer onFinish");
                        Log.d(TAG, "Timer finished");
                      /*  MainActivity.textViewTime.setText("Out of time!"); */
                        /* MainActivity.pieView.setInnerText("Out of time!"); */
                        DevicePolicyManager deviceManager = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
                        ComponentName compName = new ComponentName(CountDownService.this, MyAdmin.class);
                        boolean active = deviceManager.isAdminActive(compName);
                        if (active) {
                            Log.d("VEIKKO2", "Locking the screen!");

                            /* If user has no time left give 10 seconds */
                            timeLeft = 10000;

                            deviceManager.lockNow();
                        }
                    }
                }.start();
                }

        },ScreenOn );

       // MainActivity.timer = new CounterClass((long)MainActivity.timeLeft,1000);
       // MainActivity.timer.start();

    }


    public int onStartCommand (Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);





//        Log.d(PREFS_NAME + "onstart" ,"" + timeLeft);
//        Log.d("VEIKKO2", "Time got" + timeGot);
        if (intent != null) {
            timeGot = intent.getFloatExtra("timeGot", 0);
            Log.d(TAG, "onStartCommand in on startcommand: " + timeLeft);
            timeLeft = timeLeft + (long)timeGot;
            mTimer.cancel();
            startTimer();
            Log.d("VEIKKO2", "cancel timer if there is some step");

//            timeGot = 0;
            Log.d("VEIKKO2", "Time got" + timeGot);
            Log.d("VEIKKO2", "Time left after take more step " + timeLeft);
        }
        return START_STICKY;

    }
    @SuppressLint("NewApi")


    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("VEIKKO2", "On Destroy");
    }


    protected void startTimer()
    {


//        if (mTimer != null)
//        {
//            mTimer.cancel();
//        }
//        Intent intent = new Intent();
//        timeGot = intent.getFloatExtra("timeGot", 0);
//        timeLeft = timeLeft + (long) timeGot;
        timer_was_touched = true;
//        mTimer.cancel();
        mTimer = new CountDownTimer(timeLeft, 1000) {

            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished ;
                String hms = String.format("%02d:%02d:%02d" , TimeUnit.MILLISECONDS.toHours(millis),TimeUnit.MILLISECONDS.toMinutes(millis)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)) );


                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
                bi.putExtra("countdown", millisUntilFinished);
                sendBroadcast(bi);

                System.out.println(hms);
               /* MainActivity.textViewTime.setText(hms); */
               /* MainActivity.pieView.setInnerText(hms); */
                timeLeft = millisUntilFinished;
                Intent mIntent = new Intent(CountDownService.this, MainActivity.class);
                mIntent.putExtra("timeLeft", millisUntilFinished);
                CountDownService.this.startService(mIntent);

                //TODO: Make this as broadcast receiver
//                Intent lockscreenIntent = new Intent(CountDownService.this, LockScreenService.class);
//                lockscreenIntent.putExtra("timeToLock", millisUntilFinished);
//                CountDownService.this.startService(lockscreenIntent);
//                Log.d("VEIKKO2", "Time left ontick in service" + timeLeft);

//                if (timeLeft <= 100000){
//                    boolean timenoti = true;
//                    Intent timenotiIntent = new Intent(CountDownService.this, MainActivity.class);
//                    mIntent.putExtra("timenoti", timenoti);
//                    CountDownService.this.startService(timenotiIntent);
//                    Log.d("VEIKKO2" , "service sending timenoti Intent " + timenotiIntent);
//
//                }


            }

            public void onFinish() {
                Log.d("VEIKKO2", "Timer onFinish");
                /*MainActivity.textViewTime.setText("Out of time!"); */
                /*MainActivity.pieView.setInnerText("Out of time!"); */
                DevicePolicyManager deviceManager = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
                ComponentName compName = new ComponentName(CountDownService.this, MyAdmin.class);
                boolean active = deviceManager.isAdminActive(compName);
                if (active) {
                    Log.d("VEIKKO2", "Locking the screen!");
                    timeLeft = 10000;

                    deviceManager.lockNow();
                }
            }
        }.start();

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
