package com.robberte.learning.tool.binary;


import com.robberte.learning.tool.util.ByteUtil;
import com.robberte.learning.tool.util.ServerLogger;

/**
 * 二进制消息头处理类
 * @author robberte
 * @date 2018/7/22 下午11:27
 */
public class MessageHeader {

    private int  protocolId = 3;
    private int  protocolVersion = 1;
    private boolean isEncryted = false;
    private boolean isZiped = false;
    private int encrytedType = EncryptedType.NONE.value();

    public MessageHeader(int encrytedType) {
        this.encrytedType = encrytedType;
    }

    public MessageHeader(byte[] header) {
        protocolId = ByteUtil.subBinary(header, 0, 2);
        protocolVersion = ByteUtil.subBinary(header, 2, 6);
        isEncryted = !(ByteUtil.subBinary(header, 8, 1) == 0);
        isZiped = !(ByteUtil.subBinary(header, 9, 1) == 0);
        encrytedType = ByteUtil.subBinary(header, 10, 2);
        if(ServerLogger.isDebugEnable()) {
            ServerLogger.debug("parse message header {protocolId:{}, ver:{}, isEnc:{}, isZip:{}, encType:{}}",
                    protocolId, protocolVersion, isEncryted, isZiped, encrytedType);
        }
    }

    @Override
    public String toString() {
        return String.format("message header {protocolId:%d, ver:%d, isEnc:%s, isZip:%s, encType:%d}",
                protocolId, protocolVersion, isEncryted, isZiped, encrytedType);
    }

    public byte[] toBytes() {
        // 0~1位：协议标识，固定为"11"；
        // 2~7位：协议版本号，第一版位："000000"，以后按二进制数值递增
        // 第8位：加密标记位，"0"表示不加密，"1"表示加密
        // 第9位：压缩标记位，"0"表示不压缩，"1"表示压缩
        // 10~11位：加密算法标记位，0位KK M9加密，1为SDK M9加密
        // 12~15位：保留扩展，全部填0

        byte protocolIdByte = ByteUtil.valueRightShift(protocolId, 2);
        byte protocalVersionByte = ByteUtil.valueRightShift(protocolVersion, 8);
        byte byte1 = (byte) (protocolIdByte | protocalVersionByte);

        byte isEncryptedByte = ByteUtil.valueRightShift(isEncryted ? 1 : 0, 1);
        byte isZipedByte = ByteUtil.valueRightShift(isZiped ? 1 : 0, 2);
        byte encryptedTypeByte = ByteUtil.valueRightShift(encrytedType, 4);
        byte unUsedByte = ByteUtil.valueRightShift(0, 8);
        byte byte2 = (byte) (isEncryptedByte | isZipedByte | encryptedTypeByte | unUsedByte);
        return new byte[]{byte1, byte2};
    }

    public int getProtocolId() {
        return protocolId;
    }
    public void setProtocolId(int protocolId) {
        this.protocolId = protocolId;
    }
    public int getProtocolVersion() {
        return protocolVersion;
    }
    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }
    public boolean isEncryted() {
        return isEncryted;
    }
    public void setEncryted(boolean encryted) {
        isEncryted = encryted;
    }
    public boolean isZiped() {
        return isZiped;
    }
    public void setZiped(boolean ziped) {
        isZiped = ziped;
    }
    public int getEncrytedType() {
        return encrytedType;
    }
    public void setEncrytedType(int encrytedType) {
        this.encrytedType = encrytedType;
    }

    public static void  main(String[] args) {
        MessageHeader messageHeader = new MessageHeader(2);
        messageHeader.setProtocolId(3);
        messageHeader.setProtocolVersion(1);
        messageHeader.setEncryted(true);
        messageHeader.setZiped(true);
        byte[] bytes = messageHeader.toBytes();

        System.out.println(bytes);

        messageHeader = new MessageHeader(bytes);
        System.out.println(messageHeader.getProtocolId());
        System.out.println(messageHeader.getProtocolVersion());
        System.out.println(messageHeader.isEncryted());
        System.out.println(messageHeader.isZiped());
        System.out.println(messageHeader.getEncrytedType());
    }
}
