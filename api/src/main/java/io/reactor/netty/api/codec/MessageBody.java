package io.reactor.netty.api.codec;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageBody {


    private long messageId;


    private String  body;

    private String  addtional;


}
