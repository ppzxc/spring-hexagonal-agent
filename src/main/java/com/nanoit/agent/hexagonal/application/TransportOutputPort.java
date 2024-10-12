package com.nanoit.agent.hexagonal.application;

import com.nanoit.agent.hexagonal.domain.Message;

public interface TransportOutputPort {

    void send(Message message);
      ChannelFutureListener futureListener = future -> {
        if (future.isSuccess()) {
            System.out.println("Message sent successfully");
        } else {
            System.err.println("Failed to send message: " + future.cause());
        }
    };
    // Netty 관련 코드 작성
}

}
