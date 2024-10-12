package com.nanoit.agent.hexagonal.application;

import com.nanoit.agent.hexagonal.domain.Message;

import java.util.List;

public interface PersistenceOutputPort {

    void update(Message message);
    Message findById(String id);
    List<Message> findAllByStatusIsWaitAndUpdate();

    void save(Message message);
}

