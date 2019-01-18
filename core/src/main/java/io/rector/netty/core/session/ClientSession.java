package io.rector.netty.core.session;

import reactor.core.Disposable;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

/**
 * @Auther: luxurong
 * @Date: 2019/1/18 16:20
 * @Description: 客户端session操作类
 **/
public interface ClientSession <T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>>  extends Disposable {





}
