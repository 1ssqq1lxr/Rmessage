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
 *   type:  ONE   GROUP
 *   FIXHEADER
 *   |-----1byte--------------------|
 *   |固定头高4bit| 消息类型低 4bit    |
 *
 *  TOPICHEADER
 *   |---1 byte ---------|--1 byte ------------|
 *   |--from目的length----|---目的key length-----|
 *
 *   |-----n byte--------|-------n byte--------|
 *   |-----from目的-------|-------目的key--------|
 *
 *  MESSAGEBODY
 *   |--------4 byte-----------|------2byte---------------------------|
 *   |----- 消息body length----- |-------additional fields  length---- |
 *   |-----n byte--------|-------n byte--------------------|
 *   |-----消息body-------|-------additional fields --------|
 *
 *  CRC
 *   |  timestamp 8byte |
 *   |---时间戳----------|
 *
 *
 *
 *  type:   PING  PONG
 *   FIXHEADER
 *   +-----1byte--------------------|
 *   |固定头高4bit| 消息类型低 4bit  |
 *
 *
 *  type:  JOIN   LEAVE  ADDUSER  DELUSER
 *
 *   FIXHEADER
 *   |-----1byte--------------------|
 *   |固定头高4bit| 消息类型低 4bit    |
 *
 *  TOPICHEADER
 *   |---1 byte ---------|--1 byte ------------|
 *   |--from目的length----|---目的key length-----|
 *
 *   |-----n byte--------|-------n byte--------|
 *   |-----from目的-------|-------目的key--------|
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
                    case PING:
                        out.add(TransportMessage.builder().type(type).build());
                        this.checkpoint(Type.FIXD_HEADER);
                        break;
                    case ONE:
                        type = ProtocolCatagory.ONE;
                        this.checkpoint(Type.TOPICHEADER);
                        break;
                    case GROUP:
                        type = ProtocolCatagory.GROUP;
                        this.checkpoint(Type.TOPICHEADER);
                        break;
                    case JOIN:
                        this.checkpoint(Type.TOPICHEADER);
                        break;
                    case LEAVE:
                        this.checkpoint(Type.TOPICHEADER);
                        break;
                    case ADDUSER:
                        break;
                    case ONLINE:
                        break;
                    case DELUSER:
                        break;
                    case PONG:
                        break;
                    case ACCEPT:
                        break;
                    default:
                        super.discardSomeReadBytes();
                        this.checkpoint(Type.FIXD_HEADER);
                        return;
                }
            case TOPICHEADER:
            case MESSAGEBODY:
            case CRC:
        }
    }

    enum Type{
        FIXD_HEADER,
        TOPICHEADER,
        MESSAGEBODY,
        CRC
    }

}
