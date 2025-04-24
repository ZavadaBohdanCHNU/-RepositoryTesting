package org.example.repositorytesting.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "items")
public class User {
    @Id
    private String id;
    private String name;
    private String code;
    private String description;
}
