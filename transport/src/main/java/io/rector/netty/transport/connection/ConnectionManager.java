package io.rector.netty.transport.connection;

import io.reactor.netty.api.codec.RConnection;

import java.util.Set;

/**
 * @Auther: lxr
 * @Date: 2019/1/21 11:53
 * @Description:
 */
public interface ConnectionManager {

    void   acceptConnection(String user, RConnection connection);

    void  removeConnection(String user, RConnection connection);

    Set<RConnection>  getUserMultiConnection(String user);

    Set<RConnection> getConnections();
}
