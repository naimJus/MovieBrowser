package capitalria.mk.moviesfragment;

import android.util.Log;

/**
 * Created by jusuf on 19.8.2017.
 */

public class SqlStatements {

    public String generateOrderSql(String s) {
        String sql;
        switch (s) {
            case "Alphabetical":
                sql = MovieSQLiteHelper.MOVIE_INFO_KEY_TITLE + " DESC";
                break;
            case "Year":
                sql = MovieSQLiteHelper.MOVIE_INFO_KEY_YEAR + " DESC";
                break;
            case "Rating":
                sql = MovieSQLiteHelper.MOVIE_INFO_KEY_RATING + " DESC";
                break;
            case "Latest":
                sql = MovieSQLiteHelper.MOVIE_INFO_KEY_ID + " DESC";
                break;
            default:
                sql = MovieSQLiteHelper.MOVIE_INFO_KEY_ID + " DESC";
        }
        return sql;
    }

    public String generateRatingSql(String s) {
        String sql;
        if (s.equalsIgnoreCase("All")) {
            sql = " IS NOT NULL";
        } else {
            sql = " >= " + s;
        }
        Log.v("SQL ", sql);
        return sql;
    }

    public String generateGenreSql(String s) {
        String sql;
        if (s.equalsIgnoreCase("All")) {
            sql = " IS NOT NULL";
        } else {
            sql = " LIKE " + " '%" + s + "%' ";
        }
        return sql;

    }

    public String generateQualitySql(String s) {
        String sql = null;
        if (s.equalsIgnoreCase("All")) {
            sql = " IS NOT NULL";
        } else if (s.equalsIgnoreCase("720p")) {
            sql = " LIKE '720p'";
        } else if (s.equalsIgnoreCase("1080p")) {
            sql = " LIKE '1080p'";
        } else if (s.equalsIgnoreCase("3d")) {
            sql = " LIKE '3d'";
        }
        return sql;
    }

    public String generateNameSql(String s) {
        String sql;
        if (!s.isEmpty()) {
            sql = " LIKE '%" + s + "%'";
        } else {
            sql = " IS NOT NULL";
        }
        return sql;
    }
}
