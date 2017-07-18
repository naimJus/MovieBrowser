package example.com.moviesfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.stetho.Stetho;

public class MainActivity extends Activity {
    Button browseBtn, recentBtn, topBtn;

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
                startActivity(intent);
            }
        });
        topBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetMovies(getApplicationContext(), MainActivity.this).execute("&sort_by=rating");
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("TOPRATED", MovieSQLiteHelper.KEY_RATING + " DESC");
                startActivity(intent);
            }
        });


    }
}
