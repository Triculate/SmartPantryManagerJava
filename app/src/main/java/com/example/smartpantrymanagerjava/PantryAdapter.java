package com.example.smartpantrymanagerjava;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PantryAdapter extends ArrayAdapter<PantryItem> {

    public interface PantryActionListener {
        void onEdit(PantryItem item);
        void onDelete(PantryItem item);
    }

    private final Activity activity;
    private final PantryActionListener listener;

    public PantryAdapter(Activity activity,ArrayList<PantryItem> items,PantryActionListener listener) {
        super(activity, 0, items);
        this.activity = activity;
        this.listener = listener;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            row = LayoutInflater.from(activity).inflate(R.layout.item_pantry,parent, false);
        }
        PantryItem item = getItem(position);
        TextView txtName =row.findViewById(R.id.txtIngredientName);
        TextView txtQuantity =row.findViewById(R.id.txtIngredientQuantity);

        Button btnEdit =row.findViewById(R.id.btnEditItem);
        Button btnDelete = row.findViewById(R.id.btnDeleteItem);

        if (item != null) {
            txtName.setText(item.getName());
            txtQuantity.setText(item.getQuantityDisplay());

            btnEdit.setOnClickListener(v -> listener.onEdit(item));

            btnDelete.setOnClickListener(v -> listener.onDelete(item));
        }
        return row;
    }
}