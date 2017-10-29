package example.com.moviesfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import example.com.moviesfragment.gson.Movie;

public class SearchResultsActivity extends AppCompatActivity {
    public static final String POSITION = ".Model.Movie";
    public static final String BUNDLE = "bundle";
    private static final String FIRSTITEMID = "firstItemId";
    ListView listView;
    MoviesDataSource moviesDataSource;
    List<Movie> getMovies;
    HashMap<String, String> sqlParams;
    int firstItemId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        if (savedInstanceState != null) {
            firstItemId = savedInstanceState.getInt(FIRSTITEMID);
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.searchResultToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listView = (ListView) findViewById(R.id.list_results);
        moviesDataSource = new MoviesDataSource(this);
        moviesDataSource.open();


        Intent i = getIntent();
        if (i != null) {
            sqlParams = (HashMap<String, String>) i.getSerializableExtra(SearchFragment.SEARCH);
            getMovies = moviesDataSource.searchMovies(sqlParams);
        } else {
            getMovies = moviesDataSource.getAllMovies();
        }

        if (getMovies.size() == 0) {
            TextView textView = new TextView(this);
            textView.setText(R.string.nothing_found);
            listView.setVisibility(View.GONE);
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.search_results);
            linearLayout.addView(textView);
            textView.setTextSize(20);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


//            textView.setGravity();

        }
        refreshAdapter();
        Log.v("SearchResultActivity", sqlParams.toString());
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

//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//
//            }
//
//            public void onScroll(AbsListView view, int firstVisibleItem,
//                                 int visibleItemCount, int totalItemCount) {
//
//                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
//                {
//                    if(flag_loading == false)
//                    {
//                        flag_loading = true;
//                        additems();
//                    }
//                }
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortByName:
                if (sqlParams.get("Order").equals(MovieSQLiteHelper.KEY_NAME + " ASC")) {
                    sqlParams.put("Order", MovieSQLiteHelper.KEY_NAME + " DESC");
                } else {
                    sqlParams.put("Order", MovieSQLiteHelper.KEY_NAME + " ASC");
                }
                getMovies = moviesDataSource.searchMovies(sqlParams);
                refreshAdapter();
                return true;
            case R.id.sortByRating:
                if (sqlParams.get("Order").equals(MovieSQLiteHelper.KEY_RATING + " ASC")) {
                    sqlParams.put("Order", MovieSQLiteHelper.KEY_RATING + " DESC");
                } else {
                    sqlParams.put("Order", MovieSQLiteHelper.KEY_RATING + " ASC");
                }
                getMovies = moviesDataSource.searchMovies(sqlParams);
                refreshAdapter();
                return true;
            case R.id.sortByYear:
                if (sqlParams.get("Order").equals(MovieSQLiteHelper.KEY_YEAR + " ASC")) {
                    sqlParams.put("Order", MovieSQLiteHelper.KEY_YEAR + " DESC");
                } else {
                    sqlParams.put("Order", MovieSQLiteHelper.KEY_YEAR + " ASC");
                }
                getMovies = moviesDataSource.searchMovies(sqlParams);
                refreshAdapter();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
        firstItemId = listView.getFirstVisiblePosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
        moviesDataSource.open();
        refreshAdapter();
    }

    public void refreshAdapter() {
        MoviesAdapter moviesAdapter = new MoviesAdapter(this, getMovies);
        listView.setAdapter(moviesAdapter);
        if (firstItemId != 0)
            listView.setSelectionFromTop(firstItemId, 0);
    }
}
