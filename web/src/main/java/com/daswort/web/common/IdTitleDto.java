package com.daswort.web.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class IdTitleDto {
    private String id;
    @NotNull
    private String title;

    @Builder
    public IdTitleDto(String id, String title) {
        this.id = id;
        this.title = title;
    }
}
