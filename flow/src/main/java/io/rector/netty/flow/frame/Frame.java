package io.rector.netty.flow.frame;


import com.sun.istack.internal.Nullable;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.Recycler;

/**
 * @Auther: lxr
 * @Date: 2018/12/11 10:12
 * @Description:
 */
public class Frame extends AbstractReferenceCounted implements ByteBufHolder {
    private static final Recycler<Frame> RECYCLER =
            new Recycler<Frame>() {
                protected Frame newObject(Handle<Frame> handle) {
                    return new Frame(handle);
                }
            };

    private final Recycler.Handle<Frame> handle;
    private ByteBuf content;

    private Frame(final Recycler.Handle<Frame> handle) {
        this.handle = handle;
    }

    @Override
    public ByteBuf content() {
        if (content.refCnt() <= 0) {
            throw new IllegalReferenceCountException(content.refCnt());
        }
        return content;
    }

    @Override
    public Frame copy() {
        return replace(content.copy());
    }

    @Override
    public Frame duplicate() {
        return replace(content.duplicate());
    }

    @Override
    public Frame retainedDuplicate() {
        return replace(content.retainedDuplicate());
    }

    @Override
    public Frame replace(ByteBuf content) {
        return from(content);
    }

    @Override
    public Frame retain() {
        super.retain();
        return this;
    }

    @Override
    public Frame retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override
    public Frame touch() {
        content.touch();
        return this;
    }


    @Override
    public Frame touch(@Nullable Object hint) {
        content.touch(hint);
        return this;
    }

    @Override
    protected void deallocate() {
        content.release();
        content = null;
        handle.recycle(this);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Frame)) {
            return false;
        }
        final Frame frame = (Frame) o;
        return content.equals(frame.content());
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }

    public static Frame from(final ByteBuf content) {
        final Frame frame = RECYCLER.get();
        frame.setRefCnt(1);
        frame.content = content;

        return frame;
    }

}

