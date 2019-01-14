package io.rector.netty.transport.distribute;

import io.reactor.netty.api.codec.TransportMessage;
import reactor.core.publisher.UnicastProcessor;

/**
 * @Auther: lxr
 * @Date: 2019/1/14 19:57
 * @Description:
 */
public class PullOfflineManager {

    private  OfflineMessageDistribute distribute;

    private  UnicastProcessor<TransportMessage> messages;



}
