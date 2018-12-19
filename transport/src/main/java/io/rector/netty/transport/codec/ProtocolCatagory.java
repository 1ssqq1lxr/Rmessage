package io.rector.netty.transport.codec;

/**
 * @Auther: lxr
 * @Date: 2018/12/19 14:25
 * @Description:
 */
public enum  ProtocolCatagory {

    CONFIRM((byte)0),// 确认身份机制
    ONE((byte)1), //单发
    GROUP((byte)2), //群发
    PING((byte)14),  // 心跳
    PONG((byte)15)  // 回复
    ;
    private byte number;

    ProtocolCatagory(byte i) {
        this.number=i;
    }
}
