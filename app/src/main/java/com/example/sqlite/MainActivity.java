package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    EditText name, loc, desig;
    Button saveBtn;
    Intent intent;
    private ToggleButton themeToggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Load the theme before setting the content view
        SharedPreferences preferences = getSharedPreferences("themePrefs", MODE_PRIVATE);
        boolean isDarkTheme = preferences.getBoolean("isDarkTheme", false);
        if (isDarkTheme) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme_Light);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //        for toggle button
        themeToggleButton = findViewById(R.id.themeToggleButton);
        themeToggleButton.setChecked(isDarkTheme);
        updateToggleButtonIcon(isDarkTheme);

        themeToggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save the theme preference
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isDarkTheme", isChecked);
            editor.apply();

            // Recreate the activity to apply the new theme
            recreate();
        });

//        other login code
        name = (EditText)findViewById(R.id.txtName);
        loc = (EditText)findViewById(R.id.txtLocation);
        desig = (EditText)findViewById(R.id.txtDesignation);
        saveBtn = (Button)findViewById(R.id.btnSave);

        saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = name.getText().toString();
                    String location = loc.getText().toString();
                    String designation = desig.getText().toString();
                    DbHandler dbHandler = new DbHandler(MainActivity.this);

                    if (username.length() > 0 && location.length() > 0 && designation.length() > 0) {
                        dbHandler.insertUserDetails(username, location, designation);
                        intent = new Intent(MainActivity.this, DetailsActivity.class);
                        Toast.makeText(getApplicationContext(), "Details Inserted Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "All fields are required..!!", Toast.LENGTH_SHORT).show();
                    }
                }
        });
    }

    private void updateToggleButtonIcon(boolean isDarkTheme) {
        if (isDarkTheme) {
            themeToggleButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_moon_foreground, 0, 0);
        } else {
            themeToggleButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_sun_foreground, 0, 0);
        }
    }
}