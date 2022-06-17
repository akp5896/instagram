package com.example.parstagram31;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.parstagram31.databinding.ActivityLoginBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(ParseUser.getCurrentUser() != null) {
            toMainActivity();
        }


        binding.btnLogin.setOnClickListener(v -> login());

        binding.tvSignUp.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));
    }

    private void login() {
        ParseUser.logInInBackground(
                binding.etLogin.getText().toString(),
                binding.etPass.getText().toString(),
                new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(e != null) {
                            Log.i("LOGIN ACTIVTY", "Issue with login" + e);
                            return;
                        }
                        toMainActivity();
                    }
                });
    }

    private void toMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }
}