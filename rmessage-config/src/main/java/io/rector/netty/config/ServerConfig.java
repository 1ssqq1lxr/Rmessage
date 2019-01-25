package io.rector.netty.config;

import io.netty.channel.ChannelOption;
import io.reactor.netty.api.codec.Protocol;
import io.reactor.netty.api.exception.CheckConfigException;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @Auther: lxr
 * @Date: 2018/12/7 16:09
 * @Description:
 */
@Data
@Builder
public class ServerConfig implements Config{

    private Map<ChannelOption,Object> channelOption;

    private Protocol protocol;

    public   String ip;

    public   int port;

    @Override
    public boolean check() {
        if(protocol == null){
            throw new CheckConfigException("please select protocol");
        }
        if(ip == null || ip ==""){
            ip="127.0.0.1";
        }
        if(port == 0 ){
            throw new CheckConfigException("please set port");
        }
        return true;
    }

}
