package example.com.moviesfragment;

/**
 * Created by jusuf on 04.7.2017.
 */

public class Movie {

    private long id;
    private String name, description;
    private int year;
    private String imageUrl;
    private float rating;

    public Movie(long id, String name, String description, int year, String imageUrl, float rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.year = year;
        this.imageUrl = imageUrl;
        this.rating = rating;
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
}
