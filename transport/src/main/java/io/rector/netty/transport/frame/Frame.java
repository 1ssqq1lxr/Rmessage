package io.rector.netty.transport.frame;

import com.sun.istack.internal.Nullable;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.Recycler;
import io.netty.util.ResourceLeakDetector;

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

    /** Return the content which is held by this {@link Frame}. */
    @Override
    public ByteBuf content() {
        if (content.refCnt() <= 0) {
            throw new IllegalReferenceCountException(content.refCnt());
        }
        return content;
    }

    /** Creates a deep copy of this {@link Frame}. */
    @Override
    public Frame copy() {
        return replace(content.copy());
    }

    /**
     * Duplicates this {@link Frame}. Be aware that this will not automatically call {@link
     * #retain()}.
     */
    @Override
    public Frame duplicate() {
        return replace(content.duplicate());
    }

    /**
     * Duplicates this {@link Frame}. This method returns a retained duplicate unlike {@link
     * #duplicate()}.
     *
     * @see ByteBuf#retainedDuplicate()
     */
    @Override
    public Frame retainedDuplicate() {
        return replace(content.retainedDuplicate());
    }

    /** Returns a new {@link Frame} which contains the specified {@code content}. */
    @Override
    public Frame replace(ByteBuf content) {
        return from(content);
    }

    /** Increases the reference count by {@code 1}. */
    @Override
    public Frame retain() {
        super.retain();
        return this;
    }

    /** Increases the reference count by the specified {@code increment}. */
    @Override
    public Frame retain(int increment) {
        super.retain(increment);
        return this;
    }

    /**
     * Records the current access location of this object for debugging purposes. If this object is
     * determined to be leaked, the information recorded by this operation will be provided to you via
     * {@link ResourceLeakDetector}. This method is a shortcut to {@link #touch(Object) touch(null)}.
     */
    @Override
    public Frame touch() {
        content.touch();
        return this;
    }

    /**
     * Records the current access location of this object with an additional arbitrary information for
     * debugging purposes. If this object is determined to be leaked, the information recorded by this
     * operation will be provided to you via {@link ResourceLeakDetector}.
     */
    @Override
    public Frame touch(@Nullable Object hint) {
        content.touch(hint);
        return this;
    }

    /** Called once {@link #refCnt()} is equals 0. */
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

