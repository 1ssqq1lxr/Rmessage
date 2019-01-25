package io.reactor.netty.api.codec;

import lombok.Builder;
import lombok.Data;

/**
 * @Auther: luxurong
 * @Date: 2019/1/16 23:22
 * @Description:
 **/
@Data
@Builder
public class OfflineMessage {

    private TransportMessage message;

    private String userId;


}
