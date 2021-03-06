package com.example.parstagram31.Models;

import android.util.Log;

import com.example.parstagram31.Adapter.PostAdapter;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.Collections;
import java.util.List;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_LIKES = "likes";
    public static final String KEY_LIKED = "liked";
    private static int limit = 20;

    public static void setLimit(int limit) {
        Post.limit = limit;
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }
    public void like() {
        increment(KEY_LIKES);
        getRelation("likedBy").add(ParseUser.getCurrentUser());
    }

    public void unlike() {
        increment(KEY_LIKES, -1);
        getRelation("likedBy").remove(ParseUser.getCurrentUser());
    }

    public boolean getLiked() {
        ParseQuery<ParseObject> likedBy = this.getRelation("likedBy").getQuery();
        likedBy.whereEqualTo(KEY_OBJECT_ID, ParseUser.getCurrentUser().getObjectId());
        try {
            return likedBy.count() > 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
    public Integer getLikes() {
        return getInt(KEY_LIKES);
    }

    public static void queryPosts(FindCallback<Post> callback, int skip) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(KEY_USER);
        query.setSkip(skip);
        query.setLimit(limit);
        query.addDescendingOrder("createdAt");
        query.findInBackground(callback);
    }

    public static void queryPostsByUser(FindCallback<Post> callback, ParseUser currentUser) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(KEY_USER);
        query.whereEqualTo(KEY_USER, currentUser);
        query.setLimit(limit);
        query.addDescendingOrder("createdAt");
        query.findInBackground(callback);
    }

    public static void countPostsByUser(CountCallback callback, ParseUser currentUser) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo(KEY_USER, currentUser);
        query.countInBackground(callback);
    }

}
