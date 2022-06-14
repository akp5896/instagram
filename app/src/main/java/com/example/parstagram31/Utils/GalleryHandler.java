package com.example.parstagram31.Utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parstagram31.databinding.HeaderBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;

public class GalleryHandler {
    private final HeaderBinding headerBinding;
    public ActivityResultLauncher<Intent> launcher;
    AppCompatActivity activity;

    public GalleryHandler(AppCompatActivity activity, HeaderBinding headerBinding) {
        this.activity = activity;
        this.headerBinding = headerBinding;
        getGalleryLauncher();
    }

    // Trigger gallery selection for a photo
    public Intent onPickPhoto() {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return intent;
    }

    public void getGalleryLauncher() {
        launcher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            if ((result.getData() != null)) {
                                Uri photoUri = result.getData().getData();

                                // Load the image located at photoUri into selectedImage
                                Bitmap selectedImage = loadFromUri(photoUri);

                                ParseUser user = ParseUser.getCurrentUser();
                                user.put("image", CameraHandler.bitmapToParseFile(selectedImage));
                                user.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        // Load the selected image into a preview
                                        headerBinding.ivBanner.setImageBitmap(selectedImage);
                                    }
                                });
                            }
                        }
                    }
                });
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if(Build.VERSION.SDK_INT > 27){
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(activity.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }


}
