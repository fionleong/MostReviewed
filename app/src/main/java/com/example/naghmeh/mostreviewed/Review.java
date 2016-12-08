package com.example.naghmeh.mostreviewed;

/**
 * Created by naghmeh on 12/8/16.
 */
public class Review {
    final String rating;
    final String text;
    final String time_created;

    public Review(String rating, String text, String time_created) {
        this.rating = rating;
        this.text = text;
        this.time_created = time_created;
    }

    public String getRating() {
        return rating;
    }

    public String getText() {
        return text;
    }

    public String getTime_created() {
        return time_created;
    }


}
