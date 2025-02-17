package org.example.Shop;

public enum PaymentMethod {
    BAR("Bar"),
    CARD("Card");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}