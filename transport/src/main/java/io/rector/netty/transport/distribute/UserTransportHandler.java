package io.rector.netty.transport.distribute;


import java.util.Set;

/**
 * @Auther: lxr
 * @Date: 2019/1/21 15:36
 * @Description:
 */
public interface UserTransportHandler {

    // 是否允许user客户端登录
    boolean isAllowLogin(String user);

    // 发送point to point 消息 判断是否为好友
    boolean checkIsFriend(String from,String to);

    // 发送point to group 消息 判断是否为群组成员
    boolean checkIsGroup(String from ,String group);

    // 获取当前好友 用于发送离线通知
    Set<String> getFriends(String user);

}
