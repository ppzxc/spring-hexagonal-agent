package com.nanoit.agent.hexagonal.adapter.data.entity;

import jakarta.persistence.*;
import com.nanoit.agent.hexagonal.domain.MessageStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "sms_msg")
public class ShortMessageService {

    @Id
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String id;

    @Column(name = "recipient_number", nullable = false)
    private String recipientNumber;

    @Column(name = "sender_number", nullable = false)
    private String senderNumber;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content", nullable = false)
    private String content;

    @ColumnDefault("now()")
    @Column(name = "created_datetime", nullable = false)
    private LocalDateTime createdDateTime;

    @ColumnDefault("now()")
    @Column(name = "modified_datetime", nullable = false)
    private LocalDateTime modifiedDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MessageStatus status;


    public static ShortMessageService createEmpty() {
        ShortMessageService sms = new ShortMessageService();
        sms.setCreatedDateTime(LocalDateTime.now());
        sms.setModifiedDateTime(LocalDateTime.now());
        sms.setStatus(MessageStatus.WAIT);
        return sms;
    }

    // 모든 필드를 초기화하는 생성자 추가
    public ShortMessageService(String id, String recipientNumber, String senderNumber, String subject, String content, MessageStatus status) {
        this.id = id;
        this.recipientNumber = recipientNumber;
        this.senderNumber = senderNumber;
        this.subject = subject;
        this.content = content;
        this.status = status;
        this.createdDateTime = LocalDateTime.now();
        this.modifiedDateTime = LocalDateTime.now();

    }

    // 객체 생성용 static factory method
    public static ShortMessageService create(String id, String recipientNumber, String senderNumber, String subject, String content, MessageStatus status) {
        ShortMessageService entity = new ShortMessageService();
        entity.setId(id);
        entity.setRecipientNumber(recipientNumber);
        entity.setSenderNumber(senderNumber);
        entity.setSubject(subject);
        entity.setContent(content);
        entity.setStatus(status);
        entity.setCreatedDateTime(LocalDateTime.now());
        entity.setModifiedDateTime(LocalDateTime.now());
        return entity;
    }
}


