package io.rector.netty.transport.connection;

/**
 * @Auther: lxr
 * @Date: 2019/1/21 14:20
 * @Description:
 */
public class ConnectionFactory {

    public ConnectionManager getConnectionManager(){
        return init().transform();
    }



    private ConnectionInterface init(){
        return DefaultConnectionManager::new;
    }


    interface  ConnectionInterface{
        ConnectionManager transform();
    }

}
