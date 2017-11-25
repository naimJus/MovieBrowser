package example.com.moviesfragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import example.com.moviesfragment.gson.Movie;
import example.com.moviesfragment.gson.Torrent;

public class MovieActivity extends YouTubeBaseActivity {

    private static final String YOUTUBE_KEY = "AIzaSyBp9dpHGyl_0MUM8z_SwKPXeWEabVlUSKk";
    private static final String LOG = MovieActivity.class.getSimpleName();
    private ImageView movieImage;
    private Movie movie;
    private TextView nameTv, yearTv, ratingTv, genreTv, descriptionTv, runtimeTv;
    private RadioButton radioButton720p, radioButton1080p, radioButton3d;
    private Button downloadBtn, magnetBtn;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener initializedListener;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        MoviesDataSource moviesDataSource = new MoviesDataSource(this);
        moviesDataSource.open();


        nameTv = (TextView) findViewById(R.id.movie_name_TV);
        yearTv = (TextView) findViewById(R.id.movie_year_TV);
        ratingTv = (TextView) findViewById(R.id.movie_rating_TV);
        genreTv = (TextView) findViewById(R.id.movie_genre_TV);
        descriptionTv = (TextView) findViewById(R.id.movie_description_TV);
        runtimeTv = (TextView) findViewById(R.id.runtimeTextView);

        radioButton720p = (RadioButton) findViewById(R.id.quality720pRadioButton);
        radioButton1080p = (RadioButton) findViewById(R.id.quality1080pRadioButton);
        radioButton3d = (RadioButton) findViewById(R.id.quality3dRadioButton);

        downloadBtn = (Button) findViewById(R.id.downloadBtn);
        magnetBtn = (Button) findViewById(R.id.magnetBtn);

        movieImage = (ImageView) findViewById(R.id.movie_image);


        // get the bundle from the intent
        //unwrap the bundle and get the movie;


        int b = getIntent().getIntExtra(MoviesListFragment.BUNDLE, 1);
        List<Movie> movies = moviesDataSource.getMovie(b + "");
        movie = movies.get(0);

        final HashMap<String, String> moviesMap = new HashMap<>();
        for (Movie m : movies) {
            Torrent t = m.getTorrent();
            moviesMap.put(t.getQuality(), t.getUrl());
        }

        if (moviesMap.containsKey("720p")) {
            radioButton720p.setVisibility(View.VISIBLE);
            radioButton720p.setChecked(true);
        }
        if (moviesMap.containsKey("1080p")) {
            radioButton1080p.setVisibility(View.VISIBLE);
            if (!radioButton720p.isChecked())
                radioButton1080p.setChecked(true);
        }
        if (moviesMap.containsKey("3D")) {
            radioButton3d.setVisibility(View.VISIBLE);
            if (!radioButton720p.isChecked() && !radioButton1080p.isChecked())
                radioButton3d.setChecked(true);
        }


        Log.v(LOG, movie.toString());

        Picasso.with(this)
                .load(movie.getMediumCoverImage())
                .fit()
                .into(movieImage);

        nameTv.setText(movie.getTitle());
        yearTv.setText(getResources().getString(R.string.year) + " " + movie.getYear());
        ratingTv.setText(getResources().getString(R.string.rating) + " " + movie.getRating());
        genreTv.setText(getResources().getString(R.string.genre) + " " + movie.getGenre());
        descriptionTv.setText(movie.getDescriptionFull());
        runtimeTv.setText(getResources().getString(R.string.runtime) + " " + movie.getRuntime());


        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "";

                if (radioButton720p.isChecked())
                    url = moviesMap.get("720p");
                if (radioButton1080p.isChecked())
                    url = moviesMap.get("1080p");
                if (radioButton3d.isChecked())
                    url = moviesMap.get("3D");

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
//                intent.setDataAndType(Uri.parse(url), "application/x-bittorrent");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        magnetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


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
