package io.reactor.netty.api.codec;

/**
 * @Auther: lxr
 * @Date: 2018/12/19 14:25
 * @Description:
 */
public enum  ProtocolCatagory {
    ACCEPT((byte)0),// 客户端接受消息
    ONE((byte)1),   // 单发
    GROUP((byte)2),// 群发
    ONLINE((byte)3),//在线
    ADDUSER((byte)4),//添加好友
    DELUSER((byte)5),//删除好友
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
