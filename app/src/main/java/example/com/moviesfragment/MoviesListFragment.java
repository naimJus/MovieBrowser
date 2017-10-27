package example.com.moviesfragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import example.com.moviesfragment.gson.Movie;


public class MoviesListFragment extends ListFragment {

    public static final String TAG = "MoviesListFragmentTag";
    public static final String LOG = MoviesListFragment.class.getSimpleName();
    public static final String POSITION = ".Model.Movie";
    public static final String BUNDLE = "bundle";
    private static final String FIRSTITEMID = "firstItemId";
    private static final String SORTED = "filter";
    private static final String ITEMID = "itemId";
    static int limit = 50;
    MoviesDataSource moviesDataSource;
    ListView listView;
    List<Movie> getMovies;
    String filter = "recent";
    int lastItemId;
    int firstItemId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        moviesDataSource = new MoviesDataSource(getActivity().getApplicationContext());
        moviesDataSource.open();
        setHasOptionsMenu(true);

//        Temporarily until the savedinstaceState is fixed,
// when its fixed the filter should get the value from previous state if not it will be randomly assigned
        filter = MovieSQLiteHelper.KEY_NAME + " DESC";
        getMovies = moviesDataSource.getAllMovies();
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            lastItemId = savedInstanceState.getInt(ITEMID);
            filter = savedInstanceState.getString(SORTED);
            firstItemId = savedInstanceState.getInt(FIRSTITEMID);
            if (limit > 50) {
                getMovies = moviesDataSource.sortAndLimit(filter, String.valueOf(limit));
                listView.setSelectionFromTop(lastItemId, 0);
            } else {
                getMovies = moviesDataSource.sortBy(filter);
                listView.setSelectionFromTop(firstItemId, 0);
            }
        } else {
            firstItemId = listView.getFirstVisiblePosition();
            getMovies = moviesDataSource.getAllMovies();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        listView = getListView();
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
//                Intent intent = new Intent(MoviesListFragment.this, MovieActivity.class);
//                intent.putExtra(BUNDLE, b);
//                startActivity(intent);
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SORTED, filter);
        outState.putInt(ITEMID, lastItemId);
        outState.putInt(FIRSTITEMID, firstItemId);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        refreshAdapter();
    }

    /*
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
            }*/

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
    public void onPause() {
        super.onPause();
        firstItemId = listView.getFirstVisiblePosition();
    }

    @Override
    public void onResume() {
        super.onResume();
        moviesDataSource.open();
        refreshAdapter();
        listView.setSelectionFromTop(firstItemId, 0);
    }

    @Override
    public void onStop() {
        super.onStop();
        moviesDataSource.close();
    }

    public void refreshAdapter() {
        MoviesAdapter moviesAdapter = new MoviesAdapter(getActivity().getApplicationContext(), getMovies);
        setListAdapter(moviesAdapter);
        if (lastItemId != 0) {
            listView.setSelectionFromTop(lastItemId, 0);
        }

    }
}