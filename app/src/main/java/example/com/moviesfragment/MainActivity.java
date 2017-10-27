package example.com.moviesfragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;

import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {
    public static final String FILTER = "TOPRATED";
    Button browseBtn, recentBtn, topBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);


//      Display the recent movies
        FragmentManager manager = getFragmentManager();
        Fragment moviesListFragment = new MoviesListFragment();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, moviesListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

  /*  @Override
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
*/
}