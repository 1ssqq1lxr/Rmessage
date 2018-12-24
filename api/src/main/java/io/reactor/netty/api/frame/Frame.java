package io.reactor.netty.api.frame;

import io.netty.buffer.ByteBufHolder;
import io.reactor.netty.api.codec.RDecoder;

/**
 * @Auther: lxr
 * @Date: 2018/12/24 17:22
 * @Description:
 */
public interface Frame  extends ByteBufHolder {

    Frame decode(RDecoder rDecoder);

}
