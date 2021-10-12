package com.daswort.web.category;

import com.daswort.core.entity.Category;
import com.daswort.core.service.category.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin(origins = {"http://localhost:3000", "https://szagoret.github.io"})
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable String categoryId) {
        return ok(categoryService.findById(categoryId));
    }

    @GetMapping("/children")
    public List<Category> getChildrenCategories(@RequestParam(required = false) String categoryId) {
        return categoryService.findChildrenCategories(categoryId);
    }

    @GetMapping("/{categoryId}/parent")
    public ResponseEntity<?> getParentCategory(@PathVariable String categoryId) {
        final var parentCategory = categoryService.getParentCategory(categoryId);
        return ok(parentCategory);
    }

    @GetMapping("/{categoryId}/parent/tree")
    public List<Category> getCategoriesParentTree(@PathVariable String categoryId) {
        return categoryService.getCategoryParentTreePath(categoryId);
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody Category addCategory) {
        final var category = categoryService.createCategory(addCategory);
        return ok(category);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(@RequestBody Category updateCategory,
                                                   @PathVariable String categoryId) {
        final var category = categoryService.updateCategory(updateCategory, categoryId);
        return ok(category);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable String categoryId) {
        categoryService.deleteCategory(categoryId);

        return ok().build();
    }


}
