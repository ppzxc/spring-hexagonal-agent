package com.nanoit.agent.hexagonal.adapter.transport;

import com.nanoit.agent.hexagonal.application.TransportOutputPort;
import com.nanoit.agent.hexagonal.domain.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Component
public class NettyTransportOutputPort implements TransportOutputPort {

    @Value("${nanoit.server.host:localhost}")
    private String host;

    @Value("${nanoit.server.port:8080}")
    private int port;

    private final EventLoopGroup group = new NioEventLoopGroup();
    private Channel channel;

    public void init() throws InterruptedException {
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new MessageEncoder());
                        p.addLast(new MessageHandler());
                    }
                });

        this.channel = b.connect(host, port).sync().channel();
    }

    public void shutdown() {
        if (channel != null) {
            channel.close();
        }
        group.shutdownGracefully();
    }

    @Override
    public void send(Message message) {
        if (channel == null || !channel.isActive()) {
            try {
                init();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Failed to initialize connection", e);
            }
        }

        channel.writeAndFlush(message).addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                System.err.println("Failed to send message: " + future.cause());
            }
        });
    }

    static class MessageEncoder extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
            if (msg instanceof Message) {
                Message message = (Message) msg;
                ByteBuf buf = ctx.alloc().buffer();
                String encodedMessage = String.format("%s|%s|%s|%s",
                        message.getToPhoneNumber(),
                        message.getFromPhoneNumber(),
                        message.getSubject(),
                        message.getContent());
                buf.writeBytes(encodedMessage.getBytes(StandardCharsets.UTF_8));
                ctx.write(buf, promise);
            } else {
                ctx.write(msg, promise);
            }
        }
    }

    static class MessageHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ByteBuf in = (ByteBuf) msg;
            try {
                StringBuilder response = new StringBuilder();
                while (in.isReadable()) {
                    response.append((char) in.readByte());
                }
                System.out.println("Server response: " + response);
            } finally {
                in.release();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }
}