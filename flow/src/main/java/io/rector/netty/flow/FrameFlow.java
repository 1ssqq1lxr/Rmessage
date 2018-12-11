package io.rector.netty.flow;


import java.util.function.Consumer;


/**
 * @Auther: lxr
 * @Date: 2018/12/11 14:35
 * @Description:
 */
public class FrameFlow {

    private String name;

    public FrameFlow(String name) {
        this.name = name;
    }

    public Consumer<FrameFlow> apply(F flowConsumer){
        flowConsumer.accept();
    }

}
