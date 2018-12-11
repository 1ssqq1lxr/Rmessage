package io.rector.netty.transport.connction;

import io.rector.netty.transport.Transport;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyContext;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2018/11/4 17:30
 * @Description:
 */
@Data
public class RConnection implements Connection {

    private NettyInbound inbound;

    private NettyOutbound outbound;

    private NettyContext context;


    public RConnection(NettyInbound inbound, NettyOutbound outbound, NettyContext context) {
        this.inbound = inbound;
        this.outbound = outbound;
        this.context = context;
        context.onClose(() -> context.dispose());
    }

    @Override
    public Mono<Void> dispose() {
        return Mono.defer(()->{
            if(!context.isDisposed())
                context.dispose();
            return Mono.empty();
        });
    }

    @Override
    public Mono<NettyInbound> onReadIdle(Supplier<? extends Runnable> readLe) {
//        return onReadIdle(connectionConfig.getReadWriteIdel(),readLe);
        return Mono.empty();
    }

    @Override
    public Mono<NettyInbound> onReadIdle(Long l, Supplier<? extends Runnable> readLe) {
        return   Mono.just(inbound.onReadIdle(l,readLe.get()));
    }

    @Override
    public Mono<NettyOutbound> onWriteIdle(Supplier< ? extends Runnable> writeLe) {
//        return  onWriteIdle(connectionConfig.getReadWriteIdel(),writeLe) ;
        return Mono.empty();
    }

    @Override
    public Mono<NettyOutbound> onWriteIdle(Long l, Supplier<? extends Runnable> write) {
        return Mono.just(outbound.onWriteIdle(l,write.get()));
    }

    @Override
    public <T> Flux<T> receiveMsg(Class<T> contentClass) {
        return inbound.receiveObject().cast(contentClass);
    }

    @Override
    public void onClose(Runnable close) {
        context.onClose(close);
    }
}
