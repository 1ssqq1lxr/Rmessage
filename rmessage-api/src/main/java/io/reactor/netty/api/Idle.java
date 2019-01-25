package io.reactor.netty.api;

import lombok.Builder;
import lombok.Data;

import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2019/1/14 17:19
 * @Description:
 */
@Data
@Builder
public class Idle {

    private long  time;

    Supplier<Runnable> event;

}
