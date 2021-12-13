package com.listner.daemon.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Status {
    TODO("todo", "재생전 상태"),
    READY("ready","재생 준비 상태"),
    PLAYING("playing", "재생 상태"),
    DONE("done", "재생 완료 상태");

    private final String code;
    private final String display;


}
