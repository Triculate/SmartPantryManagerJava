package com.example.smartpantrymanagerjava;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Locale;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText editName, editQuantity, editExpiry;
    private Spinner spinnerUnit;
    private DatabaseHelper databaseHelper;
    private int ingredientId = -1;

    private final String[] units = {
            "pcs", "g", "kg", "ml", "L"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        databaseHelper = new DatabaseHelper(this);

        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        editExpiry = findViewById(R.id.editExpiry);
        spinnerUnit = findViewById(R.id.spinnerUnit);

        Button btnSave = findViewById(R.id.btnSaveIngredient);
        TextView title = findViewById(R.id.txtFormTitle);

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,units
        );
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(unitAdapter);

        editExpiry.setOnClickListener(v -> showDatePicker());

        ingredientId = getIntent().getIntExtra("ingredient_id", -1);

        if (ingredientId != -1) {
            title.setText("Edit Ingredient");
            loadIngredient();
        }
        btnSave.setOnClickListener(v -> saveIngredient());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        new DatePickerDialog(this, (view, year, month, day) -> {
            String date = String.format(Locale.US, "%04d-%02d-%02d",year, month + 1, day);editExpiry.setText(date);
        }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void loadIngredient() {
        try (Cursor cursor =databaseHelper.getIngredientById(ingredientId)) {

            if (cursor.moveToFirst()) {
                editName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));

                editQuantity.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow("quantity"))));
                String unit = cursor.getString(cursor.getColumnIndexOrThrow("unit"));

                for (int i = 0; i < units.length; i++) {
                    if (units[i].equals(unit)) {
                        spinnerUnit.setSelection(i);
                        break;
                    }
                }
                String expiry = cursor.getString(cursor.getColumnIndexOrThrow("expiry_date"));
                editExpiry.setText(expiry == null ? "" : expiry);
            }
        }
    }

    private void saveIngredient() {
        String name = editName.getText().toString().trim();
        String quantityText =editQuantity.getText().toString().trim();
        String unit = spinnerUnit.getSelectedItem().toString();
        String expiry = editExpiry.getText().toString().trim();

        if (name.isEmpty()) {
            editName.setError("Enter an ingredient name");
            editName.requestFocus();
            return;
        }

        if (quantityText.isEmpty()) {
            editQuantity.setError("Enter a quantity");
            editQuantity.requestFocus();
            return;
        }
        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            editQuantity.setError("Invalid quantity");
            return;
        }
        if (!Double.isFinite(quantity) || quantity <= 0) {
            editQuantity.setError("Quantity must be greater than zero");
            return;
        }
        boolean success;

        if (ingredientId == -1) {
            success = databaseHelper.addIngredient(name, quantity, unit, expiry);
        } else {
            success = databaseHelper.updateIngredient(ingredientId, name, quantity, unit, expiry);
        }

        if (success) {
            Toast.makeText(this, "Ingredient saved!",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Unable to save ingredient",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
