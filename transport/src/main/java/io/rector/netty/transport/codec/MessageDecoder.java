package io.rector.netty.transport.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.reactor.netty.api.codec.*;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @Auther: lxr
 * @Date: 2018/12/19 10:59
 * @Description:
 *   type:  ONE   GROUP
 *   FIXHEADER
 *   |-----1byte--------------------|
 *   |客户端类型4bit| 消息类型低 4bit    |
 *
 *    TOPICHEADER
 *   ---1 byte ---------|--1 byte ------------|
 *   -from目的length----|---目的key length-----|
 *
 *   |-----n byte--------|-------n byte--------|
 *   |-----from目的-------|-------目的key--------|
 *
 *   MESSAGEBODY
 *   |---8 byte ---------|
 *   |---messageId ---------|
 *   |--------2 byte-----------|------2byte---------------------------|
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
 *  *   |固定头高4bit| 消息类型低 4bit  |
 *       * ACKBODY
 *  *   |-----8byte--------------------|
 *  *   |------messageId------------  |
 *
 *
 *   ONLINE  OFFLINE
 *
 *    *    *   FIXHEADER
 *  *  *   |-----1byte--------------------|
 *  *  *   |固定头高4bit| 消息类型低 4bit  |
 *  *       *  ON
 *  *  *   |-----1byte---------------| |-----=nbyte---------------|
 *  *  *   |     userId length       |  userId
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

    private ClientType clientType;





    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) {
        switch (state()){
            case FIXD_HEADER:
                byte header=buf.readByte();
                clientType=MessageUtils.obtainHigh(header);
                switch ((type=MessageUtils.obtainLow(header))){
                    case PING:
                        out.add(TransportMessage.builder().type(type).clientType(clientType).build());
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
                    case OFFLINE:
                        this.checkpoint(Type.CONNETION_STATE);
                        break;
                    case ONLINE:
                        this.checkpoint(Type.CONNETION_STATE);
                        break;
                    case PONG:
                        out.add(TransportMessage.builder().type(type).clientType(clientType).build());
                        this.checkpoint(Type.FIXD_HEADER);
                        break ;
                    case ONEACK:  //ack 暂时未实现
                        out.add(TransportMessage.builder().type(type).clientType(clientType).build());
                        this.checkpoint(Type.FIXD_HEADER);
                        break ;
                    case GROUPACK://ack 暂时未实现
                        this.checkpoint(Type.ACKBODY);
                        break ;
                    default:
                        super.discardSomeReadBytes();
                        this.checkpoint(Type.FIXD_HEADER);
                        return;
                }
                break;
            case CONNETION_STATE:
                short userIdLength= buf.readByte();
                byte[] userIdBytes = new byte[userIdLength];
                buf.readBytes(userIdBytes);
                out.add(TransportMessage.builder().clientType(clientType).type(type)
                        .messageBody(
                                ConnectionState.builder()
                                        .userId(new String(userIdBytes,Charset.defaultCharset()))
                                        .build()
                        )
                        .build());
                this.checkpoint(Type.FIXD_HEADER);
                break ;
            case ACKBODY:
                messageId=buf.readLong();
                out.add(TransportMessage.builder().clientType(clientType).type(type)
                        .messageBody(AckMessage.builder().messageId(messageId).build())
                        .build());
                this.checkpoint(Type.FIXD_HEADER);
                break  ;
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
                break ;
            case MESSAGEBODY:
                messageId=buf.readLong(); // 消息id
                int bodyLength= buf.readUnsignedShort();
                byte[]  bodyBytes = new byte[bodyLength];
                buf.readBytes(bodyBytes);
                body=new String(bodyBytes,Charset.defaultCharset());
                this.checkpoint(Type.CRC);
                break ;
            case CRC:
                out.add(TransportMessage.builder().clientType(clientType).type(type)
                        .messageBody(MessageBody.builder()
                                .from(from)
                                .to(to)
                                .messageId(messageId)
                                .body(body)
                                .timestammp(buf.readLong())
                                .build())
                        .build());
                this.checkpoint(Type.FIXD_HEADER);
                break ;
        }
    }

    enum Type{
        FIXD_HEADER,
        TOPICHEADER,
        MESSAGEBODY,
        CONNETION_STATE,
        ACKBODY,
        CRC
    }

}
