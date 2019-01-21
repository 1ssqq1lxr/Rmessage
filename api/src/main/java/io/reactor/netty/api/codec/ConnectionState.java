package io.reactor.netty.api.codec;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ConnectionState {

    private String userId;


}
