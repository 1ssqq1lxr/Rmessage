package io.rector.netty.transport.distribute.def;

import io.rector.netty.transport.distribute.UserTransportHandler;

import java.util.Set;

/**
 * @Auther: lxr
 * @Date: 2019/1/21 16:01
 * @Description:
 */
public class DefaultUserTransportHandler implements UserTransportHandler {
    @Override
    public boolean isAllowLogin(String user) {
        return true;
    }

    @Override
    public boolean checkIsFriend(String from, String to) {
        return true;
    }

    @Override
    public boolean checkIsGroup(String from, String group) {
        return true;
    }

    @Override
    public Set<String> getFriends(String user) {
        return null;
    }
}
