package com.listener.comu.domain.music.domain;

import com.listener.comu.domain.music.dto.SharePlaylistMusicRes;
import com.listener.comu.domain.oauthlogin.api.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "history")
@Getter
@Setter
@SqlResultSetMapping(
        name="HistoryResponseMapping",
        classes = @ConstructorResult(
                targetClass = SharePlaylistMusicRes.class,
                columns = {
                        @ColumnResult(name="id", type = String.class),
                        @ColumnResult(name="title", type = String.class),
                        @ColumnResult(name="contents", type = String.class),
                        @ColumnResult(name="timestamp", type = LocalDateTime.class),
                        @ColumnResult(name="name", type = String.class),
                        @ColumnResult(name="singer", type = String.class),
                        @ColumnResult(name="username", type = String.class),
                        @ColumnResult(name="likes", type = String.class),
                }
        )
)
@NamedNativeQueries(
        @NamedNativeQuery(
                name="getHistoryMusicsByRoomId",
                query="SELECT history.id, title, contents, timestamp, name,likes FROM history WHERE history.id = :roomId",
                resultSetMapping = "HistoryResponseMapping"
        ),
        @NamedNativeQuery(
                name="getHistoryMusicById",
                query="SELECT history.id, title, contents, timestamp, name, singer,username,likes FROM history, user, music WHERE history.id = :id",
                resultSetMapping = "HistoryResponseMapping"
        )
)
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long roomId;
    private Long musicId;
    private Long userId;

    @Column(name = "title")
    private String title;

    @Column(name = "contents")
    private String contents;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "likes")
    private Long likes;
}
