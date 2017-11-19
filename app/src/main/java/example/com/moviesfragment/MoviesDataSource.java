package example.com.moviesfragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

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

   /* List<Movie> getAllMovies() {
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
                List<Torrent> torrents = new ArrayList<Torrent>(3);


                movie.setId(cursor.getInt(cursor.getColumnIndex(MovieSQLiteHelper.KEY_ID)));
                movie.setTitle(cursor.getString(1));
                movie.setSummary(cursor.getString(2));
                movie.setYear(cursor.getInt(3));
                movie.setMediumCoverImage(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_IMAGE_URL)));
                movie.setRating(cursor.getDouble(cursor.getColumnIndex(MovieSQLiteHelper.KEY_RATING)));
                movie.setYtTrailerCode(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_TRAILER)));

                Torrent t = new Torrent();
                t.setUrl(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_720P)));
                t.setHash(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_HASH720P)));
                torrents.add(t);

                Torrent t1 = new Torrent();
                t1.setUrl(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_1080P)));
                t1.setHash(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_HASH1080P)));
                torrents.add(t1);

                Torrent t2 = new Torrent();
                t2.setUrl(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_3D)));
                t2.setHash(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_HASH3D)));
                torrents.add(t2);

                movie.setGenre(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_GENRE)));
                movie.setAvailableInQuality(cursor.getString(cursor.getColumnIndex(MovieSQLiteHelper.KEY_QUALITY)));
                movie.setTorrents(torrents);
                movies.add(movie);
            }
        }
        return movies;
    }*/

    int getCount() {
        Cursor mCount = database.rawQuery("select count(*) from movie", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();
        return count;
    }


    public long createMovieInfo(Movie movie, long[] movieInfo_ids) {
        ContentValues values = new ContentValues();
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_ID, movie.getId());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_NAME, movie.getTitle());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_TITLE_LONG, movie.getTitleLong());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_DESCRIPTION, movie.getDescriptionFull());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_YEAR, movie.getYear());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_RATING, movie.getRating());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_TRAILER, movie.getYtTrailerCode());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_GENRE, movie.getGenres().toString());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_RUNTIME, movie.getRuntime());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_MPARATING, movie.getMpaRating());
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_IMDB, movie.getImdbCode());

        long resultId = database.insertOrThrow(MovieSQLiteHelper.TABLE_MOVIE_INFO, null, values);

        // assigning tags to todo
        for (long movieInfoId : movieInfo_ids) {
            createMovieTorrent(resultId, movieInfoId);
        }
        return resultId;
    }


    public long createMovieTorrent(long movieId, long torrentId) {

        ContentValues values = new ContentValues();
        values.put(MovieSQLiteHelper.TORRENT_KEY_ID, torrentId);
        values.put(MovieSQLiteHelper.MOVIE_INFO_KEY_ID, movieId);
        long id = database.insert(MovieSQLiteHelper.TABLE_MOVIES, null, values);
        return id;
    }

    public long createTorrent(Torrent torrent) {
        ContentValues values = new ContentValues();
        values.put(MovieSQLiteHelper.TORRENT_KEY_ID, torrent.getId());
        values.put(MovieSQLiteHelper.TORRENT_KEY_QUALITY, torrent.getQuality());
        values.put(MovieSQLiteHelper.TORRENT_KEY_HASH, torrent.getHash());
        values.put(MovieSQLiteHelper.TORRENT_KEY_SIZE, torrent.getSize());
        values.put(MovieSQLiteHelper.TORRENT_KEY_URL, torrent.getUrl());

        try {
            long torrentId = database.insertOrThrow(MovieSQLiteHelper.TABLE_TORRENTS, null, values);
            return torrentId;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

  /*  long createMovie(long id, String name, String description, int year, String imageUrl, double rating, String trailerCode, String genre, HashMap<String, String> torrents, HashMap<String, String> hashValues) {
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
    }*/
}
