package io.rector.netty.transport.distribute;

import io.rector.netty.transport.connction.RConnection;
import io.rector.netty.transport.socket.ServerSocketAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.function.Consumer;

/**
 * @Auther: lxr
 * @Date: 2018/12/27 19:23
 * @Description: 消息传输逻辑
 */
@AllArgsConstructor
@Data
public class ServerMessageDistribute {

    private final ServerSocketAdapter serverSocketAdapter;

    /**
     * 一对一发送
     */
    public void  sendOne(String key, Consumer<RConnection> connection){
        connection.accept((RConnection) serverSocketAdapter.getIds().get(key));
    }




}
