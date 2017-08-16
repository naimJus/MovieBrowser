package example.com.moviesfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SearchActivity extends Activity {

    Spinner qualitySpinner, genreSpinner, ratingSpinner;
    EditText searchET;
    Button searchBtn;
    public static final String SEARCH = "search";
    private static final String LOG = SearchActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        qualitySpinner = (Spinner) findViewById(R.id.qualitySpinner);
        genreSpinner = (Spinner) findViewById(R.id.genreSpinner);
        ratingSpinner = (Spinner) findViewById(R.id.ratingSpinner);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        searchET = (EditText) findViewById(R.id.searchET);


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] searchParameters = {
                        searchET.getText().toString(),
                        qualitySpinner.getSelectedItem().toString(),
                        genreSpinner.getSelectedItem().toString(),
                        ratingSpinner.getSelectedItem().toString()};

                Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
                intent.putExtra(SEARCH, searchParameters);
                startActivity(intent);
            }
        });


    }
}

