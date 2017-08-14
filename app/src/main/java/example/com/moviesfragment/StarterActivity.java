package example.com.moviesfragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import java.util.List;


public class StarterActivity extends Activity {

    public static final int DELAYED_MILI = 3000;
    private MoviesDataSource moviesDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);
        //Create a new Runnable to show a welcome screen and after
        // n-milliseconds to run the application
        Handler handler = new Handler();
        moviesDataSource = new MoviesDataSource(this);
        moviesDataSource.open();
        List<Movie> movies = moviesDataSource.getAllMovies();
        if (isNetworkAvailable()) {
            new GetMovies(getApplicationContext()).execute();
        }
        Intent intent = new Intent(StarterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        moviesDataSource.close();

    }

    @Override
    protected void onResume() {
        super.onResume();
        moviesDataSource.open();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

class GetMovies extends AsyncTask<String, Void, Void> {
    JsonParser jsonParser;
    Context context;

    public GetMovies(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        int pageNumber = 1;
        int pages;
        try {
            jsonParser = new JsonParser(context);
            String url = "https://yts.ag/api/v2/list_movies.json?limit=50";
            pages = jsonParser.getJsonFromWeb(url);
            while (pageNumber <= pages) {
                jsonParser.getJsonFromWeb(url + "&page=" + pageNumber);
                pageNumber++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

