package com.nanoit.agent.hexagonal.adapter.data.service;

import com.nanoit.agent.hexagonal.adapter.data.entity.ShortMessageService;
import com.nanoit.agent.hexagonal.adapter.data.entity.Status;
import com.nanoit.agent.hexagonal.adapter.data.repository.ShortMessageServiceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShortMessageServiceServiceImpl implements ShortMessageServiceService {

    private final ShortMessageServiceRepository shortMessageServiceRepository;

    public ShortMessageServiceServiceImpl(ShortMessageServiceRepository shortMessageServiceRepository) {
        this.shortMessageServiceRepository = shortMessageServiceRepository;
    }

    /**
     * 테이블 내 대기중인 전송 메시지 데이터를 조회하고 상태값을 변경한 후 조회된 데이터를 리턴한다.
     */
    @Override
    public List<ShortMessageService> findAllByStatusIsWaitAndUpdate() {
        // 1. 대기 상태인 메시지 조회
        List<ShortMessageService> waitingMessages = shortMessageServiceRepository.findByStatus("WAIT");

        // 2. 상태 업데이트
        for (ShortMessageService message : waitingMessages) {
            message.setStatus(Status.SENT);
            message.setModifiedDateTime(LocalDateTime.now());
        }

        // 3. DB에 값 저장
        shortMessageServiceRepository.saveAll(waitingMessages);

        // 4. 조회된 데이터 반환
        return waitingMessages;
    }
}
