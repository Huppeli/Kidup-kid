package com.kidup.kidup;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.kidup.kidup.models.Kid;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeveloperMode extends AppCompatActivity {

    Switch switchToggle;
    Button btnSubmit;
    EditText etId;
    public static final String PREFS_NAME = "MyPrefsFile";
    private KidupAPI mainApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_mode);

        SharedPreferences getPrefs = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        final boolean switchState = getPrefs.getBoolean("switch_toggle_lockscreen", false);


        switchToggle = (Switch) findViewById(R.id.switchToggle);
        if(switchState) {
            switchToggle.setChecked(true);
        }

        switchToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.putBoolean("switch_toggle_lockscreen", switchToggle.isChecked());
                editor.commit();
                Log.d("MIKA","Lockscreen toggle");
                if (switchToggle.isChecked()) {
                    Toast.makeText(DeveloperMode.this, "Disable the Lockscreen",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DeveloperMode.this, "Enable the Lockscreen",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        etId = (EditText)findViewById(R.id.et_id);
        btnSubmit = (Button)findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etId.getText().toString().length() < 12) {
                    Toast.makeText(DeveloperMode.this, "Wrong kid id",
                            Toast.LENGTH_LONG).show();
                } else {
                    Log.e("maitung", "Clicked");
                    String keyword = etId.getText().toString();
                    Log.e("maitung", "key word : " + keyword);
                    SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    Log.d("maitung", " id saved");
                    editor.putString("kid_id",keyword);
                    editor.commit();
                    getKidDetail(keyword);
                }
            }
        });

    }

    private void getKidDetail(String kid_id) {
        mainApi.getKidDetail(kid_id)
                .enqueue(new Callback<Kid>() {
                    @Override
                    public void onResponse(Call<Kid> call, Response<Kid> response) {
                        response.body();
                        if (response.body()!= null) {
                            String kid_name = (String) response.body().name;
                            Log.d("kid_name", "onResponse: " + kid_name);
                            SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("kid_name",kid_name);
                            editor.commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<Kid> call, Throwable t) {

                    }
                });
    }

}
