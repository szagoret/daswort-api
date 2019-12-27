package com.daswort.web.controller;

import com.daswort.core.entity.Category;
import com.daswort.core.service.category.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/children")
    public List<Category> getChildrenCategories(@RequestParam(required = false) String categoryId) {
        return categoryService.findChildrenCategories(categoryId);
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody Category addCategory) {
        final var category = categoryService.createCategory(addCategory);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(@RequestBody Category updateCategory,
                                                   @PathVariable String categoryId) {
        final var category = categoryService.updateCategory(updateCategory, categoryId);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable String categoryId) {
        categoryService.deleteCategory(categoryId);

        return ResponseEntity.ok().build();
    }


}
