package com.example.parstagram31;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.parstagram31.Adapter.CommentAdapter;
import com.example.parstagram31.Models.Comment;
import com.example.parstagram31.Models.Post;
import com.example.parstagram31.databinding.ActivityDetailsBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    ActivityDetailsBinding binding;
    Post post;
    List<Comment> comments;

    @Override
    protected void onResume() {
        super.onResume();
        try {
            post.fetch();
            setLikeColor();
            binding.tvLikes.setText(post.getLikes().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

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

        comments = new ArrayList<>();
        CommentAdapter adapter = new CommentAdapter(this, comments);
        binding.rvComments.setAdapter(adapter);
        binding.rvComments.setLayoutManager(new LinearLayoutManager(this));
        Comment.queryCommentToPost(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> objects, ParseException e) {
                comments.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        }, post);

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comment comment = new Comment();
                comment.setCommentTo(post);
                comment.setAuthor(ParseUser.getCurrentUser());
                comment.setContent(binding.etInput.getText().toString());
                comment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        comments.add(0, comment);
                        adapter.notifyItemInserted(0);
                        binding.rvComments.smoothScrollToPosition(0);
                        binding.etInput.setText(null);
                    }
                });
            }
        });

        setLikeColor();
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
                        setLikeColor();
                        binding.tvLikes.setText(post.getLikes().toString());
                    }
                });
            }
        });
    }

    private void setLikeColor() {
        if (post.getLiked()) {
            binding.ivLike.setColorFilter(getResources().getColor(R.color.red));
        } else {
            binding.ivLike.setColorFilter(getResources().getColor(R.color.black));
        }
    }
}