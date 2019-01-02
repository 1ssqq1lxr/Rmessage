package io.reactor.netty.api.codec;

/**
 * @Auther: lxr
 * @Date: 2018/12/19 14:25
 * @Description:
 */
public enum  ProtocolCatagory {

    ONE((byte)1),   // 单发
    GROUP((byte)2),// 群发
    CONFIRM((byte)3),//确认身份机制
    ACCEPT((byte)0),// 接受消息
    JOIN((byte)12), //加入群组
    LEAVE((byte)13),//离开群组
    PING((byte)14),  //心跳
    PONG((byte)15)  //回复
    ;
    private byte number;

    ProtocolCatagory(byte i) {
        this.number=i;
    }

}
