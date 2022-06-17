package com.example.parstagram31.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.parstagram31.MainActivity;
import com.example.parstagram31.Models.Post;
import com.example.parstagram31.R;
import com.example.parstagram31.Utils.CameraHandler;
import com.example.parstagram31.Utils.GalleryHandler;
import com.example.parstagram31.databinding.FragmentComposeBinding;
import com.example.parstagram31.databinding.HeaderBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

public class ComposeFragment extends Fragment {

    FragmentComposeBinding binding;
    CameraHandler handler;
    HeaderBinding header;
    private String TAG = "COMPOSE FRAGMENT";
    private GalleryHandler galleryHandler;
    Bitmap selectedImage = null;

    public ComposeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.pbProgressAction.setVisibility(View.INVISIBLE);
        binding.btnsubmit.setOnClickListener(v -> {
            if(binding.etDescription.getText() == null || binding.etDescription.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "no description", Toast.LENGTH_SHORT).show();
                return;
            }
            savePost(binding.etDescription.getText().toString(), ParseUser.getCurrentUser());
        });


        if(handler == null) {
            Log.e("COMPOSE FRAGMENT", "No handler attached! Photo opportunities disabled.");
        }
        else {
            binding.btnTakePhoto.setOnClickListener(v -> handler.launcher.launch(Intent.createChooser(handler.launchCamera(), "Select Picture")));
        }
        if(galleryHandler == null) {
            Log.e("COMPOSE FRAGMENT", "No handler attached! Gallery opportunities disabled.");
        } else {
            binding.btnUpload.setOnClickListener(v -> galleryHandler.launcher.launch(Intent.createChooser(galleryHandler.onPickPhoto(), "Select Picture")));
        }
    }

    private void savePost(String description, ParseUser currentUser) {
        binding.pbProgressAction.setVisibility(View.VISIBLE);
        Post post = new Post();
        post.setDescription(description);
        post.setUser(currentUser);
        post.setImage(CameraHandler.bitmapToParseFile(selectedImage));
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    Toast.makeText(getActivity(), "saving failed", Toast.LENGTH_SHORT);
                    Log.i(TAG, "fail: " + e);
                    return;
                }
                header.tvPostsCount.setText(
                        String.valueOf(
                                Integer.parseInt(
                                        header.tvPostsCount.getText().toString()) + 1));
                binding.etDescription.setText(null);
                binding.ivPhoto.setImageBitmap(null);
                binding.pbProgressAction.setVisibility(View.INVISIBLE);
            }
        });
    }

    public static ComposeFragment newInstance(HeaderBinding header) {
        ComposeFragment fragment = new ComposeFragment();
        fragment.header = header;
        return  fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentComposeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @NonNull
    public ActivityResultCallback<ActivityResult> ComposeCallback() {
        return new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    handler.onActivityResult();
                    selectedImage = handler.getImageToUpload();
                    binding.ivPhoto.setImageBitmap(handler.getImageToUpload());
                } else {
                    Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @NonNull
    public ActivityResultCallback<ActivityResult> ComposeGalleryCallback() {
        return new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Uri photoUri = result.getData().getData();
                    selectedImage = GalleryHandler.loadFromUri(photoUri, getActivity());
                    binding.ivPhoto.setImageBitmap(selectedImage);
                } else {
                    Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public void setCameraHandler(CameraHandler cameraHandler) {
        this.handler = cameraHandler;
    }

    public void setGalleryHandler(GalleryHandler galleryHandler) {
        this.galleryHandler = galleryHandler;
    }
}