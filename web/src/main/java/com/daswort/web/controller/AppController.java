package com.daswort.web.controller;

import com.daswort.core.service.category.CategoryService;
import com.daswort.web.dto.breadcrumb.Breadcrumb;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.daswort.web.dto.breadcrumb.BreadcrumbBuilder.buildBreadcrumb;


@RestController
@RequestMapping("/api")
public class AppController {

    private final CategoryService categoryService;

    public AppController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("/breadcrumb/{categoryId}")
    public Breadcrumb getBreadcrumb(@PathVariable String categoryId) {
        return buildBreadcrumb(categoryService.computeCategoryTreePath(categoryId));
    }

}
