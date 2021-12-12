package com.listener.comu.domain.music.api;

import com.listener.comu.domain.music.domain.Music;
import com.listener.comu.domain.music.domain.MusicRepository;
import com.listener.comu.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class StreamingService {

    private final S3Uploader s3Uploader;
    private final MusicRepository musicRepository;

    @Async
    public void executeDownloadAndUploadToS3(Music music){
        String cmd = "youtube-dl -f 160+140 -o " + music.getSpotifyId() + ".%(ext)s " + music.getSource();
//        String cmd = "bash -c \"youtube-dl -f 160+140 -o " + music.getSpotifyId() + ".%(ext)s " + music.getSource() + "\"";
        Runtime rt = Runtime.getRuntime();
        try {
            Process pr = rt.exec(cmd);
            pr.waitFor();
            pr.destroy();
            String sourceFilepath = music.getSpotifyId() + ".mp4";
//            String sourceFilepath = "bash -c \"" + music.getSpotifyId() + ".mp4\"";
            s3Uploader.dirUpload(new File(sourceFilepath),"static");
            music.setOnCloud(1);
            musicRepository.save(music);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
