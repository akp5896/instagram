package com.example.parstagram31;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(com.example.parstagram31.Models.Post.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("rOjXNt9ZmyxiJiG4SdzTCcNjyuasH3ksdwdoJzCm")
                .clientKey("lsMJyi7LyN0jnEaQO9DCFcybOotF7ubjobN2KDJb")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
