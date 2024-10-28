package com.nanoit.agent.hexagonal.application;

import com.nanoit.agent.hexagonal.domain.Message;

public interface TransportOutputPort {

    void init(); // 연결 초기화
    void login(); // 로그인
    void alive(); // alive 신호
    void send(Message message); // 메시지 전송

}


