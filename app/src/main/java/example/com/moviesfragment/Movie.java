package example.com.moviesfragment;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jusuf on 04.7.2017.
 */

public class Movie implements Parcelable {

    private long id;
    private String name, description;
    private int year;
    private String imageUrl;
    private float rating;

    public Movie(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        year = in.readInt();
        imageUrl = in.readString();
        rating = in.readFloat();
    }

    public Movie() {

    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getYear() {
        return year;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public float getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return this.getId() + this.getName() + this.getDescription() + this.getYear() + this.getImageUrl() + this.getRating();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(year);
        dest.writeString(imageUrl);
        dest.writeFloat(rating);
    }

    public static final Parcelable.Creator<Movie> CREATOR =
            new Parcelable.Creator<Movie>() {

                @Override
                public Movie createFromParcel(Parcel source) {
                    return new Movie(source);
                }

                @Override
                public Movie[] newArray(int size) {
                    return new Movie[size];
                }

            };


}
