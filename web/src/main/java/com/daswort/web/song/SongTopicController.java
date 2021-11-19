package com.daswort.web.song;

import com.daswort.core.song.domain.Topic;
import com.daswort.core.song.repository.TopicRepository;
import com.daswort.web.common.IdTitleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = {"http://localhost:3000", "https://szagoret.github.io", "https://daswort.gitlab.io", "http://192.168.1.38:3000", "http://192.168.1.38:3000/daswort-ui"})
@RestController
@RequestMapping("song/topics")
@RequiredArgsConstructor
public class SongTopicController {
    private final TopicRepository topicRepository;

    @GetMapping("{topicId}")
    public ResponseEntity<?> findById(@PathVariable("topicId") String topicId) {
        return ResponseEntity.of(topicRepository.findById(topicId));
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(topicRepository.findAll());
    }

    @DeleteMapping("{topicId}")
    public ResponseEntity<?> removeTopic(@PathVariable("topicId") String topicId) {
        topicRepository.deleteById(topicId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> saveTopic(@RequestBody @Valid IdTitleDto topic) {
        topicRepository.save(new Topic(topic.getId(), topic.getTitle()));
        return ResponseEntity.ok().build();
    }
}
