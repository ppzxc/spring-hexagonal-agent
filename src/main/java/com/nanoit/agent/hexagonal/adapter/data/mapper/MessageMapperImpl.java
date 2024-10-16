package com.nanoit.agent.hexagonal.adapter.data.mapper;

import com.nanoit.agent.hexagonal.adapter.data.entity.ShortMessageService;
import com.nanoit.agent.hexagonal.domain.SmsMessage;

public class MessageMapperImpl implements MessageMapper {

    @Override
    public SmsMessage toMessage(ShortMessageService shortMessageService) {
        return new SmsMessage(
                shortMessageService.getId(),
                shortMessageService.getToPhoneNumber(),
                shortMessageService.getFromPhoneNumber(),
                shortMessageService.getSubject(),
                shortMessageService.getContent(),
                shortMessageService.getStatus(),
                shortMessageService.getResultCode()
        );
    }
}