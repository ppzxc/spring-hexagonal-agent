package com.nanoit.agent.hexagonal.application;

import com.nanoit.agent.hexagonal.domain.Message;

import java.util.List;

public interface MessageInputPort {

    void send(Message message);
    List<Message> processWaitingMessages();
}
