package io.reactor.netty.api.codec;


import io.reactor.netty.api.ByteUtil;
import lombok.Builder;
import lombok.Data;

import java.nio.charset.Charset;
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

    private ProtocolCatagory type;

    private ClientType clientType;

    private Object messageBody;

    private transient boolean discard;

    private transient RConnection connection;


    public TransportMessage setConnection(RConnection connection) {
        this.connection = connection;
        return this;
    }



    public byte[] getBytes(){
        List<Byte> list =  new LinkedList<>();
        switch (type){
            case OFFLINE:
                ConnectionState connectionState =(ConnectionState) messageBody;
                byte[] userId = connectionState.getUserId().getBytes();
                ByteUtil.byteToByteList(clientType.getType(),type.getNumber(),list);
                list.add((byte)userId.length);
                ByteUtil.byteArrayToList(userId,list);
                break;
            case ONLINE:
                ConnectionState state =(ConnectionState) messageBody;
                byte[] user = state.getUserId().getBytes();
                byte[] pwd = state.getPassword().getBytes();
                ByteUtil.byteToByteList(clientType.getType(),type.getNumber(),list);
                list.add((byte)user.length);
                list.add((byte)pwd.length);
                ByteUtil.byteArrayToList(user,list);
                ByteUtil.byteArrayToList(pwd,list);
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
