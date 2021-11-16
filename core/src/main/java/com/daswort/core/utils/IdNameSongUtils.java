package com.daswort.core.utils;

import com.daswort.core.annotation.CollectionName;
import com.daswort.core.entity.IdNameCollection;
import com.daswort.core.song.domain.Song;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class IdNameSongUtils {

    public static Optional<String> getFieldName(IdNameCollection collection) {
        requireNonNull(collection);
        return findAnnotatedField(collection).map(Field::getName);
    }

    public static Optional<Class<?>> getFieldType(IdNameCollection collection) {
        requireNonNull(collection);
        return findAnnotatedField(collection).map(Field::getType);
    }

    public static Optional<Field> findAnnotatedField(IdNameCollection collection) {
        requireNonNull(collection);
        return Stream.of(FieldUtils.getFieldsWithAnnotation(Song.class, CollectionName.class))
                .filter(field -> field.getAnnotation( CollectionName.class).value().equals(collection.getName()))
                .findFirst();
    }
}
