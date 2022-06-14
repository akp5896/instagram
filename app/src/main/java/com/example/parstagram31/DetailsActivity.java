package com.example.parstagram31;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.parstagram31.Models.Post;
import com.example.parstagram31.databinding.ActivityDetailsBinding;
import com.parse.ParseFile;

import org.parceler.Parcel;
import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {

    ActivityDetailsBinding binding;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        post = Parcels.unwrap(getIntent().getParcelableExtra("post"));

        // Bind the post data to the view elements
        binding.tvDescription.setText(post.getDescription());
        binding.tvUsername.setText(post.getUser().getUsername());
        binding.tvTime.setText(post.getCreatedAt().toString());
        ParseFile image = post.getImage();
        if (image != null) {
            binding.ivImage.setVisibility(View.VISIBLE);
            Glide.with(this).load(image.getUrl()).into(binding.ivImage);
        }
        else {
            binding.ivImage.setVisibility(View.GONE);
        }
    }
}