package io.rector.netty.transport.codec;

import io.netty.buffer.ByteBuf;
import io.reactor.netty.api.codec.TransportMessage;
import io.reactor.netty.api.frame.Frame;
import io.rector.netty.transport.Transport;
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

    private TransportMessage  message;

    private RConnection rConnection;

    @Override
    public void transportMessage() { // 分发消息

    }



}
