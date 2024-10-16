package com.nanoit.agent.hexagonal.adapter.data.mapper;

import com.nanoit.agent.hexagonal.adapter.data.entity.ShortMessageService;
import com.nanoit.agent.hexagonal.domain.Message;

public interface MessageMapper {
    Message toMessage(ShortMessageService shortMessageService);
}
