package com.listener.comu.domain.music.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Music")
@Builder
@Getter
@Setter
@ToString
@Table(name = "music")
public class Music {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String spotifyId;
    private String thumbnail;
    private String name;
    private String singer;
    private String source;
    private String album;
    private int onCloud;
}

