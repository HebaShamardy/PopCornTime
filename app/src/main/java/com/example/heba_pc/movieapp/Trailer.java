package com.example.heba_pc.movieapp;

/**
 * Created by HEBA-PC on 11/25/2016.
 */

public class Trailer {

    private String id;
    private String url;
    private String name;
    private String imageURL;


    public Trailer(String id, String url, String name, String imageURL) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
