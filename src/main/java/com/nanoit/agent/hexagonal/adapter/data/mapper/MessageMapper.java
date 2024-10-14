package com.nanoit.agent.hexagonal.adapter.data.mapper;

import com.nanoit.agent.hexagonal.adapter.data.entity.ShortMessageService;
import com.nanoit.agent.hexagonal.domain.Message;
import com.nanoit.agent.hexagonal.domain.MessageStatus;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public Message toDomain(ShortMessageService entity) {
        return new Message() {
            @Override
            public String getId() { return entity.getId(); }
            @Override
            public String getToPhoneNumber() { return entity.getRecipientNumber(); }
            @Override
            public String getFromPhoneNumber() { return entity.getSenderNumber(); }
            @Override
            public String getSubject() { return entity.getSubject(); }
            @Override
            public String getContent() { return entity.getContent(); }
            @Override
            public MessageStatus getStatus() { return entity.getStatus(); }
            @Override
            public void setStatus(MessageStatus status) { entity.setStatus(status); }
        };
    }

public ShortMessageService toEntity(Message message) {
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

