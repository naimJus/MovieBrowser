package example.com.moviesfragment;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class HomeActivity extends ListActivity {

    public static final String LOG = "HomeActivity";
    public static final String POSITION = ".Model.Movie";
    public static final String BUNDLE = "bundle";
    MoviesDataSource moviesDataSource;
    ListView listView;
    List<Movie> getMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG, "onCreate() ");
        listView = getListView();

        moviesDataSource = new MoviesDataSource(this);
        moviesDataSource.open();
        getMovies = moviesDataSource.getAllMovies();
//        Log.v(LOG, movies.get(1).toString());
        refreshAdapter();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //create a new movie from the getMovies List (which comes from the database)
                //wrap the movie in a Bundle
                //put the bundle in a Intent
                //Start the new Activity
                Movie movie = getMovies.get(position);

                Bundle b = new Bundle();
                b.putParcelable(POSITION, movie);
                Intent intent = new Intent(HomeActivity.this, MovieActivity.class);
                intent.putExtra(BUNDLE, b);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortByName:
                getMovies = moviesDataSource.sortBy(MovieSQLiteHelper.KEY_NAME + " ASC");
                refreshAdapter();
                return true;
            case R.id.sortByRating:
                getMovies = moviesDataSource.sortBy(MovieSQLiteHelper.KEY_RATING + " ASC");
                refreshAdapter();
                return true;
            case R.id.sortByYear:
                getMovies = moviesDataSource.sortBy(MovieSQLiteHelper.KEY_YEAR + " ASC");
                refreshAdapter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    public void refreshAdapter() {
        MoviesAdapter moviesAdapter = new MoviesAdapter(this, getMovies);
        setListAdapter(moviesAdapter);
    }
}