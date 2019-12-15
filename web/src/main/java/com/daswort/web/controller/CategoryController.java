package com.daswort.web.controller;

import com.daswort.core.entity.Category;
import com.daswort.core.service.category.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public List<Category> getChildrenCategories(@RequestParam(required = false) String categoryId) {
        return categoryService.findChildrenCategories(categoryId);
    }


}
