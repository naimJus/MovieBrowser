package capitalria.mk.moviesfragment.gson;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jusuf on 28.8.2017.
 */
public class Movie implements Parcelable {

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("imdb_code")
    @Expose
    private String imdbCode;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("title_long")
    @Expose
    private String titleLong;
    @SerializedName("year")
    @Expose
    private Integer year;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("runtime")
    @Expose
    private Integer runtime;
    @SerializedName("genres")
    @Expose
    private List<String> genres = null;
    private String genre;
    @SerializedName("description_full")
    @Expose
    private String descriptionFull;
    @SerializedName("yt_trailer_code")
    @Expose
    private String ytTrailerCode;
    @SerializedName("mpa_rating")
    @Expose
    private String mpaRating;
    @SerializedName("medium_cover_image")
    @Expose
    private String mediumCoverImage;
    @SerializedName("large_cover_image")
    @Expose
    private String largeCoverImage;
    @SerializedName("torrents")
    @Expose
    private List<Torrent> torrents = null;
    private Torrent torrent;

    public Movie() {
    }

    public Movie(Parcel in) {
        url = in.readString();
        imdbCode = in.readString();
        title = in.readString();
        titleLong = in.readString();
        year = in.readInt();
        rating = in.readDouble();
        genres = in.createStringArrayList();
        genre = in.readString();
        descriptionFull = in.readString();
        ytTrailerCode = in.readString();
        mpaRating = in.readString();
        mediumCoverImage = in.readString();
        largeCoverImage = in.readString();
        torrents = in.createTypedArrayList(Torrent.CREATOR);
        torrent = in.readParcelable(Torrent.class.getClassLoader());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImdbCode() {
        return imdbCode;
    }

    public void setImdbCode(String imdbCode) {
        this.imdbCode = imdbCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleLong() {
        return titleLong;
    }

    public void setTitleLong(String titleLong) {
        this.titleLong = titleLong;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescriptionFull() {
        return descriptionFull;
    }

    public void setDescriptionFull(String descriptionFull) {
        this.descriptionFull = descriptionFull;
    }

    public String getYtTrailerCode() {
        return ytTrailerCode;
    }

    public void setYtTrailerCode(String ytTrailerCode) {
        this.ytTrailerCode = ytTrailerCode;
    }


    public String getMpaRating() {
        return mpaRating;
    }

    public void setMpaRating(String mpaRating) {
        this.mpaRating = mpaRating;
    }

    public String getMediumCoverImage() {
        return mediumCoverImage;
    }

    public void setMediumCoverImage(String mediumCoverImage) {
        this.mediumCoverImage = mediumCoverImage;
    }

    public String getLargeCoverImage() {
        return largeCoverImage;
    }

    public void setLargeCoverImage(String largeCoverImage) {
        this.largeCoverImage = largeCoverImage;
    }

    public List<Torrent> getTorrents() {
        return torrents;
    }

    public void setTorrents(List<Torrent> torrents) {
        this.torrents = torrents;
    }

    public Torrent getTorrent() {
        return torrent;
    }

    public void setTorrent(Torrent torrent) {
        this.torrent = torrent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(imdbCode);
        dest.writeString(title);
        dest.writeString(titleLong);
        dest.writeInt(year);
        dest.writeDouble(rating);
        dest.writeStringList(genres);
        dest.writeString(genre);
        dest.writeString(descriptionFull);
        dest.writeString(ytTrailerCode);
        dest.writeString(mpaRating);
        dest.writeString(mediumCoverImage);
        dest.writeString(largeCoverImage);
        dest.writeTypedList(torrents);
        dest.writeParcelable(torrent, 0);
    }
}