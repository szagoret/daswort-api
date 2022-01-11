package com.daswort.web.song;

import com.daswort.core.song.repository.AuthorRepository;
import com.daswort.core.song.repository.InstrumentRepository;
import com.daswort.core.song.repository.TopicRepository;
import com.daswort.core.song.repository.VocalRepository;
import com.daswort.web.song.dto.SongProperties;
import lombok.RequiredArgsConstructor;
import org.assertj.core.util.Lists;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("song/properties")
@RequiredArgsConstructor
public class SongPropertiesController {
    private final InstrumentRepository instrumentRepository;
    private final TopicRepository topicRepository;
    private final VocalRepository vocalRepository;
    private final AuthorRepository authorRepository;

    @GetMapping
    public ResponseEntity<?> getSongProperties() {

        return ResponseEntity.ok(new SongProperties(
                Lists.newArrayList(instrumentRepository.findAll()),
                Lists.newArrayList(topicRepository.findAll()),
                Lists.newArrayList(vocalRepository.findAll()),
                Lists.newArrayList(authorRepository.findAll())
        ));
    }
}
