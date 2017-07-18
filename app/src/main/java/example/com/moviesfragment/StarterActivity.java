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
            new GetMovies(this, StarterActivity.this).execute("");
            Intent intent = new Intent(StarterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else{
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(StarterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            },DELAYED_MILI);

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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}


class GetMovies extends AsyncTask<String, Void, Void> {
    private Context context;
    public static final String LOGTAG = "AsyncTask";
    private Activity mActivity;

    public GetMovies(Context context, Activity mActivity) {
        this.context = context;
        this.mActivity = mActivity;
    }

    @Override
    protected Void doInBackground(String... params) {
        JsonParser jsonParser = new JsonParser(context);
        StringBuilder url =  new StringBuilder ("https://yts.ag/api/v2/list_movies.json?limit=50");
        if (params[0] != null) {
            url.append(params[0]);
        }
        Log.v(LOGTAG, url.toString());
        jsonParser.getJsonFromWeb(url.toString());

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
//        Intent intent = new Intent(context, MainActivity.class);
//        context.startActivity(intent);
//        mActivity.finish();

    }



}


