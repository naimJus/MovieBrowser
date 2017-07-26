package example.com.moviesfragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static example.com.moviesfragment.GetMovieCount.pageNumber;
import static example.com.moviesfragment.GetMovieCount.pages;

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
            new GetMovieCount().execute();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    new GetMovies(getApplicationContext()).execute();
                }
            }, DELAYED_MILI);
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(StarterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, DELAYED_MILI);
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


class GetMovieCount extends AsyncTask<Void, Void, Void> {
    public static final String LOGTAG = GetMovieCount.class.getSimpleName();
    String result;
    public static int pageNumber;
    public int movieCount;
    public static int pages;


    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL url = new URL("https://yts.ag/api/v2/list_movies.json?limit=50");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null) {
            try {

                JSONObject jsonObject = new JSONObject(result);
                JSONObject data = jsonObject.getJSONObject("data");
                pageNumber = data.getInt("page_number");
                movieCount = data.getInt("movie_count");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pages = calculate(movieCount);
    }

    public int calculate(int allMovies) {
        Log.v(LOGTAG, "all movies " + allMovies);
        double pages = allMovies / 50;
        int pagesToLoad = (int) pages;
        return pagesToLoad;
    }
}

class GetMovies extends AsyncTask<Void, Void, Void> {
    public static final String LOGTAG = GetMovies.class.getSimpleName();
    Context context;

    GetMovies(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        JsonParser jsonParser = new JsonParser(context);
        Log.v(LOGTAG, "pages " + pages + " page number " + pageNumber);
        while (pages >= pageNumber) {
            StringBuilder url = new StringBuilder("https://yts.ag/api/v2/list_movies.json?limit=50");
            url.append("&page=" + pageNumber);
            Log.v(LOGTAG, url.toString());
            jsonParser.getJsonFromWeb(url.toString());
            pageNumber++;
        }
        return null;
    }
}


