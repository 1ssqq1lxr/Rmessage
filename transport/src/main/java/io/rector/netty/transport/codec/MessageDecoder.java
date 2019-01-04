package io.rector.netty.transport.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.reactor.netty.api.codec.MessageUtils;
import io.reactor.netty.api.codec.ProtocolCatagory;
import io.reactor.netty.api.codec.TransportMessage;

import java.util.List;

/**
 * @Auther: lxr
 * @Date: 2018/12/19 10:59
 * @Description:
 *   ****  ONE   GROUP
 *  * +-----1byte--------------------|---1 byte --|--1 byte -| --------4 byte-----|------2byte---|-----n byte------ | ----n byte-----|-----n byte----- |--------nbyte---- |  timestamp |
 *  * |固定头高4bit| 消息类型低 4bit  |from目的key| to目的key |     发送body长度   | 附加字段      | from发送kjey     |  to发送kjey     |   body          |additional fields |   8byte    |
 *
 *  ****  PING  PONG
 *   +-----1byte--------------------|
 *   |固定头高4bit| 消息类型低 4bit  |
 *
 *  ****  JOIN   LEAVE
 *    * +-----1byte--------------------|---1 byte --|--1 byte -|-----n byte------ | ----n byte-----|
 *     |固定头高4bit| 消息类型低 4bit  |from目的key | to目的key-| from发送kjey     |  to发送kjey     |
 *
 *  ****  ADDUSER  DELUSER
 *  * * +-----1byte--------------------|---1 byte --|--1 byte -|-----n byte------ | ----n byte-----|
 *  *  |固定头高4bit| 消息类型低 4bit  |from目的key | to目的key-| from发送kjey     |  to发送kjey     |
 *
 *
 * @see ProtocolCatagory
 */


public class MessageDecoder extends ReplayingDecoder<MessageDecoder.Type> {

    public MessageDecoder() {
        super(Type.FIXD_HEADER);
    }

    private ProtocolCatagory type;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) {
        switch (state()){
            case FIXD_HEADER:
                byte header=buf.readByte();
                switch ((type=MessageUtils.obtainLow(header))){
                    case ONE:
                        type = ProtocolCatagory.ONE;
                        this.checkpoint(Type.BODY);
                    case JOIN:
                        this.checkpoint(Type.BODY);
                    case PING:
                        out.add(TransportMessage.builder().type(type).build());
                        this.checkpoint(Type.FIXD_HEADER);
                        return;
                    case GROUP:
                    case LEAVE:
                    case CONFIRM:
                    case ACCEPT:
                    default:
                        super.discardSomeReadBytes();
                        this.checkpoint(Type.FIXD_HEADER);
                        return;
                }
            case BODY:
            case ADDITIONAL:
            case CRC:
        }
    }

    enum Type{
        FIXD_HEADER,
        BODY,
        ADDITIONAL,
        CRC
    }

}
