package example.com.moviesfragment;

import android.util.Log;

/**
 * Created by jusuf on 19.8.2017.
 */

public class SqlStatements {

    public String generateOrderSql(String s) {
        String sql;
        switch (s) {
            case "Alphabetical":
                sql = MovieSQLiteHelper.KEY_NAME + " ASC";
                break;
            case "Year":
                sql = MovieSQLiteHelper.KEY_YEAR + " DESC";
                break;
            case "Rating":
                sql = MovieSQLiteHelper.KEY_RATING + " DESC";
                break;
            case "Latest":
                sql = MovieSQLiteHelper.KEY_ID + " DESC";
                break;
            default:
                sql = MovieSQLiteHelper.KEY_ID + " DESC";
        }
        Log.v("SQL ", sql);
        return sql;
    }

    public String generateRatingSql(String s) {
        String sql;
        if (s.equalsIgnoreCase("All")) {
            sql = MovieSQLiteHelper.KEY_RATING + " IS NOT NULL";
        } else {
            sql = MovieSQLiteHelper.KEY_RATING + " >= " + s;
        }
        Log.v("SQL ", sql);
        return sql;
    }

    public String generateGenreSql(String s) {
        String sql;
        if (s.equalsIgnoreCase("All")) {
            sql = MovieSQLiteHelper.KEY_GENRE + " IS NOT NULL";
        } else {
            sql = MovieSQLiteHelper.KEY_GENRE + " LIKE " + " '%" + s + "%' ";
        }
        Log.v("SQL ", sql);
        return sql;

    }

    public String generateQualitySql(String s) {
        String sql;
        if (s.equalsIgnoreCase("All")) {
            sql = MovieSQLiteHelper.KEY_QUALITY + " IS NOT NULL";
        } else {
            sql = MovieSQLiteHelper.KEY_QUALITY + " >= " + s;
        }
        Log.v("SQL ", sql);
        return sql;
    }

    public String generateNameSql(String s) {
        String sql;
        if (!s.isEmpty()) {
            sql = MovieSQLiteHelper.KEY_NAME + " LIKE '% " + s + "%' ";
        } else {
            sql = MovieSQLiteHelper.KEY_NAME + " IS NOT NULL";
        }
        Log.v("SQL ", sql);
        return sql;
    }
}
