package com.listener.comu.domain.music.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "MyMainPlaylist")
@Builder
@Getter
@Setter
@ToString
@Table(name = "my_main_playlist")
public class MyMainPlaylist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long musicId;
    private int playOrder;
    private long userSeq;

}
