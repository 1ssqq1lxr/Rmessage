package io.rector.netty.transport.codec;

import io.netty.buffer.ByteBuf;
import io.reactor.netty.api.frame.Frame;
import io.rector.netty.transport.connction.RConnection;
import io.rector.netty.transport.distribute.ServerMessageDistribute;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: lxr
 * @Date: 2018/12/26 17:08
 * @Description:
 */
@AllArgsConstructor
@Slf4j
public class ServerDecoderAcceptor implements DecoderAcceptor{

    private ServerMessageDistribute distribute;

    private Frame frame;

    private RConnection rConnection;

    @Override
    public void transportMessage() { // 分发消息
        try {
            ByteBuf byteBuf=frame.content();
            byte header=byteBuf.readByte();
//            if(obtainHigh(header) != TransportMessage.MESSAGE_HEADER){
//                distribute.getServerSocketAdapter().removeConnection(rConnection).subscribe();
//                return;
//            }
//            switch (obtainLow(header)){
//                case ONE:
//                   byte userLength = byteBuf.readByte();
//                   int  msgLength= byteBuf.readInt();
//                   byte[] userByte = new byte[userLength];
//                   byteBuf.readBytes(userByte);
//                   String user = new String(userByte, Charset.defaultCharset());
//
//                   break;
//                case JOIN:
//                case PING:
//                case PONG:
//                case GROUP:
//                case LEAVE:
//                case CONFIRM:
//            }
        }
        catch (Exception e){

        }
        finally {// 保证释放内存
            int count = frame.refCnt();
            frame.release(count);
        }
    }



}
