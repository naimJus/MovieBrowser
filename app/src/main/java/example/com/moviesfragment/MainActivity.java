package example.com.moviesfragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.Button;

import com.facebook.stetho.Stetho;

public class MainActivity extends Activity {
    public static final String FILTER = "TOPRATED";
    Button browseBtn, recentBtn, topBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);

//      Display the recent movies
        FragmentManager manager = getFragmentManager();

/*
        browseBtn = (Button) findViewById(R.id.browseBtn);
        recentBtn = (Button) findViewById(R.id.recentBtn);
        topBtn = (Button) findViewById(R.id.topBtn);

        browseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        recentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MoviesListFragment.class);
                intent.putExtra(FILTER, MovieSQLiteHelper.KEY_ID + " DESC");
                startActivity(intent);
            }
        });
        topBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
*/

    }
}