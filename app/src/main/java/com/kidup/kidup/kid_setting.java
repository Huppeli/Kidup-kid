package com.kidup.kidup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class kid_setting extends AppCompatActivity {

    private Button passwordSubmit;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        passwordSubmit = (Button) findViewById(R.id.passwordSubmit);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        passwordSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordInput.getText()!= null) {
                    if (passwordInput.getText().toString().equals("Kidup123")) {
                        Intent intent = new Intent(kid_setting.this, DeveloperMode.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(kid_setting.this, "Wrong password!",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(kid_setting.this, "Please input password!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
