package io.rector.netty.transport.codec;

/**
 * @Auther: lxr
 * @Date: 2018/12/19 14:24
 * @Description:
 *  {
 *     "type" :1,
 *     "fromId": "123",
 *     "toKey": "678",
 *     "message": "123123"
 *     "timestammp": 123123123213123
 *  }
 *
 */
public abstract class TransportMessage {

    private ProtocolCatagory type;

    private String fromId;

    private String toKey;

    private String messgae;

    private long   timestammp;

}
