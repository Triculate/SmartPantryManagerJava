package com.example.smartpantrymanagerjava;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_PANTRY = "pantry_items";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //pantry table
        String createPantry ="CREATE TABLE pantry_items (" +"id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT NOT NULL, " + "quantity REAL NOT NULL, " + "unit TEXT NOT NULL, " + "expiry_date TEXT)";
        db.execSQL(createPantry);

        //recipe tables and insert starter recipes
        createRecipeTables(db);
        seedRecipes(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion) {
        if(oldVersion < 2){
            createRecipeTables(db);
            seedRecipes(db);
        }

    }
    public boolean addIngredient(String name, double quantity, String unit,String expiryDate) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", name.trim());
        values.put("quantity", quantity);
        values.put("unit", unit.trim());
        values.put("expiry_date", expiryDate);

        long result = db.insert(TABLE_PANTRY, null, values);
        return result != -1;
    }

    public Cursor getAllIngredients() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM pantry_items ORDER BY name ASC", null);
    }


    public Cursor getIngredientById(int id) {

        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery("SELECT * FROM pantry_items WHERE id= ?",new String[]{String.valueOf(id)});
    }

    public boolean updateIngredient(int id, String name, double quantity, String unit, String expiryDate) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name.trim());
        values.put("quantity", quantity);
        values.put("unit", unit.trim());
        values.put("expiry_date", expiryDate);

        int rowsAffected = db.update(TABLE_PANTRY, values, "id = ?", new String[]{String.valueOf(id)}
        );

        return rowsAffected > 0;
    }
    public boolean deleteIngredient(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_PANTRY, "id = ?",new String[]{String.valueOf(id)}
        );
        return rowsDeleted > 0;
    }
    private void createRecipeTables(SQLiteDatabase db) {
        String createRecipes ="CREATE TABLE IF NOT EXISTS recipes (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT NOT NULL, " + "steps TEXT NOT NULL)";
        db.execSQL(createRecipes);
        String createIngredients ="CREATE TABLE IF NOT EXISTS recipe_ingredients (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "recipe_id INTEGER NOT NULL, " + "ingredient_name TEXT NOT NULL, " + "quantity REAL NOT NULL, " + "unit TEXT NOT NULL, " + "FOREIGN KEY(recipe_id) REFERENCES recipes(id))";
        db.execSQL(createIngredients);
    }
    private void addRecipe(SQLiteDatabase db,String name,String steps,String... requirements) {
        ContentValues recipeValues = new ContentValues();
        recipeValues.put("name", name);
        recipeValues.put("steps", steps);
        long recipeId = db.insertOrThrow("recipes", null, recipeValues);

        for (String requirement : requirements) {
            String[] parts = requirement.split(":");
            ContentValues ingredientValues = new ContentValues();
            ingredientValues.put("recipe_id", recipeId);
            ingredientValues.put("ingredient_name", parts[0]);
            ingredientValues.put("quantity", Double.parseDouble(parts[1]));
            ingredientValues.put("unit", parts[2]);
            db.insertOrThrow("recipe_ingredients", null, ingredientValues);
        }
    }
    private void seedRecipes(SQLiteDatabase db) {
        //Homemade Chakalaka
        addRecipe(db, "Homemade Chakalaka","Chop cabbage, carrot, onion and tomato. " + "Heat oil, cook the onion, add vegetables and curry powder. " +"Cook until tender.","cabbage:150:g", "carrot:2:pcs","onion:1:pcs", "tomato:2:pcs","oil:20:ml", "curry powder:5:g");
        //Traditional Pap
        addRecipe(db, "Traditional Pap","Boil water. Gradually stir in maize meal. " +"Cover and cook, stirring occasionally until ready.","maize meal:200:g", "water:500:ml");
        //Tomato and Onion Sheba
        addRecipe(db, "Tomato and Onion Sheba","Chop tomato and onion. Fry onion in oil, " + "add tomato and simmer until thick.","tomato:3:pcs", "onion:1:pcs","oil:15:ml");
        //Cabbage and Potato Pot
        addRecipe(db, "Cabbage and Potato Pot","Chop vegetables. Cook onion in oil, add potato, " +"cabbage and water. Simmer until soft.","cabbage:200:g", "potato:2:pcs","onion:1:pcs", "oil:15:ml","water:250:ml");
        //Quick Vetkoek
        addRecipe(db, "Quick Vetkoek","Mix flour, baking powder and salt. Add water " +"to make dough. Shape and fry in hot oil until " +"golden and cooked through.","flour:250:g", "baking powder:10:g","water:150:ml", "salt:3:g","oil:250:ml");
        //Creamy Maize Porridge
        addRecipe(db, "Creamy Maize Porridge", "Heat milk, stir in maize meal and cook gently. " +"Add sugar and stir until creamy.","maize meal:60:g", "milk:300:ml","sugar:15:g");
        //Spicy Egg and Tomato Skillet
        addRecipe(db, "Egg and Tomato Skillet","Cook chopped onion and tomato in oil. " +"Add eggs and cook until completely set.","egg:2:pcs", "tomato:2:pcs", "onion:1:pcs", "oil:15:ml");
        //Golden Potato Hash
        addRecipe(db, "Golden Potato Hash","Dice potatoes and onion. Fry in oil over " +"medium heat until potatoes are cooked and golden.","potato:3:pcs", "onion:1:pcs","oil:25:ml");
        //Student Budget Fried Rice
        addRecipe(db, "Student Budget Fried Rice","Heat oil in a pan. Cook carrot, add egg and " +"scramble until set. Add cooked rice and heat through.","cooked rice:200:g", "egg:1:pcs","carrot:1:pcs", "oil:15:ml");
        //Leftover Rice Pancakes
        addRecipe(db, "Leftover Rice Pancakes","Mix cooked rice, egg and flour into a thick " +"mixture. Shape small pancakes and fry until cooked.","cooked rice:150:g", "egg:1:pcs", "flour:40:g", "oil:20:ml");
        //Creamy Spinach
        addRecipe(db, "Creamy Spinach","Cook spinach until tender. Melt butter, stir " +"in flour and milk to make a sauce. Add spinach.","spinach:200:g", "milk:150:ml","flour:20:g", "butter:20:g");
        //Peanut Butter Banana bread
        addRecipe(db, "Peanut Butter Banana Toast","Toast bread in a dry pan. Spread peanut butter " + "on top and add sliced banana.", "bread:2:pcs", "peanut butter:30:g", "banana:1:pcs");
        //Cinnamon Apple Oats
        addRecipe(db, "Cinnamon Apple Oats","Cook oats in milk until soft. Add chopped apple " +"and cinnamon and stir.","oats:50:g", "apple:1:pcs","cinnamon:2:g", "milk:200:ml");
        //rainbow Vegetable Stir-Fry
        addRecipe(db, "Rainbow Vegetable Stir-Fry","Slice cabbage, carrot and onion. Stir-fry " + "in hot oil until tender.","cabbage:150:g", "carrot:2:pcs","onion:1:pcs", "oil:20:ml");
        //spiced Potato Wedges
        addRecipe(db, "Spiced Potato Wedges","Cut potatoes into wedges. Mix with oil and " +"paprika. Bake until golden and cooked through.","potato:3:pcs", "paprika:5:g", "oil:20:ml");
        //tomato Rice Bowl
        addRecipe(db, "Tomato Rice Bowl","Cook chopped onion and tomato in oil. " +"Add cooked rice and stir until heated through.", "cooked rice:200:g", "tomato:2:pcs","onion:1:pcs", "oil:15:ml");
        //banana Oat Pancakes
        addRecipe(db, "Banana Oat Pancakes","Mash banana, mix with oats and beaten egg. " +"Cook small pancakes in oil until fully cooked.","banana:1:pcs", "oats:50:g","egg:2:pcs", "oil:15:ml");
        //homemade Potato Rosti
        addRecipe(db, "Homemade Potato Rosti","Grate potato and onion, squeeze out moisture, " + "mix with egg and fry until cooked and golden.", "potato:2:pcs", "egg:1:pcs","onion:1:pcs", "oil:20:ml");
        //sweet Cinnamon French Toast
        addRecipe(db, "Sweet Cinnamon French Toast","Beat egg with milk and cinnamon. Dip bread " +"into mixture and fry in oil until cooked.","bread:2:pcs", "egg:1:pcs","milk:50:ml", "cinnamon:2:g","oil:15:ml");
    }

    public static class RecipeSuggestion {
        public final int id;
        public final String name;

        public RecipeSuggestion(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    private String normalizeName(String name) {
        String result = name.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");

        switch (result) {
            case "cooking oil": case "vegetable oil": return "oil";
            case "eggs": return "egg";
            case "tomatoes": return "tomato";
            case "potatoes": return "potato";
            case "carrots": return "carrot";
            case "bananas": return "banana";
            case "apples": return "apple";
            case "onions": return "onion";
            case "strawberries": return "strawberry";
            case "loaves": return "loaf";
            default:
                if (result.endsWith("s") && !result.endsWith("ss") && !result.endsWith("us") && !result.endsWith("is") && !result.equals("oats")) {
                    return result.substring(0, result.length() - 1);
                }
                return result;
        }
    }

    private String unitCategory(String unit) {
        String u = unit.trim().toLowerCase(Locale.ROOT);
        if (u.equals("kg") || u.equals("g")) {
            return "mass";
        }
        if (u.equals("l") || u.equals("ml")) {
            return "volume";
        }
        if (u.equals("pcs") || u.equals("pc") || u.equals("piece") || u.equals("pieces")) {
            return "pieces";
        }
        return "unknown";
    }

    private double baseQuantity(double quantity, String unit) {
        String u = unit.trim().toLowerCase(Locale.ROOT);

        if (u.equals("kg") || u.equals("l")) {
            return quantity * 1000.0;
        }
        return quantity;
    }

    private String ingredientKey(String name, String unit) {
        return normalizeName(name) + "|" + unitCategory(unit);
    }
    public ArrayList<RecipeSuggestion> getSuggestedRecipes() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<RecipeSuggestion> suggestions =new ArrayList<>();

        Map<String, Double> pantry = new HashMap<>();

        try (Cursor cursor = db.rawQuery("SELECT name, quantity, unit FROM pantry_items", null)) {

            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                double quantity = cursor.getDouble(1);
                String unit = cursor.getString(2);

                if (quantity <= 0) continue;

                String category = unitCategory(unit);

                if (category.equals("unknown")) continue;
                String key = ingredientKey(name, unit);
                double amount = baseQuantity(quantity, unit);

                pantry.put(key,pantry.getOrDefault(key, 0.0) + amount);
            }
        }
        //every recipe is read
        try (Cursor recipes = db.rawQuery("SELECT id, name FROM recipes ORDER BY name", null)) {

            while (recipes.moveToNext()) {
                int recipeId = recipes.getInt(0);
                String recipeName = recipes.getString(1);
                boolean canMake = true;
                boolean hasRequirements = false;

                try (Cursor required = db.rawQuery("SELECT ingredient_name, quantity, unit " + "FROM recipe_ingredients WHERE recipe_id = ?",
                        new String[]{String.valueOf(recipeId)})) {
                    Map<String, Double> needed = new HashMap<>();

                    while (required.moveToNext()) {
                        hasRequirements = true;
                        String name = required.getString(0);
                        double quantity = required.getDouble(1);
                        String unit = required.getString(2);
                        String category = unitCategory(unit);
                        if (category.equals("unknown") || quantity <= 0) {
                            canMake = false;
                            break;
                        }
                        String key = ingredientKey(name, unit);
                        needed.put(key, needed.getOrDefault(key, 0.0) + baseQuantity(quantity, unit));
                    }

                    if (canMake) {
                        for (Map.Entry<String, Double> entry: needed.entrySet()) {
                            double available = pantry.getOrDefault(entry.getKey(), 0.0);
                            if (available + 0.000001< entry.getValue()) {
                                canMake = false;
                                break;
                            }
                        }
                    }
                }
                if (canMake && hasRequirements) {
                    suggestions.add(new RecipeSuggestion(recipeId, recipeName));
                }
            }
        }
        return suggestions;
    }
    public Cursor getRecipeById(int recipeId) {
        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery("SELECT id, name, steps FROM recipes WHERE id = ?",new String[]{String.valueOf(recipeId)});
    }

    public Cursor getRecipeIngredients(int recipeId) {
        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery("SELECT ingredient_name, quantity, unit " +"FROM recipe_ingredients WHERE recipe_id = ? " +"ORDER BY id ASC",new String[]{String.valueOf(recipeId)});
    }


}
