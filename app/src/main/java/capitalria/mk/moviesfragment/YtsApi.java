package capitalria.mk.moviesfragment;

import capitalria.mk.moviesfragment.gson.Example;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jusuf on 28.8.2017.
 */

public interface YtsApi {

    @GET("list_movies.json")
    Call<Example> getExampleCall(
            @Query("limit") int limit,
            @Query("page") int pageIndex
    );
}

