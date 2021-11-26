package com.listener.comu.domain.music.api;

import com.listener.comu.domain.music.domain.Music;
import com.listener.comu.domain.music.domain.MusicRepository;
import com.listener.comu.domain.music.domain.SharePlaylistMusic;
import com.listener.comu.domain.music.domain.Status;
import com.listener.comu.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
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

        String cmd = "sh /home/stream.sh " + roomId + " " + musicName; // stream.sh가 위치한 절대경로를 쓸 것
        //    String cmd = "%userprofile%/stream.bat " + roomId + " " + musicName;
        try {
            System.out.println("Streaming start...");
            DefaultExecutor executor = new DefaultExecutor();
            ExecuteWatchdog watchdog = new ExecuteWatchdog(60000*6); //6 mins Timeout
            executor.setWatchdog(watchdog);
            DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

            CommandLine cmdLine = CommandLine.parse(cmd);
            executor.execute(cmdLine, resultHandler);
            resultHandler.waitFor();
            int exitCode = resultHandler.getExitValue();
            System.out.println("FFMPEG Streaming exited " + exitCode);

            operations.delete(nowMusicKey, "room:" + roomId);
            if( !nowPlay.getPlayId().equals("Anonymous")) {
                nowPlay.setStatus(Status.DONE);
                listOps.rightPush("roomPlayed:" +roomId , nowPlay);
            }
            System.out.println("Streaming eneded...Asynchronously");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

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
