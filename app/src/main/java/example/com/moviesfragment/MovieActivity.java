package example.com.moviesfragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private Button quality720Btn, quality1080Btn, quality3dBtn;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        nameTV = (TextView) findViewById(R.id.movie_name);
        yearTV = (TextView) findViewById(R.id.movie_year);
        ratingTV = (TextView) findViewById(R.id.movie_rating);
        descriptionTV = (TextView) findViewById(R.id.movie_description);
        quality720Btn = (Button) findViewById(R.id.quality720p);
        quality1080Btn = (Button) findViewById(R.id.quality1080p);
        quality3dBtn = (Button) findViewById(R.id.quality3d);

        // get the bundle from the intent
        //unwrap the bundle and get the movie;
        Bundle b = getIntent().getBundleExtra(HomeActivity.BUNDLE);
        movie = b.getParcelable(HomeActivity.POSITION);

        nameTV.setText(movie.getName());
        yearTV.setText(String.valueOf(movie.getYear()));
        ratingTV.setText(String.valueOf(movie.getRating()));
        descriptionTV.setText(movie.getDescription());
        final String url720 = movie.getUrl720p();
        String url1080 = movie.getUrl1080p();
        String url3d = movie.getUrl3d();

        if (url720 == null)
            quality720Btn.setVisibility(View.GONE);
        if (url1080 == null)
            quality1080Btn.setVisibility(View.GONE);
        if (url3d == null)
            quality3dBtn.setVisibility(View.GONE);

        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);

        quality720Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setData(Uri.parse(url720));
                startActivity(intent);
            }
        });
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
