package io.reactor.netty.api.codec;


import io.reactor.netty.api.ByteUtil;
import io.reactor.netty.api.exception.NotSupportException;
import lombok.Builder;
import lombok.Data;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @Auther: lxr
 * @Date: 2018/12/19 14:24
 * @Description: 消息传输 entity
 *
 */
@Data
@Builder
public  class TransportMessage {

    private transient RConnection connection;

    private ProtocolCatagory type;

    private ClientType clientType;

    private Object messageBody;

    private transient boolean discard;


    public TransportMessage setConnection(RConnection connection) {
        this.connection = connection;
        return this;
    }



    public byte[] getBytes(){
        List<Byte> list =  new LinkedList<>();
        switch (type){
            case ONLINE:
                OnlineMessage onlineMessage =(OnlineMessage) messageBody;
                byte[] userId = onlineMessage.getUserId().getBytes();
                ByteUtil.byteToByteList(clientType.getType(),type.getNumber(),list);
                list.add((byte)userId.length);
                ByteUtil.byteArrayToList(userId,list);
                break;
            case PONG:
            case PING:
                ByteUtil.byteToByteList(clientType.getType(),type.getNumber(),list);
                break;
            case ONE:
            case GROUP:
                MessageBody body =(MessageBody)this.messageBody;
                byte[] from =body.getFrom().getBytes(Charset.defaultCharset());
                byte[] to   = body.getTo().getBytes(Charset.defaultCharset());
                ByteUtil.byteToByteList(clientType.getType(),type.getNumber(),list);
                list.add((byte)from.length);
                list.add((byte)to.length);
                ByteUtil.byteArrayToList(from,list);
                ByteUtil.byteArrayToList(to,list);
                ByteUtil.longToByteList(body.getMessageId(),list);
                byte[] by=body.getBody().getBytes();
                ByteUtil.shortToByteList((short) by.length,list);
                ByteUtil.byteArrayToList(by,list);
                ByteUtil.longToByteList(body.getTimestammp(),list);
                break;
            case ONEACK:
            case GROUPACK:
                AckMessage groupAckMessage =(AckMessage)this.messageBody;
                ByteUtil.byteToByteList(clientType.getType(),type.getNumber(),list);
                ByteUtil.longToByteList(groupAckMessage.getMessageId(),list);
                break;
        }
        return   getBytes(list) ;
    }



    private byte[] getBytes(List<Byte> list) {
        if( list==null ||  list.size()==0){
            return new byte[]{};
        }
        else {
            byte[]  bytes = new byte[list.size()];
            int index=0;
            for(byte b:list){
                bytes[index]=b;
                index++;
            }
            return bytes;
        }

    }


}
