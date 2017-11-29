package example.com.moviesfragment;

import android.util.Log;

/**
 * Created by jusuf on 19.8.2017.
 */

public class SqlStatements {

/*    public String generateOrderSql(String s) {
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
        String sql = null;
        if (s.equalsIgnoreCase("All")) {
            sql = MovieSQLiteHelper.KEY_ID + " IS NOT NULL";
        } else if (s.equalsIgnoreCase("720p")) {
            sql = MovieSQLiteHelper.KEY_720P + " IS NOT NULL";
        } else if (s.equalsIgnoreCase("1080p")) {
            sql = MovieSQLiteHelper.KEY_1080P + " IS NOT NULL";
        } else if (s.equalsIgnoreCase("3d")) {
            sql = MovieSQLiteHelper.KEY_3D + " IS NOT NULL";
        }
//        Log.v("SQL ", sql);
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
    }*/
}
