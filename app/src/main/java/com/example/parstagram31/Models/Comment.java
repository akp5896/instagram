package com.example.parstagram31.Models;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    public static final String KEY_COMMENT_TO = "commentTo";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_CONTENT = "content";

    public void setCommentTo(Post description) {
        put(KEY_COMMENT_TO, description);
    }

    public Post setCommentTo() {
        return (Post) getParseObject(KEY_COMMENT_TO);
    }

    public ParseUser getAuthor() {
        return getParseUser(KEY_AUTHOR);
    }

    public void setAuthor(ParseUser image) {
        put(KEY_AUTHOR, image);
    }

    public String getContent() {
        return getString(KEY_CONTENT);
    }

    public void setContent(String user) {
        put(KEY_CONTENT, user);
    }

    public static void queryCommentToPost(FindCallback<Comment> callback, Post post) {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.include(KEY_AUTHOR);
        query.whereEqualTo(KEY_COMMENT_TO, post);
        query.setLimit(20);
        query.addDescendingOrder("createdAt");
        query.findInBackground(callback);
    }
}
