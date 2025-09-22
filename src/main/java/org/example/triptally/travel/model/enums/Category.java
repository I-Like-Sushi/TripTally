package org.example.triptally.travel.model.enums;

public enum Category {
    FOOD("Food & Dining"),
    TRANSPORT("Transport"),
    ACCOMMODATION("Accommodation"),
    ENTERTAINMENT("Entertainment"),
    SHOPPING("Shopping"),
    ACTIVITIES("Activities"),
    GIFT("Gift"),
    OTHER("Other");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}