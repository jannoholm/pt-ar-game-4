package com.playtech.ptargame4.server.web.model;

import com.playtech.ptargame4.server.database.model.Occasion;

public class OccasionWrapper {
    private final int id;
    private final String description;

    public OccasionWrapper(Occasion occasion) {
        this.id = occasion.getOccasionId();
        this.description = occasion.getDescription();
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
