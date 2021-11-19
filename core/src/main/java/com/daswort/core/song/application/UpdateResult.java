package com.daswort.core.song.application;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateResult {
    UpdateResultType resultType;

    public static UpdateResult error() {
        return new UpdateResult(UpdateResultType.ERROR);
    }

    public static UpdateResult success() {
        return new UpdateResult(UpdateResultType.SUCCESS);
    }

    public enum UpdateResultType {
        SUCCESS, ERROR;
    }

}
