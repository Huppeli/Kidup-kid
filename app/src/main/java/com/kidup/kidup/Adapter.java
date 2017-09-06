package com.kidup.kidup;

/**
 * Created by t3math00 on 5/15/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kidup.kidup.models.Task;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<Task> tasks;
    private KidupAPI mainApi;
    public Adapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_description.setText("Description: " + tasks.get(position).description);
        holder.tv_time.setText("Time: " + tasks.get(position).time);
        holder.tv_registered.setText("Registed: " + tasks.get(position).registed);

        if(tasks.get(position).registed == false){
            holder.btn_register.setText("Register");
        }
        else {
            holder.btn_register.setText("Unregister");
        }

        holder.btn_register.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                Log.e("tung", tasks.get(position)._id);
                String _id = tasks.get(position)._id;
                mainApi.registerTask(_id);
            }
        });

    }



    @Override
    public int getItemCount() {
        return tasks == null ? 0 : tasks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_description;
        public TextView tv_time;
        public TextView tv_registered;
        public Button btn_register;


        public ViewHolder(View itemView) {
            super(itemView);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_registered = (TextView) itemView.findViewById(R.id.tv_registered);
            btn_register = (Button) itemView.findViewById(R.id.btn_register);


        }
    }
}
