package io.rector.netty.transport.connection;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: lxr
 * @Date: 2019/1/21 11:53
 * @Description: connection relation manager
 */
@Setter
@Getter
public class ConnectionRelation {


    private Multimap<String,RConnection> multimap = Multimaps.synchronizedMultimap(ArrayListMultimap.create()); // user -> list<RConnection>


}
