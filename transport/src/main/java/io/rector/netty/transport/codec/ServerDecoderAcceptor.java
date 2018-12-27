package io.rector.netty.transport.codec;

import io.netty.buffer.ByteBuf;
import io.reactor.netty.api.frame.Frame;
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

    @Override
    public void transportMessage() { // 分发消息
        try {
            ByteBuf byteBuf=frame.content();
            if(byteBuf.readableBytes()<0)
                return;

        }
        finally {// 保证释放内存
            int count = frame.refCnt();
            frame.release(count);
        }
    }



}
