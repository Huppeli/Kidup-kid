package com.kidup.kidup;

import com.kidup.kidup.models.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Part;

/**
 * Created by t3math00 on 5/11/2017.
 */

public interface KidupAPI {
    @GET("kids/tasks/5910cfd18204a116d433a63f")
    rx.Observable<List<Task>> getTasks();

    @GET("kids/tasks/registed/5910cfd18204a116d433a63f")
    rx.Observable<List<Task>> getRegisteredTasks();

    @PUT("kids/tasks/item/task_id")
    Call<Task> registerTask(@Part("task_id") String id);
}
