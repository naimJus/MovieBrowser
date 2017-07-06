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

    public MoviesDataSource(Context context) {
        dbHelper = MovieSQLiteHelper.getsInstance(context);
    }

    public void open() throws SQLiteException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Movie> getAllMovies() {
        List<Movie> movieList = new ArrayList<Movie>();
        Cursor cursor = database.query(MovieSQLiteHelper.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Movie movie = cursorToMovie(cursor);
            movieList.add(movie);
            cursor.moveToNext();
        }
        return movieList;
    }

    public Movie createMovie(long id, String name, String description, int year, String imageUrl, float rating) {
        ContentValues values = new ContentValues();
        values.put(MovieSQLiteHelper.KEY_ID, id);
        values.put(MovieSQLiteHelper.KEY_NAME, name);
        values.put(MovieSQLiteHelper.KEY_DESCRIPTION, description);
        values.put(MovieSQLiteHelper.KEY_YEAR, year);
        values.put(MovieSQLiteHelper.KEY_IMAGE_URL, imageUrl);
        values.put(MovieSQLiteHelper.KEY_RATING, rating);
        long resultId = database.insert(MovieSQLiteHelper.TABLE_NAME, null, values);
        Log.v("SQL" , resultId + "");
        Cursor cursor = database.query(MovieSQLiteHelper.TABLE_NAME, null, null, null, null, null, null);
        Movie movie = cursorToMovie(cursor);
        cursor.close();
        return movie;
    }


    private Movie cursorToMovie(Cursor cursor) {
        Movie movie = new Movie();
        movie.setId(cursor.getLong(0));
        movie.setName(cursor.getString(1));
        movie.setDescription(cursor.getString(2));
        movie.setYear(cursor.getInt(3));
        movie.setImageUrl(cursor.getString(4));
        movie.setRating(cursor.getFloat(5));
        return movie;
    }
}
