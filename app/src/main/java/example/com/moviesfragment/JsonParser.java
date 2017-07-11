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
    private StringBuilder sb;
    private String result;
    private MoviesDataSource moviesDataSource;
    private BufferedReader bufferedReader;
    private HttpURLConnection httpURLConnection;
    float resultId;

    public JsonParser(Context context) {
        this.context = context;
        moviesDataSource = new MoviesDataSource(context);
        moviesDataSource.open();
    }

    void getJsonFromWeb(String stringUrl) {
        try {
            URL url = new URL(stringUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            sb = new StringBuilder();

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null) {
            fromJsonToDatabase(result);
        }
    }

    private void fromJsonToDatabase(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray movies = data.getJSONArray("movies");


            for (int i = 0; i < movies.length(); i++) {
                Log.v("SQL", movies.length() + "");
                JSONObject m = movies.getJSONObject(i);
                String id = m.getString("id");
                String name = m.getString("title");
                String year = m.getString("year");
                String rating = m.getString("rating");
                String summary = m.getString("summary");
                String imageUrl = m.getString("medium_cover_image");
                String trailerCode = m.getString("yt_trailer_code");
                resultId = moviesDataSource.createMovie(Long.valueOf(id), name, summary, Integer.valueOf(year), imageUrl, Float.valueOf(rating), trailerCode);
                if (resultId == -1) {
                    Toast.makeText(context, "The Movie " + name + " is already in the database ", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            moviesDataSource.close();
        }
    }
}
