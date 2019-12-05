package com.daswort.core.service.category;

import com.daswort.core.entity.Category;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GraphLookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
public class CategoryService {

    private final MongoOperations mongoOperations;

    public CategoryService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Transactional
    public List<Category> computeCategoryTreePath(String categoryId) {
        Objects.requireNonNull(categoryId);

        MatchOperation filter = Aggregation.match(Criteria.where("_id").is(categoryId));

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

            return CategoryUtils.sortListByFieldReference(categoryList, new ArrayDeque<>(), targetCategory.getId());

        }
        return Collections.emptyList();
    }


}
