package com.example.smartpantrymanagerjava;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TextView txtRecipeName;
    private TextView txtRecipeIngredients;
    private TextView txtRecipeSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        databaseHelper = new DatabaseHelper(this);

        txtRecipeName = findViewById(R.id.txtRecipeName);
        txtRecipeIngredients =findViewById(R.id.txtRecipeIngredients);
        txtRecipeSteps = findViewById(R.id.txtRecipeSteps);

        Button btnBack =findViewById(R.id.btnBackToRecipes);

        btnBack.setOnClickListener(v -> finish());

        int recipeId = getIntent().getIntExtra("recipe_id", -1);

        if (recipeId == -1) {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        loadRecipe(recipeId);
    }

    private void loadRecipe(int recipeId) {
        try (Cursor cursor =databaseHelper.getRecipeById(recipeId)) {

            if (!cursor.moveToFirst()) {
                Toast.makeText(this, "Recipe not found",Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));

            String steps = cursor.getString(cursor.getColumnIndexOrThrow("steps"));

            txtRecipeName.setText(name);
            txtRecipeSteps.setText(steps);
        }
        StringBuilder ingredients = new StringBuilder();

        try (Cursor cursor =databaseHelper.getRecipeIngredients(recipeId)) {

            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("ingredient_name"));

                double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow("quantity"));

                String unit = cursor.getString(cursor.getColumnIndexOrThrow("unit"));

                String amount;
                if (quantity == Math.floor(quantity)) {
                    amount = String.valueOf((long) quantity);
                } else {
                    amount = String.valueOf(quantity);
                }
                ingredients.append("• ")
                        .append(name)
                        .append(" - ")
                        .append(amount)
                        .append(" ")
                        .append(unit)
                        .append("\n");
            }
        }
        txtRecipeIngredients.setText(ingredients.toString().trim());
    }
}
