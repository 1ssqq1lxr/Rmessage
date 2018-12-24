package io.reactor.netty.api.codec;

import io.reactor.netty.api.frame.Frame;

/**
 * @Auther: lxr
 * @Date: 2018/12/24 17:29
 * @Description:
 */
@FunctionalInterface
public interface RDecoder {
    Frame decode(Frame frame);
}
