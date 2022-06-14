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
import com.example.parstagram31.Models.Comment;
import com.example.parstagram31.Models.Post;
import com.example.parstagram31.ProfileActivity;
import com.example.parstagram31.R;
import com.example.parstagram31.databinding.ItemCommentBinding;
import com.example.parstagram31.databinding.ItemPostBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Context context;
    private List<Comment> comments;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void clear() {
        comments.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Comment> list) {
        comments.addAll(list);
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
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemCommentBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCommentBinding.bind(itemView);
        }

        public void bind(Comment comment) {
            // Bind the post data to the view elements
            binding.tvDescription.setText(comment.getContent());
            binding.tvUsername.setText(comment.getAuthor().getUsername());
            binding.tvTime.setText(comment.getCreatedAt().toString());
            ParseFile parseFile = null;
            try {
                parseFile = comment.getAuthor().fetch().getParseFile("image");
                if (parseFile != null) {
                    Glide.with(context).load(parseFile.getUrl()).transform(new RoundedCorners(50)).into(binding.ivProfilePicture);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
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

