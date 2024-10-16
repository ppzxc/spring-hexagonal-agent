package com.nanoit.agent.hexagonal.adapter.transport;

import com.nanoit.agent.hexagonal.domain.SmsMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ByteArrayToMessageCodec extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        String type = msg.readSlice(10).toString(StandardCharsets.UTF_8);
        String resultCode = msg.readSlice(10).toString(StandardCharsets.UTF_8);
//        String helloWorld2 = msg.readSlice(10).toString(StandardCharsets.UTF_8);
//        String helloWorld3 = msg.readSlice(10).toString(StandardCharsets.UTF_8);
//        String helloWorld4 = msg.readSlice(10).toString(StandardCharsets.UTF_8);
        if (type.contains("SMS")) {
            SmsMessage smsMessage = new SmsMessage();
            smsMessage.setResultCode(resultCode);
            out.add(smsMessage);
        }
    }
}
