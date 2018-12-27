package io.rector.netty.transport.codec;

import io.reactor.netty.api.frame.Frame;
import io.rector.netty.transport.connction.RConnection;
import io.rector.netty.transport.socket.ServerSocketAdapter;

/**
 * @Auther: lxr
 * @Date: 2018/12/26 17:11
 * @Description:
 */
public interface Rdocoder {
    DecoderAcceptor decoder(ServerSocketAdapter serverSocketAdapter, Frame frame, RConnection rConnection);
}
