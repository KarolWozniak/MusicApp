package Json;

/**
 * Created by Karol on 2018-03-17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class _4 {

    @SerializedName("dloadUrl")
    @Expose
    private String dloadUrl;
    @SerializedName("bitrate")
    @Expose
    private Integer bitrate;
    @SerializedName("mp3size")
    @Expose
    private String mp3size;

    public String getDloadUrl() {
        return dloadUrl;
    }

    public void setDloadUrl(String dloadUrl) {
        this.dloadUrl = dloadUrl;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    public String getMp3size() {
        return mp3size;
    }

    public void setMp3size(String mp3size) {
        this.mp3size = mp3size;
    }

}
