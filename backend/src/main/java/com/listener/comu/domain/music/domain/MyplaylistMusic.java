package com.listener.comu.domain.music.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "MyplaylistMusic")
@Builder
@Getter
@Setter
@ToString
@Table(name = "myplaylist_music")
public class MyplaylistMusic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long myplaylistId;
    private long musicId;


}

