package io.rector.netty.transport.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.reactor.netty.api.codec.MessageUtils;
import io.reactor.netty.api.codec.ProtocolCatagory;

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


public class MessageDecoder extends ReplayingDecoder<MessageDecoder.Type> {

    public MessageDecoder() {
        super(Type.FIXD_HEADER);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) {
        switch (state()){
            case FIXD_HEADER:
                byte header=buf.readByte();
                checkpoint();
                MessageUtils.obtainLow(header);
                out.add(buf);
                checkpoint(Type.BODY);
            case BODY:
            case CRC:
        }
    }

    enum Type{
        FIXD_HEADER,
        BODY,
        CRC

    }

}
