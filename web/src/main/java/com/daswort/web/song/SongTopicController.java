package com.daswort.web.song;

import com.daswort.core.song.application.TopicService;
import com.daswort.core.song.domain.Topic;
import com.daswort.core.song.repository.TopicRepository;
import com.daswort.web.common.IdTitleDto;
import com.daswort.web.http.UpdateResultHttpResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = {"https://noav.vercel.app", "http://localhost:3000"})
@RestController
@RequestMapping("song/topics")
@RequiredArgsConstructor
public class SongTopicController {
    private final TopicService topicService;
    private final TopicRepository topicRepository;

    @GetMapping("{topicId}")
    public ResponseEntity<?> getById(@PathVariable("topicId") String topicId) {
        return ResponseEntity.of(topicRepository.findById(topicId));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(topicRepository.findAll());
    }

    @DeleteMapping("{topicId}")
    public ResponseEntity<?> removeTopic(@PathVariable("topicId") String topicId) {
        return UpdateResultHttpResponseMapper.toResponse(topicService.removeTopic(topicId));
    }

    @PostMapping
    public ResponseEntity<?> saveTopic(@RequestBody @Valid IdTitleDto topic) {
        topicService.saveTopic(new Topic(topic.getId(), topic.getTitle()));
        return ResponseEntity.ok().build();
    }
}
