package com.example.parstagram31.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.parstagram31.MainActivity;
import com.example.parstagram31.Models.Post;
import com.example.parstagram31.R;
import com.example.parstagram31.Utils.CameraHandler;
import com.example.parstagram31.databinding.FragmentComposeBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ComposeFragment extends Fragment {

    FragmentComposeBinding binding;
    CameraHandler handler;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private String TAG = "COMPOSE FRAGMENT";

    public ComposeFragment() {
        // Required empty public constructor
    }

    public static ComposeFragment newInstance(String param1, String param2) {
        ComposeFragment fragment = new ComposeFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnsubmit.setOnClickListener(v -> {
            if(binding.etDescription.getText() == null || binding.etDescription.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "no description", Toast.LENGTH_SHORT).show();
                return;
            }
            savePost(binding.etDescription.getText().toString(), ParseUser.getCurrentUser());
        });

        binding.btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(handler.launchCamera(), CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private void savePost(String description, ParseUser currentUser) {
        Post post = new Post();
        post.setDescription(description);
        post.setUser(currentUser);
        post.setImage(handler.bitmapToParseFile());
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    Toast.makeText(getActivity(), "saving failed", Toast.LENGTH_SHORT);
                    Log.i(TAG, "fail: " + e);
                    return;
                }
                binding.etDescription.setText(null);
                binding.ivPhoto.setImageBitmap(null);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("called", "called");
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // by this point we have the camera photo on disk

                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                handler.onActivityResult();
                binding.ivPhoto.setImageBitmap(handler.getImageToUpload());

            } else { // Result was a failure
                Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new CameraHandler(getActivity(), CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentComposeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}