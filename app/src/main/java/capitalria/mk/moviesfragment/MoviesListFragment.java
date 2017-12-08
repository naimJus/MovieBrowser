package capitalria.mk.moviesfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import capitalria.mk.moviesfragment.gson.Movie;


public class MoviesListFragment extends Fragment {

    public static final String BUNDLE = "bundle";
    private static final String FIRSTITEMID = "firstItemId";
    private static final String SORTED = "filter";
    private static final String ITEMID = "itemId";
    static int limit = 50;
    int scrollPosition = 0;
    int lastItemScrollPosition;
    private static final int VISIBLEITEMS = 4;
    protected LinearLayoutManager mLayoutManager;
    private MoviesDataSource mMoviesDataSource;
    private String mFilter = MovieSQLiteHelper.MOVIE_INFO_KEY_ID;
    private List<Movie> mMovieList;
    private RecyclerView mRecyclerView;
    private MoviesAdapter mAdapter;
    private boolean flag_loading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMoviesDataSource = new MoviesDataSource(getActivity().getApplicationContext());
        mMoviesDataSource.open();
        mMovieList = mMoviesDataSource.limitMovies(limit);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(capitalria.mk.moviesfragment.R.layout.fragment_movies_list, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(capitalria.mk.moviesfragment.R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration itemDecor = new DividerItemDecoration(mRecyclerView.getContext(), mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(itemDecor);
        mAdapter = new MoviesAdapter(this.getActivity(), mMovieList);

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

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this.getActivity(), mRecyclerView, new MainActivity.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                Movie movie = mMovieList.get(position);
                Intent intent = new Intent(getActivity(), MovieActivity.class);
                intent.putExtra(BUNDLE, movie.getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mFilter = savedInstanceState.getString(SORTED);
            scrollPosition = savedInstanceState.getInt(FIRSTITEMID);
            lastItemScrollPosition = savedInstanceState.getInt(ITEMID);
            if (limit > 50) {
                mMovieList = mMoviesDataSource.sortAndLimit(mFilter, limit);
            } else {
                mMovieList = mMoviesDataSource.limitMovies(limit);
                mRecyclerView.scrollToPosition(scrollPosition);
            }
        } else {
            scrollPosition = mLayoutManager.findFirstVisibleItemPosition();
            mMovieList = mMoviesDataSource.sortAndLimit(mFilter, limit);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(SORTED, mFilter);
        savedInstanceState.putInt(FIRSTITEMID, scrollPosition);
        savedInstanceState.putInt(ITEMID, lastItemScrollPosition);
    }


    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        refreshAdapter();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(capitalria.mk.moviesfragment.R.menu.menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case capitalria.mk.moviesfragment.R.id.sortByName:
                if (mFilter.equals(MovieSQLiteHelper.MOVIE_INFO_KEY_TITLE + " ASC")) {
                    mFilter = MovieSQLiteHelper.MOVIE_INFO_KEY_TITLE + " DESC";
                } else {
                    mFilter = MovieSQLiteHelper.MOVIE_INFO_KEY_TITLE + " ASC";
                }
                lastItemScrollPosition = 0;
                mMovieList = mMoviesDataSource.sortAndLimit(mFilter, limit);
                refreshAdapter();
                return true;
            case capitalria.mk.moviesfragment.R.id.sortByRating:
                if (mFilter.equals(MovieSQLiteHelper.MOVIE_INFO_KEY_RATING + " ASC")) {
                    mFilter = MovieSQLiteHelper.MOVIE_INFO_KEY_RATING + " DESC";
                } else {
                    mFilter = MovieSQLiteHelper.MOVIE_INFO_KEY_RATING + " ASC";
                }
                lastItemScrollPosition = 0;
                mMovieList = mMoviesDataSource.sortAndLimit(mFilter, limit);
                refreshAdapter();
                return true;
            case capitalria.mk.moviesfragment.R.id.sortByYear:
                if (mFilter.equals(MovieSQLiteHelper.MOVIE_INFO_KEY_YEAR + " ASC")) {
                    mFilter = MovieSQLiteHelper.MOVIE_INFO_KEY_YEAR + " DESC";
                } else {
                    mFilter = MovieSQLiteHelper.MOVIE_INFO_KEY_YEAR + " ASC";
                }
                lastItemScrollPosition = 0;
                mMovieList = mMoviesDataSource.sortAndLimit(mFilter, limit);
                refreshAdapter();
                return true;
            case capitalria.mk.moviesfragment.R.id.sortByRecent:
                if (mFilter.equals(MovieSQLiteHelper.MOVIE_INFO_KEY_ID + " ASC")) {
                    mFilter = MovieSQLiteHelper.MOVIE_INFO_KEY_ID + " DESC";
                } else {
                    mFilter = MovieSQLiteHelper.MOVIE_INFO_KEY_ID + " ASC";
                }
                lastItemScrollPosition = 0;
                mMovieList = mMoviesDataSource.sortAndLimit(mFilter, limit);
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
        mAdapter = new MoviesAdapter(getContext(), mMovieList);
        mRecyclerView.setAdapter(mAdapter);
        if (lastItemScrollPosition != 0) {
            mRecyclerView.scrollToPosition(lastItemScrollPosition);
        }
    }

    public void loadMoreData() {
        limit = limit + 50;
        mMovieList = mMoviesDataSource.sortAndLimit(mFilter, limit);
        lastItemScrollPosition = mLayoutManager.findLastVisibleItemPosition() - 1;
        refreshAdapter();
        mRecyclerView.scrollToPosition(lastItemScrollPosition);
        flag_loading = false;
    }
}

