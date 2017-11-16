package example.com.moviesfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import example.com.moviesfragment.gson.Movie;

public class MovieActivity extends YouTubeBaseActivity {

    private static final String YOUTUBE_KEY = "AIzaSyBp9dpHGyl_0MUM8z_SwKPXeWEabVlUSKk";
    private static final String LOG = MovieActivity.class.getSimpleName();
    private ImageView movieImage;
    private Movie movie;
    private TextView nameTV, yearTV, ratingTV, genreTV, descriptionTV;
    private RadioButton radioButton720p, radioButton1080p, radioButton3d;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener initializedListener;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        nameTV = (TextView) findViewById(R.id.movie_name_TV);
        yearTV = (TextView) findViewById(R.id.movie_year_TV);
        ratingTV = (TextView) findViewById(R.id.movie_rating_TV);
        genreTV = (TextView) findViewById(R.id.movie_genre_TV);
        descriptionTV = (TextView) findViewById(R.id.movie_description_TV);

        radioButton720p = (RadioButton) findViewById(R.id.quality720pRadioButton);
        radioButton1080p = (RadioButton) findViewById(R.id.quality1080pRadioButton);
        radioButton3d = (RadioButton) findViewById(R.id.quality3dRadioButton);

        movieImage = (ImageView) findViewById(R.id.movie_image);

        // get the bundle from the intent
        //unwrap the bundle and get the movie;
        Bundle b = getIntent().getBundleExtra(MoviesListFragment.BUNDLE);
        movie = b.getParcelable(MoviesListFragment.POSITION);

        String quality = movie.getAvailableInQuality();

        if (quality.contains("720p")) {
            radioButton720p.setVisibility(View.VISIBLE);
            radioButton720p.setChecked(true);
        }
        if (quality.contains("1080p"))
            radioButton1080p.setVisibility(View.VISIBLE);
        if (quality.contains("3D"))
            radioButton3d.setVisibility(View.VISIBLE);


        Log.v(LOG, movie.toString());

        Picasso.with(this)
                .load(movie.getMediumCoverImage())
                .fit()
                .into(movieImage);

        nameTV.setText(movie.getTitle());
        yearTV.setText(getResources().getString(R.string.year) + " " + movie.getYear());
        ratingTV.setText(getResources().getString(R.string.rating) + " " + movie.getRating());
        genreTV.setText(getResources().getString(R.string.genre) + " " + movie.getGenre());
        descriptionTV.setText(movie.getSummary());


        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.playerYouTube);
        initializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(movie.getYtTrailerCode());
                youTubePlayer.addFullscreenControlFlag(BIND_ADJUST_WITH_ACTIVITY);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        youTubePlayerView.initialize(YOUTUBE_KEY, initializedListener);
    }

}
