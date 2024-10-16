package com.nanoit.agent.hexagonal.adapter.data.entity;

import com.nanoit.agent.hexagonal.domain.Message;
import jakarta.persistence.*;
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
    @Column(name = "id", nullable = false, length = 20)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String id;

    @Column(nullable = false)
    private String toPhoneNumber;

    @Column(nullable = false)
    private String fromPhoneNumber;

    @Column(nullable = false, length = 100)
    private String subject;

    @Lob
    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Status status;

    private String resultCode;

    @ColumnDefault("now()")
    @Column(name = "created_datetime", nullable = false)
    private LocalDateTime createdDateTime;

    @ColumnDefault("now()")
    @Column(name = "modified_datetime", nullable = false)
    private LocalDateTime modifiedDateTime;
}