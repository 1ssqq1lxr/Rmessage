//package io.reactor.netty.api.frame;
//
//
//import com.sun.istack.internal.Nullable;
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.ByteBufHolder;
//import io.netty.util.AbstractReferenceCounted;
//import io.netty.util.IllegalReferenceCountException;
//import io.netty.util.Recycler;
//
///**
// * @Auther: lxr
// * @Date: 2018/12/11 10:12
// * @Description:
// */
//public class ByteFrame extends AbstractReferenceCounted implements ByteBufHolder, Frame {
//    private static final Recycler<ByteFrame> RECYCLER =
//            new Recycler<ByteFrame>() {
//                protected ByteFrame newObject(Handle<ByteFrame> handle) {
//                    return new ByteFrame(handle);
//                }
//            };
//
//    private final Recycler.Handle<ByteFrame> handle;
//    private ByteBuf content;
//
//    private ByteFrame(final Recycler.Handle<ByteFrame> handle) {
//        this.handle = handle;
//    }
//
//    @Override
//    public ByteBuf content() {
//        if (content.refCnt() <= 0) {
//            throw new IllegalReferenceCountException(content.refCnt());
//        }
//        return content;
//    }
//
//    @Override
//    public ByteFrame copy() {
//        return replace(content.copy());
//    }
//
//    @Override
//    public ByteFrame duplicate() {
//        return replace(content.duplicate());
//    }
//
//    @Override
//    public ByteFrame retainedDuplicate() {
//        return replace(content.retainedDuplicate());
//    }
//
//    @Override
//    public ByteFrame replace(ByteBuf content) {
//        return from(content);
//    }
//
//    @Override
//    public ByteFrame retain() {
//        super.retain();
//        return this;
//    }
//
//    @Override
//    public ByteFrame retain(int increment) {
//        super.retain(increment);
//        return this;
//    }
//
//    @Override
//    public ByteFrame touch() {
//        content.touch();
//        return this;
//    }
//
//
//    @Override
//    public ByteFrame touch(@Nullable Object hint) {
//        content.touch(hint);
//        return this;
//    }
//
//    @Override
//    protected void deallocate() {
//        content.release();
//        content = null;
//        handle.recycle(this);
//    }
//
//
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (!(o instanceof ByteFrame)) {
//            return false;
//        }
//        final ByteFrame frame = (ByteFrame) o;
//        return content.equals(frame.content());
//    }
//
//    @Override
//    public int hashCode() {
//        return content.hashCode();
//    }
//
//    public static ByteFrame from(final ByteBuf content) {
//        final ByteFrame frame = RECYCLER.get();
//        frame.setRefCnt(1);
//        frame.content = content;
//        return frame;
//    }
//
//}
//
