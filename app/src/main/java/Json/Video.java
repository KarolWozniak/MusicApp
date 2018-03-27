package Json;

/**
 * Created by Karol on 2018-03-17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("vidID")
    @Expose
    private String vidID;
    @SerializedName("vidTitle")
    @Expose
    private String vidTitle;
    @SerializedName("vidInfo")
    @Expose
    private VidInfo vidInfo;

    public String getVidID() {
        return vidID;
    }

    public void setVidID(String vidID) {
        this.vidID = vidID;
    }

    public String getVidTitle() {
        return vidTitle;
    }

    public void setVidTitle(String vidTitle) {
        this.vidTitle = vidTitle;
    }

    public VidInfo getVidInfo() {
        return vidInfo;
    }

    public void setVidInfo(VidInfo vidInfo) {
        this.vidInfo = vidInfo;
    }

}
