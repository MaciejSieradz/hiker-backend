package com.example.hiker.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@CompoundIndex(def = "{'userEmail': 1, 'trailId': 1}", unique = true)
@Data
public class SavedTrail {

    @Id
    private String id;
    private String userEmail;
    private String trailId;

    @CreatedDate
    private Instant savedAt;

    public SavedTrail(String userEmail, String trailId) {
        this.userEmail = userEmail;
        this.trailId = trailId;
    }
}
