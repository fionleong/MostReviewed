package com.example.naghmeh.mostreviewed;

/**
 * Created by Wilson on 12/7/2016.
 */

public class YelpToken {
    final String access_token;
    final String token_type;
    final String expires_in;

    public YelpToken(String access_token, String token_type, String expires_in) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
    }
}