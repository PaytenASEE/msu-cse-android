package com.payten.msu.cse;

/**
 * Created by jasmin.suljic@monri.com
 * MSU CSE
 */
class EncryptTaskResult {
    private String encrypted;
    private EncryptException encryptException;

    EncryptTaskResult(String encrypted, EncryptException encryptException) {
        this.encrypted = encrypted;
        this.encryptException = encryptException;
    }

    public static EncryptTaskResult success(String encrypted) {
        return new EncryptTaskResult(encrypted, null);
    }

    public static EncryptTaskResult failed(EncryptException throwable) {
        return new EncryptTaskResult(null, throwable);
    }

    public static EncryptTaskResult failed(Throwable cause, EncryptExceptionCode code) {
        return new EncryptTaskResult(null, EncryptException.create(cause, code));
    }

    public static EncryptTaskResult failed(EncryptExceptionCode code) {
        return new EncryptTaskResult(null, EncryptException.create(code));
    }

    public String getEncrypted() {
        return encrypted;
    }

    public EncryptException getEncryptException() {
        return encryptException;
    }
}
