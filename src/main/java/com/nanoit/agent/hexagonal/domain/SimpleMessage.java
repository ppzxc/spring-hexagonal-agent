package com.nanoit.agent.hexagonal.domain;

public class SimpleMessage implements Message {
    private final String id;
    private final String toPhoneNumber;
    private final String fromPhoneNumber;
    private final String subject;
    private final String content;
    private MessageStatus status;

    public SimpleMessage(String id, String toPhoneNumber, String fromPhoneNumber, String subject, String content, MessageStatus status) {
        this.id = id;
        this.toPhoneNumber = toPhoneNumber;
        this.fromPhoneNumber = fromPhoneNumber;
        this.subject = subject;
        this.content = content;
        this.status = status;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getToPhoneNumber() {
        return this.toPhoneNumber;
    }

    @Override
    public String getFromPhoneNumber() {
        return this.fromPhoneNumber;
    }

    @Override
    public String getSubject() {
        return this.subject;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public MessageStatus getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(MessageStatus status) {
        this.status = status;
    }
}
