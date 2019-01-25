package io.reactor.netty.api.codec;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AckMessage {

    private long messageId;


}
