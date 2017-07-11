package example.com.moviesfragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends Activity {

    public static final int DELAYED_MILI = 3000;
    private MoviesDataSource moviesDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize Stetho for debugging Databases and network calls
        Stetho.newInitializerBuilder(this);

        //Create a new Runnable to show a welcome screen and after
        // n-milliseconds to run the application
        Handler handler = new Handler();
        moviesDataSource = new MoviesDataSource(this);
        moviesDataSource.open();
        List<Movie> movies = moviesDataSource.getAllMovies();

        //Check if the database had movie records
        if (movies.isEmpty()) {
            new GetMovies(this, MainActivity.this).execute();
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, DELAYED_MILI);
        }
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
}


class GetMovies extends AsyncTask<Void, Void, Void> {
    private Context context;
    private Activity mActivity;
    private StringBuilder sb;
    private String result;
    private MoviesDataSource moviesDataSource;
    private SQLiteDatabase database;
    private BufferedReader bufferedReader;
    private HttpURLConnection httpURLConnection;
    float resultId;


    GetMovies(Context context, Activity mActivity) {
        this.mActivity = mActivity;
        this.context = context;

    }

    @Override
    protected Void doInBackground(Void... params) {
        moviesDataSource = new MoviesDataSource(context);
        moviesDataSource.open();
        try {
            //construct the url to get the movies from
            //open the connection, set the read method and read the input stream
            // wrap the inputstream in a BufferedReader, the buffered reader is like
            //going to the supermarket without a cart.

//            URL url = new URL("https://yts.ag/api/v2/list_movies.json?limit=100&minimum_rating=8&sort_by=rating");
            URL url = new URL("https://yts.ag/api/v2/list_movies.json?limit=50");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            sb = new StringBuilder();

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.v(HomeActivity.LOG, result.toString());
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray movies = data.getJSONArray("movies");


                for (int i = 0; i < movies.length(); i++) {
                    Log.v("SQL", movies.length() + "");
                    JSONObject m = movies.getJSONObject(i);
                    String id = m.getString("id");
                    String name = m.getString("title");
                    String year = m.getString("year");
                    String rating = m.getString("rating");
                    String summary = m.getString("summary");
                    String imageUrl = m.getString("medium_cover_image");
                    String trailerCode = m.getString("yt_trailer_code");
                    resultId = moviesDataSource.createMovie(Long.valueOf(id), name, summary, Integer.valueOf(year), imageUrl, Float.valueOf(rating),trailerCode);
                    if (resultId == -1) {
                        Toast.makeText(context, "The Movie " + name + " is already in the database ", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                moviesDataSource.close();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
        mActivity.finish();
    }
}


