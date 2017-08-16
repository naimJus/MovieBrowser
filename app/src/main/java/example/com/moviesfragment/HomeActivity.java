package example.com.moviesfragment;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class HomeActivity extends ListActivity {

    public static final String LOG = "HomeActivity";
    public static final String POSITION = ".Model.Movie";
    public static final String BUNDLE = "bundle";
    private static final String FIRSTITEMID = "firstItemId";
    public static final String SORTED = "filter";
    public static final String ITEMID = "itemId";
    MoviesDataSource moviesDataSource;
    ListView listView;
    List<Movie> getMovies;
    String filter;
    Button loadMoreBtn;
    static int limit = 50;
    int lastItemId;
    int firstItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listView = getListView();
        moviesDataSource = new MoviesDataSource(this);
        moviesDataSource.open();
        View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, null, false);
        listView.addFooterView(footerView);


        if (savedInstanceState != null) {
            lastItemId = savedInstanceState.getInt(ITEMID);
            filter = savedInstanceState.getString(SORTED);
            firstItemId = savedInstanceState.getInt(FIRSTITEMID);
            if (limit > 50) {
                getMovies = moviesDataSource.sortAndLimit(filter, String.valueOf(limit));
                listView.setSelectionFromTop(lastItemId, 0);
            } else {
                getMovies = moviesDataSource.sortBy(filter);
                listView.setSelectionFromTop(firstItemId,0);
            }
        } else {
            firstItemId = listView.getFirstVisiblePosition();
            Bundle b = getIntent().getExtras();
            if (b != null) {
                filter = b.getString(MainActivity.FILTER);
                getMovies = moviesDataSource.sortBy(filter);
            } else {
                getMovies = moviesDataSource.getAllMovies();
            }
        }

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
        loadMoreBtn = (Button) findViewById(R.id.loadMoreButton);
        loadMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastItemId = listView.getCount() - 1;
                limit = limit + 50;
                getMovies = moviesDataSource.sortAndLimit(filter, String.valueOf(limit));
                refreshAdapter();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SORTED, filter);
        outState.putInt(ITEMID, lastItemId);
        outState.putInt(FIRSTITEMID, firstItemId);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshAdapter();

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
                if (filter.equals(MovieSQLiteHelper.KEY_NAME + " ASC")) {
                    filter = MovieSQLiteHelper.KEY_NAME + " DESC";
                } else {
                    filter = MovieSQLiteHelper.KEY_NAME + " ASC";
                }
                getMovies = moviesDataSource.sortBy(filter);
                refreshAdapter();
                return true;
            case R.id.sortByRating:
                if (filter.equals(MovieSQLiteHelper.KEY_RATING + " ASC")) {
                    filter = MovieSQLiteHelper.KEY_RATING + " DESC";
                } else {
                    filter = MovieSQLiteHelper.KEY_RATING + " ASC";
                }
                getMovies = moviesDataSource.sortBy(filter);
                refreshAdapter();
                return true;
            case R.id.sortByYear:
                if (filter.equals(MovieSQLiteHelper.KEY_YEAR + " ASC")) {
                    filter = MovieSQLiteHelper.KEY_YEAR + " DESC";
                } else {
                    filter = MovieSQLiteHelper.KEY_YEAR + " ASC";
                }
                getMovies = moviesDataSource.sortBy(filter);
                refreshAdapter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        firstItemId = listView.getFirstVisiblePosition();
        Log.v(LOG, "first item id " + firstItemId);
        moviesDataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshAdapter();
        listView.setSelectionFromTop(firstItemId, 0);
        moviesDataSource.open();
    }

    public void refreshAdapter() {
        MoviesAdapter moviesAdapter = new MoviesAdapter(this, getMovies);
        setListAdapter(moviesAdapter);
        if (lastItemId != 0) {
            listView.setSelectionFromTop(lastItemId, 0);
        }
//        if (firstItemId != 0) {
//            listView.setSelectionFromTop(firstItemId, 0);
//        }
    }
}