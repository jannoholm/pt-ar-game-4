package com.playtech.ptargame4.server.database.model;

public class Occasion {
    private final int occasionId;
    private final String description;
    private final boolean hidden;

    public Occasion(int occasionId, String description, boolean isHidden) {
        this.occasionId = occasionId;
        this.description = description;
        this.hidden = isHidden;
    }

    public int getOccasionId() {
        return occasionId;
    }

    public String getDescription() {
        return description;
    }

    public boolean isHidden() {
        return hidden;
    }

    @Override
    public String toString() {
        return "Event{" +
                "occasionId=" + occasionId +
                ", description='" + description + '\'' +
                ", hidden='" + hidden + '\'' +
                '}';
    }
}
