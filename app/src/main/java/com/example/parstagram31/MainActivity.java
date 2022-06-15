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
import com.example.parstagram31.fragments.ProfileFragment;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN ACTIVITY";

    ActivityMainBinding binding;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    GalleryHandler handler;
    ComposeFragment compose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        compose = new ComposeFragment();
        FeedFragment feed = new FeedFragment();
        ProfileFragment profile = new ProfileFragment();

        handler = new GalleryHandler(this, ProfileToolbar.getHeaderCallback(binding.header, this));

        compose.setCameraHandler(new CameraHandler(this, compose.ComposeCallback()));


        ProfileToolbar.Initialize(binding.header, this);

        binding.header.ivBanner.setOnClickListener(v -> handler.launcher.launch(Intent.createChooser(handler.onPickPhoto(), "Select Picture")));

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.compose:
                        binding.header.appbar.setVisibility(View.GONE);
                        fragmentManager.beginTransaction().replace(R.id.fragmentPlaceholder, compose).commit();
                        return true;
                    case R.id.feed:
                        binding.header.appbar.setVisibility(View.VISIBLE);
                        fragmentManager.beginTransaction().replace(R.id.fragmentPlaceholder, feed).commit();
                        return true;
                    case R.id.profile:
                        binding.header.appbar.setVisibility(View.VISIBLE);
                        fragmentManager.beginTransaction().replace(R.id.fragmentPlaceholder, profile).commit();
                        return true;
                    default:
                        return true;
                }
            }
        });

        binding.bottomNavigation.setSelectedItemId(R.id.feed);
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
        return true;
    }
}