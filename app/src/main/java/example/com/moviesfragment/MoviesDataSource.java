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
        Cursor cursor = database.query(MovieSQLiteHelper.TABLE_NAME, null, null, null, null, null, null, "10");
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

    List<Movie> searchMovies(String[] search) {
        if (search[3].equalsIgnoreCase("all"))
            search[3] = "0";
//        if (search[2].equalsIgnoreCase("all"))
//            search[2] =

        Cursor cursor = database.query(MovieSQLiteHelper.TABLE_NAME,
                null,
                MovieSQLiteHelper.KEY_NAME + " LIKE " + search[0] + " AND " +
//                MovieSQLiteHelper.KEY_QUALITY + "=" + search[1] + " AND " +
                        MovieSQLiteHelper.KEY_GENRE + "=" + search[2] + " AND " +
                        MovieSQLiteHelper.KEY_RATING + ">=" + search[3],
                null,
                null,
                null,
                null);
        List<Movie> movies = cursorToList(cursor);
        return movies;
    }

    private List<Movie> cursorToList(Cursor cursor) {
        List<Movie> movies = new ArrayList<Movie>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Movie movie = new Movie();
                movie.setId(cursor.getLong(cursor.getColumnIndex(MovieSQLiteHelper.KEY_ID)));
                movie.setName(cursor.getString(1));
                movie.setDescription(cursor.getString(2));
                movie.setYear(cursor.getInt(3));
                movie.setImageUrl(cursor.getString(4));
                movie.setRating(cursor.getFloat(5));
                movie.setTrailerCode(cursor.getString(4));
                movie.setGenre(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_GENRE)));
                movie.setUrl720p(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_720P)));
                movie.setUrl1080p(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_1080P)));
                movie.setUrl3d(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_3D)));
                movies.add(movie);
            }
        }
        return movies;
    }

    long createMovie(long id, String name, String description, int year, String imageUrl, float rating, String trailerCode, String genre, HashMap<String, String> torrents) {
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
        values.put(MovieSQLiteHelper.KEY_720P, torrents.get("720p"));
        values.put(MovieSQLiteHelper.KEY_1080P, torrents.get("1080p"));
        values.put(MovieSQLiteHelper.KEY_3D, torrents.get("3D"));
        resultId = database.insertWithOnConflict(MovieSQLiteHelper.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        return resultId;
    }
}
