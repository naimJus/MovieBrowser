package example.com.moviesfragment;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jusuf on 11.7.2017.
 */

public class JsonParser {
    private Context context;
    private String result;
    private MoviesDataSource moviesDataSource;
    float resultId;
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
        Log.v(LOG, "fromJsonToDatabase called");
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject data = jsonObject.getJSONObject("data");
            int movie_count = data.getInt("movie_count");
            JSONArray movies = data.getJSONArray("movies");


            for (int i = 0; i < movies.length(); i++) {
                JSONObject m = movies.getJSONObject(i);
                String id = m.getString("id");
                String name = m.getString("title");
                String year = m.getString("year");
                String rating = m.getString("rating");
                String summary = m.getString("summary");
                String imageUrl = m.getString("medium_cover_image");
                String trailerCode = m.getString("yt_trailer_code");
                resultId = moviesDataSource.createMovie(Long.valueOf(id), name, summary, Integer.valueOf(year), imageUrl, Float.valueOf(rating), trailerCode);
                if (resultId == -1)
                    Toast.makeText(context, "The Movie " + name + " is already in the database ", Toast.LENGTH_SHORT).show();
            }
            pages = movie_count / 50;
            if (movie_count % 50 != 0)
                pages = pages + 1;
            Log.v("Pages", "Pages " + pages);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pages;
    }

}
