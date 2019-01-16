package io.rector.netty.transport.group;

import java.util.Set;

/**
 * @Auther: lxr
 * @Date: 2019/1/16 09:40
 * @Description:
 */
public @FunctionalInterface interface GroupCollector {

    Set<String>   loadGroupUser(String groupId);

}
