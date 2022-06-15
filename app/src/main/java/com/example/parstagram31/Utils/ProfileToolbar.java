package com.example.parstagram31.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.parstagram31.Models.Post;
import com.example.parstagram31.databinding.HeaderBinding;
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ProfileToolbar {
    private static final String TAG = "Profile setup";

    public static void Initialize(HeaderBinding binding, AppCompatActivity activity) {
        Context context = activity.getApplicationContext();
        activity.setSupportActionBar(binding.toolbar);
        activity.getSupportActionBar().setTitle("");
        setBanner(context, binding);
        setPostCount(binding);
    }

    private static void setPostCount(HeaderBinding binding) {
        Post.countPostsByUser(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                binding.tvPostsCount.setText(String.valueOf(count));
            }
        }, ParseUser.getCurrentUser());
    }

    public static void setBanner(Context context, HeaderBinding binding) {

        try {
            ParseFile parseFile = ParseUser.getCurrentUser().fetch().getParseFile("image");
            if(parseFile != null) {
                Glide.with(context)
                        .load(parseFile.getUrl())
                        .transform(new RoundedCorners(50))
                        .into(binding.ivBanner);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @NonNull
    public static ActivityResultCallback<ActivityResult> getHeaderCallback(HeaderBinding binding, Activity loadActivity) {
        return new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if ((result.getData() != null)) {
                        Uri photoUri = result.getData().getData();

                        // Load the image located at photoUri into selectedImage
                        Bitmap selectedImage = GalleryHandler.loadFromUri(photoUri, loadActivity);

                        ParseUser user = ParseUser.getCurrentUser();
                        user.put("image", CameraHandler.bitmapToParseFile(selectedImage));
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                // Load the selected image into a preview
                                binding.ivBanner.setImageBitmap(selectedImage);
                            }
                        });
                    }
                }
            }
        };
    }
}


