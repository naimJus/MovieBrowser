package example.com.moviesfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MovieActivity extends Activity {

    private Movie movie;
    private TextView nameTV, yearTV, ratingTV, descriptionTV;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        nameTV = (TextView) findViewById(R.id.movie_name);
        yearTV = (TextView) findViewById(R.id.movie_year);
        ratingTV = (TextView) findViewById(R.id.movie_rating);
        descriptionTV = (TextView) findViewById(R.id.movie_description);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        movie = bundle.getParcelable(HomeActivity.POSITION);

        nameTV.setText(movie.getName());
        yearTV.setText(String.valueOf(movie.getYear()));
        ratingTV.setText(String.valueOf(movie.getRating()));
        descriptionTV.setText(movie.getDescription());
    }
}
