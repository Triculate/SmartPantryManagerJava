package com.example.smartpantrymanagerjava;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PantryActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ListView listPantry;
    private Button btnAddIngredient;

    private ArrayList<PantryItem> pantryItems;
    private PantryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        databaseHelper = new DatabaseHelper(this);

        listPantry = findViewById(R.id.listPantry);
        btnAddIngredient =findViewById(R.id.btnAddIngredient);

        pantryItems = new ArrayList<>();
        adapter = new PantryAdapter(
                this,pantryItems,new PantryAdapter.PantryActionListener() {
            @Override
            public void onEdit(PantryItem item) {
                editIngredient(item);
            }
            @Override
            public void onDelete(PantryItem item) {
                confirmDelete(item);
            }
        }
        );
        listPantry.setAdapter(adapter);

        btnAddIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(PantryActivity.this,AddEditIngredientActivity.class);
            startActivity(intent);
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadIngredients();
    }
    private void loadIngredients() {
        pantryItems.clear();

        try (Cursor cursor =databaseHelper.getAllIngredients()) {

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow("quantity"));
                String unit = cursor.getString(cursor.getColumnIndexOrThrow("unit"));
                String expiry = cursor.getString(cursor.getColumnIndexOrThrow("expiry_date"));
                pantryItems.add(new PantryItem(id, name, quantity, unit,getExpiryWarning(expiry)));
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void editIngredient(PantryItem item) {
        Intent intent = new Intent(this, AddEditIngredientActivity.class);

        intent.putExtra("ingredient_id", item.getId());
        startActivity(intent);
    }

    private void confirmDelete(PantryItem item) {
        new AlertDialog.Builder(this).setTitle("Delete Ingredient").setMessage("Delete " + item.getName() + "?").setPositiveButton("Delete", (dialog, which) -> {
                    boolean deleted = databaseHelper.deleteIngredient(item.getId());
                    if (deleted) {
                        loadIngredients();
                        Toast.makeText(this,"Ingredient deleted",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Unable to delete ingredient",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null).show();
    }
    private String getExpiryWarning(String expiryDate) {
        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            return "";
        }

        SharedPreferences preferences =getSharedPreferences("SmartPantrySettings", MODE_PRIVATE);
        if (!preferences.getBoolean("expiry_alerts", true)) {
            return "";
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            format.setLenient(false);

            Date expiry = format.parse(expiryDate.trim());
            if (expiry == null) return "";

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            Calendar expiryCalendar = Calendar.getInstance();
            expiryCalendar.setTime(expiry);

            Calendar expiryDay = Calendar.getInstance();
            expiryDay.clear();
            expiryDay.set(expiryCalendar.get(Calendar.YEAR), expiryCalendar.get(Calendar.MONTH), expiryCalendar.get(Calendar.DAY_OF_MONTH));

            long days = (expiryDay.getTimeInMillis() - today.getTimeInMillis()) / 86400000L;

            if (days < 0) {
                return " [EXPIRED]";
            } else if (days == 0) {
                return " [EXPIRES TODAY]";
            } else if (days <= 3) {
                return " [EXPIRING SOON]";
            }
        } catch (ParseException e) {
            return "";
        }
        return "";
    }
}