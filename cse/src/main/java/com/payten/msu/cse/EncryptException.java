package com.payten.msu.cse;

/**
 * Created by jasmin.suljic@monri.com
 * MSU CSE
 */
public class EncryptException extends RuntimeException {

    private final EncryptExceptionCode code;

    private EncryptException(String message, EncryptExceptionCode code) {
        super(message);
        this.code = code;
    }

    private EncryptException(Throwable cause, EncryptExceptionCode code) {
        super(cause);
        this.code = code;
    }

    public EncryptExceptionCode getCode() {
        return code;
    }

    static EncryptException create(EncryptExceptionCode code) {
        return new EncryptException(code.toString(), code);
    }

    static EncryptException create(String message, EncryptExceptionCode code) {
        return new EncryptException(message, code);
    }

    static EncryptException create(Throwable cause, EncryptExceptionCode code) {
        return new EncryptException(cause, code);
    }

}
