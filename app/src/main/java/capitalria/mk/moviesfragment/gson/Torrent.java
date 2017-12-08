package capitalria.mk.moviesfragment.gson;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Torrent implements Parcelable {


    public static final Creator<Torrent> CREATOR = new Creator<Torrent>() {
        @Override
        public Torrent createFromParcel(Parcel in) {
            return new Torrent(in);
        }

        @Override
        public Torrent[] newArray(int size) {
            return new Torrent[size];
        }
    };
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("quality")
    @Expose
    private String quality;
    @SerializedName("seeds")
    @Expose
    private Integer seeds;
    @SerializedName("peers")
    @Expose
    private Integer peers;
    @SerializedName("size")
    @Expose
    private String size;

    public Torrent() {
    }

    public Torrent(Parcel in) {
        url = in.readString();
        hash = in.readString();
        quality = in.readString();
        size = in.readString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public Integer getSeeds() {
        return seeds;
    }

    public void setSeeds(Integer seeds) {
        this.seeds = seeds;
    }

    public Integer getPeers() {
        return peers;
    }

    public void setPeers(Integer peers) {
        this.peers = peers;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(hash);
        dest.writeString(quality);
        dest.writeString(size);
    }
}