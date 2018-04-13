package com.nhatro.model;

/**
 * Created by CongHoang on 4/13/2018.
 */

public class Add_Images {
    private String path;
    private String uri;

    public Add_Images(String path, String uri) {
        this.path = path;
        this.uri = uri;
    }

    public String getPath() {

        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
