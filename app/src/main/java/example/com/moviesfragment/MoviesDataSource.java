package example.com.moviesfragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import example.com.moviesfragment.gson.Movie;
import example.com.moviesfragment.gson.Torrent;

public class MoviesDataSource {
    private static final String LOG = MoviesDataSource.class.getSimpleName();
    private SQLiteDatabase database;
    private MovieSQLiteHelper dbHelper;

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

                movie.setId(cursor.getInt(cursor.getColumnIndex(MovieSQLiteHelper.KEY_ID)));
                movie.setTitle(cursor.getString(1));
                movie.setSummary(cursor.getString(2));
                movie.setYear(cursor.getInt(3));
                movie.setMediumCoverImage(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_BACKGROUND_IMAGE_URL)));
                movie.setRating(cursor.getDouble(cursor.getColumnIndex(MovieSQLiteHelper.KEY_RATING)));
                movie.setYtTrailerCode(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_YOUTUBE_TRAILER)));

                Torrent[] torrents = new Torrent[3];
                torrents[0] = new Torrent();
                torrents[0].setUrl(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_720P)));
                torrents[0].setHash(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_HASH720P)));
                torrents[0].setSize(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_720P_SIZE)));
                torrents[1] = new Torrent();
                torrents[1].setUrl(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_1080P)));
                torrents[1].setHash(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_HASH1080P)));
                torrents[1].setSize(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_1080P_SIZE)));
                torrents[2] = new Torrent();
                torrents[2].setUrl(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_3D)));
                torrents[2].setHash(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_HASH3D)));
                torrents[2].setSize(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_3D_SIZE)));

                movie.setGenre(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_GENRE)));
                movie.setAvailableInQuality(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_QUALITY)));
                movie.setTorrents(Arrays.asList(torrents));
                movies.add(movie);
            }
        }
        return movies;
    }

    int getCount() {
        Cursor mCount = database.rawQuery("select count(*) from movie", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();
        return count;
    }

    long createMovie(Movie movie) {
        long resultId = -1;
        ContentValues values = new ContentValues();
        values.put(MovieSQLiteHelper.KEY_ID, movie.getId());
        values.put(MovieSQLiteHelper.KEY_TITLE, movie.getTitle());
        values.put(MovieSQLiteHelper.KEY_DESCRIPTION, movie.getDescriptionFull());
        values.put(MovieSQLiteHelper.KEY_YEAR, movie.getYear());
        values.put(MovieSQLiteHelper.KEY_BACKGROUND_IMAGE_URL, movie.getMediumCoverImage());
        values.put(MovieSQLiteHelper.KEY_RATING, movie.getRating());
        values.put(MovieSQLiteHelper.KEY_YOUTUBE_TRAILER, movie.getYtTrailerCode());
        if (movie.getGenres() != null)
            values.put(MovieSQLiteHelper.KEY_GENRE, TextUtils.join(", ", movie.getGenres()));
        if (movie.getTorrents() != null)
            for (Torrent t : movie.getTorrents()) {
                if (t.getQuality().equals("720p")) {
                    values.put(MovieSQLiteHelper.KEY_720P, t.getUrl());
                    values.put(MovieSQLiteHelper.KEY_HASH720P, t.getHash());
                    values.put(MovieSQLiteHelper.KEY_720P_SIZE, t.getSize());
                }
                if (t.getQuality().equals("1080p")) {
                    values.put(MovieSQLiteHelper.KEY_1080P, t.getUrl());
                    values.put(MovieSQLiteHelper.KEY_HASH1080P, t.getHash());
                    values.put(MovieSQLiteHelper.KEY_1080P_SIZE, t.getSize());
                }
                if (t.getQuality().equals("3D")) {
                    values.put(MovieSQLiteHelper.KEY_3D, t.getUrl());
                    values.put(MovieSQLiteHelper.KEY_HASH3D, t.getHash());
                    values.put(MovieSQLiteHelper.KEY_3D_SIZE, t.getSize());
                }
                values.put(MovieSQLiteHelper.KEY_QUALITY, movie.getFromListAvailableInQuality());
            }
        resultId = database.insertWithOnConflict(MovieSQLiteHelper.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        return resultId;
    }
}
