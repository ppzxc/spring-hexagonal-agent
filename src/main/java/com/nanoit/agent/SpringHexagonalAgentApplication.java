package com.nanoit.agent;
import com.nanoit.agent.hexagonal.adapter.data.H2PersistenceOutputPort;
import com.nanoit.agent.hexagonal.adapter.transport.NettyTransportOutputPort;
import com.nanoit.agent.hexagonal.domain.Message;
import com.nanoit.agent.hexagonal.domain.MessageStatus;
import com.nanoit.agent.hexagonal.domain.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class SpringHexagonalAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringHexagonalAgentApplication.class, args);

    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
 //           @Autowired
 //           private NettyTransportOutputPort nettyTransportOutputPort;
            @Autowired
            private H2PersistenceOutputPort persistenceOutputPort;

            @Override
            public void run(String... args) throws Exception {
                // 메시지를 생성하고 전송
                Message message = new SimpleMessage("1", "01012345678", "01087654321", "Test Subject", "Test Content", MessageStatus.WAIT);
     //           persistenceOutputPort.save(message);  필요한 경우 메시지 전송
            }
        };
    }
}