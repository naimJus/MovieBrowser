package example.com.moviesfragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import example.com.moviesfragment.gson.Movie;

/**
 * Created by jusuf on 05.7.2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private List<Movie> mDataset;
    private Context context;

    public MoviesAdapter(Context context, List<Movie> mDataset) {
        this.context = context;
        this.mDataset = mDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = mDataset.get(position);
        holder.tvName.setText(movie.getTitle());
        holder.tvYear.setText(movie.getYear().toString());
        holder.tvRating.setText(movie.getRating().toString());
        holder.tvGenres.setText(movie.getGenre());
        Picasso.with(context)
                .load(movie.getMediumCoverImage())
                .placeholder(R.drawable.welcome)
                .fit()
                .into(holder.imageView);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView tvName;
        TextView tvYear;
        TextView tvRating;
        TextView tvGenres;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.list_item_name);
            tvYear = (TextView) itemView.findViewById(R.id.list_item_year);
            tvRating = (TextView) itemView.findViewById(R.id.list_item_rating);
            imageView = (ImageView) itemView.findViewById(R.id.list_item_image);
            tvGenres = (TextView) itemView.findViewById(R.id.list_item_genres);
        }
    }


}
