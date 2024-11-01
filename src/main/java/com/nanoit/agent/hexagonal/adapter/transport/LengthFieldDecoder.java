package com.nanoit.agent.hexagonal.adapter.transport;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;

/**
 * <pre>
 * 현재 Client <-> G/W 와 통신은 읽어야할 Body Length 를 Stream Header 에 표기해 디코딩 하는 방식을 사용 중.
 * 서버에서 정의된 프로토콜이 Byte (int8, int16, int32, int64) 단위를 따르지 않고 10이라는 임의의 사이즈를
 * 사용중이고 이 사이즈 또한 2진수에서 표기할 수 있는 숫자를 뜻하는게 아닌 숫자를 문자열로 바꾸고 그 문자열을
 * Header에 표기하는 방식을 사용중.
 * </pre>
 *
 * <pre>
 * @SuppressWarnings("all") 은 buf.order(order) 의 deprecated 경고를 off함.
 * LengthFieldDecoder 클래스는 netty 기본 디코더인 LengthFieldBasedFrameDecoder 를 확장하는데,
 * LengthFieldBasedFrameDecoder 의 getUnadjustedFrameLength 에서 커스텀 사이즈를 사용하기 위함임.
 * (참고 // custom it )
 *
 * 또 LengthFieldBasedFrameDecoder 의 getUnadjustedFrameLength 메소드 역시 아직 buf.order를 사용함으로,
 * 본 확장 클래스에서도 그대로 유지함.
 * </pre>
 */
@SuppressWarnings("all")
public class LengthFieldDecoder extends LengthFieldBasedFrameDecoder {

  public LengthFieldDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                            int lengthAdjustment, int initialBytesToStrip) {
    super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
  }

  @Override
  protected long getUnadjustedFrameLength(ByteBuf buf, int offset, int length, ByteOrder order) {
    buf = buf.order(order);
    long frameLength;
    switch (length) {
      case 1:
        frameLength = buf.getUnsignedByte(offset);
        break;
      case 2:
        frameLength = buf.getUnsignedShort(offset);
        break;
      case 3:
        frameLength = buf.getUnsignedMedium(offset);
        break;
      case 4:
        frameLength = buf.getUnsignedInt(offset);
        break;
      case 8:
        frameLength = buf.getLong(offset);
        break;
      case 10: // custom it
        byte[] size = new byte[10];
        buf.getBytes(offset, size, 0, length);
        frameLength = Integer.parseInt(new String(size).trim());
        break;
      default:
        throw new DecoderException("unsupported lengthFieldLength (expected: 1, 2, 3, 4, or 8)");
    }
    return frameLength;
  }
}
