package com.daswort.web.dto.breadcrumb;

import com.daswort.core.entity.Category;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class BreadcrumbBuilder {

    public static Breadcrumb buildBreadcrumb(List<Category> categories) {
        List<BreadcrumbItem> breadcrumbItems = categories.stream()
                .map(category ->
                        BreadcrumbItem.builder()
                                .id(category.getId())
                                .value(category.getName())
                                .build())
                .collect(toList());
        return Breadcrumb.builder().items(breadcrumbItems).build();
    }

}
