package com.example.sqlite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;

public class DetailsActivity extends AppCompatActivity {
    Intent intent;
    DbHandler db;
    ArrayList<HashMap<String, String>> userList;
    UserAdapter adapter;
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
        setContentView(R.layout.details);

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

        db = new DbHandler(this);
        userList = db.GetUsers();
        ListView lv = findViewById(R.id.user_list);
        adapter = new UserAdapter();
        lv.setAdapter(adapter);

        Button back = findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(DetailsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    class UserAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return userList.size();
        }

        @Override
        public Object getItem(int position) {
            return userList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.layout.list_row, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        TextView designation = convertView.findViewById(R.id.designation);
        TextView location = convertView.findViewById(R.id.location);
        Button delete = convertView.findViewById(R.id.btnDelete);

        final HashMap<String, String> user = userList.get(position);

        name.setText(user.get("name"));
        designation.setText(user.get("designation"));
        location.setText(user.get("location"));

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete the user from the database
                int userId = Integer.parseInt(user.get("id"));
                db.DeleteUser(userId);

                // Update the list
                userList.remove(position);
                notifyDataSetChanged();

                // Show a toast
                Toast.makeText(DetailsActivity.this, "User deleted", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    }
    private void updateToggleButtonIcon(boolean isDarkTheme) {
        if (isDarkTheme) {
            themeToggleButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_moon_foreground, 0, 0);
        } else {
            themeToggleButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_sun_foreground, 0, 0);
        }
    }
}
