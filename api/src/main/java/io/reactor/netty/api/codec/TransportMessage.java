package io.reactor.netty.api.codec;


import io.reactor.netty.api.ByteUtil;
import io.reactor.netty.api.exception.NotSupportException;
import lombok.Builder;
import lombok.Data;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

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

            case ACCEPT:

            case GROUPACK:
                AckMessage groupAckMessage =(AckMessage)this.messageBody;
                ByteUtil.byteToByteArray(clientType.getType(),type.getNumber(),list);
                ByteUtil.longToByteArray(groupAckMessage.getMessageId(),list);
                break;
            case PONG:
                ByteUtil.byteToByteArray(clientType.getType(),type.getNumber(),list);
                break;
            case GROUP:
            case PING:

            case ONE:

            case ONEACK:
                AckMessage oneAckMessage =(AckMessage)this.messageBody;
                ByteUtil.byteToByteArray(clientType.getType(),type.getNumber(),list);
                ByteUtil.longToByteArray(oneAckMessage.getMessageId(),list);
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
            for(byte b:bytes){
                bytes[index]=b;
                index++;
            }
            return bytes;
        }

    }


}
