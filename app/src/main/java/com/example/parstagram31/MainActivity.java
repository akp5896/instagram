package com.example.parstagram31;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.parstagram31.Models.Post;
import com.example.parstagram31.databinding.ActivityMainBinding;
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
    public String photoFileName = "photo.jpg";


    ActivityMainBinding binding;
    private File photoFile;
    Bitmap imageToUpload = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnsubmit.setOnClickListener(v -> {
            if(binding.etDescription.getText() == null || binding.etDescription.getText().toString().equals("")) {
                Toast.makeText(MainActivity.this, "no description", Toast.LENGTH_SHORT).show();
                return;
            }
            savePost(binding.etDescription.getText().toString(), ParseUser.getCurrentUser());
        });

        binding.btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(MainActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Uri takenPhotoUri = Uri.fromFile(getPhotoFileUri(photoFileName));
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(takenImage, 300);

                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                binding.ivPhoto.setImageBitmap(resizedBitmap);
                imageToUpload = resizedBitmap;
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void savePost(String description, ParseUser currentUser) {
        Post post = new Post();
        post.setDescription(description);
        post.setUser(currentUser);
        post.setImage(bitmapToParseFile());
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    Toast.makeText(MainActivity.this, "saving failed", Toast.LENGTH_SHORT);
                    Log.i(TAG, "fail: " + e);
                    return;
                }
                binding.etDescription.setText(null);
                binding.ivPhoto.setImageBitmap(null);
            }
        });
    }

    @Nullable
    private ParseFile bitmapToParseFile() {
        if(imageToUpload == null)
            return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageToUpload.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapBytes = stream.toByteArray();

        ParseFile image = new ParseFile("myImage", bitmapBytes);
        return image;
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