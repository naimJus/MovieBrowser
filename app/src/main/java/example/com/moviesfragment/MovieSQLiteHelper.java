package example.com.moviesfragment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jusuf on 04.7.2017.
 */

public class MovieSQLiteHelper extends SQLiteOpenHelper {

    private static MovieSQLiteHelper sInstance;

    public static final String DATABASE_NAME = "movie.db";
    public static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "movie";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_YEAR = "year";
    public static final String KEY_IMAGE_URL = "imageUrl";
    public static final String KEY_RATING = "rating";
    public static final String KEY_TRAILER = "trailer";


    private MovieSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized MovieSQLiteHelper getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MovieSQLiteHelper(context);
        }
        return sInstance;
    }

    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_NAME + " TEXT," +
            KEY_DESCRIPTION + " TEXT," +
            KEY_YEAR + " INTEGER," +
            KEY_IMAGE_URL + " TEXT," +
            KEY_RATING + " REAL," +
            KEY_TRAILER + " TEXT" + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("DROP TABLE " + TABLE_NAME);
        db.execSQL(CREATE_TABLE);
        Log.v("SQL", "database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.v("SQL", "database created");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE " + TABLE_NAME);
        }
    }
}
