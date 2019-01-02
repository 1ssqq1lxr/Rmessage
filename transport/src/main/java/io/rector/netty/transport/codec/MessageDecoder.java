package io.rector.netty.transport.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;
import io.reactor.netty.api.codec.ProtocolCatagory;

import java.nio.ByteOrder;
import java.util.List;

/**
 * @Auther: lxr
 * @Date: 2018/12/19 10:59
 * @Description:
 *
 *  * +-----1byte--------------------|---1 byte --|--1 byte -| --------4 byte-----|-----n byte------ | ----n byte-----|-----n byte-----|  timestamp |
 *  * |固定头高4bit| 消息类型低 4bit  |from目的key| to目的key|     发送body长度   | from发送kjey         |  to发送kjey     |   body          |   8byte    |
 *  @see ProtocolCatagory
 */


public class MessageDecoder extends ReplayingDecoder {

    private ByteOrder byteOrder;

    public MessageDecoder() {
        this.byteOrder =  ByteOrder.BIG_ENDIAN;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) {
        byte header=buf.readByte();
    }
}
