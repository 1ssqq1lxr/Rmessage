package io.rector.netty.transport.codec;

import io.netty.buffer.ByteBuf;
import io.reactor.netty.api.codec.TransportMessage;
import io.reactor.netty.api.frame.Frame;
import io.rector.netty.transport.connction.RConnection;
import io.rector.netty.transport.socket.ServerSocketAdapter;
import lombok.AllArgsConstructor;

/**
 * @Auther: lxr
 * @Date: 2018/12/26 17:08
 * @Description:
 */
@AllArgsConstructor
public class ServerDecoderAcceptor implements DecoderAcceptor{

    private ServerSocketAdapter serverSocketAdapter;

    private Frame frame;

    private RConnection rConnection;

    @Override
    public void transportMessage() { // 分发消息
        try {
            ByteBuf byteBuf=frame.content();
            byte header=byteBuf.readByte();
            if(obtainHigh(header) != TransportMessage.MESSAGE_HEADER){
                // 发送error消息
                serverSocketAdapter.removeConnection(rConnection).subscribe();
                return;
            }
            switch (obtainLow(header)){
                case ERROR:
                case ONE:
                case JOIN:
                case PING:
                case PONG:
                case GROUP:
                case LEAVE:
                case CONFIRM:
            }
        }
        catch (Exception e){

        }
        finally {// 保证释放内存
            int count = frame.refCnt();
            frame.release(count);
        }
    }



}
