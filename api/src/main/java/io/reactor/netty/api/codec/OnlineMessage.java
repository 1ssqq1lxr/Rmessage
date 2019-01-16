package io.reactor.netty.api.codec;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OnlineMessage {

    private String userId;


}
