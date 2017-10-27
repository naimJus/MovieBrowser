package example.com.moviesfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.HashMap;

public class SearchFragment extends Fragment {

    public static final String SEARCH = "search";
    private static final String LOG = SearchFragment.class.getSimpleName();
    Spinner qualitySpinner, genreSpinner, ratingSpinner, orderBySpinner;
    EditText nameET;
    Button searchBtn;
    HashMap<String, String> searchParams = new HashMap<>(5);
    SqlStatements sqlStatements;
    MoviesDataSource moviesDataSource;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        moviesDataSource = new MoviesDataSource(getActivity().getApplicationContext());
        moviesDataSource.open();
        sqlStatements = new SqlStatements();

        qualitySpinner = (Spinner) view.findViewById(R.id.qualitySpinner);
        genreSpinner = (Spinner) view.findViewById(R.id.genreSpinner);
        ratingSpinner = (Spinner) view.findViewById(R.id.ratingSpinner);
        orderBySpinner = (Spinner) view.findViewById(R.id.orderBySpinner);
        searchBtn = (Button) view.findViewById(R.id.searchBtn);
        nameET = (EditText) view.findViewById(R.id.nameET);

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
                String name = nameET.getText().toString();

                String orderSql = sqlStatements.generateOrderSql(order);
                String ratingSql = sqlStatements.generateRatingSql(rating);
                String genreSql = sqlStatements.generateGenreSql(genre);
                String qualitySql = sqlStatements.generateQualitySql(quality);
                String nameSql = sqlStatements.generateNameSql(name);

                searchParams.put("Search", nameSql);
                searchParams.put("Quality", qualitySql);
                searchParams.put("Genre", genreSql);
                searchParams.put("Rating", ratingSql);
                searchParams.put("Order", orderSql);


                Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                intent.putExtra(SEARCH, searchParams);
                startActivity(intent);

            }
        });
        return view;
    }
}