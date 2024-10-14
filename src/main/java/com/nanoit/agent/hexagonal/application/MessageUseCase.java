package com.nanoit.agent.hexagonal.application;

import com.nanoit.agent.hexagonal.domain.Message;
import com.nanoit.agent.hexagonal.domain.MessageStatus;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

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
        validateMessage(message);// 메시지 유효성 검증
        try {
            transportOutputPort.send(message); // NettyTransportOutputPort로 메시지 전송
            message.setStatus(MessageStatus.SENT); //메시지 전송 성공하면 sent
        }catch (Exception e) {
            message.setStatus(MessageStatus.FAILED); // 메시지 전송 실패하면 failed, 예외 메시지를 기록
            System.err.println("Failed to send message:" + e.getMessage());
        }
        persistenceOutputPort.update(message); // 메시지 상태 업데이트

    }
    @Override
    public List<Message> processWaitingMessages() {
        List<Message> waitingMessages = persistenceOutputPort.findAllByStatusIsWaitAndUpdate();
        for (Message message : waitingMessages) {
            // 메시지 처리
            transportOutputPort.send(message);
            message.setStatus(MessageStatus.PROCESSING);
            persistenceOutputPort.update(message);
        }
        return waitingMessages;
    }

    private void validateMessage(Message message) {
        validatePhoneNumber(message.getToPhoneNumber(), "Recipient");
        validatePhoneNumber(message.getFromPhoneNumber(),"Sender");
        validateSubject(message.getSubject());
        validateContent(message.getContent());
    }

    private void validatePhoneNumber(String phoneNumber, String type) {
      String regex ="^\\d{10,11}$";
      if (!Pattern.matches(regex, phoneNumber)) {
          throw new IllegalArgumentException(type +"Invalid phone number");
      }
    }
    private void validateSubject(String subject) {
      if (subject == null || subject.trim().isEmpty()) {
          throw new IllegalArgumentException("Subject cannot be empty");
      }
        if (subject.getBytes(StandardCharsets.UTF_8).length > 90) {
            throw new IllegalArgumentException("Subject must be less than 90 bytes");
        }
    }
    private void validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
        if (content.getBytes(StandardCharsets.UTF_8).length > 200) {
            throw new IllegalArgumentException("Content must be less than 200 bytes");
        }
    }
}
