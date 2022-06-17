package com.example.parstagram31.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.parstagram31.DetailsActivity;
import com.example.parstagram31.Models.Post;
import com.example.parstagram31.ProfileActivity;
import com.example.parstagram31.R;
import com.example.parstagram31.TapView;
import com.example.parstagram31.Utils.GestureListener;
import com.example.parstagram31.Utils.LikesSetup;
import com.example.parstagram31.databinding.ItemPostBinding;
import com.parse.ParseException;
import com.parse.ParseFile;

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
                binding.likable.ivImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(image.getUrl()).into(binding.likable.ivImage);
            }
            else {
                binding.likable.ivImage.setVisibility(View.GONE);
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
            //binding.ivImage.setOnClickListener(getNavigationListener(post));
            binding.tvUsername.setOnClickListener(getUserListener(post));
            binding.ivProfilePicture.setOnClickListener(getUserListener(post));


            LikesSetup.Setup(binding.likes, post, context);

            binding.likable.ivImage.
                    setGestureDetector(
                            new GestureDetector(context,
                                    new GestureListener(context, post, binding.likable, binding.likes)));
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
    }
}
