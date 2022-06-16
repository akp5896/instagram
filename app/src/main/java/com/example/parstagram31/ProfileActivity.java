package com.example.parstagram31;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.parstagram31.Adapter.PostAdapter;
import com.example.parstagram31.Models.Post;
import com.example.parstagram31.Utils.ProfileToolbar;
import com.example.parstagram31.databinding.ActivityProfileBinding;
import com.example.parstagram31.databinding.FragmentProfileBinding;
import com.example.parstagram31.fragments.ViewDmListFragment;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "PROFILE ACTIVITY";
    ActivityProfileBinding binding;
    private PostAdapter adapter;
    private List<Post> allPosts;
    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ProfileToolbar.Initialize(binding.header, this);

        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        allPosts = new ArrayList<>();
        adapter = new PostAdapter(this, allPosts);
        binding.rvPosts.setAdapter(adapter);
        // set the layout manager on the recycler view
        binding.rvPosts.setLayoutManager(new GridLayoutManager(this, 2));
        Post.queryPostsByUser(getPost(), user);

        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override

            public void onRefresh() {
                Post.queryPostsByUser(getPost(), ParseUser.getCurrentUser());
                binding.swipeContainer.setRefreshing(false);
            }
        });
        binding.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        binding.fabDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(ProfileActivity.this, DMActivity.class);
                i.putExtra("other", Parcels.wrap(user));
                startActivity(i);
            }
        });
    }

    @NonNull
    private FindCallback<Post> getPost() {
        return (posts, e) -> {
            if(e != null) {
                Log.i("POST", "error has occured" + e);
                return;
            }
            allPosts.addAll(posts);
            adapter.notifyDataSetChanged();
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout) {
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e != null) {
                        Log.i(TAG, "Logout failed");
                        return;
                    }
                    finish();
                }
            });
        }
        if(item.getItemId() == R.id.direct) {
            FragmentManager fm = getSupportFragmentManager();
            ViewDmListFragment viewDmListFragment = ViewDmListFragment.newInstance();
            viewDmListFragment.show(fm, "fragment_compose_tweet");
        }
        return true;
    }
}