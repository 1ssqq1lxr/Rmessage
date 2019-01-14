package io.rector.netty.transport.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.reactor.netty.api.codec.MessageBody;
import io.reactor.netty.api.codec.MessageUtils;
import io.reactor.netty.api.codec.ProtocolCatagory;
import io.reactor.netty.api.codec.TransportMessage;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @Auther: lxr
 * @Date: 2018/12/19 10:59
 * @Description:
 *   type:  ONE   GROUP
 *   FIXHEADER
 *   |-----1byte--------------------|
 *   |客户端类型| 消息类型低 4bit    |
 *
 *  TOPICHEADER
 *   |---4 byte ---------|---1 byte ---------|--1 byte ------------|
 *   |--mesageId --------|--from目的length----|---目的key length-----|
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
 *   CRC
 *   |  timestamp 8byte |
 *   |---时间戳----------|
 * @see ProtocolCatagory
 */


public class MessageDecoder extends ReplayingDecoder<MessageDecoder.Type> {

    public MessageDecoder() {
        super(Type.FIXD_HEADER);
    }

    private ProtocolCatagory type;

    private String from;

    private String to;


    private MessageBody messageBody;



    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) {
        header:switch (state()){
            case FIXD_HEADER:
                byte header=buf.readByte();
                switch ((type=MessageUtils.obtainLow(header))){
                    case PING:
                        out.add(TransportMessage.builder().type(type).build());
                        this.checkpoint(Type.FIXD_HEADER);
                        break header;
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
                        this.checkpoint(Type.TOPICHEADER);
                        break;
                    case ONLINE:
                        this.checkpoint(Type.TOPICHEADER);
                        break;
                    case DELUSER:
                        this.checkpoint(Type.TOPICHEADER);
                        break;
                    default:
                        super.discardSomeReadBytes();
                        this.checkpoint(Type.FIXD_HEADER);
                        return;
                }
            case TOPICHEADER:
                short fromlength= buf.readByte();
                short tolength= buf.readByte();
                byte[] fromBytes = new byte[fromlength];
                byte[] toBytes = new byte[tolength];
                buf.readBytes(fromBytes);
                buf.readBytes(toBytes);
                from =new String(fromBytes, Charset.defaultCharset());
                to   =new String(toBytes, Charset.defaultCharset());
                if( type == ProtocolCatagory.JOIN
                        || type == ProtocolCatagory.LEAVE
                        || type == ProtocolCatagory.ADDUSER
                        || type == ProtocolCatagory. DELUSER ){
                    out.add(TransportMessage.builder().type(type)
                            .from(from)
                            .to(to)
                            .build());
                    this.checkpoint(Type.FIXD_HEADER);
                    break  header;
                }
                else
                    this.checkpoint(Type.MESSAGEBODY);
            case MESSAGEBODY:
                long messageId=buf.readLong(); // 消息id
                int bodyLength= buf.readInt();
                short  additionalLength= buf.readShort();
                byte[]  body = new byte[bodyLength];
                byte[]  addtional = new byte[additionalLength];
                buf.readBytes(body);
                buf.readBytes(addtional);
                this.messageBody=
                        MessageBody.builder()
                                .messageId(messageId)
                                .body(new String(body,Charset.defaultCharset()))
                                .addtional(new String(addtional,Charset.defaultCharset()))
                                .build();
                this.checkpoint(Type.CRC);

            case CRC:
                out.add(TransportMessage.builder().type(type)
                        .from(from)
                        .to(to)
                        .messageBody(messageBody)
                        .timestammp(buf.readLong())
                        .build());
                this.checkpoint(Type.FIXD_HEADER);
        }
    }

    enum Type{
        FIXD_HEADER,
        TOPICHEADER,
        MESSAGEBODY,
        CRC
    }

}
