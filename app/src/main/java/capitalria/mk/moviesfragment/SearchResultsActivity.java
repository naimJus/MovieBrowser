package capitalria.mk.moviesfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import capitalria.mk.moviesfragment.gson.Movie;

public class SearchResultsActivity extends AppCompatActivity {
    public static final String POSITION = ".Model.Movie";
    public static final String BUNDLE = "bundle";
    private static final String FIRSTITEMID = "firstItemId";
    private static final int VISIBLEITEMS = 4;
    protected LinearLayoutManager mLayoutManager;
    HashMap<String, String> sqlParams;
    int firstItemId;
    int lastItemScrollPosition;
    int scrollPosition = 0;
    private MoviesDataSource mMoviesDataSource;
    private String mFilter = MovieSQLiteHelper.MOVIE_INFO_KEY_ID;
    private List<Movie> mMovieList;
    private RecyclerView mRecyclerView;
    private MoviesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(capitalria.mk.moviesfragment.R.layout.activity_search_results);

        if (savedInstanceState != null) {
            firstItemId = savedInstanceState.getInt(FIRSTITEMID);
        }

        mMoviesDataSource = new MoviesDataSource(this);
        mMoviesDataSource.open();

        mRecyclerView = (RecyclerView) findViewById(capitalria.mk.moviesfragment.R.id.search_activity_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration itemDecor = new DividerItemDecoration(mRecyclerView.getContext(), mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(itemDecor);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecyclerView, new MainActivity.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                Movie movie = mMovieList.get(position);
                Intent intent = new Intent(SearchResultsActivity.this, MovieActivity.class);
                intent.putExtra(BUNDLE, movie.getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(SearchResultsActivity.this, "Long press on position :" + position,
                        Toast.LENGTH_LONG).show();
            }
        }));
        mAdapter = new MoviesAdapter(this, mMovieList);
        mRecyclerView.setAdapter(mAdapter);
        Intent i = getIntent();
        if (i != null) {
            sqlParams = (HashMap<String, String>) i.getSerializableExtra(SearchFragment.SEARCH);
            mMovieList = mMoviesDataSource.searchMovies(sqlParams);
        } else {
            mMovieList = mMoviesDataSource.limitMovies(50);
        }

        if (mMovieList.size() == 0) {
            TextView textView = new TextView(this);
            textView.setText(capitalria.mk.moviesfragment.R.string.nothing_found);
            LinearLayout linearLayout = (LinearLayout) findViewById(capitalria.mk.moviesfragment.R.id.search_results);
            linearLayout.addView(textView);
            textView.setTextSize(20);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        }
        mAdapter = new MoviesAdapter(this, mMovieList);
        mRecyclerView.setAdapter(mAdapter);
        refreshAdapter();
        Log.v("SearchResultActivity", sqlParams.toString());
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
        mAdapter = new MoviesAdapter(SearchResultsActivity.this, mMovieList);
        mRecyclerView.setAdapter(mAdapter);
        if (lastItemScrollPosition != 0) {
            mRecyclerView.scrollToPosition(lastItemScrollPosition);
        }
    }
}