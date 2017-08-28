package example.com.moviesfragment;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import example.com.moviesfragment.gson.Data;
import example.com.moviesfragment.gson.Example;
import example.com.moviesfragment.gson.Movie;
import example.com.moviesfragment.gson.Torrent;

/**
 * Created by jusuf on 11.7.2017.
 */

public class JsonParser {
    private Context context;
    private String result;
    private MoviesDataSource moviesDataSource;
    private String LOG = JsonParser.class.getSimpleName();
    int pages;

    public JsonParser(Context context) {
        this.context = context;
        moviesDataSource = new MoviesDataSource(context);
        moviesDataSource.open();
    }

    public int getJsonFromWeb(String stringUrl) {
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");
            }
            bufferedReader.close();
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null) {
            pages = gsonToDb(result);
        }

        return pages;
    }


    private int gsonToDb(String json) {
        long resultId;
        try {
            Gson gson = new Gson();
            Example example = gson.fromJson(json, Example.class);
            Data data = example.getData();
            List<Movie> movies = data.getMovies();
            int movieCount = data.getMovieCount();

            for (Movie movie : movies) {
                HashMap<String, String> torrentMap = new HashMap<>();
                HashMap<String, String> hashMap = new HashMap<>();

                List<Torrent> torrents = movie.getTorrents();
                StringBuilder gen = new StringBuilder();

                int id = movie.getId();
                String title = movie.getTitle();
                String summary = movie.getSummary();
                int year = movie.getYear();
                String mediumImage = movie.getMediumCoverImage();
                double rating = movie.getRating();
                String trailerCode = movie.getYtTrailerCode();
                List<String> genre = movie.getGenres();
                for (String s : genre) {
                    gen.append(s);
                }
                for (Torrent torrent : torrents) {

                    String quality = torrent.getQuality();
                    String hash = torrent.getHash();
                    torrentMap.put(quality, torrent.getUrl());
                    hashMap.put(quality, hash);
                }

                resultId = moviesDataSource.createMovie(id, title, summary, year, mediumImage, rating, trailerCode, gen.toString(), torrentMap, hashMap);
            }

            pages = movieCount / 50;
            if (movieCount % 50 != 0)
                pages = pages + 1;
        } catch (
                Exception e)

        {
            e.printStackTrace();
        }
        return pages;
    }

}
