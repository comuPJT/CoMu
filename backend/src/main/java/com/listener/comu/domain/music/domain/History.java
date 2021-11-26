package com.listener.comu.domain.music.domain;

import com.listener.comu.domain.oauthlogin.api.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "history")
@Getter
@Setter
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id")
    private Long roomId; //애도 MariaDB에서 FK이긴 한데

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name= "music_id",
            nullable = false
    )
    private Music music;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name= "user_seq",
            nullable = false
    )
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "contents")
    private String contents;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "likes")
    private Long likes;

    public History() { }
    public History(Long roomId, String title, String contents, LocalDateTime timestamp, Long likes) {
        this.roomId = roomId;
        this.title = title;
        this.contents = contents;
        this.timestamp = timestamp;
        this.likes = likes;
    }
}
