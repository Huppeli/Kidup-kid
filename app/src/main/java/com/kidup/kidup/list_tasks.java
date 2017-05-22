package com.kidup.kidup;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kidup.kidup.models.Task;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class list_tasks extends AppCompatActivity {

    private RecyclerView rv;
    private List<Task> tasks;
    private String kid_id;
    private EditText etId;
    private Button btnSubmit;
    public static final String PREFS_NAME = "MyPrefsFile";



    private KidupAPI mainApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_tasks);
        etId = (EditText)findViewById(R.id.et_id);

        /* Get time from system */
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        Log.d("mai thanh tung", " id saved");

        editor.commit();
        btnSubmit = (Button)findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("maitung", "Clicked");
                String keyword = etId.getText().toString();
                Log.e("maitung", "key word : " + keyword);
                SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPref.edit();
                Log.d("maitung", " id saved");
                editor.putString("kid_id",keyword);
                editor.commit();



                kid_id = sharedPref.getString("kid_id","ditme");
                Log.e("maitung", "kid_id" + kid_id );
                init(kid_id);
            }
        });
        if( sharedPref.getString("kid_id",null) != ""){
            kid_id = sharedPref.getString("kid_id",null);

            Log.e("maitung", "check sharedpref" + kid_id);
//
        }
        if (kid_id != null){
            btnSubmit.setVisibility(View.INVISIBLE);
            etId.setVisibility(View.INVISIBLE);
            mainApi = API.get().create(KidupAPI.class);
            init(kid_id);
        }
    }

    private void  init(String kid_id){
        rv = (RecyclerView) findViewById(R.id.rv_list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        Log.d("maitung", "init: " + kid_id);
        getTask(kid_id);
    }


    private void getTask(String kid_id) {

        mainApi.getTasks(kid_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Task>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Task> tasks) {
                        rv.setAdapter(new Adapter(tasks));
                        rv.getAdapter().notifyDataSetChanged();


                    }
                });
    }
}
