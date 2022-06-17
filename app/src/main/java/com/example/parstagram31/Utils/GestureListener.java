package com.example.parstagram31.Utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.example.parstagram31.DetailsActivity;
import com.example.parstagram31.Models.Post;
import com.example.parstagram31.databinding.LikableImageBinding;
import com.example.parstagram31.databinding.LikesBinding;

import org.parceler.Parcels;

import java.util.concurrent.TimeUnit;

public class GestureListener extends GestureDetector.SimpleOnGestureListener {

    Context context;
    Post post;
    LikableImageBinding binding;
    LikesBinding likes;

    public GestureListener() {
    }

    public GestureListener(Context context, Post post, LikableImageBinding binding, LikesBinding likes) {
        this.context = context;
        this.post = post;
        this.binding = binding;
        this.likes = likes;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }
    // event when double tap occurs
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");
        binding.ivHeart.setVisibility(View.VISIBLE);
        LikesSetup.onLikeClick(post, likes, context);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Looper.myLooper() == null)
                    {
                        Looper.prepare();
                    }
                    enterReveal();
                    Thread.sleep(700);
                    exitReveal();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d("Double Tap", "Single");
        Intent i = new Intent(context, DetailsActivity.class);
        i.putExtra("post", Parcels.wrap(post));
        context.startActivity(i);
        return super.onSingleTapConfirmed(e);
    }

    void enterReveal() {
        final View myView = binding.ivHeart;
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight()) ;
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        myView.setVisibility(View.VISIBLE);
        anim.setDuration(100);
        anim.start();
    }

    void exitReveal() {
        final View myView = binding.ivHeart;
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;
        int initialRadius = myView.getWidth() / 2;

        // create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                myView.setVisibility(View.INVISIBLE);
            }
        });
        anim.setDuration(20);
        // start the animation
        anim.start();
    }
}