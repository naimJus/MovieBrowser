package capitalria.mk.moviesfragment;import android.content.Context;import android.content.Intent;import android.net.ConnectivityManager;import android.net.NetworkInfo;import android.os.AsyncTask;import android.os.Bundle;import android.os.Handler;import android.support.v4.app.DialogFragment;import android.support.v7.app.AppCompatActivity;import android.view.View;import android.widget.ProgressBar;import android.widget.TextView;import java.util.List;import capitalria.mk.moviesfragment.gson.Data;import capitalria.mk.moviesfragment.gson.Example;import capitalria.mk.moviesfragment.gson.Movie;import retrofit2.Call;import retrofit2.Callback;import retrofit2.Response;/** The first Activity that runs when the app is Started.* The Activity makes a request, fetches the movies from web* and saves them into a database.* */public class StarterActivity extends AppCompatActivity {    public MoviesDataSource moviesDataSource;    public int totalPages;    int currentPage = 1;    YtsApi ytsApi;    int movieCount;    TextView connectingTv, gatheringMoviesTv;    private ProgressBar progressBar;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(capitalria.mk.moviesfragment.R.layout.activity_starter);        moviesDataSource = new MoviesDataSource(this);        moviesDataSource.open();        progressBar = (ProgressBar) findViewById(capitalria.mk.moviesfragment.R.id.progressBar);        connectingTv = (TextView) findViewById(capitalria.mk.moviesfragment.R.id.progressBarTextView);        gatheringMoviesTv = (TextView) findViewById(capitalria.mk.moviesfragment.R.id.progressBarLoadingDataTv);        progressBar.setVisibility(View.VISIBLE);        connectingTv.setVisibility(View.VISIBLE);        gatheringMoviesTv.setVisibility(View.VISIBLE);        checkForNetAndMovies();    }    @Override    protected void onRestart() {        super.onRestart();        checkForNetAndMovies();    }    private void checkForNetAndMovies() {        if (isNetworkAvailable()) {            ytsApi = ApiClient.getClient().create(YtsApi.class);            fetchMovies(currentPage);            Handler handler = new Handler();            handler.postDelayed(() -> {                calculateApiPages();                progressBar.setVisibility(View.GONE);                connectingTv.setVisibility(View.GONE);                gatheringMoviesTv.setVisibility(View.GONE);                Intent intent = new Intent(StarterActivity.this, MainActivity.class);                startActivity(intent);                finish();            }, 3000);        } else {            if (moviesDataSource.getCount() == 0) {                DialogFragment dialogFragment = new InternetUnavailableFragment();                dialogFragment.show(getSupportFragmentManager(), "KEY");                dialogFragment.setCancelable(false);            } else {                progressBar.setVisibility(View.GONE);                connectingTv.setVisibility(View.GONE);                gatheringMoviesTv.setVisibility(View.GONE);                Intent intent = new Intent(StarterActivity.this, MainActivity.class);                startActivity(intent);                finish();            }        }    }    private void calculateApiPages() {        AsyncTask.execute(() -> {            while (currentPage <= totalPages) {                fetchMovies(currentPage);                currentPage++;            }        });    }    private void fetchMovies(int actualPage) {        Call<Example> call = ytsApi.getExampleCall(50, actualPage);        call.enqueue(new Callback<Example>() {            @Override            public void onResponse(Call<Example> call, retrofit2.Response<Example> response) {                responseToDb(response);            }            @Override            public void onFailure(Call<Example> call, Throwable t) {                DialogFragment dialogFragment = new ServerDownFragment();                dialogFragment.getDialog().setCanceledOnTouchOutside(false);                dialogFragment.show(getSupportFragmentManager(), "KEY");                return;            }        });    }    private synchronized void responseToDb(Response<Example> response) {        Data data;        if (response.body() != null) {            data = response.body().getData();            List<Movie> movies = data.getMovies();            movieCount = data.getMovieCount();            totalPages = movieCount / 50;            if (movieCount % 50 != 0)                totalPages++;            for (Movie movie : movies) {                moviesDataSource.createMovieInfo(movie);            }        } else {            DialogFragment dialogFragment = new ServerDownFragment();            dialogFragment.getDialog().setCanceledOnTouchOutside(false);            dialogFragment.show(getSupportFragmentManager(), "KEY");            return;        }    }    /** Creates a handler object with a time delay of 1000 millis* so the equation of how many pages are available doesn't run immediately* if it runs immediately the result will be 0 pages because the response for total pages is still 0* while currentPage is smaller then totalPages the fetchMovies method will run and it will receive only one argument the currentPage*The api contains >160 Pages the method loops through all tha pages of the requests*and for each page calls the fetchResult() method which takes only one argument,*the response for that actual page.*/    private boolean isNetworkAvailable() {        ConnectivityManager connectivityManager                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();        return activeNetworkInfo != null && activeNetworkInfo.isConnected();    }}