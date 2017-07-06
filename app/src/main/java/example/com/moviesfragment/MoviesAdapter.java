package example.com.moviesfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jusuf on 05.7.2017.
 */

public class MoviesAdapter extends ArrayAdapter<Movie> {
    public MoviesAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Movie movie = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.list_item_name);
        TextView tvYear = (TextView) convertView.findViewById(R.id.list_item_year);
        TextView tvRating = (TextView) convertView.findViewById(R.id.list_item_rating);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.list_item_image);

        // Populate the data into the template view using the data object
        tvName.setText(movie.getName());
        tvYear.setText(String.valueOf(movie.getYear()));
        tvRating.setText(String.valueOf(movie.getRating()));
        Picasso.with(getContext())
                .load(movie.getImageUrl())
                .placeholder(R.drawable.welcome)
                .fit()
                .into(imageView);
        // Return the completed view to render on screen
        return convertView;
    }
}
