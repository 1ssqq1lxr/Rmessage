package io.rector.netty.transport.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @Auther: lxr
 * @Date: 2018/12/19 10:59
 * @Description:
 */
public class MessageDecoder extends LengthFieldBasedFrameDecoder {

    public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                      int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength,
                lengthAdjustment, initialBytesToStrip);
    }
}
