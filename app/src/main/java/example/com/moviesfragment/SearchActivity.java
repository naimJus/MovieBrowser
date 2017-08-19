package example.com.moviesfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.HashMap;

public class SearchActivity extends Activity {

    Spinner qualitySpinner, genreSpinner, ratingSpinner, orderBySpinner;
    EditText searchET;
    Button searchBtn;
    public static final String SEARCH = "search";
    private static final String LOG = SearchActivity.class.getSimpleName();
    HashMap<String, String> searchParams = new HashMap<>(4);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        qualitySpinner = (Spinner) findViewById(R.id.qualitySpinner);
        genreSpinner = (Spinner) findViewById(R.id.genreSpinner);
        ratingSpinner = (Spinner) findViewById(R.id.ratingSpinner);
        orderBySpinner = (Spinner) findViewById(R.id.orderBySpinner);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        searchET = (EditText) findViewById(R.id.searchET);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                StringBuilder sb = new StringBuilder("'% ");
//                sb.append(searchET.getText().toString());
//                sb.append(" %'");
//                String[] searchParameters = {
//                        sb.toString(),
//                        qualitySpinner.getSelectedItem().toString(),
//                        genreSpinner.getSelectedItem().toString(),
//                        ratingSpinner.getSelectedItem().toString()};

                String quality = qualitySpinner.getSelectedItem().toString();
                String genre = genreSpinner.getSelectedItem().toString();
                String rating = ratingSpinner.getSelectedItem().toString();
                String order = orderBySpinner.getSelectedItem().toString();
                String search = searchET.getText().toString();

                searchParams.put("Quality", quality);
                searchParams.put("Genre", genre);
                searchParams.put("Rating", rating);
                searchParams.put("Order", order);
                searchParams.put("Search", search);


                Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
                intent.putExtra(SEARCH, searchParams);
                startActivity(intent);
            }
        });


    }
}

