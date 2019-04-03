package android.sa.com.guessthecelebrity;

import java.io.Serializable;

public class Celebrity implements Serializable {
    String name;
    String imgUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
