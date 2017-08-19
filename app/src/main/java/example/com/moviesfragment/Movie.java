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
    private String trailerCode;
    private String genre;
    private String quality;
    private String url720p;
    private String url1080p;
    private String url3d;


    public Movie(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        year = in.readInt();
        imageUrl = in.readString();
        rating = in.readFloat();
        trailerCode = in.readString();
        genre = in.readString();
        quality = in.readString();
        url720p = in.readString();
        url1080p = in.readString();
        url3d = in.readString();
    }

    public Movie() {

    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getUrl720p() {
        return url720p;
    }

    public void setUrl720p(String url720p) {
        this.url720p = url720p;
    }

    public String getUrl1080p() {
        return url1080p;
    }

    public void setUrl1080p(String url1080p) {
        this.url1080p = url1080p;
    }

    public String getUrl3d() {
        return url3d;
    }

    public void setUrl3d(String url3d) {
        this.url3d = url3d;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTrailerCode() {
        return trailerCode;
    }

    public void setTrailerCode(String trailerCode) {
        this.trailerCode = trailerCode;
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
        dest.writeString(trailerCode);
        dest.writeString(genre);
        dest.writeString(quality);
        dest.writeString(url720p);
        dest.writeString(url1080p);
        dest.writeString(url3d);
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
