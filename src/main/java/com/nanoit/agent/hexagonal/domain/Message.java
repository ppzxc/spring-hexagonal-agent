package com.nanoit.agent.hexagonal.domain;

import com.nanoit.agent.hexagonal.adapter.data.entity.Status;

public interface Message {

    String getId();

    String getToPhoneNumber();

    String getFromPhoneNumber();

    String getSubject();

    String getContent();

    Status getStatus();

    String getResultCode();

    void setResultCode(String resultCode);
}