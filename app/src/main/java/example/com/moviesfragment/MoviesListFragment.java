package example.com.moviesfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import example.com.moviesfragment.gson.Movie;


public class MoviesListFragment extends Fragment {

    public static final String TAG = MoviesListFragment.class.getSimpleName();
    public static final String POSITION = ".Model.Movie";
    public static final String BUNDLE = "bundle";
    private static final String FIRSTITEMID = "firstItemId";
    private static final String SORTED = "filter";
    private static final String ITEMID = "itemId";
    private static final int VISIBLEITEMS = 4;
    static int limit = 50;
    protected LinearLayoutManager mLayoutManager;
    MoviesDataSource moviesDataSource;
    List<Movie> getMovies;
    String filter = "_id";
    int scrollPosition = 0;
    int lastItemScrollPosition;

    private RecyclerView mRecyclerView;
    private MoviesAdapter mAdapter;
    private boolean flag_loading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        moviesDataSource = new MoviesDataSource(getActivity().getApplicationContext());
        moviesDataSource.open();
        getMovies = moviesDataSource.getAllMovies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies_list, container, false);

        setHasOptionsMenu(true);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MoviesAdapter(this.getActivity(), getMovies);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mLayoutManager.getItemCount() - VISIBLEITEMS == mLayoutManager.findLastVisibleItemPosition()) {
                    if (!flag_loading) {
                        flag_loading = true;
                        loadMoreData();
                    }
                }
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
        });
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putString(SORTED, filter);
        savedInstanceState.putInt(FIRSTITEMID, scrollPosition);
        savedInstanceState.putInt(ITEMID, lastItemScrollPosition);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            filter = savedInstanceState.getString(SORTED);
            scrollPosition = savedInstanceState.getInt(FIRSTITEMID);
            lastItemScrollPosition = savedInstanceState.getInt(ITEMID);
            if (limit > 50) {
                getMovies = moviesDataSource.sortAndLimit(filter, String.valueOf(limit));
                mRecyclerView.scrollToPosition(lastItemScrollPosition);
            } else {
                getMovies = moviesDataSource.sortBy(filter);
                mRecyclerView.scrollToPosition(scrollPosition);
            }
        } else {
            scrollPosition = mLayoutManager.findFirstVisibleItemPosition();
            getMovies = moviesDataSource.getAllMovies();
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        refreshAdapter();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);

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
                lastItemScrollPosition = 0;
                getMovies = moviesDataSource.sortAndLimit(filter, String.valueOf(limit));
                refreshAdapter();
                return true;
            case R.id.sortByRating:
                if (filter.equals(MovieSQLiteHelper.KEY_RATING + " ASC")) {
                    filter = MovieSQLiteHelper.KEY_RATING + " DESC";
                } else {
                    filter = MovieSQLiteHelper.KEY_RATING + " ASC";
                }
                lastItemScrollPosition = 0;
                getMovies = moviesDataSource.sortAndLimit(filter, String.valueOf(limit));
                refreshAdapter();
                return true;
            case R.id.sortByYear:
                if (filter.equals(MovieSQLiteHelper.KEY_YEAR + " ASC")) {
                    filter = MovieSQLiteHelper.KEY_YEAR + " DESC";
                } else {
                    filter = MovieSQLiteHelper.KEY_YEAR + " ASC";
                }
                lastItemScrollPosition = 0;
                getMovies = moviesDataSource.sortAndLimit(filter, String.valueOf(limit));
                refreshAdapter();
                return true;
            case R.id.sortByRecent:
                if (filter.equals(MovieSQLiteHelper.KEY_ID + " ASC")) {
                    filter = MovieSQLiteHelper.KEY_ID + " DESC";
                } else {
                    filter = MovieSQLiteHelper.KEY_ID + " ASC";
                }
                lastItemScrollPosition = 0;
                getMovies = moviesDataSource.sortAndLimit(filter, String.valueOf(limit));
                refreshAdapter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                .findFirstCompletelyVisibleItemPosition();

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshAdapter();
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    public void refreshAdapter() {
        mAdapter = new MoviesAdapter(getContext(), getMovies);
        mRecyclerView.setAdapter(mAdapter);
        if (lastItemScrollPosition != 0) {
            mRecyclerView.scrollToPosition(lastItemScrollPosition);
        }
    }

    public void loadMoreData() {
        lastItemScrollPosition = mLayoutManager.findLastVisibleItemPosition() - 1;
        limit = limit + 50;
        getMovies = moviesDataSource.sortAndLimit(filter, String.valueOf(limit));
        mRecyclerView.getAdapter().notifyDataSetChanged();
        refreshAdapter();
        flag_loading = false;

    }
}

