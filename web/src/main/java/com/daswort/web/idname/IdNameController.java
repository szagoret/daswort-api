package com.daswort.web.idname;

import com.daswort.core.entity.IdName;
import com.daswort.core.entity.IdNameCollection;
import com.daswort.core.service.idname.IdNameService;
import com.daswort.core.service.song.SongUpdateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

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

    @PutMapping("/{collection}/{itemId}")
    public ResponseEntity<IdName> updateIdName(@PathVariable IdNameCollection collection,
                                               @PathVariable String itemId,
                                               @RequestBody IdName updateIdName) {
        final var idName = idNameService.updateIdName(collection, itemId, updateIdName);
        songUpdateService.updateSongField(collection, idName);
        return ok(idName);
    }

    @DeleteMapping("/{collection}/{itemId}")
    public ResponseEntity<?> removeItem(@PathVariable IdNameCollection collection,
                                        @PathVariable String itemId) {

        songUpdateService.removeSongField(collection, itemId);
        idNameService.removeIdNameItem(collection, itemId);
        return ok().build();
    }
}
