//package com.nanoit.agent.hexagonal.adapter.transport;
//
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.*;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.handler.codec.string.StringDecoder;
//import io.netty.handler.codec.string.StringEncoder;
//import java.nio.charset.StandardCharsets;
//
//public class NettyServer {
//    private final int port;
//
//    public NettyServer(int port) {
//        this.port = port;
//    }
//
//    public void start() throws InterruptedException {
//        EventLoopGroup bossGroup = new NioEventLoopGroup();
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        try {
//            ServerBootstrap b = new ServerBootstrap();
//            b.group(bossGroup, workerGroup)
//                    .channel(NioServerSocketChannel.class)
//                    .childHandler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        protected void initChannel(SocketChannel ch) {
//                            ChannelPipeline p = ch.pipeline();
//                            p.addLast(new StringDecoder(StandardCharsets.UTF_8));
//                            p.addLast(new StringEncoder(StandardCharsets.UTF_8));
//                            p.addLast(new SimpleChannelInboundHandler<String>() {
//                                @Override
//                                protected void channelRead0(ChannelHandlerContext ctx, String msg) {
//                                    System.out.println("Received: " + msg);
//                                    ctx.writeAndFlush("Message received");
//                                }
//                            });
//                        }
//                    });
//
//
//            ChannelFuture f = b.bind(port).sync();
//            f.channel().closeFuture().sync();
//        } finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }
//    }
//
//    public static void main(String[] args) throws InterruptedException {
//        new NettyServer(8080).start();
//    }
//}

