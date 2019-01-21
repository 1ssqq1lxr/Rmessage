package io.reactor.netty.api.codec;

import lombok.Getter;

/**
 * @Auther: lxr
 * @Date: 2018/12/19 14:25
 * @Description:
 */
@Getter
public enum  ProtocolCatagory {
    ONLINE((byte)0), //在线
    ONE((byte)1),    // 单发
    GROUP((byte)2),  // 群发
    OFFLINE((byte)3),//离线
    ONEACK((byte)10),
    GROUPACK((byte)11),
    PING((byte)14),  //心跳
    PONG((byte)15) //回复
    ;
    private byte number;

    ProtocolCatagory(byte i) {
        this.number=i;
    }

}
