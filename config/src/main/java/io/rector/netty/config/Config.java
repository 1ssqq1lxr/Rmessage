package io.rector.netty.config;

import io.reactor.netty.api.codec.Protocol;


/**
 * @Auther: lxr
 * @Date: 2018/12/7 16:09
 * @Description:
 */
public interface Config {

    void setIp(String ip);

    void setPort(int port);

    String getIp();

    int getPort();

    Protocol getProtocol();

    void setProtocol(Protocol protocol);

    boolean check();




}
