package io.rector.netty.config;

import io.reactor.netty.api.codec.Protocol;
import lombok.Builder;
import lombok.Data;

/**
 * @Auther: lxr
 * @Date: 2018/12/7 16:09
 * @Description:
 */
@Data
@Builder
public class ServerConfig implements Config{

    private Protocol protocol;

    public   String ip;

    public   int port;

}
