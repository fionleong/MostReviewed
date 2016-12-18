package com.example.naghmeh.mostreviewed;
public class Review {
    String rating;
    String image_url;
    String name;
    String text;
    String time_created;
    String url;

    public Review(String rating, String text, String time_created) {
        this.rating = rating;
        this.text = text;
        this.time_created = time_created;
    }

    public String toString(){
        return "rating: "+rating+", image_url: "+image_url+", name: "+name
                +", text: "+text+", time_created: "+time_created+", url: "
                +url;
    }
}
