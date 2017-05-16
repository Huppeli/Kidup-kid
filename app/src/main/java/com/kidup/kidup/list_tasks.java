package com.kidup.kidup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.kidup.kidup.models.Task;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class list_tasks extends AppCompatActivity {

    private RecyclerView rv;
    private List<Task> tasks;


    private KidupAPI mainApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_tasks);



        mainApi = API.get().create(KidupAPI.class);
        init();
    }

    private void  init(){
        rv = (RecyclerView) findViewById(R.id.rv_list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        getTask();
        Log.d("mai thanh tung", "init: ");
    }


    private void getTask() {

        mainApi.getTasks()
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
//                        TextView textView = (TextView)findViewById(R.id.);
                        rv.setAdapter(new Adapter(tasks));
                        rv.getAdapter().notifyDataSetChanged();
                        Log.e("Task thu nhat ", tasks.get(0).description);
                        Log.e("Task thu hai ", tasks.get(1).description);
//                        textView.setText(tasks.get(0).description);

                    }
                });
    }
}
