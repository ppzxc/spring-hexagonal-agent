package com.nanoit.agent.hexagonal.domain;

import com.nanoit.agent.hexagonal.adapter.data.entity.Status;

public class SmsMessage implements Message {

    private String id;
    private String toPhoneNumber;
    private String fromPhoneNumber;
    private String subject;
    private String content;
    private Status status;
    private String resultCode;

    public SmsMessage(String id, String toPhoneNumber, String fromPhoneNumber, String subject, String content, Status status, String resultCode) {
        this.id = id;
        this.toPhoneNumber = toPhoneNumber;
        this.fromPhoneNumber = fromPhoneNumber;
        this.subject = subject;
        this.content = content;
        this.status = status;
        this.resultCode = resultCode;
    }

    public SmsMessage() {

    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getToPhoneNumber() {
        return toPhoneNumber;
    }

    @Override
    public String getFromPhoneNumber() {
        return fromPhoneNumber;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public String getResultCode() {
        return resultCode;
    }

    @Override
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
}
