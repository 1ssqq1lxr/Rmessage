package io.reactor.netty.api.codec;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Data;

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
public  class TransportMessage {

    private ProtocolCatagory type;

    private String from;

    private String to;

    private String messgae;

    private long   timestammp;


}
