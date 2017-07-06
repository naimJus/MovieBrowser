package example.com.moviesfragment;

import android.app.ListActivity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HomeActivity extends ListActivity {
    MoviesDataSource moviesDataSource;
    public static final String LOG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = getListView();

        //Gets a reference to the database and gets all the records into a List
        //close the database
        moviesDataSource = new MoviesDataSource(this);
        moviesDataSource.open();
        List<Movie> movies = moviesDataSource.getAllMovies();
        moviesDataSource.close();

        //Check if the database had movie records
        if (!movies.isEmpty()) {
            //if there were movie records they will be displayed
            Log.v(LOG, "displaying movies");
            MoviesAdapter moviesAdapter = new MoviesAdapter(this, movies);
            listView.setAdapter(moviesAdapter);
        } else {
            //if no movie records were in the databas
            //a call to GetMovies asyncTask is made to fetch movies from the Internet
            new GetMovies(this).execute();
            Log.v(LOG, "fetching movies");
        }
    }
}

class GetMovies extends AsyncTask<Void, Void, Void> {
    private Context context;
    private StringBuilder sb;
    private String result;
    private MovieSQLiteHelper movieSQLiteHelper;
    private SQLiteDatabase database;
    private BufferedReader bufferedReader;

    HttpURLConnection httpURLConnection;

    GetMovies(Context context) {
        this.context = context;
        movieSQLiteHelper = MovieSQLiteHelper.getsInstance(context);
        database = movieSQLiteHelper.getWritableDatabase();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL url = new URL("https://yts.ag/api/v2/list_movies.json?limit=50&minimum_rating=8&sort_by=rating");
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
                    String imageUrl = m.getString("background_image");
                    movieSQLiteHelper.createMovie(database, Long.valueOf(id), name, summary, Integer.valueOf(year), imageUrl, Float.valueOf(rating));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                database.close();
                movieSQLiteHelper.close();
            }
        }
        return null;
    }
}
