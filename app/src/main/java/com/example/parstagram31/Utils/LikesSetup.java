package com.example.parstagram31.Utils;

import android.content.Context;
import android.view.View;

import com.example.parstagram31.Models.Post;
import com.example.parstagram31.R;
import com.example.parstagram31.databinding.LikesBinding;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class LikesSetup {
    public static void Setup(LikesBinding binding, Post post, Context context) {
        setLikeColor(binding, post, context);
        binding.tvLikes.setText(post.getLikes().toString());

        binding.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(post.getLiked()) {
                    post.unlike();
                }
                else {
                    post.like();
                }
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        setLikeColor(binding, post, context);
                    }
                });
            }
        });
    }
    public static void setLikeColor(LikesBinding binding, Post post, Context context) {
        binding.tvLikes.setText(post.getLikes().toString());
        if (post.getLiked()) {
            binding.ivLike.setColorFilter(context.getResources().getColor(R.color.red));
        } else {
            binding.ivLike.setColorFilter(context.getResources().getColor(R.color.black));
        }
    }

}
