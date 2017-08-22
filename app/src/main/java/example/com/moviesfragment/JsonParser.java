package example.com.moviesfragment;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jusuf on 11.7.2017.
 */

public class JsonParser {
    private Context context;
    private String result;
    private MoviesDataSource moviesDataSource;
    private long resultId;
    private String LOG = JsonParser.class.getSimpleName();
    int pages;

    public JsonParser(Context context) {
        this.context = context;
        moviesDataSource = new MoviesDataSource(context);
        moviesDataSource.open();
    }

    public int getJsonFromWeb(String stringUrl) {
        Log.v(LOG, "the url is " + stringUrl);
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
            pages = fromJsonToDatabase(result);
        }

        return pages;
    }


    private int fromJsonToDatabase(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject data = jsonObject.getJSONObject("data");
            int movie_count = data.getInt("movie_count");
            JSONArray movies = data.getJSONArray("movies");

            for (int i = 0; i < movies.length(); i++) {
                StringBuilder genreSb = new StringBuilder();
                StringBuilder qualitySb = new StringBuilder();
                HashMap<String, String> torrent = new HashMap<>(3);
                HashMap<String, String> hashes = new HashMap<>(3);

                JSONObject m = movies.getJSONObject(i);
                String id = m.getString("id");
                String name = m.getString("title");
                String year = m.getString("year");
                String rating = m.getString("rating");
                String summary = m.getString("summary");
                String imageUrl = m.getString("medium_cover_image");
                String trailerCode = m.getString("yt_trailer_code");

                JSONArray genresJsonArray = m.getJSONArray("genres");
                for (int j = 0; j < genresJsonArray.length(); j++) {
                    genreSb.append(genresJsonArray.getString(j));
                    genreSb.append(" ");
                }

                JSONArray torrentsJsonArray = m.getJSONArray("torrents");
                for (int k = 0; k < torrentsJsonArray.length(); k++) {
                    JSONObject torrentJson = torrentsJsonArray.getJSONObject(k);
                    String url = torrentJson.getString("url");
                    String quality = torrentJson.getString("quality");
                    String hash = torrentJson.getString("hash");
                    qualitySb.append(quality);
                    qualitySb.append(" ");
                    torrent.put(quality, url);
                    hashes.put(quality,hash);
                }
                resultId = moviesDataSource.createMovie(Long.valueOf(id), name, summary, Integer.valueOf(year), imageUrl, Float.valueOf(rating), trailerCode, genreSb.toString(), qualitySb.toString(), torrent, hashes);
            }

            pages = movie_count / 50;
            if (movie_count % 50 != 0)
                pages = pages + 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pages;
    }

}
