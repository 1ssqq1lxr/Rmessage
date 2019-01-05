package io.reactor.netty.api.codec;

/**
 * @Auther: lxr
 * @Date: 2018/12/28 10:11
 * @Description:
 *  {
 *
 *  }
 */
public class MessageUtils {


    public static  final  byte MESSAGE_HEADER = 0x0F;

    public  static  boolean  obtainHigh(byte b){
        return  (b>>4 & 0xFF) ==  MESSAGE_HEADER;
    }

    public  static ProtocolCatagory obtainLow(byte b) {
        switch (b & 0x0F) {
            case 0:
                return ProtocolCatagory.ACCEPT;
            case 1:
                return ProtocolCatagory.ONE;
            case 2:
                return ProtocolCatagory.GROUP;
            case 3:
//                return ProtocolCatagory.;

            case 12:
                return ProtocolCatagory.JOIN;
            case 13:
                return ProtocolCatagory.LEAVE;
            case 14:
                return ProtocolCatagory.PING;
            case 15:
                return ProtocolCatagory.PONG;
            default:
                throw new RuntimeException("不支持 protocol");
        }
    }


}
