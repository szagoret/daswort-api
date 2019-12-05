package com.daswort.core.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class File {
    private String name;
    private String extension;
    private String fileCode;
    private String thumbnailSmCode;
    private String thumbnailLgCode;
    private Long size;
}
