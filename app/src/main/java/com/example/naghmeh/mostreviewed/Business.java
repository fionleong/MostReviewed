package com.example.naghmeh.mostreviewed;

/**
 * Created by naghmeh on 11/25/16.
 */
public class Business {
    final String name;
    final String rating;
    final String review_count;
    final String image_url;
    final String display_address;
    final String latitude;
    final String longitude;


    public Business(String name, String rating, String review_count, String image_url, String display_address, String latitude, String longitude) {
        this.name = name;
        this.rating = rating;
        this.review_count = review_count;
        this.image_url = image_url;
        this.display_address = display_address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}