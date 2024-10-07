package com.nanoit.agent.hexagonal.application;

import com.nanoit.agent.hexagonal.domain.Message;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
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
        validateMessage(message);
        transportOutputPort.send(message);
        persistenceOutputPort.update(message);

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
