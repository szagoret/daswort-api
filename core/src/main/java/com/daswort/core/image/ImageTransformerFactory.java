package com.daswort.core.image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ImageTransformerFactory {
    private final List<ImageTransformer> imageTransformers;
    private Map<TransformationType, ImageTransformer> imageTransformerMap;

    public ImageTransformer get(TransformationType transformationType) {
        return imageTransformerMap.get(transformationType);
    }

    @PostConstruct
    public void postConstruct() {
        imageTransformerMap = imageTransformers.stream().collect(Collectors.toMap(ImageTransformer::imageTransformerType, Function.identity()));
    }
}
