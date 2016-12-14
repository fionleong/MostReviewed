//package com.example.naghmeh.mostreviewed;
//
//import java.util.List;
//
///**
// * Created by naghmeh on 11/25/16.
// */
//public class Business {
//    final String id;
//    final String name;
//    final Float rating;
//    final String review_count;
//    final String image_url;
//    final String price;
//    final String phone;
//    final String categories;
//    final String distance;
//    final String address1;
//    final String address2;
//    final String address3;
//    final String city;
//    final String state;
//    final String zip_code;
//    final List<String> photos;
//    final List<Review> reviews;
//
//
//    public Business(String name, Float rating, String review_count,
//                    String image_url, String display_address, String latitude,
//                    String longitude, String id, String price, String phone,
//                    String categories, String distance, String address1,
//                    String address2, String address3, String city,
//                    String state, String zip_code, List<String> photos, List<Review> reviews) {
//        this.name = name;
//        this.rating = rating;
//        this.review_count = review_count;
//        this.image_url = image_url;
//        this.id = id;
//        this.phone = phone;
//        this.price = price;
//        this.categories = categories;
//        this.distance = distance;
//        this.address1 = address1;
//        this.address2 = address2;
//        this.address3 = address3;
//        this.city = city;
//        this.state = state;
//        this.zip_code = zip_code;
//        this.photos = photos;
//        this.reviews = reviews;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public Float getRating() {
//        return rating;
//    }
//
//    public String getReview_count() {
//        return review_count;
//    }
//
//    public String getImage_url() {
//        return image_url;
//    }
//
//    public String getPrice() {
//        return price;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public String getCategories() {
//        return categories;
//    }
//
//    public String getDistance() {
//        return distance;
//    }
//
//    public String getAddress1() {
//        return address1;
//    }
//
//    public String getAddress2() {
//        return address2;
//    }
//
//    public String getAddress3() {
//        return address3;
//    }
//
//    public String getZip_code() {
//        return zip_code;
//    }
//
//    public List<String> getPhotos() {
//        return photos;
//    }
//
//    public String getCity() {
//        return city;
//    }
//
//    public String getState() {
//        return state;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public List<Review> getReviews() {
//        return reviews;
//    }
// }

package com.example.naghmeh.mostreviewed;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by naghmeh on 11/25/16.
 */
public class Business {
    String name;
    String rating;
    String price;
    String review_count;
    String image_url;
    String url; // business's yelp url
    String phone;
    String id;
    String latitude;
    String longitude;

    /*
    *  The ones that are commented out need to reimplement
    *  getting the JSON file since these are inside an array, etc.
    */

    //    String cuisine;       // categories: title
//    String hours;
//    String miles;
//
//    String photo1;
//    String photo2;
//    String photo3;
//
//    String address1;
//    String city;
//    String state;
//


    /*
    *   This is the complete list for all the info we need in the restaurant view
    */

//    public Business(String name, String rating, String cuisine, String hours, String miles, String price, String review_count,
//                    String image_url, String photo1, String photo2, String photo3, String url,
//                    String address1, String city, String state, String latitude, String longitude) {
//        this.name = name;
//        this.rating = rating;
//        this.cuisine = cuisine;
//        this.hours = hours;
//        this.miles = miles;
//        this.price = price;
//        this.review_count = review_count;
//        this.image_url = image_url;
//        this.photo1 = photo1;
//        this.photo2 = photo2;
//        this.photo3 = photo3;
//        this.url = url;
//        this.address1 = address1;
//        this.city = city;
//        this.state = state;
//        this.latitude = latitude;
//        this.longitude = longitude;
//    }


    public Business(String name, String rating, String review_count, String image_url, String id) {
        this.name = name;
        this.rating = rating;
        this.review_count = review_count;
        this.image_url = image_url;
        this.id = id;
    }

    public Business(String name, String rating, String price, String review_count,
                    String image_url, String url, String latitude, String longitude) {
        this.name = name;
        this.rating = rating;
        this.price = price;
        this.review_count = review_count;
        this.image_url = image_url;
        this.url = url;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Business() {

    }

}
