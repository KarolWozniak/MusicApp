package Json;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class downloadURL {

        @SerializedName("downloadURL")
        @Expose
        private String downloadURL;

        public String getDownloadURL() {
            return downloadURL;
        }

        public void setDownloadURL(String downloadURL) {
            this.downloadURL = downloadURL;
        }
        public String getVidInfo() {
            return this.downloadURL;
    }
}
