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
    String review_count;
    String image_url;
    String display_address;
    String latitude;
    String longitude;
    String phone;
    String id;

    public Business(String name, String rating, String review_count, String image_url, String id) {
        this.name = name;
        this.rating = rating;
        this.review_count = review_count;
        this.image_url = image_url;
        this.id = id;
    }

    public Business(String name, String rating, String review_count, String image_url,
                    String display_address, String latitude, String longitude) {
        this.name = name;
        this.rating = rating;
        this.review_count = review_count;
        this.image_url = image_url;
        this.display_address = display_address;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public Business() {

    }

    public String getName() {
        return name;
    }

    public String getRating() {
        return rating;
    }

    public String getReview_count() {
        return review_count;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getDisplay_address() {
        return display_address;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public static ArrayList<Business> getRecipesFromFile(String filename, Context context){
        final ArrayList<Business> businessList = new ArrayList<>();

        try {
            // Load data
            String jsonString = loadJsonFromAsset("data.json", context);
            Log.i("jsonString: ", jsonString);
            JSONObject json = new JSONObject(jsonString);
            Log.i("json: ", json.toString());
            JSONArray businessArray = json.getJSONArray("businesses");

            // Get Recipe objects from data
            for(int i = 0; i < businessArray.length(); i++){
                Business business = new Business();
                JSONObject busJson = businessArray.getJSONObject(i);

                business.name = busJson.optString("name");
                business.image_url = busJson.optString("image_url");
                business.rating = busJson.optString("rating");
                //business.phone = busJson.optString("phone");
                business.review_count = busJson.optString("review_count");

                businessList.add(business);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return businessList;
    }

    private static String loadJsonFromAsset(String filename, Context context) {
        String json = null;

        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }
}
