package com.example.smartpantrymanagerjava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class SuggestedRecipesActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private ListView listSuggestedRecipes;
    private TextView txtNoRecipes;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> recipeNames;
    private ArrayList<DatabaseHelper.RecipeSuggestion> matchingRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_suggested_recipes);

        databaseHelper = new DatabaseHelper(this);

        listSuggestedRecipes =findViewById(R.id.listSuggestedRecipes);

        txtNoRecipes =findViewById(R.id.txtNoRecipes);

        recipeNames = new ArrayList<>();
        matchingRecipes = new ArrayList<>();

        //show recipe names in the ListView
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,recipeNames);

        listSuggestedRecipes.setAdapter(adapter);
        listSuggestedRecipes.setOnItemClickListener((parent, view, position, id) -> {
            int selectedRecipeId =matchingRecipes.get(position).id;

            Intent intent = new Intent(SuggestedRecipesActivity.this,RecipeDetailActivity.class);

            intent.putExtra("recipe_id", selectedRecipeId);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSuggestedRecipes();
    }

    private void loadSuggestedRecipes() {
        recipeNames.clear();
        matchingRecipes.clear();

        matchingRecipes.addAll(databaseHelper.getSuggestedRecipes());

        for (DatabaseHelper.RecipeSuggestion recipe: matchingRecipes) {
            recipeNames.add(recipe.name);
        }


        adapter.notifyDataSetChanged();

        if (matchingRecipes.isEmpty()) {
            txtNoRecipes.setVisibility(View.VISIBLE);
            listSuggestedRecipes.setVisibility(View.GONE);
        } else {
            txtNoRecipes.setVisibility(View.GONE);
            listSuggestedRecipes.setVisibility(View.VISIBLE);
        }
    }
}
