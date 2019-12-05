package com.daswort.core.service.category;

import com.daswort.core.entity.Category;
import org.bson.Document;

import java.util.Deque;
import java.util.List;
import java.util.Objects;

public class CategoryUtils {

    public static Category documentToEntity(Document document) {
        Objects.requireNonNull(document);
        return Category.builder()
                .id(document.getString("_id"))
                .name(document.getString("name"))
                .parentId(document.getString("parentId"))
                .build();
    }

    /**
     * Take an unordered list of Categories
     * and order them by linking reference
     *
     * @param categories
     * @param accumulator
     * @param leafId
     * @return
     */
    public static List<Category> sortListByFieldReference(List<Category> categories, Deque<Category> accumulator, String leafId) {
        Objects.requireNonNull(categories);
        Objects.requireNonNull(accumulator);
        Objects.requireNonNull(leafId);

        // find next sequence element
        Category nextCategory = categories.stream()
                .filter(category -> Objects.equals(leafId, category.getId()))
                .findFirst()
                .orElseThrow();

        // add element to path
        accumulator.addFirst(nextCategory);

        String parentId = nextCategory.getParentId();

        if (parentId == null) {
            return List.of(accumulator.toArray(new Category[]{}));
        }

        sortListByFieldReference(categories, accumulator, parentId);

        return List.of(accumulator.toArray(new Category[]{}));
    }
}
