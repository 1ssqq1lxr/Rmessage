package io.rector.netty.transport.connection;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import io.reactor.netty.api.codec.RConnection;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Auther: lxr
 * @Date: 2019/1/21 14:22
 * @Description:
 */
public class DefaultConnectionManager implements  ConnectionManager {


    private Multimap<String, RConnection> multimap = Multimaps.synchronizedMultimap(ArrayListMultimap.create()); // user -> list<RConnection>


    @Override
    public void acceptConnection(String user, RConnection connection) {
         multimap.put(user,connection);
    }

    @Override
    public void removeConnection(String user, RConnection connection) {
         multimap.remove(user,connection);
    }

    @Override
    public Set<RConnection> getUserMultiConnection(String user) {
        return multimap.get(user).stream().collect(Collectors.toSet());
    }

    @Override
    public Set<Object> getConnections() {
        return multimap.values().stream().collect(Collectors.toSet());
    }


}
