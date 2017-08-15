package example.com.moviesfragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import static example.com.moviesfragment.StarterActivity.progressBar;


public class StarterActivity extends Activity {
    static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        if (isNetworkAvailable()) {
            new GetMovies(getApplicationContext(), this).execute();
        } else {
            Intent intent = new Intent(StarterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

class GetMovies extends AsyncTask<String, Void, Void> {
    private JsonParser jsonParser;
    private Activity activity;
    private Context context;

    public GetMovies(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressBar.setVisibility(View.GONE);
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();

    }
}

