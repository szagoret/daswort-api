package com.daswort.web.dto.search;

import com.daswort.web.dto.breadcrumb.Breadcrumb;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchDto {
    private Breadcrumb breadcrumb;
    private String result;
}
