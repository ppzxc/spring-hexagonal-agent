package com.nanoit.agent.hexagonal.adapter.data;

import com.nanoit.agent.hexagonal.adapter.data.entity.ShortMessageService;
import com.nanoit.agent.hexagonal.adapter.data.entity.Status;
import com.nanoit.agent.hexagonal.adapter.data.repository.ShortMessageServiceRepository;
import com.nanoit.agent.hexagonal.application.PersistenceOutputPort;
import com.nanoit.agent.hexagonal.domain.Message;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class H2PersistenceOutputPort implements PersistenceOutputPort {

    private final ShortMessageServiceRepository shortMessageServiceRepository;

    public H2PersistenceOutputPort(ShortMessageServiceRepository shortMessageServiceRepository) {
        this.shortMessageServiceRepository = shortMessageServiceRepository;
    }

    /**
     * JPA 구현체 Hibernate를 사용할 수 있는 repository, service 영역의 구현체가 위치하며 실제 update를 진행한다.
     */
    @Override
    public void update(Message message) {
        // 메시지 업데이트 진행
        ShortMessageService entity = shortMessageServiceRepository.findById(message.getId())
            .orElseThrow(() -> new IllegalArgumentException("Message not found: id - " + message.getId()));

        entity.setStatus(Status.FAILED);
        shortMessageServiceRepository.save(entity);
    }
}
