package com.example.parstagram31;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("rOjXNt9ZmyxiJiG4SdzTCcNjyuasH3ksdwdoJzCm")
                .clientKey("lsMJyi7LyN0jnEaQO9DCFcybOotF7ubjobN2KDJb")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
