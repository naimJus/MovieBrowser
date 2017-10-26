package example.com.moviesfragment;import android.content.Context;import android.content.Intent;import android.net.ConnectivityManager;import android.net.NetworkInfo;import android.os.Bundle;import android.os.Handler;import android.support.v4.app.DialogFragment;import android.support.v7.app.AppCompatActivity;import android.util.Log;import android.view.View;import android.widget.ProgressBar;import android.widget.TextView;import java.util.HashMap;import java.util.List;import example.com.moviesfragment.gson.Data;import example.com.moviesfragment.gson.Example;import example.com.moviesfragment.gson.InternetUnavailableFragment;import example.com.moviesfragment.gson.Movie;import example.com.moviesfragment.gson.Torrent;import retrofit2.Call;import retrofit2.Callback;import retrofit2.Response;/** The first Activity that runs when the app is Started.* The Activity makes a request, fetches the movies from web* and saves them into a database.* */public class StarterActivity extends AppCompatActivity {    public static final String LOG = StarterActivity.class.getSimpleName();    public MoviesDataSource moviesDataSource;    public int totalPages;    long resultId;    int currentPage;    YtsApi ytsApi;    int movieCount;    TextView progressBarTV;    private ProgressBar progressBar;    @Override    protected void onResume() {        super.onResume();        if ((moviesDataSource.getAllMovies().size() == 0) && isNetworkAvailable()) {            ytsApi = ApiClient.getClient().create(YtsApi.class);            loadPages(currentPage);            Handler handler = new Handler();            handler.postDelayed(new Runnable() {                @Override                public void run() {                    totalPages = movieCount / 50;                    if (totalPages % 50 != 0)                        totalPages++;                    while (currentPage < totalPages) {                        currentPage++;                        loadPages(currentPage);                        progressBar.setVisibility(View.VISIBLE);                        progressBarTV.setVisibility(View.VISIBLE);                    }                    progressBar.setVisibility(View.GONE);                    progressBarTV.setVisibility(View.GONE);                    Intent intent = new Intent(StarterActivity.this, MainActivity.class);                    startActivity(intent);                    finish();                }            }, 4000);        }    }    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        moviesDataSource = new MoviesDataSource(this);        moviesDataSource.open();        setContentView(R.layout.activity_starter);        progressBar = (ProgressBar) findViewById(R.id.progressBar1);        progressBarTV = (TextView) findViewById(R.id.progressBarTextView);/** Checks if there is a internet access* if there is it will run a retrofit request* if there's not it will ask the user to get internet access for the database to be updated or populated.* */        if ((moviesDataSource.getAllMovies().size() == 0) && !isNetworkAvailable()) {            DialogFragment dialogFragment = new InternetUnavailableFragment();            dialogFragment.show(getSupportFragmentManager(), "KEY");        } else {            ytsApi = ApiClient.getClient().create(YtsApi.class);            loadPages(currentPage);/** Creates a handler object with a time delay of 1000 millis* so the equation of how many pages are available doesn't run immediately* if it runs immediately the result will be 0 pages because the response for total pages is still 0* while currentPage is smaller then totalPages the loadPages method will run and it will receive only one argument the currentPage*/            Handler handler = new Handler();            handler.postDelayed(new Runnable() {                @Override                public void run() {                    totalPages = movieCount / 50;                    if (totalPages % 50 != 0)                        totalPages++;                    while (currentPage < totalPages) {                        currentPage++;                        loadPages(currentPage);                        progressBar.setVisibility(View.VISIBLE);                        progressBarTV.setVisibility(View.VISIBLE);                    }                    progressBar.setVisibility(View.GONE);                    progressBarTV.setVisibility(View.GONE);                    Intent intent = new Intent(StarterActivity.this, MainActivity.class);                    startActivity(intent);                    finish();                }            }, 4000);//After running the request the MainActivity class will run.        }    }    /*    *The api contains >160 Pages the methot loops through all tha pages of the requests    *and for each page calls the fetchResult() method which takes only one argument,    *the response for that actual page.    */    private void loadPages(int actualPage) {        Call<Example> call = ytsApi.getExampleCall(50, actualPage);        call.enqueue(new Callback<Example>() {            @Override            public void onResponse(Call<Example> call, retrofit2.Response<Example> response) {                fetchResults(response);            }            @Override            public void onFailure(Call<Example> call, Throwable t) {            }        });    }    //    Fetching the result from Response and saving it in SQLite    private void fetchResults(Response<Example> response) {        Data data = response.body().getData();        List<Movie> movies = data.getMovies();        movieCount = data.getMovieCount();        Log.v(LOG, "movie count " + movieCount);        for (Movie movie : movies) {            HashMap<String, String> torrentMap = new HashMap<>();            HashMap<String, String> hashMap = new HashMap<>();            List<Torrent> torrents = movie.getTorrents();            StringBuilder gen = new StringBuilder();            int id = movie.getId();            String title = movie.getTitle();            String summary = movie.getSummary();            int year = movie.getYear();            String mediumImage = movie.getMediumCoverImage();            double rating = movie.getRating();            String trailerCode = movie.getYtTrailerCode();            List<String> genre = movie.getGenres();            if (genre != null) {                for (String s : genre) {                    gen.append(s);                    gen.append(" ");                }            }            if (torrents != null)                for (Torrent torrent : torrents) {                    String quality = torrent.getQuality();                    String hash = torrent.getHash();                    torrentMap.put(quality, torrent.getUrl());                    hashMap.put(quality, hash);                }            resultId = moviesDataSource.createMovie(id, title, summary, year, mediumImage, rating, trailerCode, gen.toString(), torrentMap, hashMap);        }    }    private boolean isNetworkAvailable() {        ConnectivityManager connectivityManager                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();        return activeNetworkInfo != null && activeNetworkInfo.isConnected();    }}