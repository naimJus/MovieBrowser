package example.com.moviesfragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        Cursor cursor = database.rawQuery("SELECT * " +
                "FROM " + MovieSQLiteHelper.TABLE_MOVIE_INFO + " m "
                + "INNER JOIN " + MovieSQLiteHelper.TABLE_TORRENTS + " t ON m.movie_info_id = t.movie_info_id ORDER BY " + MovieSQLiteHelper.MOVIE_INFO_KEY_ID + " ASC ", null);
        List<Movie> movies = cursorToList(cursor);
        return movies;
    }

    /*
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
*/
    private List<Movie> cursorToList(Cursor cursor) {
        List<Movie> movies = new ArrayList<Movie>();
        Set<Movie> movieSet = new HashSet<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Movie movie = new Movie();

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
                movie.setBackgroundImage(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.MOVIE_INFO_IMAGE_BACKGROUND)));
                movie.setBackgroundImageOriginal(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.MOVIE_INFO_IMAGE_BACKGROUND_ORIGINAL)));
                movie.setSmallCoverImage(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.MOVIE_INFO_IMAGE_SMALL_COVER)));
                movie.setMediumCoverImage(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.MOVIE_INFO_IMAGE_MEDIUM_COVER)));
                movie.setLargeCoverImage(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.MOVIE_INFO_IMAGE_LARGE_COVER)));

                Torrent torrent = new Torrent();
                torrent.setQuality(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.TORRENT_KEY_QUALITY)));
                torrent.setHash(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.TORRENT_KEY_HASH)));
                torrent.setUrl(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.TORRENT_KEY_URL)));
                torrent.setSize(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.TORRENT_KEY_SIZE)));

//                movie.setTorrents(torrent);
                movieSet.add(movie);
            }
        }
        movies.addAll(movieSet);
        return movies;
    }

    public long createMovieInfo(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_ID, movie.getId());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_TITLE, movie.getTitle());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_TITLE_LONG, movie.getTitleLong());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_DESCRIPTION, movie.getDescriptionFull());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_YEAR, movie.getYear());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_RATING, movie.getRating());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_TRAILER, movie.getYtTrailerCode());
        if (movie.getGenres() != null)
            values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_GENRE, movie.getGenres().toString());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_RUNTIME, movie.getRuntime());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_MPARATING, movie.getMpaRating());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_IMDB, movie.getImdbCode());
        values.put(MovieSQLiteHelper.MOVIE_INFO_IMAGE_BACKGROUND, movie.getBackgroundImage());
        values.put(MovieSQLiteHelper.MOVIE_INFO_IMAGE_BACKGROUND_ORIGINAL, movie.getBackgroundImageOriginal());
        values.put(MovieSQLiteHelper.MOVIE_INFO_IMAGE_SMALL_COVER, movie.getSmallCoverImage());
        values.put(MovieSQLiteHelper.MOVIE_INFO_IMAGE_MEDIUM_COVER, movie.getMediumCoverImage());
        values.put(MovieSQLiteHelper.MOVIE_INFO_IMAGE_LARGE_COVER, movie.getLargeCoverImage());

        long resultId = database.insertWithOnConflict(MovieSQLiteHelper.TABLE_MOVIE_INFO, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        return resultId;
    }

    public long createTorrent(Movie movie) {
        ContentValues values;
        long torrentId = -1;
        for (Torrent torrent : movie.getTorrents()) {
            values = new ContentValues();
            values.put(MovieSQLiteHelper.TORRENT_KEY_ID, movie.getId() + torrent.getHash());
            values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_ID, movie.getId());
            values.put(MovieSQLiteHelper.TORRENT_KEY_QUALITY, torrent.getQuality());
            values.put(MovieSQLiteHelper.TORRENT_KEY_HASH, torrent.getHash());
            values.put(MovieSQLiteHelper.TORRENT_KEY_SIZE, torrent.getSize());
            values.put(MovieSQLiteHelper.TORRENT_KEY_URL, torrent.getUrl());
            torrentId = database.insertWithOnConflict(MovieSQLiteHelper.TABLE_TORRENTS, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        }
        return torrentId;
    }
/*

    int getCount() {
        Cursor mCount = database.rawQuery("select count(*) from movie", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();
        return count;
    }



  long createMovie(long id, String name, String description, int year, String imageUrl, double rating, String trailerCode, String genre, HashMap<String, String> torrents, HashMap<String, String> hashValues) {
        long resultId = -1;
        StringBuilder quality = new StringBuilder();
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
        values.put(MovieSQLiteHelper.KEY_HASH720P, hashValues.get("720p"));
        values.put(MovieSQLiteHelper.KEY_HASH1080P, hashValues.get("1080p"));
        values.put(MovieSQLiteHelper.KEY_HASH3D, hashValues.get("3D"));
        if (torrents.get("720p") != null)
            quality.append("720p ");
        if (torrents.get("1080p") != null)
            quality.append("1080p ");
        if (torrents.get("3D") != null)
            quality.append("3D");
        values.put(MovieSQLiteHelper.KEY_QUALITY, quality.toString());
        resultId = database.insertWithOnConflict(MovieSQLiteHelper.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        return resultId;
    }
*/
}
