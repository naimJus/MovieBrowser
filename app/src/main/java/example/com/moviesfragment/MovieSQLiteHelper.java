package example.com.moviesfragment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MovieSQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "movie";
    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_YEAR = "year";
    public static final String KEY_BACKGROUND_IMAGE_URL = "imageUrl";
    public static final String KEY_RATING = "rating";
    public static final String KEY_YOUTUBE_TRAILER = "trailer";
    public static final String KEY_GENRE = "genre";
    public static final String KEY_720P = "url720p";
    public static final String KEY_1080P = "url1080p";
    public static final String KEY_3D = "url3d";
    public static final String KEY_HASH720P = "hash720p";
    public static final String KEY_HASH1080P = "hash1080p";
    public static final String KEY_HASH3D = "hash3d";
    public static final String KEY_QUALITY = "quality";
    public static final String KEY_720P_SIZE = "size720p";
    public static final String KEY_1080P_SIZE = "size1080p";
    public static final String KEY_3D_SIZE = "size3d";
    private static MovieSQLiteHelper sInstance;
    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_TITLE + " TEXT," +
            KEY_DESCRIPTION + " TEXT," +
            KEY_YEAR + " INTEGER," +
            KEY_BACKGROUND_IMAGE_URL + " TEXT," +
            KEY_RATING + " REAL," +
            KEY_YOUTUBE_TRAILER + " TEXT," +
            KEY_GENRE + " TEXT," +
            KEY_720P + " TEXT," +
            KEY_1080P + " TEXT," +
            KEY_3D + " TEXT," +
            KEY_HASH720P + " TEXT," +
            KEY_HASH1080P + " TEXT," +
            KEY_HASH3D + " TEXT," +
            KEY_720P_SIZE + " TEXT," +
            KEY_1080P_SIZE + " TEXT," +
            KEY_3D_SIZE + " TEXT," +
            KEY_QUALITY + " TEXT" + ")";

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
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE " + TABLE_NAME);
        }
    }
}
