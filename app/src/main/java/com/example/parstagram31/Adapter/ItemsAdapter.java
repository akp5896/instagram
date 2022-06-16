package com.example.parstagram31.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parstagram31.DMActivity;
import com.example.parstagram31.ProfileActivity;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public interface OnLongClickListener{
        void onItemLongItemClicked(int position);
    }

    List<ParseUser> users;
    Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(todoView);
    }

    public ItemsAdapter(List<ParseUser> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParseUser item = users.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        public void bind(ParseUser user) {
            tvItem.setText(user.getUsername());
            tvItem.setOnClickListener(v -> {
                Intent i  = new Intent(context, DMActivity.class);
                i.putExtra("other", Parcels.wrap(user));
                context.startActivity(i);
            });
        }
    }
}
