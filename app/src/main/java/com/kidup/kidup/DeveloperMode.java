package com.kidup.kidup;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;


public class DeveloperMode extends AppCompatActivity {

    Switch switchToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_mode);

        SharedPreferences getPrefs = getSharedPreferences("com.kidup.kidup",MODE_PRIVATE);
        boolean switchState = getPrefs.getBoolean("switch_toggle_lockscreen", false);


        switchToggle = (Switch) findViewById(R.id.switchToggle);
        if(switchState) {
            switchToggle.setChecked(true);
        }

        switchToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("com.kidup.kidup", MODE_PRIVATE).edit();
                editor.putBoolean("switch_toggle_lockscreen", switchToggle.isChecked());
                editor.commit();
                Log.d("MIKA","Lockscreen toggle");
            }
        });


    }


}
