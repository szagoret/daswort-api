package com.daswort.core.song.application;

import com.daswort.core.song.domain.Topic;
import com.daswort.core.song.domain.UpdateErrorMessage;
import com.daswort.core.song.domain.UpdateResult;
import com.daswort.core.song.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;
    private final SongService songService;

    public void saveTopic(Topic topic) {
        topicRepository.save(topic);
        songService.updateRef(topic);
    }

    public UpdateResult removeTopic(String id) {
        if (!ObjectId.isValid(id)) {
            return UpdateResult.error(UpdateErrorMessage.INVALID_OBJECT_ID);
        }
        final var hasReferences = topicRepository.findById(id)
                .map(songService::isReferencedBy)
                .orElse(false);
        if (hasReferences) {
            return UpdateResult.error(UpdateErrorMessage.REFERENCE_CONSTRAINT_ERROR);
        } else {
            topicRepository.deleteById(id);
            return UpdateResult.success();
        }
    }

}
