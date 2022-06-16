package com.example.parstagram31.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parstagram31.Adapter.ItemsAdapter;
import com.example.parstagram31.R;
import com.example.parstagram31.databinding.FragmentViewDmListBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ViewDmListFragment extends DialogFragment {

    FragmentViewDmListBinding binding;
    List<ParseUser> users;

    public ViewDmListFragment() {
    }

    public static ViewDmListFragment newInstance() {
        ViewDmListFragment fragment = new ViewDmListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        users = new ArrayList<>();
        ItemsAdapter adapter = new ItemsAdapter(users, getContext());
        binding.rvTalks.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvTalks.setAdapter(adapter);
        ParseQuery<ParseObject> directs = ParseUser.getCurrentUser().getParseObject("directs").getRelation("directs").getQuery();
        directs.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for(ParseObject object : objects) {
                    users.add((ParseUser) object);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentViewDmListBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        getDialog().getWindow().setLayout((6 * width) / 7, (3 * height) / 5);
        super.onStart();
    }
}