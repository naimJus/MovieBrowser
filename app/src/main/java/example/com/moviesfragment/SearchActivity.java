package example.com.moviesfragment;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SearchActivity extends Activity {

    Spinner qualitySpinner, genreSpinner, ratingSpinner;
    EditText searchET;
    Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        qualitySpinner = (Spinner) findViewById(R.id.qualitySpinner);
        genreSpinner = (Spinner) findViewById(R.id.genreSpinner);
        ratingSpinner = (Spinner) findViewById(R.id.ratingSpinner);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        searchET = (EditText) findViewById(R.id.searchET);

        

    }
}

