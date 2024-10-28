package com.nanoit.agent.hexagonal.adapter.transport;

import com.nanoit.agent.hexagonal.application.TransportOutputPort;
import com.nanoit.agent.hexagonal.domain.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.DisposableBean;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class NettyTransportOutputPort implements TransportOutputPort, DisposableBean {

    private final String host = "localhost";
    private final int port = 8080;
    private final EventLoopGroup group = new NioEventLoopGroup();
    private Channel channel;

    @Override
    public void init() {
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) {
                            ch.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
                                    String receivedMessage = msg.toString(StandardCharsets.UTF_8);
                                    log.info("Received: {}", receivedMessage);
                                }
                            });
                        }
                    });
            this.channel = b.connect(host, port).sync().channel();
            log.info("Successfully connected to {}:{}", host, port);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to initialize connection", e);
        }
    }

    @Override
    public void login() {
        send("LOGIN", "username|password"); // 실제 로그인 정보로 대체 필요
    }

    @Override
    public void send(Message message) {
        if (channel == null || !channel.isActive()) {
            init();
        }

        String encodedMessage = String.format("SEND|%s|%s|%s|%s",
                message.getToPhoneNumber(),
                message.getFromPhoneNumber(),
                message.getSubject(),
                message.getContent());

        ByteBuf buf = channel.alloc().buffer();
        buf.writeBytes(encodedMessage.getBytes(StandardCharsets.UTF_8));

        channel.writeAndFlush(buf).addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                log.error("Failed to send message: {}", future.cause().getMessage());
                throw new RuntimeException("Failed to send message", future.cause());
            } else {
                log.info("Message sent successfully: {}", message.getId());
            }
        });
    }

    @Override
    @Scheduled(fixedRate = 60000) // 60초마다 실행
    public void alive() {
        try {
            send("ALIVE", "");
            log.info("Alive signal sent successfully");
        } catch (Exception e) {
            log.error("Failed to send alive signal: {}", e.getMessage());
        }
    }

    private void send(String messageType, String payload) {
        if (channel == null || !channel.isActive()) {
            init();
        }
        String encodedMessage = String.format("%s|%s", messageType, payload);
        ByteBuf buf = channel.alloc().buffer();
        buf.writeBytes(encodedMessage.getBytes(StandardCharsets.UTF_8));

        channel.writeAndFlush(buf).addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                log.error("Failed to send {}: {}", messageType, future.cause().getMessage());
            } else {
                log.info("{} sent successfully", messageType);
            }
        });
    }

    @Override
    public void destroy() {
        if (channel != null) {
            channel.close();
        }
        group.shutdownGracefully();
        log.info("Netty resources released");
    }
}