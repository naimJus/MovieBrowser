package example.com.moviesfragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.util.List;

import example.com.moviesfragment.gson.Movie;
import example.com.moviesfragment.gson.Torrent;

public class MovieActivity extends YouTubeBaseActivity {

    private static final String YOUTUBE_KEY = "AIzaSyBp9dpHGyl_0MUM8z_SwKPXeWEabVlUSKk";
    private static final String LOG = MovieActivity.class.getSimpleName();
    ImageView movieImage;
    private Movie movie;
    private TextView nameTV, yearTV, ratingTV, genreTV, descriptionTV;
    private Button quality720Btn, quality1080Btn, quality3dBtn;
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
        quality720Btn = (Button) findViewById(R.id.quality_720p);
        quality1080Btn = (Button) findViewById(R.id.quality_1080p);
        quality3dBtn = (Button) findViewById(R.id.quality_3d);
        movieImage = (ImageView) findViewById(R.id.movie_image);

        // get the bundle from the intent
        //unwrap the bundle and get the movie;
        Bundle b = getIntent().getBundleExtra(MoviesListFragment.BUNDLE);
        movie = b.getParcelable(MoviesListFragment.POSITION);
        Log.v(LOG, movie.toString());

        Picasso.with(this)
                .load(movie.getMediumCoverImage())
                .fit()
                .into(movieImage);

        nameTV.setText(movie.getTitle());
        yearTV.setText(movie.getYear().toString());
        ratingTV.setText(movie.getRating().toString());
        descriptionTV.setText(movie.getSummary());
        List<Torrent> torrents = movie.getTorrents();


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
