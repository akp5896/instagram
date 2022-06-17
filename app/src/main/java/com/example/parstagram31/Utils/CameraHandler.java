package com.example.parstagram31.Utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.example.parstagram31.databinding.FragmentFeedBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class CameraHandler {

    private static final String TAG = "CAMERA";
    public String photoFileName = "photo.jpg";
    private File photoFile;
    public ActivityResultLauncher<Intent> launcher;
    Bitmap imageToUpload = null;
    ComponentActivity activity;

    public CameraHandler(ComponentActivity activity, ActivityResultCallback<ActivityResult> callback) {
        this.activity = activity;
        getCameraLauncher(callback);
    }

    public Bitmap getImageToUpload() {
        return imageToUpload;
    }

    public Intent launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(activity, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            // Start the image capture intent to take photo
            return intent;
        }
        return null;
    }
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    public Bitmap onActivityResult() {
        //Uri takenPhotoUri = Uri.fromFile(getPhotoFileUri(photoFileName));
        Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
        Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(takenImage, 300);
        imageToUpload = resizedBitmap;
        return resizedBitmap;
    }

    public void getCameraLauncher(ActivityResultCallback<ActivityResult> callack) {
        launcher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                callack);
    }


    @Nullable
    public static ParseFile bitmapToParseFile(Bitmap image) {
        if(image == null)
            return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapBytes = stream.toByteArray();

        ParseFile result = new ParseFile("myImage", bitmapBytes);
        return result;
    }
}
