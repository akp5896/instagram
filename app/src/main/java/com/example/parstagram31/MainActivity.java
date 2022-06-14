package com.example.parstagram31;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.parstagram31.Utils.CameraHandler;
import com.example.parstagram31.Utils.GalleryHandler;
import com.example.parstagram31.Utils.ProfileToolbar;
import com.example.parstagram31.databinding.ActivityMainBinding;
import com.example.parstagram31.fragments.ComposeFragment;
import com.example.parstagram31.fragments.FeedFragment;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN ACTIVITY";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int PICK_PHOTO_CODE = 1046;


    ActivityMainBinding binding;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    GalleryHandler handler;
    ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ComposeFragment compose = new ComposeFragment();
        FeedFragment feed = new FeedFragment();

        handler = new GalleryHandler(this, binding.header);
        launcher = handler.getGalleryLauncher();

        ProfileToolbar.Initialize(binding.header, this);

        binding.header.ivBanner.setOnClickListener(v -> launcher.launch(Intent.createChooser(handler.onPickPhoto(), "Select Picture")));

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.compose:
                        fragmentManager.beginTransaction().replace(R.id.fragmentPlaceholder, compose).commit();
                        return true;
                    case R.id.feed:
                        fragmentManager.beginTransaction().replace(R.id.fragmentPlaceholder, feed).commit();
                        return true;
                    case R.id.profile:
                        return true;
                    default:
                        return true;
                }
            }
        });
    }


    //    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if ((data != null) && requestCode == PICK_PHOTO_CODE) {
//            Uri photoUri = data.getData();
//
//            // Load the image located at photoUri into selectedImage
//            Bitmap selectedImage = handler.loadFromUri(photoUri);
//
//            ParseUser user = ParseUser.getCurrentUser();
//            user.put("image", CameraHandler.bitmapToParseFile(selectedImage));
//            user.saveInBackground(new SaveCallback() {
//                @Override
//                public void done(ParseException e) {
//                    // Load the selected image into a preview
//                    binding.header.ivBanner.setImageBitmap(selectedImage);
//                }
//            });
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
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
        return true;
    }
}