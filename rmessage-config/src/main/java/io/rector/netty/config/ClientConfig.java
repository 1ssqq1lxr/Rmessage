package io.rector.netty.config;

import io.netty.channel.ChannelOption;
import io.reactor.netty.api.codec.ClientType;
import io.reactor.netty.api.codec.Protocol;
import io.reactor.netty.api.exception.CheckConfigException;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

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

    private String  password;

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
        if(clientType == null){
            throw new CheckConfigException("please set clientType");
        }
        if(userId == null){
            throw new CheckConfigException("please set userId");
        }

        if(password == null){
            throw new CheckConfigException("please set password");
        }
        return true;
    }

    private Map<ChannelOption,Object> channelOption;
}
