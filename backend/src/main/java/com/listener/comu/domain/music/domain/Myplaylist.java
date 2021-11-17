package com.listener.comu.domain.music.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Myplaylist")
@Builder
@Getter
@Setter
@ToString
@Table(name = "myplaylist")
public class Myplaylist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public Date createdAt;

    @Column(name = "user_seq")
    private long userSeq;

}
