package com.nanoit.agent.hexagonal.adapter.data;

import com.nanoit.agent.hexagonal.adapter.data.entity.ShortMessageService;
import com.nanoit.agent.hexagonal.adapter.data.repository.ShortMessageServiceRepository;
import com.nanoit.agent.hexagonal.adapter.data.service.ShortMessageServiceService;
import com.nanoit.agent.hexagonal.application.PersistenceOutputPort;
import com.nanoit.agent.hexagonal.domain.Message;
import com.nanoit.agent.hexagonal.domain.MessageStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class H2PersistenceOutputPort implements PersistenceOutputPort {

    private final ShortMessageServiceRepository repository;
    private final ShortMessageServiceService service;

    public H2PersistenceOutputPort(ShortMessageServiceRepository repository, ShortMessageServiceService service) {
        this.repository = repository;
        this.service = service;
    }

    @Override
    @Transactional
    public void update(Message message) {
        ShortMessageService entity = repository.findById(message.getId())
                .orElseGet(ShortMessageService::createEmpty);
        updateEntityFromMessage(entity, message);
        entity.setModifiedDateTime(LocalDateTime.now());
        repository.save(entity);
    }


    @Override
    public Message findById(String id) {
        return repository.findById(id)
                .map(this::convertToMessage)
                .orElse(null);
    }

    @Override
    @Transactional
    public List<Message> findAllByStatusIsWaitAndUpdate() {
        return service.findAllByStatusIsWaitAndUpdate()
                .stream()
                .map(this::convertToMessage)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void save(Message message) {
        ShortMessageService entity = ShortMessageService.createEmpty();
        updateEntityFromMessage(entity, message);
        entity.setCreatedDateTime(LocalDateTime.now());
        entity.setModifiedDateTime(LocalDateTime.now());
        repository.save(entity);
    }


    private void updateEntityFromMessage(ShortMessageService entity, Message message) {
        entity.setId(message.getId());
        entity.setRecipientNumber(message.getToPhoneNumber());
        entity.setSenderNumber(message.getFromPhoneNumber());
        entity.setSubject(message.getSubject());
        entity.setContent(message.getContent());
        entity.setStatus(message.getStatus());
    }

    private Message convertToMessage(ShortMessageService entity) {
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
}