package com.example.restaurantorderingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPassword, etConfirmPassword;
    private Button btnSignUp;
    private TextView tvLogin;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogin = findViewById(R.id.tvLogin);

        btnSignUp.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if user already exists
            if (sharedPreferences.contains("password_" + email)) {
                Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save user
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name_" + email, name);
            editor.putString("password_" + email, password);
            editor.putString("loggedInUser", email);
            editor.putBoolean("isLoggedIn", true);
            editor.apply();

            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignupActivity.this, DashboardActivity.class));
            finish();
        });

        tvLogin.setOnClickListener(v -> {
            finish();
        });
    }
}

