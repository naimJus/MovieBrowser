package example.com.moviesfragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.stetho.Stetho;

public class MainActivity extends Activity {
    Button browseBtn, recentBtn, topBtn;
    public static final String FILTER = "TOPRATED";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.newInitializerBuilder(this);

        browseBtn = (Button) findViewById(R.id.browseBtn);
        recentBtn = (Button) findViewById(R.id.recentBtn);
        topBtn = (Button) findViewById(R.id.topBtn);


        browseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        recentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra(FILTER, MovieSQLiteHelper.KEY_ID + " DESC");
                startActivity(intent);
            }
        });
        topBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetMoviesTwo(getApplicationContext(), MainActivity.this).execute("&sort_by=rating");
            }
        });


    }
}
class GetMoviesTwo extends GetMovies {
    private Context context;
    public static final String LOGTAG = "AsyncTask";
    private Activity mActivity;

    public GetMoviesTwo(Context context, Activity mActivity) {
        super(context, mActivity);
        this.context = context;
        this.mActivity = mActivity;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(MainActivity.FILTER, MovieSQLiteHelper.KEY_RATING + " DESC");
        mActivity.startActivity(intent);
    }
}