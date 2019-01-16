package io.reactor.netty.api;

import io.netty.util.AttributeKey;
import reactor.ipc.netty.NettyInbound;

import java.util.List;
import java.util.Optional;

/**
 * @Auther: lxr
 * @Date: 2018/12/24 14:41
 * @Description:
 */
public class ChannelAttr {

    private static final String   USER_ID   =  "USERID";

    public static  void boundUserId(NettyInbound inbound,String userId){
        AttributeKey<String> key = AttributeKey.valueOf(USER_ID);
        inbound.attr(key).getAndSet(userId);
    }


    public static  Optional<String> getUserId(NettyInbound inbound){
        AttributeKey<String> key = AttributeKey.valueOf(USER_ID);
        return Optional.ofNullable(inbound.attr(key).get());
    }


}
