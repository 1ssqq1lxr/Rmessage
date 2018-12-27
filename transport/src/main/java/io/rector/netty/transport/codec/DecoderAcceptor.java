package io.rector.netty.transport.codec;


import io.reactor.netty.api.codec.ProtocolCatagory;

/**
 * @Auther: lxr
 * @Date: 2018/12/26 17:13
 * @Description:
 */
public interface DecoderAcceptor {

     void transportMessage();

     default  int  obtainHigh(byte b){
          return  b>>4 & 0x0F ;
     }

     default ProtocolCatagory obtainLow(byte b) {
          switch (b & 0x0F) {
               case 1:
                    return ProtocolCatagory.ONE;
               case 0:
                    return ProtocolCatagory.CONFIRM;

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
