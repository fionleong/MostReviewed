package com.example.naghmeh.mostreviewed;

import java.text.DecimalFormat;

public class Business {
    String id;
    String name;
    String image_url;
    boolean is_closed;
    String url; // business's yelp url
    String price;
    String rating;
    String review_count;
    String phone;
    String photo1;
    String photo2;
    String photo3;
    String hours;
    Double latitude;
    Double longitude;
    String cuisine;       // categories: title
    String address1;
    String city;
    String state;
    String country;
    String zip_code;
    Double distance;
    DecimalFormat df = new DecimalFormat("#.#");

    //This constructor is for the searchController, for the list view.
    public Business(String rating, String price, String phone, String id, boolean is_closed, String review_count,
                    String name, String url, String image_url, Double distance) {
        this.rating = rating;
        this.price = price;
        this.phone = phone;
        this.id = id;
        this.is_closed = is_closed;
        this.review_count = review_count;
        this.name = name;
        this.url = url;
        this.image_url = image_url;
        this.distance = distance;
    }

    public String getDistance(){
        return df.format(distance);
    }

    //This constructor is for restaurantActivity.
    public Business(String id, String name, String image_url, boolean is_closed, String url, String price,
                    String rating, String review_count, String phone) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.is_closed = is_closed;
        this.url  = url;
        this.price = price;
        this.rating = rating;
        this.review_count = review_count;
        this.phone = phone;
    }

    public String toString(){
        return "id: "+id+", name: "+name+", image_url: "+image_url+", is_closed: "+
                is_closed+", url: "+url+", price: "+price+
                ", rating: "+rating+", review_count: "+review_count+", phone: "+phone+
                ", photo1: "+photo1+", photo2: "+photo2+", photo3: "+photo3+", hours:"+
                hours+", latitude: "+latitude+", longitude: "+longitude+", cuisine: "+
                cuisine+", distance: "+distance+", address1: "+address1+", city: "+
                city+", state: "+state+", country:"+country+", zip_code: "+zip_code;
    }
}
