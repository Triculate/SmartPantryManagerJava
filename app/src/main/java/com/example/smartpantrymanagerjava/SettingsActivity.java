
package com.example.smartpantrymanagerjava;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchExpiryAlerts;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = getSharedPreferences("SmartPantrySettings", MODE_PRIVATE);

        switchExpiryAlerts = findViewById(R.id.switchExpiryAlerts);

        boolean alertsEnabled = preferences.getBoolean("expiry_alerts", true);

        switchExpiryAlerts.setChecked(alertsEnabled);

        //save preference when the switch changes.
        switchExpiryAlerts.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    preferences.edit() .putBoolean("expiry_alerts", isChecked) .apply();

                    Toast.makeText(this,isChecked ? "Warnings enabled" : "Warnings disabled", Toast.LENGTH_SHORT).show();
                });

        Button btnPantry =findViewById(R.id.btnSettingsPantry);
        Button btnRecipes =findViewById(R.id.btnSettingsRecipes);
        Button btnHome =findViewById(R.id.btnSettingsHome);

        btnPantry.setOnClickListener(v ->
                startActivity(new Intent( this, PantryActivity.class)));

        btnRecipes.setOnClickListener(v ->
                startActivity(new Intent(this, SuggestedRecipesActivity.class)));

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}
