package com.nanoit.agent.hexagonal.adapter.transport;

import com.nanoit.agent.hexagonal.application.TransportOutputPort;
import com.nanoit.agent.hexagonal.domain.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class NettyTransportOutputPort implements TransportOutputPort {

    private String host = "localhost"; // 서버 호스트 설정
    private int port = 8080; // 서버 포트설정

    private final EventLoopGroup group = new NioEventLoopGroup();// 이벤트 그룹 생성
    private Channel channel;

    // 클라이언트 초기화
    public void init() throws InterruptedException {
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
                                System.out.println("Received: " + msg.toString(StandardCharsets.UTF_8)); // 서버로부터 받은 메시지 출력
                            }
                        });
                    }
                });
        this.channel = b.connect(host, port).sync().channel(); //서버에 연결
    }

    // 메시지 전송
    @Override
    public void send(Message message) {
        if (channel == null || !channel.isActive()) {
            try {
                init(); // 연결 없거나 비활성화된 경우 초기화
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Failed to initialize connection", e);
            }
        }
        String encodedMessage = String.format("%s|%s|%s|%s",
                message.getToPhoneNumber(),
                message.getFromPhoneNumber(),
                message.getSubject(),
                message.getContent()); // 메시지 인코딩

        ByteBuf buf = channel.alloc().buffer(); // ByteBuf 생성 (전송 준비)
        buf.writeBytes(encodedMessage.getBytes(StandardCharsets.UTF_8));
        channel.writeAndFlush(buf).addListener((ChannelFutureListener) future -> {

            if (!future.isSuccess()) {
                System.err.println("Failed to send message: " + future.cause()); // 전송 실패 시
            } else {
                System.out.println("Message sent successfully"); // 전송 성공 시
            }
        });
    }

    // 클라이언트 종료
    public void shutdown() {
        if (channel != null) {
            channel.close(); // 채널 닫기
        }
        group.shutdownGracefully(); // 이벤트 루프 종료
    }
}