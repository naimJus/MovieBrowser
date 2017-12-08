package capitalria.mk.moviesfragment.gson;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Meta implements Parcelable {

    @SerializedName("server_time")
    @Expose
    private Integer serverTime;
    @SerializedName("server_timezone")
    @Expose
    private String serverTimezone;
    @SerializedName("api_version")
    @Expose
    private Integer apiVersion;
    @SerializedName("execution_time")
    @Expose
    private String executionTime;

    protected Meta(Parcel in) {
        serverTimezone = in.readString();
        executionTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serverTimezone);
        dest.writeString(executionTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Meta> CREATOR = new Creator<Meta>() {
        @Override
        public Meta createFromParcel(Parcel in) {
            return new Meta(in);
        }

        @Override
        public Meta[] newArray(int size) {
            return new Meta[size];
        }
    };

    public Integer getServerTime() {
        return serverTime;
    }

    public void setServerTime(Integer serverTime) {
        this.serverTime = serverTime;
    }

    public String getServerTimezone() {
        return serverTimezone;
    }

    public void setServerTimezone(String serverTimezone) {
        this.serverTimezone = serverTimezone;
    }

    public Integer getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(Integer apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

}
