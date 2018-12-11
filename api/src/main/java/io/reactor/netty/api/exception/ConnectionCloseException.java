package io.reactor.netty.api.exception;

/**
 * @Auther: lxr
 * @Date: 2018/12/11 11:33
 * @Description: 连接关闭异常
 */
public class ConnectionCloseException extends RuntimeException {

    public ConnectionCloseException(String message) {
        super(message);
    }
}
