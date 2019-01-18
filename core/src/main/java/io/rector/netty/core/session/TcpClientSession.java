package io.rector.netty.core.session;

import io.rector.netty.transport.socket.ClientSocketAdapter;

/**
 * @Auther: luxurong
 * @Date: 2019/1/18 16:20
 * @Description:
 **/
public class TcpClientSession  implements ClientSession{

    private final ClientSocketAdapter clientSocketAdapter;


    public TcpClientSession(ClientSocketAdapter clientSocketAdapter) {
        this.clientSocketAdapter = clientSocketAdapter;
    }

    @Override
    public void dispose() {
        clientSocketAdapter.closeServer().subscribe();
    }
}
