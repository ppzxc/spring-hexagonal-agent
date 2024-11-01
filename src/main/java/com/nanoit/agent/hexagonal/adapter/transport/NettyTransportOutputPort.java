package com.nanoit.agent.hexagonal.adapter.transport;

import com.nanoit.agent.hexagonal.application.TransportOutputPort;
import com.nanoit.agent.hexagonal.domain.Message;
import io.github.ppzxc.crypto.Crypto;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class NettyTransportOutputPort implements TransportOutputPort {
    private final Crypto crypto;
    private EventLoopGroup group;
    private Channel channel;

    public NettyTransportOutputPort(Crypto crypto) {
        this.crypto = crypto;
    }

    // 1. Netty 애플리케이션 초기화
    @SneakyThrows
    @PostConstruct
    void init() {
//        http://dist.funsms.kr/
//        testagent1
//        YsbehiNUmc4FLDM5
//        FqWvV9xzP5wJ4houY76tek1dknrj1


        System.out.println("init");
        // GET http://localhost:9990?username=test1&password={ENCRYPTED PW}
        UriComponents uriComponents = UriComponentsBuilder.fromUriString("http://dist.funsms.kr/")
                .queryParam("id", "testagent1")
                .queryParam("password", crypto.encryptToString("YsbehiNUmc4FLDM5"))
                .build();

        // URI 인코딩 필수
        // URI 추가적으로 찾아보시면 됩니다.

        log.info("{}", uriComponents.encode());
        log.info("{}", uriComponents.toUriString());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.getForEntity(uriComponents.encode().toUri(), String.class);
        if (result.getStatusCode() != HttpStatus.OK) {
            log.error("LOGIN FAILED");
            System.exit(-1);
        } else {
            log.info("{}", result.getBody());
        }

        //
        // Byte Stream
        //

        group = new NioEventLoopGroup(); // 비동기 이벤트 루프 그룹 설정
        try {
            Bootstrap bootstrap = new Bootstrap(); // 애플리케이션 부트스트랩(초기화) 객체 생성
            bootstrap.group(group) // 부트스트랩에 그룹 할당
                    .channel(NioSocketChannel.class) // 비동기 TCP 소켓 통신을 처리할 채널을 만들겠다
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));

                            pipeline.addLast(new LengthFieldDecoder(10000000, 10, 10, 0, 0));
                            pipeline.addLast(new ByteArrayToMessageCodec());
                        }
                    });

            // 2. 서버에 연결
            ChannelFuture channelFuture = bootstrap.connect("localhost", 8080).sync();
            this.channel = channelFuture.channel();
            log.info("Successfully connected to server.");
        } catch (InterruptedException e) {
            log.error("Error during initiation: ", e);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void send(Message message) {
        if (channel == null || !channel.isActive()) {
            log.error("Channel is not active.");
            return;
        }

        // 3. 메시지 전송
        ByteBuf buffer = Unpooled.copiedBuffer(message.getContent(), StandardCharsets.UTF_8);
        channel.writeAndFlush(buffer).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("Message id {}: successfully sent.", message.getId());
            } else {
                log.error("Message id {}: failed to send - {}", message.getId(), future.cause());
            }
        });

        // 4. 전송 후 연결 종료 대기
        channel.closeFuture().addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("Channel closed successfully.");
                close();
            } else {
                log.error("Error while closing the channel: {}", future.cause());
            }
        });
    }

    // 5. 자원 해제
    public void close() {
        if (group != null) {
            group.shutdownGracefully(); // 자원 해제
            log.info("EventLoopGroup shutdown completed.");
        }
    }
}
