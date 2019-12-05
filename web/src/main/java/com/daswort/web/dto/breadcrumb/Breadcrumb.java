package com.daswort.web.dto.breadcrumb;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Breadcrumb {
    private List<BreadcrumbItem> items;
}
