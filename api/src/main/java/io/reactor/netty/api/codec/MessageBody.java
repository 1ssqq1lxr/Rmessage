package io.reactor.netty.api.codec;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageBody {

    private String from;

    private String to;

    private long messageId;

    private String  body;

    private long   timestammp;



}
