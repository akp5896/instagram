package com.example.parstagram31.fragments;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.parstagram31.databinding.FragmentBiographyBinding;
import com.example.parstagram31.databinding.HeaderBinding;
import com.google.android.material.dialog.MaterialDialogs;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import okhttp3.Headers;

public class BiographyFragment extends DialogFragment {
    private static final String TAG = "COMPOSE TWEET";
    FragmentBiographyBinding binding;
    private String text;
    HeaderBinding headerBinding;

    public BiographyFragment() {
    }
    public static BiographyFragment newInstance(String text, HeaderBinding headerBinding) {
        BiographyFragment fragment = new BiographyFragment();
        fragment.text = text;
        fragment.headerBinding = headerBinding;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.etTweetText.setText(text);
        binding.btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = ParseUser.getCurrentUser();
                String bio = binding.etTweetText.getText().toString();
                user.put("bio", bio);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        headerBinding.tvBio.setText(bio);
                        dismiss();
                    }
                });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBiographyBinding.inflate(getLayoutInflater());
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
