package com.daswort.web.common;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@Builder
public class IdTitleDto {
    String id;
    @NotNull
    String title;
}
