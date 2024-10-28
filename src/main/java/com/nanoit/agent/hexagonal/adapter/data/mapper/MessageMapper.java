package com.nanoit.agent.hexagonal.adapter.data.mapper;

import com.nanoit.agent.hexagonal.adapter.data.entity.ShortMessageService;
import com.nanoit.agent.hexagonal.domain.Message;
import com.nanoit.agent.hexagonal.domain.SimpleMessage;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public SimpleMessage toDomain(ShortMessageService entity) {
        return new SimpleMessage(
                entity.getId(),
                entity.getRecipientNumber(),
                entity.getSenderNumber(),
                entity.getSubject(),
                entity.getContent(),
                entity.getStatus()
        );
    }

    public ShortMessageService toEntity(SimpleMessage message) {
        return ShortMessageService.create(
                message.getId(),
                message.getToPhoneNumber(),
                message.getFromPhoneNumber(),
                message.getSubject(),
                message.getContent(),
                message.getStatus()
        );
    }
}

