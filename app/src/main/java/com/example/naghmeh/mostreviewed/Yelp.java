package com.example.naghmeh.mostreviewed;

/**
 * Created by naghmeh on 11/25/16.
 */
import android.content.Context;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 * Example for accessing the Yelp API.
 */
public class Yelp {

    OAuthService service;
    Token accessToken;
    String token = "zv7Zcivo-VbjKGTd_UA1wzV942gVQ-FCckrUZ5mmOkBpIgrcc4GvhS1NNbjhTrmE3XqHCTbreJJo_gDGCMJB9NAxyhQ9RbW1tiKkbfohne0o7DvUYwF_rLpQ7O1IWHYx";

    public static Yelp getYelp(Context context) {
        return new Yelp(context.getString(R.string.consumer_key), context.getString(R.string.consumer_secret),
                context.getString(R.string.token), context.getString(R.string.token_secret));
    }

    public Yelp(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        this.service = new ServiceBuilder().provider(YelpApi2.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
        this.accessToken = new Token(token, tokenSecret);
    }

    public String search(String term, double latitude, double longitude) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("ll", latitude + "," + longitude);
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }

    public String search(String term, String location) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("location", location);
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }

    public String getBusiness(String id) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/business/{ "+ id + "}");;
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }

}
