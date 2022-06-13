package com.example.parstagram31;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.parstagram31.Models.Post;
import com.example.parstagram31.Utils.CameraHandler;
import com.example.parstagram31.databinding.ActivityMainBinding;
import com.example.parstagram31.fragments.ComposeFragment;
import com.example.parstagram31.fragments.FeedFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN ACTIVITY";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;

    ActivityMainBinding binding;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ComposeFragment compose = new ComposeFragment();
        FeedFragment feed = new FeedFragment();

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.compose:
                        fragmentManager.beginTransaction().replace(R.id.fragmentPlaceholder, compose).commit();
                        return true;
                    case R.id.feed:
                        fragmentManager.beginTransaction().replace(R.id.fragmentPlaceholder, feed).commit();
                    default:
                        return true;
                }
            }
        });
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