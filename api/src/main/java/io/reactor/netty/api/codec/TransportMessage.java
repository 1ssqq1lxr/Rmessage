package io.reactor.netty.api.codec;


import lombok.Builder;
import lombok.Data;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

/**
 * @Auther: lxr
 * @Date: 2018/12/19 14:24
 * @Description:
 *  {
 *     "type" :1,
 *     "from": "123",
 *     "to": "678",
 *     "message": "123123"
 *     "timestammp": 123123123213123
 *  }
 *
 */
@Data
@Builder
public  class TransportMessage {

    private transient NettyOutbound outbound;

    private transient NettyInbound  inbound;

    private ProtocolCatagory type;

    private Object messageBody;


    private boolean discard;

    public Mono<byte[]> toBytes(){
        return Mono.create(monoSink -> {
            monoSink.success(null);
        });
    }

    public TransportMessage setOutbound(NettyOutbound outbound) {
        this.outbound = outbound;
        return this;
    }

    public TransportMessage setInbound(NettyInbound inbound) {
        this.inbound = inbound;
        return this;
    }
}
