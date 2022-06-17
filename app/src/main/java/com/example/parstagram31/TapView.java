package com.example.parstagram31;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.parstagram31.Models.Post;
import com.example.parstagram31.Utils.GestureListener;

import org.parceler.Parcels;

public class TapView extends androidx.appcompat.widget.AppCompatImageView {

    GestureDetector gestureDetector;

    public TapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // creating new gesture detector
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public void setGestureDetector(GestureDetector detector) {
        this.gestureDetector = detector;
    }

    // skipping measure calculation and drawing

    // delegate the event to the gesture detector
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return gestureDetector.onTouchEvent(e);
    }
}
