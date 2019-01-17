package io.rector.netty.transport.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.reactor.netty.api.codec.*;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: lxr
 * @Date: 2018/12/19 10:59
 * @Description:
 *   type:  ONE   GROUP
 *   FIXHEADER
 *   |-----1byte--------------------|
 *   |客户端类型4bit| 消息类型低 4bit    |
 *
 *  TOPICHEADER
 *   ---1 byte ---------|--1 byte ------------|
 *   |--mesageId --------|--from目的length----|---目的key length-----|
 *
 *   |-----n byte--------|-------n byte--------|
 *   |-----from目的-------|-------目的key--------|
 *
 *   MESSAGEBODY
 *   |---4 byte ---------|
 *   |---messageId ---------|
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
 *
 *
 *   ACK
 *    *   FIXHEADER
 *  *   |-----1byte--------------------|
 *  *   |固定头高4bit| 消息类型低 4bit    |
 *       * ACKBODY
 *  *   |-----1byte--------------------|
 *  *   |固定头高4bit| 消息类型低 4bit    |
 *
 *
 *   ONLINE
 *
 *    *    *   FIXHEADER
 *  *  *   |-----1byte--------------------|
 *  *  *   |固定头高4bit| 消息类型低 4bit  |
 *  *       *  ON
 *  *  *   |-----1byte---------|
 *  *  *   |     userId        |
 *
 * @see ProtocolCatagory
 */


public class MessageDecoder extends ReplayingDecoder<MessageDecoder.Type> {

    public MessageDecoder() {
        super(Type.FIXD_HEADER);
    }

    private ProtocolCatagory type;

    private String from;

    private String to;

    private long  messageId;

    private String body;

    private String  addtional;

    private ClientType clientType;





    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) {
        header:switch (state()){
            case FIXD_HEADER:
                byte header=buf.readByte();
                ClientType clientType=MessageUtils.obtainHigh(header);
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
                    case ONLINE:
                        this.checkpoint(Type.ONLINE);
                        break;
                    case GROUPACK:
                        this.checkpoint(Type.ACKBODY);
                        break ;
                    case ACCEPT:
                        this.checkpoint(Type.ACKBODY);
                        break ;
                    default:
                        super.discardSomeReadBytes();
                        this.checkpoint(Type.FIXD_HEADER);
                        return;
                }
            case ONLINE:
                short userIdLength= buf.readByte();
                byte[] userIdBytes = new byte[userIdLength];
                buf.readBytes(userIdBytes);
                out.add(TransportMessage.builder().type(type)
                        .messageBody(
                                OnlineMessage.builder()
                                        .userId(new String(userIdBytes,Charset.defaultCharset()))
                                        .build()
                        )
                        .build());
                this.checkpoint(Type.FIXD_HEADER);
                break  header;
            case ACKBODY:
                messageId=buf.readLong();
                out.add(TransportMessage.builder().type(type)
                        .messageBody(AckMessage.builder().messageId(messageId).build())
                        .build());
                this.checkpoint(Type.FIXD_HEADER);
                break  header;
            case TOPICHEADER:
                short fromlength= buf.readByte();
                short tolength= buf.readByte();
                byte[] fromBytes = new byte[fromlength];
                byte[] toBytes = new byte[tolength];
                buf.readBytes(fromBytes);
                buf.readBytes(toBytes);
                from =new String(fromBytes, Charset.defaultCharset());
                to   =new String(toBytes, Charset.defaultCharset());
                this.checkpoint(Type.MESSAGEBODY);
            case MESSAGEBODY:
                messageId=buf.readLong(); // 消息id
                int bodyLength= buf.readInt();
                short  additionalLength= buf.readShort();
                byte[]  bodyBytes = new byte[bodyLength];
                byte[]  addtionalBytes = new byte[additionalLength];
                buf.readBytes(bodyBytes);
                buf.readBytes(addtionalBytes);
                body=new String(bodyBytes,Charset.defaultCharset());
                addtional=new String(addtionalBytes,Charset.defaultCharset());
                this.checkpoint(Type.CRC);

            case CRC:
                out.add(TransportMessage.builder().type(type)
                        .messageBody(MessageBody.builder()
                                .messageId(messageId)
                                .body(body)
                                .addtional(addtional)
                                .timestammp(buf.readLong())
                                .build())
                        .build());
                this.checkpoint(Type.FIXD_HEADER);
        }
    }

    enum Type{
        FIXD_HEADER,
        TOPICHEADER,
        MESSAGEBODY,
        ONLINE,
        ACKBODY,
        CRC
    }

}
