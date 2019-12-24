package com.daswort.core.entity;

public enum IdNameCollection {
    composition(Constants.composition),
    difficulty(Constants.difficulty),
    instrument(Constants.instrument),
    partition(Constants.partition),
    tag(Constants.tag),
    topic(Constants.topic);

    private final String name;

    IdNameCollection(String name) {
        if (!name.equals(this.name())) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static class Constants {
        public static final String composition = "composition";
        public static final String difficulty = "difficulty";
        public static final String instrument = "instrument";
        public static final String partition = "partition";
        public static final String tag = "tag";
        public static final String topic = "topic";
    }
}
