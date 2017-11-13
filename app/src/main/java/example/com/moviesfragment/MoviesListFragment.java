package example.com.moviesfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import example.com.moviesfragment.gson.Movie;


public class MoviesListFragment extends Fragment {

    public static final String TAG= MoviesListFragment.class.getSimpleName();
    public static final String POSITION = ".Model.Movie";
    public static final String BUNDLE = "bundle";
    private static final String FIRSTITEMID = "firstItemId";
    private static final String SORTED = "filter";
    private static final String ITEMID = "itemId";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;
    static int limit = 50;
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;
    MoviesDataSource moviesDataSource;
    List<Movie> getMovies;
    String filter = "_id";
    int lastItemId;
    int firstItemId;
    boolean flag_loading;
    private RecyclerView mRecyclerView;
    private MoviesAdapter mAdapter;

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
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new MoviesAdapter(this.getActivity(), getMovies);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        savedInstanceState.putString(SORTED, filter);
        savedInstanceState.putInt(ITEMID, lastItemId);
        savedInstanceState.putInt(FIRSTITEMID, firstItemId);
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
            lastItemId = savedInstanceState.getInt(ITEMID);
            filter = savedInstanceState.getString(SORTED);
            firstItemId = savedInstanceState.getInt(FIRSTITEMID);
            if (limit > 50) {
                getMovies = moviesDataSource.sortAndLimit(filter, String.valueOf(limit));
                mRecyclerView.scrollToPosition(lastItemId);
            } else {
                getMovies = moviesDataSource.sortBy(filter);
                mRecyclerView.scrollToPosition(firstItemId);
            }
        } else {
            LinearLayoutManager layoutManager = ((LinearLayoutManager) mRecyclerView.getLayoutManager());
            firstItemId = layoutManager.findFirstVisibleItemPosition();
            getMovies = moviesDataSource.getAllMovies();
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        refreshAdapter();
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
                getMovies = moviesDataSource.sortAndLimit(filter, String.valueOf(limit));
                refreshAdapter();
                return true;
            case R.id.sortByRating:
                if (filter.equals(MovieSQLiteHelper.KEY_RATING + " ASC")) {
                    filter = MovieSQLiteHelper.KEY_RATING + " DESC";
                } else {
                    filter = MovieSQLiteHelper.KEY_RATING + " ASC";
                }
                getMovies = moviesDataSource.sortAndLimit(filter, String.valueOf(limit));
                refreshAdapter();
                return true;
            case R.id.sortByYear:
                if (filter.equals(MovieSQLiteHelper.KEY_YEAR + " ASC")) {
                    filter = MovieSQLiteHelper.KEY_YEAR + " DESC";
                } else {
                    filter = MovieSQLiteHelper.KEY_YEAR + " ASC";
                }
                getMovies = moviesDataSource.sortAndLimit(filter, String.valueOf(limit));
                refreshAdapter();
                return true;
            case R.id.sortByRecent:
                if (filter.equals(MovieSQLiteHelper.KEY_ID + " ASC")) {
                    filter = MovieSQLiteHelper.KEY_ID + " DESC";
                } else {
                    filter = MovieSQLiteHelper.KEY_ID + " ASC";
                }
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
        LinearLayoutManager layoutManager = ((LinearLayoutManager) mRecyclerView.getLayoutManager());
        firstItemId = layoutManager.findFirstVisibleItemPosition();

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshAdapter();
        mRecyclerView.scrollToPosition(firstItemId);
    }

    public void refreshAdapter() {
        mAdapter = new MoviesAdapter(getContext(), getMovies);
        mRecyclerView.setAdapter(mAdapter);
        if (lastItemId != 0) {
            mRecyclerView.scrollToPosition(lastItemId);
        }
    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
}
    /*




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }


*/
/*        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //create a new movie from the getMovies List (which comes from the database)
                //wrap the movie in a Bundle
                //put the bundle in a Intent
                //Start the new Activity
                Movie movie = getMovies.get(position);
                Log.v(LOG, movie.toString());
                Bundle b = new Bundle();
                b.putParcelable(POSITION, movie);
                Intent intent = new Intent(getActivity(), MovieActivity.class);
                intent.putExtra(BUNDLE, b);
                startActivity(intent);
            }
        });


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {


            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (flag_loading == false) {
                        flag_loading = true;
                        add();
                    }
                }
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }*//*


 */
/*   private void add() {
        lastItemId = listView.getCount() - 1;
        limit = limit + 50;
        getMovies = moviesDataSource.sortAndLimit(filter, String.valueOf(limit));
        refreshAdapter();
        flag_loading = false;
    }*//*




    }
}*/
