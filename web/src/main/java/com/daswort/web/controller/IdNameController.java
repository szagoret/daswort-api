package com.daswort.web.controller;

import com.daswort.core.entity.IdName;
import com.daswort.core.entity.IdNameCollection;
import com.daswort.core.service.idname.IdNameService;
import com.daswort.core.service.song.SongUpdateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/filter")
public class IdNameController {

    private final IdNameService idNameService;
    private final SongUpdateService songUpdateService;

    public IdNameController(IdNameService idNameService,
                            SongUpdateService songUpdateService) {
        this.idNameService = idNameService;
        this.songUpdateService = songUpdateService;
    }

    @GetMapping("/{collection}/{itemId}")
    public IdName getIdNameById(@PathVariable IdNameCollection collection,
                                @PathVariable String itemId) {
        return idNameService.getById(collection, itemId);
    }

    @DeleteMapping("/{collection}/{itemId}")
    public ResponseEntity<?> removeItem(@PathVariable IdNameCollection collection,
                                        @PathVariable String itemId) {

        songUpdateService.removeSongField(collection, itemId);
        idNameService.removeIdNameItem(collection, itemId);

        return ResponseEntity.ok().build();
    }
}
