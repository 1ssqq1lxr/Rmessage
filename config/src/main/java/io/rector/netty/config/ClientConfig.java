package io.rector.netty.config;

import io.reactor.netty.api.codec.ClientType;
import io.reactor.netty.api.codec.Protocol;
import lombok.Builder;
import lombok.Data;

/**
 * @Auther: luxurong
 * @Date: 2019/1/18 13:43
 * @Description:
 **/
@Data
@Builder
public class ClientConfig  implements   Config{

    private Protocol protocol;

    public  String ip;

    public  int port;

    public  ClientType clientType;

    private String  userId;

}
