package com.example.parstagram31.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.parstagram31.DetailsActivity;
import com.example.parstagram31.MainActivity;
import com.example.parstagram31.Models.Post;
import com.example.parstagram31.ProfileActivity;
import com.example.parstagram31.R;
import com.example.parstagram31.databinding.ItemPostBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemPostBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemPostBinding.bind(itemView);
        }

        public void bind(Post post) {
            // Bind the post data to the view elements
            binding.tvDescription.setText(post.getDescription());
            binding.tvUsername.setText(post.getUser().getUsername());
            binding.tvTime.setText(post.getCreatedAt().toString());
            ParseFile image = post.getImage();
            if (image != null) {
                binding.ivImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(image.getUrl()).into(binding.ivImage);
            }
            else {
                binding.ivImage.setVisibility(View.GONE);
            }

            ParseFile parseFile = null;
            try {
                parseFile = post.getUser().fetch().getParseFile("image");
                if(parseFile != null) {
                    Glide.with(context).load(parseFile.getUrl()).transform(new RoundedCorners(50)).into(binding.ivProfilePicture);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            binding.tvDescription.setOnClickListener(getNavigationListener(post));
            binding.ivImage.setOnClickListener(getNavigationListener(post));
            binding.tvUsername.setOnClickListener(getUserListener(post));
            binding.ivProfilePicture.setOnClickListener(getUserListener(post));

            try {
                post.fetch();
                setLikeColor(post);
                binding.tvLikes.setText(post.getLikes().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

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
                            setLikeColor(post);
                            binding.tvLikes.setText(post.getLikes().toString());
                        }
                    });
                }
            });

        }

        @NonNull
        private View.OnClickListener getUserListener(Post post) {
            return v -> {
                Intent i = new Intent(context, ProfileActivity.class);
                i.putExtra("user", Parcels.wrap(post.getUser()));
                context.startActivity(i);
            };
        }

        @NonNull
        private View.OnClickListener getNavigationListener(Post post) {
            return v -> {
                Intent i = new Intent(context, DetailsActivity.class);
                i.putExtra("post", Parcels.wrap(post));
                context.startActivity(i);
            };
        }

        private void setLikeColor(Post post) {
            if (post.getLiked()) {
                binding.ivLike.setColorFilter(context.getResources().getColor(R.color.red));
            } else {
                binding.ivLike.setColorFilter(context.getResources().getColor(R.color.black));
            }
        }

    }
}
