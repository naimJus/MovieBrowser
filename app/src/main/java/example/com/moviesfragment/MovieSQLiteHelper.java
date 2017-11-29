package example.com.moviesfragment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MovieSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    static final String TABLE_MOVIE_INFO = "movieInfo";
    static final String TABLE_TORRENTS = "torrent";
    // table movie info fields
    static final String MOVIE_INFO_KEY_ID = "movie_info_id";
    static final String MOVIE_INFO_KEY_TITLE = "name";
    static final String MOVIE_INFO_KEY_TITLE_LONG = "title_long";
    static final String MOVIE_INFO_KEY_DESCRIPTION = "description";
    static final String MOVIE_INFO_KEY_YEAR = "year";
    static final String MOVIE_INFO_KEY_RATING = "rating";
    static final String MOVIE_INFO_KEY_TRAILER = "trailer";
    static final String MOVIE_INFO_KEY_GENRE = "genre";
    static final String MOVIE_INFO_KEY_RUNTIME = "runtime";
    static final String MOVIE_INFO_KEY_MPARATING = "mpa_rating";
    static final String MOVIE_INFO_KEY_IMDB = "imdb_code";
    static final String MOVIE_INFO_IMAGE_BACKGROUND = "background_image";
    static final String MOVIE_INFO_IMAGE_BACKGROUND_ORIGINAL = "background_image_original";
    static final String MOVIE_INFO_IMAGE_SMALL_COVER = "small_cover_image";
    static final String MOVIE_INFO_IMAGE_MEDIUM_COVER = "medium_cover_image";
    static final String MOVIE_INFO_IMAGE_LARGE_COVER = "large_cover_image";
    //    table Torrent fields
    static final String TORRENT_KEY_ID = "torrent_id";
    static final String TORRENT_KEY_QUALITY = "quality";
    static final String TORRENT_KEY_HASH = "hash";
    static final String TORRENT_KEY_URL = "url";
    static final String TORRENT_KEY_SIZE = "size";

    //  SQL command to create tables
    private static final String CREATE_TABLE_TORRENT = "CREATE TABLE " + TABLE_TORRENTS + " (" +
            TORRENT_KEY_ID + " TEXT PRIMARY KEY," +
            MOVIE_INFO_KEY_ID + " INTEGER," +
            TORRENT_KEY_QUALITY + " TEXT," +
            TORRENT_KEY_HASH + " TEXT," +
            TORRENT_KEY_URL + " TEXT," +
            TORRENT_KEY_SIZE + " TEXT" + ")";

    private static final String CREATE_TABLE_INFO = "CREATE TABLE " + TABLE_MOVIE_INFO + "(" +
            MOVIE_INFO_KEY_ID + " INTEGER PRIMARY KEY," +
            MOVIE_INFO_KEY_TITLE + " TEXT," +
            MOVIE_INFO_KEY_TITLE_LONG + " TEXT," +
            MOVIE_INFO_KEY_DESCRIPTION + " TEXT," +
            MOVIE_INFO_KEY_YEAR + " INTEGER," +
            MOVIE_INFO_KEY_RATING + " REAL," +
            MOVIE_INFO_KEY_TRAILER + " TEXT," +
            MOVIE_INFO_KEY_GENRE + " TEXT," +
            MOVIE_INFO_KEY_RUNTIME + " INTEGER," +
            MOVIE_INFO_KEY_MPARATING + " TEXT," +
            MOVIE_INFO_KEY_IMDB + " TEXT," +
            MOVIE_INFO_IMAGE_BACKGROUND + " TEXT," +
            MOVIE_INFO_IMAGE_BACKGROUND_ORIGINAL + " TEXT," +
            MOVIE_INFO_IMAGE_SMALL_COVER + " TEXT," +
            MOVIE_INFO_IMAGE_MEDIUM_COVER + " TEXT," +
            MOVIE_INFO_IMAGE_LARGE_COVER + " TEXT" + ")";

    private static MovieSQLiteHelper sInstance;


    private MovieSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized MovieSQLiteHelper getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MovieSQLiteHelper(context);
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_INFO);
        db.execSQL(CREATE_TABLE_TORRENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TORRENTS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE " + TABLE_MOVIE_INFO);
            db.execSQL("DROP TABLE " + TABLE_TORRENTS);
        }
    }
}
