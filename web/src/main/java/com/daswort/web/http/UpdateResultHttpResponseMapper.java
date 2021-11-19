package com.daswort.web.http;

import com.daswort.core.song.application.UpdateResult;
import org.springframework.http.ResponseEntity;

public class UpdateResultHttpResponseMapper {
    public static ResponseEntity<?> toResponse(UpdateResult updateResult) {
        if (updateResult.getResultType().equals(UpdateResult.UpdateResultType.SUCCESS)) {
            return ResponseEntity.ok().build();
        } else if (updateResult.getResultType().equals(UpdateResult.UpdateResultType.ERROR)) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.accepted().build();
        }
    }
}
