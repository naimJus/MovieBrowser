package example.com.moviesfragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MoviesDataSource {
    private SQLiteDatabase database;
    private MovieSQLiteHelper dbHelper;
    private static final String LOG = MoviesDataSource.class.getSimpleName();

    MoviesDataSource(Context context) {
        dbHelper = MovieSQLiteHelper.getsInstance(context);
    }

    void open() throws SQLiteException {
        database = dbHelper.getWritableDatabase();
    }

    void close() {
        dbHelper.close();
    }

    List<Movie> getAllMovies() {
        Cursor cursor = database.query(MovieSQLiteHelper.TABLE_NAME, null, null, null, null, null, null, "50");
        List<Movie> movies = cursorToList(cursor);
        return movies;

    }

    List<Movie> sortBy(String orderBy) {
        Cursor cursor = database.query(MovieSQLiteHelper.TABLE_NAME, null, null, null, null, null, orderBy, "50");
        List<Movie> movies = cursorToList(cursor);
        return movies;
    }

    List<Movie> sortAndLimit(String orderBy, String limit) {
        Cursor cursor = database.query(MovieSQLiteHelper.TABLE_NAME, null, null, null, null, null, orderBy, limit);
        List<Movie> movies = cursorToList(cursor);
        return movies;
    }

    List<Movie> searchMovies(HashMap<String, String> searchParams) {

        String quality = searchParams.get("Quality");
        String genre = searchParams.get("Genre");
        String rating = searchParams.get("Rating");
        String order = searchParams.get("Order");
        String search = searchParams.get("Search");


        Cursor cursor = database.query(MovieSQLiteHelper.TABLE_NAME,
                null,
                quality + " AND " +
                        genre + " AND " +
                        rating + " AND " +
                        search,
                null,
                null,
                null,
                order);

        List<Movie> movies = cursorToList(cursor);
        return movies;
    }

    private List<Movie> cursorToList(Cursor cursor) {
        List<Movie> movies = new ArrayList<Movie>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Movie movie = new Movie();
                String[] hash = {cursor.getString(10), cursor.getString(11), cursor.getString(12)};
                movie.setId(cursor.getLong(cursor.getColumnIndex(MovieSQLiteHelper.KEY_ID)));
                movie.setName(cursor.getString(1));
                movie.setDescription(cursor.getString(2));
                movie.setYear(cursor.getInt(3));
                movie.setImageUrl(cursor.getString(4));
                movie.setRating(cursor.getFloat(5));
                movie.setTrailerCode(cursor.getString(4));
                movie.setGenre(cursor.getString(5));
                movie.setQuality(cursor.getString(6));
                movie.setUrl720p(cursor.getString(7));
                movie.setUrl1080p(cursor.getString(8));
                movie.setUrl3d(cursor.getString(9));
                movie.setHashValues(hash);
                movies.add(movie);
            }
        }
        return movies;
    }

    long createMovie(long id, String name, String description, int year, String imageUrl, float rating, String trailerCode, String genre, String quality, HashMap<String, String> torrents, HashMap<String, String> hashValues) {
        long resultId = -1;
        ContentValues values = new ContentValues();
        values.put(MovieSQLiteHelper.KEY_ID, id);
        values.put(MovieSQLiteHelper.KEY_NAME, name);
        values.put(MovieSQLiteHelper.KEY_DESCRIPTION, description);
        values.put(MovieSQLiteHelper.KEY_YEAR, year);
        values.put(MovieSQLiteHelper.KEY_IMAGE_URL, imageUrl);
        values.put(MovieSQLiteHelper.KEY_RATING, rating);
        values.put(MovieSQLiteHelper.KEY_TRAILER, trailerCode);
        values.put(MovieSQLiteHelper.KEY_GENRE, genre);
        values.put(MovieSQLiteHelper.KEY_QUALITY, quality);
        values.put(MovieSQLiteHelper.KEY_720P, torrents.get("720p"));
        values.put(MovieSQLiteHelper.KEY_1080P, torrents.get("1080p"));
        values.put(MovieSQLiteHelper.KEY_3D, torrents.get("3D"));
        values.put(MovieSQLiteHelper.KEY_HASH720P, hashValues.get("720p"));
        values.put(MovieSQLiteHelper.KEY_HASH1080P, hashValues.get("1080p"));
        values.put(MovieSQLiteHelper.KEY_HASH3D, hashValues.get("3D"));
        resultId = database.insertWithOnConflict(MovieSQLiteHelper.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        return resultId;
    }
}
