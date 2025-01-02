package com.example.hiker.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document("User")
@Data
public class User {

    @Id
    private String id;
    @Indexed(unique = true)
    private String email;
    private String username;
    private String avatarUrl;

    private Set<String> savedTrailIds;
}
