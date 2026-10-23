
package com.example.smartpantrymanagerjava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnViewPantry =findViewById(R.id.btnViewPantry);
        btnViewPantry.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,PantryActivity.class);

            startActivity(intent);
        });
        Button btnRecipes = findViewById(R.id.btnRecipes);
        btnRecipes.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, SuggestedRecipesActivity.class);
        startActivity(intent);
        });

        Button btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

    }
}
