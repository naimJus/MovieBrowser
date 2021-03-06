package capitalria.mk.moviesfragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import capitalria.mk.moviesfragment.gson.Torrent;
import capitalria.mk.moviesfragment.gson.Movie;

public class MoviesDataSource {
    private static final String LOG = MoviesDataSource.class.getSimpleName();
    private static final String MY_QUERY = "SELECT * FROM " + MovieSQLiteHelper.TABLE_MOVIE_INFO + " m INNER JOIN " + MovieSQLiteHelper.TABLE_TORRENTS
            + " t ON m.movie_info_id = t.movie_info_id GROUP BY m.title";
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


    List<Movie> limitMovies(int limit) {
        if (limit < 50)
            limit = 50;

        Cursor cursor = database.rawQuery(MY_QUERY + " ORDER BY " + MovieSQLiteHelper.MOVIE_INFO_KEY_ID + " DESC LIMIT " + limit, null);
        List<Movie> movies = cursorToList(cursor);
        return movies;
    }


    List<Movie> getMovie(String id) {
        String[] ids = new String[]{id};
        Cursor cursor = database.rawQuery("SELECT * FROM " + MovieSQLiteHelper.TABLE_MOVIE_INFO + " m INNER JOIN " + MovieSQLiteHelper.TABLE_TORRENTS
                + " t ON m." + MovieSQLiteHelper.MOVIE_INFO_KEY_ID + " = t." + MovieSQLiteHelper.MOVIE_INFO_KEY_ID + " WHERE m." + MovieSQLiteHelper.MOVIE_INFO_KEY_ID + " =?", ids);

        List<Movie> movies = cursorToList(cursor);
        return movies;
    }

    List<Movie> sortAndLimit(String orderBy, int limit) {
        Cursor cursor = database.rawQuery(MY_QUERY + " ORDER BY " + orderBy + " LIMIT " + limit, null);
        List<Movie> movies = cursorToList(cursor);
        return movies;
    }


    List<Movie> searchMovies(HashMap<String, String> searchParams) {

        String quality = searchParams.get("Quality");
        String genre = searchParams.get("Genre");
        String rating = searchParams.get("Rating");
        String search = searchParams.get("Search");
        String order = searchParams.get("Order");


        String[] selection = {
                quality,
                genre,
                rating,
                search};

        String query = "SELECT * FROM " + MovieSQLiteHelper.TABLE_MOVIE_INFO + " m INNER JOIN " + MovieSQLiteHelper.TABLE_TORRENTS
                + " t ON m.movie_info_id = t.movie_info_id WHERE "
                + MovieSQLiteHelper.TORRENT_KEY_QUALITY + selection[0]
                + " AND " + MovieSQLiteHelper.MOVIE_INFO_KEY_GENRE + selection[1]
                + " AND " + MovieSQLiteHelper.MOVIE_INFO_KEY_RATING + selection[2]
                + " AND " + MovieSQLiteHelper.MOVIE_INFO_KEY_TITLE + selection[3]
                + " GROUP BY m." + MovieSQLiteHelper.MOVIE_INFO_KEY_TITLE
                + " ORDER BY " + order;
        Cursor cursor = database.rawQuery(query, null);
        List<Movie> movies = cursorToList(cursor);
        return movies;
    }

    private List<Movie> cursorToList(Cursor cursor) {
        List<Movie> movies = new ArrayList<>(cursor.getCount());
        Movie movie;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                movie = new Movie();

                movie.setId(cursor.getInt(cursor.getColumnIndex(MovieSQLiteHelper.MOVIE_INFO_KEY_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.MOVIE_INFO_KEY_TITLE)));
                movie.setTitleLong(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.MOVIE_INFO_KEY_TITLE_LONG)));
                movie.setDescriptionFull(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.MOVIE_INFO_KEY_DESCRIPTION)));
                movie.setYear(cursor.getInt(cursor.getColumnIndex(MovieSQLiteHelper.MOVIE_INFO_KEY_YEAR)));
                movie.setRating(cursor.getDouble(cursor.getColumnIndex(MovieSQLiteHelper.MOVIE_INFO_KEY_RATING)));
                movie.setYtTrailerCode(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.MOVIE_INFO_KEY_TRAILER)));
                movie.setGenre(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.MOVIE_INFO_KEY_GENRE)));
                movie.setRuntime(cursor.getInt(cursor.getColumnIndex(MovieSQLiteHelper.MOVIE_INFO_KEY_RUNTIME)));
                movie.setMpaRating(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.MOVIE_INFO_KEY_MPARATING)));
                movie.setImdbCode(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.MOVIE_INFO_KEY_IMDB)));
                movie.setMediumCoverImage(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.MOVIE_INFO_IMAGE_MEDIUM_COVER)));
                movie.setLargeCoverImage(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.MOVIE_INFO_IMAGE_LARGE_COVER)));

                Torrent torrent = new Torrent();
                torrent.setQuality(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.TORRENT_KEY_QUALITY)));
                torrent.setHash(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.TORRENT_KEY_HASH)));
                torrent.setUrl(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.TORRENT_KEY_URL)));
                torrent.setSize(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.TORRENT_KEY_SIZE)));

                movie.setTorrent(torrent);
                movies.add(movie);
            }
        }
        return movies;
    }

    public void createMovieInfo(Movie movie) {
        long newId;
        ContentValues values = new ContentValues();
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_ID, movie.getId());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_TITLE, movie.getTitle());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_TITLE_LONG, movie.getTitleLong());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_DESCRIPTION, movie.getDescriptionFull());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_YEAR, movie.getYear());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_RATING, movie.getRating());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_TRAILER, movie.getYtTrailerCode());
        if (movie.getGenres() != null)
            values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_GENRE, TextUtils.join(", ", movie.getGenres()));
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_RUNTIME, movie.getRuntime());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_MPARATING, movie.getMpaRating());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_IMDB, movie.getImdbCode());
        values.put(MovieSQLiteHelper.MOVIE_INFO_IMAGE_MEDIUM_COVER, movie.getMediumCoverImage());
        values.put(MovieSQLiteHelper.MOVIE_INFO_IMAGE_LARGE_COVER, movie.getLargeCoverImage());

        if (movie.getTorrents() != null)
            createTorrent(movie);
        try {
            newId = database.insertOrThrow(MovieSQLiteHelper.TABLE_MOVIE_INFO, null, values);
            Log.v(LOG, newId + " was added in database");
        } catch (SQLException es) {
            Log.v(LOG, "movie with id " + movie.getId() + " was already in database");
        }
    }

    private void createTorrent(Movie movie) {
        ContentValues values;
        for (Torrent torrent : movie.getTorrents()) {
            values = new ContentValues();
            values.put(MovieSQLiteHelper.TORRENT_KEY_ID, movie.getId() + torrent.getHash());
            values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_ID, movie.getId());
            values.put(MovieSQLiteHelper.TORRENT_KEY_QUALITY, torrent.getQuality());
            values.put(MovieSQLiteHelper.TORRENT_KEY_HASH, torrent.getHash());
            values.put(MovieSQLiteHelper.TORRENT_KEY_SIZE, torrent.getSize());
            values.put(MovieSQLiteHelper.TORRENT_KEY_URL, torrent.getUrl());
            database.insertWithOnConflict(MovieSQLiteHelper.TABLE_TORRENTS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }


    int getCount() {
        Cursor cursor = database.rawQuery("SELECT * FROM " + MovieSQLiteHelper.TABLE_MOVIE_INFO, null);
        int count = cursor.getCount();
        cursor.close();
        return count;

    }
}
