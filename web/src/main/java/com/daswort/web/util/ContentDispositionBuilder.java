package com.daswort.web.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContentDispositionBuilder {

    private String filename;

    @Override
    public String toString() {
        return "attachment; filename=\"" + filename + "\"";
    }

}