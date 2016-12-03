package com.example.heba_pc.movieapp;


import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by HEBA-PC on 10/29/2016.
 */

public class Movie {

    private int id;
    private String title;
    private String imageUrl;
    private Bitmap image;
    private String plot;
    private String releaseDate;
    private double rating;
    private String trailerJSON;
    private String reviewJSON;
    private ArrayList<Trailer> trailers;
    private ArrayList<Review> reviews;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public ArrayList<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public String getTrailerJSON() {
        return trailerJSON;
    }

    public void setTrailerJSON(String trailerJSON) {
        this.trailerJSON = trailerJSON;
    }

    public String getReviewJSON() {
        return reviewJSON;
    }

    public void setReviewJSON(String reviewJSON) {
        this.reviewJSON = reviewJSON;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
