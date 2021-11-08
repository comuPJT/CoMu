package com.listener.comu.domain.music.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "MyPlayinglist")
@Builder
@Getter
@Setter
@ToString
@Table(name = "myplayinglist")
public class MyMainPlaylist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long music_id;

    @Column(name = "play_order")
    private int playOrder;

    @Column(name = "user_seq")
    private long userSeq;

}
