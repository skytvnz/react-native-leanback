package com.rs.leanbacknative.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("thumb")
    @Expose
    String thumbUrl;

    @SerializedName("title")
    @Expose
    String title;

    public Data(String thumbUrl, String title) {
        this.thumbUrl = thumbUrl;
        this.title = title;
    }


    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
