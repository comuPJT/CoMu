package com.listener.comu.domain.mymusic.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

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

    private String spotify_id;
    private String thumbnail;
    private String name;
    private String singer;
    private String source;

}

