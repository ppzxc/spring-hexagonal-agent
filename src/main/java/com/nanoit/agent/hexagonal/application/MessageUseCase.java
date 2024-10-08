package com.nanoit.agent.hexagonal.application;

import com.nanoit.agent.hexagonal.domain.Message;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MessageUseCase implements MessageInputPort {

    private final TransportOutputPort transportOutputPort;
    private final PersistenceOutputPort persistenceOutputPort;

    public MessageUseCase(TransportOutputPort transportOutputPort, PersistenceOutputPort persistenceOutputPort) {
        this.transportOutputPort = transportOutputPort;
        this.persistenceOutputPort = persistenceOutputPort;
    }

    /**
     * 비즈니스 로직을 구현하고 유효한 메시지일 경우 transport 영역으로 전달한다.
     * 필요한 경우 persistence port로 업데이트를 진행한다.
     */
    @Override
    public void send(Message message) {
        if (isValidMessage(message)) {
            transportOutputPort.send(message);
        }
    }

    public static boolean isValidMessage(Message message) {
        if (!isValidNumber(message.getToPhoneNumber())) {
            throw new IllegalArgumentException("수신 번호가 유효하지 않습니다: " + message.getToPhoneNumber());
        }

        if (!isValidNumber(message.getFromPhoneNumber())) {
            throw new IllegalArgumentException("발신 번호가 유효하지 않습니다: " + message.getFromPhoneNumber());
        }

        if (!isValidSubject(message.getSubject())) {
            throw new IllegalArgumentException("제목은 90Byte 이하로 입력해야 합니다: " + message.getSubject());
        }

        if (!isValidContent(message.getContent())) {
            throw new IllegalArgumentException("메시지 내용은 200Byte 이하로 입력해야 합니다: " + message.getContent());
        }

        return true;
    }

    // 전화번호 유효성 검사
    public static boolean isValidNumber(String phoneNumber) {
        return Pattern.matches("^(01[016789]|02|0[3-9][0-9])\\d{3,4}\\d{4}$", phoneNumber);
    }

    public static boolean isValidSubject(String subject) {
        return subject.getBytes(StandardCharsets.UTF_8).length <= 90;
    }

    public static boolean isValidContent(String content) {
        return content.getBytes(StandardCharsets.UTF_8).length <= 200;
    }
}
