package com.matchskills.ia.service.entitys;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "Softskills")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SoftskillEntity {

    @Id
    private String id;
    private String name;
    private String description;
    private List<AnchorEntity> bars;
}