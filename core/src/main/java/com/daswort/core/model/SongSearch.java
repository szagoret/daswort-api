package com.daswort.core.model;

import com.daswort.core.entity.IdName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongSearch {
    private String name;
    private String categoryId;
    private Set<String> tagsIds = new HashSet<>();
    private Set<String> compositionsIds = new HashSet<>();
    private Set<String> instrumentsIds = new HashSet<>();
    private Set<String> difficultiesIds = new HashSet<>();
    private Set<String> topicsIds = new HashSet<>();
    private Set<String> authorsIds = new HashSet<>();

    private Set<IdName> melody = new HashSet<>();
    private Set<IdName> arrangement = new HashSet<>();
    private Set<IdName> adaptation = new HashSet<>();

    @NonNull
    private Integer page;
    @NonNull
    private Integer size;

    private String sortDirection = Sort.DEFAULT_DIRECTION.name();
    private String sortProperty = "name";

    public Optional<String> getName() {
        return ofNullable(name);
    }
    public Optional<String> getCategoryId() {
        return ofNullable(categoryId);
    }

}
