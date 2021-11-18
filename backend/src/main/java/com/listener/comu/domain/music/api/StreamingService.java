package com.listener.comu.domain.music.api;

import com.listener.comu.domain.music.domain.SharePlaylistMusic;
import com.listener.comu.domain.music.domain.Status;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class StreamingService {

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
}
