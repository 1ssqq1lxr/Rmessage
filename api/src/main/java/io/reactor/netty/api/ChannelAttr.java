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

    private static final String   GROUPS   =  "GROUPS";

    public static  void boundUserId(NettyInbound inbound,String userId){
        AttributeKey<String> key = AttributeKey.valueOf(USER_ID);
        inbound.attr(key).getAndSet(userId);
    }


    public static  Optional<String> getUserId(NettyInbound inbound){
        AttributeKey<String> key = AttributeKey.valueOf(USER_ID);
        return Optional.ofNullable(inbound.attr(key).get());
    }

    public static  void boundGroups(NettyInbound inbound,List<String> groups){
        AttributeKey<List<String>> key = AttributeKey.valueOf(GROUPS);
        Optional<List<String>> ops=Optional.ofNullable(inbound.attr(key).get());
        if(ops.isPresent()){
            inbound.attr(key).set(groups);
        }
        else {
            inbound.attr(key).get().addAll(groups);
        }
    }

    public static  Optional<List<String> > getGroups(NettyInbound inbound){
        AttributeKey<List<String>> key = AttributeKey.valueOf(GROUPS);
        return Optional.ofNullable(inbound.attr(key).get());
    }

}
