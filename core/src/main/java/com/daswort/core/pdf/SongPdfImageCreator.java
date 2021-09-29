package com.daswort.core.pdf;

import com.daswort.core.service.song.SongFileService;
import com.daswort.core.storage.FileResourceBytes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SongPdfImageCreator {
    private final PdfToImageRenderer pdfToImageRenderer;
    private final SongFileService songFileService;

    public void createPdfPreview(String songCode, String fileCode) {
        final var songFileOpt = songFileService.getSongFile(songCode, fileCode);
        songFileOpt.ifPresent(fileResource -> {
            try {
                final var imageOutputStream = pdfToImageRenderer.render(fileResource.getInputStream());
                final var fileResourceBytes = new FileResourceBytes(imageOutputStream.toByteArray(), fileCode, "application/octet-stream");
                songFileService.saveSongThumbnail(songCode, fileCode, fileResourceBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
