
package com.example.smartpantrymanagerjava;

public class PantryItem {

    private final int id;
    private final String name;
    private final double quantity;
    private final String unit;
    private final String expiryWarning;

    public PantryItem(int id, String name,
                      double quantity, String unit,
                      String expiryWarning) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryWarning = expiryWarning;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getQuantityDisplay() {
        return quantity + " " + unit + expiryWarning;
    }
}
