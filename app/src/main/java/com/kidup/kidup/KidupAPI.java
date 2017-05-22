package com.kidup.kidup;

import com.kidup.kidup.models.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by t3math00 on 5/11/2017.
 */

public interface KidupAPI {
    @GET("kids/tasks/{kid_id}")
    rx.Observable<List<Task>> getTasks(@Path("kid_id") String kid_id);

    @PUT("kids/tasks/item/{task_id}")
    Call<Task> registerTask(@Path(value = "task_id" ,encoded=true) String id);
}
