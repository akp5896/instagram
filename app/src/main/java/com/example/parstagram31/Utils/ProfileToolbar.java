package com.example.parstagram31.Utils;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.parstagram31.databinding.HeaderBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class ProfileToolbar {
    private static final String TAG = "Profile setup";

    public static void Initialize(HeaderBinding binding, AppCompatActivity activity) {
        Context context = activity.getApplicationContext();
        activity.setSupportActionBar(binding.toolbar);
        activity.getSupportActionBar().setTitle("");
        setBanner(context, binding);

        binding.ivBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public static void setBanner(Context context, HeaderBinding binding) {

        try {
            ParseFile parseFile = ParseUser.getCurrentUser().fetch().getParseFile("image");
            Glide.with(context).load(parseFile.getUrl()).transform(new RoundedCorners(50)).into(binding.ivBanner);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}


