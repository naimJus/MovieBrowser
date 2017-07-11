package example.com.moviesfragment;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class MovieActivity extends YouTubeBaseActivity {

    private Movie movie;
    private TextView nameTV, yearTV, ratingTV, descriptionTV;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener initializedListener;
    private static final String YOUTUBE_KEY = "AIzaSyBp9dpHGyl_0MUM8z_SwKPXeWEabVlUSKk";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        nameTV = (TextView) findViewById(R.id.movie_name);
        yearTV = (TextView) findViewById(R.id.movie_year);
        ratingTV = (TextView) findViewById(R.id.movie_rating);
        descriptionTV = (TextView) findViewById(R.id.movie_description);

        // get the bundle from the intent
        //unwrap the bundle and get the movie;
        Bundle b = getIntent().getBundleExtra(HomeActivity.BUNDLE);
        movie = b.getParcelable(HomeActivity.POSITION);

        nameTV.setText(movie.getName());
        yearTV.setText(String.valueOf(movie.getYear()));
        ratingTV.setText(String.valueOf(movie.getRating()));
        descriptionTV.setText(movie.getDescription());
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.playerYouTube);
        initializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(movie.getTrailerCode());
                youTubePlayer.addFullscreenControlFlag(BIND_ADJUST_WITH_ACTIVITY);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        youTubePlayerView.initialize(YOUTUBE_KEY, initializedListener);
    }

}
