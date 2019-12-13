package com.daswort.core.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class File {
    @Id
    private String id;
    private String name;
    private String fileCode;
    private String thumbnailSmCode;
    private String thumbnailLgCode;
    private Long size;

}
