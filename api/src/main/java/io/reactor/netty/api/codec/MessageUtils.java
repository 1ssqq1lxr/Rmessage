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



    public  static  ClientType  obtainHigh(byte b){
        switch ((b>>4 & 0xFF)){
            case 1:
                return ClientType.Web;
            case 2:
                return ClientType.Android;
            case 3:
                return ClientType.Ios;
            default:
                return ClientType.Other;
        }
    }


    public  static ProtocolCatagory obtainLow(byte b) {
        switch (b & 0x0F) {
            case 0:
                return ProtocolCatagory.ONLINE;
            case 1:
                return ProtocolCatagory.ONE;
            case 2:
                return ProtocolCatagory.GROUP;
            case 3:
                return ProtocolCatagory.ACCEPT;
            case 10:
                return ProtocolCatagory.ONEACK;
            case 11:
                return ProtocolCatagory.GROUPACK;
            case 14:
                return ProtocolCatagory.PING;
            case 15:
                return ProtocolCatagory.PONG;
            default:
                throw new RuntimeException("不支持 protocol");
        }
    }


}
