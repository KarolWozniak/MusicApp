package Json;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class downloadURL {

        @SerializedName("downloadURL")
        @Expose
        private String downloadURL;

        @SerializedName("title")
        @Expose
        private String title;

        public String getDownloadURL() {
            return downloadURL;
        }

        public void setDownloadURL(String downloadURL) {
            this.downloadURL = downloadURL;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
}
