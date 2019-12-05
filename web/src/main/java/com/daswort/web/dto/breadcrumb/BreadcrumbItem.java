package com.daswort.web.dto.breadcrumb;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BreadcrumbItem {
    private String id;
    private String value;
}
