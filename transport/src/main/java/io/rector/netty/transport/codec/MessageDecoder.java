package io.rector.netty.transport.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @Auther: lxr
 * @Date: 2018/12/19 10:59
 * @Description:
 *
 *  * +-----1byte--------------------|---1 byte -| --------4 byte-----|-----n byte-----|-----n byte-----|  timestamp |
 *  * |固定头高4bit| 消息类型低 4bit  | 发送目的key|     发送body长度   |  发送kjey     |   body          |   8byte    |
 *  @see ProtocolCatagory
 */


public class MessageDecoder extends LengthFieldBasedFrameDecoder {

    public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                      int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength,
                lengthAdjustment, initialBytesToStrip);
    }
}
