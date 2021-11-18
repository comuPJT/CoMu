package com.listener.comu.domain.music.api;

import com.listener.comu.domain.music.domain.Music;
import com.listener.comu.domain.music.domain.MusicRepository;
import com.listener.comu.domain.music.domain.SharePlaylistMusic;
import com.listener.comu.domain.music.domain.Status;
import com.listener.comu.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
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
    public void executeStreamingShell(long roomId, ListOperations<String, Object> listOps, HashOperations<String, Object, Object> operations, String musicName, String nowMusicKey, SharePlaylistMusic nowPlay) {
        nowPlay.setStatus(Status.READY);
        operations.put(nowMusicKey, "room:"+roomId , nowPlay);
        Runtime rt = Runtime.getRuntime();
        String cmd = "sh stream.sh " + roomId + " " + musicName;
        //    String cmd = "stream.bat " + roomId + " " + musicName;
        try {
            System.out.println("Streaming start...");
            Process pr = rt.exec(cmd);
            pr.waitFor();
            pr.destroy();
            nowPlay.setStatus(Status.DONE);
            operations.delete(nowMusicKey, "room:" + roomId);
            listOps.rightPush("roomPlayed:" +roomId , nowPlay);
            System.out.println("Streaming eneded...Asynchronously");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void executeDownloadAndUploadToS3(Music music){
        String cmd = "youtube-dl -f 160+140 -o src/main/resources/static/" + music.getSpotifyId() + ".%(ext)s " + music.getSource();
        Runtime rt = Runtime.getRuntime();
        try {
            Process pr = rt.exec(cmd);
            pr.waitFor();
            String sourceFilepath = "src/main/resources/static/" + music.getSpotifyId() + ".mp4";
            s3Uploader.dirUpload(new File(sourceFilepath),"static");
            pr.destroy();
            music.setOnCloud(1);
            musicRepository.save(music);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}