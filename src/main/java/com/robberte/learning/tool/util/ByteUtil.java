package com.robberte.learning.tool.util;

/**
 * 字节操作相关工具类
 * @author robberte
 * @date 2018/7/22 下午11:34
 */
public class ByteUtil {

    /**
     * 将字节数组转成二进制数组
     * @param bytes
     * @return
     */
    public static boolean[] getBinaryFromBytes(byte[] bytes) {
        boolean[] binary = new boolean[bytes.length * 8];
        int offset = 0;
        for(int i = 0; i < bytes.length; i++) {
            boolean[] byteBinnary = getBinaryFromByte(bytes[i]);
            System.arraycopy(byteBinnary, 0, binary, offset, byteBinnary.length);
            offset += byteBinnary.length;
        }
        return binary;
    }


    /**
     * 将字节转化成二进制数组
     * @param b
     * @return
     */
    public static boolean[] getBinaryFromByte(byte b) {
        boolean[] binary = new boolean[8];
        for(int i = 0; i < 8; i++) {
            binary[i] = pos(b, i);
        }
        return binary;
    }

    /**
     * 获取字节在某个位置上的值，从左到算起，编号分别为0～7
     * @param b 字节
     * @param pos 位置
     * @return
     */
    public static boolean pos(byte b, int pos) {
        byte tmp = (byte)(0x01 << (8 - pos - 1));
        return (tmp & b) == tmp;
    }

    /**
     * 从字节截取截取数字，从左向右截取，坐标从0开始
     * @param bytes
     * @param begin
     * @param length
     * @return
     */
    public static int subBinary(byte[] bytes, int begin, int length) {
        return subBinary(getBinaryFromBytes(bytes), begin, length);
    }

    /**
     * 截取二进制数组，截取后转化为数字
     * @param binary
     * @param begin
     * @param length
     * @return
     */
    public static int subBinary(boolean[] binary, int begin, int length) {
        int sum = 0;
        int i = 0;
        while(i < length) {
            sum = sum << 1;
            if(binary[begin + i]) {
                sum += 1;
            }
            i++;
        }
        return sum;
    }


    /**
     * 把一个数字右移cnt位后到字节表达
     * 如：3右移2位后为：11000000，如：1右移8位后是：000000001
     * @param value
     * @param cnt
     * @return
     */
    public static byte valueRightShift(int value, int cnt) {
        byte bit4 = (byte) 0x00;
        for(int i = 0; i < cnt; i++ ) {
            int mod = value % 2;
            bit4 = (byte) (bit4 >> 1);
            if(mod == 1) {
                bit4 = (byte) (bit4 | 0x80);
            } else {
                bit4 = (byte) (bit4 & 0x7f);
            }
            value /= 2;
        }
        return bit4;
    }



}
