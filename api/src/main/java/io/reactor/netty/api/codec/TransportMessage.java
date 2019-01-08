package io.reactor.netty.api.codec;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Builder;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.nio.Buffer;

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

    private ProtocolCatagory type;

    private String from;

    private String to;

    private MessageBody messageBody;

    private long   timestammp;

    private boolean discard;

    public Mono<byte[]> toBytes(){
        return Mono.create(monoSink -> {
            monoSink.success(null);
        });
    }


}
