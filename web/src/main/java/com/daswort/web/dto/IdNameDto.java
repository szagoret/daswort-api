package com.daswort.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IdNameDto {
    private String id;
    private String name;
}
