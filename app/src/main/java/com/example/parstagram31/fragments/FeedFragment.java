package com.example.parstagram31.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parstagram31.Adapter.PostAdapter;
import com.example.parstagram31.Models.Post;
import com.example.parstagram31.R;
import com.example.parstagram31.databinding.FragmentFeedBinding;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;


public class FeedFragment extends Fragment {
    FragmentFeedBinding binding;
    private PostAdapter adapter;
    private List<Post> allPosts;

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        allPosts = new ArrayList<>();
        adapter = new PostAdapter(getActivity(), allPosts);
        binding.rvPosts.setAdapter(adapter);
        // set the layout manager on the recycler view
        binding.rvPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        Post.queryPosts(getPost());

        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override

            public void onRefresh() {
                Post.queryPosts(getPost());
                binding.swipeContainer.setRefreshing(false);
            }
        });
        binding.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFeedBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}