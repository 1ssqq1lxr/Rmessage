package io.rector.netty.transport.distribute;

import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * @Auther: lxr
 * @Date: 2019/1/21 15:36
 * @Description:
 */
public interface UserTransportHandler {

    boolean isAllowLogin(String user);

    boolean checkIsFriend(String from,String to);

    boolean checkIsGroup(String from ,String group);

    Set<String> getFriends(String user);

}
