//package com.nanoit.agent.hexagonal.adapter.transport;
//
//import com.nanoit.agent.hexagonal.domain.Message;
//import io.netty.channel.embedded.EmbeddedChannel;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelPromise;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//class NettyTransportOutputPortTest {
//
//    private NettyTransportOutputPort transportOutputPort;
//    @ExtendWith(MockitoExtension.class)
//    class NettyTransportOutputPortTest {
//
//        @InjectMocks
//        private NettyTransportOutputPort transportOutputPort;
//
//        @Mock
//        private Message message;
//
//        @BeforeEach
//        void setUp() {
//            when(message.getToPhoneNumber()).thenReturn("1234567890");
//            when(message.getFromPhoneNumber()).thenReturn("0987654321");
//            when(message.getSubject()).thenReturn("Test Subject");
//            when(message.getContent()).thenReturn("Test Content");
//        }
//
//        @Test
//        void testSend() {
//            assertDoesNotThrow(() -> transportOutputPort.send(message));
//        }
//
//        @Test
//        void testMessageEncoder() {
//            MessageEncoder encoder = new NettyTransportOutputPort.MessageEncoder();
//            EmbeddedChannel channel = new EmbeddedChannel(encoder);
//
//            channel.writeOutbound(message);
//
//            ByteBuf encodedMessage = channel.readOutbound();
//            assertNotNull(encodedMessage);
//            String decodedMessage = encodedMessage.toString(CharsetUtil.UTF_8);
//            assertEquals("1234567890|0987654321|Test Subject|Test Content", decodedMessage);
//        }
//
//        @Test
//        void testMessageHandler() {
//            MessageHandler handler = new NettyTransportOutputPort.MessageHandler();
//            EmbeddedChannel channel = new EmbeddedChannel(handler);
//
//            String testResponse = "Test Response";
//            channel.writeInbound(Unpooled.copiedBuffer(testResponse, CharsetUtil.UTF_8));
//
//            assertDoesNotThrow(() -> channel.checkException());
//        }
//    }
