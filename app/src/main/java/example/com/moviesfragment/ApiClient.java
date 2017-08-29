package example.com.moviesfragment;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jusuf on 28.8.2017.
 */

public class ApiClient {

    private static Retrofit retrofit = null;
    private static String BASE_URL = "https://yts.ag/api/v2/";

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
