package com.example.parstagram31.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Message")
public class Message extends ParseObject {
    public static final String USER_ID_KEY = "userId";
    public static final String BODY_KEY = "body";
    public static final String RECEIVER_KEY = "receiverId";

    public ParseUser getUserId() {
        return getParseUser(USER_ID_KEY);
    }

    public String getBody() {
        return getString(BODY_KEY);
    }

    public void setUserId(ParseUser user) {
        put(USER_ID_KEY, user);
    }

    public void setBody(String body) {
        put(BODY_KEY, body);
    }
}
