package example.com.moviesfragment;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;

import example.com.moviesfragment.gson.Movie;

public class SearchResultsActivity extends ListActivity {
    ListView listView;
    MoviesDataSource moviesDataSource;
    List<Movie> getMovies;
    HashMap<String, String> sqlParams;
    public static final String POSITION = ".Model.Movie";
    public static final String BUNDLE = "bundle";
    private static final String FIRSTITEMID = "firstItemId";
    int firstItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            firstItemId = savedInstanceState.getInt(FIRSTITEMID);
        }

        listView = getListView();
        moviesDataSource = new MoviesDataSource(this);
        moviesDataSource.open();
        Intent i = getIntent();
        if (i != null) {
            sqlParams = (HashMap<String, String>) i.getSerializableExtra(SearchFragment.SEARCH);
            getMovies = moviesDataSource.searchMovies(sqlParams);
        } else
            getMovies = moviesDataSource.getAllMovies();
        moviesDataSource.close();
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
                Intent intent = new Intent(SearchResultsActivity.this, MovieActivity.class);
                intent.putExtra(BUNDLE, b);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        firstItemId = listView.getFirstVisiblePosition();
        moviesDataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        moviesDataSource.open();
        refreshAdapter();
    }

    public void refreshAdapter() {
        MoviesAdapter moviesAdapter = new MoviesAdapter(this, getMovies);
        setListAdapter(moviesAdapter);
        if (firstItemId != 0)
            listView.setSelectionFromTop(firstItemId, 0);
    }
}
