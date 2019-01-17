package io.reactor.netty.api;

import java.io.*;
import java.util.List;

/**
 * @Auther: lxr
 * @Date: 2019/1/17 16:16
 * @Description: 操作字节
 */
public class ByteUtil {

    public static byte intToByte(int x) {
        return (byte) x;
    }

    public static int byteToInt(byte b) {
        //Java的byte是有符号，通过 &0xFF转为无符号
        return b & 0xFF;
    }

    public static int byteArrayToInt(byte[] b) {
        return   b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }
    public static int byteArrayToInt(byte[] b, int index){
        return   b[index+3] & 0xFF |
                (b[index+2] & 0xFF) << 8 |
                (b[index+1] & 0xFF) << 16 |
                (b[index+0] & 0xFF) << 24;
    }

    public static byte[] longToByteArray(long a) {
        return new byte[] {
                (byte) ((a >> 56) & 0xFF),
                (byte) ((a >> 48) & 0xFF),
                (byte) ((a >> 40) & 0xFF),
                (byte) ((a >> 32) & 0xFF),
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    public static void byteToByteArray(byte high,byte low,List<Byte> list) {
       list.add((byte)((high << 4 )|(low & 0x0F)));
    }



    public static void longToByteArray(long a, List<Byte> list) {
        list.add((byte) ((a >> 56) & 0xFF));
        list.add((byte) ((a >> 48) & 0xFF));
        list.add((byte) ((a >> 40) & 0xFF));
        list.add((byte) ((a >> 32) & 0xFF));
        list.add((byte) ((a >> 24) & 0xFF));
        list.add((byte) ((a >> 16) & 0xFF));
        list.add((byte) ((a >> 8) & 0xFF));
        list.add((byte) (a  & 0xFF));
    }



    public static void intToByteArray(int a, List<Byte> list) {
        list.add((byte) ((a >> 24) & 0xFF));
        list.add((byte) ((a >> 16) & 0xFF));
        list.add((byte) ((a >> 8) & 0xFF));
        list.add((byte) (a  & 0xFF));
    }

    public static byte[] intToByteArray(int a) {
        return new byte[] {
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }


    public static void byteArrToShort(byte b[], short s, int index) {
        b[index + 1] = (byte) (s >> 8);
        b[index + 0] = (byte) (s >> 0);
    }

    public static short byteArrToShort(byte[] b, int index) {
        return (short) (((b[index] << 8) | b[index + 1] & 0xff));
    }

    public static byte[] shortToByteArr(short s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    public static short byteArrToShort(byte[] b){
        return byteArrToShort(b,0);
    }



    public static byte[] getByteArr(byte[]data,int start ,int end){
        byte[] ret=new byte[end-start];
        for(int i=0;(start+i)<end;i++){
            ret[i]=data[start+i];
        }
        return ret;
    }


    public static boolean isEq(byte[] s1,byte[] s2){
        int slen=s1.length;
        if(slen==s2.length){
            for(int index=0;index<slen;index++){
                if(s1[index]!=s2[index]){
                    return false;
                }
            }
            return true;
        }
        return  false;
    }

    public static String getString(byte[] s1,String encode,String err){
        try {
            return new String(s1,encode);
        } catch (UnsupportedEncodingException e) {
            return err==null?null:err;
        }
    }

    public static String getString(byte[] s1,String encode){
        return getString(s1,encode,null);
    }

    public static String byteArrToHexString(byte[] b){
        String result="";
        for (byte aB : b) {
            result += Integer.toString((aB & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }


    public static int hexStringToInt(String hexString){
        return Integer.parseInt(hexString,16);
    }

    public static String intToBinary(int i){
        return Integer.toBinaryString(i);
    }

}
