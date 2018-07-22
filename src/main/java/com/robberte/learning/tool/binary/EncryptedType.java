package com.robberte.learning.tool.binary;

public enum EncryptedType {

    NONE(0), IM(1);

    private int value;

    private EncryptedType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
