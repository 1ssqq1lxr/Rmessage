package io.reactor.netty.api.codec;


import io.reactor.netty.api.ByteUtil;
import io.reactor.netty.api.exception.NotSupportException;
import lombok.Builder;
import lombok.Data;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

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

    private transient NettyOutbound outbound;

    private transient NettyInbound  inbound;

    private ProtocolCatagory type;

    private ClientType clientType;

    private Object messageBody;

    private boolean discard;


    public TransportMessage setOutbound(NettyOutbound outbound) {
        this.outbound = outbound;
        return this;
    }

    public TransportMessage setInbound(NettyInbound inbound) {
        this.inbound = inbound;
        return this;
    }


    public byte[] getBytes(){
        List<Byte> list =  new LinkedList<>();
        switch (type){
            case ONLINE:
                OnlineMessage onlineMessage =(OnlineMessage) messageBody;
                String userId = onlineMessage.getUserId();
                ByteUtil.byteToByteList(clientType.getType(),type.getNumber(),list);
                list.add((byte)userId.length());
                ByteUtil.byteArrayToList(userId.getBytes(),list);
                break;
            case PONG:
            case PING:
                ByteUtil.byteToByteList(clientType.getType(),type.getNumber(),list);
                break;
            case ONE:
            case GROUP:
                // 群聊
                MessageBody body =(MessageBody)this.messageBody;
                String from =body.getFrom();
                String to   = body.getTo();
                ByteUtil.byteToByteList(clientType.getType(),type.getNumber(),list);
                list.add((byte)from.length());
                list.add((byte)to.length());
                ByteUtil.byteArrayToList(from.getBytes(),list);
                ByteUtil.byteArrayToList(to.getBytes(),list);
                ByteUtil.longToByteList(body.getMessageId(),list);
                String by=body.getBody();
                String ad=body.getAddtional();
                ByteUtil.shortToByteList((short) by.length(),list);
                ByteUtil.shortToByteList((short) ad.length(),list);
                ByteUtil.byteArrayToList(by.getBytes(),list);
                ByteUtil.byteArrayToList(ad.getBytes(),list);
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
