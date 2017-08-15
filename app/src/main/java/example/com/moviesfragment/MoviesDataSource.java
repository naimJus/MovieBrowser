package example.com.moviesfragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jusuf on 04.7.2017.
 */

public class MoviesDataSource {
    SQLiteDatabase database;
    MovieSQLiteHelper dbHelper;
    private static final String LOGTAG = MoviesDataSource.class.getSimpleName();

    public MoviesDataSource(Context context) {
        dbHelper = MovieSQLiteHelper.getsInstance(context);
    }

    public void open() throws SQLiteException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    List<Movie> getAllMovies() {
        Cursor cursor = database.query(MovieSQLiteHelper.TABLE_NAME, null, null, null, null, null, null);
        List<Movie> movies = cursorToList(cursor);
        return movies;

    }

    List<Movie> sortBy(String orderBy) {

        Cursor cursor = database.query(MovieSQLiteHelper.TABLE_NAME, null, null, null, null, null, orderBy);
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
                movie.setTrailerCode(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_TRAILER)));
                movies.add(movie);
            }
        }
        return movies;
    }

    long createMovie(long id, String name, String description, int year, String imageUrl, float rating, String trailerCode) {
        long resultId = -1;
        ContentValues values = new ContentValues();
        values.put(MovieSQLiteHelper.KEY_ID, id);
        values.put(MovieSQLiteHelper.KEY_NAME, name);
        values.put(MovieSQLiteHelper.KEY_DESCRIPTION, description);
        values.put(MovieSQLiteHelper.KEY_YEAR, year);
        values.put(MovieSQLiteHelper.KEY_IMAGE_URL, imageUrl);
        values.put(MovieSQLiteHelper.KEY_RATING, rating);
        values.put(MovieSQLiteHelper.KEY_TRAILER, trailerCode);
        resultId = database.insertWithOnConflict(MovieSQLiteHelper.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        Log.v(LOGTAG, "Movie " + name + ", with ID " + id + " was added in database");
        return resultId;
    }
}
