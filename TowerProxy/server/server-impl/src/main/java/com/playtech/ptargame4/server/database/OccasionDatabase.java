package com.playtech.ptargame4.server.database;

import com.playtech.ptargame4.server.database.model.Occasion;

import java.util.List;

public interface OccasionDatabase {
    Occasion createOccasion(String description);
    Occasion getOccasion(int occasionId);
    void updateOccasion(Occasion occasion);
    List<Occasion> listOccasions();
    Occasion getCurrentOccasion();
    void setCurrentOccasion(Occasion occasion);
}
