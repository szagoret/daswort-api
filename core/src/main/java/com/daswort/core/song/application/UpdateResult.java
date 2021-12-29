package com.daswort.core.song.application;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateResult {
    UpdateResultType resultType;
    String message;

    public static UpdateResult error(String message) {
        return new UpdateResult(UpdateResultType.ERROR, message);
    }

    public static UpdateResult success() {
        return new UpdateResult(UpdateResultType.SUCCESS, "");
    }

    public enum UpdateResultType {
        SUCCESS, ERROR;
    }

}
