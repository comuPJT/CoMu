package com.listener.comu.domain.mymusic.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

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

