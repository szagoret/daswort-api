package com.daswort.core.service.category;

import com.daswort.core.entity.Category;
import com.daswort.core.entity.Song;
import com.daswort.core.exception.CategoryNotFoundException;
import com.daswort.core.exception.CategoryReferenceException;
import com.daswort.core.repository.CategoryRepository;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GraphLookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.mongodb.core.FindAndReplaceOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MongoOperations mongoOperations;

    public CategoryService(CategoryRepository categoryRepository, MongoOperations mongoOperations) {
        this.categoryRepository = categoryRepository;
        this.mongoOperations = mongoOperations;
    }

    public Optional<Category> findById(String categoryId) {
        requireNonNull(categoryId);
        return categoryRepository.findById(categoryId);
    }

    public List<Category> findChildrenCategories(String categoryId) {
        return categoryRepository.findCategoriesByParentId(categoryId);
    }

    public Optional<Category> getParentCategory(String categoryId) {
        requireNonNull(categoryId);
        return categoryRepository.findById(categoryId)
                .map(Category::getParentId)
                .flatMap(categoryRepository::findById);
    }

    public List<Category> getCategoryParentTreePath(String categoryId) {
        if (categoryId == null) {
            return List.of();
        }

        MatchOperation filter = Aggregation.match(where("_id").is(categoryId));

        GraphLookupOperation graphLookupOperation = GraphLookupOperation.builder()
                .from("category")
                .startWith("$parentId")
                .connectFrom("parentId")
                .connectTo("_id")
                .as("treePath");


        Aggregation aggregation = Aggregation.newAggregation(filter, graphLookupOperation);

        Document result = mongoOperations.aggregate(aggregation, "category", Document.class).getUniqueMappedResult();


        if (result != null) {
            Category targetCategory = CategoryUtils.documentToEntity(result);

            List<Document> treePath = result.getList("treePath", Document.class, Collections.emptyList());

            List<Category> categoryList = treePath.stream().map(CategoryUtils::documentToEntity).collect(toList());
            categoryList.add(targetCategory);

            return CategoryUtils.sortCategoryListByFieldReference(categoryList, new ArrayDeque<>(), targetCategory.getId());

        }
        return Collections.emptyList();
    }


    public Category updateCategory(Category updateCategory, String categoryId) {
        requireNonNull(updateCategory);
        requireNonNull(categoryId);
        final var category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);

        // check if parent id is a valid reference
        Optional<Category> parentCategory = ofNullable(updateCategory.getParentId())
                .flatMap(categoryRepository::findById);
        if (parentCategory.isPresent()) {
            parentCategory.map(Category::getId).ifPresent(category::setParentId);
        } else if (updateCategory.getParentId() == null) {
            category.setParentId(null);
        } else {
            throw new CategoryReferenceException();
        }

        ofNullable(updateCategory.getName()).ifPresent(category::setName);

        mongoOperations.update(Category.class)
                .matching(query(where("id").is(category.getId())))
                .replaceWith(category)
                .withOptions(options().upsert().returnNew())
                .as(Category.class)
                .findAndReplaceValue();

        mongoOperations.update(Song.class)
                .matching(query(where("category._id").is(category.getId())))
                .apply(new Update().set("category", category))
                .all();

        return category;
    }

    public Category createCategory(Category createCategory) {
        requireNonNull(createCategory);
        final var category = categoryRepository.save(new Category());
        return updateCategory(createCategory, category.getId());
    }

    public void deleteCategory(String categoryId) {
        requireNonNull(categoryId);
        final var existsCategoryReferences = mongoOperations.exists(query(where("category._id").is(categoryId)), Song.class);
        if (existsCategoryReferences) {
            throw new CategoryReferenceException();
        } else {
            categoryRepository.deleteById(categoryId);
        }
    }
}
